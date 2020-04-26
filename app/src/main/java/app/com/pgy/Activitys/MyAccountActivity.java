package app.com.pgy.Activitys;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.androidkun.xtablayout.XTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Adapters.ViewPagerAdapter;
import app.com.pgy.Fragments.MyAccountBibiFragment;
import app.com.pgy.Fragments.MyAccountFabiFragment;
import app.com.pgy.Fragments.MyAccountTotalFragment;
import app.com.pgy.Models.Beans.EventBean.EventAccountChange;
import app.com.pgy.R;
import butterknife.BindView;

/**
 * Create by Android on 2019/10/14 0014
 */
public class MyAccountActivity extends BaseActivity {
    @BindView(R.id.iv_activity_my_account_back)
    ImageView ivActivityMyAccountBack;
    @BindView(R.id.activity_my_account_title)
    XTabLayout activityMyAccountTitle;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private List<Fragment> fragments;
    private List<String> fragmentsName;


    @Override
    public int getContentViewId() {
        return R.layout.activity_my_account;
    }

    @Override
    protected void initData() {
        /*初始化所有fragment*/
        fragments = getFragments();
        fragmentsName = getFragmentsNames();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ivActivityMyAccountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initViewpagerView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    private void initViewpagerView() {
        /*初始化ViewPager的懒加载，添加几页缓存*/
        vpContent.setOffscreenPageLimit(fragments.size());
        setupViewPager(vpContent);
        /*设置ViewPager标题*/
        for (String name : fragmentsName) {
            activityMyAccountTitle.addTab(activityMyAccountTitle.newTab().setText(name));
        }
        activityMyAccountTitle.setupWithViewPager(vpContent);
    }

    /**
     * 将构造出来的不同类别的货币添加到ViewPager的适配器中
     */
    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //往适配器中两个参数，第一个是fragment，第二个是该类在viewpager的标题
        for (int i = 0; i < fragments.size(); i++) {
            adapter.addFragment(fragments.get(i), fragmentsName.get(i));
        }
        mViewPager.setAdapter(adapter);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyAccountTotalFragment());
        fragments.add(new MyAccountFabiFragment());
        fragments.add(new MyAccountBibiFragment());
        return fragments;
    }


    /**
     * 添加所有fragment的标题
     */
    private List<String> getFragmentsNames() {
        List<String> names = new ArrayList<>();
        names.add("总览");
        names.add("法币");
        names.add("币币");
        return names;
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void AccountChangeEvent(EventAccountChange eventAccountChange){
        if (eventAccountChange != null && eventAccountChange.getIndex() != vpContent.getCurrentItem()){
            vpContent.setCurrentItem(eventAccountChange.getIndex());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
