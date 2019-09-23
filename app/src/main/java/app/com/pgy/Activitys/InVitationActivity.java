package app.com.pgy.Activitys;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.OrdinRewardInfo;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.SimpleUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToastUtils;
import app.com.pgy.Widgets.InvitationPosterDialog;
import app.com.pgy.Widgets.ShareDialog;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

public class InVitationActivity extends PermissionActivity {
    @BindView(R.id.rb_invitation_now)
    RadioButton rbInvitationNow;
    @BindView(R.id.rb_invitation_all)
    RadioButton rbInvitationAll;
    @BindView(R.id.fragment_trade_group)
    RadioGroup rgInvitationGroup;
    @BindView(R.id.line_invitation_now)
    View lineInvitationNow;
    @BindView(R.id.line_invitation_all)
    View lineInvitationAll;
    @BindView(R.id.tv_invitation_first_name)
    TextView tvInvitationFirstName;
    @BindView(R.id.tv_invitation_first_value)
    TextView tvInvitationFirstValue;
    @BindView(R.id.tv_invitation_second_name)
    TextView tvInvitationSecondName;
    @BindView(R.id.tv_invitation_second_value)
    TextView tvInvitationSecondValue;
    @BindView(R.id.tv_invitation_my_rank)
    TextView tvInvitationMyRank;
    @BindView(R.id.iv_invitation_rank2)
    ImageView ivInvitationRank2;
    @BindView(R.id.tv_invitation_rank2_tel)
    TextView tvInvitationRank2Tel;
    @BindView(R.id.tv_invitation_rank2_num)
    TextView tvInvitationRank2Num;
    @BindView(R.id.iv_invitation_rank1)
    ImageView ivInvitationRank1;
    @BindView(R.id.tv_invitation_rank1_tel)
    TextView tvInvitationRank1Tel;
    @BindView(R.id.tv_invitation_rank1_num)
    TextView tvInvitationRank1Num;
    @BindView(R.id.iv_invitation_rank3)
    ImageView ivInvitationRank3;
    @BindView(R.id.tv_invitation_rank3_tel)
    TextView tvInvitationRank3Tel;
    @BindView(R.id.tv_invitation_rank3_num)
    TextView tvInvitationRank3Num;
    @BindView(R.id.iv_invitation_my_code)
    TextView ivInvitationMyCode;
    @BindView(R.id.iv_invitation_my_invitor)
    TextView ivInvitationMyInvitor;
    @BindView(R.id.ll_invitation_rank2)
    LinearLayout llInvitationRank2;
    @BindView(R.id.ll_invitation_rank1)
    LinearLayout llInvitationRank1;
    @BindView(R.id.ll_invitation_rank3)
    LinearLayout llInvitationRank3;

    private OrdinRewardInfo rewardInfo;

    @Override
    public int getContentViewId() {
        return R.layout.activity_invitation_new;
    }

    @Override
    protected void initData() {
        getInfo();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rgInvitationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_invitation_now:
                        tvInvitationFirstName.setText("我本期获得奖励PGY");
                        tvInvitationSecondName.setText("我本期获得奖励ECN");
                        tvInvitationFirstValue.setText(rewardInfo == null ? "0.00" : rewardInfo.getThisOrder());
                        tvInvitationSecondValue.setText(rewardInfo == null ? "0.00" : rewardInfo.getThisUnion());
                        lineInvitationAll.setVisibility(View.INVISIBLE);
                        lineInvitationNow.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_invitation_all:
                        tvInvitationFirstName.setText("我累计获得奖励PGY");
                        tvInvitationSecondName.setText("我累计获得奖励ECN");
                        tvInvitationFirstValue.setText(rewardInfo == null ? "0.00" : rewardInfo.getAllOrder());
                        tvInvitationSecondValue.setText(rewardInfo == null ? "0.00" : rewardInfo.getAllUnion());
                        lineInvitationAll.setVisibility(View.VISIBLE);
                        lineInvitationNow.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_question, R.id.tv_invitation_to_history, R.id.iv_invitation_copy, R.id.iv_invitation_my_invition_poster, R.id.iv_invitation_my_invition_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_question:
                if (rewardInfo == null){
                    return;
                }
                //说明
                Intent intent = new Intent(mContext,WebDetailActivity.class);
                intent.putExtra("title","帮助文档说明");
                intent.putExtra("url",rewardInfo.getDocUrl());
                startActivity(intent);
                break;
            case R.id.tv_invitation_to_history:
                //查看历史邀请奖励榜
                Intent intent1 = new Intent(mContext,InVitationHistoryActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_invitation_copy:
                //复制
                if (rewardInfo == null || TextUtils.isEmpty(rewardInfo.getReferCode())) {
                    return;
                }
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", rewardInfo.getReferCode());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                showToast("复制成功");
                break;
            case R.id.iv_invitation_my_invition_poster:
                //海报
                checkPermission(new PermissionActivity.CheckPermListener() {
                    @Override
                    public void superPermission() {
                       showPoster();
                    }
                }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);

                break;
            case R.id.iv_invitation_my_invition_follow:
                //推广记录
                Intent intent2 = new Intent(mContext,InVitationMyFollowActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void getInfo() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getOrdinRewardInfo(Preferences.getAccessToken(), map, new getBeanCallback<OrdinRewardInfo>() {
            @Override
            public void onSuccess(OrdinRewardInfo ordinRewardInfo) {
                hideLoading();
                rewardInfo = ordinRewardInfo;
                updateView();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                rewardInfo = null;
                onFail(errorCode, reason);
            }
        });

    }

    private void updateView() {
        if (rewardInfo != null) {
            tvInvitationFirstValue.setText(rewardInfo.getThisOrder());
            tvInvitationSecondValue.setText(rewardInfo.getThisUnion());
            tvInvitationMyRank.setText(TextUtils.isDigitsOnly(rewardInfo.getRank()) ? "第"+rewardInfo.getRank()+"名": "暂无排名" );
            ivInvitationMyCode.setText(rewardInfo.getReferCode());
            if (TextUtils.isEmpty(rewardInfo.getReferPhone())) {
                ivInvitationMyInvitor.setText("");
            } else {
                ivInvitationMyInvitor.setText("我的邀请者" + rewardInfo.getReferPhone());
            }
            llInvitationRank1.setVisibility(View.INVISIBLE);
            llInvitationRank2.setVisibility(View.INVISIBLE);
            llInvitationRank3.setVisibility(View.INVISIBLE);
            if (rewardInfo.getTopList() != null){
                if (rewardInfo.getTopList().size() > 0){
                    OrdinRewardInfo.TopListBean topListBean = rewardInfo.getTopList().get(0);
                    llInvitationRank1.setVisibility(View.VISIBLE);
                    tvInvitationRank1Tel.setText(topListBean.getPhone());
                    tvInvitationRank1Num.setText(topListBean.getAmount()+"ECN");
                }

                if (rewardInfo.getTopList().size() > 1){
                    OrdinRewardInfo.TopListBean topListBean = rewardInfo.getTopList().get(1);
                    llInvitationRank2.setVisibility(View.VISIBLE);
                    tvInvitationRank2Tel.setText(topListBean.getPhone());
                    tvInvitationRank2Num.setText(topListBean.getAmount()+"ECN");
                }

                if (rewardInfo.getTopList().size() > 2){
                    OrdinRewardInfo.TopListBean topListBean = rewardInfo.getTopList().get(2);
                    llInvitationRank3.setVisibility(View.VISIBLE);
                    tvInvitationRank3Tel.setText(topListBean.getPhone());
                    tvInvitationRank3Num.setText(topListBean.getAmount()+"ECN");
                }
            }
        }
    }

    private void showPoster(){
        if (rewardInfo == null){
            return;
        }

        InvitationPosterDialog dialog = new InvitationPosterDialog
                .Builder(mContext)
                .setInvitationCode(rewardInfo.getReferCode())
                .setQrUrl(rewardInfo.getQrCode())
                .setOnClickListener(new InvitationPosterDialog.InvitationPosterDialogOnClick() {
                    @Override
                    public void savePic(boolean show) {
                        if (show){
                            showLoading(null);
                        }else {
                            hideLoading();
                        }
                    }

                    @Override
                    public void sharePic(Bitmap bitmap) {
                        showShareDialog(bitmap);
                    }
                }).create();
        dialog.show();
    }

    private void showShareDialog(Bitmap bitmap){
        final ShareDialog dialog = new ShareDialog.Builder(mContext)
                .setBitmap(bitmap)
                .setOnClick(new ShareDialog.ShareOnClick() {
                    @Override
                    public void share(Bitmap img, SHARE_MEDIA sharePlatform) {
                        showLoading(null);
                        boolean isSave = SimpleUtils.saveBitmapToSdCard(mContext, img, "odin_shareImg_" + rewardInfo.getReferCode());
                        if (isSave) {
                            startShare(sharePlatform);
                        } else {
                            hideLoading();
                            ToastUtils.LongToast(mContext, "保存失败");
                        }

                    }
                }).create();
        dialog.show();
    }

    /**申请权限*/
    private void startShare(SHARE_MEDIA sharePlatform){
        //授权代码
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(false);
        config.isOpenShareEditActivity(true);
        config.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);

        String path = Environment.getExternalStorageDirectory() + "/1000ttt/";
        File file = new File(path+"odin_shareImg_"+rewardInfo.getReferCode()+".jpg");
        UMImage image = new UMImage(mContext, file);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        new ShareAction(InVitationActivity.this)
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
}
