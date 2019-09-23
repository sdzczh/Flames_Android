package app.com.pgy.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.ShareInfo;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.QrCodeUtil;
import app.com.pgy.Utils.SimpleUtils;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/10.
 * 邀请返佣
 */

public class PostersActivity extends PermissionActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.vp_activity_invitation_posters_viewpage)
    ViewPager mViewPager;
    @BindView(R.id.ll_activity_invitation_posters_share_wx)
    LinearLayout ll_wx;
    @BindView(R.id.ll_activity_invitation_posters_share_wxcircle)
    LinearLayout ll_wxcircle;
    @BindView(R.id.ll_activity_invitation_posters_share_qq)
    LinearLayout ll_qq;
    @BindView(R.id.ll_activity_invitation_posters_share_zone)
    LinearLayout ll_zone;

    private MyPagerAdapter pagerAdapter;
    private int currentImg = 0;

    private View shareView;
    private ImageView shareImgl,shareQR;
    private TextView tv_1,tv_2;

    private ShareInfo mShareInfo;
    private Bitmap qrBitmap;
    private View currentView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_invitation_posters;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getshareInfo();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_title.setText("选择海报分享");
    }
    private void initViewpage(){
        if (mShareInfo == null){
            mShareInfo = new ShareInfo();
        }
        if (mShareInfo != null && mShareInfo.getList() != null && currentImg < mShareInfo.getList().size()){
            if (qrBitmap == null){
                qrBitmap = QrCodeUtil.createBitmap(mShareInfo.getList().get(0).getShareUrl());
            }
        }
        initShareView();
        float width = MathUtils.getWidthInPx(this);
        int height = (int) (width*0.56/0.67);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
        params.height = height;
        mViewPager.requestLayout();
        if (pagerAdapter == null) {
            pagerAdapter = new MyPagerAdapter(getApplicationContext(), mShareInfo.getList(), new onViewClickListen() {
                @Override
                public void onVClick(int position,String img) {
                    pagerAdapter.setChoosedId(position);
                    currentImg = position;


                }
            });
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0 && position !=pagerAdapter.getCount()-1){
                    pagerAdapter.setLeftMargin(false);
                }else {
                    pagerAdapter.setLeftMargin(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

                /*初始化顶部ViewPager界面*/
        mViewPager.setCurrentItem(0);
        //设置预加载数量
        mViewPager.setOffscreenPageLimit(2);
        //设置每页之间的左右间隔
//        mViewPager.setPageMargin(MathUtils.dip2px(mContext, 15));
        mViewPager.setAdapter(pagerAdapter);

    }

    private void initShareView(){
        if (mShareInfo == null){
            mShareInfo = new ShareInfo();
        }
        // 本View是inflate加载而来，不是Activity的xml本身的
        shareView= getLayoutInflater().inflate(R.layout.view_poster_shareimg,null);
        shareImgl =  shareView.findViewById(R.id.iv_shareImg);
        shareQR =  shareView.findViewById(R.id.iv_shareImg_erweima);
        tv_1 = shareView.findViewById(R.id.tv_1);
        tv_2= shareView.findViewById(R.id.tv_2);
        if (mShareInfo != null && mShareInfo.getList() != null && currentImg < mShareInfo.getList().size()){
            updateShareView(mShareInfo.getList().get(currentImg));
        }
    }
    private void updateShareView(ShareInfo.ListBean bean){
        if (bean == null){
            return;
        }
        ImageLoaderUtils.displayWithCache(mContext,shareImgl,bean.getImgpath());
        tv_1.setText(bean.getMaintitle());
        tv_2.setText(bean.getSubtitle());
        if (qrBitmap == null){
            qrBitmap = QrCodeUtil.createBitmap(mShareInfo.getList().get(0).getShareUrl());
        }
        if (qrBitmap != null){
            shareQR.setImageBitmap(qrBitmap);
        }
    }

    private void getshareInfo(){
        Map<String,Object> maps = new HashMap<>();
        maps.put("deviceNum",Preferences.getDeviceId());
        maps.put("syetemType",SYSTEMTYPE_ANDROID);
        maps.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getPosters(Preferences.getAccessToken(),maps, new getBeanCallback<ShareInfo>() {
            @Override
            public void onSuccess(ShareInfo shareInfo) {
               mShareInfo = shareInfo;
//               if (mShareInfo != null && mShareInfo.getList() != null && mShareInfo.getList().size() > 0){
//                   for (int i = 0;i < mShareInfo.getList().size();i++){
//                       downloadImage(mShareInfo.getList().get(i).getImgpath(),i);
//                   }
//               }
               initViewpage();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode,reason);
            }
        });
    }

    @OnClick({R.id.iv_back,R.id.ll_activity_invitation_posters_share_wx,R.id.ll_activity_invitation_posters_share_wxcircle,
            R.id.ll_activity_invitation_posters_share_qq,R.id.ll_activity_invitation_posters_share_zone})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_activity_invitation_posters_share_wx:
                getShareImg(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_activity_invitation_posters_share_wxcircle:
                getShareImg(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_activity_invitation_posters_share_qq:
                getShareImg(SHARE_MEDIA.QQ);
                break;
            case R.id.ll_activity_invitation_posters_share_zone:
                getShareImg(SHARE_MEDIA.QZONE);
                break;
        }
    }

    private void getShareImg(final SHARE_MEDIA sharePlatform){
        if (currentView != null&&mShareInfo != null && mShareInfo.getList() != null && mShareInfo.getList().size() > currentImg){
            checkPermission(new CheckPermListener() {
                @Override
                public void superPermission() {
                    showLoading(null);
                    LogUtils.w("permission","PosterActivity:读写权限已经获取");
                    // View生成截图
                    Bitmap cacheBitmapFromView = SimpleUtils.getCacheBitmapFromView(currentView);
                    // 保存bitmap到sd卡
                    boolean isSave = SimpleUtils.saveBitmapToSdCard(PostersActivity.this,cacheBitmapFromView,"odin_shareImg_"+currentImg);
                    hideLoading();
                    if (isSave){
                        shareContent(sharePlatform);
                    }else {
                        showToast("保存图片失败");
                    }
                }
            }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);

        }


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

        String path = Environment.getExternalStorageDirectory() + "/1000ttt/";
        File file = new File(path+"odin_shareImg_"+currentImg+".jpg");
        UMImage image = new UMImage(mContext, file);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        new ShareAction(PostersActivity.this)
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
            showLoading(null);
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

    public void downloadImage(final String url, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Context context = getApplicationContext();
                    FutureTarget<File> target = Glide.with(context)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                    final File imageFile = target.get();
                    LogUtils.e("glide","url="+url+",file="+imageFile.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mContext).onActivityResult(requestCode,resultCode,data);
    }


    private class MyPagerAdapter extends PagerAdapter {
        private List<ShareInfo.ListBean> urls ;
        private Context mContext;
        private int choosedId = 0;
        private boolean isLeftMargin = true;
        private onViewClickListen listen;
        public MyPagerAdapter(Context context,List<ShareInfo.ListBean> urls,onViewClickListen listen) {
            this.mContext = context;
            this.urls = urls;
            this.listen = listen;
        }

        public void setChoosedId(int position){
            this.choosedId = position;
            notifyDataSetChanged();
        }

        public void setLeftMargin(boolean left){
            if (left != isLeftMargin){
                isLeftMargin = left;
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return urls == null?0:urls.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_invitation_posters_view, null);
            ImageView iv_img = view.findViewById(R.id.iv_item_posters_img);
            ImageView iv_choosed = view.findViewById(R.id.iv_item_posters_choosed);
            ImageView iv_erweima = view.findViewById(R.id.iv_item_posters_erweima);
            TextView tv1 = view.findViewById(R.id.tv1);
            TextView tv2 = view.findViewById(R.id.tv2);
            RelativeLayout rl_bg = view.findViewById(R.id.rl_item_posters_bg);
            RelativeLayout rl_bottom = view.findViewById(R.id.rl_item_posters_choosed);
            RelativeLayout rl_share = view.findViewById(R.id.rl_item_posters_share);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_bg.getLayoutParams();
            if (isLeftMargin){
                layoutParams.leftMargin = MathUtils.dip2px(mContext,15);
                layoutParams.rightMargin = 0;
            }else {
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = MathUtils.dip2px(mContext,15);
            }
            rl_bg.requestLayout();
            if (position == choosedId){
                rl_bg.setSelected(true);
                iv_choosed.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.VISIBLE);
                updateShareView(urls.get(position));
                currentView = rl_share;
            }else {
                rl_bg.setSelected(false);
                iv_choosed.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.GONE);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (choosedId != position && listen != null){
                        listen.onVClick(position,urls.get(position).getImgpath());
                    }
                }
            });
            ImageLoaderUtils.displayWithCache(mContext,iv_img,urls.get(position).getImgpath());
            tv1.setText(urls.get(position).getMaintitle());
            tv2.setText(urls.get(position).getSubtitle());
            if (qrBitmap == null){
                qrBitmap = QrCodeUtil.createBitmap(urls.get(position).getShareUrl());
            }
            if (qrBitmap != null){
                iv_erweima.setImageBitmap(qrBitmap);
            }
            container.addView(view);
            return view;

        }

        /**
         * 页面宽度所占ViewPager测量宽度的权重比例，默认为1
         */
        @Override
        public float getPageWidth(int position) {
            return (float) 0.56;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public interface onViewClickListen{
        void onVClick(int position,String img);
    }
}
