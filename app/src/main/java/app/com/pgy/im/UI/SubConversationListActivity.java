package app.com.pgy.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import butterknife.BindView;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.R;
import app.com.pgy.Widgets.TitleView;
import app.com.pgy.im.adapter.SubConversationListAdapterEx;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.SubConversationListFragment;

/**
 * Created by Bob on 15/11/3.
 * 聚合会话列表
 */
public class SubConversationListActivity extends BaseActivity {

    @BindView(R.id.activity_imConversation_title)
    TitleView activityImConversationTitle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_rong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        //聚合会话参数
        String type = intent.getData().getQueryParameter("type");

        if (type == null) {
            return;
        }
        if (type.equals("group")) {
            setTitle(R.string.de_actionbar_sub_group);
        } else if (type.equals("private")) {
            setTitle(R.string.de_actionbar_sub_private);
        } else if (type.equals("discussion")) {
            setTitle(R.string.de_actionbar_sub_discussion);
        } else if (type.equals("system")) {
            setTitle(R.string.de_actionbar_sub_system);
        } else {
            setTitle(R.string.de_actionbar_sub_defult);
        }
    }

    @Override
    public void setTitle(int titleId) {
        activityImConversationTitle.setTitle(mContext.getResources().getString(titleId));
        activityImConversationTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubConversationListActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        SubConversationListFragment fragment = new SubConversationListFragment();
        fragment.setAdapter(new SubConversationListAdapterEx(RongContext.getInstance()));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
    }
}
