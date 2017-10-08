package com.krp.android.animation.bottomsheet.menu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.krp.android.animation.bottomsheet.menu.Application;

/**
 * Created by Kumar Purushottam on 07/10/17
 */
public class BaseActivity extends AppCompatActivity {

    protected Application mApplication;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (Application) getApplication();
    }

    public void onEvent(int pEventType, Object pObject) {

    }

}
