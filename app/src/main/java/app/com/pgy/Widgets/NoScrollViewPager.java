package app.com.pgy.Widgets;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
	public NoScrollViewPager(Context context) {
		super(context);
	}


	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;// 自己不处理事件，把ViewPager默认的滑动事件干掉
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;// 不拦截孩子的事件
	}

}
