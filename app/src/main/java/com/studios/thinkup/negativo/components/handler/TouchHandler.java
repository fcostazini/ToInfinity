package com.studios.thinkup.negativo.components.handler;

import android.graphics.Rect;
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
    private Float prevX;
    private Float prevY;
    private ViewGroup container;
    private List<NumeroText> selected;
    private ISelectableHandler handler;
    private int selectedColor;

    public TouchHandler(ViewGroup container, ISelectableHandler handler) {

        this.container = container;
        this.selected = new Vector<>();
        this.handler = handler;
        this.selectedColor = container.getResources().getColor(android.R.color.holo_green_dark);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (prevY == null) {
            prevY = motionEvent.getY();
        }
        if (prevX == null) {
            prevX = motionEvent.getX();
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                clearStatus();
                break;
            }
            case MotionEvent.ACTION_UP: {

                if (selected.size() == 0) {
                    NumeroText v;
                    try {
                        v = (NumeroText) findViewAtPosition(container, (int) motionEvent.getX(), (int) motionEvent.getY());
                    } catch (ClassCastException e) {
                        v = null;
                    }
                    if (v != null) {
                        selectView(v);
                    }
                }
                if (selected.size() > 0) {
                    handler.selectedClick(selected);
                    selected.clear();
                }


                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                container.getParent().requestDisallowInterceptTouchEvent(false);
                clearStatus();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                NumeroText v;
                try {
                    v = (NumeroText) findViewAtPosition(container, (int) motionEvent.getX(), (int) motionEvent.getY());
                } catch (ClassCastException e) {
                    v = null;
                }

                container.getParent().requestDisallowInterceptTouchEvent(true);


                if (v != null) {
                    selectView(v);
                    v.setTextColor(this.selectedColor);
                    v.setShadowLayer(25, 0, 0, this.selectedColor);
                }


                break;
            }
        }

        prevX = motionEvent.getX();
        prevY = motionEvent.getY();

        return true;
    }

    private void clearStatus() {
        selected.clear();

    }

    private boolean selectView(NumeroText v) {
        if (v != null && !selected.contains(v)) {

            selected.add(v);
            return true;

        }
        return false;


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
