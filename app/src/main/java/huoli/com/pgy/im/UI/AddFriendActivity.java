package huoli.com.pgy.im.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.im.adapter.AddFriendListAdapter;
import huoli.com.pgy.im.server.response.GetUserInfoByPhoneResponse;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/***
 * @author xuqingzhong
 * 搜索好友界面
 */

public class AddFriendActivity extends BaseActivity implements getPositionCallback {
    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    @BindView(R.id.layout_searchTitle_input)
    EditText layoutSearchTitleInput;
    @BindView(R.id.activity_addFriend_list)
    EasyRecyclerView recyclerView;
    private AddFriendListAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_add_friend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        if (adapter == null){
            adapter = new AddFriendListAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        /*添加Layout*/
        recyclerView.setLayoutManager(layoutManager);
        /*添加加载进度条*/
        recyclerView.setAdapter(adapter);
        adapter.setCallback(this);
        layoutSearchTitleInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String mPhone = layoutSearchTitleInput.getText().toString().trim();
                    if (TextUtils.isEmpty(mPhone)) {
                        showToast("搜索内容为空");
                    }else {
                        hintKbTwo();
                        request(mPhone);
                    }
                    return true;
                }
                return false;
            }
        });

    }


    /**
     * 调用接口，查询输入手机号
     */
    private void request(String searchPhone) {
        if (!isLogin()){
            showToast(R.string.unlogin);
            return;
        }
        adapter.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("syetemType",SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("account", searchPhone);
        NetWorks.imFindFriend(Preferences.getAccessToken(), map, new getBeanCallback<GetUserInfoByPhoneResponse>() {
            @Override
            public void onSuccess(GetUserInfoByPhoneResponse getUserInfoByPhoneResponse) {
                List<GetUserInfoByPhoneResponse.ResultEntity> list = getUserInfoByPhoneResponse.getList();
                if (list == null || list.size() <= 0) {
                    showToast("未搜到好友信息");
                    recyclerView.showEmpty();
                    return;
                }
                adapter.addAll(list);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
            }
        });
    }

    @OnClick({R.id.layout_searchTitle_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*点击取消*/
            case R.id.layout_searchTitle_cancel:
                AddFriendActivity.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    public void getPosition(int pos) {
        /*点击获取选中的好友*/
        //Friend mFriend = new Friend(item.getPhone(),item.getNickName(),Uri.parse(item.getHeadPath()));
        GetUserInfoByPhoneResponse.ResultEntity item = adapter.getItem(pos);
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra("friendPhone", item.getPhone());
        intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
        startActivity(intent);
    }
}
