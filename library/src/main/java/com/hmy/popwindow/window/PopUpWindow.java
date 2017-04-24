package com.hmy.popwindow.window;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hmy.popwindow.PopItemAction;
import com.hmy.popwindow.PopSimpleAnimationListener;
import com.hmy.popwindow.PopWindow;
import com.hmy.popwindow.R;
import com.hmy.popwindow.view.PopUpView;
import com.hmy.popwindow.viewinterface.PopWindowInterface;

/**
 * Created by HMY on 2016/9/10.
 */
public class PopUpWindow extends Dialog implements PopWindowInterface, DialogInterface.OnShowListener, View.OnClickListener,
        PopWindowInterface.OnStartShowListener, PopWindowInterface.OnStartDismissListener {

    private FrameLayout mRootLayout;
    private FrameLayout mContainLayout;
    private PopUpView mPopUpView;
    private LinearLayout mContentLayout;
    private PopWindow mPopWindow;

    private Animation mAlphaOpenAnimation;
    private Animation mAlphaCloseAnimation;
    private Animation mPopOpenAnimation;
    private Animation mPopCloseAnimation;

    private PopItemAction mCancelPopItemAction;
    private boolean mIsDismissed = true;

    private View mCustomView;

    public PopUpWindow(Activity activity, int titleResId, int messageResId, PopWindow popWindow) {
        this(activity, titleResId == 0 ? null : activity.getString(titleResId), messageResId == 0 ? null : activity.getString(messageResId), popWindow);
    }

    public PopUpWindow(Activity activity, CharSequence title, CharSequence message, PopWindow popWindow) {
        super(activity, R.style.PopWindowStyle);
        setContentView(R.layout.pop_up_window);
        getWindow().setWindowAnimations(R.style.PopWindowAnimation);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, getScreenHeight(activity) - getStatusBarHeight(activity));
        setOnShowListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mPopWindow = popWindow;

        initRootView();
        initContentView(title, message);
        initAnim();
    }

    private void initRootView() {
        mRootLayout = (FrameLayout) findViewById(R.id.layout_root);
        mRootLayout.setOnClickListener(this);
    }

    private void initContentView(CharSequence title, CharSequence message) {
        mPopUpView = (PopUpView) findViewById(R.id.popUpView);
        mPopUpView.setPopWindow(mPopWindow);
        mPopUpView.setTitleAndMessage(title, message);

        mContentLayout = (LinearLayout) findViewById(R.id.layout_bottom);
        mContainLayout = (FrameLayout) findViewById(R.id.layout_contain);
    }

    private void initAnim() {
        mPopOpenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_action_sheet_enter);
        mPopCloseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_action_sheet_exit);
        mPopCloseAnimation.setAnimationListener(new PopSimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (mCustomView != null) {
                    mContentLayout.post(mDismissRunnable);
                } else
                    mPopUpView.post(mDismissRunnable);
            }
        });

        mAlphaOpenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_alpha_enter);
        mAlphaCloseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_alpha_exit);
        mAlphaCloseAnimation.setAnimationListener(new PopSimpleAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                onStartDismiss(PopUpWindow.this);
                if (mCustomView != null) {
                    mContentLayout.startAnimation(mPopCloseAnimation);
                } else
                    mPopUpView.startAnimation(mPopCloseAnimation);
            }
        });
    }

    private Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
            PopUpWindow.super.dismiss();
        }
    };


    @Override
    public void onShow(DialogInterface dialog) {
        if (mIsDismissed) {
            onStartShow(PopUpWindow.this);
            mIsDismissed = false;
            mRootLayout.startAnimation(mAlphaOpenAnimation);
            if (mCustomView != null) {
                mContentLayout.startAnimation(mPopOpenAnimation);
            } else if (mPopUpView.showAble()) {
                mPopUpView.refreshBackground();
                mPopUpView.startAnimation(mPopOpenAnimation);
            } else {
                throw new RuntimeException("必须至少添加一个PopItemView");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_root) {
            onBackPressed();
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mCancelPopItemAction != null) {
            mCancelPopItemAction.onClick();
        }
    }

    @Override
    public void dismiss() {
        executeExitAnim();
    }

    private void executeExitAnim() {
        if (!mIsDismissed) {
            mIsDismissed = true;
            mRootLayout.startAnimation(mAlphaCloseAnimation);
        }
    }

    @Override
    public void addContentView(View view) {
        view.setClickable(true);
        mPopUpView.addContentView(view);
    }

    @Override
    public void setView(View view) {
        view.setClickable(true);
        mCustomView = view;
        mContentLayout.setVisibility(View.VISIBLE);
        mPopUpView.setVisibility(View.GONE);
        mContentLayout.addView(mCustomView);
    }

    @Override
    public void addItemAction(PopItemAction popItemAction) {
        if (mCustomView != null) {
            return;
        }
        mContentLayout.setVisibility(View.GONE);
        mPopUpView.setVisibility(View.VISIBLE);
        mPopUpView.addItemAction(popItemAction);
        if (popItemAction.getStyle() == PopItemAction.PopItemStyle.Cancel) {
            setCancelable(true);
            setCanceledOnTouchOutside(true);
            mCancelPopItemAction = popItemAction;
        }
    }

    @Override
    public void setIsShowLine(boolean isShowLine) {
        mPopUpView.setIsShowLine(isShowLine);
    }

    @Override
    public void setIsShowCircleBackground(boolean isShow) {
        mPopUpView.setIsShowCircleBackground(isShow);
    }

    @Override
    public void setPopWindowMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContainLayout.getLayoutParams();
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        mContainLayout.setLayoutParams(layoutParams);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    @Override
    public void onStartDismiss(PopWindowInterface popWindowInterface) {
        mPopWindow.onStartDismiss(popWindowInterface);
    }

    @Override
    public void onStartShow(PopWindowInterface popWindowInterface) {
        mPopWindow.onStartShow(popWindowInterface);
    }
}
