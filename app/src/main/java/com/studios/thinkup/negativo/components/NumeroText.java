package com.studios.thinkup.negativo.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.studios.thinkup.negativo.R;
import com.studios.thinkup.negativo.providers.TypefaceProvider;

/**
 * Created by fcostazini on 20/08/2015.
 */
public class NumeroText extends TextView {

    public static final int MAX_VAL = 9;
    public static final int MIN_VAL = 0;

    public NumeroText(Context context) {
        super(context);
        init(context);
    }

    public int getValue() {
        return Integer.valueOf(this.getText().toString());
    }

    private void init(Context context) {
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (20 * scale + 0.5f);
        this.setPadding((int) (10 * scale + 0.5f), dpAsPixels, (int) (10 * scale + 0.5f), dpAsPixels);
        this.setEms(1);
        this.setTypeface(TypefaceProvider.getInstance(context).getTypeface(TypefaceProvider.NUMBER));
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.number_font));
        this.setGravity(Gravity.CENTER);
        this.setIncludeFontPadding(false);
    }

    public NumeroText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumeroText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }


}
