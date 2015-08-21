package com.studios.thinkup.negativo.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by fcostazini on 21/08/2015.
 */
class DrawingView extends SurfaceView {

    private final SurfaceHolder surfaceHolder;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DrawingView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.BLACK);
                canvas.drawCircle(event.getX(), event.getY(), 50, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        return false;
    }

}

