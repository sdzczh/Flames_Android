package app.com.pgy.Utils;

import android.content.Context;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 创建日期：2017/11/21 0021 on 下午 4:26
 * 描述:数学工具类
 *
 * @author 徐庆重
 */
public class MathUtils {
    public static final int getHeightInPx(Context context) {
        final int height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static final int getWidthInPx(Context context) {
        final int width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static final int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    public static final int getWidthInDp(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        int widthInDp = px2dip(context, width);
        return widthInDp;
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**小数位数处理*/
    public static String handle2decimals(String strNum,int scale){
        String strNewNum = "";
        try {
            BigDecimal bigCommAmount = new BigDecimal(strNum).setScale(scale,BigDecimal.ROUND_DOWN);
            strNewNum = bigCommAmount.toPlainString();
        }catch (Exception e){
            e.getStackTrace();
            return strNewNum;
        }
        return strNewNum;
    }

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return Sting
     */
    public static String formatdoubleNumber(double value) {
        String result;
        if (value <= 0){
            result = "0.00";
        }else{
            BigDecimal bigDecimal = new BigDecimal(value);
            bigDecimal = bigDecimal.setScale(8,BigDecimal.ROUND_HALF_UP);
            result = bigDecimal.stripTrailingZeros().toPlainString();
        }
        /*else if (value > 1){
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            result = df.format(value);
        }else{
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.####");
            result = df.format(value);
        }*/
        return result;

    }

    /**将科学计数法格式化，小数点后八位*/
    public static String formatDoubleNumber(Double value) {
        String result;
        if (value == null || value <= 0){
            result = "0.00";
        }else {
            try{
                BigDecimal bigDecimal = new BigDecimal(value);
                bigDecimal = bigDecimal.setScale(8,BigDecimal.ROUND_HALF_UP);
                result = bigDecimal.stripTrailingZeros().toPlainString();
            }catch (Exception e){
                result = "0.00";
            }
        }
        return result;
    }

    public static String formatDoubleNumber(Double value,String defaultStr){
        String result;
        if (value == null || value <= 0){
            result = defaultStr;
        }else {
            try{
                BigDecimal bigDecimal = new BigDecimal(value);
                bigDecimal = bigDecimal.setScale(8,BigDecimal.ROUND_HALF_UP);
                result = bigDecimal.stripTrailingZeros().toPlainString();
            }catch (Exception e){
                result = defaultStr;
            }
        }
        return result;
    }

    /**将科学计数法格式化，小数点后scale位*/
    public static String formatDoubleNumber(Double value,int scale) {
        int newScale = 2;
        String result;
        if (scale >= 0){
            newScale = scale;
        }
        if (value == null || value <= 0){
            if (newScale > 0){
                result = "0.";
                for (int i = 0;i<newScale;i++){
                    result +="0";
                }
            }else {
                result = "0";
            }
        }else {
            try{
                BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
                bigDecimal = bigDecimal.setScale(newScale,BigDecimal.ROUND_DOWN);
                result = bigDecimal.stripTrailingZeros().toPlainString();
            }catch (Exception e){
                if (newScale > 0){
                    result = "0.";
                    for (int i = 0;i<newScale;i++){
                        result +="0";
                    }
                }else {
                    result = "0";
                }
            }
        }
        return result;
    }

    /**字符串转Integer*/
    public static Integer string2Integer(String text){
        Integer result;
        if (TextUtils.isEmpty(text)){
            return  0;
        }
        try {
            result = Integer.parseInt(text);
        }catch (Exception e){
            result = 0;
        }
        return result;
    }

    /**字符串转Double*/
    public static Double string2Double(String text){
        Double result;
        if (TextUtils.isEmpty(text)){
            return  0.00;
        }

        if (text.startsWith(".")){
            result = Double.parseDouble("0"+text);
        }else{
            try {
                result = Double.parseDouble(text);
            }catch (Exception e){
                result = 0.00;
            }
        }
        return result;
    }


    public static float string2float(String text) {
        float result;
        if (TextUtils.isEmpty(text)){
            return  0;
        }
        try {
            result = Float.parseFloat(text);
        }catch (Exception e){
            result = 0;
        }
        return result;
    }

    /*弃用，将double转string使用上面的formatdouble2String*/
    public static String Double2String(Double dou){
        String result = "0.00";
        if (dou == null || dou < 0){
            return result;
        }
        try {
            result =String.valueOf(dou);
        }catch (Exception e){
            result = "0.00";
        }
        return result;
    }

    public static String int2String(int integer){
        String result = "0";
        if (integer <= 0){
            return result;
        }
        try {
            result = String.valueOf(integer);
        }catch (Exception e){
            result = "0";
        }
        return result;

    }

    public static String getVolUnit(float num) {

        int e = (int) Math.floor(Math.log10(num));

        if (e >= 8) {
            return "亿手";
        } else if (e >= 4) {
            return "万手";
        } else {
            return "手";
        }
    }

    public static int getVolUnitNum(float num) {

        int e = (int) Math.floor(Math.log10(num));

        if (e >= 8) {
            return 8;
        } else if (e >= 4) {
            return 4;
        } else {
            return 1;
        }
    }

    public static DecimalFormat getDecimalFormat(int digits){
        DecimalFormat mFormat;
        if (digits >= 10){
            digits = 10;
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            if (i == 0) {
                b.append(".");
            }
            b.append("0");
        }
        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
        return mFormat;
    }

    public static String getVolUnitText(int unit, float num) {
        /*if (unit == 1) {
            mFormat = new DecimalFormat("#0");
        } else if (unit == 2){
            mFormat = new DecimalFormat("#0.00");
        }else if (unit == 3){
            mFormat = new DecimalFormat("#0.000");
        }else if (unit == 4){
            mFormat = new DecimalFormat("#0.0000");
        }*/
        DecimalFormat mFormat = getDecimalFormat(unit);
        num = num / unit;
        if (num == 0) {
            return "0";
        }
        return mFormat.format(num);
    }

    /**获取当前进度条的进度*/
    public static int getCurrentPercent(int start, int end, int currentForce, int max) {
        if (end <= start){
            return 0;
        }
        if (currentForce <= start){
            return 0;
        }else if (currentForce >= end){
            return max;
        }else{
            return (currentForce-start)*100/(end-start);
        }
    }

    public static String minStr(String str1, String str2) {
        Double first = string2Double(str1);
        Double second = string2Double(str2);
        double min = Math.min(first, second);
        return formatdoubleNumber(min);
    }
}
