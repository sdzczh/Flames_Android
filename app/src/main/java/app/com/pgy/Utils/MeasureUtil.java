package app.com.pgy.Utils;

import android.content.Context;

/**
 * https://github.com/liuwan1992/CustomProgressBar/blob/master/app/src/main/java/com/liuwan/customprogressbar/MeasureUtil.java
 */
public class MeasureUtil {

    /**
     * 说明：根据手机的分辨率将dp转成为px
     * 作者：刘婉
     * 添加时间：2017/3/22 13:08
     * 修改人：刘婉
     * 修改时间：2017/3/22 13:08
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 说明：根据手机的分辨率将sp转成为px
     * 作者：刘婉
     * 添加时间：2017/3/22 18:59
     * 修改人：刘婉
     * 修改时间：2017/3/22 18:59
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}