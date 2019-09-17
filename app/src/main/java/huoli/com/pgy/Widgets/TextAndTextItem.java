package huoli.com.pgy.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import huoli.com.pgy.R;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:
 * 自定义TextAndTextItem显示信息的Item
 * 布局：左侧为文字，右侧为文字
 *
 * @author 徐庆重
 */
public class TextAndTextItem extends LinearLayout {
    /**
     * 左侧文字、问题图片
     */
    @BindView(R.id.layout_widget_textAndText_leftText)
    TextView layoutWidgetTextAndTextLeftText;
    @BindView(R.id.layout_widget_textAndText_leftImage)
    ImageView layoutWidgetTextAndTextLeftImage;
    /**
     * 右侧文字、图片
     */
    @BindView(R.id.layout_widget_textAndText_rightText)
    TextView layoutWidgetTextAndTextRightText;
    @BindView(R.id.layout_widget_textAndText_rightImage)
    ImageView layoutWidgetTextAndTextRightImage;

    @BindView(R.id.layout_widget_textAndText_copy)
    TextView layoutWidgetTextAndTextCopy;
    public TextAndTextItem(Context context) {
        super(context);
        init(null);
    }

    public TextAndTextItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextAndTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View rootFrame = LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_textandtext, this);
        ButterKnife.bind(this, rootFrame);
        //判断attrs为空的情况，即只有一个参数的构造函数
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextAndTextItem);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = a.getIndex(i);
            //获取用户自定义的属性值，并设置为空时的默认值
            switch (index){
                case R.styleable.TextAndTextItem_tatv_leftImg_visibility:
                    //给左侧问题按钮设置是否显示，默认为不显示
                    setVisibility(layoutWidgetTextAndTextLeftImage,a.getInt(index,2));
                    break;
                case R.styleable.TextAndTextItem_tatv_left_txt:
                    //给左侧设置文本
                    layoutWidgetTextAndTextLeftText.setText(a.getString(index));
                    break;
                case R.styleable.TextAndTextItem_tatv_rightImg_visibility:
                    //给右侧图标设置是否显示，默认为消失
                    setVisibility(layoutWidgetTextAndTextRightImage,a.getInt(index,2));
                    break;
                case R.styleable.TextAndTextItem_tatv_right_txt:
                    //给右侧设置文本
                    layoutWidgetTextAndTextRightText.setText(a.getString(index));
                    break;
                case R.styleable.TextAndTextItem_tatv_right_img:
                    /*给右侧图片设置资源*/
                    layoutWidgetTextAndTextRightImage.setImageResource(a.getResourceId(index,R.mipmap.shuaxin));
                    break;
                case R.styleable.TextAndTextItem_tatv_copy_visibility:
                    //给左侧问题按钮设置是否显示，默认为不显示
                    setVisibility(layoutWidgetTextAndTextCopy,a.getInt(index,2));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    private void setVisibility(View view, int visable){
        switch (visable){
            case 0:
                view.setVisibility(View.VISIBLE);
                break;
            case 1:
                view.setVisibility(View.INVISIBLE);
                break;
            case 2:
                view.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 设置左侧文本显示的内容
     */
    public void setLeftText(String leftTxt) {
        layoutWidgetTextAndTextLeftText.setText(TextUtils.isEmpty(leftTxt) ? "" : leftTxt);
    }

    /**
     * 设置右侧文本显示的内容
     */
    public void setRightText(String rightTxt) {
        layoutWidgetTextAndTextRightText.setText(TextUtils.isEmpty(rightTxt) ? "" : rightTxt);
    }

    public String getRightText(){
        return layoutWidgetTextAndTextRightText.getText().toString().trim();
    }

    /**设置右侧图标显示*/
    public void setRightImageVisible(int visible){
        layoutWidgetTextAndTextRightImage.setVisibility(visible);
    }

    //添加返回按钮的点击事件
    public void setLeftImageClickListener(OnClickListener listener){
        layoutWidgetTextAndTextLeftImage.setOnClickListener(listener);
    }

    //添加右侧按钮或文本的点击事件
    public void setRightClickListener(OnClickListener listener){
        layoutWidgetTextAndTextRightImage.setOnClickListener(listener);
    }
    //添加复制按钮点击事件
    public void setCopyClickListener(OnClickListener listener){
        layoutWidgetTextAndTextCopy.setOnClickListener(listener);
    }

}
