package huoli.com.pgy.Widgets;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import huoli.com.pgy.R;

/**
 * 自定义Toast
 * @author xuqingzhong
 */

public class MyToast {

    private static TextView myToastNotice;

    public static void showToast(Context context, String message) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.layout_mytoast, null);
        //初始化布局控件
        myToastNotice = toastRoot.findViewById(R.id.myToast_notice);
        //为控件设置属性
        myToastNotice.setText(Html.fromHtml(message));
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.CENTER, 0,0);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
}
