package app.com.pgy.im.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.Utils;
import app.com.pgy.Widgets.RoundImageView;
import app.com.pgy.im.SealAppContext;
import app.com.pgy.im.SealConst;
import app.com.pgy.im.SealUserInfoManager;
import app.com.pgy.im.adapter.FriendListAdapter;
import app.com.pgy.im.db.Friend;
import app.com.pgy.im.server.broadcast.BroadcastManager;
import app.com.pgy.im.server.pinyin.CharacterParser;
import app.com.pgy.im.server.pinyin.PinyinComparator;
import app.com.pgy.im.server.response.GetUserInfosResponse;
import io.rong.imkit.RongIM;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 联系人
 *
 * @author xuqingzhong
 */

public class IMContactFragment extends BaseFragment implements View.OnClickListener {
    private static IMContactFragment instance;
    @BindView(R.id.fragment_imContact_list)
    ListView mListView;
    /**
     * 中部展示的字母提示
     */
    @BindView(R.id.fragment_imContact_group_dialog)
    TextView mDialogTextView;
    /*@BindView(R.id.fragment_imContact_sidebar)
    SideBar mSidBar;*/
    private TextView mUnreadTextView;
    private View mHeadView;
    private PinyinComparator mPinyinComparator;
    private List<GetUserInfosResponse.ResultEntity> mResultList;
    private List<GetUserInfosResponse.ResultEntity> mFilteredFriendList;
    /**
     * 好友列表的 mFriendListAdapter
     */
    private FriendListAdapter mFriendListAdapter;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;
    private String jinglingName,jinglingId;

    public IMContactFragment() {
    }

    public static IMContactFragment newInstance() {
        if (instance == null) {
            instance = new IMContactFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_im_contact;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.w("receive", "onResume");
        updateUI();
        /*更新好友监听*/
        refreshUIListener();
    }

    @Override
    protected void initData() {
        mResultList = new ArrayList<>();
        FriendListAdapter adapter = new FriendListAdapter(mContext, mResultList);
        mListView.setAdapter(adapter);
        mFilteredFriendList = new ArrayList<>();
        mPinyinComparator = PinyinComparator.getInstance();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //mSidBar.setTextView(mDialogTextView);
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header, null);
        mUnreadTextView = mHeadView.findViewById(R.id.tv_unread);
        LinearLayout newFriendsLayout = mHeadView.findViewById(R.id.re_newfriends);
        LinearLayout reYibijingling = mHeadView.findViewById(R.id.re_yibijingling);
        RoundImageView ivYbAvatar = mHeadView.findViewById(R.id.iv_yb_avatar);
        TextView ivYbName = mHeadView.findViewById(R.id.iv_yb_name);
        /*从配置文件获取COIN精灵*/
        Configuration.YibiElve yibijingling = getConfiguration().getYibiElve();
        if (yibijingling == null){
            yibijingling=new Configuration.YibiElve();
        }
        String headPath = yibijingling.getHeadPath();
        ImageLoaderUtils.displayCircle(mContext,ivYbAvatar,headPath);
        jinglingName = TextUtils.isEmpty(yibijingling.getName())?"PGY精灵":yibijingling.getName();
        ivYbName.setText(jinglingName);
        jinglingId = yibijingling.getPhone();
        RongIM.getInstance().refreshUserInfoCache(new Friend(jinglingId,jinglingName, Uri.parse(headPath)));
        /*添加头部新的好友、COIN精灵*/
        mListView.addHeaderView(mHeadView);

        newFriendsLayout.setOnClickListener(this);
        reYibijingling.setOnClickListener(this);
        //设置右侧触摸监听
        /*mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });*/
    }

    /**
     * 搜索我的好友
     */
    @OnClick(R.id.fragment_imContact_search)
    public void onViewClicked() {
        Utils.IntentUtils(mContext, SearchMineFriendActivity.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*新的朋友*/
            case R.id.re_newfriends:
                Preferences.setHasNewFriend(false);
                Utils.IntentUtils(mContext, NewFriendListActivity.class);
                break;
                /*COIN精灵*/
            case R.id.re_yibijingling:
                try{
                    RongIM.getInstance().startPrivateChat(mContext,jinglingId,jinglingName);
                }catch (Exception e){
                    e.printStackTrace();
                    showToast("系统异常");
                }
                break;
                default:break;
        }

    }

    /**
     * 从服务器获取好友列表
     */
    private void updateUI() {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getMyFriendList(Preferences.getAccessToken(), map, new getBeanCallback<GetUserInfosResponse>() {
            @Override
            public void onSuccess(GetUserInfosResponse response) {
                List<GetUserInfosResponse.ResultEntity> list = response.getList();
                mResultList.clear();
                mResultList.addAll(list);
                /*更新到本地数据库*/
                List<Friend> friends = SealUserInfoManager.getInstance().addFriends(list);
                for (Friend friend :friends) {
                    RongIM.getInstance().refreshUserInfoCache(friend);
                }
                updateFriendsList();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
            }
        });
    }

    /**
     * 添加好友更新，新的好友监听
     */
    private void refreshUIListener() {
        BroadcastManager.getInstance(mContext).addAction(SealAppContext.UPDATE_FRIEND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {
                    updateUI();
                }
            }
        });
        boolean isHasNewFriend = Preferences.isHasNewFriend();
        mUnreadTextView.setVisibility(isHasNewFriend?View.VISIBLE:View.INVISIBLE);
    }


    private void updateFriendsList() {
        //updateUI fragment初始化和好友信息更新时都会调用,isReloadList表示是否是好友更新时调用
        if (mResultList != null && mResultList.size() > 0) {
            handleFriendDataForSort();
        }

        // 根据a-z进行排序源数据
        Collections.sort(mResultList, mPinyinComparator);
        //mSidBar.setVisibility(View.VISIBLE);
        mFriendListAdapter = new FriendListAdapter(mContext, mResultList);

        mListView.setAdapter(mFriendListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView.getHeaderViewsCount() > 0) {
                    startFriendDetailsPage(mResultList.get(position - 1));
                } else {
                    startFriendDetailsPage(mFilteredFriendList.get(position));
                }
            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                GetUserInfosResponse.ResultEntity bean = mResultList.get(position - 1);
                startFriendDetailsPage(bean);
                return true;
            }
        });

    }

    private void startFriendDetailsPage(GetUserInfosResponse.ResultEntity friend) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
        intent.putExtra("friendPhone", friend.getPhone());
        startActivity(intent);
    }


    private void handleFriendDataForSort() {
        for (GetUserInfosResponse.ResultEntity friend : mResultList) {
            String spelling = CharacterParser.getInstance().getSpelling(friend.getName());
            String letters = replaceFirstCharacterWithUppercase(spelling);
            friend.setLetters(letters);
        }
    }

    private String replaceFirstCharacterWithUppercase(String spelling) {
        LogUtils.w(TAG,"spelling:"+spelling);
        if (!TextUtils.isEmpty(spelling)) {
            char first = spelling.charAt(0);
            LogUtils.w(TAG,"first:"+first);
            char newFirst = first;
            if (first >= 'a' && first <= 'z') {
                newFirst -= 32;
                LogUtils.w(TAG,"newFirst:"+newFirst);
                String s = spelling.replaceFirst(String.valueOf(first), String.valueOf(newFirst));
                LogUtils.w(TAG,"result:"+s);
                return s;
            } else {
                return spelling;
            }
        } else {
            return "#";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            BroadcastManager.getInstance(mContext).destroy(SealAppContext.UPDATE_FRIEND);
            //BroadcastManager.getInstance(mContext).destroy(SealAppContext.UPDATE_RED_DOT);
            BroadcastManager.getInstance(mContext).destroy(SealConst.CHANGEINFO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
