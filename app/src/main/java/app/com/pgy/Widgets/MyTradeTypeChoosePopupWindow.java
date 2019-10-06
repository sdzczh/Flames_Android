package app.com.pgy.Widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;

/**
 * Create by YX on 2019/10/6 0006
 */
public class MyTradeTypeChoosePopupWindow extends PopupWindow {
    private Context context;
    private View spinnerView;
    private RadioGroup rg;
    private RadioButton rb1,rb2;
    private spinnerSingleChooseListener mySpinnerListener;
    private boolean isBuy = false;
    private  int tradePos =0;
    public void setMySpinnerListener(spinnerSingleChooseListener mySpinnerListener) {
        this.mySpinnerListener = mySpinnerListener;
    }
    public MyTradeTypeChoosePopupWindow(Context context,boolean isBuy,int tradepos) {
        super(context);
        this.context = context;
        this.isBuy = isBuy;
        this.tradePos = tradepos;
        initView(context);
        setPopConfig();
    }

    /**
     *   初始化控件
     * @param context
     */
    private void initView(Context context) {
        spinnerView = View.inflate(context, R.layout.my_trade_type_popupwindow, null);
        rg = spinnerView.findViewById(R.id.rg);
        rb1 = spinnerView.findViewById(R.id.rb_xianjia);
        rb2 = spinnerView.findViewById(R.id.rb_shijia);
        if (!isBuy){
            rb1.setText("  限价卖出");
            rb2.setText("  市价卖出");
        }else {
            rb1.setText("  限价买入");
            rb2.setText("  市价买入");
        }
        if (tradePos == 0){
            rb1.setChecked(true);
        }else {
            rb2.setChecked(true);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mySpinnerListener != null){
                    if (checkedId == R.id.rb_xianjia){
                        mySpinnerListener.onItemClickListener(0);
                    }else {
                        mySpinnerListener.onItemClickListener(1);
                    }
                }
                dismiss();

            }
        });
        setContentView(spinnerView);
    }

    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        //获取自身的长宽高
        spinnerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        popupWidth = spinnerView.getMeasuredWidth();
//        popupHeight = spinnerView.getMeasuredHeight();
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setClippingEnabled(false);
        //setWindowFullScreen(true);
//        this.setAnimationStyle(R.style.AnimationLeftMove);
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(true);
        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //setWindowFullScreen(false);
                /*if (mySpinnerListener != null) {
                    mySpinnerListener.onPopupWindowDismissListener();
                }*/
            }
        });
    }
}
