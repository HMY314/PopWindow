package com.hmy.popwindow.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hmy.popwindow.PopItemAction;
import com.hmy.popwindow.PopWindow;
import com.hmy.popwindow.demo.R;

public class MainActivity extends Activity {

    private ImageView mArrowIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArrowIv = (ImageView) findViewById(R.id.iv_arrow);
    }


    public void click1(View view) {
        PopWindow popWindow = new PopWindow.Builder(this)
                .setStyle(PopWindow.PopWindowStyle.PopUp)
                .setTitle("注意")
                .setMessage("今天天气不错")
                .addItemAction(new PopItemAction("选项1"))
                .addItemAction(new PopItemAction("选项2", PopItemAction.PopItemStyle.Normal))
                .addItemAction(new PopItemAction("选项3", PopItemAction.PopItemStyle.Normal, new PopItemAction.OnClickListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, "选项3", Toast.LENGTH_SHORT).show();
                    }
                }))
                .addItemAction(new PopItemAction("确定", PopItemAction.PopItemStyle.Warning, new PopItemAction.OnClickListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT).show();
                    }
                }))
                .addItemAction(new PopItemAction("取消", PopItemAction.PopItemStyle.Cancel))
                .create();
        popWindow.show();
    }

    public void click2(View view) {
        View customView = View.inflate(this, R.layout.layout_test, null);
        new PopWindow.Builder(this)
                .setStyle(PopWindow.PopWindowStyle.PopUp)
                .addItemAction(new PopItemAction("选项1"))
                .addContentView(customView)
                .addItemAction(new PopItemAction("选项2"))
                .addItemAction(new PopItemAction("确定", PopItemAction.PopItemStyle.Warning))
                .addItemAction(new PopItemAction("取消", PopItemAction.PopItemStyle.Cancel))
                .show();
    }

    public void click3(View view) {
        View customView = View.inflate(this, R.layout.layout_test, null);
        PopWindow popWindow = new PopWindow(MainActivity.this, "标题", null, PopWindow.PopWindowStyle.PopUp);
        popWindow.setIsShowCircleBackground(false);
        popWindow.setIsShowLine(false);
        popWindow.addItemAction(new PopItemAction("取消", PopItemAction.PopItemStyle.Cancel));
        popWindow.addItemAction(new PopItemAction("选项1"));
        popWindow.addContentView(customView);
        popWindow.addItemAction(new PopItemAction("选项2"));
        popWindow.show();
    }

    public void click4(View view) {
        View customView = View.inflate(this, R.layout.layout_test, null);
        new PopWindow.Builder(this)
                .setStyle(PopWindow.PopWindowStyle.PopUp)
                .addItemAction(new PopItemAction("选项"))//注意这里setVIew的优先级最高，及只要执行了setView，其他添加的view都是无效的
                .setView(customView)
                .show();
    }

    public void click5(View view) {
        View customView = View.inflate(this, R.layout.layout_test, null);
        new PopWindow.Builder(this)
                .setStyle(PopWindow.PopWindowStyle.PopUp)
                .setView(customView)
                .setPopWindowMargins(dip2px(20), dip2px(0), dip2px(20), dip2px(0))
                .show();
    }

    public void click6(View view) {
        View customView = View.inflate(this, R.layout.layout_test, null);
        new PopWindow.Builder(this)
                .setStyle(PopWindow.PopWindowStyle.PopDown)
                .setIsShowCircleBackground(false)
                .addItemAction(new PopItemAction("选项1"))
                .addContentView(customView)
                .addItemAction(new PopItemAction("选项2"))
                .addItemAction(new PopItemAction("取消", PopItemAction.PopItemStyle.Cancel, new PopItemAction.OnClickListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                }))
                .setPopWindowMargins(dip2px(10), dip2px(0), dip2px(10), dip2px(0))
                .setControlViewAnim(mArrowIv, R.anim.btn_rotate_anim_1, R.anim.btn_rotate_anim_2, true)
                .show(view);
    }

    public void click7(View view) {
        View customView = View.inflate(this, R.layout.layout_test, null);
        new PopWindow.Builder(this)
                .setStyle(PopWindow.PopWindowStyle.PopDown)
                .setView(customView)
                .show(view);
    }

    public void click8(View view) {
        View customView = View.inflate(this, R.layout.layout_test, null);
        new PopWindow.Builder(this)
                .setStyle(PopWindow.PopWindowStyle.PopAlert)
                .setMessage("今天天气不错")
                .setIsShowCircleBackground(false)
//                .setIsShowLine(false)
                .addItemAction(new PopItemAction("选项1"))
                .addContentView(customView)
                .addItemAction(new PopItemAction("选项2"))
                .addItemAction(new PopItemAction("取消", PopItemAction.PopItemStyle.Cancel))
                .setPopWindowMargins(dip2px(30), dip2px(0), dip2px(30), dip2px(0))
                .show();
    }

    public void click9(View view) {
        View customView = View.inflate(this, R.layout.layout_test, null);
        new PopWindow.Builder(this)
                .setStyle(PopWindow.PopWindowStyle.PopAlert)
                .setView(customView)
                .show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
