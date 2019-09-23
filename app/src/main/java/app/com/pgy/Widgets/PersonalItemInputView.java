package app.com.pgy.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.com.pgy.R;

/**
 * Created by YX on 2018/7/7.
 */

public class PersonalItemInputView extends LinearLayout {
    ImageView iv_left;
    TextView tv_left;
    EditText edt_right;
    public PersonalItemInputView(Context context) {
        super(context);
        init(null);
    }

    public PersonalItemInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PersonalItemInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.view_personal_custom_input_item,this);
        iv_left = findViewById(R.id.iv_left);
        tv_left = findViewById(R.id.tv_left);
        edt_right = findViewById(R.id.edt_right);
        if (attrs == null){
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PersonalItemInputView);
        for (int i = 0;i<a.getIndexCount();i++){
            int index = a.getIndex(i);
            switch (index){
                case R.styleable.PersonalItemInputView_iv_input_item_left_icon_visible:
                    if (a.getBoolean(index,false)){
                        iv_left.setVisibility(VISIBLE);
                    }else {
                        iv_left.setVisibility(GONE);
                    }
                    break;
                case R.styleable.PersonalItemInputView_iv_input_item_left_icon:
                    if (iv_left.getVisibility() != VISIBLE){
                        iv_left.setVisibility(VISIBLE);
                    }
                    iv_left.setImageResource(a.getResourceId(index,R.mipmap.ic_launcher));
                    break;
                case R.styleable.PersonalItemInputView_tv_input_item_left_text:
                    tv_left.setText(a.getString(index));
                    break;
                case R.styleable.PersonalItemInputView_tv_input_item_left_color:
                    tv_left.setTextColor(a.getColor(index,getResources().getColor(R.color.color_7a8799)));
                    break;
                case R.styleable.PersonalItemInputView_edt_input_item_right_color:
                    edt_right.setTextColor(a.getColor(index,getResources().getColor(R.color.color_525a66)));
                    break;
                case R.styleable.PersonalItemInputView_edt_input_item_right_hintcolor:
                    edt_right.setHintTextColor(a.getColor(index,getResources().getColor(R.color.color_c4c7cc)));
                    break;
                case R.styleable.PersonalItemInputView_edt_input_item_right_text:
                    edt_right.setText(a.getString(index));
                    break;
                case R.styleable.PersonalItemInputView_edt_input_item_right_hint:
                    edt_right.setHint(a.getString(index));
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
            if (tv_left.getVisibility() != VISIBLE){
                tv_left.setVisibility(VISIBLE);
            }
        }
    }

    public void setRightTxt(String txt){
        if(edt_right != null && !TextUtils.isEmpty(txt)){
            edt_right.setText(txt);
            if (edt_right.getVisibility() != VISIBLE){
                edt_right.setVisibility(VISIBLE);
            }
        }
    }

    public String getRightTxt(){
        if (edt_right != null){
            return edt_right.getText().toString().trim();
        }else {
            return "";
        }
    }

    public void setRightHint(String hint){
        if(edt_right != null && !TextUtils.isEmpty(hint)){
            edt_right.setHint(hint);
            if (edt_right.getVisibility() != VISIBLE){
                edt_right.setVisibility(VISIBLE);
            }
        }
    }

    public EditText getEdt(){
        return edt_right;
    }
}
