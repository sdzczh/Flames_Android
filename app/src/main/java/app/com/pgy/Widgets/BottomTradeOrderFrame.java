package app.com.pgy.Widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import app.com.pgy.Models.Beans.C2cNormalBusiness;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.ToolsUtils;

import static app.com.pgy.Constants.Preferences.isLogin;

/**
 * Created by xuqingzhong on 2017/12/21 0021.
 *
 * @author xuqingzhong
 * 下单C2C买卖从父容器底部上弹列表框
 */

public class BottomTradeOrderFrame extends PopupWindow implements TextWatcher {
    private static final String TAG = "BottomTradeOrderFrame";
    @BindView(R.id.layout_widget_bottomTradeOrder_tradeCoinName)
    TextView layoutWidgetBottomTradeOrderTradeCoinName;
    @BindView(R.id.layout_widget_bottomTradeOrder_perPrice)
    TextView layoutWidgetBottomTradeOrderPerPrice;
    @BindView(R.id.layout_widget_bottomTradeOrder_input_amount)
    NumberEditText layoutWidgetBottomTradeOrderInputAmount;
    @BindView(R.id.layout_widget_bottomTradeOrder_coinName)
    TextView layoutWidgetBottomTradeOrderCoinName;
    @BindView(R.id.layout_widget_bottomTradeOrder_limitPrice)
    TextView layoutWidgetBottomTradeOrderLimitPrice;
    @BindView(R.id.layout_widget_bottomTradeOrder_realAmount)
    TextView layoutWidgetBottomTradeOrderRealAmount;
    @BindView(R.id.layout_widget_bottomTradeOrder_tradeAllAmount)
    TextView layoutWidgetBottomTradeOrderTradeAllAmount;
    @BindView(R.id.layout_widget_bottomTradeOrder_btn_tradeAll)
    TextView layoutWidgetBottomTradeOrderBtnTradeAll;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    private View rootFrame;
    private Context context;
    private C2cNormalBusiness.ListBean bean;
    private boolean isBuy;
    private String coinName;
    /**
     * 交易数量
     */
    private double sendAmount = 0.00;
    private String price;
    private String availBalance;
    private GetTradeOrderCallback getTradeOrderCallback;

    public void setGetTradeOrderCallback(GetTradeOrderCallback getTradeOrderCallback) {
        this.getTradeOrderCallback = getTradeOrderCallback;
    }

    public BottomTradeOrderFrame(Context context, C2cNormalBusiness.ListBean bean, boolean isBuy, int coinType, String availBalance) {
        super(context);
        this.context = context;
        this.isBuy = isBuy;
        this.availBalance = availBalance;
        this.bean = bean;
        coinName = ToolsUtils.getCoinName(coinType);
        initView(context);
        initData(bean, isBuy, coinType);
        setPopConfig();
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        rootFrame = View.inflate(context, R.layout.layout_widget_bottomtradeorder, null);
        ButterKnife.bind(this, rootFrame);
        setContentView(rootFrame);
    }

    /**
     * 初始化数据
     *
     * @param bean
     * @param isBuy
     * @param coinType
     */
    private void initData(C2cNormalBusiness.ListBean bean, boolean isBuy, int coinType) {
        layoutWidgetBottomTradeOrderTradeCoinName.setText((isBuy ? "购买" : "出售") + coinName);
        price = bean.getPrice();
        layoutWidgetBottomTradeOrderPerPrice.setText("单价  ¥  " + price);
        layoutWidgetBottomTradeOrderCoinName.setText(coinName);
        layoutWidgetBottomTradeOrderBtnTradeAll.setText(isBuy ? "全部买入" : "全部卖出");
        layoutWidgetBottomTradeOrderRealAmount.setText("实际" + (isBuy ? "购买" : "出售") + "：0.00" + coinName);
        layoutWidgetBottomTradeOrderLimitPrice.setText("限额¥" + bean.getTotalMin() + "~¥" + bean.getTotalMax());
        Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(coinType);
        int minOrderNum = coinInfo.getC2cNumScale();//币种数量小数位数
        Configuration.CoinInfo kn = ToolsUtils.getCoinInfo(0);
        int orderPriceNum = kn.getC2cPriceScale();//kn币种价格小数位数
        LogUtils.w(TAG, "币种：" + coinName + ",数量：" + minOrderNum + ",价格：" + orderPriceNum);
        layoutWidgetBottomTradeOrderInputAmount.setDigits(isBuy ? minOrderNum : orderPriceNum);
        layoutWidgetBottomTradeOrderInputAmount.setHint(isBuy ? "请输入购买数量" : "请输入出售数量");
        layoutWidgetBottomTradeOrderInputAmount.addTextChangedListener(this);
    }

    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        //this.setContentView(mDataView);//设置要显示的视图
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        setBackgroundAlpha(0.8f);//设置屏幕透明度
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(false);

        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
                /*if (mySpinnerListener != null) {
                    mySpinnerListener.onPopupWindowDismissListener();
                }*/
            }
        });

        //点击外围
        /*this.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusable(false);
                dismiss();
                return true;
            }
        });*/
    }

    /**
     * 设置显示在父布局上方(居中)
     *
     * @param target
     */
    public void showUp(View target) {
        showAtLocation(target.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    @OnClick({R.id.layout_widget_bottomTradeOrder_btn_tradeAll, R.id.layout_widget_bottomTradeOrder_btn_trade,
    R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*全部买入、全部卖出*/
            case R.id.layout_widget_bottomTradeOrder_btn_tradeAll:
                String allAmount;
                if (isBuy) {
                    allAmount = bean.getRemain();
                } else {
                    allAmount = MathUtils.minStr(bean.getRemain(), availBalance);
                }
                layoutWidgetBottomTradeOrderInputAmount.setText(allAmount);
                String trim = layoutWidgetBottomTradeOrderInputAmount.getText().toString().trim();
                layoutWidgetBottomTradeOrderInputAmount.setSelection(TextUtils.isEmpty(trim) ? 0 : trim.length());
                break;
            /*交易下单*/
            case R.id.layout_widget_bottomTradeOrder_btn_trade:
                if (sendAmount <= 0) {
                    MyToast.showToast(context, "请输入交易数量");
                    return;
                }

                if (getTradeOrderCallback != null) {
                    /*买入*/
                    if (!isLogin()) {
                        showToast("尚未登录");
                        return;
                    }
                    /*判断输入*/
                    if (TextUtils.isEmpty(bean.getPrice())) {
                        showToast("价格错误");
                        return;
                    }
                    if (sendAmount <= 0) {
                        showToast("请输入数量");
                        return;
                    }
                    double tradeAmount = sendAmount * MathUtils.string2Double(bean.getPrice());
                    if (tradeAmount < MathUtils.string2Double(bean.getTotalMin())) {
                        showToast("小于最低限额");
                        layoutWidgetBottomTradeOrderInputAmount.setText("");
                        return;
                    }
                    if (tradeAmount > MathUtils.string2Double(bean.getTotalMax())) {
                        showToast("高于最高限额");
                        layoutWidgetBottomTradeOrderInputAmount.setText("");
                        return;
                    }
                    if (!isBuy && tradeAmount > MathUtils.string2Double(availBalance)) {
                        showToast("法币账户余额不足");
                        layoutWidgetBottomTradeOrderInputAmount.setText("");
                        return;
                    }
                    getTradeOrderCallback.getInputDoubleAmount(sendAmount);
                    this.dismiss();
                }
                break;
            case R.id.iv_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void showToast(String notice) {
        Toast.makeText(context, notice, Toast.LENGTH_SHORT).show();
    }

    /**
     * 监听输入变化
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String inputAmount = layoutWidgetBottomTradeOrderInputAmount.getText().toString().trim();
        if (!TextUtils.isEmpty(inputAmount)) {
            sendAmount = MathUtils.string2Double(inputAmount);
            layoutWidgetBottomTradeOrderRealAmount.setText((isBuy ? "实际购买：" : "实际出售：") + inputAmount + coinName);
            Double priceDouble = MathUtils.string2Double(price);
            String sendAmountStr = MathUtils.formatdoubleNumber(sendAmount * priceDouble);
            layoutWidgetBottomTradeOrderTradeAllAmount.setText("¥" + sendAmountStr);
        } else {
            sendAmount = 0.0;
            layoutWidgetBottomTradeOrderTradeAllAmount.setText("¥0.00");
            layoutWidgetBottomTradeOrderRealAmount.setText("实际" + (isBuy ? "购买" : "出售") + "：0.00" + coinName);
        }

    }

    public interface GetTradeOrderCallback {
        void getInputDoubleAmount(Double amount);
    }
}
