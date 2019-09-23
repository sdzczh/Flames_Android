package app.com.pgy.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import app.com.pgy.Activitys.LoginActivity;
import app.com.pgy.Constants.Preferences;

/**
 * Created by YX on 2018/7/9.
 */

public class LoginUtils {

    public static boolean isLogin(Activity activity){
        if (Preferences.isLogin()){
            return true;
        }else {
            Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
            activity.startActivity(intent);
            return false;
        }
    }
    public static boolean isLogin(Context context){
        if (Preferences.isLogin()){
            return true;
        }else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        }
    }
}
