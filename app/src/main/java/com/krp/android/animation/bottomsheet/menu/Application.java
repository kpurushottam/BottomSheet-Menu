package com.krp.android.animation.bottomsheet.menu;

import android.content.Context;
import android.content.SharedPreferences;

import com.krp.android.animation.bottomsheet.menu.utils.Constants;
import com.krp.android.animation.bottomsheet.menu.utils.TypefaceUtility;

/**
 * Created by Kumar Purushottam on 07/10/17
 */
public class Application extends android.app.Application {

    private SharedPreferences mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialise Shared Prefs
        mPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        TypefaceUtility.overrideFont(this, "SERIF", getString(R.string.font_caviar_dreams_regular));
    }

    public void setDisclaimerAccepted(boolean accepted) {
        mPrefs.edit()
                .putBoolean(Constants.PREFS_KEY_DISCLAIMER_ACCEPTED, accepted)
                .apply();
    }

    public boolean isDisclaimerAccepted() {
        return mPrefs.getBoolean(Constants.PREFS_KEY_DISCLAIMER_ACCEPTED, false);
    }

}
