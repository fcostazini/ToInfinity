package com.studios.thinkup.negativo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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


public class MainActivity extends AppCompatActivity implements ISelectableHandler {

    private HorizontalScrollView hs;
    private Integer startNum = 8;
    private LinearLayout valoresLy;
    private InterstitialAd mInterstitialAd;
    float[] alphas = {0.20f, 0.17f, 0.14f, 0.11f, 0.07f, 0.04f, 0.01f};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        setContentView(R.layout.activity_main);


        TextView back = (TextView) findViewById(R.id.background);
        Animation marq = AnimationUtils.loadAnimation(this, R.anim.marq_izq);
        AnimationSet as = new AnimationSet(true);
        as.setFillAfter(true);
        as.addAnimation(marq);
        back.startAnimation(as);
        back.setVisibility(View.VISIBLE);
        valoresLy = (LinearLayout) findViewById(R.id.ly_numeros);
        hs = (HorizontalScrollView) findViewById(R.id.scroll);
        valoresLy.setOnTouchListener(new TouchHandler(valoresLy, this, 2, 9999));
        ArrayList<Integer> valores = generarValores(startNum);
        findViewById(R.id.ly_fin).setVisibility(View.GONE);
        findViewById(R.id.scroll).setVisibility(View.VISIBLE);

        NumeroText t = null;
        for (Integer i : valores) {
            valoresLy.addView(getNuevoNumero(i));
        }
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("sony-d6503-BH91CE6Q16")
                .build();

        mInterstitialAd.loadAd(adRequest);
        deseleccionarValores();
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

    private String getInfiniteNumber() {
        String s = "";
        NumeroText n = null;
        for (int i = 0; i < valoresLy.getChildCount(); i++) {
            n = (NumeroText) valoresLy.getChildAt(i);
            s += String.valueOf(n.getValue()) + " ";
        }

        for (int i = 0; i < 10; i++) {
            s = s + s;
        }
        return s;

    }

    private void deseleccionarValores() {

        NumeroText n;
        for (int i = 0; i < valoresLy.getChildCount(); i++) {
            n = (NumeroText) valoresLy.getChildAt(i);
            n.setTextColor(getResources().getColor(android.R.color.black));
            n.setShadowLayer(0, 0, 0, getResources().getColor(android.R.color.black));
        }

    }


    private boolean checkFinDeJuego() {
        if (valoresLy.getChildCount() == 2) {
            return ((NumeroText) valoresLy.getChildAt(0)).getValue()
                    == ((NumeroText) valoresLy.getChildAt(1)).getValue();
        }
        return valoresLy.getChildCount() == 1;

    }

    private boolean simplificar(List<NumeroText> seleccionados) {
        if (seleccionados.size() < 3) {
            return false;
        } else {
            int i = seleccionados.get(0).getValue();
            for (NumeroText n : seleccionados) {
                if (n.getValue() != i) {
                    return false;
                }
            }
            int index = 0;
            for (NumeroText n : seleccionados) {

                if (index != 0) {
                    n.startAnimation(createConsumeAnimation(index, seleccionados.get(0), n));
                }

                index++;
            }

            return true;
        }


    }

    private Animation createConsumeAnimation(float distance, NumeroText to, final NumeroText from) {

        AnimationSet as = new AnimationSet(true);

        if (valoresLy.indexOfChild(to) < valoresLy.indexOfChild(from)) {
            distance = distance * -1;
        }
        Animation rotar = new RotateAnimation(0f, 360f * 2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Animation achicar = new ScaleAnimation(1.15f, 0, 1.15f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Animation desplazar = new TranslateAnimation(0f, 150f * distance, 0, 0);

        rotar.setInterpolator(new AccelerateDecelerateInterpolator());
        rotar.setDuration(300);
        rotar.setFillAfter(true);

        achicar.setInterpolator(new BounceInterpolator());
        achicar.setDuration(400);
        achicar.setFillAfter(true);

        desplazar.setInterpolator(new BounceInterpolator());
        desplazar.setDuration(300);
        desplazar.setFillAfter(true);

        as.addAnimation(rotar);
        as.addAnimation(achicar);
        as.addAnimation(desplazar);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                valoresLy.post(new Runnable() {
                    public void run() {
                        valoresLy.removeView(from);
                        finJuego();

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return as;

    }

    private void mostrarFondoInfinito(int progress) {
        final TextView t = (TextView) findViewById(R.id.background);
        float to = 0;
        if (progress <= 7) {
            t.setText(getInfiniteNumber());
            to = alphas[progress - 1];
        }

        t.setAlpha(to);


    }

    private void combinar(final NumeroText n1, NumeroText n2) {

        if ((getNuevoNumero(n1.getValue() + 1)).getValue() == n2.getValue()) {
            final int posn1 = valoresLy.indexOfChild(n1);
            final int posn2 = valoresLy.indexOfChild(n2);
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

            valoresLy.removeViewAt(posn1);
            valoresLy.addView(getNuevoNumero(n1.getValue() - 1), posn1);
            valoresLy.removeViewAt(posn2);
            finJuego();


        }

    }

    @Override
    public void selectedClick(List<NumeroText> selected, boolean afterLongClick) {
        if (selected.size() == 1) {
            romper(selected);
        } else if (selected.size() > 1) {
            if (afterLongClick) {
                simplificar(selected);
            } else {
                combinar(selected.get(0), selected.get(1));
            }
        }
        deseleccionarValores();
    }

    private void romper(List<NumeroText> selected) {
        int pos = valoresLy.indexOfChild(selected.get(0));
        int value = (selected.get(0)).getValue();
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.expand_right);
        NumeroText n1 = getNuevoNumero(value + 1);
        n1.startAnimation(myFadeInAnimation);
        NumeroText n2 = getNuevoNumero(value + 2);
        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.expand_left);
        n2.startAnimation(myFadeInAnimation);
        valoresLy.removeViewAt(pos);
        valoresLy.addView(n1, pos);
        valoresLy.addView(n2, pos + 1);
        finJuego();

    }

    private void finJuego() {
        mostrarFondoInfinito(valoresLy.getChildCount());
        if (checkFinDeJuego()) {
            hs.setVisibility(View.GONE);
            findViewById(R.id.ly_fin).setVisibility(View.VISIBLE);
        }
    }

}
