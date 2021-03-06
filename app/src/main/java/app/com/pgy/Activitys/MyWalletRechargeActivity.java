package app.com.pgy.Activitys;

import android.Manifest;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Widgets.YubibaoCoinspinner;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.RechargeBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.FileUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.QrCodeUtil;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/12.
 */

public class MyWalletRechargeActivity extends PermissionActivity {
            @BindView(R.id.iv_back)
            ImageView iv_back;
            @BindView(R.id.tv_title)
            TextView tv_title;
            @BindView(R.id.view_line)
            View viewLine;
            @BindView(R.id.edt_activity_mywallet_recharge_account)
            EditText edt_account;
            @BindView(R.id.edt_activity_mywallet_recharge_address)
            EditText edt_address;
            @BindView(R.id.tv_activity_mywallet_recharge_fee)
            TextView tv_fee;

            @BindView(R.id.iv_activity_mywallet_recharge_qr)
            ImageView iv_qr;
            @BindView(R.id.tv_activity_mywallet_recharge_save)
            TextView tv_save;
            @BindView(R.id.tv_activity_mywallet_recharge_address)
            TextView tv_address;
            @BindView(R.id.tv_activity_mywallet_recharge_copy)
            TextView tv_copy;
            @BindView(R.id.tv_activity_mywallet_recharge_desc)
            TextView tv_desc;
            @BindView(R.id.tv_activity_mywallet_transfer_coin)
            TextView tvActivityMywalletTransferCoin;
            /**
             * 配置文件中的充值二维码、公司钱包地址
             */
            private String companyWalletAddress;
            private RechargeBean mRechargeBean;
            private Configuration.CoinInfo currentCoin;
            private int coinType = -1;
            private int accountType = StaticDatas.ACCOUNT_GOODS;

            private String coinName = "";
            private Bitmap bitmap;

            @Override
            public int getContentViewId() {
                return R.layout.activity_mywallet_recharge;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化界面
     */
    @Override
    protected void initData() {
        coinType = getIntent().getIntExtra("coinType", -1);
        accountType = getIntent().getIntExtra("accountType", StaticDatas.ACCOUNT_GOODS);
        if (coinType == -1) {
            /*获取计价币种列表，交易币种map*/
            List<Integer> rechAndWithCoinTypeList = getRechAndWithCoinTypeList();
            if (rechAndWithCoinTypeList != null && rechAndWithCoinTypeList.size() > 0) {
                coinType = rechAndWithCoinTypeList.get(0);
            }
            if (coinType == -1) {
                showToast("没有币种信息");
                finish();
                return;
            }
        }
        coinName = getCoinName(coinType);
        switchCoinFrameText();
        getRecharge2Net();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    /**
     * 根据当前币种，设置二维码、公司钱包地址、币种名称
     */
    private void switchCoinFrameText() {
        tv_title.setText(coinName + " 充币");
        tvActivityMywalletTransferCoin.setText(coinName);
        if (mRechargeBean == null) {
            iv_qr.setImageResource(R.mipmap.ic_launcher);
            tv_address.setText("暂未开放充币");
            tv_save.setEnabled(false);
            tv_copy.setEnabled(false);
        } else {

            companyWalletAddress = mRechargeBean.getRechAddress();
            bitmap = QrCodeUtil.createBitmap(companyWalletAddress);
            if (bitmap == null){
                showToast("生成二维码失败");
            }else {
                iv_qr.setImageBitmap(bitmap);
            }
            tv_address.setText(companyWalletAddress);
            tv_desc.setText(mRechargeBean.getRechargeInfo());
            tv_save.setEnabled(true);
            tv_copy.setEnabled(true);
            tv_fee.setText(mRechargeBean.getFee());
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_activity_mywallet_recharge_qr, R.id.tv_activity_mywallet_recharge_save,
            R.id.tv_activity_mywallet_recharge_copy,R.id.tv_activity_mywallet_recharge_submit,R.id.ll_activity_mywallet_transfer_coin})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_activity_mywallet_recharge_qr:
            case R.id.tv_activity_mywallet_recharge_save:
                if (bitmap == null) {
                    bitmap = QrCodeUtil.createBitmap(companyWalletAddress);
                    if (bitmap == null){
                        showToast("生成二维码失败");
                    }else {
                        iv_qr.setImageBitmap(bitmap);
                        showToast("生成二维码成功，点击保存");
                    }
                    return;
                }

                checkPermission(new PermissionActivity.CheckPermListener() {
                    @Override
                    public void superPermission() {
                        LogUtils.w("permission","BaseUploadPicActivity:读写权限已经获取");
                        FileUtils.saveBmp2Gallery(MyWalletRechargeActivity.this,bitmap,"PGY充币二维码_" + coinName);
                    }
                }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.tv_activity_mywallet_recharge_copy:
                if (TextUtils.isEmpty(companyWalletAddress)) {
                    return;
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cm.setText(companyWalletAddress);
                showToast("复制成功");
                break;
            case R.id.tv_activity_mywallet_recharge_submit:
                showPayDialog(new getStringCallback() {
                    @Override
                    public void getString(String string) {
                        submitRecharged(string);
                    }
                });
                break;
            case R.id.ll_activity_mywallet_transfer_coin:
                showSpinner();
                break;

        }
    }

    /**
     * 获取充值信息
     */
    private void getRecharge2Net() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType",coinType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getRecharge(Preferences.getAccessToken(), map, new getBeanCallback<RechargeBean>() {
            @Override
            public void onSuccess(RechargeBean rechargeBean) {
                mRechargeBean = rechargeBean;
                switchCoinFrameText();
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                mRechargeBean = null;
                switchCoinFrameText();
                hideLoading();
            }
        });
    }

    /**
     * 提交充值信息
     */
    private void submitRecharged(String psw) {
        String acount = edt_account.getText().toString();
        String address = edt_address.getText().toString();
        if (TextUtils.isEmpty(acount)){
            showToast("充值数量不能为空");
            return;
        }
        if (TextUtils.isEmpty(address)){
            showToast("个人地址不能为空");
            return;
        }
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType",coinType);
        map.put("accountType",accountType);
        map.put("amount",acount);
        map.put("password",psw);
        map.put("rechargeAddress",address);
        map.put("fee",mRechargeBean.getFee());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.submitRecharge(Preferences.getAccessToken(), map,new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast("提交成功");
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,reason);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroy();
    }


    private YubibaoCoinspinner coinspinner;
    private void showSpinner() {
        if (getRechAndWithCoinTypeList() == null || getRechAndWithCoinTypeList().size() <= 0) {
            return;
        }
        if (coinspinner == null) {
            coinspinner = new YubibaoCoinspinner(getApplicationContext(), getRechAndWithCoinTypeList(), new spinnerSingleChooseListener() {
                @Override
                public void onItemClickListener(int position) {
                    coinspinner.dismiss();
                    if (getRechAndWithCoinTypeList().get(position) == coinType) {
                        return;
                    }
                    coinType = getRechAndWithCoinTypeList().get(position);
                    coinName = getCoinName(coinType);
                    switchCoinFrameText();
                    getRecharge2Net();
                }
            });
        }
        if (!coinspinner.isShowing()) {
            coinspinner.showDown(viewLine);
        }
    }

}
