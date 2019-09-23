package app.com.pgy.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioButton;

import app.com.pgy.R;

/**
 * Created by YX on 2018/7/13.
 */

public class RatioRadioButton extends RadioButton {
    private float ratio = 0f;
    public RatioRadioButton(Context context) {
        super(context);
    }

    public RatioRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义属性的值，赋值给ratio
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ratio);
        ratio = typedArray.getFloat(R.styleable.Ratio_ratioValue, 0f);
    }

    public RatioRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性的值，赋值给ratio
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ratio);
        ratio = typedArray.getFloat(R.styleable.Ratio_ratioValue, 0f);
    }

    /**
     * MeasureSpec: 测量规则，由size和mode组成 size: 表示传入的具体的值 mode: 用来封装xml中的布局的参数
     * MeasureSpec.AT_MOST: 对应的是wrap_content MeasureSpec.EXACTLY:
     * 对应的是具体的dp值，match_parent MeasureSpec.UNSPECIFIED:
     * 未定义的，一般多用于adapter的VIew测量中
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1.由于widthMeasureSpec是有size和mode组成，则可以反向的从widthMeasureSpec中获取里面的size和mode
        int width = MeasureSpec.getSize(widthMeasureSpec);
//		int mode = MeasureSpec.getMode(widthMeasureSpec);
        // LogUtil.e(this,"spec: "+ widthMeasureSpec+
        // "  width: "+width+"  mode: "+mode);
        // 2.根据ratio和width计算出对应的高度
        if (ratio != 0) {
            float height = width / ratio;// 得到对应的高度
            // 重新组建高度的测量规则，赋值给heightMeasureSpec
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height,
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
