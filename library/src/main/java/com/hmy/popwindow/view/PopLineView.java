package com.hmy.popwindow.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hmy.popwindow.R;


/**
 * Created by HMY on 2016/9/10.
 */
public class PopLineView extends View {

    public static final String TAG_LINE_VIEW = "tag_line_view";

    public PopLineView(Context context) {
        super(context);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.pop_line_height)));
        setBackgroundResource(R.color.pop_item_bg_pressed);
        setTag(TAG_LINE_VIEW);
    }
}
