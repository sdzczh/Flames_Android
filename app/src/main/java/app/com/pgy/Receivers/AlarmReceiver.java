package app.com.pgy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.com.pgy.Services.HeartbeatService;
import app.com.pgy.Utils.LogUtils;

/**
 * Created by Administrator on 2018/2/9 0009.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.print("AlarmReceiver,接收到唤醒广播");
        Intent i = new Intent(context, HeartbeatService.class);
        context.startService(i);
    }
}
