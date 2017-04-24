package com.hmy.popwindow;

/**
 * Created by HMY on 2016/9/10.
 */
public class PopItemAction {

    private CharSequence mText;
    private int mTextResId;
    private PopItemStyle mPopItemStyle;
    private OnClickListener mOnClickListener;

    public PopItemAction(CharSequence text) {
        this(text, PopItemStyle.Normal, null);
    }

    public PopItemAction(int textResId) {
        this(textResId, PopItemStyle.Normal, null);
    }

    public PopItemAction(CharSequence text, OnClickListener listener) {
        this(text, PopItemStyle.Normal, listener);
    }

    public PopItemAction(int textResId, OnClickListener listener) {
        this(textResId, PopItemStyle.Normal, listener);
    }


    public PopItemAction(CharSequence text, PopItemStyle style) {
        this(text, style, null);
    }

    public PopItemAction(int textResId, PopItemStyle style) {
        this(textResId, style, null);
    }

    public PopItemAction(CharSequence text, PopItemStyle style, OnClickListener listener) {
        mText = text;
        mPopItemStyle = style;
        mOnClickListener = listener;
    }


    public PopItemAction(int textResId, PopItemStyle style, OnClickListener listener) {
        mTextResId = textResId;
        mPopItemStyle = style;
        mOnClickListener = listener;
    }

    public PopItemStyle getStyle() {
        return mPopItemStyle;
    }

    public CharSequence getText() {
        return mText;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setText(CharSequence text) {
        this.mText = text;
    }


    public void onClick() {
        if (mOnClickListener != null) {
            mOnClickListener.onClick();
        }
    }

    public interface OnClickListener {
        void onClick();
    }

    public enum PopItemStyle {
        Normal, Cancel, Warning
    }
}
