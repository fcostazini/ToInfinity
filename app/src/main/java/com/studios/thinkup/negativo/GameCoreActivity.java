package com.studios.thinkup.negativo;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.studios.thinkup.negativo.components.NumeroText;
import com.studios.thinkup.negativo.components.handler.ISelectableHandler;
import com.studios.thinkup.negativo.tutoriales.TutorialCombinar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public abstract class GameCoreActivity extends AppCompatActivity implements ISelectableHandler {

    protected HorizontalScrollView hs;

    protected LinearLayout valoresLy;
    protected InterstitialAd mInterstitialAd;
    float[] alphas = {0.20f, 0.17f, 0.14f, 0.11f, 0.07f, 0.04f, 0.01f};


    protected void reset() {

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

    protected void restartActivity() {
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected NumeroText getNuevoNumero(Integer valor) {
        NumeroText t = new NumeroText(this);
        t.setText(String.valueOf(getNuevoValor(valor)));
        return t;
    }

    protected Integer getNuevoValor(Integer valor) {
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


    protected ArrayList<Integer> generarValores(Integer startNum) {
        ArrayList<Integer> valores = new ArrayList<>();
        Random r = new Random(SystemClock.currentThreadTimeMillis());

        for (int i = 0; i < startNum; i++) {
            valores.add(r.nextInt(6) + 1);
        }

        return valores;
    }

    protected void help() {
        Intent intent = new Intent(this, TutorialCombinar.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    protected String getInfiniteNumber() {
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

    protected void deseleccionarValores() {

        NumeroText n;
        for (int i = 0; i < valoresLy.getChildCount(); i++) {
            n = (NumeroText) valoresLy.getChildAt(i);
            n.setTextColor(getResources().getColor(android.R.color.black));
            n.setShadowLayer(0, 0, 0, getResources().getColor(android.R.color.black));
        }

    }


    protected boolean checkFinDeJuego() {
        if (valoresLy.getChildCount() == 2) {
            return ((NumeroText) valoresLy.getChildAt(0)).getValue()
                    == ((NumeroText) valoresLy.getChildAt(1)).getValue();
        }
        return valoresLy.getChildCount() == 1;

    }

    protected boolean simplificar(List<NumeroText> seleccionados) {
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

    protected Animation createConsumeAnimation(float distance, NumeroText to, final NumeroText from) {

        AnimationSet as = new AnimationSet(true);

        if (valoresLy.indexOfChild(to) < valoresLy.indexOfChild(from)) {
            distance = distance * -1;
        }

        Animation achicar = new ScaleAnimation(1.15f, 0, 1.15f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Animation desplazar = new TranslateAnimation(0f, 150f * distance, 0, 0);


        achicar.setInterpolator(new BounceInterpolator());
        achicar.setDuration(100);
        achicar.setFillAfter(true);

        desplazar.setInterpolator(new BounceInterpolator());
        desplazar.setDuration(150);
        desplazar.setFillAfter(true);

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

    protected void mostrarFondoInfinito(int progress) {
        final TextView t = (TextView) findViewById(R.id.background);
        float to = 0;
        if (progress <= 7) {
            t.setText(getInfiniteNumber());
            to = alphas[progress - 1];
        }

        t.setAlpha(to);


    }

    protected void combinar(final NumeroText n1, NumeroText n2) {
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
    public void selectedClick(List<NumeroText> selected) {
        if (selected.size() >= 1) {
            if (selected.size() == 1) {
                romper(selected);
            } else if (selected.size() == 2) {
                combinar(selected.get(0), selected.get(1));
            } else {
                simplificar(selected);
            }
            deseleccionarValores();
        }


    }

    protected void romper(List<NumeroText> selected) {
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

    protected void finJuego() {
        valoresLy.invalidate();
        valoresLy.requestLayout();
        mostrarFondoInfinito(valoresLy.getChildCount());
        if (checkFinDeJuego()) {
            Animation alpha = new AlphaAnimation(1, 0);
            alpha.setFillAfter(true);
            alpha.setDuration(500);
            alpha.setInterpolator(new AccelerateDecelerateInterpolator());
            hs.startAnimation(alpha);

            LinearLayout ly = (LinearLayout) findViewById(R.id.ly_fin);
            ly.setVisibility(View.VISIBLE);

            String mensajeWin = this.getResources().getString(R.string.winner);
            TextView t;
            Animation a = null;
            ly.removeAllViews();
            for (int i = 0; i < mensajeWin.length(); i++) {
                t = createLetter(mensajeWin.substring(i, i + 1));
                a = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
                a.setStartOffset(200 * i);
                t.startAnimation(a);
                ly.addView(t);
            }
            ly.refreshDrawableState();

        }
    }

    protected TextView createLetter(String letter) {
        TextView t = new TextView(this);
        t.setText(letter);
        t.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.number_font));
        t.setGravity(Gravity.CENTER);
        t.setIncludeFontPadding(false);
        t.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        t.setShadowLayer(21, 0, 0, getResources().getColor(android.R.color.holo_green_light));
        t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return t;
    }

}
