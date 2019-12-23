package app.com.pgy.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 创建时间：2018/7/27
 * 编写人：whe by Administrator wh9684@outlook.com
 * 功能描述：
 */

public class BBigDecimal extends BigDecimal {

    /**
     *
     */
    private static final long serialVersionUID = -8187328246570289879L;

    public BBigDecimal(BigInteger val) {
        super(val);
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1
     *            被加数
     * @param value2
     *            加数
     * @return 两个参数的和
     */
    public static double add(double value1, double value2) {
        BigDecimal a = new BigDecimal(Double.toString(value1));
        BigDecimal b = new BigDecimal(Double.toString(value2));
        return a.add(b).doubleValue();
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1
     *            被加数
     * @param value2
     *            加数
     * @param scale
     *            精确范围
     * @return 两个参数的和
     * @throws IllegalAccessException
     */
    public static double add(double value1, double value2,int scale) throws IllegalAccessException {
        BigDecimal a = new BigDecimal(Double.toString(value1));
        BigDecimal b = new BigDecimal(Double.toString(value2));
        // 如果精确范围小于0，抛出异常信息
        if(scale<0){
            throw new IllegalAccessException("精确度不能小于0");
        }
        a=a.setScale(scale);//必须重新赋值
        b=b.setScale(scale);//必须重新赋值
        return a.add(b).setScale(scale).doubleValue();
    }

    /**
     * 提供精确减法运算的subtract方法
     *
     * @param value1
     *            被减数
     * @param value2
     *            减数
     * @return 两个参数的差
     */
    public static double subtract(double value1, double value2) {
        BigDecimal a = new BigDecimal(Double.toString(value1));
        BigDecimal b = new BigDecimal(Double.toString(value2));
        return a.subtract(b).doubleValue();
    }

    /**
     * 提供精确减法运算的subtract方法
     *
     * @param value1
     *            被减数
     * @param value2
     *            减数
     * @param scale
     *            精确范围
     * @return 两个参数的差
     * @throws IllegalAccessException
     */
    public static double subtract(double value1, double value2,int scale) throws IllegalAccessException {
        BigDecimal a = new BigDecimal(Double.toString(value1));
        BigDecimal b = new BigDecimal(Double.toString(value2));
        // 如果精确范围小于0，抛出异常信息
        if(scale<0){
            throw new IllegalAccessException("精确度不能小于0");
        }
        a=a.setScale(scale);//必须重新赋值
        b=b.setScale(scale);//必须重新赋值
        return a.subtract(b).setScale(scale).doubleValue();
    }

    /**
     * 提供精确乘法运算的multiply方法
     *
     * @param value1
     *            被乘数
     * @param value2
     *            乘数
     * @return 两个参数的积
     */
    public static double multiply(double value1, double value2) {
        BigDecimal a = new BigDecimal(Double.toString(value1));
        BigDecimal b = new BigDecimal(Double.toString(value2));
        return a.multiply(b).doubleValue();
    }

    /**
     * 提供精确乘法运算的multiply方法
     *
     * @param value1
     *            被乘数
     * @param value2
     *            乘数
     * @param scale
     *            精确范围
     * @return 两个参数的积
     * @throws IllegalAccessException
     */
//    public static double multiply(double value1, double value2,int scale) throws IllegalAccessException {
//        BigDecimal a = new BigDecimal(Double.toString(value1));
//        BigDecimal b = new BigDecimal(Double.toString(value2));
//        // 如果精确范围小于0，抛出异常信息
//        if(scale<0){
//            throw new IllegalAccessException("精确度不能小于0");
//        }
//        a=a.setScale(scale);//必须重新赋值
//        b=b.setScale(scale);//必须重新赋值
//
//        BigDecimal result = a.multiply(b);
//        result = result.setScale(scale,BBigDecimal.ROUND_HALF_UP);
//
//
//        return result.doubleValue();
//    }
    public static double multiply(double value1, double value2,int scale) throws IllegalAccessException {
        BigDecimal a = new BigDecimal(Double.toString(value1));
        BigDecimal b = new BigDecimal(Double.toString(value2));
        // 如果精确范围小于0，抛出异常信息
        if(scale<0){
            throw new IllegalAccessException("精确度不能小于0");
        }
        a=a.setScale(scale,BBigDecimal.ROUND_HALF_UP);//必须重新赋值
        b=b.setScale(scale,BBigDecimal.ROUND_HALF_UP);//必须重新赋值

        BigDecimal result = a.multiply(b);
        result = result.setScale(scale,BBigDecimal.ROUND_HALF_UP);


        return result.doubleValue();
    }

    /**
     * 提供精确的除法运算方法divide
     *
     * @param value1
     *            被除数
     * @param value2
     *            除数
     * @param scale
     *            精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double divide(double value1, double value2, int scale)
            throws IllegalAccessException {
        BigDecimal a = new BigDecimal(Double.toString(value1));
        BigDecimal b = new BigDecimal(Double.toString(value2));
        // 如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        a=a.setScale(scale);//必须重新赋值
        b=b.setScale(scale);//必须重新赋值
        return a.divide(b,scale).doubleValue();
    }


}
