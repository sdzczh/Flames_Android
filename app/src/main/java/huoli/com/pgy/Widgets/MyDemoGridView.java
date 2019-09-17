package huoli.com.pgy.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;


/**
 * Created by YX on 2018/7/26.
 */

public class MyDemoGridView extends GridView {
    OnTouchBlankPositionListener mTouchBlankPosListener;
    public MyDemoGridView(Context context) {
        super(context);
    }

    public MyDemoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDemoGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void setOnTouchBlankPositionListener(OnTouchBlankPositionListener listener) {
        mTouchBlankPosListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(mTouchBlankPosListener != null) {
            if (!isEnabled()) {
                // A disabled view that is clickable still consumes the touch
                // events, it just doesn't respond to them.
                return isClickable() || isLongClickable();
            }

            if(event.getActionMasked() == MotionEvent.ACTION_UP) {
                final int motionPosition = pointToPosition((int)event.getX(), (int)event.getY());
                if( motionPosition == -1 ) {
                    return mTouchBlankPosListener.onTouchBlankPosition();
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public interface OnTouchBlankPositionListener {
        /**
         *
         * @return 是否要终止事件的路由
         */
        boolean onTouchBlankPosition();
    }
}
