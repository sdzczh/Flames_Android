package huoli.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import huoli.com.pgy.Activitys.Base.BaseListActivity;
import huoli.com.pgy.Adapters.BlockAssetsAdapter;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.MyAssets;
import huoli.com.pgy.Models.Beans.MyWallet;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.TimeUtils;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/16.
 */

public class BlockAssetsActivity extends BaseListActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_activity_my_block_assets)
    EasyRecyclerView rvActivityMyBlockAssets;

    BlockAssetsAdapter adapter;
    boolean isFirst = true;
    @Override
    public int getContentViewId() {
        return R.layout.activity_my_block_assets;
    }

    @Override
    protected void initData() {
        if (adapter == null){
            adapter = new BlockAssetsAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("挖矿资产");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init(rvActivityMyBlockAssets,adapter);
        rvActivityMyBlockAssets.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                                .color((getResources().getColor(R.color.divider_line)))
                                .size(MathUtils.dip2px(getApplicationContext(),1))
                                .build());
    }

    /**点击某Item时候的操作*/
    @Override
    public void onListItemClick(int position) {
        MyWallet.ListBean item = adapter.getItem(position);
        /*点击跳转到币种详情*/
        Intent intent2Detail = new Intent(mContext, BlockAssetsCoininfoActivity.class);
        intent2Detail.putExtra("coinType", item.getCoinType());
        intent2Detail.putExtra("coinAvail",item.getTotalBalance());
        intent2Detail.putExtra("coinCnyAvail",item.getTotalOfCny());
        startActivity(intent2Detail);
    }

    /**下拉刷新*/
    @Override
    public void onRefresh() {
        adapter.clear();
        if (!isLogin()){
            return;
        }
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            rvActivityMyBlockAssets.setRefreshing(false);
            return;
        }
        requestData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            onRefresh();
        }
        isFirst = false;
    }

    /**上拉加载*/
    @Override
    public void onLoadMore() {
        rvActivityMyBlockAssets.setRefreshing(false);
        adapter.stopMore();
    }

    /**请求数据*/
    private void requestData() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType", StaticDatas.ACCOUNT_DIG);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getMyAssetsList(Preferences.getAccessToken(), map, new getBeanCallback<MyAssets>() {
            @Override
            public void onSuccess(MyAssets myAssets) {
                if (myAssets == null) {
                    myAssets = new MyAssets();
                    adapter.stopMore();
                }
                adapter.addAll(myAssets.getList());
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();
            }
        });
    }
}
