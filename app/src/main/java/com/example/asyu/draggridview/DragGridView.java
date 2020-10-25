package com.example.asyu.draggridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class DragGridView extends GridView implements AdapterView.OnItemLongClickListener {

    private ImageView dragIiewView;//ImageView，图像容器
    private WindowManager windowManager;//窗口
    private WindowManager.LayoutParams dragParams;//用于记录窗口展示的位置
    private int oldPos;//用于记录拖动的item的position
    private int rawX;//用于记录初始坐标X
    private int rawY;//用于记录初始坐标Y
    private boolean isDrag;//用来判断当前是否是在拖动的状态

    public DragGridView(Context context) {
        super(context);
        initView();
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOnItemLongClickListener(this);
        dragIiewView = new ImageView(getContext());
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        dragParams = new WindowManager.LayoutParams();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //记下item的position
        oldPos = i;
        //获取长按item的DrawingCache
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        //获取item的Bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        //设置拖动参数、以及设置显示出来可以拖动的item放大1.2倍
        dragParams.gravity = Gravity.TOP | Gravity.START;
        dragParams.width = (int) (1.2f * bitmap.getWidth());
        dragParams.height = (int) (1.2f * bitmap.getHeight());
        //获取拖动的中心点
        dragParams.x = rawX - dragParams.width / 2;
        dragParams.y = rawY - dragParams.height / 2;
        dragParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        dragParams.format = PixelFormat.TRANSLUCENT;
        dragParams.windowAnimations = 0;
        //设置ImageView的图像为本次长按的item的图像
        dragIiewView.setImageBitmap(bitmap);
        //将ImageView显示到屏幕上
        windowManager.addView(dragIiewView, dragParams);
        //设置当前状态为true
        isDrag = true;
        //设置长按的item隐藏
        ((DragGridViewAdapter) getAdapter()).hideView(i);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指触碰屏幕时，记录下坐标
            rawX = (int) ev.getRawX();
            rawY = (int) ev.getRawY();
        } else if (isDrag && ev.getAction() == MotionEvent.ACTION_MOVE) {
            //item被拖动时,记录拖动过程中的（当前）坐标
            dragParams.x = (int) (ev.getRawX() - dragIiewView.getWidth() / 2);
            dragParams.y = (int) (ev.getRawY() - dragIiewView.getHeight() / 2);
            //更新窗口显示
            windowManager.updateViewLayout(dragIiewView, dragParams);
            //获取拖动过程中，触摸点所在GridView中item的位置（position）
            int newPos = pointToPosition(((int) ev.getX()), (int) ev.getY());
            //如果当前位置不等于上次停留的位置，则交换两次位置的item
            if (newPos != AdapterView.INVALID_POSITION && newPos != oldPos) {
                ((DragGridViewAdapter) getAdapter()).updataView(oldPos, newPos);
                oldPos = newPos;
            }
        } else if (isDrag && ev.getAction() == MotionEvent.ACTION_UP) {
            //操作停止时（手指离开屏幕）,回归正常显示状态
            ((DragGridViewAdapter) getAdapter()).showHideView();
            //清空窗口
            windowManager.removeView(dragIiewView);
            //状态改成false
            isDrag = false;
        }
        return super.onTouchEvent(ev);
    }
}