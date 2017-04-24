# PopWindow
对Android的底部弹窗、顶部弹窗菜单及自定义界面的使用封装。

CSDN：[http://blog.csdn.net/hmyang314/article/details/70613478](http://blog.csdn.net/hmyang314/article/details/70613478)

![](https://github.com/HMY314/PopWindow/blob/master/imageCache/gif1.gif)
![](https://github.com/HMY314/PopWindow/blob/master/imageCache/gif2.gif)

----------
## 一、介绍
    主要是用于在弹窗中显示一些有限的菜单项，也支持添加自定义View，有如下3中弹出方式：
        1、底部弹出，主要参考默认风格是参考IOS的UIAlertController，可以添加自定义View；
        2、底部弹出，从顶部向下弹出的效果，同时支持控制按钮执行动画，默认是旋转动画；
        3、屏幕中间弹出

---
## 二、核心类PopWindow

    PopWindow采用Build模式，可以像AlertDialog一样方便的使用，通过PopWindowStyle控制弹窗的显示方式：

    public enum PopWindowStyle {
        PopUp, PopDown, PopAlert
    }

    控制不同的弹窗的主要类是：PopUpWindow、PopDownWindow、PopAlertDialog

----------
## 三、使用方法

第一种

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

  效果图：
 ![](https://github.com/HMY314/PopWindow/blob/master/imageCache/img1.png)


第二种

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

效果图：
 ![](https://github.com/HMY314/PopWindow/blob/master/imageCache/img4.png)

#### 更多效果请看代码。

---
## 四、使用封装

    在demo中有个PopWindowController类，是对一些特殊的使用进行了再次封装，大家可以根据自己的实际使用情况进行封装。

    public class PopWindowController {

    public static PopWindow showPopDownList(Activity activity, BaseAdapter adapter, View controlView, View rotateView,
                                            int[] margins, AdapterView.OnItemClickListener listener) {
        return showPopDownList(activity, adapter, controlView, rotateView, true, margins, listener);
    }

    public static PopWindow showPopDownList(Activity activity, BaseAdapter adapter, View controlView, View rotateView,
                                            boolean rotateViewAnim, int[] margins, AdapterView.OnItemClickListener listener) {
        Animation openAnim = AnimationUtils.loadAnimation(activity, R.anim.btn_rotate_anim_1);
        Animation closeAnim = AnimationUtils.loadAnimation(activity, R.anim.btn_rotate_anim_2);
        return showPopDownList(activity, adapter, controlView, rotateView, openAnim, closeAnim, rotateViewAnim, margins, listener);
    }

    public static PopWindow showPopDownList(Activity activity, BaseAdapter adapter, View controlView, View rotateView, int openAnimId,
                                            int closeAnimId, boolean rotateViewAnim, int[] margins,
                                            AdapterView.OnItemClickListener listener) {
        Animation openAnim = AnimationUtils.loadAnimation(activity, openAnimId);
        Animation closeAnim = AnimationUtils.loadAnimation(activity, closeAnimId);
        return showPopDownList(activity, adapter, controlView, rotateView, openAnim, closeAnim, rotateViewAnim, margins, listener);
    }

    /**
     * 以ListView方式向下弹出框
     *
     * @param activity
     * @param adapter
     * @param controlView    操作显示出弹框的按钮
     * @param rotateView     要旋转的按钮
     * @param openAnim       旋转按钮开启动画
     * @param closeAnim      旋转按钮关闭动画
     * @param rotateViewAnim 是否显示旋转动画
     * @param margins        margins[0]:leftMargin、margins[1]:topMargin、margins[2]:rightMargin、margins[3]:bottomMargin
     * @param listener
     * @return
     */
    public static PopWindow showPopDownList(Activity activity, final BaseAdapter adapter, View controlView, View rotateView,
                                            Animation openAnim, Animation closeAnim, boolean rotateViewAnim, int[] margins,
                                            AdapterView.OnItemClickListener listener) {
        final ListView listView = new ListView(activity);
        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        listView.setDivider(null);
        listView.setOnItemClickListener(listener);
        listView.setAdapter(adapter);

        int leftMargin = 0, topMargin = 0, rightMargin = 0, bottomMargin = 0;
        if (margins != null) {
            for (int i = 0; i < margins.length; i++) {
                if (i == 0) {
                    leftMargin = margins[i];
                } else if (i == 1) {
                    topMargin = margins[i];
                } else if (i == 2) {
                    rightMargin = margins[i];
                } else if (i == 3) {
                    bottomMargin = margins[i];
                }
            }
        }

        PopWindow popWindow = new PopWindow.Builder(activity)
                .setStyle(PopWindow.PopWindowStyle.PopDown)
                .setView(listView)
                .setControlViewAnim(rotateView, openAnim, closeAnim, rotateViewAnim)
                .setPopWindowMargins(leftMargin, topMargin, rightMargin, bottomMargin)
                .create();
        popWindow.show(controlView);
        return popWindow;
    }

}