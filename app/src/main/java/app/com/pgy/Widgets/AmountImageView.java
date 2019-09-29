package app.com.pgy.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.math.BigDecimal;

import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;

/**
 * Create by YX on 2019/9/29 0029
 */
public class AmountImageView extends LinearLayout implements View.OnClickListener, TextWatcher {
    private static final String TAG = "AmountView";
    private double currentAmount; //当前数量
    private double maxAmount;         //最大可输入的数量
    private double perAmount = 1;   //根据小数位数获取单位值

    private NumberEditText leftText;
    private ImageView btnDecrease;
    private ImageView btnIncrease;

    public AmountImageView(Context context) {
        this(context, null);
        init(null);
    }

    public double getCurrentAmount() {
        return MathUtils.string2Double(leftText.getText().toString().trim());
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setDigits(int digits) {
        perAmount = BigDecimal.valueOf(Math.pow(0.1,digits)).doubleValue();
        leftText.setDigits(digits);
        LogUtils.w(TAG,"digits:"+digits+",perAmount:"+perAmount);
    }

    public AmountImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    public AmountImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_amount_img, this);
        leftText = findViewById(R.id.layout_widget_etAmount);
        btnDecrease = findViewById(R.id.layout_widget_btnDecrease);
        btnIncrease = findViewById(R.id.layout_widget_btnIncrease);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        leftText.addTextChangedListener(this);

        /*初始化当前数量*/
        currentAmount = MathUtils.string2Double(leftText.getText().toString().trim());
        //判断attrs为空的情况，即只有一个参数的构造函数
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = a.getIndex(i);
            //获取用户自定义的属性值，并设置为空时的默认值
            switch (index) {
                case R.styleable.AmountView_av_input:
                    //给左侧输入框设置文字
                    leftText.setText(a.getString(index));
                    break;
                case R.styleable.AmountView_av_input_hint:
                    //给左侧输入框设置hint文字
                    leftText.setHint(a.getString(index));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_widget_btnDecrease:
                /*减法*/
                if (currentAmount <= 0){
                    currentAmount = 0;
                }else {
                    currentAmount = currentAmount - perAmount;
                }
                leftText.setText(MathUtils.formatDoubleNumber(currentAmount));
                break;
            case R.id.layout_widget_btnIncrease:
                /*加法*/
                if (maxAmount > 0 && currentAmount >= maxAmount){
                    currentAmount = maxAmount;
                }else {
                    currentAmount = currentAmount + perAmount;
                }
                leftText.setText(MathUtils.formatDoubleNumber(currentAmount,""));

                break;
            default:break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()) {
//            btnDecrease.setTextColor(getResources().getColor(R.color.txt_content));
//            btnIncrease.setTextColor(getResources().getColor(R.color.txt_content));
            currentAmount = 0;
        }else{
//            btnDecrease.setTextColor(getResources().getColor(R.color.txt_app));
//            btnIncrease.setTextColor(getResources().getColor(R.color.txt_app));
            /*实时更新当前数值*/
            currentAmount = MathUtils.string2Double(s.toString());
            leftText.setSelection(leftText.getText().length());
        }

    }


    public void setInputHint(String inputHint){
        leftText.setHint(inputHint);
    }

    public String getInputHint(){
        return leftText.getHint().toString().trim();
    }

    public void setInput(String input){
        LogUtils.w(TAG,"input:"+input);
        leftText.setText(input);
        String leftTxt = leftText.getText().toString().trim();
        if (leftTxt.endsWith(".")){
            LogUtils.w(TAG,"endsWith.");
            if (leftTxt.length() <= 1){
                LogUtils.w(TAG,"endsWith.length<=1");
                leftTxt = "";
            }else {
                leftTxt = leftTxt.substring(0, leftTxt.length() - 1);
                LogUtils.w(TAG,"new Input:"+input);
            }
        }
        LogUtils.w(TAG,"finally:"+leftTxt);
        leftText.setText(leftTxt);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        if (watcher != null) {
            leftText.addTextChangedListener(watcher);
        }
    }
}
