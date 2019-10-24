package app.com.pgy.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.security.rp.RPSDK;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Activitys.HuiLvActivity;
import app.com.pgy.Activitys.InVitationActivity;
import app.com.pgy.Activitys.LoginActivity;
import app.com.pgy.Activitys.MyWalletActivity;
import app.com.pgy.Activitys.PersonalGroupsActivity;
import app.com.pgy.Activitys.PersonalInfoActivity;
import app.com.pgy.Activitys.SecuritycenterActivity;
import app.com.pgy.Activitys.SystemSettingActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.EventBean.EventRealName;
import app.com.pgy.Models.Beans.EventBean.EventUserInfoChange;
import app.com.pgy.Models.Beans.RealNameResult;
import app.com.pgy.Models.Beans.StringNameBean;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Services.MyWebSocket;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.ExitDialog;
import app.com.pgy.Widgets.PersonalItemView;
import app.com.pgy.im.SealConst;
import app.com.pgy.im.db.Friend;
import app.com.pgy.im.server.broadcast.BroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/7.
 */

public class PersonalFragment extends BaseFragment {
    private static PersonalFragment instance;
    @BindView(R.id.rl_fragment_personal_userInfo)
    RelativeLayout rl_userInfo;
    @BindView(R.id.riv_fragment_personal_headerImg)
    RoundedImageView riv_headerImg;
    @BindView(R.id.tv_fragment_personal_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_fragment_personal_tel)
    TextView tv_tel;
    @BindView(R.id.piv_fragment_personal_item_version)
    PersonalItemView pivFragmentPersonalItemVersion;
    @BindView(R.id.tv_fragment_personal_loginout)
    TextView tvFragmentPersonalLoginout;
    @BindView(R.id.tv_fragment_personal_realLevel)
    TextView tvFragmentPersonalRealLevel;
    @BindView(R.id.tv_fragment_personal_matterLevel)
    TextView tvFragmentPersonalMatterLeve;
    private String jinglingName, jinglingId;

    public static PersonalFragment newInstance() {
        if (instance == null) {
            instance = new PersonalFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        switchScene(null);
        refreshUserMessage();
    }

    private void refreshUserMessage() {
        if (isLogin()) {
            User user = Preferences.getLocalUser();
            tv_nickname.setText(user.getName());
            tv_tel.setText("UID：" + user.getUuid());
            ImageLoaderUtils.displayCircle(mContext, riv_headerImg, Preferences.getLocalUser().getHeadImg());
            tvFragmentPersonalLoginout.setVisibility(View.VISIBLE);
            tvFragmentPersonalRealLevel.setVisibility(View.VISIBLE);
            tvFragmentPersonalMatterLeve.setVisibility(View.VISIBLE);
            tvFragmentPersonalRealLevel.setText("v2   实名等级");
            tvFragmentPersonalMatterLeve.setText("奖励等级");
        } else {
            tv_nickname.setText("登录/注册");
            tv_tel.setText("欢迎来到蒲公英");
            riv_headerImg.setImageResource(R.mipmap.icon_unlogin);
            tvFragmentPersonalLoginout.setVisibility(View.GONE);
            tvFragmentPersonalRealLevel.setVisibility(View.GONE);
            tvFragmentPersonalMatterLeve.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_fragment_personal_nickname,R.id.ll_fragment_personal_userinfo, R.id.riv_fragment_personal_headerImg, R.id.rl_fragment_personal_userInfo,
            R.id.piv_fragment_personal_item_wallet, R.id.piv_fragment_personal_item_safety,
            R.id.piv_fragment_personal_item_poster, R.id.piv_fragment_personal_item_group, R.id.piv_fragment_personal_item_yibi,
            R.id.piv_fragment_personal_item_system, R.id.iv_invitation, R.id.piv_fragment_personal_item_web,
            R.id.ll_fragment_personal_renzheng,R.id.piv_fragment_personal_item_help,R.id.piv_fragment_personal_item_version,
            R.id.piv_fragment_personal_item_huilv,R.id.piv_fragment_personal_item_feelv,R.id.tv_fragment_personal_loginout})
    public void onViewClick(View v) {
        Intent intent = null;
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.tv_fragment_personal_nickname:
            case R.id.ll_fragment_personal_userinfo:
            case R.id.riv_fragment_personal_headerImg:
            case R.id.rl_fragment_personal_userInfo:
                if (isLogining()) {
                    intent = new Intent(mContext, PersonalInfoActivity.class);
                }
                break;
            case R.id.piv_fragment_personal_item_wallet:
                //  跳转我的钱包
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyWalletActivity.class);
                    intent.putExtra("index", 0);
                }
                break;
            case R.id.piv_fragment_personal_item_safety:
                if (isLogining()) {
                    intent = new Intent(mContext, SecuritycenterActivity.class);
                }
                break;
            case R.id.iv_invitation:
            case R.id.piv_fragment_personal_item_poster:
                // 2018/7/7 跳转邀请海报
                if (isLogining()) {
                    intent = new Intent(mContext, InVitationActivity.class);
                }
                break;
            case R.id.piv_fragment_personal_item_group:
                //  跳转加入社群
                intent = new Intent(mContext, PersonalGroupsActivity.class);
                break;
            case R.id.piv_fragment_personal_item_yibi:
                if (!isLogining()) {
                    return;
                }
                /*从配置文件获取COIN精灵*/
//                Configuration.YibiElve yibijingling = getConfiguration().getYibiElve();
//                if (yibijingling == null) {
//                    yibijingling = new Configuration.YibiElve();
//                }
//                jinglingId = yibijingling.getPhone();
//                jinglingName = TextUtils.isEmpty(yibijingling.getName()) ? "COIN精灵" : yibijingling.getName();
//                String headPath = yibijingling.getHeadPath();
//                RongIM.getInstance().refreshUserInfoCache(new Friend(jinglingId, jinglingName, Uri.parse(headPath)));
//                try {
//                    RongIM.getInstance().startPrivateChat(mContext, jinglingId, jinglingName);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    showToast("系统异常");
//                }
                break;
            case R.id.piv_fragment_personal_item_web:
                /*跳转COIN官网*/
                intent = new Intent(mContext, WebDetailActivity.class);
                intent.putExtra("title", "蒲公英官网");
                intent.putExtra("url", getConfiguration().getIndexUrl());
                break;

            case R.id.piv_fragment_personal_item_system:
                intent = new Intent(mContext, SystemSettingActivity.class);
                break;

            case R.id.ll_fragment_personal_renzheng:
                // 跳转实名认证
                if (isLogining()){
                    if (Preferences.getLocalUser().isIdCheckFlag()) {
                        showToast("您已完成实名认证");
                        return;
                    }
                    start2RealName();
                }
                break;
            case R.id.piv_fragment_personal_item_help:
                if (isLogining()) {

                }
                break;
            case R.id.piv_fragment_personal_item_version:
                if (isLogining())
                break;
            case R.id.piv_fragment_personal_item_huilv:
                intent = new Intent(mContext, HuiLvActivity.class);
                break;
            case R.id.piv_fragment_personal_item_feelv:
                int a1 = 0;
                break;
            case R.id.tv_fragment_personal_loginout:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                ShowExitDialog();
                break;
            default:
                break;

        }
        if (intent != null) {
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
    }
    private ExitDialog exitDialog;

    /**
     * 退出
     */
    private void ShowExitDialog() {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("确定要退出当前账号？");
        builder.setCancelable(true);

        builder.setPositiveButton("确定退出",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Preferences.clearAllUserData()) {
                            showToast("退出登录成功");
                            tvFragmentPersonalLoginout.setVisibility(View.GONE);
                            sendLogoutMessage();
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        exitDialog = builder.create();
        exitDialog.show();
    }
    /**
     * 发送退出登录广播，在baseFragment和baseActivity中接收
     */
    private void sendLogoutMessage() {
        LogUtils.w("receiver", "Setting发送退出通知");
        EventBus.getDefault().post(new EventLoginState(false));
        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.EXIT);
        MyWebSocket.getMyWebSocket().stopSocket();
        refreshUserMessage();
    }

    private boolean isLogining() {
        if (isLogin()) {
            return true;
        }
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        return false;
    }

    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        refreshUserMessage();
    }

    /**
     * 用户信息改变状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventUserInfoCHange(EventUserInfoChange change) {
        refreshUserMessage();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /*切换底部行情页面的时候*/
        LogUtils.w("home", "home----onHiddenChanged:" + hidden);
        if (!hidden) {
            switchScene(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private String taskId;
    /**
     * 去请求实名认证token
     */
    private void start2RealName() {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getRealNameToken(Preferences.getAccessToken(), map, new getBeanCallback<RealNameResult>() {
            @Override
            public void onSuccess(RealNameResult realNameResult) {
                if (realNameResult == null) {
                    realNameResult = new RealNameResult();
                }
                LogUtils.w("realName", "start2RealName:" + realNameResult.toString());
                LogUtils.w(TAG, realNameResult.toString());
                String verifyToken = realNameResult.getToken();
                taskId = realNameResult.getTaskId();
                if (TextUtils.isEmpty(verifyToken)) {
                    showToast("获取token失败");
                    return;
                }
                start2Certification(verifyToken);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
            }
        });
    }

    /**
     * 开始认证
     */
    private void start2Certification(String verifyToken) {
        RPSDK.start(verifyToken, mContext, new RPSDK.RPCompletedListener() {
            @Override
            public void onAuditResult(RPSDK.AUDIT audit) {
                LogUtils.w("realName", "阿里认证结果：" + audit);
                if (TextUtils.isEmpty(taskId)) {
                    return;
                }
                getRealNameState(taskId);
                if (audit == RPSDK.AUDIT.AUDIT_PASS) { //认证通过
                    //showToast("认证通过");

                } else if (audit == RPSDK.AUDIT.AUDIT_FAIL) { //认证不通过
                    //showToast("认证不通过");
                } else if (audit == RPSDK.AUDIT.AUDIT_NOT) { //未认证，用户取消
                    //showToast("未认证，用户取消");
                }
            }

        });
    }


    private void getRealNameState(String taskId) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getRealNameStatus(Preferences.getAccessToken(), map, new getBeanCallback<StringNameBean>() {
            @Override
            public void onSuccess(StringNameBean realNameStatus) {
                hideLoading();
                showToast("实名认证成功");
                LogUtils.w("realName", "getRealNameState：" + realNameStatus.toString());
                Preferences.saveUserName(realNameStatus.getName());
                LogUtils.w("realName", "userName:" + realNameStatus.getName());
                Preferences.setIsHasRealName(true);
                EventBus.getDefault().post(new EventRealName(true));
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                /*网络错误*/
            }
        });

    }

}
