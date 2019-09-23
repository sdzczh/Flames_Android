package app.com.pgy.Fragments.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import app.com.pgy.Constants.ErrorHandler;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.PushBean.PushData;
import app.com.pgy.Models.Beans.PushBean.SocketRequestBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Services.MyWebSocket;
import app.com.pgy.Utils.JsonUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import app.com.pgy.Widgets.LoadingProgress;
import app.com.pgy.Widgets.MyToast;
import app.com.pgy.Widgets.PayDialog;
import app.com.pgy.Widgets.PayPsdInputView;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: Fragment基类
 * 用户是否登录、网络状态改变监听、toast、onFail错误处理
 *
 * @author 徐庆重
 */
public abstract class BaseFragment extends Fragment{
    protected String TAG = "BaseFragment";
    protected Context mContext;
    private ConnectivityManager manager;
    private boolean isActive;
    /** 加载中环形进度条*/
    private LoadingProgress loadingProgress;
    /** 配置文件*/
    private Configuration configuration;
    /**fragment界面是否显示，在onCreateView中设置为true*/
    protected boolean isViewCreated = false;
    //protected NetWorkStateReceiver netWorkStateReceiver;
    protected InputMethodManager imm;
    private MyWebSocket myWebSocket;
    private LocalBroadcastManager localBroadcastManager;
    public abstract int getContentViewId();
    private Unbinder unbinder;
    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (mContext == null){
            mContext = getContext();
        }
        TAG = getClass().getSimpleName();
        Preferences.init(mContext);
        /*初始化配置文件*/
        configuration = Preferences.getConfiguration();
        imm = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    protected void hideSoftKeyboard(Activity activity){
        //isOpen若返回true，则表示输入法打开
        if (activity == null){
            return;
        }
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null){
            return;
        }
        IBinder windowToken = currentFocus.getWindowToken();
        boolean isOpen=imm.isActive();
        if (isOpen && imm != null && windowToken!=null){
            imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        isViewCreated = true;
        rootView =inflater.inflate(getContentViewId(),container,false);
        unbinder = ButterKnife.bind(this,rootView);//绑定framgent
        initData();
        initView(savedInstanceState);
        return rootView;
    }

    protected abstract void initData();
    protected abstract void initView(Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null){
            unbinder.unbind();
        }
    }

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

    /**获取默认现货档位*/
    protected Integer getDefaultGear(){
        int size = getGearList().size();
        return size >= 1?getGearList().get(size-1):10;
    }

    /**获取充值、提现、资金划转币种列表*/
    protected List<Integer> getRechAndWithCoinTypeList() {
        List<Integer> rechAndWithCoinTypeList = getConfiguration().getRechAndWithCoin();
        if (rechAndWithCoinTypeList == null){
            rechAndWithCoinTypeList = new ArrayList<>();
        }
        return rechAndWithCoinTypeList;
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
        goodsPerCoins.addAll(getGoodsCoinMap().keySet());
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

    /**获取配置文件中所有币种列表*/
    protected List<Integer> getCoinTypeList(){
        Map<Integer, Configuration.CoinInfo> coinInfoMap = getConfiguration().getCoinInfo();
        if (coinInfoMap == null){
            coinInfoMap = new HashMap<>();
        }
        List<Integer> coins = new ArrayList<>();
        coins.addAll(coinInfoMap.keySet());
        Collections.sort(coins);
        return coins;
    }

    /**根据coin的int值获取name*/
    protected String getCoinName(int type) {
        String coinName = getCoinInfo(type).getCoinname();
        return TextUtils.isEmpty(coinName)?"..":coinName;
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
        loadingProgress = new LoadingProgress(mContext, R.style.LoadingDialog,view);
        loadingProgress.show();
    }
    protected boolean isLogin(){
        return Preferences.isLogin();
    }
    protected void isJump2Login() {
        if (!isLogin()){
            showToast(R.string.unlogin);
            //Utils.IntentUtils(mContext,LoginActivity.class);
        }
    }

    protected void log(String log){
        LogUtils.w(TAG,log);
    }
    protected void showToast(String toast){
        if (!isActive){
            return;
        }
        MyToast.showToast(mContext,TextUtils.isEmpty(toast)?"null":toast);
    }
    protected void showToastAndLog(String msg){
        showToast(msg);
        log(msg);
    }

    protected void showToast(int toast){
        if (!isActive){
            return;
        }
        String str = mContext.getResources().getString(toast);
        showToast(str);
    }

    /**显示加载中进度条*/
    protected void showLoading(View view){
        if (!isActive){
            return;
        }
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
            LogUtils.w("clear","清空的Edit："+edit.toString()+",text:"+edit.getText());
        }
    }

    protected LocalBroadcastManager getLocalBroadcastManager(){
        if (localBroadcastManager == null){
            localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        }
        return localBroadcastManager;
    }

    protected void localSendBroadcast(Intent intent){
        getLocalBroadcastManager().sendBroadcast(intent);
    }

    /**发送长连接消息*/
    protected void sendSocketMessage(SocketRequestBean message) {
        String msg = JsonUtils.serialize(message);
        //LogUtils.w("HeartBeat","向服务器发送消息："+msg+","+ TimeUtils.timeStampInt());
        if (myWebSocket == null){
            myWebSocket = MyWebSocket.getMyWebSocket();
        }
        if (myWebSocket!=null && myWebSocket.isConnect()) {
            myWebSocket.sendRequestMessage(message);
        } else {
            LogUtils.w("HeartBeat","BaseFragment未连接");
            myWebSocket.handlerDrops("BaseFragment未连接",false,true);
        }
    }

    /**切换长连接场景*/
    protected boolean switchScene(PushData toData){
        PushData currentPushData = Preferences.getCurrentPushData();
        if (toData!=null && currentPushData!=null && toData.equals(currentPushData)){
            LogUtils.w("switch","进入场景和当前场景相同不切换");
            return false;
        }
        if (currentPushData != null){
            LogUtils.w("switch","离开"+currentPushData.toString());
            SocketRequestBean outDigFrame = new SocketRequestBean();
            outDigFrame.setCmsg_code(currentPushData.getScene());
            outDigFrame.setAction("leave");
            outDigFrame.setData(currentPushData);
            sendSocketMessage(outDigFrame);
            /*离开之后，将本地设置为空*/
            Preferences.setCurrentPushData(null);
            //LogUtils.w("switch","本地场景："+ (Preferences.getCurrentPushData() == null?"为空":"不为空"));
        }

        if (toData != null){
            LogUtils.w("switch","进入"+toData.toString());
            SocketRequestBean inFrame = new SocketRequestBean();
            inFrame.setCmsg_code(toData.getScene());
            inFrame.setAction("join");
            inFrame.setData(toData);
            sendSocketMessage(inFrame);
            /*进入场景后，设置本地*/
            Preferences.setCurrentPushData(toData);
        }
        return  true;
    }

    @Override
    public void onResume() {
        isActive = true;
        if (myWebSocket == null){
            myWebSocket = MyWebSocket.getMyWebSocket();
        }
        super.onResume();
    }

    /**
     * 获取网络状态
     * @return 是否联网
     */
    protected boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        try {
            if (manager == null) {
                manager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            }
            //去进行判断网络是否连接
            if (manager.getActiveNetworkInfo() != null) {
                flag = manager.getActiveNetworkInfo().isAvailable();
            }
        }catch (Exception e){
        }
        return flag;
    }

    @Override
    public void onStop() {
        isActive = false;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (netWorkStateReceiver != null){
            getLocalBroadcastManager().unregisterReceiver(netWorkStateReceiver);
            LogUtils.w("receiver","BaseFragment解除监听");
        }*/
        mContext = null;
    }

    /*@Override
    public void getNetworkState(int state) {
        LogUtils.w("receiver","BaseFragment:getNetworkState");
    }*/

   /* @Override
    public void onLoginStateChanged() {
        LogUtils.w("receiver","BaseFragment:onLoginStateChanged"+isLogin());

    }*/
    /**登录状态监听*/
   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void Events(EventLoginState isLoged) {
        *//*在父类中检测登录状态，若登录则init，若退出则停止socket*//*
        if (isLogin()){
            if (myWebSocket == null){
                myWebSocket = MyWebSocket.getMyWebSocket();
            }
            myWebSocket.sendInitMessage();
            LogUtils.print("BaseFragment，成功登录,发送init");
        } else{
            disConnectSocket();
        }
    }*/

    private void disConnectSocket(){
        LogUtils.print("BaseFragment，退出登录,断开socket连接");
        LogUtils.w("HeartBeat","BaseFragment断开连接");
        /*socket断开*/
        if (myWebSocket == null){
            myWebSocket = MyWebSocket.getMyWebSocket();
        }
        myWebSocket.stopSocket();
    }

}
