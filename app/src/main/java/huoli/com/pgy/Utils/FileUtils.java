package huoli.com.pgy.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import huoli.com.pgy.Constants.Constants;
import huoli.com.pgy.Widgets.MyToast;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: 工具类，获取宽高 dp px格式
 *
 * @author 徐庆重
 */

public class FileUtils {

    public static String getLocalPath(){
        String sDir = "";
        //首先判断sdcard是否插入
        String status = Environment.getExternalStorageState();
        //然后根据是否插入状态指定目录
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            sDir = Constants.LOCAL_PHOTO_PATH;
        } else {
            sDir = Constants.ROOT_PHOTO_PATH;
        }
        //然后是创建文件夹
        File destDir = new File(sDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getAbsolutePath();
    }

    public static String getLocalPhotoPath(){
        String sDir;
        //首先判断sdcard是否插入
        String status = Environment.getExternalStorageState();
        //然后根据是否插入状态指定目录
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            sDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        } else {
            sDir = "/data/data/com/DCIM/Camera/";
        }
        //然后是创建文件夹
        File destDir = new File(sDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getAbsolutePath();
    }

    public static byte[] File2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * Save image to the SD card
     * @param photoBitmap 要保存的Bitmap位图
     * @param photoName 要保存图片的名字,不加后缀，如：13165641615 固定生成".png"图片
     * @param path  要保存图片的本地路径  如：/data/data/com.konglaoshi/
     */
    public static String savePhoto(Bitmap photoBitmap, String path,
                                   String photoName) {
        String localPath = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName + ".png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    // 转换完成
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,fileOutputStream)) {
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            } catch (IOException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localPath;
    }

    /**保存图片到手机指定目录*/
    public static  void saveBitmap(String imgName, byte[] bytes) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = null;
            FileOutputStream fos = null;
            try {
                filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/MyImg";
                File imgDir = new File(filePath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                imgName = filePath + "/" + imgName;
                fos = new FileOutputStream(imgName);
                fos.write(bytes);
                //Toast.makeText(context, "图片已保存到" + filePath, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //Toast.makeText(context, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
        }
    }

    public static String savePhoto(byte[] bytes, String path,String photoName) {
        String localPath = null;
        FileOutputStream fos = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            localPath = path + "/" + photoName + ".png";
            try {
                fos = new FileOutputStream(localPath);
                fos.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localPath;
    }


    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(android.provider.MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /***
     * 根据播放路径设置缩略图
     * @param filePath 视频资源的路径
     * @return 返回缩略图的Bitmap对象
     */
    public static Bitmap getVideoThumbNail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

//    public static boolean imgInsertMedia(Context context,String filepath,String fileName){
//        boolean flag = false;
//        if (context == null){
//            ToastUtils.ShortToast(context,"");
//            return flag;
//        }
//        if (TextUtils.isEmpty(filepath)){
//            ToastUtils.ShortToast(context,"图片地址为空");
//            return flag;
//        }
//        File file = new File(filepath);
//        if (file != null) {
//            try {
//                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, "COIN");
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri uri = Uri.fromFile(file);
//            intent.setData(uri);
//            context.sendBroadcast(intent);
//
//            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
//            flag = true;
//
//        } else {
//            ToastUtils.ShortToast(context,"保存入图册失败");
//        }
//        return flag;
//    }
//
//    public static boolean imgInsertMedia(Context context,String filepath){
//        boolean flag = false;
//        if (context == null){
//            ToastUtils.ShortToast(context,"");
//            return flag;
//        }
//        if (TextUtils.isEmpty(filepath)){
//            ToastUtils.ShortToast(context,"图片地址为空");
//            return flag;
//        }
//        File file = new File(filepath);
//        if (file != null) {
//            try {
//                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), "COIN");
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri uri = Uri.fromFile(file);
//            intent.setData(uri);
//            context.sendBroadcast(intent);
//
//            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
//            flag = true;
//
//        } else {
//            ToastUtils.ShortToast(context,"保存入图册失败");
//        }
//        return flag;
//    }

    /**
     * @param bmp 获取的bitmap数据
     * @param picName 自定义的图片名
     */
    public static void saveBmp2Gallery(Context context,Bitmap bmp, String picName) {

        String fileName = null;
        //系统相册目录
        String galleryPath= Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                +File.separator+"Camera"+File.separator;

        File foder = new File(galleryPath);
        if (!foder.exists()) {
            galleryPath= Environment.getExternalStorageDirectory()
                    + File.separator + Environment.DIRECTORY_DCIM
                    +File.separator;
        }
        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;

        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName+ ".png");

            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.PNG, 90, outStream);
            }
            //通知相册更新
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    bmp, fileName, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            MyToast.showToast(context,picName+"保存入图册成功");
        } catch (Exception e) {
            e.getStackTrace();
            MyToast.showToast(context,"保存入图册失败");
        }finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
