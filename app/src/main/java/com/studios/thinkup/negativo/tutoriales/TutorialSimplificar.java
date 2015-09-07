package com.studios.thinkup.negativo.tutoriales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studios.thinkup.negativo.R;

public class TutorialSimplificar extends AppCompatActivity {
    Animation transition;
    AnimationSet as;
    TextView num2;
    TextView num1;
    TextView num3;
    TextView num4;
    ImageView hand;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_simplificar);
        this.c = this.getResources().getColor(android.R.color.holo_green_dark);


        Button siguiente = (Button) findViewById(R.id.btn_siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorialSimplificar.this, TutorialGanador.class);
                startActivity(i);
            }
        });
        Button anterior = (Button) findViewById(R.id.btn_anterior);
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorialSimplificar.this, TutorialExpandir.class);
                startActivity(i);
            }
        });


        num1 = (TextView) findViewById(R.id.view1);
        num2 = (TextView) findViewById(R.id.view2);
        num3 = (TextView) findViewById(R.id.view3);
        num4 = (TextView) findViewById(R.id.txt_simplificado);
        hand = (ImageView) findViewById(R.id.hand);


        final LinearLayout layout = (LinearLayout) findViewById(R.id.ly_numeros);
        final ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Put your code here.
                as = new AnimationSet(true);
                as.setFillAfter(true);
                as.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        layout.setVisibility(View.VISIBLE);
                        num4.setVisibility(View.GONE);
                        int c = TutorialSimplificar.this.getResources().getColor(android.R.color.black);
                        num1.setTextColor(c);
                        num1.setShadowLayer(1, 0, 0, c);
                        num2.setTextColor(c);
                        num2.setShadowLayer(1, 0, 0, c);
                        num3.setTextColor(c);
                        num3.setShadowLayer(1, 0, 0, c);
                        int[] location = new int[2];
                        num1.getLocationOnScreen(location);
                        hand.setX(location[0] + num1.getPaddingLeft() / 2 + num1.getWidth() / 2);
                        hand.setY(location[1]);
                        hand.setImageResource(R.drawable.point);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                Animation alpha = new AlphaAnimation(0, 1);
                alpha.setDuration(1000);
                alpha.setFillAfter(true);
                alpha.setInterpolator(new AccelerateInterpolator());
                alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        hand.setImageResource(R.drawable.point_hold);

                        num1.setTextColor(c);
                        num1.setShadowLayer(25, 0, 0, c);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                Animation alpha2 = new AlphaAnimation(1, 1);
                alpha2.setDuration(100);
                alpha2.setStartOffset(1500);
                alpha2.setInterpolator(new AccelerateInterpolator());
                alpha2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        hand.setImageResource(R.drawable.point_hold);

                        num1.setTextColor(c);
                        num1.setShadowLayer(25, 0, 0, c);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                Animation desplazar = new TranslateAnimation(0f, num3.getWidth() * 2, 0f, 0f);
                desplazar.setDuration(2000);
                desplazar.setStartOffset(2000);
                desplazar.setInterpolator(new AccelerateDecelerateInterpolator());
                desplazar.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                Animation select = new TranslateAnimation(0, 0, 0, 0);
                select.setDuration(100);
                select.setStartOffset(2500);
                select.setInterpolator(new LinearInterpolator());
                select.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        num2.setTextColor(c);
                        num2.setShadowLayer(25, 0, 0, c);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                Animation select2 = new AlphaAnimation(1, 1);
                select2.setDuration(100);
                select2.setStartOffset(3500);
                select2.setInterpolator(new LinearInterpolator());
                select2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        num3.setTextColor(c);
                        num3.setShadowLayer(25, 0, 0, c);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                Animation alpha3 = new AlphaAnimation(1, 0);
                alpha3.setDuration(1000);
                alpha3.setInterpolator(new LinearInterpolator());
                alpha3.setStartOffset(4000);
                alpha3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        AnimationSet desaparecer = new AnimationSet(true);
                        Animation rotar = new RotateAnimation(0f, 360f * 2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        Animation achicar = new ScaleAnimation(1.15f, 0, 1.15f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        Animation desplazar = new TranslateAnimation(0f, -150f, 0, 0);

                        rotar.setInterpolator(new AccelerateDecelerateInterpolator());
                        rotar.setDuration(300);
                        rotar.setFillAfter(true);

                        achicar.setInterpolator(new BounceInterpolator());
                        achicar.setDuration(400);
                        achicar.setFillAfter(true);

                        desplazar.setInterpolator(new BounceInterpolator());
                        desplazar.setDuration(300);
                        desplazar.setFillAfter(true);

                        desaparecer.addAnimation(rotar);
                        desaparecer.addAnimation(achicar);
                        desaparecer.addAnimation(desplazar);
                        num2.startAnimation(desaparecer);

                        desaparecer = new AnimationSet(true);
                        desplazar = new TranslateAnimation(0f, -300f, 0, 0);
                        desplazar.setInterpolator(new BounceInterpolator());
                        desplazar.setDuration(300);
                        desplazar.setFillAfter(true);

                        desaparecer.addAnimation(rotar);
                        desaparecer.addAnimation(achicar);
                        desaparecer.addAnimation(desplazar);

                        desaparecer.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                layout.setVisibility(View.GONE);
                                num4.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        num3.startAnimation(desaparecer);


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                Animation alpha4 = new TranslateAnimation(0, 0, 0, 0);

                alpha4.setDuration(1000);
                alpha4.setInterpolator(new LinearInterpolator());
                alpha4.setStartOffset(6000);
                alpha4.setAnimationListener(new Animation.AnimationListener() {
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
                as.addAnimation(alpha);
                as.addAnimation(alpha2);
                as.addAnimation(desplazar);
                as.addAnimation(select);
                as.addAnimation(select2);
                as.addAnimation(alpha3);
                as.addAnimation(alpha4);
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
