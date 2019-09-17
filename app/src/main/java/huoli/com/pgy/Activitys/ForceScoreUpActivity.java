package huoli.com.pgy.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tmall.ultraviewpager.UltraViewPager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.PermissionActivity;
import huoli.com.pgy.Activitys.Base.WebDetailActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.BannerInfo;
import huoli.com.pgy.Models.Beans.EventBean.EventRealName;
import huoli.com.pgy.Models.Beans.ForceUpBean;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.BannerIntentUtils;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.im.widget.DemoGridView;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/14.
 */

public class ForceScoreUpActivity extends PermissionActivity {
    private final static int AUTO_SCROLL = 4 * 1000;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.uvp_activity_forcescore_up_slide)
    UltraViewPager uvpActivityForcescoreUpSlide;
    @BindView(R.id.tv_activity_forcescore_up_rule)
    TextView tvActivityForcescoreUpRule;
    @BindView(R.id.dgv_activity_forcescore_up_one)
    DemoGridView dgvActivityForcescoreUpOne;
    @BindView(R.id.dgv_activity_forcescore_up_share)
    DemoGridView dgvActivityForcescoreUpShare;
    @BindView(R.id.dgv_activity_forcescore_up_long_task)
    DemoGridView dgvActivityForcescoreUpLongTask;
    @BindView(R.id.ll__error)
    LinearLayout ll_error;
    @BindView(R.id.btn_reload)
    Button btn_reload;


    private int[] upIcons = {R.mipmap.force_friends,R.mipmap.force_sign,R.mipmap.force_trade};
    private String[] upTitle = {"邀请好友","签到+1算力","币币/法币交易"};
    private String[] upDesc = {"+50算力/人","点击领取","+10算力"};
    private int[] shareIcons = {R.mipmap.force_wx,R.mipmap.force_circle,R.mipmap.force_qq,R.mipmap.force_zone};
    private String[] shareTitle = {"微信好友","朋友圈","QQ好友","QQ空间"};
    private String[] shareDesc = {"+1算力","+5算力","+1算力","+5算力"};
    private SHARE_MEDIA[] medias = {SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE};

    private String[] taskTitle = {"实名认证","加入微信群","加入QQ群","关注公众号","连续登录10天","连续登录30天"};
    private int[] taskScores = {80,10,10,20,10,50};
    private List<BannerInfo> slides;
    private ForceUpBean mForceUpBean;
    private ShareGridAdapter upAdapter;
    private List<ForceShareInfo> upList;
    private ShareGridAdapter shareAdapter;
    private List<ForceShareInfo> shareList;
    private LongTaskGridAdapter taskAdapter;
    private List<LongTaskInfo> taskList;
    @Override
    public int getContentViewId() {
        return R.layout.activity_forcescore_up;
    }

    @Override
    protected void initData() {
        updateForceUp();
        updatShare();
        updatTask();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getData();
    }

    private void updateSlide(){
        if (mForceUpBean == null){
            mForceUpBean = new ForceUpBean();
        }
        if (mForceUpBean.getBanners() != null){
            slides = mForceUpBean.getBanners();
        }
        if (slides == null || slides.size() < 1){
            slides = new ArrayList<>();
            for (int i = 0;i < 5;i++){
                BannerInfo info = new BannerInfo();
                slides.add(info);
            }
        }
        uvpActivityForcescoreUpSlide.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        uvpActivityForcescoreUpSlide.setMultiScreen(1);
        uvpActivityForcescoreUpSlide.setAutoMeasureHeight(true);
        uvpActivityForcescoreUpSlide.setAutoScroll(AUTO_SCROLL);
        uvpActivityForcescoreUpSlide.initIndicator();
        uvpActivityForcescoreUpSlide.getIndicator()
                .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
                .setMargin(0, 0,0 , MathUtils.dip2px(mContext,5))
                .setRadius(MathUtils.dip2px(mContext,1))
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusResId(R.mipmap.home_slide_sel)
                .setNormalResId(R.mipmap.home_slide_def)
                .setIndicatorPadding(MathUtils.dip2px(mContext,5))
                .build();
        uvpActivityForcescoreUpSlide.setAdapter(new SlideAdapter(slides));
    }

    private void updateForceUp(){
        upList = getForeUpList();
        upAdapter = new ShareGridAdapter(mContext,upList);
        dgvActivityForcescoreUpOne.setAdapter(upAdapter);
        dgvActivityForcescoreUpOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                if (!Preferences.getLocalUser().isIdCheckFlag()) {
                    showToast(R.string.setRealNameFirst);
                    return;
                }
                if (position == 0){
                // 跳转到邀请好友
                   Intent intent =  new Intent(mContext, PostersActivity.class);
                   startActivity(intent);
                }else if (position == 1){
                    //签到
                    getCalculateForceGift(0);
                }else if (position == 2){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
    private void updatShare(){
        shareList = getShareList();
        shareAdapter = new ShareGridAdapter(mContext,shareList);
        dgvActivityForcescoreUpShare.setAdapter(shareAdapter);
        dgvActivityForcescoreUpShare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shareContent(medias[position%4]);
            }
        });
    }

    private void updatTask(){
        taskList = getTaskList();
        taskAdapter = new LongTaskGridAdapter(mContext, taskList, new TaskButtonClick() {
            @Override
            public void click(int position) {
                Intent intent = null;
                switch (position){
                    case 0:
                        // 跳转到实名认证
                        if (!isLogin()) {
                            showToast(R.string.unlogin);
                            return;
                        }
                        intent = new Intent(mContext, SecuritycenterActivity.class);
                        break;
                    case 1:
                        //  加入微信群
                        if (!Preferences.getLocalUser().isIdCheckFlag()) {
                            showToast(R.string.setRealNameFirst);
                            return;
                        }
                        if (mForceUpBean == null){
                            showToast("数据为空！");
                            return;
                        }

                        intent = new Intent(mContext, AddGroupsActivity.class);
                        intent.putExtra("type",AddGroupsActivity.TYPE_WX);
                        break;
                    case 2:
                        // 加入QQ群
                        if (!Preferences.getLocalUser().isIdCheckFlag()) {
                            showToast(R.string.setRealNameFirst);
                            return;
                        }
                        if (mForceUpBean == null){
                            showToast("数据为空！");
                            return;
                        }
                        intent = new Intent(mContext, AddGroupsActivity.class);
                        intent.putExtra("type",AddGroupsActivity.TYPE_QQ);
                        break;
                    case 3:
                        // 公众号
                        if (!Preferences.getLocalUser().isIdCheckFlag()) {
                            showToast(R.string.setRealNameFirst);
                            return;
                        }
                        if (mForceUpBean == null){
                            showToast("数据为空！");
                            return;
                        }
                        intent = new Intent(mContext, AddGroupsActivity.class);
                        intent.putExtra("type",AddGroupsActivity.TYPE_GZH);
                        break;
                    case 4:
                        //  十天
                        if (!Preferences.getLocalUser().isIdCheckFlag()) {
                            showToast(R.string.setRealNameFirst);
                            return;
                        }
                        if (mForceUpBean == null){
                            showToast("数据为空！");
                            return;
                        }
                        getCalculateForceGift(1);
                        break;
                    case 5:
                        //  三十天
                        if (!Preferences.getLocalUser().isIdCheckFlag()) {
                            showToast(R.string.setRealNameFirst);
                            return;
                        }
                        if (mForceUpBean == null){
                            showToast("数据为空！");
                            return;
                        }
                        getCalculateForceGift(2);
                        break;
                }

                if (intent != null){
                    startActivity(intent);
                }
            }
        });
        dgvActivityForcescoreUpLongTask.setAdapter(taskAdapter);
    }

    private List<ForceShareInfo> getForeUpList(){
        List<ForceShareInfo> upList = new ArrayList<>();
        for (int i = 0;i < 3;i++){
            ForceShareInfo info = new ForceShareInfo();
            info.setResIcon(upIcons[i]);
            info.setTitle(upTitle[i]);
            info.setDesc(upDesc[i]);
            info.setState(0);
            upList.add(info);
        }
        return upList;
    }
    private List<ForceShareInfo> getShareList(){
        List<ForceShareInfo> shareList = new ArrayList<>();
        for (int i = 0;i < 4;i++){
            ForceShareInfo info = new ForceShareInfo();
            info.setResIcon(shareIcons[i]);
            info.setTitle(shareTitle[i]);
            info.setDesc(shareDesc[i]);
            info.setState(0);
            shareList.add(info);
        }
        return shareList;
    }
    private List<LongTaskInfo> getTaskList(){
        List<LongTaskInfo> list = new ArrayList<>();
        for (int i = 0;i < 6;i++){
            LongTaskInfo info = new LongTaskInfo();
            info.setTitle(taskTitle[i]);
            info.setScore(taskScores[i]);
            if (i == 4){
                info.setState(1);
                info.setDesc("0/10");
            }else if (i == 5){
                info.setState(1);
                info.setDesc("0/30");
            }else {
                info.setState(0);
            }

            list.add(info);
        }
        return list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RealNameEvent(EventRealName event){
        LogUtils.e(TAG,"接受到广播");
        if (event != null && event.getSuccess()){
            getData();
        }
    }

    @OnClick({R.id.iv_back,R.id.btn_reload,R.id.tv_activity_forcescore_up_rule})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_reload:
                getData();
                break;
            case R.id.tv_activity_forcescore_up_rule:
                // 跳转到算力规则
                if (mForceUpBean == null){
                    showToast("数据为空，请稍后重试");
                    return;
                }
                Intent intent = new Intent(mContext,WebDetailActivity.class);
                intent.putExtra("title","详细规则");
                intent.putExtra("url",mForceUpBean.getInstruUrl());
                startActivity(intent);
                break;
        }
    }

    //获取页面数据
    private void getData(){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());

        NetWorks.getForceUpData(Preferences.getAccessToken(), map, new getBeanCallback<ForceUpBean>() {
            @Override
            public void onSuccess(ForceUpBean forceUpBean) {
                hideLoading();
                mForceUpBean = forceUpBean;
                updateViews();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,reason);
                mForceUpBean = null;
                updateViews();
            }
        });
    }

    //分享成功提交
    private void submitShare(final int type){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.submitForceShare(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
//                8每日分享QQ 9每日分享微信 10每日分享空间 11每日分享朋友圈 12每日现货/C2C交易
                hideLoading();
                switch (type){
                    case 8:
                        mForceUpBean.getQqShareTask().setFinished(true);
                        break;
                    case 9:
                        mForceUpBean.getWechatShareTask().setFinished(true);
                        break;
                    case 10:
                        mForceUpBean.getQzoneShareTask().setFinished(true);
                        break;
                    case 11:
                        mForceUpBean.getCircleShareTask().setFinished(true);
                        break;
                }
               shareAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,"分享失败："+reason);
            }
        });
    }
    boolean isUsing = false;
    private void getCalculateForceGift(final int type){
        if (isUsing){
            return;
        }
        isUsing = true;
        showLoading(null);
        Map<String,Object> map = new HashMap<>();
        map.put("type", type);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCalculateForceGift(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                isUsing = false;
                hideLoading();
                switch (type){
                    case 0:
                        mForceUpBean.getDaySignTask().setFinished(true);
                        mForceUpBean.setSignDays(mForceUpBean.getSignDays()+1);
                        updateUpView();
                        break;
                    case 1:
                        mForceUpBean.getTenSignTask().setFinished(true);
                        break;
                    case 2:
                        mForceUpBean.getMonthSignTask().setFinished(true);
                        break;
                }
                updateTaskView();
            }

            @Override
            public void onError(int errorCode, String reason) {
                isUsing = false;
                hideLoading();
                onFail(errorCode,reason);
            }
        });
    }



    //分享
    private void shareContent(final SHARE_MEDIA sharePlatform){
        if (!isLogin()) {
            showToast(R.string.unlogin);
            return;
        }
        if (sharePlatform == null){
            return;
        }
        if (mForceUpBean == null){
            showToast("数据为空！");
            return;
        }
        /*请求读写权限*/
        checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                LogUtils.w("permission","ForceScoreUpActivity:读写权限已经获取");
                startShare(sharePlatform);
            }
        }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);

    }


    /**申请权限*/
    private void startShare(SHARE_MEDIA sharePlatform){
        //授权代码
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(false);
        config.isOpenShareEditActivity(true);
        config.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);
        if (mForceUpBean.getShareUrl().startsWith("http")) {
            /*创建分享链接*/
            UMWeb web = new UMWeb(mForceUpBean.getShareUrl()+Preferences.getLocalUser().getPhone());
            /*设置标题*/
            web.setTitle(mForceUpBean.getShareTitle());
            /*设置缩略图*/
            UMImage thumb = new UMImage(mContext, R.mipmap.ic_launcher);
            web.setThumb(thumb);
            /*设置描述*/
            web.setDescription(mForceUpBean.getShareDes());
            new ShareAction(this)
                    .withMedia(web)
                    .setPlatform(sharePlatform)
                    .setCallback(umShareListener)
                    .share();


        }else {
            showToast("地址开头不是http");
        }
    }

    /**
     * 三方分享回调接口
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoading(null);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtils.w("plat", "platform" + platform + ",name:" + platform.name() + ",string:" + platform.toString());
            int i = shareState(platform, getString(R.string.SHARE_SUCCESS),true);
            if (i != 0){
                submitShare(i);
            }
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
                if (mForceUpBean != null && !mForceUpBean.getWechatShareTask().isFinished()){
                    type = 9;
                }
                break;
            case "QQ":
                plat = "QQ好友";
                if (mForceUpBean != null && !mForceUpBean.getQqShareTask().isFinished()){
                    type = 8;
                }
                break;
            case "QZONE":
                plat = "QQ空间";
                if (mForceUpBean != null && !mForceUpBean.getQzoneShareTask().isFinished()){
                    type = 10;
                }
                break;
            case "WEIXIN_CIRCLE":
                plat = "微信朋友圈";
                if (mForceUpBean != null && !mForceUpBean.getCircleShareTask().isFinished()){
                    type = 11;
                }
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


    private void updateViews(){
        if (mForceUpBean == null && !isFinishing()){
            if (ll_error.getVisibility() != View.VISIBLE){
                ll_error.setVisibility(View.VISIBLE);
            }

        }else {
            if (ll_error.getVisibility() == View.VISIBLE){
                ll_error.setVisibility(View.GONE);
            }
            updateSlide();
            updateUpView();
            updateShareView();
            updateTaskView();
        }
    }

    private void updateUpView(){
        if (mForceUpBean == null){
            mForceUpBean = new ForceUpBean();
        }

        String invitedesc = "+"+mForceUpBean.getInviteTask().getSoulForce()+"算力/人";
        if (!invitedesc.equals(upDesc[0])){
            upList.get(0).setDesc(invitedesc);
        }
        if (mForceUpBean.getDaySignTask().isFinished()){
            upList.get(1).setState(1);
        }else {
            upList.get(1).setState(0);
            upList.get(1).setDesc("点击领取");
        }


        if (mForceUpBean.getDealTask().isFinished()){
            upList.get(2).setState(1);

        }else {
            upList.get(2).setState(0);
            if (mForceUpBean.getDealTask().getSoulForce() != 0){
                upList.get(2).setDesc("+"+mForceUpBean.getDealTask().getSoulForce()+"算力");
            }else {
                upList.get(2).setDesc(upDesc[2]);
            }

        }
        upAdapter.notifyDataSetChanged();

    }

    private void updateShareView(){
        if (mForceUpBean == null){
            mForceUpBean = new ForceUpBean();
        }
        if (mForceUpBean.getWechatShareTask().isFinished()){
            shareList.get(0).setState(1);
        }else {
            shareList.get(0).setState(0);
            if (mForceUpBean.getWechatShareTask().getSoulForce() != 0){
                shareList.get(0).setDesc("+"+mForceUpBean.getWechatShareTask().getSoulForce()+"算力");
            }else {
                shareList.get(0).setDesc(shareDesc[0]);
            }
        }

        if (mForceUpBean.getCircleShareTask().isFinished()){
            shareList.get(1).setState(1);
        }else {
            shareList.get(1).setState(0);
            if (mForceUpBean.getCircleShareTask().getSoulForce() != 0){
                shareList.get(1).setDesc("+"+mForceUpBean.getCircleShareTask().getSoulForce()+"算力");
            }else {
                shareList.get(1).setDesc(shareDesc[1]);
            }
        }

        if (mForceUpBean.getQqShareTask().isFinished()){
            shareList.get(2).setState(1);
        }else {
            shareList.get(2).setState(0);
            if (mForceUpBean.getQqShareTask().getSoulForce() != 0){
                shareList.get(2).setDesc("+"+mForceUpBean.getQqShareTask().getSoulForce()+"算力");
            }else {
                shareList.get(2).setDesc(shareDesc[2]);
            }
        }

        if (mForceUpBean.getQzoneShareTask().isFinished()){
            shareList.get(3).setState(1);
        }else {
            shareList.get(3).setState(0);
            if (mForceUpBean.getQzoneShareTask().getSoulForce() != 0){
                shareList.get(3).setDesc("+"+mForceUpBean.getQzoneShareTask().getSoulForce()+"算力");
            }else {
                shareList.get(3).setDesc(shareDesc[3]);
            }
        }
        shareAdapter.notifyDataSetChanged();
    }

    private void updateTaskView(){
        if (mForceUpBean == null){
            mForceUpBean = new ForceUpBean();
        }
        if (mForceUpBean.getRealNameTask().getSoulForce() != taskScores[0]){
            taskList.get(0).setScore(mForceUpBean.getRealNameTask().getSoulForce());
        }
        if (Preferences.getLocalUser().isIdCheckFlag()) {
            taskList.get(0).setState(2);
        }else {
            taskList.get(0).setState(0);
        }

        if (mForceUpBean.getJoinWechatTask().getSoulForce() != taskScores[1]){
            taskList.get(1).setScore(mForceUpBean.getJoinWechatTask().getSoulForce());
        }
        if (mForceUpBean.getJoinWechatTask().isFinished()){
            taskList.get(1).setState(2);
        }else {
            taskList.get(1).setState(0);
        }

        if (mForceUpBean.getJoinQGroupTask().getSoulForce() != taskScores[2]){
            taskList.get(2).setScore(mForceUpBean.getJoinQGroupTask().getSoulForce());
        }
        if (mForceUpBean.getJoinQGroupTask().isFinished()){
            taskList.get(2).setState(2);
        }else {
            taskList.get(2).setState(0);
        }

        if (mForceUpBean.getJoinPublicTask().getSoulForce() != taskScores[3]){
            taskList.get(3).setScore(mForceUpBean.getJoinPublicTask().getSoulForce());
        }
        if (mForceUpBean.getJoinPublicTask().isFinished()){
            taskList.get(3).setState(2);
        }else {
            taskList.get(3).setState(0);
        }

        if (mForceUpBean.getTenSignTask().getSoulForce() != taskScores[4]){
            taskList.get(4).setScore(mForceUpBean.getTenSignTask().getSoulForce());
        }
        if (mForceUpBean.getTenSignTask().isFinished()){
            taskList.get(4).setState(2);
        }else {
            if (mForceUpBean.getSignDays() >= 10){
                taskList.get(4).setState(0);
                taskList.get(4).setDesc("领取");
            }else {
                taskList.get(4).setState(1);
                taskList.get(4).setDesc(mForceUpBean.getSignDays()+"/10");
            }
        }

        if (mForceUpBean.getMonthSignTask().getSoulForce() != taskScores[5]){
            taskList.get(5).setScore(mForceUpBean.getMonthSignTask().getSoulForce());
        }
        if (mForceUpBean.getMonthSignTask().isFinished()){
            taskList.get(5).setState(2);
        }else {
            if (mForceUpBean.getSignDays() >= 30){
                taskList.get(5).setState(0);
                taskList.get(5).setDesc("领取");
            }else {
                taskList.get(5).setState(1);
                taskList.get(5).setDesc(mForceUpBean.getSignDays()+"/30");
            }
        }
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onDestroy();
    }

    public class SlideAdapter extends PagerAdapter {
        List<BannerInfo> list;

        public SlideAdapter(List<BannerInfo> data) {
            super();
            list = data;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView riv = new RoundedImageView(mContext);
            riv.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoaderUtils.displayWithCache(mContext,riv,list.get(position).getImgpath());
            riv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BannerIntentUtils.bannerToActivity(ForceScoreUpActivity.this,list.get(position));
                }
            });
            container.addView(riv);
            return riv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof ImageView) {
                ImageView iv = (ImageView) object;
                container.removeView(iv);
            }
        }
    }

    class ForceShareInfo {
        int resIcon;
        String title;
        String desc;
        int state;//0 未完成 1已完成

        public void setResIcon(int resIcon) {
            this.resIcon = resIcon;
        }

        public int getResIcon() {
            return resIcon;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

    class ShareGridAdapter extends BaseAdapter {
        private List<ForceShareInfo> list;
        Context context;


        public ShareGridAdapter(Context context, List<ForceShareInfo> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_forcescore_up_view, parent, false);
            }
            ForceShareInfo info = (ForceShareInfo) getItem(position);
            ImageView iv_icon = convertView.findViewById(R.id.iv_item_forcescore_up_icon);
            TextView tv_title = convertView.findViewById(R.id.tv_item_forcescore_up_title);
            TextView tv_desc = convertView.findViewById(R.id.tv_item_forcescore_up_desc);
            TextView tv_state = convertView.findViewById(R.id.tv_item_forcescore_up_finish);
            iv_icon.setImageResource(info.getResIcon());
            tv_title.setText(info.getTitle() + "");
            tv_desc.setText(info.getDesc() + "");
            if (info.getState() == 1) {
                tv_desc.setVisibility(View.GONE);
                tv_state.setVisibility(View.VISIBLE);
            } else {
                tv_desc.setVisibility(View.VISIBLE);
                tv_state.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list == null || list.size() <= position ? null : list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    class LongTaskInfo {
        String title;
        int score;
        int state;//0，未完成，1，进行中，2，已完成
        String desc;

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    class LongTaskGridAdapter extends BaseAdapter {
        private List<LongTaskInfo> list;
        Context context;
        TaskButtonClick listen;

        public LongTaskGridAdapter(Context context, List<LongTaskInfo> list,TaskButtonClick click) {
            this.list = list;
            this.context = context;
            this.listen = click;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_forcescore_long_task, parent, false);
            }
            final LongTaskInfo info = (LongTaskInfo) getItem(position);
            TextView tv_title = convertView.findViewById(R.id.tv_item_forcescore_long_task_title);
            TextView tv_desc = convertView.findViewById(R.id.tv_item_forcescore_long_task_state);
            TextView tv_score = convertView.findViewById(R.id.tv_item_forcescore_long_task_score);
            tv_title.setText(info.getTitle() + "");
            tv_score.setText("+" + info.getScore() + "算力");
            tv_desc.setEnabled(false);
            tv_desc.setSelected(false);
            if (info.getState() == 2) {
                tv_desc.setEnabled(true);
                tv_desc.setSelected(true);
                tv_desc.setText("已完成");
            } else if (info.getState() == 1) {
                tv_desc.setText(info.getDesc());
            } else {
                tv_desc.setEnabled(true);
                tv_desc.setText(TextUtils.isEmpty(info.getDesc())?"做任务":info.getDesc());
            }
            tv_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getState() == 0 && listen != null){
                        listen.click(position);
                    }
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list == null || list.size() <= position ? null : list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public interface TaskButtonClick{
        void click(int position);
    }
}
