package app.com.pgy.Activitys.Base;

import android.content.Intent;
import androidx.annotation.NonNull;
import java.util.List;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;

/**
 * @author xuqingzhong
 * @描述	      ${Activity基类 }
 * @更新描述   ${适配6.0权限问题}
 */
public abstract class PermissionActivity extends BaseActivity implements
        EasyPermissions.PermissionCallbacks {

    protected static final int RC_PERM = 123;

    protected static int reSting = R.string.ask_again;//默认提示语句

    /**
     * 权限回调接口
     */
    private CheckPermListener mListener;

    public interface CheckPermListener {
        /**权限通过后的回调方法*/
        void superPermission();
    }

    public void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
        LogUtils.w(TAG,"permissions:"+mPerms.length);
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null) {
                mListener.superPermission();
                LogUtils.w(TAG,"所有权限都获取到了");
            }
        } else {
            LogUtils.w(TAG,"未获取到的权限："+mPerms.toString());
            EasyPermissions.requestPermissions(this, getString(resString),
                    RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            //设置返回
            LogUtils.w(TAG,"设置返回");
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
       //同意了某些权限可能不是全部
        LogUtils.w(TAG,"同意了某些权限可能不是全部");
    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null) {
            mListener.superPermission();//同意了全部权限的回调
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.perm_tip),
                R.string.setting, R.string.cancel, null, perms);
    }



}
