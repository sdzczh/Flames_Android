package huoli.com.pgy.Utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;
import java.io.File;

import huoli.com.pgy.R;

/**
 * @author xuqingzhong
 * 版本更新工具
 */

public class DownloadUtils {
    private DownloadManager downloadManager;
    private Context mContext;
    private long downloadId;
    private File apkFile;

    public DownloadUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**下载apk*/
    public void downloadAPK(String url, String name) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(mContext.getResources().getString(R.string.app_name));
        request.setDescription("app新版本下载");
        request.setVisibleInDownloadsUi(true);
        //设置下载的路径
        apkFile = new File(FileUtils.getLocalPath()+"/"+name);
        request.setDestinationInExternalPublicDir("myphoto",name);
        LogUtils.w("download","fileName;"+apkFile.getPath());
        //获取DownloadManager
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        downloadId = downloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        LocalBroadcastManager.getInstance(mContext).registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**广播监听下载的各个状态*/
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.w("download","接收到广播");
            checkStatus();
        }
    };


    /**检查下载状态*/
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    LogUtils.w("download","下载暂停");
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    LogUtils.w("download","下载延迟");
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    //showToast("正在下载，请稍后");
                    LogUtils.w("download","正在下载");
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    LogUtils.w("download","下载完成");
                    /*安装完解除监听*/
                    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
                    installAPK();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    showToast("下载失败");
                    LogUtils.w("download","下载失败");
                    break;
                default:break;
            }
        }
        c.close();
    }

    private void showToast(String toast){
        if (TextUtils.isEmpty(toast)){
            return;
        }
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    /**下载到本地后执行安装*/
    private void installAPK() {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!apkFile.exists()){
            showToast("安装包下载失败");
            return;
        }
        //判读版本是否在7.0以上
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            //获取下载文件的Uri
            //在AndroidManifest中的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(mContext, "huoli.com.onecoin.fileprovider", apkFile);
            /*△添加这一句表示对目标应用临时授权该Uri所代表的文件*/
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri,"application/vnd.android.package-archive");
        } else{
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        if (mContext == null){
            return;
        }
        mContext.startActivity(install);
    }

}