package com.hmy.popwindow.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hmy.popwindow.PopItemAction;
import com.hmy.popwindow.PopWindow;
import com.hmy.popwindow.R;

/**
 * Created by HMY on 2016/9/10.
 */
public class PopItemView extends TextView implements View.OnClickListener {

    private PopItemAction mPopItemAction;
    private PopWindow mPopWindow;

    public PopItemView(Context context, PopItemAction popItemAction, PopWindow windowController) {
        super(context);
        mPopItemAction = popItemAction;
        mPopWindow = windowController;

        int padding = getResources().getDimensionPixelOffset(R.dimen.pop_item_padding);
        setPadding(padding, padding, padding, padding);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (popItemAction != null) {
            if (popItemAction.getStyle() == PopItemAction.PopItemStyle.Normal) {
                setTextColor(getResources().getColor(R.color.pop_item_text_normal_color));
            } else if (popItemAction.getStyle() == PopItemAction.PopItemStyle.Cancel) {
                setTextColor(getResources().getColor(R.color.pop_item_text_normal_color));
                getPaint().setFakeBoldText(true);
            } else if (popItemAction.getStyle() == PopItemAction.PopItemStyle.Warning) {
                setTextColor(getResources().getColor(R.color.pop_item_text_warning_color));
            }
        }
        setGravity(Gravity.CENTER);
        setClickable(true);
        setOnClickListener(this);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelOffset(R.dimen.pop_item_text_size));
        setText(popItemAction.getText());
    }

    @Override
    public void onClick(View view) {
        mPopItemAction.onClick();
        mPopWindow.dismiss();
    }
}
