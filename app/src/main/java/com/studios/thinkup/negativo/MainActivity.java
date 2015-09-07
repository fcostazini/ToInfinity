package com.studios.thinkup.negativo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.studios.thinkup.negativo.components.NumeroText;
import com.studios.thinkup.negativo.components.handler.TouchHandler;

import java.util.ArrayList;


public class MainActivity extends GameCoreActivity {
    float[] alphas = {0.20f, 0.17f, 0.14f, 0.11f, 0.07f, 0.04f, 0.01f};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_home);
        getSupportActionBar().setTitle("");
        setContentView(R.layout.activity_main);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));


        TextView back = (TextView) findViewById(R.id.background);
        Animation marq = AnimationUtils.loadAnimation(this, R.anim.marq_izq);
        AnimationSet as = new AnimationSet(true);
        as.setFillAfter(true);
        as.addAnimation(marq);
        back.startAnimation(as);
        back.setVisibility(View.VISIBLE);
        valoresLy = (LinearLayout) findViewById(R.id.ly_numeros);
        hs = (HorizontalScrollView) findViewById(R.id.scroll);
        valoresLy.setOnTouchListener(new TouchHandler(valoresLy, this));
        ArrayList<Integer> valores;
        findViewById(R.id.ly_fin).setVisibility(View.GONE);
        findViewById(R.id.scroll).setVisibility(View.VISIBLE);
        valores = generarValores(8);
        NumeroText t = null;
        for (Integer i : valores) {
            valoresLy.addView(getNuevoNumero(i));
        }
        valoresLy.refreshDrawableState();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("sony-d6503-BH91CE6Q16")
                .build();

        mInterstitialAd.loadAd(adRequest);
        deseleccionarValores();

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


}
