package huoli.com.pgy.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import huoli.com.pgy.Activitys.Base.ActivityController;
import huoli.com.pgy.Models.Beans.PushBean.SocketRequestBean;
import huoli.com.pgy.Receivers.AlarmReceiver;
import huoli.com.pgy.Utils.LogUtils;

/**
 * 心跳服务
 * @author  xuqingzhong
 */

public class HeartbeatService extends Service{
    private static final String TAG = "HeartbeatService";
    private static final String PING = "ping";
    /**睡眠时间*/
    private static final int DELAY_SECOND = 6000;
    private MyWebSocket myWebSocket;
    private ExecutorService executorService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //flags = START_FLAG_REDELIVERY ;
        LogUtils.w(TAG, "service onStart");
        if (ActivityController.getSize() <= 0){
            LogUtils.w(TAG, "service onStart activityController中没有activity");
            return START_STICKY_COMPATIBILITY;
        }
        if (intent == null){
            LogUtils.w(TAG, "service onStart intent 为null");
            return super.onStartCommand(intent, flags, startId);
        }
        if (myWebSocket == null){
            myWebSocket = MyWebSocket.getMyWebSocket();
            myWebSocket.isConnect();
        }
        LogUtils.print("HeartbeatService,onStartCommand开启心跳轮询");
        if (executorService == null){
            /*自定义线程池*/
            int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
            int KEEP_ALIVE_TIME = 1;
            TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
            BlockingQueue<Runnable> taskQueue = new LinkedBlockingDeque<>();
            executorService = new ThreadPoolExecutor(NUMBER_OF_CORES,NUMBER_OF_CORES*2,KEEP_ALIVE_TIME,KEEP_ALIVE_TIME_UNIT,taskQueue);
        }
        executorService.execute(new MyHeartBeatTask());
        return super.onStartCommand(intent, flags, startId);
    }

    /**运行心跳轮询*/
    private class MyHeartBeatTask extends TimerTask {
        MyHeartBeatTask() {}
        @Override
        public void run() {
            handlerSend.sendEmptyMessage(1);
            sendAlarm();
        }
    }

    private void sendAlarm() {
    /*隔6s后，发送广播，广播接收到后再唤醒service的onStartCommand方法*/
        AlarmManager manager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + DELAY_SECOND;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        }else{
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        }
    }

    private Handler handlerSend = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                /*发送心跳包*/
                if (myWebSocket == null){
                    myWebSocket = MyWebSocket.getMyWebSocket();
                }
                myWebSocket.sendRequestMessage(new SocketRequestBean(PING,"1"));
            }
        }

    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        /*当停止心跳服务的时候，停止socket，停止轮询，停止广播*/
        if (myWebSocket == null){
            myWebSocket = MyWebSocket.getMyWebSocket();
        }
        myWebSocket.stopSocket();
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
        cancelAlarm();
        LogUtils.w("exit","HeartbeatOnDestroy,connect=null");
        LogUtils.print("HeartbeatService,onDestroy，停止心跳服务");
    }

    private void cancelAlarm() {
        LogUtils.print("HeartbeatService,cancelAlarm，取消心跳广播");
        AlarmManager manager=(AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(this, 0, i,0);
        if (manager != null) {
            manager.cancel(pi);
        }
    }

}