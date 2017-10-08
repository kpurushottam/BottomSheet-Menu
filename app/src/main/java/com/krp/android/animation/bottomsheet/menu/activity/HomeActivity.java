package com.krp.android.animation.bottomsheet.menu.activity;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.krp.android.animation.bottomsheet.menu.R;
import com.krp.android.animation.bottomsheet.menu.fragment.CommonWebDialogFragment;
import com.krp.android.animation.bottomsheet.menu.utils.Constants;
import com.krp.android.animation.bottomsheet.menu.utils.Helper;

/**
 * Created by Kumar Purushottam on 07/10/17
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private BottomSheetBehavior<View> mBottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.krp_l_activity_home);

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        View bottomViewOptions = findViewById(R.id.bottom_view_options);
        bottomViewOptions.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomViewOptions);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //Do Nothing
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                animateBottomSheet(bottomSheet, slideOffset);
            }
        });

        CardView cvMore = (CardView) findViewById(R.id.cv_menu_mini);
        cvMore.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mBottomSheetBehavior.setPeekHeight(cvMore.getMeasuredHeight());
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        findViewById(R.id.fab_invite_d).setOnClickListener(this);
        findViewById(R.id.fab_contact_us_d).setOnClickListener(this);
        findViewById(R.id.fab_disclaimer_d).setOnClickListener(this);
        findViewById(R.id.fab_more_d).setOnClickListener(this);

        findViewById(R.id.fab_contact_us).setOnClickListener(this);
        findViewById(R.id.fab_settings).setOnClickListener(this);
        findViewById(R.id.fab_disclaimer).setOnClickListener(this);
        findViewById(R.id.fab_tou).setOnClickListener(this);
        findViewById(R.id.fab_about_us).setOnClickListener(this);
    }

    private void animateBottomSheet(View pBottomSheetView, float pSlideOffset) {
        int menuColorValue = (int) new ArgbEvaluator().evaluate(pSlideOffset, Color.TRANSPARENT, ContextCompat.getColor(this, R.color.colorFabTint));
        ((CardView) findViewById(R.id.cv_menu_mini)).setCardBackgroundColor(menuColorValue);
        ((CardView) findViewById(R.id.cv_settings)).setCardBackgroundColor(menuColorValue);
        ((CardView) findViewById(R.id.cv_about_us)).setCardBackgroundColor(menuColorValue);
        //Add elevation to cards
        ((CardView)findViewById(R.id.cv_menu_mini))
                .setCardElevation(Helper.dpToPx(this, 12) * pSlideOffset);
        ((CardView)findViewById(R.id.cv_settings))
                .setCardElevation(Helper.dpToPx(this, 12) * pSlideOffset);
        ((CardView)findViewById(R.id.cv_about_us))
                .setCardElevation(Helper.dpToPx(this, 12) * pSlideOffset);
        //Add vertical animators to selected pairs
        Helper.animateMenuItems(this, pSlideOffset,
                new Pair<>(R.id.fab_invite_d, R.id.fab_invite_d),
                new Pair<>(R.id.fab_disclaimer_d, R.id.fab_disclaimer),
                new Pair<>(R.id.fab_contact_us_d, R.id.fab_contact_us),
                new Pair<>(R.id.fab_more_d, R.id.fab_more_d));
        //Add horizontal animators to selected sub menus
        Helper.createChildExpandSlideTransitionalAnimators(this, pSlideOffset,
                new int[]{R.id.fab_contact_us, R.id.fab_settings},
                new int[]{R.id.fab_disclaimer, R.id.fab_tou, R.id.fab_about_us});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_more_d: {
                if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            }
            case R.id.fab_invite_d: {
                Helper.share(this, Constants.SHARE_APP);
                break;
            }
            case R.id.fab_disclaimer: {
                CommonWebDialogFragment.getInstance(CommonWebDialogFragment.ALERT_DISCLAIMER_INFO)
                        .show(getSupportFragmentManager(), CommonWebDialogFragment.TAG);
                break;
            }
            case R.id.fab_disclaimer_d: {
                CommonWebDialogFragment.getInstance(CommonWebDialogFragment.ALERT_DISCLAIMER_INFO)
                        .show(getSupportFragmentManager(), CommonWebDialogFragment.TAG);
                break;
            }
            case R.id.fab_tou: {
                Snackbar.make(findViewById(R.id.container_coordinator), "Terms of Use", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.fab_contact_us: {
                Helper.openIntent(this, Constants.INTENT_ACTION_PREFIX_MAILTO + ":" + Constants.EXTRA_CONTACT_US_EMAIL);
                break;
            }
            case R.id.fab_contact_us_d: {
                Helper.openIntent(this, Constants.INTENT_ACTION_PREFIX_MAILTO + ":" + Constants.EXTRA_CONTACT_US_EMAIL);
                break;
            }
            case R.id.fab_settings: {
                Snackbar.make(findViewById(R.id.container_coordinator), "Settings", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.fab_about_us: {
                Snackbar.make(findViewById(R.id.container_coordinator), "About Us", Snackbar.LENGTH_SHORT).show();
                break;
            }
        }
    }

}
