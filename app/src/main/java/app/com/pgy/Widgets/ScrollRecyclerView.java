package app.com.pgy.Widgets;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import app.com.pgy.Utils.LogUtils;

/**
 * 创建日期：2018/7/18 0018 on 下午 6:34
 * 描述:
 *
 * @author xu
 */

public class ScrollRecyclerView extends RecyclerView {
    private static final String TAG = "ScrollRecyclerView";
    public ScrollRecyclerView(Context context) {
        super(context);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean parentIsIntercept = super.onInterceptTouchEvent(ev);
        LogUtils.w(TAG,"Intercept:"+parentIsIntercept);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
                case MotionEvent.ACTION_MOVE:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
                default:break;
        }
        boolean parentDispatch = super.dispatchTouchEvent(ev);
        LogUtils.w(TAG,"dispatch:"+parentDispatch);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        return super.onTouchEvent(e);
    }
}
