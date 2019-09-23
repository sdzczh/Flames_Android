package app.com.pgy.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.RoundImageView;
import app.com.pgy.Widgets.TitleView;
import app.com.pgy.im.SealAppContext;
import app.com.pgy.im.server.broadcast.BroadcastManager;
import app.com.pgy.im.server.response.GetFriendInfoByIDResponse;
import io.rong.imkit.RongIM;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by tiankui on 16/11/2.
 */

public class UserDetailActivity extends BaseActivity {

    @BindView(R.id.activity_userDetail_title)
    TitleView activityUserDetailTitle;
    @BindView(R.id.activity_userDetail_icon)
    RoundImageView activityUserDetailIcon;
    @BindView(R.id.activity_userDetail_nikeName)
    TextView activityUserDetailNikeName;
    @BindView(R.id.activity_userDetail_sex)
    ImageView activityUserDetailSex;
    @BindView(R.id.activity_userDetail_note)
    EditText activityUserDetailNote;
    @BindView(R.id.activity_userDetail_level)
    TextView activityUserDetailLevel;
    @BindView(R.id.activity_userDetail_deleteFriend)
    TextView activityUserDetailDeleteFriend;
    @BindView(R.id.activity_userDetail_noteFrame)
    LinearLayout activityUserDetailNoteFrame;
    @BindView(R.id.activity_userDetail_isFriend)
    LinearLayout activityUserDetailIsFriend;
    @BindView(R.id.activity_userDetail_notFriend)
    LinearLayout activityUserDetailNotFriend;
    private String friendPhone;
    private String nikeName;
    private String remarkName;
    private String headImg;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_user_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityUserDetailTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        friendPhone = getIntent().getStringExtra("friendPhone");
        getUserMessage(friendPhone);
    }

    /**去服务器获取好友信息*/
    private void getUserMessage(String phone) {
        if (!isLogin()){
            showToast(R.string.unlogin);
            return;
        }
        if (TextUtils.isEmpty(phone)){
            showToast("获取用户失败");
            return;
        }
        showLoading(null);
        Map<String,Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("syetemType",SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("phone", phone);
        NetWorks.getImUserDetail(Preferences.getAccessToken(), map, new getBeanCallback<GetFriendInfoByIDResponse>() {
            @Override
            public void onSuccess(GetFriendInfoByIDResponse userEntity) {
                hideLoading();
                if (Preferences.getLocalUser().getPhone().equals(friendPhone)){
                    /*如果是自己*/
                } else if (!userEntity.isIsFriend()){
                    /*如果是陌生人*/
                    activityUserDetailNoteFrame.setVisibility(View.GONE);
                    activityUserDetailIsFriend.setVisibility(View.GONE);
                    activityUserDetailNotFriend.setVisibility(View.VISIBLE);
                }else{
                    /*如果是好友*/
                    activityUserDetailNoteFrame.setVisibility(View.VISIBLE);
                    activityUserDetailIsFriend.setVisibility(View.VISIBLE);
                    activityUserDetailNotFriend.setVisibility(View.GONE);
                    remarkName = userEntity.getRemarkName();
                    activityUserDetailNote.setText(remarkName);
                }
                nikeName = userEntity.getNickName();
                headImg = userEntity.getHeadPath();
                ImageLoaderUtils.displayCircle(mContext,activityUserDetailIcon,headImg);
                activityUserDetailNikeName.setText(nikeName);
                activityUserDetailLevel.setText(userEntity.getRoleName());
                switch (userEntity.getSex()){
                    /*男性*/
                    case 0:
                        activityUserDetailSex.setVisibility(View.VISIBLE);
                        activityUserDetailSex.setImageResource(R.mipmap.sex_male);
                        break;
                        /*女性*/
                    case 1:
                        activityUserDetailSex.setVisibility(View.VISIBLE);
                        activityUserDetailSex.setImageResource(R.mipmap.sex_female);
                        break;
                        /*默认*/
                    default:
                    case -1:
                        activityUserDetailSex.setVisibility(View.GONE);
                        break;

                }

            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode,reason);
                hideLoading();
            }
        });

    }


    /**开始聊天*/
    public void startChat() {
        if (!isLogin()){
            showToast(R.string.unlogin);
            return;
        }
        if (Preferences.getLocalUser().getPhone().equals(friendPhone)){
            showToast("不能与自己聊天");
            return;
        }
        RongIM.getInstance().startPrivateChat(mContext, friendPhone,nikeName);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserMessage(friendPhone);
    }

    @OnClick({R.id.activity_userDetail_note, R.id.activity_userDetail_sendMessage, R.id.activity_userDetail_deleteFriend, R.id.activity_userDetail_addFriend, R.id.activity_userDetail_sayHello})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*好友，去设置备注*/
            case R.id.activity_userDetail_note:
                Intent intent = new Intent(mContext, NoteInformationActivity.class);
                intent.putExtra("friendPhone", friendPhone);
                intent.putExtra("remarkName", remarkName);
                intent.putExtra("headImg", headImg);
                startActivityForResult(intent, 99);
                break;
                /*好友，发送消息*/
            case R.id.activity_userDetail_sendMessage:
                startChat();
                break;
                /*好友，删除好友*/
            case R.id.activity_userDetail_deleteFriend:
                deleteFriend();
                break;
                /*陌生人，添加好友*/
            case R.id.activity_userDetail_addFriend:
                addFriend();
                break;
                /*陌生人，打个招呼*/
            case R.id.activity_userDetail_sayHello:
                startChat();
                break;
            default:
                break;
        }
    }

    /**删除好友*/
    private void deleteFriend() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", friendPhone);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.deleteIMFriend(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("删除成功");
                hideLoading();
                /*更新好友列表*/
                BroadcastManager.getInstance(mContext).sendBroadcast(SealAppContext.UPDATE_FRIEND);
                UserDetailActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**添加好友*/
    private void addFriend() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", friendPhone);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.addIMFriend(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("申请成功");
                hideLoading();
                UserDetailActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**回调修改的备注*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 155 && data != null) {
            /*回调昵称*/
            String displayName = data.getStringExtra("displayName");
            if (!TextUtils.isEmpty(displayName)) {
                activityUserDetailNote.setText(displayName);
            }
        }
    }

}
