package com.krp.android.animation.bottomsheet.menu.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krp.android.animation.bottomsheet.menu.Application;
import com.krp.android.animation.bottomsheet.menu.R;
import com.krp.android.animation.bottomsheet.menu.activity.BaseActivity;
import com.krp.android.animation.bottomsheet.menu.utils.Constants;

/**
 * Created by Kumar Purushottam on 07/10/17
 */
public class CommonWebDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = CommonWebDialogFragment.class.getSimpleName();

    public static final int ALERT_DISCLAIMER = 11;
    public static final int ALERT_DISCLAIMER_INFO = 12;
    public static final int ALERT_TERMS_OF_USE = 13;
    private BaseActivity mBaseActivity;
    private NestedScrollView mScrollView;
    private CheckBox mCbInfo;
    private WebView mWebView;
    private int mAlertType;
    private String mUrl;

    public CommonWebDialogFragment() {
    }

    public static CommonWebDialogFragment getInstance(int pAlertType) {
        CommonWebDialogFragment commonDialogFragment = new CommonWebDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_TYPE, pAlertType);
        commonDialogFragment.setArguments(bundle);
        commonDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return commonDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;    //Prevent app to dismiss on back pressed
                }
                return false;
            }
        });
        View view = inflater.inflate(R.layout.krp_l_dialog_common_web, container, false);

        mScrollView = (NestedScrollView) view.findViewById(R.id.scroll_view);
        final TextView txtHeader = (TextView) view.findViewById(R.id.txt_header);
        final FloatingActionButton btnScrollDown = (FloatingActionButton) view.findViewById(R.id.btn_scroll_down);
        btnScrollDown.setOnClickListener(this);
        final TextView btnRight = (TextView) view.findViewById(R.id.btn_right);
        btnRight.setOnClickListener(this);
        final TextView btnLeft = (TextView) view.findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(this);
        final LinearLayout layoutChkbxInfo = (LinearLayout) view.findViewById(R.id.layout_chkbx_info);
        final TextView txtChkbxInfo = (TextView) view.findViewById(R.id.txt_chkbx_info);
        mCbInfo = (CheckBox) view.findViewById(R.id.chkbx_info);
        mWebView = (WebView) view.findViewById(R.id.web_view);

        mWebView.getSettings();
        mWebView.setBackgroundColor(Color.TRANSPARENT);

        final RelativeLayout progressBarLayout = (RelativeLayout) view.findViewById(R.id.progressBarLayout);
        progressBarLayout.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels;
        progressBarLayout.setVisibility(View.VISIBLE);
        //Scroll NestedScrollView to top when page load completes
        //WebView scrolls not works within ScrollView
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(final WebView view, String url) {
                btnLeft.setVisibility(View.VISIBLE);
                btnRight.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.GONE);
                if (mAlertType == ALERT_DISCLAIMER) {
                    layoutChkbxInfo.setVisibility(View.VISIBLE);
                }
                mScrollView.smoothScrollTo(0, 0);
            }
        });
        //Update Title when scroll-view scrolled to hide "disclaimer" or "toc"
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY + oldScrollY >= 100) {
                    if (mAlertType == ALERT_DISCLAIMER) {
                        txtHeader.setText(mUrl == Constants.DISCLAIMER_URL ?
                                R.string.txt_header_disclaimer : R.string.txt_header_tou);
                    } else if (mAlertType == ALERT_TERMS_OF_USE) {
                        txtHeader.setText(R.string.txt_header_tou);
                    }
                } else {
                    txtHeader.setText(R.string.app_name);
                }

                Rect visibleRect = new Rect();
                mScrollView.getDrawingRect(visibleRect);
                if (btnLeft.getLocalVisibleRect(visibleRect)) {
                    btnScrollDown.setVisibility(View.GONE);
                } else {
                    btnScrollDown.setVisibility(View.VISIBLE);
                }
            }
        });

        mAlertType = getArguments().getInt(Constants.KEY_TYPE);
        switch (mAlertType) {
            case ALERT_DISCLAIMER: {
                txtHeader.setText(R.string.app_name);
                mWebView.loadUrl(mUrl = Constants.DISCLAIMER_URL);
                btnRight.setText(R.string.btn_cancel);
                btnLeft.setText(R.string.btn_accept);
                btnLeft.setEnabled(false);
                btnLeft.setAlpha(0.3f);

                SpannableString span = new SpannableString(getString(R.string.msg_accept_tou));
                span.setSpan(new URLSpan(Constants.TERMS_OF_USE_URL),
                        span.length() - 7, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                        span.length() - 7, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                txtChkbxInfo.setText(span);
                txtChkbxInfo.setMovementMethod(mLinkTermsMovementMethod);

                mCbInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            btnLeft.setEnabled(true);
                            btnLeft.setAlpha(1f);
                        } else {
                            btnLeft.setEnabled(false);
                            btnLeft.setAlpha(0.3f);
                        }
                    }
                });
                break;
            }
            case ALERT_TERMS_OF_USE: {
                txtHeader.setText(R.string.app_name);
                mWebView.loadUrl(Constants.TERMS_OF_USE_URL);
                btnRight.setVisibility(View.GONE);
                btnLeft.setText(R.string.btn_ok);
                break;
            }
            case ALERT_DISCLAIMER_INFO: {
                txtHeader.setText(R.string.app_name);
                mWebView.loadUrl(Constants.DISCLAIMER_URL);
                btnRight.setVisibility(View.GONE);
                btnLeft.setText(R.string.btn_ok);
                break;
            }
        }

        return view;
    }

    private MovementMethod mLinkTermsMovementMethod = new LinkMovementMethod() {
        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            if (mUrl.equals(Constants.DISCLAIMER_URL)) {
                mWebView.loadUrl(mUrl = Constants.TERMS_OF_USE_URL);
            }
            return true;
        }
    };

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right: {
                if (mAlertType == ALERT_DISCLAIMER) {
                    ((Application) mBaseActivity.getApplication())
                            .setDisclaimerAccepted(false);
                }
                mBaseActivity.onEvent(Constants.ACTION_RIGHT_CLICK, mAlertType);
                dismiss();
                break;
            }
            case R.id.btn_left: {
                if (mAlertType == ALERT_DISCLAIMER) {
                    ((Application) mBaseActivity.getApplication())
                            .setDisclaimerAccepted(true);
                } else if (mAlertType == ALERT_TERMS_OF_USE) {
                }
                mBaseActivity.onEvent(Constants.ACTION_LEFT_CLICK, mAlertType);
                dismiss();
                break;
            }
            case R.id.btn_scroll_down: {
                mScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                break;
            }
        }
    }
}
