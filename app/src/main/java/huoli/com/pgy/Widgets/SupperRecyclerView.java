package huoli.com.pgy.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jude.easyrecyclerview.EasyRecyclerView;

/**
 * 解决BottomSheet与RecyclerView的滑动冲突
 * @author xuqingzhong
 */

public class SupperRecyclerView extends EasyRecyclerView {

    public SupperRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupperRecyclerView(Context context) {
        super(context);
    }


    //重写这个方法，并且在方法里面请求所有的父控件都不要拦截他的事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}

