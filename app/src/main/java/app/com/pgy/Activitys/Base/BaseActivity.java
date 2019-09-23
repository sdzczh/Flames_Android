package app.com.pgy.Activitys.Base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import app.com.pgy.Activitys.MainActivity;
import app.com.pgy.Constants.ErrorHandler;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import app.com.pgy.Widgets.ExitDialog;
import app.com.pgy.Widgets.LoadingProgress;
import app.com.pgy.Widgets.MyToast;
import app.com.pgy.Widgets.PayDialog;
import app.com.pgy.Widgets.PayPsdInputView;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:Activity基类
 * 获取登录状态、网络状态改变监听、toast、onFail
 *
 * @author 徐庆重
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = "BaseActivity";
    protected Context mContext;
    private long mExitTime;
    private ConnectivityManager manager;
    /** 申诉对话框*/
    private ExitDialog complaintDialog;
    /** 加载中环形进度条*/
    private LoadingProgress loadingProgress;
    private Configuration configuration;
    private LocalBroadcastManager mLocalBroadcastManager;
    public abstract int getContentViewId();
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置顶部状态栏颜色*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 状态栏 顶部
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 导航栏 底部
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(getContentViewId());
        mContext = this;
        Preferences.init(this);
        /*初始化配置文件*/
        configuration = Preferences.getConfiguration();
        TAG = mContext.getClass().getName();
        /*添加子类到管理控制器中*/
        ActivityController.addActivity(this);
        LogUtils.w("app","当前activity数量："+ActivityController.getSize());
        unbinder = ButterKnife.bind(this);
        initData();
        initView(savedInstanceState);
    }

    protected abstract void initData();
    protected abstract void initView(Bundle savedInstanceState);

    /**获取配置文件各种信息*/
    protected Configuration getConfiguration() {
        if (configuration == null){
            configuration = Preferences.getConfiguration();
        }
        return configuration;
    }

    /**获取币种信息*/
    protected Configuration.CoinInfo getCoinInfo(int coinType){
        Map<Integer, Configuration.CoinInfo> coinInfoMap = getConfiguration().getCoinInfo();
        if (coinInfoMap == null){
            return new Configuration.CoinInfo();
        }
        Configuration.CoinInfo coinInfo = coinInfoMap.get(coinType);
        if (coinInfo == null){
            return new Configuration.CoinInfo();
        }
        return coinInfo;
    }

    /**获取C2C顶部列表*/
    protected List<Integer> getC2cCoinList(){
        List<Integer> c2cPerCoins = configuration.getC2cCoin();
        if (c2cPerCoins == null){
            c2cPerCoins = new ArrayList<>();
        }
        return c2cPerCoins;
    }

    /**获取现货档位列表*/
    protected List<Integer> getGearList() {
        List<Integer> gearList = configuration.getOrderCount();
        if (gearList == null){
            gearList = new ArrayList<>();
        }
        Collections.reverse(gearList);
        return gearList;
    }

    /**获取充值、提现、资金划转币种列表*/
    protected List<Integer> getRechAndWithCoinTypeList() {
        List<Integer> rechAndWithCoinTypeList = getConfiguration().getRechAndWithCoin();
        if (rechAndWithCoinTypeList == null){
            rechAndWithCoinTypeList = new ArrayList<>();
        }
        return rechAndWithCoinTypeList;
    }

    /**获取余币宝币种列表*/
    protected List<Integer> getYubiCoinTypeList() {
        List<Integer> yubiCoinTypeList = getConfiguration().getYubiCoin();
        if (yubiCoinTypeList == null){
            yubiCoinTypeList = new ArrayList<>();
        }
        return yubiCoinTypeList;
    }

    /**获取现货顶部计价币、交易币对应map*/
    protected Map<Integer,List<Integer>> getGoodsCoinMap(){
        Map<Integer, List<Integer>> goodsMap = configuration.getSpotCoinPair();
        if (goodsMap == null){
            goodsMap = new HashMap<>();
        }
        return goodsMap;
    }


    /**获取现货计价币列表*/
    protected List<Integer> getGoodsPerCoinList(){
        List<Integer> goodsPerCoins = new ArrayList<>();
        for (Integer i : getGoodsCoinMap().keySet()) {
            goodsPerCoins.add(i);
        }
        Collections.sort(goodsPerCoins );
        return goodsPerCoins;
    }

    /**获取现货该计价币对应的交易币列表*/
    protected List<Integer> getGoodsTradeList(int perCoin){
        List<Integer> goodsTradeCoins = new ArrayList<>();
        if (getGoodsCoinMap().size() <= 0){
            return goodsTradeCoins;
        }
        goodsTradeCoins = getGoodsCoinMap().get(perCoin);
        return goodsTradeCoins;
    }



    /**获取杠杆交易顶部计价币、交易币对应map*/
    protected Map<Integer,List<Integer>> getLeverCoinMap(){
        Map<Integer, List<Integer>> leverMap = configuration.getLeverageOrderType();
        if (leverMap == null){
            leverMap = new HashMap<>();
        }
        return leverMap;
    }


    /**获取杠杆计价币列表*/
    protected List<Integer> getLeverPerCoinList(){
        List<Integer> leverPerCoins = new ArrayList<>();
        for (Integer i : getLeverCoinMap().keySet()) {
            leverPerCoins.add(i);
        }
        Collections.sort(leverPerCoins );
        return leverPerCoins;
    }

    /**获取杠杆该计价币对应的交易币列表*/
    protected List<Integer> getLeverTradeList(int perCoin){
        List<Integer> leverTradeCoins = new ArrayList<>();
        if (getLeverCoinMap().size() <= 0){
            return leverTradeCoins;
        }
        leverTradeCoins = getLeverCoinMap().get(perCoin);
        return leverTradeCoins;
    }

    /**根据coin的int值获取name*/
    protected String getCoinName(int type) {
        String coinName = getCoinInfo(type).getCoinname();
        return TextUtils.isEmpty(coinName)?"..":coinName;
    }

    /**获取转账类型、名称对应map*/
    protected Map<Integer,String> getAccountMap(){
        Map<Integer,String> accountMap = new HashMap<>();
        accountMap.put(StaticDatas.ACCOUNT_C2C,"法币账户");
        accountMap.put(StaticDatas.ACCOUNT_GOODS,"币币账户");
        return accountMap;
    }

    /**获取转账币种列表*/
    protected List<Integer> getAccountTypeList() {
        List<Integer> accountList = new ArrayList<>();
        for (Integer i : getAccountMap().keySet()) {
            accountList.add(i);
        }
        return accountList;
    }

    /**获取所有算力等级详情*/
    protected List<Configuration.CalculateForceLevel> getForceLevels(){
        Map<Integer, Configuration.CalculateForceLevel> calculateForceLevelMap = getConfiguration().getHonorList();
        if (calculateForceLevelMap == null){
            return new ArrayList<>();
        }
        List<Integer> levels = new ArrayList<>();
        /*先获取所有的key值即所有等级值*/
        levels.addAll(calculateForceLevelMap.keySet());
        /*对等级排序*/
        Collections.sort(levels);
        /*获取所有等级对应的详情*/
        List<Configuration.CalculateForceLevel> allLevelDetails = new ArrayList<>();
        for (Integer level:levels){
            allLevelDetails.add(calculateForceLevelMap.get(level));
        }
        return allLevelDetails;
    }

    /**获取对应的算力等级详情*/
    protected Configuration.CalculateForceLevel getLevelDetail(int level){
        Map<Integer, Configuration.CalculateForceLevel> calculateForceLevelMap = getConfiguration().getHonorList();
        if (calculateForceLevelMap == null){
            calculateForceLevelMap=new HashMap<>();
        }
        Configuration.CalculateForceLevel calculateForceLevel = calculateForceLevelMap.get(level);
        if (calculateForceLevel == null){
            calculateForceLevel = new Configuration.CalculateForceLevel();
        }
        return calculateForceLevel;
    }

    /** 初始化loading*/
    private void showProgressDialog(View view) {
        loadingProgress = new LoadingProgress(mContext,R.style.LoadingDialog,view);
        loadingProgress.show();
    }

    protected boolean isLogin(){
        return Preferences.isLogin();
    }

    protected void log(String log){
        LogUtils.w(TAG,log);
    }
    protected void showToast(String toast){
        if (mContext == null){
            return;
        }
        //LogUtils.w("toast",mContext.getClass().getName().toString());
        MyToast.showToast(mContext,TextUtils.isEmpty(toast)?"null":toast);
    }
    protected void showToastAndLog(String msg){
        showToast(msg);
        log(msg);
    }

    protected void showToast(int toast){
        //LogUtils.w("toast",mContext.getClass().getName().toString());
        String str = mContext.getResources().getString(toast);
        showToast(str);
    }

    protected void onFail(int errorCode,String reason){
        ErrorHandler errorHandler = new ErrorHandler(mContext);
        errorHandler.handlerError(errorCode,reason);
    }

    /**获取输入的交易密码*/
    protected void showPayDialog(final getStringCallback passwordCallback) {
        /*判断是否设置过交易密码*/
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);

        NetWorks.isOrderPwdFlag(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                /*如果设置过交易密码，弹出输入交易密码框*/
                final PayDialog.Builder builder = new PayDialog.Builder(mContext);
                builder.setFinishListener(new PayPsdInputView.onPasswordListener() {
                    @Override
                    public void getInput(String data) {
                        if (!ToolsUtils.isTradePwd(data)) {
                            showToast(getString(R.string.illegal_tradePwd));
                            return;
                        }
                        if (passwordCallback != null) {
                            passwordCallback.getString(data);
                        }
                    }
                });
                builder.setCancelable(true);
                builder.create().show();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**清空输入框*/
    protected void clearInput(EditText... views){
        for (EditText edit : views) {
            edit.setText("");
        }
    }

    protected LocalBroadcastManager getmLocalBroadcastManager(){
        if (mLocalBroadcastManager == null){
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        }
        return mLocalBroadcastManager;
    }

    protected void localSendBroadcast(Intent intent){
        getmLocalBroadcastManager().sendBroadcast(intent);
    }

    /**显示加载中进度条*/
    protected void showLoading(View view){
        if (loadingProgress == null){
            showProgressDialog(view);
        }else{
            loadingProgress.show();
        }
    }

    /**隐藏加载中进度条*/
    protected void hideLoading(){
        if (loadingProgress != null) {
            loadingProgress.dismiss();
            loadingProgress = null;
        }
    }

    /**弹出先实名认证对话框*/
    protected void showSetRealNameFirstDialog(){
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("请先进行实名认证");
        builder.setCancelable(true);

        builder.setPositiveButton("去设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Utils.IntentUtils(mContext,SecurityCenterActivity.class);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**弹出申诉对话框*/
    protected void showComplaintDialog() {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("请联系官方客服");
        builder.setCancelable(true);

        builder.setPositiveButton("联系客服",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2Detail = new Intent(mContext, WebDetailActivity.class);
                        intent2Detail.putExtra("title","联系我们");
                        String contactUrl = getConfiguration().getContactusUrl();
                        intent2Detail.putExtra("url", TextUtils.isEmpty(contactUrl)?"":contactUrl);
                        startActivity(intent2Detail);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        complaintDialog = builder.create();
        complaintDialog.show();
    }

    protected void setPasswordShow(EditText view , boolean visible) {
        if (visible) {
            /*显示密码明文*/
            view.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            /*显示密码密文*/
            view.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        int currentIndex = view.getText().toString().length();
        view.setSelection(currentIndex > 0 ? currentIndex : 0);
    }

    /**
     * 获取网络状态
     * @return 是否联网
     */
    protected boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        if (manager == null) {
            manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (complaintDialog != null && complaintDialog.isShowing()){
            complaintDialog.dismiss();
        }
        mContext = null;
        ActivityController.removeActivity(this);
        if (unbinder != null){
            unbinder.unbind();
        }
        LogUtils.w("app","Destroy当前activity数量："+ActivityController.getSize());
    }

    //双击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*如果是MainActivity界面的话，点击两次则退出*/
            if (getClass().getName().equals(MainActivity.class.getName())){
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    showToast(R.string.onceMoreExit);
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                    ActivityController.closeAllActivity();
                    LogUtils.w("exit","BaseActivityFinish");
                }
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
