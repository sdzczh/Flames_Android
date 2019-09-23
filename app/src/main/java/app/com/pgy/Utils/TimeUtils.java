package app.com.pgy.Utils;

import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: 时间工具类
 *
 * @author 徐庆重
 */

public class TimeUtils {
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds   精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(int seconds, String format) {
        if (format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 获取时间与当前时间的差，转换成几分钟以前，几天前等
     * @param dateTimeStamp 评论的时间戳 精确到秒
     * @return
     */
    public static String getDateDiff(int dateTimeStamp) {
        int minute =  60;
        int hour = minute * 60;
        int day = hour * 24;
        int month = day * 30;
        int year = month * 12;
        //获取当年时间，精确到秒
        int now = timeStampInt();
        int diffValue = now - dateTimeStamp;
        if (diffValue < 0) {
            return "...";
        }
        String result;
        int monthC = diffValue / month;
        int weekC = diffValue / (7 * day);
        int dayC = diffValue / day;
        int hourC = diffValue / hour;
        int minC = diffValue / minute;
        int yearC = diffValue / year;
        if (yearC >= 1){
            result = "" + String.valueOf(yearC) + "年前";
        }else if (monthC >= 1) {
            result = "" + String.valueOf(monthC) + "月前";
        } else if (weekC >= 1) {
            result = "" + String.valueOf(weekC) + "周前";
        } else if (dayC >= 1) {
            result = "" + String.valueOf(dayC) + "天前";
        } else if (hourC >= 1) {
            result = "" + String.valueOf(hourC) + "小时前";
        } else if (minC >= 1) {
            result = "" + String.valueOf(minC) + "分钟前";
        } else {
            result = "刚刚";
        }
        return result;
    }

    /**
     * 将时间戳转换成几岁几个月
     * @param dateTimeStamp
     * @return
     */
    public static String getBabyAge(int dateTimeStamp) {
        int minute =  60;
        int hour = minute * 60;
        int day = hour * 24;
        int month = day * 30;
        int year = month * 12;
        //获取当年时间，精确到秒
        int now = timeStampInt();
        int diffValue = now - dateTimeStamp;
        if (diffValue < 0) {
            return "...";
        }
        String result = "";
        int monthC = diffValue / month;
        int yearC = diffValue / year;
        if (yearC >= 1){
            result = "" + String.valueOf(yearC) + "岁";
        }else if (monthC >= 1) {
            result = "" + String.valueOf(monthC) + "个月";
        }else {
            result = "婴儿";
        }
        return result;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStamp   时间戳
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(int dateStamp, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(String.valueOf(dateStamp*1000)).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int getMonthSpace(String date1, String date2){
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(sdf.parse(date1));
            c2.setTime(sdf.parse(date2));
        }catch (Exception e){

        }

        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

        return result == 0 ? 1 : Math.abs(result);

    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return String字符串
     */
    public static String timeStampStr() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return int数值
     */
    public static int timeStampInt() {
        int t = (int) (System.currentTimeMillis()/1000);
        return t;
    }

    /**
     * 取得当前时间戳（精确到毫秒）
     * @return long数值
     */
    public static long timeStampLong() {
        long l = System.currentTimeMillis();
        return l;
    }

    /**上传时间戳=本地时间戳+两端时间差*/
    public static long getUpLoadTime(){
        return  timeStampLong();// + Preferences.getDeltaTime();
    }

    //获取当天的开始时间
    public static int getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int t = (int) (cal.getTimeInMillis()/1000);
        return t;
    }
    //获取当天的结束时间
    public static int getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        int t = (int) (cal.getTimeInMillis()/1000);
        return t;
    }
    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }
    //获取本周的结束时间
    public static Date getEndDayOfWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }
    //获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }
    //获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }
    //获取本年的开始时间
    public static Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        // cal.set
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);

        return getDayStartTime(cal.getTime());
    }
    //获取本年的结束时间
    public static Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }
    //获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d){ calendar.setTime(d);}
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    //获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }
    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(Calendar.YEAR));
    }
    //获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.MONTH) + 1;
    }
    //两个日期相减得到的天数
    public static int getDiffDays(Date beginDate, Date endDate) {

        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }

        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);

        int days = new Long(diff).intValue();

        return days;
    }
    //两个日期相减得到的毫秒数
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }
    //获取两个日期中的最大日期
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }
    //获取两个日期中的最小日期
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }
    //返回某月该季度的第一个月
    public static Date getFirstSeasonDate(Date date) {
        final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = SEASON[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        return cal.getTime();
    }
    //返回某个日期下几天的日期
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }
    //返回某个日期前几天的日期
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }
    //获取某年某月到某年某月按天的切片日期集合（间隔天数的日期集合）
    public static List getTimeList(int beginYear, int beginMonth, int endYear,
                                   int endMonth, int k) {
        List list = new ArrayList();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getTimeList(beginYear, j, k));

            }
        } else {
            {
                for (int j = beginMonth; j < 12; j++) {
                    list.add(getTimeList(beginYear, j, k));
                }

                for (int i = beginYear + 1; i < endYear; i++) {
                    for (int j = 0; j < 12; j++) {
                        list.add(getTimeList(i, j, k));
                    }
                }
                for (int j = 0; j <= endMonth; j++) {
                    list.add(getTimeList(endYear, j, k));
                }
            }
        }
        return list;
    }
    //获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
    public static List getTimeList(int beginYear, int beginMonth, int k) {
        List list = new ArrayList();
        Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
        int max = begincal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i < max; i = i + k) {
            list.add(begincal.getTime());
            begincal.add(Calendar.DATE, k);
        }
        begincal = new GregorianCalendar(beginYear, beginMonth, max);
        list.add(begincal.getTime());
        return list;
    }

    public static String dateFormat_day = "HH:mm";
    public static String dateFormat_month = "MM-dd";

    /**
     * 时间转换成字符串,默认为"yyyy-MM-dd HH:mm:ss"
     *
     * @param time 时间
     */
    public static String dateToString(long time) {
        //return dateToString(time, "yyyy.MM.dd HH:mm");
        return dateToString(time, "MM-dd HH:mm");
    }

    /**
     * 时间转换成字符串,指定格式
     *
     * @param time   时间
     * @param format 时间格式
     */
    public static String dateToString(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    //把字符串2017-10-09 14:26:28转换为日期
    private static Date strToDate(String timeStr) throws Exception{
        if (TextUtils.isEmpty(timeStr)){
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(timeStr);
        return date;
    }

    //把日期转为14:26时间字符串
    private static String DateToString(Date date){
        if (date == null){
            return "";
        }
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);
    }

    /**
     * "createTime":"2017-10-09 14:26:28"折线图中的createTime格式要转换成14:26
     * @param fullTime
     * @return
     */
    public static String timeFormat(String fullTime){
        String formatTime = "";
        try {
            formatTime = DateToString(strToDate(fullTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatTime;
    }

    /***
     * 将时间转换成倒计时
     * 如： 将600秒 -> 10:00
     *      将12个小时 -> 12:00:00
     * @param time 秒数
     * @return
     */
    public static String seconds2CountDownTime(int time){
        int hh = time / 60 / 60 % 60;
        int mm = time / 60 % 60;
        int ss = time % 60;
        if (hh > 0){
            return hold2Digits(hh) + ":" +hold2Digits(mm)+":"+hold2Digits(ss);
        }else{
            return hold2Digits(mm)+":"+hold2Digits(ss);
        }
    }

    /***
     * 保持两位数 如 9 -> 09
     * @param before
     * @return
     */
    private static String hold2Digits(int before){
        if (before >= 60){
            return "59";
        }else if (before < 10){
            return "0"+before;
        }else{
            return String.valueOf(before);
        }
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
    }
}