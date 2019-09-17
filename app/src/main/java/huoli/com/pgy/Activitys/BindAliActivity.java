package huoli.com.pgy.Activitys;

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
import huoli.com.pgy.Activitys.Base.BaseUploadPicActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getStringCallback;
import huoli.com.pgy.Interfaces.spinnerSingleChooseListener;
import huoli.com.pgy.Models.Beans.User;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.EdittextUtils;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Utils.KeyboardUtil;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.MyBottomSpinnerList;
import huoli.com.pgy.Widgets.PersonalItemInputView;

import static huoli.com.pgy.Constants.StaticDatas.ALIPAY;
import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/9.
 */

public class BindAliActivity extends BaseUploadPicActivity implements getStringCallback {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.piiv_activity_bind_ali_name)
    PersonalItemInputView piiv_name;
    @BindView(R.id.piiv_activity_bind_ali)
    PersonalItemInputView piiv_ali;
    @BindView(R.id.iv_activity_bind_ali_addImg)
    ImageView iv_add;
    @BindView(R.id.tv_activity_bind_ali_submit)
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
    private boolean isBindAli;

    @Override
    public int getContentViewId() {
        return R.layout.activity_bind_ali;
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
        tv_title.setText("绑定支付宝");
        typesList = new ArrayList<>();
        typesList.add("相机");
        typesList.add("从相册选择");
        /*是否绑定支付宝*/
        User.BindInfoModel aliPayInfo = Preferences.getUserPayInfo(StaticDatas.ALIPAY);
        if (aliPayInfo == null){
            return;
        }
        /*如果有绑定信息，则设置在界面上*/
        userName = aliPayInfo.getName();
        piiv_name.setRightTxt(userName);
        piiv_name.getEdt().setSelection(TextUtils.isEmpty(userName)?0:userName.length());
        userAccount = aliPayInfo.getAccount();
        piiv_ali.setRightTxt(userAccount);
        piiv_ali.getEdt().setSelection(TextUtils.isEmpty(userAccount)?0:userAccount.length());
        erWeiMaUrl = aliPayInfo.getImgUrl();
        ImageLoaderUtils.display(mContext,iv_add,erWeiMaUrl,R.mipmap.add_erweima);
        isBindAli = !TextUtils.isEmpty(aliPayInfo.getAccount());
        tv_submit.setText(isBindAli ?"修改绑定信息":"确认绑定");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        piiv_ali.getEdt().setFilters(new InputFilter[]{EdittextUtils.getAli(getApplicationContext()),new InputFilter.LengthFilter(20)});
        piiv_name.getEdt().setFilters(new InputFilter[]{EdittextUtils.getNoEmoji(getApplicationContext()),new InputFilter.LengthFilter(10)});

        /*添加MineIconFragment中的头像修改成功监听*/
        setStringCallback(this);
        setSpinnerListener();
    }

    @OnClick({R.id.iv_back,R.id.iv_activity_bind_ali_addImg, R.id.tv_activity_bind_ali_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            /*上传二维码*/
            case R.id.iv_activity_bind_ali_addImg:
                KeyboardUtil.hideSoftKeyboard(this);
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                showPhotoChooseDialog();
                break;
            /*提交绑定*/
            case R.id.tv_activity_bind_ali_submit:
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
        userAccount = piiv_ali.getRightTxt();
        /*判断输入是否有空*/
        if (TextUtils.isEmpty(userAccount)) {
            showToast("请输入支付宝账号");
            return;
        }
        if (userAccount.length() < 6) {
            showToast("支付宝账号长度不能小于6位");
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
                submitBindAli2Net(string);
            }
        });
    }

    /**
     * 去服务器绑定微信
     */
    private void submitBindAli2Net(String pwd) {
        showLoading(tv_submit);
        Map<String, Object> map = new HashMap<>();
        map.put("name", userName);
        map.put("account",userAccount);
        map.put("imgUrl", erWeiMaUrl);
        map.put("password", pwd);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.bindAliPay(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("绑定支付宝成功");
                LogUtils.w(TAG, "绑定支付宝成功");
                User.BindInfoModel infoModel = new User.BindInfoModel();
                infoModel.setType(ALIPAY);
                infoModel.setName(userName);
                infoModel.setAccount(userAccount);
                infoModel.setImgUrl(erWeiMaUrl);
                Preferences.saveUserPay_Ali(infoModel);
                hideLoading();
                BindAliActivity.this.finish();
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
