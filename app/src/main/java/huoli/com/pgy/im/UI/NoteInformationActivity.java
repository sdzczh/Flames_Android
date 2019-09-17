package huoli.com.pgy.im.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.TitleView;
import huoli.com.pgy.im.SealAppContext;
import huoli.com.pgy.im.SealUserInfoManager;
import huoli.com.pgy.im.db.Friend;
import huoli.com.pgy.im.server.broadcast.BroadcastManager;
import huoli.com.pgy.im.server.pinyin.CharacterParser;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by AMing on 16/8/10.
 * 设置备注
 * Company RongCloud
 */
@SuppressWarnings("deprecation")
public class NoteInformationActivity extends BaseActivity {
    @BindView(R.id.activity_noteInfo_title)
    TitleView activityNoteInfoTitle;
    @BindView(R.id.activity_noteInfo_inputNote)
    EditText activityNoteInfoInputNote;
    @BindView(R.id.activity_noteInfo_update)
    TextView activityNoteInfoUpdate;
    private String friendPhone,remarkName,headImg;
    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_noteinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        friendPhone = getIntent().getStringExtra("friendPhone");
        remarkName = getIntent().getStringExtra("remarkName");
        headImg = getIntent().getStringExtra("headImg");
        if (!TextUtils.isEmpty(remarkName)) {
            activityNoteInfoInputNote.setText(remarkName);
            activityNoteInfoInputNote.setSelection(activityNoteInfoInputNote.getText().length());
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityNoteInfoTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteInformationActivity.this.finish();
            }
        });
    }

    @OnClick(R.id.activity_noteInfo_update)
    public void onViewClicked() {
        if (!isLogin()){
            showToast(R.string.unlogin);
            return;
        }
        String newRemarkName = activityNoteInfoInputNote.getText().toString();
        if (TextUtils.isEmpty(newRemarkName)){
            showToast("备注不能为空");
            return;
        }
        updateNoteFromNet(newRemarkName);
    }

    private void updateNoteFromNet(final String newRemark) {
        showLoading(activityNoteInfoUpdate);
        Map<String, Object> map = new HashMap<>();
        map.put("remarkName", newRemark);
        map.put("phone", friendPhone);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.updateIMFriendRemarkName(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast("修改成功");
                LogUtils.w(TAG, "成功");
                SealUserInfoManager.getInstance().addFriend(
                        new Friend(friendPhone,
                                newRemark, null, newRemark, null, null,
                                null, null,
                                CharacterParser.getInstance().getSpelling(newRemark),
                                CharacterParser.getInstance().getSpelling(newRemark)));
                BroadcastManager.getInstance(mContext).sendBroadcast(SealAppContext.UPDATE_FRIEND);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(friendPhone, newRemark, Uri.parse(headImg)));
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
                intent.putExtra("displayName", newRemark);
                setResult(155, intent);
                finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });

    }
}
