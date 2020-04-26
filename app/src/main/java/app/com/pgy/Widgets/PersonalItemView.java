package app.com.pgy.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.com.pgy.R;

/**
 * Created by YX on 2018/7/6.
 */

public class PersonalItemView extends LinearLayout {
    ImageView iv_left;
    TextView tv_left,tv_right;
    ImageView iv_next,iv_erweima;
    public PersonalItemView(Context context) {
        super(context);
        init(null);
    }

    public PersonalItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PersonalItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.view_personal_custom_item,this);
        iv_left = findViewById(R.id.iv_left);
        tv_left = findViewById(R.id.tv_left);
        tv_right = findViewById(R.id.tv_right);
        iv_next = findViewById(R.id.iv_next);
        iv_erweima = findViewById(R.id.iv_erweima);

        //判断attrs为空的情况，即只有一个参数的构造函数
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PersonalItemView);
        for (int i=0;i<a.getIndexCount();i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.PersonalItemView_iv_item_left_icon_visible:
                    if (a.getBoolean(attr,false)){
                        iv_left.setVisibility(VISIBLE);
                    }else {
                        iv_left.setVisibility(GONE);
                    }
                    break;
                case R.styleable.PersonalItemView_iv_item_left_icon:
                    iv_left.setImageResource(a.getResourceId(attr,R.mipmap.ic_launcher));
                    break;
                case R.styleable.PersonalItemView_tv_item_left_text:
                    tv_left.setText(a.getString(attr));
                    break;
                case R.styleable.PersonalItemView_tv_item_left_color:
                    tv_left.setTextColor(a.getColor(attr,getResources().getColor(R.color.color_292d33)));
                    break;
                case R.styleable.PersonalItemView_tv_item_right_text:
                    tv_right.setText(a.getString(attr));
                    break;
                case R.styleable.PersonalItemView_tv_item_right_color:
                    tv_right.setTextColor(a.getColor(attr,getResources().getColor(R.color.color_c4c7cc)));
                    break;
                case R.styleable.PersonalItemView_iv_item_erweima_visible:
                    if (a.getBoolean(attr,false)){
                        iv_erweima.setVisibility(VISIBLE);
                    }else {
                        iv_erweima.setVisibility(GONE);
                    }
                    break;
                case R.styleable.PersonalItemView_iv_item_next_icon:
                    iv_next.setVisibility(VISIBLE);
                    iv_next.setImageResource(a.getResourceId(attr,R.mipmap.next));
                    break;
                case R.styleable.PersonalItemView_iv_item_next_visible:
                    if (a.getBoolean(attr,false)){
                        iv_next.setVisibility(VISIBLE);
                    }else {
                        iv_next.setVisibility(GONE);
                    }
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    public void setLeftImg(int imgResourceId){
        if (iv_left != null){
            iv_left.setImageResource(imgResourceId);
            if (iv_left.getVisibility() != VISIBLE){
                iv_left.setVisibility(VISIBLE);
            }
        }
    }

    public void setLeftTxt(String txt){
        if(tv_left != null && !TextUtils.isEmpty(txt)){
            tv_left.setText(txt);
            if (TextUtils.isEmpty(txt)){
                if (tv_left.getVisibility() == VISIBLE){
                    tv_left.setVisibility(GONE);
                }
            }else {
                if (tv_left.getVisibility() != VISIBLE){
                    tv_left.setVisibility(VISIBLE);
                }
            }
        }
    }

    public void setRightTxt(String txt){
        if(tv_right != null){
            tv_right.setText(txt);
            if (TextUtils.isEmpty(txt)){
                if (tv_right.getVisibility() == VISIBLE){
                    tv_right.setVisibility(GONE);
                }
            }else {
                if (tv_right.getVisibility() != VISIBLE){
                    tv_right.setVisibility(VISIBLE);
                }
            }
        }
    }

    public ImageView getErweimaView(){
        return iv_erweima;
    }

}
