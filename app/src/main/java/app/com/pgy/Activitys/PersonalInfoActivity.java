package app.com.pgy.Activitys;

import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseUploadPicActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.EventBean.EventUserInfoChange;
import app.com.pgy.Models.Beans.MyPersonal;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.EdittextUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.KeyboardUtil;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.MyBottomSpinnerList;
import app.com.pgy.Widgets.PersonalItemView;
import app.com.pgy.im.SealConst;
import app.com.pgy.im.server.broadcast.BroadcastManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/7.
 */

public class PersonalInfoActivity extends BaseUploadPicActivity implements getStringCallback {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.riv_activity_personal_info_headerImg)
    RoundedImageView riv_header;
    @BindView(R.id.rl_activity_personal_info_nickname)
    RelativeLayout rl_nickname;
    @BindView(R.id.edt_activity_personal_info_nickname)
    EditText edt_nickname;
    @BindView(R.id.piv_activity_personal_info_sex)
    PersonalItemView piv_sex;
    @BindView(R.id.piv_activity_personal_info_force)
    PersonalItemView piv_force;

    /**
     * 头像选择下拉列表数据和位置
     */
    private List<String> typesList;

    private MyBottomSpinnerList photoChoose;
    private spinnerSingleChooseListener spinnerTypesListener;
    private User user;
    private MyPersonal myPersonal;

    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!Preferences.isLogin()){
            showToast("请先登录！");
            finish();
            return;
        }
        tv_title.setText("个人信息");
        /*添加MineIconFragment中的头像修改成功监听*/
        setStringCallback(this);
        setSpinnerListener();
        typesList = new ArrayList<>();
        typesList.add("相机");
        typesList.add("从相册选择");
        updateData();
        getPersionalInfo();
        edt_nickname.setFilters(new InputFilter[]{EdittextUtils.getNoEmoji(getApplicationContext()),new InputFilter.LengthFilter(10)});
    }

    private void updateData(){
        user = Preferences.getLocalUser();
        if (user == null){
            showToast("本地用户信息为空！");
            finish();
            return;
        }
        ImageLoaderUtils.displayCircle(mContext,riv_header,user.getHeadImg());
        if (!TextUtils.isEmpty(user.getName())){
            edt_nickname.setText(user.getName());
        }
    }

    @OnClick({R.id.iv_back,R.id.riv_activity_personal_info_headerImg,/*R.id.rl_activity_personal_info_nickname,*/R.id.tv_activity_personal_info_submit})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.riv_activity_personal_info_headerImg:
                showPhotoChooseDialog();
                break;
//            case R.id.rl_activity_personal_info_nickname:
//                edt_nickname.setFocusable(true);
//                edt_nickname.setFocusableInTouchMode(true);
//                edt_nickname.requestFocus();
//                int currentIndex = edt_nickname.getText().toString().length();
//                edt_nickname.setSelection(currentIndex > 0 ? currentIndex : 0);
//                KeyboardUtil.showSoftKeyboard(this);
//                break;
            case R.id.tv_activity_personal_info_submit:
                if (edt_nickname.getText().length() < 1|| edt_nickname.getText().toString().trim().length() < 1){
                    showToast("请输入昵称");
                }else  if (edt_nickname.getText().toString().trim().equals(Preferences.getLocalUser().getName())){
                    showToast("昵称未修改");
                }else {
                    changeName(edt_nickname.getText().toString().trim());
                }
                break;
        }
    }

    private void showPhotoChooseDialog() {
        KeyboardUtil.hideSoftKeyboard(this);
        photoChoose = new MyBottomSpinnerList(mContext, typesList);
        photoChoose.setMySpinnerListener(spinnerTypesListener);
        photoChoose.showUp(riv_header);
    }


    private void getPersionalInfo(){
        showLoading(null);
        final Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getPersionalInfo(Preferences.getAccessToken(), map, new getBeanCallback<MyPersonal>() {
            @Override
            public void onSuccess(MyPersonal myPersonal) {
                hideLoading();
                if (myPersonal == null){
                    myPersonal = new MyPersonal();
                }
                piv_sex.setRightTxt(myPersonal.getSexStr());
                piv_force.setRightTxt(myPersonal.getGrade());
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,reason);
                piv_sex.setRightTxt("");
                piv_force.setRightTxt("");
            }
        });
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

    /**
     * 将url上传
     */
    protected void setIcon2Net(final String iconUrl) {
        if (TextUtils.isEmpty(iconUrl)){
            showToast("获取图片地址为空");
            return;
        }
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("imgPath", iconUrl);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.setUserIcon(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("设置头像成功");
                LogUtils.w(TAG, "设置成功");
                Preferences.setUserLogo(iconUrl);
                EventBus.getDefault().post(new EventUserInfoChange());
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(user.getPhone(), user.getName(), Uri.parse(iconUrl)));
                }
                BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.CHANGEINFO);
                updateData();
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    private void changeName(final String newName){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("nickname", newName);
        NetWorks.changeName(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                Preferences.saveUserName(newName);
                EventBus.getDefault().post(new EventUserInfoChange());
                BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.CHANGEINFO);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getPhone(), newName, Uri.parse(user.getHeadImg())));
                RongIM.getInstance().setCurrentUserInfo(new UserInfo(user.getPhone(), newName, Uri.parse(user.getHeadImg())));
                showToast("修改昵称成功");
                LogUtils.w(TAG, "修改昵称成功");
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        LogUtils.w("login", "GoodsBuyFragment:" + loginState.isLoged());
        if(!loginState.isLoged()){
            finish();
        }
    }
    @Override
    public void getString(String string) {
        /*更换头像回调*/
        LogUtils.w("realName", "iconUrl:" + string);
        setIcon2Net(string);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtil.hideSoftKeyboard(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


}
