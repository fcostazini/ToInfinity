package com.studios.thinkup.negativo.providers;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by FaQ on 08/06/2015.
 */
public class TypefaceProvider {
    public final static String NUMBER = "number_font.ttf";

    private static TypefaceProvider instance;
    private HashMap<String, Typeface> typeFaces;

    private TypefaceProvider(Context context) {
        typeFaces = new HashMap<>();
        typeFaces.put(NUMBER, Typeface.createFromAsset(context.getAssets(), NUMBER));


    }


    public static TypefaceProvider getInstance(Context context) {
        if (TypefaceProvider.instance == null) {
            TypefaceProvider.instance = new TypefaceProvider(context);
        }
        return TypefaceProvider.instance;
    }

    public Typeface getTypeface(String nombre) {
        return this.typeFaces.get(nombre);
    }

}
