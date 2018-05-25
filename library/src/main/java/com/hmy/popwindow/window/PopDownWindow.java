package com.hmy.popwindow.window;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hmy.popwindow.PopItemAction;
import com.hmy.popwindow.PopSimpleAnimationListener;
import com.hmy.popwindow.PopWindow;
import com.hmy.popwindow.R;
import com.hmy.popwindow.view.PopDownView;
import com.hmy.popwindow.viewinterface.PopWindowInterface;

import java.util.TimerTask;

/**
 * Created by HMY on 2016/9/10.
 */
public class PopDownWindow extends PopupWindow implements PopWindowInterface, View.OnClickListener,
        PopWindowInterface.OnStartShowListener, PopWindowInterface.OnStartDismissListener {

    private Activity mActivity;
    private View mPopWindowLayout;
    private FrameLayout mRootLayout;
    private FrameLayout mContainLayout;
    private PopDownView mPopDownView;
    private LinearLayout mContentLayout;
    private PopWindow mPopWindow;

    private Animation mAlphaOpenAnimation;
    private Animation mAlphaCloseAnimation;
    private Animation mPopOpenAnimation;
    private Animation mPopCloseAnimation;

    private boolean mIsDismissed = true;

    private View mCustomView;

    public PopDownWindow(Activity activity, int titleResId, int messageResId, PopWindow popWindow) {
        this(activity, titleResId == 0 ? null : activity.getString(titleResId), messageResId == 0 ? null : activity.getString(messageResId), popWindow);
    }

    public PopDownWindow(Activity activity, CharSequence title, CharSequence message, PopWindow popWindow) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopWindowLayout = inflater.inflate(R.layout.pop_down_window, null);
        this.setContentView(mPopWindowLayout);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.PopDownWindow);

        mActivity = activity;
        mPopWindow = popWindow;

        initRootView(mPopWindowLayout);
        initContentView(mPopWindowLayout, title, message);
        setListener();
        initAnim();
    }

    private void initRootView(View view) {
        mRootLayout = (FrameLayout) view.findViewById(R.id.layout_root);
        mRootLayout.setOnClickListener(this);
    }

    private void initContentView(View view, CharSequence title, CharSequence message) {
        mPopDownView = (PopDownView) view.findViewById(R.id.popDownView);
        mPopDownView.setPopWindow(mPopWindow);
        mPopDownView.setTitleAndMessage(title, message);

        mContentLayout = (LinearLayout) view.findViewById(R.id.layout_top);
        mContainLayout = (FrameLayout) view.findViewById(R.id.layout_contain);
    }

    private void setListener() {
        mPopWindowLayout.setFocusable(true);
        mPopWindowLayout.setFocusableInTouchMode(true);

        mPopWindowLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });

        mPopWindowLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void initAnim() {
        mPopOpenAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation
                .RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        mPopOpenAnimation.setDuration(mActivity.getResources().getInteger(R.integer.pop_animation_duration));
        mPopCloseAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mPopCloseAnimation.setDuration(mActivity.getResources().getInteger(R.integer.pop_animation_duration));
        mPopCloseAnimation.setFillAfter(true);

        mPopOpenAnimation.setAnimationListener(new PopSimpleAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsDismissed = false;
                mRootLayout.startAnimation(mAlphaOpenAnimation);
            }
        });

        mPopCloseAnimation.setAnimationListener(new PopSimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (mCustomView != null) {
                    mContentLayout.post(mDismissRunnable);
                } else
                    mPopDownView.post(mDismissRunnable);
            }

            @Override
            public void onAnimationStart(Animation animation) {
                mIsDismissed = true;
                onStartDismiss(PopDownWindow.this);
                mRootLayout.startAnimation(mAlphaCloseAnimation);
            }
        });

        mAlphaOpenAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.pop_alpha_enter);
        mAlphaCloseAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.pop_alpha_exit);
    }

    private Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
            PopDownWindow.super.dismiss();
        }
    };

    public void show(View view) {
        if (mIsDismissed) {
            onStartShow(PopDownWindow.this);
            if (mCustomView != null) {
                mContentLayout.startAnimation(mPopOpenAnimation);
            } else if (mPopDownView.showAble()) {
                mPopDownView.refreshBackground();
                mPopDownView.startAnimation(mPopOpenAnimation);
            } else {
                throw new RuntimeException("必须至少添加一个PopItemView");
            }
            
            if (Build.VERSION.SDK_INT < 24) {
                showAsDropDown(view);
            } else {
                Rect visibleFrame = new Rect();
                view.getGlobalVisibleRect(visibleFrame);
                int height = view.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                setHeight(height);
                showAsDropDown(view, 0, 0);
            }
        }
    }

    @Override
    public void dismiss() {
        executeExitAnim();
    }

    private void executeExitAnim() {
        if (!mIsDismissed) {
            if (mCustomView != null) {
                mContentLayout.startAnimation(mPopCloseAnimation);
            } else
                mPopDownView.startAnimation(mPopCloseAnimation);
        }
    }

    @Override
    public void addContentView(View view) {
        view.setClickable(true);
        mPopDownView.addContentView(view);
    }

    @Override
    public void setView(View view) {
        view.setClickable(true);
        mCustomView = view;
        mContentLayout.setVisibility(View.VISIBLE);
        mPopDownView.setVisibility(View.GONE);
        mContentLayout.addView(mCustomView);
    }

    @Override
    public void addItemAction(PopItemAction popItemAction) {
        if (mCustomView != null) {
            return;
        }
        mContentLayout.setVisibility(View.GONE);
        mPopDownView.setVisibility(View.VISIBLE);
        mPopDownView.addItemAction(popItemAction);
    }

    @Override
    public void setIsShowLine(boolean isShowLine) {
        mPopDownView.setIsShowLine(isShowLine);
    }

    @Override
    public void setIsShowCircleBackground(boolean isShow) {
        mPopDownView.setIsShowCircleBackground(isShow);
        if (!isShow) {
            mPopDownView.setBackgroundColor(mActivity.getResources().getColor(R.color.pop_bg_content));
            mPopDownView.post(new TimerTask() {
                @Override
                public void run() {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPopDownView.getLayoutParams();
                    layoutParams.topMargin = 0;
                }
            });
        }
    }

    @Override
    public void setPopWindowMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContainLayout.getLayoutParams();
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        mContainLayout.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_root) {
            dismiss();
        }
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
