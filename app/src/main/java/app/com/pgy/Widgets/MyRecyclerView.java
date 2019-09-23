package app.com.pgy.Widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import app.com.pgy.Utils.LogUtils;

/**
 * 创建日期：2018/7/9 0009 on 下午 4:37
 * 描述:
 *
 * @author xu
 */

public class MyRecyclerView extends RecyclerView {
    private static final String TAG = "MyRecyclerView";
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }else {
            LogUtils.w(TAG, "ev:" + ev.getAction());
            // 自己不处理事件，把ViewPager默认的滑动事件干掉
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        LogUtils.w(TAG,"arg:"+arg0.getAction());
        if (arg0.getAction() == MotionEvent.ACTION_DOWN){
            return true;
        }else {
            return true;// 不拦截孩子的事件
        }
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.w(TAG,"dis:"+ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }else {
            return false;
        }
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
