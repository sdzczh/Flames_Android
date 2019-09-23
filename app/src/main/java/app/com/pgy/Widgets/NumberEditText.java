package app.com.pgy.Widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/1/25 0025.
 * 限制小数位数的输入框
 * @author xuqingzhong
 */

public class NumberEditText extends AppCompatEditText implements TextWatcher{
    /**小数的位数*/
    private int digits = 0;

    public NumberEditText(Context context) {
        super(context);
        addTextChangedListener(this);
    }

    public NumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);
    }

    public NumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public void setDigits(int digits) {
        this.digits = digits;
        int inputType;
        if (digits > 0){
            inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }else{
            inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
        }
        this.setInputType(inputType);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                s = s.toString().subSequence(0,s.toString().indexOf(".") + digits +1);
                setText(s);
                setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            setText(s);
            setSelection(2);
        }
        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                setText(s.subSequence(0, 1));
                setSelection(1);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
