package app.com.pgy.Activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Adapters.BannerListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.BannerInfo;
import app.com.pgy.Models.Beans.C2cNormalEntrust;
import app.com.pgy.Models.ListBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static app.com.pgy.Constants.StaticDatas.NORMAL;
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
        tvTitle.setText("PGY 活动");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityBaselistList.setLayoutManager(new LinearLayoutManager(mContext));
        activityBaselistList.setAdapter(adapter);
        activityBaselistList.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.transparent), MathUtils.dip2px(mContext,15)));
        requestData();
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
