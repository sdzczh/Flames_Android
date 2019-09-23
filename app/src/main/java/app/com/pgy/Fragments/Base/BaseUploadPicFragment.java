package app.com.pgy.Fragments.Base;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.File;

import app.com.pgy.Activitys.Base.CropImgActivity;
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.StringBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.FileUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import static android.app.Activity.RESULT_OK;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:上传头像基类
 *
 * @author 徐庆重
 */
public abstract class BaseUploadPicFragment extends BaseFragment {
    /**
     * 静态变量，result值
     */
    private static final int CHOOSE_PICTURE = 0x111; //图库
    private static final int TAKE_PICTURE = 0x112;    //拍照
    private static final int CROP_SMALL_PICTURE = 0x113;//裁剪
    private Uri tempUri;
    private String iconFileLocalPath;
    private ImageView iconImage;
    /**
     * 头像上传后回调的url地址
     */
    private getStringCallback stringCallback;

    public void setIconImage(ImageView iconImage) {
        this.iconImage = iconImage;
    }

    public void setStringCallback(getStringCallback stringCallback) {
        this.stringCallback = stringCallback;
    }

    /**
     * 点击拍照，调用相机拍照
     */
    protected void takePhoto() {
        //首先Fragment依存的 Activity extends PermissionActivity
        ((PermissionActivity) getActivity()).checkPermission(
                new PermissionActivity.CheckPermListener() {
                    @Override
                    public void superPermission() {
                        LogUtils.w("permission","BaseUploadPicFragment:获取相机权限");
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(FileUtils.getLocalPath(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                    }
                }, R.string.camera, Manifest.permission.CAMERA);
    }

    /**
     * 点击图库，选择相册图片
     */
    protected void choosePhoto() {
        /*请求读写权限*/
        ((PermissionActivity) getActivity()).checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                LogUtils.w("permission","BaseUploadPicFragment:读写权限已经获取");
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                } else {
                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                }
            }
        }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.w(TAG,"requestCode:"+requestCode+",resultCode:"+resultCode+",data:"+data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                /*拍照*/
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri);
                    break;
                /*图库*/
                case CHOOSE_PICTURE:
                    try {
                        // 开始对图片进行裁剪处理
                        startPhotoZoom(data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("。。。");
                    }
                    break;
                case CROP_SMALL_PICTURE:
                    try {
                        if (data != null) {
//                            Bitmap photo = getBitmap(data);
//                            // 让刚才选择裁剪得到的图片显示在界面上
//                            iconImage.setImageBitmap(photo);
//                            /**将裁剪好的图片保存在本地，路径为iconImage，可通过getIconImage获取
//                             * 裁剪后的图片名称为：“UID”+时间戳
//                             * */
//                            iconFileLocalPath = saveIcon2Local(photo, "pgy" + TimeUtils.timeStampStr());
//                            LogUtils.w(TAG, "setIconPath:" + iconFileLocalPath);
//                            uploadPic(iconFileLocalPath);
                            iconFileLocalPath = data.getStringExtra("file");
                            LogUtils.w(TAG, "setIconPath:" + iconFileLocalPath);
                            ImageLoaderUtils.display(mContext,iconImage,iconFileLocalPath);
                            uploadPic(iconFileLocalPath);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("无法裁剪图片");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 上传该文件到服务器
     */
    private void uploadPic(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            // 拿着imagePath上传了
            File iconFromSD = new File(imagePath);
            if (!iconFromSD.exists()) {
                showToast("文件不存在");
                return;
            }
            LogUtils.w(TAG, "文件存在");
            showLoading(null);
            LogUtils.w(TAG, "显示loading");
            NetWorks.uploadImage(iconFromSD, Preferences.getLocalUser().getPhone(), new getBeanCallback<StringBean>() {
                @Override
                public void onSuccess(StringBean stringBean) {
                    LogUtils.w(TAG, "成功：" + stringBean.toString());
                    String iconUrl = stringBean.getImgPath();
                    //setIcon2Net(iconUrl);
                    if (stringCallback != null) {
                        stringCallback.getString(iconUrl);
                    }
                    hideLoading();
                }

                @Override
                public void onError(int errorCode, String reason) {
                    showToast("上传图片错误");
                    LogUtils.w(TAG, "失败：" + reason);
                    hideLoading();
                }
            });

        }
    }

    /**
     * 裁剪图片方法实现
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        //tempUri = uri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            String url = CropPhotoHelper.newInstance().getPath(mContext, uri);
//            if (TextUtils.isEmpty(url)) {
//                return;
//            }
//            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
//        } else {
//            intent.setDataAndType(uri, "image/*");
//        }
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_SMALL_PICTURE);

        Intent intent = new Intent();
        intent.setClass(mContext, CropImgActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private Bitmap getBitmap(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap photo = null;
        if (extras != null) {
            photo = extras.getParcelable("data");
            //photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
        }
        return photo;
    }

    /**
     * 将Bitmap存储在本地并上传到服务器
     */
    private String saveIcon2Local(Bitmap bitmap, String iconName) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        iconFileLocalPath = FileUtils.savePhoto(bitmap, FileUtils.getLocalPath(), iconName);
        return iconFileLocalPath;
    }

}
