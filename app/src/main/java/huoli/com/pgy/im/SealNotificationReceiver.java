package huoli.com.pgy.im;

import android.content.Context;

import huoli.com.pgy.Utils.LogUtils;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;


public class SealNotificationReceiver extends PushMessageReceiver {
    private static final String TAG = "SealNotificationReceive";
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        LogUtils.w(TAG,"接收到通知");
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        LogUtils.w(TAG,"点击通知");
        return false;
    }

}
