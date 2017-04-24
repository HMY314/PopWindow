package com.hmy.popwindow.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hmy.popwindow.PopItemAction;
import com.hmy.popwindow.PopWindow;
import com.hmy.popwindow.PopWindowHelper;
import com.hmy.popwindow.R;
import com.hmy.popwindow.viewinterface.PopViewInterface;

/**
 * Created by HMY on 2016/9/10.
 */
public class PopUpView extends LinearLayout implements PopViewInterface {

    private TextView mTitleTv;
    private LinearLayout mContanierLl;

    private int mTitleColor;
    private int mTitleTextSize;
    private int mMessageColor;
    private int mMessageTextSize;

    private PopWindow mPopWindow;
    private PopItemView mCancelItemView;
    private View mContentView;

    private boolean mIsFirstShow = true;
    private boolean mIsShowLine = true;
    private boolean mIsShowCircleBackground = true;

    public PopUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        mTitleTv = new TextView(context);
        mTitleTv.setGravity(Gravity.CENTER);
        mTitleTv.setBackgroundResource(android.R.color.transparent);
        int padding = getResources().getDimensionPixelOffset(R.dimen.pop_item_padding);
        mTitleTv.setPadding(padding * 2, padding, padding * 2, padding);
        mTitleTv.setClickable(true);

        mContanierLl = new LinearLayout(getContext());
        mContanierLl.setOrientation(VERTICAL);
        mContanierLl.setBackgroundResource(R.drawable.pop_shape_bg);
        mContanierLl.addView(mTitleTv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        addView(mContanierLl, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        initTextAndMessage();
    }

    private void initTextAndMessage() {
        mTitleColor = getResources().getColor(R.color.pop_action_sheet_title);
        mTitleTextSize = getResources().getDimensionPixelOffset(R.dimen.pop_action_sheet_text_size_title);
        mMessageColor = getResources().getColor(R.color.pop_action_sheet_message);
        mMessageTextSize = getResources().getDimensionPixelOffset(R.dimen.pop_action_sheet_text_size_message);
    }

    @Override
    public void setTitleColor(int color) {
        mTitleColor = color;
    }

    @Override
    public void setTitleTextSize(int textSize) {
        mTitleTextSize = textSize;
    }

    @Override
    public void setMessageColor(int color) {
        mMessageColor = color;
    }

    @Override
    public void setMessageTextSize(int textSize) {
        mMessageTextSize = textSize;
    }

    @Override
    public void setTitleAndMessage(CharSequence title, CharSequence message) {
        PopWindowHelper.setTitleAndMessage(mTitleTv, mTitleColor, mTitleTextSize, mMessageColor, mMessageTextSize, title, message);
    }

    @Override
    public void addContentView(View view) {
        mContentView = view;
        addLineView();
        mContanierLl.addView(mContentView);
    }

    @Override
    public void setPopWindow(PopWindow popWindow) {
        mPopWindow = popWindow;
    }

    @Override
    public void addItemAction(PopItemAction popItemAction) {
        PopItemView popItemView = new PopItemView(getContext(), popItemAction, mPopWindow);
        if (popItemAction.getStyle() == PopItemAction.PopItemStyle.Cancel) {
            if (mCancelItemView == null) {
                mCancelItemView = popItemView;
                if (mIsShowCircleBackground) {
                    mCancelItemView.setBackgroundResource(R.drawable.pop_selector_cancel);
                } else {
                    mCancelItemView.setBackgroundResource(R.drawable.pop_selector_cancel_square);
                }
                MarginLayoutParams params = (MarginLayoutParams) mCancelItemView.getLayoutParams();
                params.topMargin = getResources().getDimensionPixelOffset(R.dimen.pop_item_padding);
                addView(popItemView);
            } else {
                throw new RuntimeException("PopWindow 只能添加一个取消操作");
            }
        } else {
            addLineView();
            mContanierLl.addView(popItemView);
        }
    }

    @Override
    public boolean showAble() {
        return mCancelItemView != null || mContanierLl.getChildCount() > 1;
    }

    @Override
    public void refreshBackground() {
        if (mIsFirstShow) {
            mIsFirstShow = false;
            PopWindowHelper.refreshBackground(mContanierLl, mIsShowCircleBackground);
        }
    }

    @Override
    public void setIsShowLine(boolean isShowLine) {
        mIsShowLine = isShowLine;
    }

    @Override
    public void setIsShowCircleBackground(boolean isShow) {
        mIsShowCircleBackground = isShow;
        if (!isShow) {
            mContanierLl.setBackgroundColor(getContext().getResources().getColor(R.color.pop_bg_content));
            if (mCancelItemView != null) {
                mCancelItemView.setBackgroundResource(R.drawable.pop_selector_cancel);
            }
        }
    }

    private void addLineView() {
        if (mIsShowLine || mContanierLl.getChildCount() == 0) {//第一根始终都要添加，为了在最后refreshBackground时保证第一个是LineView
            mContanierLl.addView(new PopLineView(getContext()));
        }
    }

}
