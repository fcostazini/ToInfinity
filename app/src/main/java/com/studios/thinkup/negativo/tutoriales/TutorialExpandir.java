package com.studios.thinkup.negativo.tutoriales;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studios.thinkup.negativo.R;

public class TutorialExpandir extends Activity {
    Animation transition;
    AnimationSet as;
    TextView num2;
    TextView num1;
    TextView num3;
    ImageView hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_expandir);
        hand = (ImageView) findViewById(R.id.hand);
        num1 = (TextView) findViewById(R.id.view1);
        num2 = (TextView) findViewById(R.id.view2);
        num3 = (TextView) findViewById(R.id.textView3);

        Button siguiente = (Button) findViewById(R.id.btn_siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorialExpandir.this, TutorialSimplificar.class);
                startActivity(i);
            }
        });
        Button anterior = (Button) findViewById(R.id.btn_anterior);
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorialExpandir.this, TutorialCombinar.class);
                startActivity(i);
            }
        });

        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        final ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Put your code here.
                int[] location = new int[2];
                num3.getLocationOnScreen(location);
                hand.setX(location[0] + num3.getPaddingLeft() / 2 + num3.getWidth() / 2);
                hand.setY(location[1]);

                as = new AnimationSet(true);
                as.setFillAfter(true);
                Animation alpha = new AlphaAnimation(0, 1);
                as.addAnimation(alpha);
                alpha.setStartOffset(100);
                alpha.setDuration(800);
                alpha.setInterpolator(new LinearInterpolator());
                alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        num1.setVisibility(View.INVISIBLE);
                        num2.setVisibility(View.INVISIBLE);
                        num3.setVisibility(View.VISIBLE);
                        hand.setImageResource(R.drawable.point);
                        hand.setVisibility(View.VISIBLE);

                        int c = TutorialExpandir.this.getResources().getColor(android.R.color.black);
                        num3.setTextColor(c);
                        num3.setShadowLayer(1, 0, 0, c);


                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        hand.setImageResource(R.drawable.point_hold);
                        int c = TutorialExpandir.this.getResources().getColor(android.R.color.holo_green_dark);
                        num3.setTextColor(c);
                        num3.setShadowLayer(25, 0, 0, c);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                Animation alpha2 = new AlphaAnimation(1, 0);
                as.addAnimation(alpha2);
                alpha2.setDuration(600);
                alpha2.setInterpolator(new LinearInterpolator());
                alpha2.setStartOffset(500);
                alpha2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        num1.setVisibility(View.VISIBLE);
                        num2.setVisibility(View.VISIBLE);
                        num3.setVisibility(View.INVISIBLE);
                        Animation expand = AnimationUtils.loadAnimation(TutorialExpandir.this, R.anim.expand_right);
                        expand.setDuration(500);
                        num1.startAnimation(expand);
                        expand = AnimationUtils.loadAnimation(TutorialExpandir.this, R.anim.expand_left);
                        expand.setDuration(500);
                        num2.startAnimation(expand);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                Animation alpha3 = new TranslateAnimation(0, 0, 0, 0);
                as.addAnimation(alpha3);
                alpha3.setDuration(700);
                alpha3.setInterpolator(new LinearInterpolator());
                alpha3.setStartOffset(2000);


                alpha3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {


                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        hand.startAnimation(as);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                hand.startAnimation(as);
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


}
