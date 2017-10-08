package com.krp.android.animation.bottomsheet.menu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krp.android.animation.bottomsheet.menu.R;

/**
 * Created by Kumar Purushottam on 07/10/17
 */
public class Helper {

    public static boolean isInternetAvailable(Context pContext) {
        ConnectivityManager cm =
                (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static int dpToPx(Context pContext, int pDp) {
        DisplayMetrics metrics = pContext.getResources().getDisplayMetrics();
        return (int) (pDp * metrics.density + 0.5f);
    }

    public static void openAppPermissionSettings(Context pContext) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", pContext.getPackageName(), null);
        intent.setData(uri);
        pContext.startActivity(intent);
    }

    public static boolean openIntent(Context pContext, String url) {
        try {
            if (url.startsWith(Constants.INTENT_ACTION_PREFIX_MAILTO)) {
                // track event -> my games
                /*AnalyticsTrackers.trackEvent(pContext,
                        R.string.analytics_event_category_app, R.string.analytics_event_action_app_contact_email, "", 0, null);*/
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            pContext.startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(pContext, R.string.err_msg_something_went_wrong, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * @param pActivity
     * @param pShareType
     */
    public static void share(Activity pActivity, int pShareType) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        switch (pShareType) {
            case Constants.SHARE_APP:
            default: {
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, Constants.EXTRA_PATH_PLAYSTORE);
                break;
            }
        }
        pActivity.startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    public static void animateMenuItems(final Activity pActivity, final float pSlideOffset, final Pair<Integer, Integer>... pSrcTrgResIds) {
        for(Pair<Integer, Integer> pSrcTrgRes : pSrcTrgResIds) {

            final View vDisSrc = pActivity.findViewById(pSrcTrgRes.first);
            final int[] locationDisSrc = new int[2];
            final Rect srcRect = new Rect();
            vDisSrc.getHitRect(srcRect);
            vDisSrc.getLocationOnScreen(locationDisSrc);

            final View vDisTrg = pActivity.findViewById(pSrcTrgRes.second);
            final int[] locationDisTrg = new int[2];
            final Rect trgRect = new Rect();
            vDisTrg.getHitRect(trgRect);
            vDisTrg.getLocationOnScreen(locationDisTrg);

            LinearLayout vSrcReplica = (LinearLayout) pActivity.getWindow().getDecorView()
                    .findViewWithTag(pSrcTrgRes.first);
            if(vSrcReplica == null) {
                vSrcReplica = (LinearLayout) LayoutInflater.from(pActivity)
                        .inflate(R.layout.krp_l_content_item_menu, null);
                vSrcReplica.setTag(pSrcTrgRes.first);

                //TODO remove
                if(pSrcTrgRes.first == R.id.fab_invite_d) {
                    FloatingActionButton vSrcReplica_fab = (FloatingActionButton) vSrcReplica.findViewById(R.id.fab);
                    vSrcReplica_fab.setImageResource(R.drawable.ic_invite);
                    TextView vSrcReplica_txt = (TextView) vSrcReplica.findViewById(R.id.txt);
                    vSrcReplica_txt.setText("Invite");
                } else if(pSrcTrgRes.first == R.id.fab_contact_us_d) {
                    FloatingActionButton vSrcReplica_fab = (FloatingActionButton) vSrcReplica.findViewById(R.id.fab);
                    vSrcReplica_fab.setImageResource(R.drawable.ic_mail);
                    TextView vSrcReplica_txt = (TextView) vSrcReplica.findViewById(R.id.txt);
                    vSrcReplica_txt.setText("Contact Us");
                } else if(pSrcTrgRes.first == R.id.fab_disclaimer_d) {
                    FloatingActionButton vSrcReplica_fab = (FloatingActionButton) vSrcReplica.findViewById(R.id.fab);
                    vSrcReplica_fab.setImageResource(R.drawable.ic_disclaimer);
                    TextView vSrcReplica_txt = (TextView) vSrcReplica.findViewById(R.id.txt);
                    vSrcReplica_txt.setText("Discalimer");
                } else if(pSrcTrgRes.first == R.id.fab_more_d) {
                    FloatingActionButton vSrcReplica_fab = (FloatingActionButton) vSrcReplica.findViewById(R.id.fab);
                    vSrcReplica_fab.setImageResource(R.drawable.ic_arrow_up);
                    TextView vSrcReplica_txt = (TextView) vSrcReplica.findViewById(R.id.txt);
                    vSrcReplica_txt.setText("More");
                }

                pActivity.addContentView(vSrcReplica,
                        new ViewGroup.LayoutParams(srcRect.right-srcRect.left, srcRect.bottom-srcRect.top));
            }
            //TODO remove   ->  End
            vSrcReplica.bringToFront();

            vSrcReplica.setX(srcRect.left+locationDisSrc[0]);
            vSrcReplica.setY(srcRect.top+locationDisSrc[1]);

            vSrcReplica.setRotation(0 - (0 - 360) * pSlideOffset);
            vSrcReplica.setScaleX(1 - (1 - 0.96f) * pSlideOffset);
            vSrcReplica.setScaleY(1 - (1 - 0.96f) * pSlideOffset);

            vDisTrg.getHitRect(trgRect);
            vDisTrg.getLocationOnScreen(locationDisTrg);

            vSrcReplica.setX(locationDisSrc[0] + (locationDisTrg[0]-locationDisSrc[0]) * pSlideOffset);
            vSrcReplica.setY(locationDisSrc[1] + (locationDisTrg[1]-locationDisSrc[1]) * pSlideOffset);

            vDisSrc.setVisibility(View.INVISIBLE);
            vDisTrg.setVisibility(View.INVISIBLE);

            if(pSlideOffset == 1) {
                vDisTrg.setVisibility(View.VISIBLE);    //Expanding bottom sheet
                vSrcReplica.setVisibility(View.GONE);
                ((ViewGroup) vSrcReplica.getParent()).removeView(vSrcReplica);

                //TODO when default menu size is NORMAL
                /*((FloatingActionButton) ((LinearLayout)vDisTrg).getChildAt(0))
                        .setSize(FloatingActionButton.SIZE_MINI);
                ((LinearLayout)vDisTrg).getChildAt(1).setVisibility(View.VISIBLE);*/

            } else if(pSlideOffset == 0){
                vDisSrc.setVisibility(View.VISIBLE);    //Collapsing bottom sheet
                vSrcReplica.setVisibility(View.GONE);
                ((ViewGroup) vSrcReplica.getParent()).removeView(vSrcReplica);

                //TODO when default menu size is NORMAL
                /*((FloatingActionButton) ((LinearLayout)vDisTrg).getChildAt(0))
                        .setSize(FloatingActionButton.SIZE_NORMAL);
                ((LinearLayout)vDisTrg).getChildAt(1).setVisibility(View.GONE);*/
            }
        }
    }

    public static void createChildExpandSlideTransitionalAnimators(final Activity pActivity, final float pSlideOffset, final int[]... pSliders) {
        for(int[] pSlider : pSliders) {

            final View vSrc = pActivity.findViewById(pSlider[0]);
            final int[] locationSrc = new int[2];
            final Rect srcRect = new Rect();
            vSrc.getHitRect(srcRect);
            vSrc.getLocationOnScreen(locationSrc);

            for(int i=1; i<pSlider.length; i++) {
                final View vTrg = pActivity.findViewById(pSlider[i]);
                final int[] locationTrg = new int[2];
                final Rect trgRect = new Rect();
                vTrg.getHitRect(trgRect);
                vTrg.getLocationOnScreen(locationTrg);

                vTrg.setAlpha(pSlideOffset);
                vTrg.setTranslationX(0 - (locationSrc[0]-locationTrg[0]) * (1-pSlideOffset));
                vTrg.setRotation(0 - (0-360) * pSlideOffset);

                vTrg.setVisibility(View.VISIBLE);
            }
        }
    }

}
