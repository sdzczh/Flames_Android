package app.com.pgy.Activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseUploadPicActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.EventBean.EventRealName;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.MyBottomSpinnerList;
import butterknife.BindView;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/31 0031
 */
public class PersonalRenZhengSecondActivity extends BaseUploadPicActivity implements getStringCallback {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_activity_renzheng_addImg)
    ImageView tvActivityRenzhengAddImg;
    @BindView(R.id.tv_activity_renzheng_submit)
    TextView tvActivityRenzhengSubmit;
    private MyBottomSpinnerList photoChoose;
    private spinnerSingleChooseListener spinnerTypesListener;
    private String imgUrl;

    @Override
    public int getContentViewId() {
        return R.layout.activity_renzheng_second;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("Lv.2 高级认证");
        /*添加MineIconFragment中的头像修改成功监听*/
        setStringCallback(this);
        setSpinnerListener();
    }

    @OnClick({R.id.iv_back, R.id.tv_activity_renzheng_addImg, R.id.tv_activity_renzheng_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_renzheng_addImg:
                addPic();
                break;
            case R.id.tv_activity_renzheng_submit:
                submit();
                break;
        }
    }

    private void addPic() {
        showPhotoChooseDialog();
    }

    @Override
    public void getString(String string) {
        if (TextUtils.isEmpty(string)){
            showToast("上传图片未返回");
            return;
        }
        showToast("图片上传成功!");
        imgUrl = string;
        LogUtils.w(TAG,"imgUrl:"+imgUrl);
        Glide.with(mContext).load(imgUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                LogUtils.w(TAG,"e:"+e.toString());
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).placeholder(R.mipmap.renzheng_success).into(tvActivityRenzhengAddImg);
    }

    private void submit() {
        /*获取输入的内容*/
        if (TextUtils.isEmpty(imgUrl)) {
            showToast("请上传图片");
            return;
        }
        showLoading(tvTitle);
        Map<String, Object> map = new HashMap<>();
        map.put("imgUrl", imgUrl);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.renzhengSecond(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                if (Preferences.getUserIdStatus() < 1){
                    Preferences.saveUserIdStatus(1);
                }
                EventBus.getDefault().post(new EventRealName(true));
                PersonalRenZhengSecondActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
//                onFail(errorCode, reason);
                showToast(reason);

                if (errorCode == 30039){
//                    toStateActivity(false);
//                    showToast(reason);
                }else {

                }
            }
        });
    }

    private List<String> typesList;

    private void showPhotoChooseDialog() {
        typesList = new ArrayList<>();
        typesList.add("相机");
        typesList.add("从相册选择");
        photoChoose = new MyBottomSpinnerList(mContext, typesList);
        photoChoose.setMySpinnerListener(spinnerTypesListener);
        photoChoose.showUp(tvTitle);
    }

    /**
     * 头像选择的列表监听
     */
    private void setSpinnerListener() {
        spinnerTypesListener = new spinnerSingleChooseListener() {
            @Override
            public void onItemClickListener(int position) {
                switch (position) {
                    /*点击拍照，调用相机拍照*/
                    case 0:
                        takePhoto();
                        break;
                    /*点击图库，选择相册图片*/
                    case 1:
                        choosePhoto();
                        break;
                    default:
                        break;
                }
            }
        };
    }

}
