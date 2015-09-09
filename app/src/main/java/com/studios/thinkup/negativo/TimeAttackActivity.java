package com.studios.thinkup.negativo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.studios.thinkup.negativo.components.handler.TouchHandler;

import java.util.ArrayList;
import java.util.Random;


public class TimeAttackActivity extends GameCoreActivity {
    float[] alphas = {0.20f, 0.17f, 0.14f, 0.11f, 0.07f, 0.04f, 0.01f};
    CountDownTimer countDownTimer;
    ProgressBar barTimer;
    TextView txtTimer;
    TextView txtScore;
    Random rand;
    int score;
    boolean isAnimating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_home);
        getSupportActionBar().setTitle("");
        setContentView(R.layout.time_attack_activity);
        Button b = (Button) findViewById(R.id.btn_nueva_partida);
        findViewById(R.id.ly_end_game).setVisibility(View.INVISIBLE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        isAnimating = false;
        score = 0;

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));

        txtScore = (TextView) findViewById(R.id.txt_score);
        txtScore.setText(String.valueOf(score));
        valoresLy = (LinearLayout) findViewById(R.id.ly_numeros);
        hs = (HorizontalScrollView) findViewById(R.id.scroll);
        valoresLy.setOnTouchListener(new TouchHandler(valoresLy, this));
        valoresLy.setEnabled(true);

        findViewById(R.id.ly_fin).setVisibility(View.GONE);
        findViewById(R.id.ly_end_game).setVisibility(View.GONE);
        findViewById(R.id.scroll).setVisibility(View.VISIBLE);


        barTimer = (ProgressBar) findViewById(R.id.barTimer);
        Animation r = new RotateAnimation(0.5f, 0.5f, 0, 90);
        r.setDuration(100);
        barTimer.startAnimation(r);
        txtTimer = (TextView) findViewById(R.id.txt_timer);
        findViewById(R.id.ly_timer).setVisibility(View.VISIBLE);
        resetNumber();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("sony-d6503-BH91CE6Q16")
                .build();

        mInterstitialAd.loadAd(adRequest);
        deseleccionarValores();
        rand = new Random(System.currentTimeMillis());

        startTimer();

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(60 * 1000, 100) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 10;

                barTimer.setProgress((int) seconds);
                txtTimer.setText(String.valueOf((int) (seconds / 100)));
                if (!isAnimating && leftTimeInMilliseconds <= 10000) {

                    Animation a = AnimationUtils.loadAnimation(TimeAttackActivity.this, R.anim.pulse);
                    txtTimer.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    txtTimer.setShadowLayer(21, 0, 0, getResources().getColor(android.R.color.holo_red_dark));
                    txtTimer.startAnimation(a);
                    isAnimating = true;
                }

            }

            @Override
            public void onFinish() {
                finTiempo();
            }
        }.start();

    }

    private void finTiempo() {
        View score = findViewById(R.id.ly_score);
        valoresLy.setEnabled(false);
        AnimationSet as = new AnimationSet(false);
        as.setFillAfter(true);

        Animation alpha = new AlphaAnimation(1f, 0f);
        alpha.setDuration(500);
        alpha.setInterpolator(new AccelerateInterpolator());
        alpha.setFillAfter(true);

        findViewById(R.id.ly_timer).startAnimation(alpha);
        valoresLy.startAnimation(alpha);


        Animation move = getMoveToCenterAnimation(score);
        move.setStartOffset(400);
        move.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                View lyEndGame = findViewById(R.id.ly_end_game);

                lyEndGame.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        as.addAnimation(move);
        score.startAnimation(as);


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

    private String getNumberText(int num) {
        if (num < 10) {
            return "0" + String.valueOf(num);
        } else {
            return String.valueOf(num);
        }
    }

    @Override
    protected void finJuego() {
        //mostrarFondoInfinito(valoresLy.getChildCount());

        if (checkFinDeJuego()) {
            /*Animation alpha = new AlphaAnimation(1, 0);
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
            ly.refreshDrawableState();*/
            score++;
            txtScore.setText(String.valueOf(score));
            resetNumber();

        }
    }

    private void resetNumber() {
        ArrayList<Integer> valores = generarValores(8);
        valoresLy.removeAllViews();
        for (Integer i : valores) {
            valoresLy.addView(getNuevoNumero(i));
        }
        valoresLy.refreshDrawableState();
    }

    private Animation getMoveToCenterAnimation(View view) {

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);


        int originalPos[] = new int[2];
        view.getLocationOnScreen(originalPos);

        int xDest = dm.widthPixels / 2;
        xDest -= (view.getMeasuredWidth() / 2);
        int yDest = dm.heightPixels / 2 - (view.getMeasuredHeight() / 2);

        TranslateAnimation anim = new TranslateAnimation(0, xDest - originalPos[0], 0, yDest - originalPos[1]);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        return anim;
    }
}
