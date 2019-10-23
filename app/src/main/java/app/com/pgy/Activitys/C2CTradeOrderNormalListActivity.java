package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Adapters.C2CTradeOrderListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.C2cNormalEntrust;
import app.com.pgy.Models.Beans.EventBean.EventC2cEntrustList;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.ConditionsChooseFrame;

import static app.com.pgy.Constants.StaticDatas.NORMAL;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2018/7/10 0010 on 下午 4:34
 * 描述: 法币(C2C)交易->顶部导航栏->订单(普通)
 *
 * @author xu
 */

public class C2CTradeOrderNormalListActivity extends BaseListActivity {
    @BindView(R.id.view_titleWithChoose_title)
    TextView viewTitleWithChooseTitle;
    @BindView(R.id.activity_tradeC2COrderNormal_list)
    EasyRecyclerView recyclerView;
    private C2CTradeOrderListAdapter adapter;
    private int tradeType = -1,stateType = -1;
    private List<String> tradeTypes;
    private List<String> stateTypes;
    private int coinType;

    @Override
    public int getContentViewId() {
        return R.layout.activity_trade_c2corder_normal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            LogUtils.w("EventC2cEntrustList","register");
        }
        if (adapter == null) {
            adapter = new C2CTradeOrderListAdapter(mContext);
        }
        if (tradeTypes == null) {
            tradeTypes = new ArrayList<>();
        }
        tradeTypes.add("买入");
        tradeTypes.add("卖出");

        if (stateTypes == null) {
            stateTypes = new ArrayList<>();
        }
        stateTypes.add("待付款");
        stateTypes.add("待确认");
        stateTypes.add("申诉中");
        stateTypes.add("已完成");
        stateTypes.add("已取消");
        stateTypes.add("超时取消");
        coinType = getIntent().getIntExtra("coinType",0);
    }

    @Override
    public void onDestroy() {
        /*解除顶部币对币监听*/
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            LogUtils.w("EventC2cEntrustList","unregister");
        }
        super.onDestroy();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        viewTitleWithChooseTitle.setText("订单记录");
        init(recyclerView,adapter);
        recyclerView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.divider_line), MathUtils.dip2px(mContext,1)));
    }

    /**
     * 订单详情状态改变，刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event1(EventC2cEntrustList entrustList) {
        LogUtils.w("EventC2cEntrustList","receiver");
        if (entrustList.isRefresh()) {
            LogUtils.w("EventC2cEntrustList","refresh");
            onRefresh();
        }
    }

    @OnClick({R.id.view_titleWithChoose_back, R.id.view_titleWithChoose_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.view_titleWithChoose_back:
                C2CTradeOrderNormalListActivity.this.finish();
                break;
                /*筛选*/
            case R.id.view_titleWithChoose_choose:
                showConditionChooseDialog();
                break;
                default:break;
        }
    }

    /**条件筛选*/
    private void showConditionChooseDialog() {
        ConditionsChooseFrame conditionChoose = new ConditionsChooseFrame(mContext,"订单记录",tradeTypes,stateTypes,tradeType,stateType);
        conditionChoose.setGetConditionsListener(new ConditionsChooseFrame.ChooseConditionsListener() {
            @Override
            public void getConditionsChoose(int firstChoose, int secondChoose) {
                tradeType = firstChoose;
                stateType = secondChoose;
                onRefresh();
            }
        });
        conditionChoose.showUp(viewTitleWithChooseTitle);
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("userRole", NORMAL);
        map.put("orderType", tradeType);
        map.put("state", stateType);
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getC2CEntrustList(Preferences.getAccessToken(), map, new getBeanCallback<C2cNormalEntrust>() {
            @Override
            public void onSuccess(C2cNormalEntrust entrust) {
                List<C2cNormalEntrust.ListBean> list = entrust.getList();
                if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                    adapter.stopMore();
                    return;
                }
                adapter.addAll(list);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                adapter.pauseMore();
                /*List<C2cNormalEntrust.ListBean> c2CTradeOrder = DefaultData.getC2CTradeOrder(tradeType, stateType);
                adapter.addAll(c2CTradeOrder);*/
            }
        });
    }

    @Override
    public void onLoadMore() {
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }

    @Override
    protected void onListItemClick(int position) {
        /*跳转详情*/
        C2cNormalEntrust.ListBean item = adapter.getItem(position);
        if (item == null){
            item = new C2cNormalEntrust.ListBean();
        }
        Intent intent = new Intent(mContext, C2CEntrustDetailsNewActivity.class);
        intent.putExtra("normalOrBusiness",NORMAL);
        intent.putExtra("entrustId",item.getId());
        startActivity(intent);
    }

}
