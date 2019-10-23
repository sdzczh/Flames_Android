package app.com.pgy.Activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import app.com.pgy.Models.Beans.C2CEntrustComplaintBean;
import app.com.pgy.Models.Beans.EventBean.EventC2cEntrustList;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.MyBottomSpinnerList;
import butterknife.BindView;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/***
 * C2C委托详情申诉界面
 * @author xuqingzhong
 */

public class C2CEntrustComplaintActivity extends BaseUploadPicActivity implements getStringCallback {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.activity_ccEntrustComplaint_input)
    EditText activityCcEntrustComplaintInput;
    @BindView(R.id.activity_ccEntrustComplaint_submit)
    TextView activityCcEntrustComplaintSubmit;
    @BindView(R.id.activity_ccEntrustComplaint_img1)
    ImageView activityCcEntrustComplaintImg1;
    @BindView(R.id.activity_ccEntrustComplaint_img2)
    ImageView activityCcEntrustComplaintImg2;
    @BindView(R.id.activity_ccEntrustComplaint_img3)
    ImageView activityCcEntrustComplaintImg3;
    @BindView(R.id.activity_ccEntrustComplaint_name1)
    TextView activityCcEntrustComplaintName1;
    @BindView(R.id.activity_ccEntrustComplaint_oldimg1)
    ImageView activityCcEntrustComplaintOldimg1;
    @BindView(R.id.activity_ccEntrustComplaint_oldimg2)
    ImageView activityCcEntrustComplaintOldimg2;
    @BindView(R.id.activity_ccEntrustComplaint_oldimg3)
    ImageView activityCcEntrustComplaintOldimg3;
    @BindView(R.id.activity_ccEntrustComplaint_oldimg)
    LinearLayout activityCcEntrustComplaintOldimg;
    @BindView(R.id.activity_ccEntrustComplaint_name2)
    TextView activityCcEntrustComplaintName2;
    @BindView(R.id.activity_ccEntrustComplaint_content)
    TextView activityCcEntrustComplaintContent;
    @BindView(R.id.activity_ccEntrustComplaint_replyContent)
    TextView activityCcEntrustComplaintReplyContent;
    @BindView(R.id.activity_ccEntrustComplaint_reply)
    LinearLayout activityCcEntrustComplaintReply;
    private int orderId;
    private String content;

    private MyBottomSpinnerList photoChoose;
    private spinnerSingleChooseListener spinnerTypesListener;

    private String img1, img2, img3;
    private int pos = 1;

    private C2CEntrustComplaintBean complaintBean;
    @Override
    public int getContentViewId() {
        return R.layout.activity_c2c_entrust_complaint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        orderId = getIntent().getIntExtra("orderId", -1);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("申诉");

        /*添加MineIconFragment中的头像修改成功监听*/
        setStringCallback(this);
        setSpinnerListener();
    }

    private void initFromNet(){
        showLoading(tvTitle);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);

        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.c2cEntrustComplaint(Preferences.getAccessToken(), map, new getBeanCallback<C2CEntrustComplaintBean>() {
            @Override
            public void onSuccess(C2CEntrustComplaintBean c2CEntrustComplaintBean) {
                hideLoading();
                complaintBean = c2CEntrustComplaintBean;
                if (complaintBean == null){
                    activityCcEntrustComplaintReply.setVisibility(View.GONE);
                }else {
                    if (complaintBean.getImgUrl() == null || complaintBean.getImgUrl().size() <1){
                        activityCcEntrustComplaintOldimg.setVisibility(View.GONE);
                    }else {
                        if (complaintBean.getImgUrl().size() > 0){
                            ImageLoaderUtils.display(mContext, activityCcEntrustComplaintOldimg1, complaintBean.getImgUrl().get(0));
                        }
                        if (complaintBean.getImgUrl().size() > 1){
                            ImageLoaderUtils.display(mContext, activityCcEntrustComplaintOldimg2, complaintBean.getImgUrl().get(1));
                        }
                        if (complaintBean.getImgUrl().size() > 2){
                            ImageLoaderUtils.display(mContext, activityCcEntrustComplaintOldimg3, complaintBean.getImgUrl().get(2));
                        }
                        activityCcEntrustComplaintContent.setText(complaintBean.getReason());
                        activityCcEntrustComplaintReplyContent.setText(complaintBean.getReply());
                        User user = Preferences.getLocalUser();
                        activityCcEntrustComplaintName1.setText(user.getName());
                        activityCcEntrustComplaintName2.setText(user.getName());
                    }

                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
                activityCcEntrustComplaintReply.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 申诉客服
     */
    private void appeal2Net() {
        showLoading(activityCcEntrustComplaintSubmit);
        String imgs = "";
        if (!TextUtils.isEmpty(img1)) {
            imgs += img1 + ",";
        }
        if (!TextUtils.isEmpty(img2)) {
            imgs += img2 + ",";
        }
        if (!TextUtils.isEmpty(img3)) {
            imgs += img3;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("reason", content);
        map.put("imgUrl", imgs);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);

        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.c2cEntrustAppeal(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast("申诉成功");
                LogUtils.w(TAG, "申诉成功");
                EventBus.getDefault().post(new EventC2cEntrustList(true));
                C2CEntrustComplaintActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.activity_ccEntrustComplaint_submit,R.id.activity_ccEntrustComplaint_img1,
            R.id.activity_ccEntrustComplaint_img2, R.id.activity_ccEntrustComplaint_img3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.iv_back:
                C2CEntrustComplaintActivity.this.finish();
                break;
            /*提交申诉*/
            case R.id.activity_ccEntrustComplaint_submit:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                if (orderId < 0) {
                    showToast("订单不存在");
                    return;
                }
                content = activityCcEntrustComplaintInput.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showToast("请输入申诉理由");
                    return;
                }
                appeal2Net();
                break;
            case R.id.activity_ccEntrustComplaint_img1:
                pos=1;
                showPhotoChooseDialog();
            break;
            case R.id.activity_ccEntrustComplaint_img2:
                pos=2;
                showPhotoChooseDialog();
                break;
            case R.id.activity_ccEntrustComplaint_img3:
                pos=3;
                showPhotoChooseDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void getString(String string) {
        if (pos == 1) {
            img1 = string;
            ImageLoaderUtils.display(mContext, activityCcEntrustComplaintImg1, img1, R.mipmap.c2c_add_img);
        }
        if (pos == 2) {
            img2 = string;
            ImageLoaderUtils.display(mContext, activityCcEntrustComplaintImg2, img2, R.mipmap.c2c_add_img);
        }
        if (pos == 3) {
            img1 = string;
            ImageLoaderUtils.display(mContext, activityCcEntrustComplaintImg3, img3, R.mipmap.c2c_add_img);
        }
        pos = -1;

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
