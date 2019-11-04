package app.com.pgy.Activitys;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.SimpleUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToastUtils;
import app.com.pgy.Widgets.MyToast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/29 0029
 */
public class PosterNewActivity extends PermissionActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_activity_posters_share_qrcode)
    ImageView ivActivityPostersShareQrcode;
    @BindView(R.id.ll_activity_posters_share_view)
    LinearLayout llActivityPostersShareView;
    @BindView(R.id.ll_activity_posters_share_wx)
    LinearLayout llActivityPostersShareWx;
    @BindView(R.id.ll_activity_posters_share_circle)
    LinearLayout llActivityPostersShareCircle;
    @BindView(R.id.ll_activity_posters_share_save)
    LinearLayout llActivityPostersShareSave;
    @BindView(R.id.tv_activity_posters_share_cancel)
    TextView tvActivityPostersShareCancel;
    private String qrCode;
    @Override
    public int getContentViewId() {
        return R.layout.activity_invitation_poster_new;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        getShareInfo();
    }

    @OnClick({R.id.iv_back, R.id.ll_activity_posters_share_wx, R.id.ll_activity_posters_share_circle, R.id.ll_activity_posters_share_save,R.id.tv_activity_posters_share_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_activity_posters_share_cancel:
                finish();
                break;
            case R.id.ll_activity_posters_share_wx:
                shareImg(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_activity_posters_share_circle:
                shareImg(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_activity_posters_share_save:
                /*请求读写权限*/
                checkPermission(new PermissionActivity.CheckPermListener() {
                    @Override
                    public void superPermission() {
                        savePic();
                    }
                }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
                break;

        }
    }

    private void getShareInfo(){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getShareQrCode(Preferences.getAccessToken(), map, new getBeanCallback<String>() {
            @Override
            public void onSuccess(String o) {
                hideLoading();
                qrCode = o;
                if (TextUtils.isEmpty(qrCode)){
                    showToast("信息为空");
                    finish();
                    return;
                }
                ImageLoaderUtils.display(mContext,ivActivityPostersShareQrcode,qrCode);
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                finish();
            }
        });
    }

    private void savePic(){
        showLoading(null);
        String imgTitle = "pgy_shareImg_"+System.currentTimeMillis();
        // View生成截图
        Bitmap cacheBitmapFromView = SimpleUtils.getCacheBitmapFromView(llActivityPostersShareView);
        // 保存bitmap到sd卡
        boolean isSave = SimpleUtils.saveBitmapToSdCard(this,cacheBitmapFromView,imgTitle);
        hideLoading();
        if (isSave){
            MyToast.showToast(this,"保存成功："+ Environment.getExternalStorageDirectory() + "/pgyttt/"+imgTitle+".jpg");
        }else {
            MyToast.showToast(this,"保存失败");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mContext).onActivityResult(requestCode,resultCode,data);
    }

    private void shareImg(SHARE_MEDIA sharePlatform){
        showLoading(null);
        String imgTitle = "pgy_shareImg_"+System.currentTimeMillis();
        // View生成截图
        Bitmap cacheBitmapFromView = SimpleUtils.getCacheBitmapFromView(llActivityPostersShareView);
        // 保存bitmap到sd卡
        boolean isSave = SimpleUtils.saveBitmapToSdCard(this,cacheBitmapFromView,imgTitle);
        if (isSave) {
            String path = Environment.getExternalStorageDirectory() + "/pgyttt/"+imgTitle+".jpg";
            startShare(path,sharePlatform);
        } else {
            hideLoading();
            ToastUtils.LongToast(mContext, "保存失败");
        }
    }


    /**申请权限*/
    private void startShare(String path,SHARE_MEDIA sharePlatform){
        //授权代码
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(false);
        config.isOpenShareEditActivity(true);
        config.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);


        File file = new File(path);
        UMImage image = new UMImage(mContext, file);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        new ShareAction(this)
//                .withText("长按识别二维码")
                .withMedia(image)
                .setPlatform(sharePlatform)
                .setCallback(umShareListener)
                .share();
    }

    /**
     * 三方分享回调接口
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
//            showLoading(null);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtils.w("plat", "platform" + platform + ",name:" + platform.name() + ",string:" + platform.toString());
            shareState(platform, getString(R.string.SHARE_SUCCESS),true);
            LogUtils.w(TAG,"分享成功:"+platform.toString());
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            LogUtils.w(TAG,"分享错误:"+t.getMessage());
            shareState(platform, getString(R.string.SHARE_ERROR),true);
            if (t != null) {
                LogUtils.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            shareState(platform, getString(R.string.SHARE_CANCEL),true);
            LogUtils.w(TAG,"分享取消:"+platform.toString());
        }
    };

    private int shareState(SHARE_MEDIA platform, String hint,boolean isToast) {
        hideLoading();
        int type = 0;
        String plat = "";
        switch (platform.name()) {
            case "WEIXIN":
                plat = "微信好友";
                break;
            case "QQ":
                plat = "QQ好友";

                break;
            case "QZONE":
                plat = "QQ空间";

                break;
            case "WEIXIN_CIRCLE":
                plat = "微信朋友圈";
                break;
            default:
                type = 0;
                break;
        }
        if (isToast){
            showToast(plat + hint);
        }
        return type;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideLoading();
    }

}
