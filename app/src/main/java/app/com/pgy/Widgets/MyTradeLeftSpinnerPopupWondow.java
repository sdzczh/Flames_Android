package app.com.pgy.Widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jude.easyrecyclerview.decoration.DividerDecoration;

import app.com.pgy.Activitys.MyWalletActivity;
import app.com.pgy.Activitys.MyWalletCoinInfoActivity;
import app.com.pgy.Activitys.MyWalletTransferActivity;
import app.com.pgy.Activitys.TradeGoodsEntrustListActivity;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.R;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.MathUtils;

/**
 * Create by YX on 2019/10/6 0006
 */
public class MyTradeLeftSpinnerPopupWondow extends PopupWindow implements View.OnClickListener {
    private Activity mActivity;
    private View spinnerView;

    private LinearLayout ll_wallet;
    private ImageView iv_wallet;
    private LinearLayout ll_wallet_content;
    private TextView tv_mingxi,tv_trust;

    private LinearLayout ll_info;
    private ImageView iv_info;
    private LinearLayout ll_info_content;
    private TextView tv_current,tv_history,tv_bibi;

    private LinearLayout ll_kefu;
    private int coinType;

    public MyTradeLeftSpinnerPopupWondow(Activity activity,int coinType) {
        super(activity);
        mActivity = activity;
        this.coinType = coinType;
        initView();
        setPopConfig();
        /*设置整个widow的背景色，为一个light8的边框*/
        this.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_corners_whitesolid));
    }

    /**
     * 初始化控件
     *
     */
    private void initView() {
        spinnerView = View.inflate(mActivity, R.layout.layout_widget_left_trade_spinner, null);
        ll_wallet = spinnerView.findViewById(R.id.ll_trade_left_spinner_wallet);
        iv_wallet = spinnerView.findViewById(R.id.iv_trade_left_spinner_wallet);
        ll_wallet_content = spinnerView.findViewById(R.id.ll_trade_left_spinner_wallet_content);
        tv_mingxi = spinnerView.findViewById(R.id.tv_trade_left_spinner_mingxi);
        tv_trust = spinnerView.findViewById(R.id.tv_trade_left_spinner_trust);

        ll_info = spinnerView.findViewById(R.id.ll_trade_left_spinner_info);
        iv_info = spinnerView.findViewById(R.id.iv_trade_left_spinner_info);
        ll_info_content = spinnerView.findViewById(R.id.ll_trade_left_spinner_info_content);
        tv_current = spinnerView.findViewById(R.id.tv_trade_left_spinner_current);
        tv_history = spinnerView.findViewById(R.id.tv_trade_left_spinner_history);
        tv_bibi = spinnerView.findViewById(R.id.tv_trade_left_spinner_bibi);

        ll_kefu = spinnerView.findViewById(R.id.ll_trade_left_spinner_kefu);

        spinnerView.findViewById(R.id.layout_widget_left_trade_spinner_close).setOnClickListener(this);
        ll_wallet.setOnClickListener(this);
        tv_mingxi.setOnClickListener(this);
        tv_trust.setOnClickListener(this);

        ll_info.setOnClickListener(this);
        tv_current.setOnClickListener(this);
        tv_history.setOnClickListener(this);
        tv_bibi.setOnClickListener(this);

        ll_kefu.setOnClickListener(this);

        /*弹出popupwindow时，二级菜单默认隐藏，当点击某项时，二级菜单再弹出*/
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
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        setBackgroundAlpha(0.8f);//设置屏幕透明度
        ColorDrawable dw = new ColorDrawable(0x50000000);
        this.setBackgroundDrawable(dw);
        this.setClippingEnabled(false);
        //setWindowFullScreen(true);
        this.setAnimationStyle(R.style.AnimationLeftMove);
        // 设置外部触摸会关闭窗口
        this.setOutsideTouchable(true);
        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
                //setWindowFullScreen(false);
                /*if (mySpinnerListener != null) {
                    mySpinnerListener.onPopupWindowDismissListener();
                }*/
            }
        });
    }

    public void showLeft() {
        showAtLocation(spinnerView, Gravity.TOP | Gravity.START,0,0);
    }

    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        mActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.layout_widget_left_trade_spinner_close:
                dismiss();
                break;
            case R.id.ll_trade_left_spinner_wallet:
                if (ll_wallet_content.getVisibility() == View.VISIBLE){
                    ll_wallet_content.setVisibility(View.GONE);
                    iv_wallet.setImageResource(R.mipmap.zhankai_down);
                }else {
                    ll_wallet_content.setVisibility(View.VISIBLE);
                    iv_wallet.setImageResource(R.mipmap.zhankai_up);
                }
                break;
            case R.id.tv_trade_left_spinner_mingxi:
                // 跳转到资产 币币账户
                if (LoginUtils.isLogin(mActivity)) {
                    intent = new Intent(mActivity, MyWalletActivity.class);
                    intent.putExtra("index", 0);
                }
                break;
            case R.id.tv_trade_left_spinner_trust:
                if (LoginUtils.isLogin(mActivity)) {
                    intent = new Intent(mActivity, MyWalletTransferActivity.class);
                    intent.putExtra("coinType", coinType);
                    intent.putExtra("accountType",  StaticDatas.ACCOUNT_GOODS);
                }
                break;
            case R.id.ll_trade_left_spinner_info:
                if (ll_info_content.getVisibility() == View.VISIBLE){
                    ll_info_content.setVisibility(View.GONE);
                    iv_info.setImageResource(R.mipmap.zhankai_down);
                }else {
                    ll_info_content.setVisibility(View.VISIBLE);
                    iv_info.setImageResource(R.mipmap.zhankai_up);
                }
                break;
            case R.id.tv_trade_left_spinner_current:
                if (LoginUtils.isLogin(mActivity)) {
                    intent = new Intent(mActivity, TradeGoodsEntrustListActivity.class);
                    intent.putExtra("index", 0);
                }
                break;
            case R.id.tv_trade_left_spinner_history:
                if (LoginUtils.isLogin(mActivity)) {
                    intent = new Intent(mActivity, TradeGoodsEntrustListActivity.class);
                    intent.putExtra("index", 1);
                }
                break;
            case R.id.tv_trade_left_spinner_bibi:
                if (LoginUtils.isLogin(mActivity)) {
                    intent = new Intent(mActivity, MyWalletCoinInfoActivity.class);
                    intent.putExtra("accountType", StaticDatas.ACCOUNT_GOODS);
                    intent.putExtra("coinType", coinType);
                }
                break;
            case R.id.ll_trade_left_spinner_kefu:
                break;
        }

        if (intent != null){
            mActivity.startActivity(intent);
        }
    }
}
