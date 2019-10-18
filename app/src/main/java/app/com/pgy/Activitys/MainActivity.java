package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import app.com.pgy.Fragments.C2CTradeFragment;
import app.com.pgy.Fragments.HomeFragmentNew;
import butterknife.BindView;
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.CircleFragment;
import app.com.pgy.Fragments.HomeFragment;
import app.com.pgy.Fragments.MarketFragment;
import app.com.pgy.Fragments.PersonalFragment;
import app.com.pgy.Fragments.TradeFragment;
import app.com.pgy.Models.Beans.EventBean.EventGoodsChange;
import app.com.pgy.Models.Beans.EventBean.EventMainChangeState;
import app.com.pgy.R;
import app.com.pgy.Services.HeartbeatService;
import app.com.pgy.Utils.LogUtils;

/**
 * 创建日期：2017/11/21 0021 on 下午 3:45
 * 描述:主界面，包含五个子界面：COIN，行情，交易，生态，我的
 *
 * @author 徐庆重
 */
public class MainActivity extends PermissionActivity implements RadioGroup.OnCheckedChangeListener {
    public final static int REQUEST_KLINE = 0x0031;
    @BindView(R.id.activity_main_group)
    RadioGroup activityMainGroup;
    @BindView(R.id.activity_main_group_goods)
    RadioButton rb_goods;
    private ArrayList<Fragment> fragments;
    private FragmentManager fm;
    private Fragment mContent = null;


    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        //获取所有fragment
        fragments = getFragments();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityMainGroup.setOnCheckedChangeListener(this);

        //获取默认值
        Preferences.setCurrentPushData(null);
        switchContent(fragments.get(0));
        //askForPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA});
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 添加所有fragment
     */
    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
//        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragmentNew.newInstance());
        fragments.add(MarketFragment.getInstance());
        fragments.add(C2CTradeFragment.newInstance());
        fragments.add(TradeFragment.newInstance());
        fragments.add(PersonalFragment.newInstance());
        return fragments;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            /*COIN*/
            case R.id.activity_main_group_oneCoin:
                switchContent(fragments.get(0));

                break;
            /*行情*/
            case R.id.activity_main_group_market:
                switchContent(fragments.get(1));
                break;
            /*生态*/
            case R.id.activity_main_group_circle:
                switchContent(fragments.get(2));
                break;
                /*交易*/
            case R.id.activity_main_group_goods:
                switchContent(fragments.get(3));
                break;

            /*我的*/
            case R.id.activity_main_group_mine:
                switchContent(fragments.get(4));
                break;
            default:
                break;
        }
    }

    /**
     * 页面跳转
     */
    public void switchContent(Fragment to) {
        if (to == null) {
            return;
        }
        if (to == mContent) {
            return;
        }
        if (fm == null) {
            fm = getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (mContent == null) {
            /*第一次进入，当前content为空，则添加第一个*/
            ft.add(R.id.activity_main_container, to);
        } else {
            if (!to.isAdded()) {
                ft.hide(mContent).add(R.id.activity_main_container, to);
            } else {
                ft.hide(mContent).show(to);
            }
        }
        ft.commitAllowingStateLoss();
        mContent = to;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_KLINE){
            //提升算力页面返回，跳转到交易页面
            int type = 0;
            if (data != null){
                type = data.getIntExtra("type", 0);
                LogUtils.e(TAG,"type=="+type);
                if (fragments.get(2) instanceof TradeFragment){
                    ((TradeFragment)fragments.get(2)).setTypes(type);
                }
            }
            rb_goods.setChecked(true);
            EventBus.getDefault().post(new EventGoodsChange(type));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeEvent(EventMainChangeState event){
        if (event != null){
            switch (event.getState()){
                case EventMainChangeState.CHANGE_TO_GOODS:
                    if (activityMainGroup.getCheckedRadioButtonId() != R.id.activity_main_group_goods){
                        activityMainGroup.check(R.id.activity_main_group_goods);
                    }
                    break;
            }
        }
    }



    @Override
    protected void onDestroy() {
        LogUtils.w("exit", "MainActivityOnDestroy");
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        try {
            Intent serviceIntent = new Intent(mContext, HeartbeatService.class);
            stopService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.print("MainActivity,开启心跳服务发生异常");
        }
        super.onDestroy();
    }


}
