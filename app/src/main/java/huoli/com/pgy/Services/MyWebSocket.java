package huoli.com.pgy.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import huoli.com.pgy.Constants.Constants;
import huoli.com.pgy.Constants.MyApplication;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Models.Beans.PushBean.PushData;
import huoli.com.pgy.Models.Beans.PushBean.SocketRequestBean;
import huoli.com.pgy.Models.Beans.PushBean.SocketResponseBean;
import huoli.com.pgy.Utils.JsonUtils;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;

import static huoli.com.pgy.Constants.Constants.DEBUG;
import static huoli.com.pgy.Constants.Constants.SOCKET_ACTION;

/**
 * @author xuqingzhong
 *         对socket长连接进行封装
 */

public class MyWebSocket {
    private static final String TAG = "Heartbeat";
    private static final String PONG = "pong";
    private static MyWebSocket myWebSocket;
    private Handler handler;
    private Context mContext;
    /**
     * 掉线次数
     */
    private int dropsCount = 0;
    private WebSocketConnection mConnect;
    /**
     * 掉线次数停止服务
     */
    private static final int STOP_COUNTS = 3;
    private LocalBroadcastManager mLocalBroadcastManager;

    public boolean isConnect() {
        if (mConnect == null) {
            mConnect = new WebSocketConnection();
        }
        //connect();
        return mConnect.isConnected();
    }

    public static MyWebSocket getMyWebSocket() {
        if (myWebSocket == null) {
            myWebSocket = new MyWebSocket();
        }
        return myWebSocket;
    }

    private MyWebSocket() {
        mContext = MyApplication.getInstance().getApplicationContext();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    /**
     * webSocket连接，接收服务器消息
     */
    private void connect() {
        LogUtils.print("MyWebSocket,重连connect");
        LogUtils.w(TAG, "重连connect");
        dropsCount = 0;
        if (mConnect == null) {
            mConnect = new WebSocketConnection();
        }
        if (mConnect.isConnected()) {
            return;
        }
        try {
            String socketUrl;
            if (DEBUG) {
                socketUrl = Constants.WS_URL_DEBUG;
            } else {
                socketUrl = Constants.WS_URL;
            }
            mConnect.connect(socketUrl, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    LogUtils.w(TAG, "长连接成功,初始化任务");
                    dropsCount = 0;
                    /*重连长连接成功后，init，发送挖矿请求，初始化场景*/
                    LogUtils.print("MyWebSocket,长连接成功，发送init");
                    Intent i = new Intent(mContext, HeartbeatService.class);
                    mContext.startService(i);
                    /*进入断开连接之前的场景*/
                    PushData currentPushData = Preferences.getCurrentPushData();
                    if (currentPushData != null) {
                        SocketRequestBean inFrame = new SocketRequestBean();
                        inFrame.setCmsg_code(currentPushData.getScene());
                        inFrame.setAction("join");
                        inFrame.setData(currentPushData);
                        sendRequestMessage(inFrame);
                    }
                }

                @Override
                public void onTextMessage(String payload) {
                    LogUtils.w(TAG, "接收到：" + payload);
                    /*{"type":0,"info":"pong","code":111111}*/
                    SocketResponseBean response = JsonUtils.deserialize(payload, SocketResponseBean.class);
                    /*处理返回值*/
                    if (response == null) {
                        LogUtils.w(TAG, "解析返回值错误，response为空");
                    } else {
                        handlerHeartBeatResponse(response);
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    handlerDrops("onClose", false, false);
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
            handlerDrops("异常：" + e.toString(), false, false);
        }
    }

    /**
     * 将币种信息发送
     */
    private void sendResponse2View(SocketResponseBean response) {
        Intent intent = new Intent();
        intent.setAction(SOCKET_ACTION);
        intent.putExtra("response", response);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 处理长连接返回值
     */
    private void handlerHeartBeatResponse(SocketResponseBean response) {
        /*if (TextUtils.isEmpty(response.getCmsgCode())){
            handlerDrops("场景为空",false,false);
        }else */
        if (!TextUtils.isEmpty(response.getCmsgCode()) && response.getCmsgCode().equals("1")) {
            /*心跳*/
            if (!response.getInfo().equals(PONG)) {
                LogUtils.w(TAG, "心跳非pong");
                handlerDrops("接收非pong信息", false, false);
            } else {
                LogUtils.w(TAG, "心跳正常");
                dropsCount = 0;
            }
        } else if (!TextUtils.isEmpty(response.getInfo())) {
            LogUtils.w(TAG, "其他各种场景");
            /*其他各种场景*/
            sendResponse2View(response);
        } else {
            LogUtils.w(TAG, "msg:" + response.getMsg());
        }
    }

    /**
     * 发送消息(暴露)
     */
    public void sendRequestMessage(SocketRequestBean requestBean) {
        /*发送消息*/
        String msg = JsonUtils.serialize(requestBean);
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        LogUtils.w(TAG, "向服务器发送消息：" + msg + "," + TimeUtils.timeStampInt());
        if (mConnect != null && mConnect.isConnected()) {
            try {
                mConnect.sendTextMessage(msg);
            } catch (Exception e) {
                handlerDrops("WebSocketWriter为空 !!", false, true);
            }
        } else {
            handlerDrops("no connection!!", false, true);
        }
    }


    /**
     * 掉线处理
     */
    public void handlerDrops(String errorMsg, boolean isForceCloseSocket, boolean isForceReconnect) {
        LogUtils.w(TAG, "MyWebSocket,errorMsg:" + errorMsg);
        /*强制停止*/
        if (isForceCloseSocket) {
            LogUtils.w(TAG, "强制停止socket");
            return;
        }
        /*强制重连*/
        if (isForceReconnect) {
            LogUtils.w(TAG, "强制重连socket");
            connect();
        } else {
            LogUtils.w(TAG, errorMsg);
            dropsCount++;
            LogUtils.w(TAG, "服务器异常" + dropsCount + "次");
            if (dropsCount >= STOP_COUNTS) {
            /*断网，尝试重连*/
                connect();
            }
        }
    }

    public void stopSocket() {
        LogUtils.print("MyWebSocket,stopSocket关闭socket连接");
        if (mConnect != null) {
            mConnect.disconnect();
            handlerDrops("关闭socket", true, false);
            mConnect = null;
            LogUtils.w("exit", "HeartbeatOnDestroy,connect=null");
        }
    }
}
