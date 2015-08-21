package com.studios.thinkup.negativo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studios.thinkup.negativo.components.NumeroText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class MainActivity extends Activity implements View.OnTouchListener, View.OnLongClickListener {

    private HorizontalScrollView hs;
    private ArrayList<NumeroText> valoresTxt;
    private List<NumeroText> seleccionados;
    private boolean toInfinity;
    private boolean esInfinito;
    private Integer startNum = 8;
    private float x1 = 0;
    private long startClickTime;
    private static final int MAX_CLICK_DURATION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seleccionados = new Vector<>();
        TextView tBack = (TextView) findViewById(R.id.background);
        tBack.setVisibility(View.GONE);
        valoresTxt = new ArrayList<>();
        final LinearLayout valoresLy = (LinearLayout) findViewById(R.id.ly_numeros);
        hs = (HorizontalScrollView) findViewById(R.id.scroll);
        valoresLy.setOnTouchListener(this);
        ArrayList<Integer> valores = generarValores(startNum);
        findViewById(R.id.ly_fin).setVisibility(View.GONE);
        findViewById(R.id.scroll).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_rejugar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        NumeroText t = null;
        for (Integer i : valores) {
            t = getNuevoNumero(i);
            valoresTxt.add(t);
        }
        updateValores();

    }

    private void reset() {
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private NumeroText getNuevoNumero(Integer valor) {
        NumeroText t = new NumeroText(this);
        t.setText(String.valueOf(getNuevoValor(valor)));
        //t.setOnLongClickListener(this);

        //t.setOnTouchListener(this);
        return t;
    }

    private Integer getNuevoValor(Integer valor) {
        int val = valor;
        if (valor > NumeroText.MAX_VAL) {
            val = valor - NumeroText.MAX_VAL - 1;
        } else if (valor < NumeroText.MIN_VAL) {
            val = NumeroText.MAX_VAL;
        } else {
            return val;
        }
        return val;
    }

    private ArrayList<Integer> generarValores(Integer startNum) {
        ArrayList<Integer> valores = new ArrayList<>();
        Random r = new Random(SystemClock.currentThreadTimeMillis());

        for (int i = 0; i < startNum; i++) {
            valores.add(r.nextInt(6) + 1);
        }

        return valores;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getInfiniteNumber(List<NumeroText> numeros) {
        String s = "";
        for (NumeroText n : numeros) {
            s += String.valueOf(n.getValue()) + " ";
        }

        for (int i = 0; i < 10; i++) {
            s = s + s;
        }
        return s;

    }

    private void updateValores() {
        LinearLayout valoresLy = (LinearLayout) findViewById(R.id.ly_numeros);
        int c;
        if (esInfinito) {
            c = getResources().getColor(android.R.color.holo_blue_dark);
            mostrarFondoInfinito(valoresTxt);
        } else {
            c = getResources().getColor(android.R.color.black);
        }
        valoresLy.removeAllViews();

        for (NumeroText n : valoresTxt) {
            if (n.getParent() != null) {
                ((ViewGroup) n.getParent()).removeView(n);
            }
            n.setTextColor(c);
            n.setShadowLayer(0, 0, 0, c);
            valoresLy.addView(n);
        }

        checkFinDeJuego();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // TODO Auto-generated method stub

        NumeroText n = (NumeroText) findViewAtPosition(view, (int) motionEvent.getRawX(), (int) motionEvent.getRawY());

        if (toInfinity) {
            return handlerToInfinityTouch(n, motionEvent);

        } else {
            return handleTouch(n, motionEvent);
        }

    }

    private boolean handleTouch(NumeroText n, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startClickTime = SystemClock.currentThreadTimeMillis();
                x1 = motionEvent.getX();
                return true;
            }
            case MotionEvent.ACTION_UP: {

                long clickDuration = SystemClock.currentThreadTimeMillis() - startClickTime;
                float deltaX = motionEvent.getX() - x1;

                if (esInfinito && !toInfinity && Math.abs(deltaX) > 200) {
                    rotar(deltaX);
                    seleccionados.clear();
                    updateValores();

                    return true;

                } else if (clickDuration < MAX_CLICK_DURATION && Math.abs(deltaX) < 100) {
                    int pos = valoresTxt.indexOf(n);
                    int value = n.getValue();

                    valoresTxt.set(pos, getNuevoNumero(value + 1));
                    valoresTxt.add(pos + 1, getNuevoNumero(value + 2));

                } else {
                    if (seleccionados.size() == 2) {

                        combinar(seleccionados.get(0), seleccionados.get(1));

                    }
                }
                toInfinity = false;
                seleccionados.clear();
                updateValores();
                return true;
            }
            case MotionEvent.ACTION_CANCEL: {
                if (seleccionados.size() == 2) {

                    combinar(seleccionados.get(0), seleccionados.get(1));

                }
                hs.requestDisallowInterceptTouchEvent(false);
                seleccionados.clear();
                toInfinity = false;
                updateValores();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                float deltaX = motionEvent.getX() - x1;
                if (deltaX < 100 && SystemClock.currentThreadTimeMillis() - startClickTime > 35) {
                    toInfinity = true;
                    seleccionarNumero(n);
                }
                hs.requestDisallowInterceptTouchEvent(true);

                if (seleccionados.size() < 2) {
                    seleccionarNumero(n);
                }


                return true;
            }


        }
        seleccionados.clear();
        updateValores();

        return true;
    }

    private void rotar(float deltaX) {
        NumeroText n = null;

        if (deltaX > 0) {
            n = valoresTxt.remove(valoresTxt.size() - 1);
            valoresTxt.add(0, n);
        } else {

            n = valoresTxt.remove(0);
            valoresTxt.add(n);
        }

    }

    private void checkFinDeJuego() {
        Integer val = null;
        boolean iguales = true;
        for (NumeroText n : valoresTxt) {
            if (val == null)
                val = n.getValue();
            if (!val.equals(n.getValue())) {
                iguales = false;
                break;
            }
        }
        if (iguales) {
            mostrarFondoInfinito(valoresTxt);
            findViewById(R.id.ly_fin).setVisibility(View.VISIBLE);
            findViewById(R.id.scroll).setVisibility(View.GONE);
        }

    }

    private boolean handlerToInfinityTouch(NumeroText n, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                if (seleccionados.size() >= 2) {
                    toInfinity(seleccionados);
                }
                toInfinity = false;
                seleccionados.clear();
                updateValores();
                return true;

            }
            case MotionEvent.ACTION_CANCEL: {
                hs.requestDisallowInterceptTouchEvent(false);
                toInfinity = false;
                seleccionados.clear();
                updateValores();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                hs.requestDisallowInterceptTouchEvent(true);
                seleccionarNumero(n);
                return true;
            }


        }
        seleccionados.clear();
        updateValores();
        return true;
    }

    private void toInfinity(List<NumeroText> seleccionados) {
        if (esInfinito) {
            return;
        }
        this.esInfinito = esInfinito(seleccionados);
        if (esInfinito) {
            mostrarFondoInfinito(seleccionados);
        }
        if (esInfinito) {
            valoresTxt.clear();
            for (NumeroText n : seleccionados) {
                valoresTxt.add(n);
            }
        }

    }

    private void mostrarFondoInfinito(List<NumeroText> seleccionados) {
        TextView t = (TextView) findViewById(R.id.background);
        t.setVisibility(View.VISIBLE);
        Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.marq_izq);
        t.startAnimation(myAnimation);
        t.setText(getInfiniteNumber(seleccionados));

    }

    private boolean esInfinito(List<NumeroText> seleccionados) {

        int posPosterior = valoresTxt.indexOf(seleccionados.get(seleccionados.size()-1)) + 1;
        int posAnterior =  valoresTxt.indexOf(seleccionados.get(0)) -1;
        int index = 0;
        for (int i = posPosterior; i < valoresTxt.size(); i++) {
            if (index > (seleccionados.size() - 1)) {
                index = 0;
            }
            if (seleccionados.get(index).getValue() != valoresTxt.get(i).getValue()) {
                return false;
            }
            index++;
        }
        index = seleccionados.size() - 1;
        for (int i = posAnterior; i >= 0; i--) {
            if (index < 0) {
                index = seleccionados.size() - 1;
            }
            if (seleccionados.get(index).getValue() != valoresTxt.get(i).getValue()) {
                return false;
            }
            index--;
        }

        return true;
    }

    private void seleccionarNumero(NumeroText n) {
        int c;
        if (n != null && !seleccionados.contains(n)) {
            if (toInfinity && !esInfinito) {
                c = getResources().getColor(android.R.color.holo_blue_dark);
            } else {
                c = getResources().getColor(android.R.color.holo_red_dark);
            }


            n.setTextColor(c);
            n.setShadowLayer((float) 20, 0, 0, c);
            seleccionados.add(n);
        }
    }


    private void combinar(NumeroText n1, NumeroText n2) {

        if ((getNuevoNumero(n1.getValue() + 1)).getValue() == n2.getValue()) {
            int posn1 = valoresTxt.indexOf(n1);
            int posn2 = valoresTxt.indexOf(n2);
            valoresTxt.set(posn1, getNuevoNumero(n1.getValue() - 1));
            valoresTxt.remove(posn2);
        }
    }

    private View findViewAtPosition(View parent, int x, int y) {
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                View viewAtPosition = findViewAtPosition(child, x, y);
                if (viewAtPosition != null) {
                    return viewAtPosition;
                }
            }
            return null;
        } else {
            Rect rect = new Rect();
            parent.getGlobalVisibleRect(rect);
            if (rect.contains(x, y)) {
                return parent;
            } else {
                return null;
            }
        }
    }


    @Override
    public boolean onLongClick(View view) {
        this.toInfinity = true;
        return false;
    }
}
