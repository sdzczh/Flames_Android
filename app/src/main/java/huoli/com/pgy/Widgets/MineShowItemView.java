package huoli.com.pgy.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import huoli.com.pgy.R;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:个人中心界面Item
 * 布局：左侧为图标，中间为Lable文字，右侧为Next>图标
 * 功能：设置左侧图标
 *       设置中间文本
 *       整个Item的点击监听
 *
 * @author 徐庆重
 */
public class MineShowItemView extends RelativeLayout implements View.OnClickListener{
    private ImageView icon;     //左边的头像
    private TextView text;      //中间的文本

    private OnClickListener l;
    public MineShowItemView(Context context) {
        super(context);
        init(null);
    }
    public MineShowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public MineShowItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    private void init(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_usershowitem,this);
        icon = findViewById(R.id.usershowitem_icon);
        text = findViewById(R.id.usershowitem_text);
        this.setOnClickListener(this);
        //判断attrs为空的情况，即只有一个参数的构造函数
        if (attrs == null){
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MineShowItemView);
        int N = a.getIndexCount();
        for (int i=0;i<N;i++){
            int index = a.getIndex(i);
            //获取用户自定义的属性值，并设置为空时的默认值
            switch (index){
                case R.styleable.MineShowItemView_msv_text:
                    //给中间文本设置文字
                    text.setText(a.getString(index));
                    break;
                case R.styleable.MineShowItemView_msv_img_icon:
                    //给头像设置资源
                    Drawable drawable = a.getDrawable(index);
                    icon.setImageDrawable(drawable);
                    break;
                case R.styleable.MineShowItemView_msv_textcolor:
                    //设置中间文本的颜色
                    text.setTextColor(a.getColor(index,getResources().getColor(R.color.black)));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    public void setTextColor(int textColor){
        text.setTextColor(textColor);
    }

    //设置中间文本显示的内容
    public void setText(String show){
        text.setText(show);
    }

    //设置左边头像显示的图标
    public void setIcon(Drawable drawable){
        icon.setImageDrawable(drawable);
    }

    @Override
    public void onClick(View v) {
        if (l !=null){
            //整个控件的点击事件
            l.onClick(this);
        }
    }
}