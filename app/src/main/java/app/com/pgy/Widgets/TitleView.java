package app.com.pgy.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.com.pgy.R;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: 自定义标题View
 * 布局：左侧为back按钮，中间为title标题文字，右侧为字体或按钮
 * 功能：设置左侧返回按钮的显示和点击监听
 *       设置中间标题的文本
 *       设置右侧textview的文本及显示
 *       设置右侧按钮的显示及监听
 *
 * @author 徐庆重
 */
public class TitleView extends RelativeLayout {
    private TextView title; //中间的标题文字
    private TextView tvRight;//右边的文字
    private ImageView back; //左边的按钮
    private ImageView imgRight;//右边的按钮

    public TitleView(Context context) {
        super(context);
        init(null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_title,this);//解析布局
        title = findViewById(R.id.title_title);
        tvRight = findViewById(R.id.title_tv_right);
        back = findViewById(R.id.title_back);
        imgRight = findViewById(R.id.title_img_right);

        //判断attrs为空的情况，即只有一个参数的构造函数
        if (attrs == null){
            return;
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        int N = a.getIndexCount();
        for (int i=0;i<N;i++){
            int index = a.getIndex(i);
            //获取用户自定义的属性值，并设置为空时的默认值
            switch (index){
                case R.styleable.TitleView_tv_back_visibility:
                    //给左侧back按钮设置是否显示，默认为显示
                    setVisibility(back,a.getInt(index,0));
                    break;
                case R.styleable.TitleView_tv_right:
                    //给右侧textview设置文本
                    tvRight.setText(a.getString(index));
                    break;
                case R.styleable.TitleView_tv_right_color:
                    //设置右侧文本的颜色
                    tvRight.setTextColor(a.getColor(index,getResources().getColor(R.color.txt_dark)));
                    break;
                case R.styleable.TitleView_tv_tv_right_visibility:
                    //给右侧textview设置是否显示，默认为消失
                    setVisibility(tvRight,a.getInt(index,2));
                    break;
                case R.styleable.TitleView_tv_img_right_visibility:
                    //给右侧按钮设置是否显示，默认为消失
                    setVisibility(imgRight,a.getInt(index,2));
                    break;
                case R.styleable.TitleView_tv_title:
                    //给中间标题设置文本
                    title.setText(a.getString(index));
                    break;
                case R.styleable.TitleView_tv_right_img:
                    /*给右侧图片设置资源*/
                    imgRight.setImageResource(a.getResourceId(index,R.mipmap.shuaxin));
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

    //以下为暴露出来用代码的方式来修改属性的方法

    //设置标题的文本
    public void setTitle(String text){
        title.setText(text);
    }

    //设置右侧的文本
    public void setTvRight(String text){
        tvRight.setText(text);
    }
    public void setRightTextColor(int textColor){
        tvRight.setTextColor(textColor);
    }

    //添加返回按钮的点击事件
    public void setBackClickListener(OnClickListener listener){
        back.setOnClickListener(listener);
    }

    //添加右侧按钮或文本的点击事件
    public void setRightClickListener(OnClickListener listener){
        tvRight.setOnClickListener(listener);
        imgRight.setOnClickListener(listener);
    }

    //设置返回按钮的是否显示
    public void setBackVisibility(int visibility){
        setVisibility(back,visibility);
    }

    //设置右侧文本的是否显示
    public void setTvRightVisibility(int visibility){
        setVisibility(tvRight,visibility);
    }

    //设置右侧按钮的是否显示
    public void setImgRightVisibility(int visibility){
        setVisibility(imgRight,visibility);
    }

    public void setImgRightResource(int resource){
        imgRight.setImageResource(resource);
    }
}
