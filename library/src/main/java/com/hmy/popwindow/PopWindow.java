package com.hmy.popwindow;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hmy.popwindow.viewinterface.PopWindowInterface;
import com.hmy.popwindow.window.PopAlertDialog;
import com.hmy.popwindow.window.PopDownWindow;
import com.hmy.popwindow.window.PopUpWindow;

/**
 * Created by HMY on 2016/9/10.
 */
public class PopWindow implements PopWindowInterface,
        PopWindowInterface.OnStartShowListener, PopWindowInterface.OnStartDismissListener {

    private Activity mActivity;

    private CharSequence mTitleText;
    private CharSequence mMessageText;
    private PopWindowStyle mStyle = PopWindowStyle.PopUp;
    private View mCustomView;
    private View mContentView;

    private PopUpWindow mPopUpWindow;
    private PopDownWindow mPopDownWindow;
    private PopAlertDialog mPopAlertDialog;

    private View mControlView = null;
    private Animation mControlViewOpenAnimation;
    private Animation mControlViewCloseAnimation;
    private boolean mIsShowControlViewAnim;

    public PopWindow(Activity activity, int titleResId, int messageResId, PopWindowStyle style) {
        this(activity, titleResId == 0 ? null : activity.getString(titleResId), messageResId == 0 ? null : activity.getString(messageResId), style);
    }

    public PopWindow(Activity activity, CharSequence title, CharSequence message, PopWindowStyle style) {
        mActivity = activity;
        setTitle(title);
        setMessage(message);
        setStyle(style);
        initPopWindow(activity, title, message);
    }

    public PopWindow(Activity activity) {
        mActivity = activity;
        initPopWindow(activity, null, null);
    }

    private void initPopWindow(Activity activity, CharSequence title, CharSequence message) {
        if (mStyle == PopWindowStyle.PopUp) {
            mPopUpWindow = new PopUpWindow(activity, title, message, this);
        } else if (mStyle == PopWindowStyle.PopDown) {
            mPopDownWindow = new PopDownWindow(activity, title, message, this);
        } else if (mStyle == PopWindowStyle.PopAlert) {
            mPopAlertDialog = new PopAlertDialog(activity, title, message, this);
        }
    }

    public void setTitle(CharSequence title) {
        mTitleText = title;
    }

    public void setMessage(CharSequence message) {
        mMessageText = message;
    }

    public void setStyle(PopWindowStyle style) {
        mStyle = style;
    }

    @Override
    public void setView(View view) {
        mCustomView = view;
        if (view != null) {
            if (mPopUpWindow != null) {
                mPopUpWindow.setView(view);
            }
            if (mPopDownWindow != null) {
                mPopDownWindow.setView(view);
            }
            if (mPopAlertDialog != null) {
                mPopAlertDialog.setView(view);
            }
        }
    }

    @Override
    public void addContentView(View view) {
        mContentView = view;
        if (mPopUpWindow != null) {
            mPopUpWindow.addContentView(view);
        }
        if (mPopDownWindow != null) {
            mPopDownWindow.addContentView(view);
        }
        if (mPopAlertDialog != null) {
            mPopAlertDialog.addContentView(view);
        }
    }

    @Override
    public void setIsShowLine(boolean isShowLine) {
        if (mPopUpWindow != null) {
            mPopUpWindow.setIsShowLine(isShowLine);
        }
        if (mPopDownWindow != null) {
            mPopDownWindow.setIsShowLine(isShowLine);
        }
        if (mPopAlertDialog != null) {
            mPopAlertDialog.setIsShowLine(isShowLine);
        }
    }

    @Override
    public void setIsShowCircleBackground(boolean isShow) {
        if (mPopUpWindow != null) {
            mPopUpWindow.setIsShowCircleBackground(isShow);
        }
        if (mPopDownWindow != null) {
            mPopDownWindow.setIsShowCircleBackground(isShow);
        }
        if (mPopAlertDialog != null) {
            mPopAlertDialog.setIsShowCircleBackground(isShow);
        }
    }

    @Override
    public void setPopWindowMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        if (mPopUpWindow != null) {
            mPopUpWindow.setPopWindowMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        }
        if (mPopDownWindow != null) {
            mPopDownWindow.setPopWindowMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        }
        if (mPopAlertDialog != null) {
            mPopAlertDialog.setPopWindowMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        }
    }

    @Override
    public void onStartDismiss(PopWindowInterface popWindowInterface) {
        if (mIsShowControlViewAnim) {
            mControlView.startAnimation(mControlViewCloseAnimation);
        }
    }

    @Override
    public void onStartShow(PopWindowInterface popWindowInterface) {
        if (mIsShowControlViewAnim) {
            mControlView.startAnimation(mControlViewOpenAnimation);
        }
    }

    /**
     * 设置控制控件的动画
     *
     * @param view       控制控件
     * @param openAnim   打开动画
     * @param closeAnim  关闭动画
     * @param isShowAnim 是否显示动画
     */
    public void setControlViewAnim(View view, Animation openAnim, Animation closeAnim, boolean isShowAnim) {
        mControlView = view;
        openAnim.setFillAfter(true);
        closeAnim.setFillAfter(true);
        mControlViewOpenAnimation = openAnim;
        mControlViewCloseAnimation = closeAnim;
        mIsShowControlViewAnim = isShowAnim;
    }

    /**
     * @param view        控制控件
     * @param openAnimId  打开动画id
     * @param closeAnimId 关闭动画id
     * @param isShowAnim  是否显示动画
     */
    public void setControlViewAnim(View view, int openAnimId, int closeAnimId, boolean isShowAnim) {
        Animation openAnim = AnimationUtils.loadAnimation(mActivity, openAnimId);
        Animation closeAnim = AnimationUtils.loadAnimation(mActivity, closeAnimId);
        setControlViewAnim(view, openAnim, closeAnim, isShowAnim);
    }

    public static class Builder {

        private Activity activity;
        private CharSequence title;
        private CharSequence message;
        private PopWindowStyle style = PopWindowStyle.PopUp;
        private PopWindow popWindow;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setTitle(int titleResId) {
            this.title = activity.getString(titleResId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(int messageResId) {
            this.message = activity.getString(messageResId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setStyle(PopWindowStyle style) {
            this.style = style;
            return this;
        }

        public Builder setView(View view) {
            create().setView(view);
            return this;
        }

        public Builder addContentView(View view) {
            create().addContentView(view);
            return this;
        }

        public Builder setIsShowLine(boolean isShowLine) {
            create().setIsShowLine(isShowLine);
            return this;
        }

        public Builder setIsShowCircleBackground(boolean isShow) {
            create().setIsShowCircleBackground(isShow);
            return this;
        }

        public Builder addItemAction(PopItemAction popItemAction) {
            create().addItemAction(popItemAction);
            return this;
        }

        public Builder setPopWindowMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
            create().setPopWindowMargins(leftMargin, topMargin, rightMargin, bottomMargin);
            return this;
        }

        public Builder setControlViewAnim(View view, Animation openAnim, Animation closeAnim, boolean isShowAnim) {
            create().setControlViewAnim(view, openAnim, closeAnim, isShowAnim);
            return this;
        }

        public Builder setControlViewAnim(View view, int openAnimId, int closeAnimId, boolean isShowAnim) {
            create().setControlViewAnim(view, openAnimId, closeAnimId, isShowAnim);
            return this;
        }

        public PopWindow create() {
            if (popWindow == null) {
                popWindow = new PopWindow(activity, title, message, style);
            }
            return popWindow;
        }

        public PopWindow show(View view) {
            create();
            popWindow.show(view);
            return popWindow;
        }

        public PopWindow show() {
            return show(null);
        }

    }

    public void show() {
        show(null);
    }

    public void show(View view) {
        if (mPopUpWindow != null) {
            mPopUpWindow.show();
        }
        if (mPopDownWindow != null) {
            mPopDownWindow.show(view);
        }
        if (mPopAlertDialog != null) {
            mPopAlertDialog.show();
        }
    }

    public void dismiss() {
        if (mPopUpWindow != null) {
            mPopUpWindow.dismiss();
        }
        if (mPopDownWindow != null) {
            mPopDownWindow.dismiss();
        }
        if (mPopAlertDialog != null) {
            mPopAlertDialog.dismiss();
        }
    }

    @Override
    public void addItemAction(PopItemAction popItemAction) {
        if (popItemAction == null) {
            return;
        }
        if (popItemAction.getTextResId() != 0) {
            popItemAction.setText(mActivity.getString(popItemAction.getTextResId()));
        }

        if (mStyle == PopWindowStyle.PopUp) {
            mPopUpWindow.addItemAction(popItemAction);
        } else if (mStyle == PopWindowStyle.PopDown) {
            mPopDownWindow.addItemAction(popItemAction);
        } else if (mStyle == PopWindowStyle.PopAlert) {
            mPopAlertDialog.addItemAction(popItemAction);
        }
    }

    public enum PopWindowStyle {
        PopUp, PopDown, PopAlert
    }
}
