package huoli.com.pgy.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.WebDetailActivity;
import huoli.com.pgy.Activitys.InVitationActivity;
import huoli.com.pgy.Activitys.LoginActivity;
import huoli.com.pgy.Activitys.MyWalletActivity;
import huoli.com.pgy.Activitys.PersonalGroupsActivity;
import huoli.com.pgy.Activitys.PersonalInfoActivity;
import huoli.com.pgy.Activitys.SecuritycenterActivity;
import huoli.com.pgy.Activitys.SystemSettingActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Fragments.Base.BaseFragment;
import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.Models.Beans.EventBean.EventLoginState;
import huoli.com.pgy.Models.Beans.EventBean.EventUserInfoChange;
import huoli.com.pgy.Models.Beans.User;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.LoginUtils;
import huoli.com.pgy.im.db.Friend;
import io.rong.imkit.RongIM;

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
    private String jinglingName,jinglingId;

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
            tv_tel.setText("UID："+ user.getUuid());
            ImageLoaderUtils.displayCircle(mContext,riv_headerImg,Preferences.getLocalUser().getHeadImg());
        } else {
            tv_nickname.setText("登录/注册");
            tv_tel.setText("欢迎来到奥丁");
            riv_headerImg.setImageResource(R.mipmap.icon_unlogin);
        }
    }
    @OnClick({R.id.ll_fragment_personal_userinfo,R.id.riv_fragment_personal_headerImg,R.id.rl_fragment_personal_userInfo,R.id.piv_fragment_personal_item_wallet,R.id.piv_fragment_personal_item_safety,
            R.id.piv_fragment_personal_item_poster,R.id.piv_fragment_personal_item_group,R.id.piv_fragment_personal_item_yibi,
            R.id.piv_fragment_personal_item_system,R.id.iv_invitation,R.id.piv_fragment_personal_item_web})
    public void onViewClick(View v){
        Intent intent = null;
        Bundle bundle = null;
        switch (v.getId()){
            case R.id.ll_fragment_personal_userinfo:
            case R.id.riv_fragment_personal_headerImg:
            case R.id.rl_fragment_personal_userInfo:
               if (isLogining()){
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
                if (isLogining()){
                    intent = new Intent(mContext, SecuritycenterActivity.class);
                }
                break;
            case R.id.piv_fragment_personal_item_poster:
                // 2018/7/7 跳转邀请海报
                if (isLogining()){
                    intent = new Intent(mContext, InVitationActivity.class);
                }
                break;
            case R.id.piv_fragment_personal_item_group:
                //  跳转加入社群
                intent = new Intent(mContext, PersonalGroupsActivity.class);
                break;
            case R.id.piv_fragment_personal_item_yibi:
                if (!isLogining()){
                    return;
                }
                 /*从配置文件获取COIN精灵*/
                Configuration.YibiElve yibijingling = getConfiguration().getYibiElve();
                if (yibijingling == null){
                    yibijingling=new Configuration.YibiElve();
                }
                jinglingId = yibijingling.getPhone();
                jinglingName = TextUtils.isEmpty(yibijingling.getName())?"COIN精灵":yibijingling.getName();
                String headPath = yibijingling.getHeadPath();
                RongIM.getInstance().refreshUserInfoCache(new Friend(jinglingId,jinglingName, Uri.parse(headPath)));
                try{
                    RongIM.getInstance().startPrivateChat(mContext,jinglingId,jinglingName);
                }catch (Exception e){
                    e.printStackTrace();
                    showToast("系统异常");
                }
                break;
            case R.id.piv_fragment_personal_item_web:
                /*跳转COIN官网*/
                intent = new Intent(mContext, WebDetailActivity.class);
                intent.putExtra("title","奥丁官网");
                intent.putExtra("url",getConfiguration().getIndexUrl());
                break;
            case R.id.piv_fragment_personal_item_system:
                intent = new Intent(mContext, SystemSettingActivity.class);
                break;
                default:break;
            case R.id.iv_invitation:
                if (isLogining()){
                    intent = new Intent(mContext, InVitationActivity.class);
                }
                break;
        }
        if (intent != null){
            if (bundle != null){
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
    }

    private boolean isLogining(){
        if (isLogin()){
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
     *用户信息改变状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventUserInfoCHange(EventUserInfoChange change) {
        refreshUserMessage();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /*切换底部行情页面的时候*/
        LogUtils.w("home","home----onHiddenChanged:"+hidden);
        if (!hidden){
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
}
