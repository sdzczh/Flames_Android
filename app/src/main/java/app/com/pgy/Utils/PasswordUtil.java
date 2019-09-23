package app.com.pgy.Utils;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import app.com.pgy.R;

/**
 * Created by YX on 2018/7/6.
 */

public class PasswordUtil {

    public static void setPasswordShow(EditText view , ImageView iv, boolean visible) {
        if (visible) {
            /*显示密码明文*/
            view.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            iv.setImageResource(R.mipmap.pw_show);
        } else {
            /*显示密码密文*/
            view.setTransformationMethod(PasswordTransformationMethod.getInstance());
            iv.setImageResource(R.mipmap.pw_unshow);
        }
        int currentIndex = view.getText().toString().length();
        view.setSelection(currentIndex > 0 ? currentIndex : 0);
    }
}
