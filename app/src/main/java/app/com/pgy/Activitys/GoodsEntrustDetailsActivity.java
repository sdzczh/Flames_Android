package app.com.pgy.Activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Models.Beans.EventBean.EventGoodsToTrade;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Adapters.GoodsEntrustDetailsListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.EntrustDetails;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;
import static app.com.pgy.Constants.StaticDatas.TRADE_LIMIT;
import static app.com.pgy.Constants.StaticDatas.TRADE_MARKET;

/***
 * 现货委托详情页
 * @author xuqingzhong
 */

public class GoodsEntrustDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView activityEntrustDetailsTitle;
    @BindView(R.id.activity_entrustDetails_state)
    TextView activityEntrustDetailsState;
    /**
     * 顶部
     */
    @BindView(R.id.activity_entrustDetails_top_type)
    TextView activityEntrustDetailsTopType;
    @BindView(R.id.activity_entrustDetails_top_entrustType)
    TextView activityEntrustDetailsTopEntrustType;
    @BindView(R.id.activity_entrustDetails_top_entrustType1)
    TextView activityEntrustDetailsTopEntrustType1;
    @BindView(R.id.activity_entrustDetails_top_entrustType2)
    TextView activityEntrustDetailsTopEntrustType2;
    @BindView(R.id.activity_entrustDetails_top_time)
    TextView activityEntrustDetailsTopTime;
    /**
     * 表格
     */
    @BindView(R.id.activity_entrustDetails_content_entrustPrice_content)
    TextView activityEntrustDetailsContentEntrustPriceContent;
    @BindView(R.id.activity_entrustDetails_content_entrustAmount_bg)
    LinearLayout activityEntrustDetailsContentEntrustAmountBg;
    @BindView(R.id.activity_entrustDetails_content_entrustAmount_title)
    TextView activityEntrustDetailsContentEntrustAmountTitle;
    @BindView(R.id.activity_entrustDetails_content_entrustAmount_content)
    TextView activityEntrustDetailsContentEntrustAmountContent;
    @BindView(R.id.activity_entrustDetails_content_tradeAmount_content)
    TextView activityEntrustDetailsContentTradeAmountContent;
    @BindView(R.id.activity_entrustDetails_content_tradeSum_content)
    TextView activityEntrustDetailsContentTradeSumContent;
    @BindView(R.id.activity_entrustDetails_content_tradeAve_content)
    TextView activityEntrustDetailsContentTradeAveContent;
    @BindView(R.id.activity_entrustDetails_content_poundage_content)
    TextView activityEntrustDetailsContentPoundageContent;
    /**
     * 列表
     */
    @BindView(R.id.activity_entrustDetails_list)
    RecyclerView activityEntrustDetailsList;
    @BindView(R.id.activity_entrustDetails_nolist)
    TextView activityEntrustDetailsNoList;
    @BindView(R.id.activity_entrustDetails_content_entrustPrice_title)
    TextView activityEntrustDetailsContentEntrustPriceTitle;
    @BindView(R.id.activity_entrustDetails_content_tradeAmount_title)
    TextView activityEntrustDetailsContentTradeAmountTitle;
    @BindView(R.id.activity_entrustDetails_content_tradeSum_bg)
    LinearLayout activityEntrustDetailsContentTradeSumBg;
    @BindView(R.id.activity_entrustDetails_content_tradeSum_title)
    TextView activityEntrustDetailsContentTradeSumTitle;
    @BindView(R.id.activity_entrustDetails_content_tradeAve_title)
    TextView activityEntrustDetailsContentTradeAveTitle;
    @BindView(R.id.activity_entrustDetails_content_poundage_title)
    TextView activityEntrustDetailsContentPoundageTitle;

    private GoodsEntrustDetailsListAdapter adapter;
    private String perCoinName, tradeCoinName;
    private int entrustId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_entrust_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        if (adapter == null) {
            adapter = new GoodsEntrustDetailsListAdapter(mContext);
        }
        /*获取列表页传递过来的委托单号id*/
        entrustId = getIntent().getIntExtra("entrustId", -1);
        getEntrustDetails(entrustId);
    }

    /**
     * 获取详情，初始化界面
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        activityEntrustDetailsTitle.setText("成交明细");
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        /*添加Layout*/
        activityEntrustDetailsList.setLayoutManager(layoutManager);
        activityEntrustDetailsList.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.divider_line), MathUtils.dip2px(mContext, 1)));
        /*添加加载进度条*/
        activityEntrustDetailsList.setAdapter(adapter);
    }

    /**
     * 去服务器获取详情页
     */
    private void getEntrustDetails(final int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getEntrustDetails(Preferences.getAccessToken(), map, new getBeanCallback<EntrustDetails>() {
            @Override
            public void onSuccess(EntrustDetails entrustDetails) {
                if (entrustDetails == null) {
                    entrustDetails = new EntrustDetails();
                }
                LogUtils.w(TAG, entrustDetails.toString());
                initDetailViews(entrustDetails);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                /*EntrustDetails details = DefaultData.getEntrustDetails(id);
                initDetailViews(details);*/
            }
        });
    }

    /**
     * 用数据填界面
     */
    private void initDetailViews(EntrustDetails entrustDetails) {
        if (entrustDetails == null) {
            entrustDetails = new EntrustDetails();
        }

        int state = entrustDetails.getState();
        String stateName="";
        switch (state) {
            default:break;
            case 0:
                stateName = "未成交";
                break;
            case 1:
                stateName = "完全成交";
                break;
            case 2:
                stateName = "已撤销";
                break;
            case 3:
                stateName = "交易失败";
                break;
        }
        activityEntrustDetailsState.setText(stateName);

        perCoinName = getCoinName(entrustDetails.getUnitCoinType());
        tradeCoinName = getCoinName(entrustDetails.getOrderCoinType());
        /*交易类型 0买入 1卖出*/
        int buyOrSale = entrustDetails.getType();
        switch (buyOrSale) {
            default:
                break;
            case 0:
                activityEntrustDetailsTopType.setText("买");
                activityEntrustDetailsTopType.setBackgroundResource(R.mipmap.trade_buy_bg);
                break;
            case 1:
                activityEntrustDetailsTopType.setText("卖");
                activityEntrustDetailsTopType.setBackgroundResource(R.mipmap.trade_sale_bg);
                break;
        }
        /*交易方式 0市阶交易 1限阶交易*/
        int tradeType = entrustDetails.getOrderType();
        switch (tradeType) {
            default:
                break;
            case TRADE_MARKET:
                activityEntrustDetailsTopEntrustType.setText(tradeCoinName);
                activityEntrustDetailsTopEntrustType1.setText("/ " + perCoinName);
                activityEntrustDetailsTopEntrustType2.setText("市价交易");
                break;
            case TRADE_LIMIT:
                activityEntrustDetailsTopEntrustType.setText(tradeCoinName );
                activityEntrustDetailsTopEntrustType1.setText("/ " + perCoinName);
                activityEntrustDetailsTopEntrustType2.setText("限阶交易");
                break;
        }
        /*时间*/
        activityEntrustDetailsTopTime.setText(entrustDetails.getCreateTime());
//        activityEntrustDetailsContentEntrustPriceTitle.setText("委托价("+perCoinName+")");
//        activityEntrustDetailsContentTradeAmountTitle.setText("成交量("+tradeCoinName+")");
//        activityEntrustDetailsContentTradeSumTitle.setText("成交总价("+perCoinName+")");
//        activityEntrustDetailsContentTradeAveTitle.setText("成交均价("+perCoinName+")");
        String feeCoinName = getCoinName(entrustDetails.getFeeCoinType());
//        activityEntrustDetailsContentPoundageTitle.setText("手续费("+feeCoinName+")");

        /*当市价买入时，中间显示为市价委托额，icon为计价币KN*/
        if (tradeType == TRADE_MARKET && buyOrSale == 0) {
//            activityEntrustDetailsContentEntrustAmountTitle.setText("委托额("+perCoinName+")");
            activityEntrustDetailsContentEntrustAmountBg.setVisibility(View.VISIBLE);
            activityEntrustDetailsContentTradeSumBg.setVisibility(View.GONE);
            activityEntrustDetailsContentEntrustAmountContent.setText(entrustDetails.getPrice()+" "+perCoinName);
        } else {
//            activityEntrustDetailsContentEntrustAmountTitle.setText("委托量("+tradeCoinName+")");
//            activityEntrustDetailsContentEntrustAmountContent.setText(entrustDetails.getAmount()+" "+tradeCoinName);
            activityEntrustDetailsContentEntrustAmountBg.setVisibility(View.GONE);
            activityEntrustDetailsContentTradeSumBg.setVisibility(View.VISIBLE);
            activityEntrustDetailsContentTradeSumContent.setText(entrustDetails.getAmount()+" "+tradeCoinName);

        }
        /*委托价、委托量、成交量*/
        activityEntrustDetailsContentEntrustPriceContent.setText(tradeType == TRADE_MARKET ? "市价" : entrustDetails.getPrice()+" "+perCoinName);
        activityEntrustDetailsContentTradeAmountContent.setText(entrustDetails.getDealAmount()+" "+tradeCoinName);
        /*成交总额、成交均价、手续费*/
//        activityEntrustDetailsContentTradeSumContent.setText(entrustDetails.getTotal()+" "+perCoinName);
        activityEntrustDetailsContentTradeAveContent.setText(entrustDetails.getAverage()+" "+perCoinName);
        activityEntrustDetailsContentPoundageContent.setText(entrustDetails.getFee()+" "+feeCoinName);

        List<EntrustDetails.ListBean> list = entrustDetails.getList();
        if (list == null || list.size() < 1){
            activityEntrustDetailsNoList.setVisibility(View.VISIBLE);
        }else {
            activityEntrustDetailsNoList.setVisibility(View.GONE);
            adapter.addAll(list);
        }

    }

    @OnClick({R.id.iv_back,R.id.activity_entrustDetails_top_toTrade})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                GoodsEntrustDetailsActivity.this.finish();
                break;
            case R.id.activity_entrustDetails_top_toTrade:
                EventBus.getDefault().post(new EventGoodsToTrade());
                finish();
                break;
        }
    }
}
