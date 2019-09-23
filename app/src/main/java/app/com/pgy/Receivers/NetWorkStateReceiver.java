package app.com.pgy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: 网络状态改变监听
 * @author 徐庆重
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    private int currentNetwork;
    getNetworkInterface getNetworkInterface;
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                currentNetwork = NETWORK_WIFI;
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                currentNetwork = NETWORK_WIFI;
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                currentNetwork = NETWORK_MOBILE;
            } else {
                currentNetwork = NETWORK_NONE;
            }
        }else {
            //获取所有网络连接的信息
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                    currentNetwork = NETWORK_WIFI;
                } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                    currentNetwork = NETWORK_MOBILE;
                }
            } else {
                currentNetwork = NETWORK_NONE;
            }

        }
        if (getNetworkInterface != null){
            getNetworkInterface.getNetworkState(currentNetwork);
        }
        /*if (intent.getAction().equals(ACTION_LOGIN) || intent.getAction().equals(ACTION_LOGOUT)){
            *//*接收到登录和退出信号时，触发方法*//*
            LogUtils.w("receiver","Receiver接收到ACTION"+intent.getAction());
            if (getNetworkInterface != null) {
                getNetworkInterface.onLogin StateChanged();
                LogUtils.w("receiver","Receiver放入接口中");
            }
        }*/
    }

    public interface getNetworkInterface{
        void getNetworkState(int state);
        //void onLoginStateChanged();
    }

    public void setGetNetworkInterface(NetWorkStateReceiver.getNetworkInterface getNetworkInterface) {
        this.getNetworkInterface = getNetworkInterface;
    }

    public static String getNetWorkState(int state) {
        String stateStr;
        switch (state){
            case NETWORK_NONE:
                stateStr="当前无网络，请开启网络";
                break;
            case NETWORK_MOBILE:
                stateStr = "当前为移动网络，请慎重使用";
                break;
            case NETWORK_WIFI:
                stateStr = "当前为WIFI，请随意使用";
                break;
            default:
                stateStr = "当前网络未知，请检查设备";
                break;
        }
        return stateStr;
    }
}
