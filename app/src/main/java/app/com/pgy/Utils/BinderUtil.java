package app.com.pgy.Utils;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.security.biometrics.face.auth.util.DisplayUtil;

/**
 * Created by gzz on 2020/4/2.
 */

public class BinderUtil {

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) DisplayUtil.dip2px(view.getContext(),200);
        view.setLayoutParams(params);
        view.requestLayout();
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) height;
        view.setLayoutParams(params);
    }


}
