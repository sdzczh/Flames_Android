package app.com.pgy.im.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.R;
import app.com.pgy.im.widget.MorePopWindow;


/***
 * 聊天主页面，包括消息、通讯、社群
 * @author xu
 */
public class IMMainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.activity_imMain_title_group)
    RadioGroup activityImMainTitleGroup;
    @BindView(R.id.activity_imMain_title_more)
    ImageView activityImMainTitleMore;
    private ArrayList<Fragment> fragments;
    private FragmentManager fm;
    private Fragment mContent = null;


    @Override
    public int getContentViewId() {
        return R.layout.activity_im_main;
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
        activityImMainTitleGroup.setOnCheckedChangeListener(this);
        //获取默认值
        switchContent(fragments.get(0));
    }

    /**
     * 添加所有fragment
     */
    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(IMMessageFragment.newInstance());
        fragments.add(IMContactFragment.newInstance());
        fragments.add(IMChatRoomListFragment.newInstance());
        return fragments;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            //消息
            case R.id.activity_imMain_title_group_message:
                switchContent(fragments.get(0));
                break;
                /*通讯录*/
            case R.id.activity_imMain_title_group_contact:
                switchContent(fragments.get(1));
                break;
            //社群
            case R.id.activity_imMain_title_group_chatRoom:
                switchContent(fragments.get(2));
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
        if (to == mContent){
            return;
        }
        if (fm == null) {
            fm = getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (mContent == null) {
            /*第一次进入，当前content为空，则添加第一个*/
            ft.add(R.id.activity_imMain_content, to);
        } else {
            if (!to.isAdded()) {
                ft.hide(mContent).add(R.id.activity_imMain_content, to);
            } else {
                ft.hide(mContent).show(to);
            }
        }
        ft.commitAllowingStateLoss();
        mContent = to;
    }

    @OnClick({R.id.activity_imMain_title_back, R.id.activity_imMain_title_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.activity_imMain_title_back:
                IMMainActivity.this.finish();
                break;
                /*加号*/
            case R.id.activity_imMain_title_more:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                MorePopWindow morePopWindow = new MorePopWindow(mContext);
                morePopWindow.showPopupWindow(activityImMainTitleMore);
                break;
                default:break;
        }
    }
}
