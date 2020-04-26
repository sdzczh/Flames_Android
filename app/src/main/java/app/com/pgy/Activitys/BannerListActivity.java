package app.com.pgy.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import java.util.HashMap;
import java.util.Map;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Adapters.BannerListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.BannerInfo;
import app.com.pgy.Models.ListBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.Utils;
import app.com.pgy.Widgets.ExitDialog;
import butterknife.BindView;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/28 0028
 */
public class BannerListActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.activity_baselist_list)
    RecyclerView activityBaselistList;

    BannerListAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_banner_list;
    }

    @Override
    protected void initData() {
        if (adapter == null) {
            adapter = new BannerListAdapter(mContext);
        }

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("Flames 活动");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityBaselistList.setLayoutManager(new LinearLayoutManager(mContext));
        activityBaselistList.setAdapter(adapter);
        activityBaselistList.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.transparent), MathUtils.dip2px(mContext,15)));
        adapter.setOnItemClickListener(position -> {
            BannerInfo item = adapter.getItem(position);
            if (item == null){
                return;
            }
            int type = item.getType();
            if (type == 1){
                //跳转认购主界面
                if (isLogin()){
                    Utils.IntentUtils(mContext,RenGouMainActivity.class);
                }else{
                    showLoginDialog();
                }
            }else if (type == 0){
                //跳转网页
                Intent intent = new Intent(mContext, WebDetailActivity.class);
                intent.putExtra("title", ""+item.getTitle());
                intent.putExtra("url", item.getAddress());
                startActivity(intent);
            }
        });
        requestData();
    }
    private ExitDialog exitDialog;

    private void showLoginDialog() {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("当前暂未登录，是否去登录？");
        builder.setCancelable(true);

        builder.setPositiveButton("去登录",
                (dialog, which) -> {
                    dialog.dismiss();
                    Utils.IntentUtils(mContext,LoginActivity.class);
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


    private void requestData(){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();

        map.put("bannerType", 1);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getBannerList(map, new getBeanCallback<ListBean<BannerInfo>>() {
            @Override
            public void onSuccess(ListBean<BannerInfo> list) {
                hideLoading();
                if (list == null || list.getList() == null||list.getList().size() <= 0) {
                    /*再无更多数据*/
                    adapter.stopMore();
                    return;
                }
                adapter.addAll(list.getList());
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                /*网络错误*/
                adapter.pauseMore();
                /*List<C2cNormalEntrust.ListBean> c2CTradeOrder = DefaultData.getC2CTradeOrder(tradeType, stateType);
                adapter.addAll(c2CTradeOrder);*/
            }
        });
    }

}
