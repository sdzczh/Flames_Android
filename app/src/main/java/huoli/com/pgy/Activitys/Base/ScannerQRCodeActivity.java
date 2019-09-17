package huoli.com.pgy.Activitys.Base;

import android.Manifest;
import android.content.Intent;

import huoli.com.pgy.Interfaces.getStringCallback;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Widgets.zxing.activity.CaptureActivity;

import static huoli.com.pgy.Widgets.zxing.activity.CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN;

/**
 *扫描二维码，返回地址
 * @author xuqingzhong
 */

public abstract class ScannerQRCodeActivity extends PermissionActivity {
    private static final int SCANNING_CODE = 123;
    /**扫描获取二维码内容后回调的地址*/
    private getStringCallback stringCallback;

    public void setStringCallback(getStringCallback stringCallback) {
        this.stringCallback = stringCallback;
    }

    /**打开相机，开始识别二维码*/
    public void goScanner() {
        /*请求相机权限*/
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                LogUtils.w("permission","ScannerQRCodeActivity:获取相机权限");
                Intent intent = new Intent(ScannerQRCodeActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCANNING_CODE);
            }
        }, R.string.camera, Manifest.permission.CAMERA);
    }

    /**回调扫描出来的二维码内容，放入接口*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*获取二维码的回调*/
            case SCANNING_CODE:
                if (data == null){
                    return;
                }
                String content = data.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
                if (stringCallback != null) {
                    stringCallback.getString(content);
                }
                break;
            default:
                break;
        }
    }
}
