package app.com.pgy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.MyWalletRechargeActivity;
import app.com.pgy.Activitys.MyWalletTransferActivity;
import app.com.pgy.Activitys.MyWalletWithdrawActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.MyAccount;
import app.com.pgy.Models.Beans.MyWallet;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static app.com.pgy.Constants.StaticDatas.ACCOUNT_C2C;
import static app.com.pgy.Constants.StaticDatas.ACCOUNT_GOODS;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/14 0014
 */
public class MyAccountTotalFragment extends BaseFragment {
    @BindView(R.id.iv_ftagment_account_total_show)
    ImageView ivFtagmentAccountTotalShow;
    @BindView(R.id.tv_ftagment_account_total_amount)
    TextView tvFtagmentAccountTotalAmount;
    @BindView(R.id.tv_ftagment_account_total_amountCny)
    TextView tvFtagmentAccountTotalAmountCny;
    @BindView(R.id.ll_ftagment_account_total_withdraw)
    LinearLayout llFtagmentAccountTotalWithdraw;
    @BindView(R.id.ll_ftagment_account_total_recharge)
    LinearLayout llFtagmentAccountTotalRecharge;
    @BindView(R.id.ll_ftagment_account_total_trust)
    LinearLayout llFtagmentAccountTotalTrust;
    @BindView(R.id.tv_ftagment_account_total_amountC2c)
    TextView tvFtagmentAccountTotalAmountC2c;
    @BindView(R.id.tv_ftagment_account_total_amountC2cCny)
    TextView tvFtagmentAccountTotalAmountC2cCny;
    @BindView(R.id.tv_ftagment_account_total_amountTrade)
    TextView tvFtagmentAccountTotalAmountTrade;
    @BindView(R.id.tv_ftagment_account_total_amountTradeCny)
    TextView tvFtagmentAccountTotalAmountTradeCny;

    private boolean isShow = false;
    private MyAccount myAccount;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_account_total_asset;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        getAccount();
    }

    @OnClick({R.id.iv_ftagment_account_total_show, R.id.ll_ftagment_account_total_withdraw, R.id.ll_ftagment_account_total_recharge, R.id.ll_ftagment_account_total_trust})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_ftagment_account_total_show:
                isShow = !isShow;
                if (isShow){
                    tvFtagmentAccountTotalAmount.setText("****");
                    tvFtagmentAccountTotalAmountCny.setText("****");
                    tvFtagmentAccountTotalAmountC2c.setText("****");
                    tvFtagmentAccountTotalAmountC2cCny.setText("****");
                    tvFtagmentAccountTotalAmountTrade.setText("****");
                    tvFtagmentAccountTotalAmountTradeCny.setText("****");
                }else {
                    tvFtagmentAccountTotalAmount.setText(myAccount.getAccountBalanceCny());
                    tvFtagmentAccountTotalAmountCny.setText(myAccount.getAccountBalanceCny());
                    tvFtagmentAccountTotalAmountC2c.setText(myAccount.getAccountList().get(ACCOUNT_C2C)+"");
                    tvFtagmentAccountTotalAmountC2cCny.setText(myAccount.getAccountList().get(ACCOUNT_C2C)+"");
                    tvFtagmentAccountTotalAmountTrade.setText(myAccount.getAccountList().get(ACCOUNT_GOODS)+"");
                    tvFtagmentAccountTotalAmountTradeCny.setText(myAccount.getAccountList().get(ACCOUNT_GOODS)+"");
                }
                break;
            case R.id.ll_ftagment_account_total_withdraw:
                intent = new Intent(mContext, MyWalletWithdrawActivity.class);
                break;
            case R.id.ll_ftagment_account_total_recharge:
                intent = new Intent(mContext, MyWalletRechargeActivity.class);
                break;
            case R.id.ll_ftagment_account_total_trust:
                intent = new Intent(mContext, MyWalletTransferActivity.class);
                break;
        }


        if (intent != null) {
            startActivity(intent);
        }
    }

    private void updateAccount(MyAccount myAccount){
       this.myAccount = myAccount;

        tvFtagmentAccountTotalAmount.setText(myAccount.getAccountBalanceCny());
        tvFtagmentAccountTotalAmountCny.setText(myAccount.getAccountBalanceCny());
        tvFtagmentAccountTotalAmountC2c.setText(myAccount.getAccountList().get(ACCOUNT_C2C)+"");
        tvFtagmentAccountTotalAmountC2cCny.setText(myAccount.getAccountList().get(ACCOUNT_C2C)+"");
        tvFtagmentAccountTotalAmountTrade.setText(myAccount.getAccountList().get(ACCOUNT_GOODS)+"");
        tvFtagmentAccountTotalAmountTradeCny.setText(myAccount.getAccountList().get(ACCOUNT_GOODS)+"");
    }

    private void getAccount(){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType", StaticDatas.ACCOUNT_C2C);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getMyWalletAccount(Preferences.getAccessToken(), map, new getBeanCallback<MyAccount>() {
            @Override
            public void onSuccess(MyAccount myWalletFromNet) {
                if (myWalletFromNet == null) {
                    myWalletFromNet = new MyAccount();
                }
                updateAccount(myWalletFromNet);
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                updateAccount(new MyAccount());
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();

            }
        });
    }

}
