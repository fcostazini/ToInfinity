package com.studios.thinkup.negativo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
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
        Button b = (Button)findViewById(R.id.btn_nueva_partida);
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
        ArrayList<Integer> valores;
        findViewById(R.id.ly_fin).setVisibility(View.GONE);
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
                if (!isAnimating && leftTimeInMilliseconds <= 57000) {
                    finTiempo();
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
        int[] location = new int[2];

        View lyEndGame = findViewById(R.id.ly_end_game);
        lyEndGame.getLocationOnScreen(location);
        lyEndGame.setVisibility(View.VISIBLE);
        findViewById(R.id.ly_timer).setVisibility(View.GONE);
        findViewById(R.id.ly_numeros).setVisibility(View.GONE);
        View score = findViewById(R.id.ly_score);
        score.setY((lyEndGame.getHeight() - score.getPaddingBottom() - score.getHeight() )/2);
        score.setX((lyEndGame.getWidth() + score.getPaddingLeft() - score.getWidth())/2);

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
}
