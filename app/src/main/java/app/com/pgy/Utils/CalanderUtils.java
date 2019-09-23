package app.com.pgy.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import app.com.pgy.Constants.Preferences;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/24 14:48
 */
public class CalanderUtils {

    //Android2.2版本以后的URL，之前的就不写了
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    //添加账户
    public static void createCalendars(Context context) {

        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, Preferences.getLocalUser().getName());

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, Preferences.getLocalUser().getPhone());
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange");
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "odin");
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, -9206951);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, Preferences.getLocalUser().getPhone());
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "mygmailaddress@gmail.com")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange")
                .build();

        context.getContentResolver().insert(calendarUri, value);
    }

    public static int getUserCursor(Context context){
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);
        LogUtils.e("cursor","Count: " + userCursor.getCount());

        return userCursor.getCount();
    }

    public static void addCalanderEvent(Context context,String title,String message,int hour){
        // 获取要出入的gmail账户的id
        String calId = "";
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToLast();  //注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));
        } else {
            createCalendars(context);
        }

        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", message);
        // 插入账户
        event.put("calendar_id", calId);
//        System.out.println("calId: " + calId);
//        event.put("eventLocation", "测试地点");
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, hour-1);
        mCalendar.set(Calendar.MINUTE, 45);
        long start = mCalendar.getTime().getTime();
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, 0);
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);//开始时间
        event.put(CalendarContract.Events.DTEND, end);//结束时间
        //设置一个全天事件的条目
        //event.put("allDay", 1); // 0 for false, 1 for true
        //事件状态暂定(0)，确认(1)或取消(2)
        //event.put("eventStatus", 1);
        //控制是否事件触发报警，提醒如下
        event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true
        //设置时区,否则会报错
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        //添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);
        //事件提醒的设定
        long id = Long.parseLong(newEvent.getLastPathSegment());
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, id);
        // 提前5分钟有提醒(提前0分钟时，值为0)
        values.put(CalendarContract.Reminders.MINUTES, 5);
        values.put(CalendarContract.Reminders.METHOD, 1);
        Uri uri = context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
        if (uri == null) {
            // 添加闹钟提醒失败直接返回
            Toast.makeText(context, "设置提醒失败!!!", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(context, "设置提醒成功!!!", Toast.LENGTH_LONG).show();
    }
}
