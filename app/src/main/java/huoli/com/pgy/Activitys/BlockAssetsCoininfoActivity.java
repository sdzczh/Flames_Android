package huoli.com.pgy.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseListActivity;
import huoli.com.pgy.Adapters.BlockAssetsCoinFlowAdapter;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.Interfaces.getStringCallback;
import huoli.com.pgy.Models.Beans.BlockAssetFlow;
import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.ExitDialog;
import huoli.com.pgy.Widgets.ShowMessageDialog;

import static huoli.com.pgy.Constants.StaticDatas.ACCOUNT_DIG;
import static huoli.com.pgy.Constants.StaticDatas.MYASSETS_WITHDRAWAL_GOODS;
import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/18.
 */

public class BlockAssetsCoininfoActivity extends BaseListActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_activity_blockassets_coininfo_icon)
    ImageView ivActivityBlockassetsCoininfoIcon;
    @BindView(R.id.tv_activity_blockassets_coininfo_cnname)
    TextView tvActivityBlockassetsCoininfoCnname;
    @BindView(R.id.tv_activity_blockassets_coininfo_avail)
    TextView tvActivityBlockassetsCoininfoAvail;
    @BindView(R.id.tv_activity_blockassets_coininfo_enname)
    TextView tvActivityBlockassetsCoininfoEnname;
    @BindView(R.id.tv_activity_blockassets_coininfo_availCny)
    TextView tvActivityBlockassetsCoininfoAvailCny;
    @BindView(R.id.tv_activity_blockassets_coininfo_desc)
    TextView tvActivityBlockassetsCoininfoDesc;
    @BindView(R.id.rv_activity_blockassets_coininfo_flow)
    EasyRecyclerView rvActivityBlockassetsCoininfoFlow;
    @BindView(R.id.srl_activity_blockassets_coininfo_refresh)
    SmartRefreshLayout srlActivityBlockassetsCoininfoRefresh;


    private BlockAssetsCoinFlowAdapter adapter;
    private int coinType;
    private String avail,cnyAvail;
    private Configuration.CoinInfo coinInfo;
    /**
     * 请求数据的页码，从0开始即第一页，每页的数据由后台定
     */
    protected int pageIndex = StaticDatas.PAGE_START;

    @Override
    public int getContentViewId() {
        return R.layout.activity_blockassets_coininfo;
    }

    @Override
    protected void initData() {
        coinType = getIntent().getIntExtra("coinType", -1);
        coinInfo = getCoinInfo(coinType);
        avail = getIntent().getStringExtra("coinAvail");
        cnyAvail = getIntent().getStringExtra("coinCnyAvail");

        adapter = new BlockAssetsCoinFlowAdapter(this);
        adapter.setGetPositionCallback(new getPositionCallback() {
            @Override
            public void getPosition(int pos) {
                BlockAssetFlow.ListBean item = adapter.getItem(pos);
                final String walletAddress = item.getPayaddress();
                if (TextUtils.isEmpty(walletAddress)){
                    showToast("钱包地址为空");
                    return;
                }
                final ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder(mContext);
                builder.setTitle("钱包地址");
                builder.setMessage(walletAddress);
                builder.setCancelable(true);
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                                cm.setText(walletAddress);
//                                showToast("复制成功");
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (coinType == -1) {
            showToast("币种信息为空");
            finish();
            return;
        }
        if (coinInfo == null) {
            showToast("获取币种信息为空");
            finish();
            return;
        }
        tvTitle.setText("资产详情");
        init(rvActivityBlockassetsCoininfoFlow,adapter);
        rvActivityBlockassetsCoininfoFlow.setRefreshing(false);
        //添加下划线
        rvActivityBlockassetsCoininfoFlow.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(MathUtils.dip2px(getApplicationContext(), 1))
                .color(getResources().getColor(R.color.divider_line))
                .build());
        srlActivityBlockassetsCoininfoRefresh.setEnableLoadMore(false);
        srlActivityBlockassetsCoininfoRefresh.setRefreshHeader(new MaterialHeader(this));


        srlActivityBlockassetsCoininfoRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        srlActivityBlockassetsCoininfoRefresh.autoRefresh();
        tvActivityBlockassetsCoininfoEnname.setText(coinInfo.getCoinname());
        tvActivityBlockassetsCoininfoCnname.setText(coinInfo.getCnname());
        tvActivityBlockassetsCoininfoDesc.setText(coinInfo.getDescription());
        tvActivityBlockassetsCoininfoAvail.setText(""+avail);
        tvActivityBlockassetsCoininfoAvailCny.setText(""+cnyAvail);
        ImageLoaderUtils.displayCircle(getApplicationContext(), ivActivityBlockassetsCoininfoIcon, coinInfo.getImgurl());
    }

    @OnClick({R.id.iv_back, R.id.tv_activity_blockassets_coininfo_toGoods, R.id.tv_activity_blockassets_coininfo_toOther})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_blockassets_coininfo_toGoods:
                showWithdrawal2MineWalletDialog();
                break;
            case R.id.tv_activity_blockassets_coininfo_toOther:
                Intent intent = new Intent(mContext,BlockWithdrawActivity.class);
                intent.putExtra("coinType",coinType);
                startActivityForResult(intent,1000);
                break;
        }
    }


    private void showWithdrawal2MineWalletDialog() {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("确认提取到币币钱包？");
        builder.setCancelable(true);

        builder.setPositiveButton("确认提取",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        withdrawal2GoodsWallet();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**全部提取到现货钱包*/
    private void withdrawal2GoodsWallet() {
         /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                submitWithdraw2Net(string);
            }
        });
    }

    public void refresh() {
        adapter.clear();
        if (!isLogin()){
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            return;
        }
        requestData(pageIndex);
    }

    public void loadMore() {
        if (!isLogin()){
            return;
        }
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }
    /**请求数据*/
    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("coinType",coinType);
        map.put("accountType", ACCOUNT_DIG);
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getBlockWithdrawOrderList(Preferences.getAccessToken(), map, new getBeanCallback<BlockAssetFlow>() {
            @Override
            public void onSuccess(BlockAssetFlow withdrawOrder) {
                if (isFinishing()){
                    return;
                }
                srlActivityBlockassetsCoininfoRefresh.finishRefresh(true);
                List<BlockAssetFlow.ListBean> list = withdrawOrder.getList();
                if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                    adapter.stopMore();
                    return;
                }
                adapter.addAll(list);
            }

            @Override
            public void onError(int errorCode, String reason) {
                if (isFinishing()){
                    return;
                }
                srlActivityBlockassetsCoininfoRefresh.finishRefresh(false);
                onFail(errorCode, reason);
                /*网络错误*/
                adapter.pauseMore();
            }
        });
    }

    /**
     * 去服务器提现
     */
    private void submitWithdraw2Net(String pwd) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("type", MYASSETS_WITHDRAWAL_GOODS);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("password", pwd);
        NetWorks.submitMyAssetsWithdraw(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("成功");
                LogUtils.w(TAG, "提现成功");
                tvActivityBlockassetsCoininfoAvail.setText("0.00");
                tvActivityBlockassetsCoininfoAvailCny.setText("0.00");
                hideLoading();
                BlockAssetsCoininfoActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK){
            tvActivityBlockassetsCoininfoAvailCny.setText("0.00");
            tvActivityBlockassetsCoininfoAvail.setText("0.00");
            refresh();
        }
    }

    @Override
    protected void onListItemClick(int position) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        loadMore();
    }
}
