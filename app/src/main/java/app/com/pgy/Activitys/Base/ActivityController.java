package app.com.pgy.Activitys.Base;

import android.app.Activity;
import android.os.Process;

import java.util.LinkedList;
import java.util.List;

import app.com.pgy.Utils.LogUtils;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:Activity控制类，用来管理所有Activity
 *
 * @author 徐庆重
 */
public class ActivityController {
    private static final String TAG = "ActivityController";
    private static List<Activity> activities = new LinkedList<>();

    public static synchronized void addActivity(Activity activity){
        LogUtils.w(TAG,activity.getClass().getSimpleName());
        activities.add(activity);
    }

    public static synchronized void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void closeAllActivity(){
        for (Activity a:activities){
            a.finish();
        }
        //杀死当前进程
        Process.killProcess(Process.myPid());
    }

    public static synchronized int getSize(){
        return activities.size();
    }
}
