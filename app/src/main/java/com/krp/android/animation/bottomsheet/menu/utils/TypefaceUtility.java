package com.krp.android.animation.bottomsheet.menu.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Kumar Purushottam on 07/10/17.
 */
public class TypefaceUtility {

    private static HashMap<String, SoftReference<Typeface>> mTypefaceCache;

    static {
        mTypefaceCache = new HashMap<>();
    }

    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.e("font not set " + customFontFileNameInAssets, " instead of " + defaultFontNameToOverride);
        }
    }

    public static Typeface getTypeface(Context pContext, @StringRes int pTypefaceName) {
        String filePath = pContext.getString(pTypefaceName);
        if(mTypefaceCache.containsKey(filePath)) {
            SoftReference<Typeface> typefaceReference = mTypefaceCache.get(filePath);
            if(typefaceReference.get() != null) {
                return typefaceReference.get();
            }
        }

        Typeface typeface = Typeface.createFromAsset(pContext.getAssets(), filePath);
        mTypefaceCache.put(filePath, new SoftReference<>(typeface));
        return typeface;
    }
}
