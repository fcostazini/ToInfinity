package com.studios.thinkup.negativo.tutoriales;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
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

public class TutorialCombinar extends AppCompatActivity {
    Animation transition;
    AnimationSet as;
    TextView num2;
    TextView num1;
    TextView num3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_combinar);

        Button siguiente = (Button) findViewById(R.id.btn_siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorialCombinar.this, TutorialExpandir.class);
                startActivity(i);
            }
        });

        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        final ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Put your code here.
                final ImageView hand = (ImageView) findViewById(R.id.hand);


                as = new AnimationSet(true);
                as.setFillAfter(true);
                Animation alpha = new AlphaAnimation(0, 1);
                alpha.setStartOffset(400);
                alpha.setDuration(400);
                as.addAnimation(alpha);
                alpha.setInterpolator(new LinearInterpolator());
                alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        num1.setVisibility(View.VISIBLE);
                        num2.setVisibility(View.VISIBLE);
                        num3.setVisibility(View.GONE);
                        int c = TutorialCombinar.this.getResources().getColor(android.R.color.black);
                        num2.setTextColor(c);
                        num2.setShadowLayer(1, 0, 0, c);
                        num1.setTextColor(c);
                        num1.setShadowLayer(1, 0, 0, c);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int c = TutorialCombinar.this.getResources().getColor(android.R.color.holo_green_dark);
                        num1.setTextColor(c);
                        num1.setShadowLayer(25, 0, 0, c);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                num1 = (TextView) findViewById(R.id.view1);
                num2 = (TextView) findViewById(R.id.view2);
                num3 = (TextView) findViewById(R.id.textView3);
                int[] location = new int[2];
                num1.getLocationOnScreen(location);
                hand.setX(location[0] + num1.getPaddingLeft() / 2 + num1.getWidth() / 2);
                hand.setY(location[1] - (num1.getPaddingTop()) / 2);

                num2.getLocationOnScreen(location);
                transition = new TranslateAnimation(0f, (location[0] + num2.getPaddingLeft() / 2 + num2.getWidth() / 2) - hand.getX(), 0f, 0f);
                transition.setDuration(1500);
                transition.setFillAfter(true);
                transition.setRepeatMode(Animation.INFINITE);
                transition.setRepeatMode(Animation.RESTART);
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
                transition.setStartOffset(900);
                as.addAnimation(transition);

                Animation alpha2 = new AlphaAnimation(1, 0);
                as.addAnimation(alpha2);
                alpha2.setDuration(400);
                alpha2.setInterpolator(new LinearInterpolator());
                alpha2.setStartOffset(2500);

                Animation alpha3 = new TranslateAnimation(0, 0, 0, 0);
                as.addAnimation(alpha3);
                alpha3.setDuration(1000);
                alpha3.setInterpolator(new LinearInterpolator());
                alpha3.setStartOffset(3000);
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
                transition.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int c = TutorialCombinar.this.getResources().getColor(android.R.color.holo_green_dark);
                        num2.setTextColor(c);
                        num2.setShadowLayer(25, 0, 0, c);
                        Animation myFadeInAnimation = AnimationUtils.loadAnimation(TutorialCombinar.this, R.anim.collapse_right);
                        myFadeInAnimation.setStartOffset(500);
                        num1.startAnimation(myFadeInAnimation);
                        myFadeInAnimation = AnimationUtils.loadAnimation(TutorialCombinar.this, R.anim.collapse_left);
                        myFadeInAnimation.setStartOffset(500);
                        num2.startAnimation(myFadeInAnimation);
                        myFadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                num1.setVisibility(View.GONE);
                                num2.setVisibility(View.GONE);
                                num3.setVisibility(View.VISIBLE);
                                hand.setVisibility(View.GONE);
                                //hand.startAnimation(as);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


}
