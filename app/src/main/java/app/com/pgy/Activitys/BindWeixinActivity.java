package app.com.pgy.Activitys;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseUploadPicActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.EdittextUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.KeyboardUtil;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.MyBottomSpinnerList;
import app.com.pgy.Widgets.PersonalItemInputView;

/**
 * Created by YX on 2018/7/7.
 */

public class BindWeixinActivity extends BaseUploadPicActivity implements getStringCallback {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.piiv_activity_bind_weixin_name)
    PersonalItemInputView piiv_name;
    @BindView(R.id.piiv_activity_bind_weixin)
    PersonalItemInputView piiv_wx;
    @BindView(R.id.iv_activity_bind_weixin_addImg)
    ImageView iv_add;
    @BindView(R.id.tv_activity_bind_weixin_submit)
    TextView tv_submit;

    /**
     * 用户输入的三方账户、账户姓名、交易密码
     */
    private String userName,userAccount,erWeiMaUrl;
    private MyBottomSpinnerList photoChoose;
    private spinnerSingleChooseListener spinnerTypesListener;
    /**
     * 选择下拉列表数据和位置
     */
    private List<String> typesList;
    private boolean isBindWx;

    @Override
    public int getContentViewId() {
        return R.layout.activity_bind_weixin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化界面
     */
    @Override
    protected void initData() {
        tv_title.setText("绑定微信");
        typesList = new ArrayList<>();
        typesList.add("相机");
        typesList.add("从相册选择");
        /*是否绑定支付宝*/
        User.BindInfoModel wxPayInfo = Preferences.getUserPayInfo(StaticDatas.WECHART);
        if (wxPayInfo == null){
            return;
        }
        /*如果有绑定信息，则设置在界面上*/
        userName = wxPayInfo.getName();
        piiv_name.setRightTxt(userName);
        piiv_name.getEdt().setSelection(TextUtils.isEmpty(userName)?0:userName.length());
        userAccount = wxPayInfo.getAccount();
        piiv_wx.setRightTxt(userAccount);
        piiv_wx.getEdt().setSelection(TextUtils.isEmpty(userAccount)?0:userAccount.length());
        erWeiMaUrl = wxPayInfo.getImgUrl();
        ImageLoaderUtils.display(mContext,iv_add,erWeiMaUrl,R.mipmap.add_erweima);
        isBindWx = !TextUtils.isEmpty(wxPayInfo.getAccount());
        tv_submit.setText(isBindWx ?"修改绑定信息":"确认绑定");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        piiv_wx.getEdt().setFilters(new InputFilter[]{EdittextUtils.getWechat(getApplicationContext()),new InputFilter.LengthFilter(20)});
        piiv_name.getEdt().setFilters(new InputFilter[]{EdittextUtils.getNoEmoji(getApplicationContext()),new InputFilter.LengthFilter(10)});

        /*添加MineIconFragment中的头像修改成功监听*/
        setStringCallback(this);
        setSpinnerListener();
    }

    @OnClick({R.id.iv_back,R.id.iv_activity_bind_weixin_addImg, R.id.tv_activity_bind_weixin_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            /*上传二维码*/
            case R.id.iv_activity_bind_weixin_addImg:
                KeyboardUtil.hideSoftKeyboard(this);
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                showPhotoChooseDialog();
                break;
            /*提交绑定*/
            case R.id.tv_activity_bind_weixin_submit:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                submit();
                break;
            default:
                break;
        }
    }

    private void showPhotoChooseDialog() {
        photoChoose = new MyBottomSpinnerList(mContext, typesList);
        photoChoose.setMySpinnerListener(spinnerTypesListener);
        photoChoose.showUp(iv_add);
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
     * 提交三方绑定
     */
    private void submit() {
        userName = piiv_name.getRightTxt();
        if (TextUtils.isEmpty(userName)){
            showToast("请输入收款人姓名");
            return;
        }
        /*获取输入的内容*/
        userAccount = piiv_wx.getRightTxt();
        /*判断输入是否有空*/
        if (TextUtils.isEmpty(userAccount)) {
            showToast("请输入微信账号");
            return;
        }
        if (userAccount.length() < 6) {
            showToast("微信账号长度不能小于6位");
            return;
        }
        /*判断输入是否有空*/
        if (TextUtils.isEmpty(erWeiMaUrl)) {
            showToast("请上传收款二维码");
            return;
        }
         /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                submitBindWx2Net(string);
            }
        });
    }

    /**
     * 去服务器绑定微信
     */
    private void submitBindWx2Net(String pwd) {
        showLoading(tv_submit);
        Map<String, Object> map = new HashMap<>();
        map.put("name", userName);
        map.put("account", userAccount);
        map.put("imgUrl", erWeiMaUrl);
        map.put("password", pwd);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.bindWxPay(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("绑定微信成功");
                LogUtils.w(TAG, "绑定微信成功");
                User.BindInfoModel infoModel = new User.BindInfoModel();
                infoModel.setName(userName);
                infoModel.setType(StaticDatas.WECHART);
                infoModel.setAccount(userAccount);
                infoModel.setImgUrl(erWeiMaUrl);
                Preferences.saveUserPay_Wechart(infoModel);
                hideLoading();
                BindWeixinActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**
     * 回调上传的图片
     */
    @Override
    public void getString(String string) {
        erWeiMaUrl = string;
        ImageLoaderUtils.display(mContext,iv_add,erWeiMaUrl,R.mipmap.add_erweima);
    }

}
