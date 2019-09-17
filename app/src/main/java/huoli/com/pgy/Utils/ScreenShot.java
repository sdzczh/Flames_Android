package huoli.com.pgy.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 截屏工具
 *
 * @author xuqingzhong
 */
public class ScreenShot {
    private static final String TAG = "ScreenShot";
    private static final int SAVE_AUTHORITY = Context.MODE_PRIVATE;

    // 获取指定Activity的截屏，保存到png文件
    private static Bitmap takeScreenShot(Activity activity) {
        //View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();


//获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

//获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();


//去掉标题栏
//Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }


    //保存到sdcard
    private static void savePic(Activity act, Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
//            fos = new FileOutputStream(strFileName);
            fos = act.openFileOutput(strFileName, SAVE_AUTHORITY);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            LogUtils.w(TAG, "savePic e = " + e.toString());
        }
    }

    private static void shareAct(Activity act, String fileName, String text) {

        Uri uri = null;

        try {
            FileInputStream input = act.openFileInput(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(act.getContentResolver(), bitmap, null, null));
            input.close();
        } catch (Exception e) {
            LogUtils.w(TAG, "shareAct e = " + e.toString());
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));

//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        File file = new File(absolutePath);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        shareIntent.setType("image/jpeg");
//        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));
//
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");//intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
//        intent.putExtra(Intent.EXTRA_TEXT, text);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent.createChooser(intent, getTitle()));
    }

    public static void share(Activity act, String text) {
        String saveFileName = "share_pic.jpg";
        savePic(act, ScreenShot.takeScreenShot(act), saveFileName);
        shareAct(act, saveFileName, text);
    }
}
