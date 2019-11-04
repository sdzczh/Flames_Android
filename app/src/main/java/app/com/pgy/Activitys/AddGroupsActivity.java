package app.com.pgy.Activitys;

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
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.GroupsInfo;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.FileUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;

/**
 * Created by YX on 2018/7/16.
 */

public class AddGroupsActivity extends PermissionActivity {
    public final static int TYPE_QQ = 0;
    public final static int TYPE_WX = 1;
    public final static int TYPE_GZH = 2;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_activity_add_group_qr)
    ImageView ivActivityAddGroupQr;
    @BindView(R.id.tv_activity_add_group_save)
    TextView tvActivityAddGroupSave;
    @BindView(R.id.tv_activity_add_group_desc)
    TextView tvActivityAddGroupDesc;

    private int type = 0;
    private GroupsInfo.Group group;
    private String desc;
    private String groupName;

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_group;
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type",0);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (type == 1){
            tvTitle.setText("加入微信群");
            groupName = "微信群";
        }else if (type == 2){
            tvTitle.setText("关注公众号");
            groupName = "公众号";
        }else {
            tvTitle.setText("加入QQ群");
            groupName = "QQ群";
        }
        getData();
    }

    private void initDesc(){
        if (type == 0){
            if (group == null){
                group = new GroupsInfo.Group();
            }
            desc = "QQ群号："+group.getKeyval()+"\n" +
                    "第一步：用QQ扫码或搜索QQ群号加入官方QQ群；\n" +
                    "第二步：将QQ群聊天页面截图，加上个人PGY会员账号（即注册手机号）一起发到公众号里进行人工审核；\n" +
                    "第三步：公众号组核对无误后增加用户算力，算力到账通知将以短信形式发送。";
        }else if (type == 1){
            desc = "扫码添加客服为好友\n" +
                    "第一步：请扫码添加客服为好友，让客服拉你加入官方微信群；\n" +
                    "第二步：将微信群聊天页面截图，加上个人PGY会员账号（即注册手机号）一起发到公众号里进行人工审核；\n" +
                    "第三步：公众号组核对无误后增加用户算力，算力到账通知将以短信形式发送。";
        }else if (type == 2){
            desc = "公众号：PGY生态社区\n" +
                    "第一步：扫码或微信搜索“PGY生态社区”公众号，并关注；\n" +
                    "第二步：将公众号对话截图，加上个人PGY会员账号（即注册手机号）一起发到公众号里进行人工审核；\n" +
                    "第三步：公众号组核对无误后增加用户算力，算力到账通知将以短信形式发送。";
        }
    }

    @OnClick({R.id.iv_back,R.id.iv_activity_add_group_qr,R.id.tv_activity_add_group_save})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_activity_add_group_qr:
            case R.id.tv_activity_add_group_save:
                if (group == null || TextUtils.isEmpty(group.getImgpath())){
                    return;
                }
                savePicture(group.getImgpath(),tvTitle.getText()+"PGY"+groupName+"二维码");
                break;
        }
    }

    private void getData(){
        showLoading(ivActivityAddGroupQr);
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
                group = groupsInfo.getList().get(type);
                initDesc();
                updateView();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,reason);
            }
        });

    }


    private void updateView(){
        tvActivityAddGroupDesc.setText(desc+"");
        if (group != null){
            ImageLoaderUtils.display(mContext,ivActivityAddGroupQr,group.getImgpath());
            tvActivityAddGroupSave.setText("点击保存二维码");
            tvActivityAddGroupSave.setEnabled(true);
            ivActivityAddGroupQr.setEnabled(true);
        }else {
            tvActivityAddGroupDesc.setText("");
            tvActivityAddGroupSave.setText("二维码获取失败");
            tvActivityAddGroupSave.setEnabled(false);
            ivActivityAddGroupQr.setEnabled(false);
        }
    }


    /**
     * @param imgUrl
     * @param imgName
     */
    private void savePicture(final String imgUrl, final String imgName) {
        /*
         * Glide 加载图片保存到本地
         * imgUrl 图片地址
         * imgName 图片名称
         */
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                LogUtils.w("permission","BaseUploadPicActivity:读写权限已经获取");
                Glide.with(AddGroupsActivity.this).load(imgUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource == null){
                            showToast("网络图片错误");
                            LogUtils.e("保存图片","网络图片错误");
                            return;
                        }
                        FileUtils.saveBmp2Gallery(AddGroupsActivity.this,resource,imgName);
                    }
                });
            }
        }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);

//
//        Glide.with(mContext).load(imgUrl).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
//            @Override
//            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
//                try {
//                    if (bytes.length <= 0) {
//                        showToast("网络图片错误");
//                        return;
//                    }
//                    String fileLocalPath = FileUtils.savePhoto(bytes, FileUtils.getLocalPath(), imgName);
//                    if (FileUtils.imgInsertMedia(mContext,fileLocalPath)){
//                        showToast("保存成功到本地：" + fileLocalPath);
//                        LogUtils.e("保存图片","路径="+fileLocalPath);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    showToast("保存失败");
//                }
//            }
//        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
