package com.studios.thinkup.negativo.components.handler;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.studios.thinkup.negativo.components.NumeroText;

import java.util.List;
import java.util.Vector;

/**
 * Created by fcostazini on 25/08/2015.
 * Handler para movimientos de Touch
 */
public class TouchHandler implements View.OnTouchListener {
    private static final Integer LONG_CLICK = 3000;
    private long holdDuration;
    private Float prevX;
    private Float prevY;
    private boolean isHolding;
    private ViewGroup container;
    private List<NumeroText> selected;
    private long startClickTime;
    private Integer maxElements;
    private Integer maxAfterLongClickElements;
    private ISelectableHandler handler;


    private boolean afterLongClick;

    public TouchHandler(ViewGroup container, ISelectableHandler handler, Integer maxElements, Integer maxAfterLongClickElements) {

        this.container = container;
        this.maxElements = maxElements;
        this.maxAfterLongClickElements = maxAfterLongClickElements;
        this.selected = new Vector<>();
        this.handler = handler;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (prevY == null) {
            prevY = motionEvent.getY();
        }
        if (prevX == null) {
            prevX = motionEvent.getX();
        }

        Point delta = new Point((int) Math.abs(motionEvent.getX() - prevX), (int) Math.abs(motionEvent.getY() - prevY));
        Point direction = new Point(prevX.compareTo(motionEvent.getX()), prevY.compareTo(motionEvent.getY()));
        if (!afterLongClick && isHolding) {
            holdDuration += SystemClock.elapsedRealtime() - startClickTime;
            afterLongClick = holdDuration >= LONG_CLICK;
        } else {
            holdDuration = 0;
        }
        if (!afterLongClick && isLongClick()) {
            afterLongClick = true;
        }
        NumeroText v;
        try {
            v = (NumeroText) findViewAtPosition(container, (int) motionEvent.getX(), (int) motionEvent.getY());
        } catch (ClassCastException e) {
            v = null;
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startClickTime = SystemClock.elapsedRealtime();
                clearStatus();
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (selected.size() > 0) {
                    handler.selectedClick(selected, afterLongClick);
                }
                selected.clear();

                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                container.getParent().requestDisallowInterceptTouchEvent(false);
                clearStatus();
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if (hasMoved(delta)) {
                    isHolding = false;
                }

                container.getParent().requestDisallowInterceptTouchEvent(true);


                if (v != null) {
                    int c;

                    if (afterLongClick) {
                        this.selectView(v);
                        c = container.getResources().getColor(android.R.color.holo_blue_dark);
                        v.setTextColor(c);
                        v.setShadowLayer(25, 0, 0, c);
                    } else {
                        if (selected.size() <= maxElements && this.selectView(v)) {
                            c = container.getResources().getColor(android.R.color.holo_green_dark);
                            v.setTextColor(c);
                            v.setShadowLayer(25, 0, 0, c);
                        }
                    }


                }
                break;
            }
        }

        prevX = motionEvent.getX();
        prevY = motionEvent.getY();

        return true;
    }

    private void clearStatus() {
        isHolding = true;
        holdDuration = 0;
        afterLongClick = false;
        selected.clear();

    }

    private boolean selectView(NumeroText v) {
        if (v != null && !selected.contains(v)) {
            int limit;
            if (afterLongClick) {
                limit = maxAfterLongClickElements;

            } else {
                limit = maxElements;

            }
            if (selected.size() < limit) {
                selected.add(v);
                return true;


            }

        }
        return false;


    }


    private boolean isLongClick() {
        return holdDuration >= LONG_CLICK && selected.size() == 0;
    }

    private boolean hasMoved(Point deltaPos) {
        return (Math.sqrt(deltaPos.x * deltaPos.x + deltaPos.y * deltaPos.y)) > 10;

    }


    private View findViewAtPosition(ViewGroup parent, int x, int y) {


        for (int i = 0; i < parent.getChildCount(); i++) {
            try {
                NumeroText view = (NumeroText) parent.getChildAt(i);
                Rect outRect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                if (outRect.contains(x, y)) {
                    return view;
                }
            } catch (ClassCastException e) {
                continue;
            }


        }
        return null;
    }

}
