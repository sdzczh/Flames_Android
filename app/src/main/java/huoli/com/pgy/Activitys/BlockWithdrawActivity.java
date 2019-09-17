package huoli.com.pgy.Activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.ScannerQRCodeActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getStringCallback;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;

import static huoli.com.pgy.Constants.StaticDatas.MYASSETS_WITHDRAWAL_OTHER;

/**
 * Created by YX on 2018/7/18.
 */

public class BlockWithdrawActivity extends ScannerQRCodeActivity implements getStringCallback {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_activity_mywallet_withdraw_toAddress)
    EditText edtActivityMywalletWithdrawToAddress;
    @BindView(R.id.tv_activity_block_withdraw_desc)
    TextView tvActivityBlockWithdrawDesc;

    private int coinType;
    private String userWalletAddress;
    @Override
    public int getContentViewId() {
        return R.layout.activity_block_withdraw;
    }

    @Override
    protected void initData() {
        coinType = getIntent().getIntExtra("coinType",-1);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (coinType == -1){
            showToast("币种信息为空");
            finish();
            return;
        }
        tvTitle.setText("提取到其他钱包");
  /*添加扫描二维码回调监听*/
        setStringCallback(this);
    }

    @OnClick({R.id.iv_back, R.id.iv_activity_mywallet_withdraw_scan, R.id.tv_activity_block_withdraw_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_activity_mywallet_withdraw_scan:
                goScanner();
                break;
            case R.id.tv_activity_block_withdraw_submit:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                submit();
                break;
        }
    }

    @Override
    public void getString(String string) {
        edtActivityMywalletWithdrawToAddress.setText(string+"");
    }

    /**
     * 提交
     */
    private void submit() {
        /*获取输入的内容*/
        if (coinType < 0) {
            showToast("请选择充值币种");
            return;
        }
        userWalletAddress = edtActivityMywalletWithdrawToAddress.getText().toString().trim();
        if (TextUtils.isEmpty(userWalletAddress)) {
            showToast("请输入钱包地址");
            return;
        }
          /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                withdrawalOtherWallet2Net(string);
            }
        });
    }

    /**
     * 提取到其他钱包
     */
    private void withdrawalOtherWallet2Net(String pwd) {
        showLoading(tvActivityBlockWithdrawDesc);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("type", MYASSETS_WITHDRAWAL_OTHER);
        map.put("accountNum",userWalletAddress);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("password", pwd);
        NetWorks.submitMyAssetsWithdraw(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("成功");
                LogUtils.w(TAG, "提现成功");
                hideLoading();
                setResult(RESULT_OK);
                BlockWithdrawActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }
}
