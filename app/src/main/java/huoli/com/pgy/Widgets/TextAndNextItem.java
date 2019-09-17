package huoli.com.pgy.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import huoli.com.pgy.R;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:
 * 自定义TextAndNextItem显示信息的Item
 * 布局：左侧为文字，右侧为Next>图标
 * 功能：设置左侧文本
 *       整个Item的点击监听
 * @author 徐庆重
 */
public class TextAndNextItem extends LinearLayout implements View.OnClickListener {
    /**右侧文字*/
    private TextView textAndNextItemText;
    /**左侧文字*/
    private TextView textAndNextItemRightText;

    private OnClickListener l;

    public TextAndNextItem(Context context) {
        super(context);
        init(null);
    }

    public TextAndNextItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextAndNextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_textandnextitem, this);
        textAndNextItemText = findViewById(R.id.textandnext_item_text);
        textAndNextItemRightText = findViewById(R.id.textandnext_item_rightText);

        this.setOnClickListener(this);
        //判断attrs为空的情况，即只有一个参数的构造函数
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextAndNextItemView);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = a.getIndex(i);
            //获取用户自定义的属性值，并设置为空时的默认值
            switch (index) {
                case R.styleable.TextAndNextItemView_tan_text:
                    /*给左边文本设置文字*/
                    textAndNextItemText.setText(a.getString(index));
                    break;
                case R.styleable.TextAndNextItemView_tan_right_text:
                    /*给右侧文本设置文字*/
                    textAndNextItemRightText.setText(a.getString(index));
                default:
                    break;
            }
        }
        a.recycle();
    }

    /**设置中间文本显示的内容*/
    public void setText(String show) {
        textAndNextItemText.setText(TextUtils.isEmpty(show)?"":show);
    }

    /**设置右侧文本显示的内容*/
    public void setRightText(String show) {
        textAndNextItemRightText.setText(TextUtils.isEmpty(show)?"":show);
    }

    @Override
    public void onClick(View v) {
        if (l != null) {
            //整个控件的点击事件
            l.onClick(this);
        }
    }
}
