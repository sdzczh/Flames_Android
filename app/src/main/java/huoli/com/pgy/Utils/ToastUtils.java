package huoli.com.pgy.Utils;

import android.content.Context;
import android.widget.Toast;

import huoli.com.pgy.Constants.Constants;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: Toast工具类
 *
 * @author 徐庆重
 */

public class ToastUtils {
    //上线时，将Debug设为false，就不会出现log信息了
    public static void ShortToast(Context c , String message) {
        if(Constants.DEBUG) {
            Toast.makeText(c,message,Toast.LENGTH_SHORT).show();
        }
    }
    public static void LongToast(Context c , String message) {
        if(Constants.DEBUG) {
            Toast.makeText(c,message,Toast.LENGTH_LONG).show();
        }
    }
}
