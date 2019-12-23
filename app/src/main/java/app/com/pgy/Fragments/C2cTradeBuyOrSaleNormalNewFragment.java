package app.com.pgy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.C2CEntrustDetailsNewActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.EventBean.EventC2cTradeCoin;
import app.com.pgy.Models.Beans.OrderIdBean;
import app.com.pgy.Models.TakerinitBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.BBigDecimal;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import app.com.pgy.Widgets.BottomTradeOrderFrame;
import app.com.pgy.Widgets.MoneyTextWatcher;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static app.com.pgy.Constants.StaticDatas.NORMAL;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2019/12/23
 * 描述:  C2C法币交易->购买->普通用户
 *
 * @author
 */

public class C2cTradeBuyOrSaleNormalNewFragment extends BaseFragment {
    private static final String TAG = "C2cTradeBuyOrSaleNormalNewFragment";
    @BindView(R.id.tv_min)
    TextView tvMin;
    @BindView(R.id.tv_cointype_str1)
    TextView tvCointypeStr1;
    @BindView(R.id.tv_max)
    TextView tvMax;
    @BindView(R.id.tv_cointype_str2)
    TextView tvCointypeStr2;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.edt_num)
    EditText edtNum;
    @BindView(R.id.tv_cointype_str3)
    TextView tvCointypeStr3;
    @BindView(R.id.edt_price)
    EditText edtPrice;
    @BindView(R.id.tv_putc2corder_enter)
    TextView tvPutc2corderEnter;
    Unbinder unbinder;
    @BindView(R.id.tv_dealnum_info)
    TextView tvDealnumInfo;
    @BindView(R.id.tv_cointype_str4)
    TextView tvCointypeStr4;

    private int coinType;
    private boolean isBuy;
    private BottomTradeOrderFrame tradeOrder;
    private String availBalance;
    private double minAmount;
    private double buyPrice;
    private String explainText;
    private double maxAmount;
    private double salePrice;
    private double amount;

    public C2cTradeBuyOrSaleNormalNewFragment() {
    }

    public static C2cTradeBuyOrSaleNormalNewFragment newInstance(int coinType, boolean isBuy) {
        C2cTradeBuyOrSaleNormalNewFragment fragment = new C2cTradeBuyOrSaleNormalNewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("coinType", coinType);
        bundle.putBoolean("isBuy", isBuy);
        LogUtils.w(TAG, "newInstance" + isBuy);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_c2c_trade_normal_new;
    }

    @Override
    protected void initData() {
        coinType = Preferences.getC2CCoin();
        isBuy = getArguments().getBoolean("isBuy");
        LogUtils.w(TAG, "onCreate" + isBuy);
        if (isBuy) {
            tvDealnumInfo.setText("买入数量");
            tvPutc2corderEnter.setText("立即买入");
            tvPutc2corderEnter.setBackground(getResources().getDrawable(R.drawable.bg_c2cbuy_enter));
        } else {
            tvDealnumInfo.setText("卖出数量");
            tvPutc2corderEnter.setText("立即卖出");
            tvPutc2corderEnter.setBackground(getResources().getDrawable(R.drawable.bg_c2csale_enter));
        }
        refreshCoinType();
        initTaker();
    }

    private void initTaker() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        NetWorks.initTaker(Preferences.getAccessToken(), map, new getBeanCallback<TakerinitBean>() {
            @Override
            public void onSuccess(TakerinitBean takerinitBean) {
                buyPrice = takerinitBean.getBuyPrice();
                salePrice = takerinitBean.getSalePrice();
                minAmount = takerinitBean.getMinAmount();
                maxAmount = takerinitBean.getMaxAmount();
                explainText = takerinitBean.getExplainText();
                DecimalFormat dfs = new DecimalFormat("#.####");
                DecimalFormat dfs2 = new DecimalFormat("0.00");
                tvMin.setText(dfs.format(minAmount) + "");
                tvMax.setText(dfs.format(maxAmount) + "");
                if (isBuy) {
                    tvPrice.setText(dfs2.format(buyPrice) + "CNY");
                } else {
                    tvPrice.setText(dfs2.format(salePrice) + "CNY");
                }

                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (isBuy) {
            edtNum.addTextChangedListener(new MoneyTextWatcher(edtNum).setDigits(2));
            edtPrice.addTextChangedListener(new MoneyTextWatcher(edtPrice).setDigits(4));

            edtNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!(edtNum.getText().toString().trim().length() == 0)) {
                        double x = Double.parseDouble(edtNum.getText().toString());
                        DecimalFormat dfs = new DecimalFormat("0.0000");
                        String result = "";
                        try {
                            amount = BBigDecimal.divide(x, buyPrice, 4);
                            result = dfs.format(amount) + "";
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        edtPrice.setText(result);
                    } else {
                        amount = 0;
                        edtPrice.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            edtNum.addTextChangedListener(new MoneyTextWatcher(edtNum).setDigits(4));
            edtPrice.addTextChangedListener(new MoneyTextWatcher(edtPrice).setDigits(2));

            edtNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!(edtNum.getText().toString().trim().length() == 0)) {
                        double x = Double.parseDouble(edtNum.getText().toString());
                        DecimalFormat dfs = new DecimalFormat("0.00");
                        String result = "";
                        try {
                            amount = BBigDecimal.multiply(x, salePrice, 2);
                            result = dfs.format(amount) + "";
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        edtPrice.setText(result);
                    } else {
                        amount = 0;
                        edtPrice.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

    }


    private void ccNormalBuy() {
        /*获取用户输入委托价格、委托数量*/
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("amount", amount);
        map.put("orderId", 0);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        NetWorks.c2cNormalBuy(Preferences.getAccessToken(), map, new getBeanCallback<OrderIdBean>() {
            @Override
            public void onSuccess(OrderIdBean info) {
                hideLoading();
                showToast("购买成功");
                LogUtils.w(TAG, "购买成功");
                if (info != null) {
                    Intent intent = new Intent(mContext, C2CEntrustDetailsNewActivity.class);
                    intent.putExtra("normalOrBusiness", NORMAL);
                    intent.putExtra("entrustId", info.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
            }
        });
    }

    private void ccNormalSale(String pwd,double amount_d) {
        /*获取用户输入委托价格、委托数量*/
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("amount", amount_d);
        map.put("password", pwd);
        map.put("orderId", 0);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        NetWorks.c2cNormalSale(Preferences.getAccessToken(), map, new getBeanCallback<OrderIdBean>() {
            @Override
            public void onSuccess(OrderIdBean info) {
                hideLoading();
                showToast("出售成功");
                LogUtils.w(TAG, "出售成功");
                if (info != null) {
                    Intent intent = new Intent(mContext, C2CEntrustDetailsNewActivity.class);
                    intent.putExtra("normalOrBusiness", NORMAL);
                    intent.putExtra("entrustId", info.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }


    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 币种状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventC2cTradeCoin c2cCoinChange) {
        LogUtils.e("C2cTradeBuyOrSaleNormalNewFragment", isBuy + "收到广播");
        coinType = c2cCoinChange.getCoinType();
        refreshCoinType();
    }

    /**
     * 更新货币文案信息
     */
    private void refreshCoinType() {
        String coinName = ToolsUtils.getCoinName(coinType);
        tvCointypeStr1.setText(coinName + ",最大下单");
        tvCointypeStr2.setText(coinName);
        if (isBuy) {
            tvCointypeStr3.setText("CNY");
            tvCointypeStr4.setText(coinName);
        } else {
            tvCointypeStr3.setText(coinName);
            tvCointypeStr4.setText("CNY");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.tv_putc2corder_enter)
    public void onViewClicked() {
        if (!LoginUtils.isLogin(getActivity())) {
            showToast(R.string.unlogin);
            return;
        }
        if (amount !=0) {
            if (isBuy) {
                if (minAmount<=amount&&amount<=maxAmount) {
                    ccNormalBuy();
                }else {
                    showToast("兑换金额不在限制范围内");
                }
            } else {
                double a = 0;
                if (edtNum.getText().toString().trim().length() != 0) {
                    a = Double.parseDouble(edtNum.getText().toString());
                }
                if (minAmount<=a&&a<=maxAmount) {
                    /*弹出输入交易密码对话框*/
                    final double finalA = a;
                    showPayDialog(new getStringCallback() {
                        @Override
                        public void getString(String string) {
                            ccNormalSale(string, finalA);
                        }
                    });
                }else {
                    showToast("卖出数量不在限制范围内");
                }


            }
        }else {
            showToast("兑换金额为0");
        }

    }
}
