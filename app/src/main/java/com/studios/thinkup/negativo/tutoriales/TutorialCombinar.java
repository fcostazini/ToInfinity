package com.studios.thinkup.negativo.tutoriales;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studios.thinkup.negativo.R;
import com.studios.thinkup.negativo.components.NumeroText;

public class TutorialCombinar extends Activity {
    Animation transition;
    AnimationSet as;
    TextView num2;
    TextView num1;
    TextView num3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_combinar);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        final ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Put your code here.
                final ImageView hand = (ImageView) findViewById(R.id.hand);


                as = new AnimationSet(true);
                as.setRepeatMode(Animation.RESTART);
                as.setRepeatCount(Animation.INFINITE);
                as.setFillAfter(true);
                Animation alpha = new AlphaAnimation(0,1);
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
                        hand.setVisibility(View.VISIBLE);

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
                hand.setY(location[1]);

                num2.getLocationOnScreen(location);
                transition = new TranslateAnimation(0f, (location[0] + num2.getPaddingLeft() / 2 + num2.getWidth() / 2) - hand.getX(), 0f, 0f);
                transition.setDuration(1500);
                transition.setFillAfter(true);
                transition.setRepeatMode(Animation.INFINITE);
                transition.setRepeatMode(Animation.RESTART);
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
                transition.setStartOffset(900);
                as.addAnimation(transition);

                Animation alpha2 = new AlphaAnimation(1,0);
                as.addAnimation(alpha2);
                alpha2.setDuration(400);
                alpha2.setInterpolator(new LinearInterpolator());
                alpha2.setStartOffset(2500);
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
                        myFadeInAnimation.setStartOffset(200);
                        num1.startAnimation(myFadeInAnimation);
                        myFadeInAnimation = AnimationUtils.loadAnimation(TutorialCombinar.this, R.anim.collapse_left);
                        myFadeInAnimation.setStartOffset(200);
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
                                hand.startAnimation(as);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial_combinar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
