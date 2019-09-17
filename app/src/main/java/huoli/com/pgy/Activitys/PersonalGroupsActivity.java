package huoli.com.pgy.Activitys;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.PermissionActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.GroupsInfo;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.FileUtils;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.GroupsPicView;
import huoli.com.pgy.Widgets.PersonalItemView;

/**
 * Created by YX on 2018/7/9.
 */

public class PersonalGroupsActivity extends PermissionActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.piv_activity_personal_groups_qq)
    PersonalItemView piv_qq;
    @BindView(R.id.piv_activity_personal_groups_wx)
    PersonalItemView piv_wx;
    @BindView(R.id.piv_activity_personal_groups_gongzong)
    PersonalItemView piv_gongzong;
    @BindView(R.id.piv_activity_personal_groups_weibo)
    PersonalItemView piv_weibo;

    //二维码地址
    private GroupsInfo.Group QQGroup,WxGroup,GZGroup,WbGroup;

    private GroupsPicView groupsPicView;

    private String saveName;
    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_groups;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData(){
        getGroupList();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_title.setText("加入社群");
        piv_qq.getErweimaView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QQGroup != null){
                    showView(QQGroup.getImgpath(),"奥丁官方QQ群");
                }else {
                    showToast("获取数据为空！");
                }
            }
        });
        piv_wx.getErweimaView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WxGroup != null){
                    showView(WxGroup.getImgpath(),"奥丁官方微信群");
                }else {
                    showToast("获取数据为空！");
                }
            }
        });
        piv_gongzong.getErweimaView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GZGroup != null){
                    showView(GZGroup.getImgpath(),"奥丁官方公众号");
                }else {
                    showToast("获取数据为空！");
                }
            }
        });
        piv_weibo.getErweimaView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WbGroup != null){
                    showView(WbGroup.getImgpath(),"奥丁官方微博");
                }else {
                    showToast("获取数据为空！");
                }
            }
        });
    }

    private void updateView(){
        if (QQGroup != null){
            piv_qq.setRightTxt(QQGroup.getKeyval());
        }else {
            piv_qq.setRightTxt("");
        }
        if (WxGroup != null){
            piv_wx.setRightTxt(WxGroup.getKeyval());
        }else {
            piv_wx.setRightTxt("");
        }
        if (GZGroup != null){
            piv_gongzong.setRightTxt(GZGroup.getKeyval());
        }else {
            piv_gongzong.setRightTxt("");
        }
        if (WbGroup != null){
            piv_weibo.setRightTxt(WbGroup.getKeyval());
        }else {
            piv_weibo.setRightTxt("");
        }
    }

    @OnClick({R.id.iv_back})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void showView(String url,String imgName){
        saveName = imgName;
        groupsPicView = new GroupsPicView(this, imgName,iv_back, new GroupsPicView.SavePic() {
            @Override
            public void save(String url) {
                if (!TextUtils.isEmpty(url)){
                    savePicture(url,saveName);
                }
            }
        });
        if (groupsPicView.isShowing()){
            groupsPicView.dismiss();
        }
        groupsPicView.show(url);
    }

    private void getGroupList(){
        showLoading(tv_title);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getGroupList(map, new getBeanCallback<GroupsInfo>() {
            @Override
            public void onSuccess(GroupsInfo groupsInfo) {
                hideLoading();
                if (groupsInfo == null){
                    groupsInfo = new GroupsInfo();
                }

                if (groupsInfo.getList() == null){
                    groupsInfo.setList(new HashMap<Integer, GroupsInfo.Group>());
                }
                QQGroup = groupsInfo.getList().get(0);
                WxGroup = groupsInfo.getList().get(1);
                GZGroup = groupsInfo.getList().get(2);
                WbGroup = groupsInfo.getList().get(3);
                updateView();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,reason);
            }
        });
    }

    /**
     * 下载二维码图片到本地
     */
    private void savePicture(final String imgUrl, final String imgName) {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                LogUtils.w("permission","BaseUploadPicActivity:读写权限已经获取");
                Glide.with(PersonalGroupsActivity.this).load(imgUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource == null){
                            showToast("网络图片错误");
                            LogUtils.e("保存图片","网络图片错误");
                            return;
                        }
                        FileUtils.saveBmp2Gallery(PersonalGroupsActivity.this,resource,imgName);
                    }
                });
            }
        }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);


//        Glide.with(mContext).load(imgUrl).asBitmap().toBytes().diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<byte[]>() {
//            @Override
//            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
//                try {
//                    if (bytes.length <= 0) {
//                        showToast("网络图片错误");
//                        LogUtils.e("保存图片","网络图片错误");
//
//                        return;
//                    }
//                    String fileLocalPath = FileUtils.savePhoto(bytes, FileUtils.getLocalPath(), imgName);
//                    if (FileUtils.imgInsertMedia(PersonalGroupsActivity.this,fileLocalPath,imgName)){
//                        showToast("保存成功到本地：" + fileLocalPath);
//                        LogUtils.e("保存图片","路径="+fileLocalPath);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    showToast("保存失败");
//                    LogUtils.e("保存图片","保存失败");
//                }
//            }
//        });

    }

}
