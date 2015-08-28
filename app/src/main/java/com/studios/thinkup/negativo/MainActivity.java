package com.studios.thinkup.negativo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.studios.thinkup.negativo.components.NumeroText;
import com.studios.thinkup.negativo.components.handler.ISelectableHandler;
import com.studios.thinkup.negativo.components.handler.TouchHandler;
import com.studios.thinkup.negativo.tutoriales.TutorialCombinar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity implements ISelectableHandler {

    private HorizontalScrollView hs;
    private ArrayList<NumeroText> valoresTxt;
    private boolean esInfinito;
    private Integer startNum = 8;
    private float x1 = 0;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));

        setContentView(R.layout.activity_main);
        TextView tBack = (TextView) findViewById(R.id.background);
        tBack.setVisibility(View.GONE);
        valoresTxt = new ArrayList<>();
        final LinearLayout valoresLy = (LinearLayout) findViewById(R.id.ly_numeros);
        hs = (HorizontalScrollView) findViewById(R.id.scroll);
        valoresLy.setOnTouchListener(new TouchHandler(valoresLy, this, 2, 9999));
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
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("sony-d6503-BH91CE6Q16")
                .build();

        mInterstitialAd.loadAd(adRequest);
        updateValores();
    }

    private void reset() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            restartActivity();
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                restartActivity();
            }
        });

    }

    private void restartActivity() {
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

        switch (id) {
            case R.id.action_reset:
                reset();
                return true;
            case R.id.action_help:
                help();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void help() {
        Intent intent = new Intent(this, TutorialCombinar.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

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

        int posPosterior = valoresTxt.indexOf(seleccionados.get(seleccionados.size() - 1)) + 1;
        int posAnterior = valoresTxt.indexOf(seleccionados.get(0)) - 1;
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


    private void combinar(final NumeroText n1, NumeroText n2) {

        if ((getNuevoNumero(n1.getValue() + 1)).getValue() == n2.getValue()) {
            final int posn1 = valoresTxt.indexOf(n1);
            final int posn2 = valoresTxt.indexOf(n2);
            if (posn1 > posn2) {
                Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.collapse_left);
                n1.startAnimation(myFadeInAnimation);
                myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.collapse_right);
                n2.startAnimation(myFadeInAnimation);
            } else {
                Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.collapse_right);
                n1.startAnimation(myFadeInAnimation);
                myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.collapse_left);
                n2.startAnimation(myFadeInAnimation);
            }

            valoresTxt.set(posn1, getNuevoNumero(n1.getValue() - 1));
            valoresTxt.remove(posn2);


        }

    }

    @Override
    public void selectedClick(List<NumeroText> selected, boolean afterLongClick) {
        if (selected.size() == 1) {
            romper(selected);
        } else if (selected.size() > 1) {
            if (afterLongClick) {
                toInfinity(selected);
            } else {
                combinar(selected.get(0), selected.get(1));
            }
        }
        updateValores();
    }

    private void romper(List<NumeroText> selected) {
        int pos = valoresTxt.indexOf(selected.get(0));
        int value = (selected.get(0)).getValue();
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.expand_right);
        NumeroText n1 = getNuevoNumero(value + 1);
        n1.startAnimation(myFadeInAnimation);
        NumeroText n2 = getNuevoNumero(value + 2);
        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.expand_left);
        n2.startAnimation(myFadeInAnimation);
        valoresTxt.set(pos, n1);
        valoresTxt.add(pos + 1, n2);
    }

}
