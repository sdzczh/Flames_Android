package app.com.pgy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.KLineActivity;
import app.com.pgy.Activitys.LoginActivity;
import app.com.pgy.Activitys.MainActivity;
import app.com.pgy.Activitys.TradeGoodsEntrustListActivity;
import app.com.pgy.Adapters.BuyOrSaleListAdapter;
import app.com.pgy.Adapters.ViewPagerAdapter;
import app.com.pgy.Constants.Constants;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Interfaces.spinnerSecondaryChooseListener;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.BuyOrSale;
import app.com.pgy.Models.Beans.EventBean.EventC2cCancelEntrust;
import app.com.pgy.Models.Beans.EventBean.EventGoodsChange;
import app.com.pgy.Models.Beans.EventBean.EventGoodsCoinChange;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.EventBean.EventMarketScene;
import app.com.pgy.Models.Beans.KLineBean;
import app.com.pgy.Models.Beans.PushBean.PushData;
import app.com.pgy.Models.Beans.PushBean.RecordsBean;
import app.com.pgy.Models.Beans.TradeCoinMarketBean;
import app.com.pgy.Models.Beans.TradeMessage;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Receivers.GoodsListReceiver;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.Utils;
import app.com.pgy.Widgets.AmountImageView;
import app.com.pgy.Widgets.MyBottomSpinnerList;
import app.com.pgy.Widgets.MyTradeCoinMarketPopupWindown;
import app.com.pgy.Widgets.MyTradeTypeChoosePopupWindow;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static app.com.pgy.Constants.StaticDatas.BUY;
import static app.com.pgy.Constants.StaticDatas.MARKET_ONECOIN;
import static app.com.pgy.Constants.StaticDatas.SALE;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2018/04/18 0022 on 上午 11:23
 * 描述:交易界面->现货-币币
 *
 * @author 徐庆重
 */
public class TradeGoodsFragment extends BaseFragment implements GoodsListReceiver.onListCallback, RadioGroup.OnCheckedChangeListener {
    private static final int LIMIT = 0;
    private static final int MARKET = 1;
    private static TradeGoodsFragment instance;
    @BindView(R.id.fragment_tradeGoods_c2cName)
    TextView fragmentTradeGoodsC2cName;
    @BindView(R.id.fragment_tradeGoods_group)
    RadioGroup fragmentTradeGoodsGroup;
    @BindView(R.id.fragment_tradeGoods_limitOrMarket)
    TextView fragmentTradeGoodsLimitOrMarket;
    @BindView(R.id.fragment_tradeGoods_limitPrice)
    AmountImageView fragmentTradeGoodsLimitPrice;
    @BindView(R.id.fragment_tradeGoods_priceOfCny)
    TextView fragmentTradeGoodsPriceOfCny;
    @BindView(R.id.fragment_tradeGoods_marketPrice)
    TextView fragmentTradeGoodsMarketPrice;
    @BindView(R.id.fragment_tradeGoods_amount)
    AmountImageView fragmentTradeGoodsAmount;
    @BindView(R.id.fragment_tradeGoods_available)
    TextView fragmentTradeGoodsAvailable;
    @BindView(R.id.fragment_tradeGoods_percentGroup)
    RadioGroup fragmentTradeGoodsPercentGroup;
    @BindView(R.id.fragment_tradeGoods_trade)
    TextView fragmentTradeGoodsTrade;
    @BindView(R.id.fragment_tradeGoods_list_sale)
    RecyclerView fragmentTradeGoodsListSale;
    @BindView(R.id.fragment_tradeGoods_gear)
    TextView fragmentTradeGoodsGear;
    @BindView(R.id.fragment_tradeGoods_currentPrice)
    TextView fragmentTradeGoodsCurrentPrice;
    @BindView(R.id.fragment_tradeGoods_currentPrice_cny)
    TextView fragmentTradeGoodsCurrentPriceCny;
    @BindView(R.id.fragment_tradeGoods_list_buy)
    RecyclerView fragmentTradeGoodsListBuy;
    @BindView(R.id.fragment_tradeGoods_tab)
    XTabLayout fragmentTradeGoodsTab;
    @BindView(R.id.fragment_tradeGoods_viewPager)
    ViewPager fragmentTradeGoodsViewPager;
    @BindView(R.id.fragment_tradeGoods_percentGroup_first)
    RadioButton fragmentTradeGoodsPercentGroupFirst;
    @BindView(R.id.fragment_tradeGoods_percentGroup_second)
    RadioButton fragmentTradeGoodsPercentGroupSecond;
    @BindView(R.id.fragment_tradeGoods_percentGroup_third)
    RadioButton fragmentTradeGoodsPercentGroupThird;
    @BindView(R.id.fragment_tradeGoods_percentGroup_forth)
    RadioButton fragmentTradeGoodsPercentGroupForth;
    @BindView(R.id.fragment_tradeGoods_tradeAmount)
    TextView fragmentTradeGoodsTradeAmount;
    @BindView(R.id.fragment_tradeGoods_tradeAmountFrame)
    LinearLayout fragmentTradeGoodsTradeAmountFrame;
    @BindView(R.id.fragment_tradeGoods_login)
    TextView fragmentTradeGoodsLogin;
    @BindView(R.id.fragment_tradeGoods_appBarLayout)
    AppBarLayout fragmentTradeGoodsAppBarLayout;
    @BindView(R.id.fragment_tradeGoods_klineTime_first)
    TextView fragmentTradeGoodsKlineTimeFirst;
    @BindView(R.id.fragment_tradeGoods_klineTime_second)
    TextView fragmentTradeGoodsKlineTimeSecond;
    @BindView(R.id.fragment_tradeGoods_klineTime_third)
    TextView fragmentTradeGoodsKlineTimeThird;
    @BindView(R.id.titleLine)
    View titleLine;



    //    Unbinder unbinder;
    private List<Fragment> fragments;
    private List<String> fragmentsName;
    private BuyOrSaleListAdapter buyListAdapter, saleListAdapter;
    /**
     * 交易方式Item点击监听
     */
    private spinnerSingleChooseListener tradeItemListener;
    private MyBottomSpinnerList spinnerTrade;
    /**
     * 切换币种监听
     */
//    private MyLeftSecondarySpinnerList spinnerCoin;
    private spinnerSecondaryChooseListener coinItemListener;
    private MyTradeCoinMarketPopupWindown coinMarketPopupWindown;
    private List<Integer> perCoinTypeList;
    private Map<Integer, List<Integer>> tradeCoinTypeMap;
    /**
     * 档位数据列表
     */
    private List<String> tradeList;
    /**
     * 选中的交易类型、现货的计价币交易币
     */
    private int tradePos, goodsPerCoin, goodsTradeCoin;
    /**
     * 当前交易币、计价币名称
     */
    private String currentTradeCoinName, currentPerCoinName;
    private boolean isBuy = true;
    private boolean isGearTen = true;
    /**
     * 用户输入
     * 限价交易：委托价格、委托数量、交易额
     */
    private Double limitPriceEntrustPrice;
    private Double limitPriceEntrustAmount;
    /**
     * orderPriceNum        委托价位数
     * minSubmitPrice       最低提交价格
     */
    private int tradePriceNum, tradeAmountNum;
    private GoodsListReceiver receiver;
    /**
     * 交易币可用币数、计价币可用余额
     */
    private Double tradeCoinAvail = 0.00;
    private Double perCoinAvail = 0.00;
    private Double rateOfCny = 0.00;
    /**
     * 买一价、卖一价
     */
    private String firstBuyPrice, firstSalePrice;
    //防止第一次加载时，接收不到广播现象 K线图跳转到此页
    private int type = 0;
    private boolean isFirstPrice = false;

    public TradeGoodsFragment() {
    }

    public static TradeGoodsFragment newInstance() {
        if (instance == null) {
            instance = new TradeGoodsFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_trade_goods;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*第一次加载*/
        //switchScene(new PushData("350",goodsPerCoin+"",goodsTradeCoin+"",gearLevel+""));
        if (receiver == null) {
            receiver = new GoodsListReceiver();
        }
        /*注册监听*/
        IntentFilter filters = new IntentFilter();
        filters.addAction(Constants.SOCKET_ACTION);
        getLocalBroadcastManager().registerReceiver(receiver, filters);
        receiver.setListCallback(this);
//        test();
    }

    @Override
    public void onDestroy() {
        /*解除挖矿状态监听*/
        if (receiver != null) {
            getLocalBroadcastManager().unregisterReceiver(receiver);
        }
        /*解除顶部币对币监听*/
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        /*初始化现货中计价币、交易币的位置，从本地获取*/
        goodsPerCoin = Preferences.getGoodsPerCoin();
        goodsTradeCoin = Preferences.getGoodsTradeCoin();
        /*获取计价币种列表，交易币种map*/
        perCoinTypeList = getGoodsPerCoinList();
        tradeCoinTypeMap = getGoodsCoinMap();
        if (goodsPerCoin < 0) {
            /*如果本地计价币不存在，则默认设置为列表第一个*/
            goodsPerCoin = (perCoinTypeList != null && perCoinTypeList.size() > 0) ? perCoinTypeList.get(0) : 0;
            Preferences.setGoodsPerCoin(goodsPerCoin);
        }
        if (goodsTradeCoin < 0) {
            List<Integer> tradeCoins = getGoodsCoinMap().get(goodsPerCoin);
            goodsTradeCoin = (tradeCoins != null && tradeCoins.size() > 0) ? tradeCoins.get(0) : 0;
            Preferences.setGoodsTradeCoin(goodsTradeCoin);
        }
        LogUtils.w(TAG, "perCoin:" + goodsPerCoin + ",name:" + getCoinName(goodsPerCoin));
        LogUtils.w(TAG, "tradeCoin:" + goodsTradeCoin + ",name:" + getCoinName(goodsTradeCoin));
        tradeList = getTradeTypeNames();
        /*初始化所有fragment*/
        fragments = getFragments();
        fragmentsName = getFragmentsNames();

        /*初始化买卖Adapter*/
        if (buyListAdapter == null) {
            buyListAdapter = new BuyOrSaleListAdapter(mContext, BUY);
        }
        if (saleListAdapter == null) {
            saleListAdapter = new BuyOrSaleListAdapter(mContext, SALE);
        }
        fragmentTradeGoodsListBuy.setAdapter(buyListAdapter);
        fragmentTradeGoodsListSale.setAdapter(saleListAdapter);
        /*设置滑到最底部*/
        if (saleListAdapter.getCount() > 1) {
            fragmentTradeGoodsListSale.scrollToPosition(saleListAdapter.getCount() - 1);
        }
        buyListAdapter.setOnItemClickListener(new BuyOrSaleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BuyOrSale.ListBean item = buyListAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                clearPercentGroupAndInput();
                fragmentTradeGoodsLimitPrice.setInput(item.getPrice());
            }
        });
        saleListAdapter.setOnItemClickListener(new BuyOrSaleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BuyOrSale.ListBean item = saleListAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                clearPercentGroupAndInput();
                fragmentTradeGoodsLimitPrice.setInput(item.getPrice());
            }
        });
    }

    private List<String> getTradeTypeNames() {
        List<String> trades = new ArrayList<>();
        trades.add("限价");
        trades.add("市价");
        return trades;
    }

    private void test() {
        List<BuyOrSale.ListBean> testData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BuyOrSale.ListBean info = new BuyOrSale.ListBean();
            info.setPrice("1");
            info.setRate("14.29");
            info.setNum(1);
            info.setRemain("1");
            testData.add(info);
        }
        onSaleListCallback(testData);
        onBuyListCallback(testData);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(TradeGoodsLastDealListFragment.newInstance(goodsPerCoin, goodsTradeCoin));
        fragments.add(TradeGoodsCurrentEntrustListFragment.newInstance(goodsPerCoin, goodsTradeCoin));
//        fragments.add(TradeGoodsHistoryListFragment.newInstance(goodsPerCoin, goodsTradeCoin));
        return fragments;
    }

    /**
     * 添加所有fragment的标题
     */
    private List<String> getFragmentsNames() {
        List<String> names = new ArrayList<>();
        names.add("普通委托");
//        names.add("当前委托");
//        names.add("历史记录");
        return names;
    }


    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initViewpagerView();
        if (type != 0) {//如果K线图点击卖出，首次进入页面，改变卖出状态
            isBuy = false;
            fragmentTradeGoodsGroup.check(R.id.fragment_tradeGoods_group_sale);
        }

        final LinearLayoutManager managerBuy = new LinearLayoutManager(mContext);
        CustomLinearLayoutManager managerSale = new CustomLinearLayoutManager(mContext);
        managerSale.setScrollEnabled(false);
        /*添加Layout*/
        fragmentTradeGoodsListBuy.setLayoutManager(managerBuy);
        fragmentTradeGoodsListSale.setLayoutManager(managerSale);
        /*添加输入监听，改变交易额*/
        LimitPriceTradeWatcher watcher = new LimitPriceTradeWatcher();
        fragmentTradeGoodsLimitPrice.addTextChangedListener(watcher);
        fragmentTradeGoodsAmount.addTextChangedListener(watcher);
        /*初始化交易方式、档位、深度位置，默认为列表最后一个*/
        tradePos = LIMIT;
        fragmentTradeGoodsGroup.setOnCheckedChangeListener(this);
        fragmentTradeGoodsPercentGroup.setOnCheckedChangeListener(this);
        /*初始化K线图*/
//        initKLineChart();
        /*设置监听，监听现货币对币的变化*/
        setSpinnerListener();
        switchLoginFrame();
        /*根据当前现货交易币计价币、交易类型、是否、设置左侧界面*/
        switchCoinFrame();
    }

    private LineData getLineData(List<KLineBean.ListBean> kLines) {
        int color = getResources().getColor(R.color.bg_blue);
        if (kLines == null || kLines.size() <= 0) {
            Log.w(TAG, "获取数据为空");
            return null;
        }
        List<String> xValues = new ArrayList<>();
        // x轴显示的数据,时间
        for (KLineBean.ListBean coinline : kLines) {
            xValues.add("");
        }
        // y轴的数据，价格
        List<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < kLines.size(); i++) {
            String price = kLines.get(i).getClosePrice();
            yValues.add(new Entry(Float.valueOf(price), i));
        }
        LineDataSet lineDataSet = new LineDataSet(yValues, "折线图" /*显示在比例图上*/);
        /*设置折线的样式*/
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(color);// 设置折线的颜色
        lineDataSet.setDrawCircles(false);  //不用小圆圈表示
        lineDataSet.setDrawCubic(false);     //允许曲线平滑
        //lineDataSet.setCubicIntensity(0.2f);    //设置曲线平滑度
        lineDataSet.setDrawHighlightIndicators(false);  //关闭指引线
        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);
        //创建LineData数据，x轴数据是list<String>，y轴是list<Entry>
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }

    /**
     * 初始化k线图界面
     */
//    private void initKLineChart() {
//        int color = getResources().getColor(R.color.bg_kline);
//        /*设置图标样式*/
//        fragmentTradeGoodsKline.setBackgroundColor(color);// 设置背景
//        fragmentTradeGoodsKline.setDrawBorders(false);  //是否在折线图上添加边框
//        fragmentTradeGoodsKline.setDescription("");// 数据描述
//        fragmentTradeGoodsKline.setDescriptionColor(color);//设置描述信息的颜色
//        //fragmentTradeGoodsKline.setDescriptionPosition(); //自定义描述信息的位置
//        Log.w(TAG, fragmentTradeGoodsKline.getData() == null ? "数据为空" : "数据不为空");
//        fragmentTradeGoodsKline.setNoDataTextDescription("暂无数据");//设置空表的描述信息
//        //fragmentTradeGoodsKline.setDescriptionTextSize(10); //自定义描述信息字体大小, 最小值6f, 最大值16f
//        fragmentTradeGoodsKline.setDrawGridBackground(false); // 是否显示表格颜色
//        fragmentTradeGoodsKline.setDrawBorders(false);  // 是否绘制边线
//        /*设置图标的交互*/
//        /*1、启用/禁用交互*/
//        fragmentTradeGoodsKline.setTouchEnabled(false); // 设置是否可以触摸
//        fragmentTradeGoodsKline.setDragEnabled(false);// 是否可以拖拽
//        fragmentTradeGoodsKline.setScaleEnabled(false);// 是否可以缩放
//        fragmentTradeGoodsKline.setScaleXEnabled(false);// 启用X轴上的缩放
//        fragmentTradeGoodsKline.setScaleYEnabled(false);//启用Y轴上的缩放
//        fragmentTradeGoodsKline.setPinchZoom(false);//   是否XY同时缩放
//        fragmentTradeGoodsKline.setDoubleTapToZoomEnabled(false); //启用双击缩放
//        /*获取X轴的Axis*/
//        XAxis xAxis = fragmentTradeGoodsKline.getXAxis();
//        xAxis.setTextColor(color);
//        xAxis.setEnabled(false);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//設置 mXAxis 的位置，在底部
//        xAxis.setSpaceBetweenLabels(2); // 每個欄位的間距，default=4
//        xAxis.setDrawGridLines(false);  //不显示垂直方向的表格线
//        xAxis.setDrawAxisLine(false);    //显示xAxis边线
//        /*获取Y轴的Axis，左边和右边*/
//        YAxis mLeftYAxis = fragmentTradeGoodsKline.getAxisLeft();
//        YAxis mRightYAxis = fragmentTradeGoodsKline.getAxisRight();
//        mLeftYAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        mRightYAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        //xAxis.setLabelsToSkip(1);  // 要略過幾個 Label 才會顯示一個 Label
//        int textColor = getResources().getColor(R.color.bg_blue);
//        mLeftYAxis.setTextColor(textColor);
//        mRightYAxis.setTextColor(textColor);
//        /*mLeftYAxis.setSpaceTop(30f);   //Top 為正值所設定多出來的向上空間
//        mRightYAxis.setSpaceTop(30f);
//        mLeftYAxis.setSpaceBottom(30f);    //Bottom 為負值所設定多出來的向下空間
//        mRightYAxis.setSpaceBottom(30f);*/
//        mLeftYAxis.setDrawGridLines(false); //不显示所有的水平表格线
//        mRightYAxis.setDrawGridLines(false);
//        //mLeftYAxis.setDrawAxisLine(false);  //不显示左边的边线
//        mLeftYAxis.setEnabled(false);   //不显示左边的边线和label
//        mRightYAxis.setDrawAxisLine(false);  //显示右边的边线
//        /*mLeftYAxis.setStartAtZero(true);
//        mRightYAxis.setStartAtZero(true);*/
//        //最靠近 Label 的垂直線 AxisLine
//        mLeftYAxis.setDrawAxisLine(false);
//        mLeftYAxis.setShowOnlyMinMax(true);
//        mRightYAxis.setShowOnlyMinMax(true);
//        mLeftYAxis.setLabelCount(1, false);  //設置顯示幾個 Label 數，垂直方向行数
//        mRightYAxis.setLabelCount(1, false);
//        /*设置数据*/
//        Legend mLegend = fragmentTradeGoodsKline.getLegend(); // 设置比例图标示，就是那个一组y的value的
//        mLegend.setEnabled(false);  //异常左下角的Legend
//    }

    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        switchLoginFrame();
    }

    /**
     * 当前委托撤销委托监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event1(EventC2cCancelEntrust cancelEntrust) {
        if (cancelEntrust.isCanceled()) {
            switchLoginFrame();
        }
    }

    private void switchLoginFrame() {
        refreshBottomList();
        if (isLogin()) {
            fragmentTradeGoodsTrade.setVisibility(View.VISIBLE);
            fragmentTradeGoodsLogin.setVisibility(View.GONE);
            refreshWallet();
        } else {
            fragmentTradeGoodsTrade.setVisibility(View.GONE);
            fragmentTradeGoodsLogin.setVisibility(View.VISIBLE);
            tradeCoinAvail = 0.00;
            perCoinAvail = 0.00;
            fragmentTradeGoodsAvailable.setText("可用  0.00  " + currentPerCoinName);
        }
    }

    /**
     * 去服务器获取左下角钱包可用余额
     */
    private void refreshWallet() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderCoin", goodsTradeCoin);
        map.put("unitCoin", goodsPerCoin);
        map.put("levFlag", 0);
        map.put("type", BUY);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getTradeMessage(Preferences.getAccessToken(), map, new getBeanCallback<TradeMessage>() {
            @Override
            public void onSuccess(TradeMessage tradeMessage) {
                initFrame(tradeMessage);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                initFrame(null);
            }
        });
    }

    private void initFrame(TradeMessage tradeMessage) {
        if (tradeMessage == null) {
            tradeMessage = new TradeMessage();
        }
        rateOfCny = MathUtils.string2Double(tradeMessage.getRmbRate());
        /*从服务器获取交易币可用余额、计价币可用余额*/
        String unitAvailBalance = tradeMessage.getUnit();
        perCoinAvail = MathUtils.string2Double(unitAvailBalance);
        String orderAvailBalance = tradeMessage.getOrder();
        tradeCoinAvail = MathUtils.string2Double(orderAvailBalance);
        tradeAmountNum = tradeMessage.getAmountScale();
        tradePriceNum = tradeMessage.getPriceScale();
        fragmentTradeGoodsLimitPrice.setDigits(tradePriceNum);
        /*设置下面输入框的小数位数*/
        setBottomInputNumber();
        if (isBuy) {
            fragmentTradeGoodsAvailable.setText("可用 " + unitAvailBalance + " " + currentPerCoinName);
        } else {
            fragmentTradeGoodsAvailable.setText("可用 " + orderAvailBalance + " " + currentTradeCoinName);
        }
    }

    private void setBottomInputNumber() {
        /*市价买入时，下面输入框为输入交易额，其他时候都为数量*/
        if (isBuy && tradePos == MARKET) {
            fragmentTradeGoodsAmount.setDigits(tradePriceNum);
            fragmentTradeGoodsAmount.setInputHint("交易额(" + currentPerCoinName + ")");
        } else {
            fragmentTradeGoodsAmount.setDigits(tradeAmountNum);
            fragmentTradeGoodsAmount.setInputHint("数量(" + currentTradeCoinName + ")");
        }
    }

    /**
     * 切换买卖
     */
    private void switchBuyOrSale() {
        clearPercentGroupAndInput();
        /*买入时获取可用的计价币*/
        /*卖出时获取可用的交易币*/
        refreshWallet();
        /*设置买一价卖一价*/
        fragmentTradeGoodsLimitPrice.setInput(!isBuy ? firstBuyPrice : firstSalePrice);

        if (isBuy) {
            fragmentTradeGoodsGroup.setBackgroundResource(R.mipmap.trade_buy_sale_bg_select);
            fragmentTradeGoodsTrade.setText("买入 " + currentTradeCoinName);
            fragmentTradeGoodsTrade.setBackgroundResource(R.mipmap.buy_trade_bg_new);
            fragmentTradeGoodsPercentGroupFirst.setBackgroundResource(R.drawable.selector_radio_bg_green_grey);
            fragmentTradeGoodsPercentGroupSecond.setBackgroundResource(R.drawable.selector_radio_bg_green_grey);
            fragmentTradeGoodsPercentGroupThird.setBackgroundResource(R.drawable.selector_radio_bg_green_grey);
            fragmentTradeGoodsPercentGroupForth.setBackgroundResource(R.drawable.selector_radio_bg_green_grey);
            fragmentTradeGoodsLimitOrMarket.setText((tradePos == LIMIT ? "限价" : "市价") + "买入");
        } else {
            fragmentTradeGoodsGroup.setBackgroundResource(R.mipmap.trade_buy_sale_bg);
            fragmentTradeGoodsTrade.setText("卖出 " + currentTradeCoinName);
            fragmentTradeGoodsTrade.setBackgroundResource(R.mipmap.sale_trade_bg_new);
            fragmentTradeGoodsPercentGroupFirst.setBackgroundResource(R.drawable.selector_radio_bg_green_grey);
            fragmentTradeGoodsPercentGroupSecond.setBackgroundResource(R.drawable.selector_radio_bg_red_grey);
            fragmentTradeGoodsPercentGroupThird.setBackgroundResource(R.drawable.selector_radio_bg_red_grey);
            fragmentTradeGoodsPercentGroupForth.setBackgroundResource(R.drawable.selector_radio_bg_green_grey);
            fragmentTradeGoodsLimitOrMarket.setText((tradePos == LIMIT ? "限价" : "市价") + "卖出");
        }

    }

    private void switchLimitOrMarketPrice() {
        clearPercentGroupAndInput();
        setBottomInputNumber();
        /*根据当前的交易类型切换左上角界面*/
        switch (tradePos) {
            /*限价*/
            default:
            case LIMIT:
                fragmentTradeGoodsLimitPrice.setVisibility(View.VISIBLE);
                fragmentTradeGoodsMarketPrice.setVisibility(View.GONE);
                /*设置限价交易时左上角界面的文字*/
                fragmentTradeGoodsPriceOfCny.setVisibility(View.VISIBLE);
                fragmentTradeGoodsTradeAmountFrame.setVisibility(View.VISIBLE);

                break;
            /*市价*/
            case MARKET:
                fragmentTradeGoodsMarketPrice.setVisibility(View.VISIBLE);
                fragmentTradeGoodsLimitPrice.setVisibility(View.GONE);
                /*设置市价交易时左上角界面的文字*/
                fragmentTradeGoodsPriceOfCny.setVisibility(View.INVISIBLE);
                fragmentTradeGoodsTradeAmountFrame.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 根据现货币对币的值，设置左侧界面文字
     */
    private void switchCoinFrame() {
        /*根据C2C交易币和计价币设置不同的文字*/
        currentPerCoinName = getCoinName(goodsPerCoin);
        currentTradeCoinName = getCoinName(goodsTradeCoin);
        fragmentTradeGoodsC2cName.setText(currentTradeCoinName + "/" + currentPerCoinName);
        fragmentTradeGoodsLimitPrice.setInputHint("价格(" + currentPerCoinName + ")");
        /*切换币种更换场景*/
        switchGearFrame();
        switchBuyOrSale();
        switchLimitOrMarketPrice();
        refreshBottomList();
        clearPercentGroupAndInput();
    }

    private void refreshBottomList() {
        /*切换币种发送广播*/
        EventBus.getDefault().post(new EventGoodsCoinChange(goodsPerCoin, goodsTradeCoin));
    }

    private void initViewpagerView() {
        /*初始化ViewPager的懒加载，添加几页缓存*/
        fragmentTradeGoodsViewPager.setOffscreenPageLimit(fragments.size());
        setupViewPager(fragmentTradeGoodsViewPager);
        /*设置ViewPager标题*/
        for (String name : fragmentsName) {
            fragmentTradeGoodsTab.addTab(fragmentTradeGoodsTab.newTab().setText(name));
        }
        fragmentTradeGoodsTab.setupWithViewPager(fragmentTradeGoodsViewPager);
    }

    /**
     * 将构造出来的不同类别的货币添加到ViewPager的适配器中
     */
    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //往适配器中两个参数，第一个是fragment，第二个是该类在viewpager的标题
        for (int i = 0; i < fragments.size(); i++) {
            adapter.addFragment(fragments.get(i), fragmentsName.get(i));
        }
        mViewPager.setAdapter(adapter);
    }

    @OnClick({R.id.fragment_tradeGoods_c2cName, R.id.fragment_tradeGoods_limitOrMarket,
            R.id.fragment_tradeGoods_trade, R.id.fragment_tradeGoods_kline_frame,
            R.id.fragment_tradeGoods_login, R.id.fragment_tradeGoods_gear,
            R.id.fragment_tradeGoods_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*切换币种*/
            case R.id.fragment_tradeGoods_c2cName:
                if (coinMarketPopupWindown == null) {
                    coinMarketPopupWindown = new MyTradeCoinMarketPopupWindown(getActivity(), perCoinTypeList);
                    coinMarketPopupWindown.setSelectedPerCoinType(goodsPerCoin);
                    coinMarketPopupWindown.setSelectedTradeCoinType(goodsTradeCoin);
                    coinMarketPopupWindown.setListener(coinItemListener);
                    coinMarketPopupWindown.setPerChangeListener(new getPositionCallback() {
                        @Override
                        public void getPosition(int pos) {
                            //切换场景

                        }
                    });
                    coinMarketPopupWindown.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            //切换场景
                            switchCoinFrame();
                        }
                    });
                }
                if (!coinMarketPopupWindown.isShowing()) {
//                    coinMarketPopupWindown.showAtLocation(titleLine, Gravity.NO_GRAVITY,0,MathUtils.dip2px(mContext,75));
                    switchScene(new PushData("3510", goodsPerCoin + "", "0", "0"));
                    coinMarketPopupWindown.showAsDropDown(titleLine, -50, -2);
                }

                break;
            /*选择限价还是市价*/
            case R.id.fragment_tradeGoods_limitOrMarket:
//                spinnerTrade = new MyBottomSpinnerList(mContext, tradeList);
//                spinnerTrade.setMySpinnerListener(tradeItemListener);
//                spinnerTrade.showUp(fragmentTradeGoodsLimitOrMarket);
                MyTradeTypeChoosePopupWindow popupWindow = new MyTradeTypeChoosePopupWindow(mContext, isBuy, tradePos);
                popupWindow.setMySpinnerListener(tradeItemListener);
                popupWindow.showAsDropDown(fragmentTradeGoodsLimitOrMarket, DensityUtil.dp2px(18), 0);
                break;
            /*登录*/
            case R.id.fragment_tradeGoods_login:
                Utils.IntentUtils(mContext, LoginActivity.class);
                break;
            /*买入或者卖出*/
            case R.id.fragment_tradeGoods_trade:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                switch (tradePos) {
                    /*限价交易*/
                    default:
                    case 0:
                        /*判断输入*/
                        if (limitPriceEntrustPrice == null || limitPriceEntrustPrice <= 0) {
                            showToast("请输入委托价格");
                            return;
                        }
                        if (limitPriceEntrustAmount == null || limitPriceEntrustAmount <= 0) {
                            showToast("请输入委托数量");
                            return;
                        }
                        break;
                    /*市价交易*/
                    case 1:
                        /*判断输入*/
                        if (limitPriceEntrustAmount == null || limitPriceEntrustAmount <= 0) {
                            showToast(isBuy ? "请输入交易金额" : "请输入交易数量");
                            return;
                        }
                        break;
                }
                String localTradePwd = Preferences.getLocalTradePwd();
                if (TextUtils.isEmpty(localTradePwd)) {
                    /*弹出输入交易密码对话框*/
                    showPayDialog(new getStringCallback() {
                        @Override
                        public void getString(String string) {
                            trade(string);
                        }
                    });
                } else {
                    trade(localTradePwd);
                }
                break;
            /*k线图*/
            case R.id.fragment_tradeGoods_kline_frame:
                Intent intent2KLine = new Intent(mContext, KLineActivity.class);
                intent2KLine.putExtra("type", MARKET_ONECOIN);
                intent2KLine.putExtra("tradeCoin", goodsTradeCoin);
                intent2KLine.putExtra("perCoin", goodsPerCoin);
                getActivity().startActivityForResult(intent2KLine, MainActivity.REQUEST_KLINE);
                break;
            /*选择档位*/
            case R.id.fragment_tradeGoods_gear:
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                isGearTen = !isGearTen;
                switchGearFrame();
                break;
            /*刷新列表*/
            case R.id.fragment_tradeGoods_refresh:
//                refreshBottomList();
                Intent intent2entrust = new Intent(mContext, TradeGoodsEntrustListActivity.class);
                startActivity(intent2entrust);
                break;
            default:
                break;
        }
    }

    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 500;  // 快速点击间隔

    private void trade(String pwd) {
        switch (tradePos) {
            /*限价交易*/
            default:
            case 0:
                limitPriceTrade(pwd);
                break;
            case 1:
                marketPriceTrade(pwd);
                break;
        }
    }

    /**
     * 限价交易
     */
    private void limitPriceTrade(final String pwd) {
        /*获取用户输入委托价格、委托数量*/
        showLoading(fragmentTradeGoodsTrade);
        Map<String, Object> map = new HashMap<>();
        map.put("price", limitPriceEntrustPrice);
        map.put("amount", limitPriceEntrustAmount);
        map.put("orderCoin", goodsTradeCoin);
        map.put("unitCoin", goodsPerCoin);
        map.put("levFlag", 0);
        map.put("password", pwd);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);

        NetWorks.limitPriceTrade(Preferences.getAccessToken(), isBuy, map, new getBeanCallback() {
            @Override
            public void onSuccess(Object info) {
                showToast(isBuy ? "限价买入成功" : "限价卖出成功");
                LogUtils.w(TAG, isBuy ? "限价买入成功" : "限价卖出成功");
                refreshBottomList();
                Preferences.setLocalTradePwd(pwd);
                /*买入成功，清空输入框，刷新钱包*/
                clearPercentGroupAndInput();
                refreshWallet();
                hideLoading();

            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**
     * 市价交易
     */
    private void marketPriceTrade(final String pwd) {
        /*获取用户输入委托价格、委托数量*/
        showLoading(fragmentTradeGoodsTrade);
        Map<String, Object> map = new HashMap<>();
        if (isBuy) {
            map.put("total", limitPriceEntrustAmount);
        } else {
            map.put("amount", limitPriceEntrustAmount);
        }
        map.put("levFlag", 0);
        map.put("password", pwd);
        map.put("orderCoin", goodsTradeCoin);
        map.put("unitCoin", goodsPerCoin);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);

        NetWorks.marketPriceTrade(Preferences.getAccessToken(), isBuy, map, new getBeanCallback() {
            @Override
            public void onSuccess(Object info) {
                showToast(isBuy ? "市价买入成功" : "市价卖出成功");
                LogUtils.w(TAG, isBuy ? "市价买入成功" : "市价卖出成功");
                refreshBottomList();
                Preferences.setLocalTradePwd(pwd);
                /*买入成功，清空输入框，刷新钱包*/
                clearPercentGroupAndInput();
                refreshWallet();
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }


    private void switchGearFrame() {
        if (isGearTen) {
            fragmentTradeGoodsGear.setText("10");
        } else {
            fragmentTradeGoodsGear.setText("50");
        }
        /*切换档位更换场景*/
        getListDataFromNet();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            /*买入*/
            case R.id.fragment_tradeGoods_group_buy:
                isBuy = true;
                switchBuyOrSale();
                break;
            /*卖出*/
            case R.id.fragment_tradeGoods_group_sale:
                isBuy = false;
                switchBuyOrSale();
                break;
            /*25%*/
            case R.id.fragment_tradeGoods_percentGroup_first:
                fragmentTradeGoodsAmount.setInput(getPercentPriceOrAmount(0.25));
                break;
            /*50%*/
            case R.id.fragment_tradeGoods_percentGroup_second:
                fragmentTradeGoodsAmount.setInput(getPercentPriceOrAmount(0.5));
                break;
            /*75%*/
            case R.id.fragment_tradeGoods_percentGroup_third:
                fragmentTradeGoodsAmount.setInput(getPercentPriceOrAmount(0.75));
                break;
            /*100%*/
            case R.id.fragment_tradeGoods_percentGroup_forth:
                fragmentTradeGoodsAmount.setInput(getPercentPriceOrAmount(1));
                break;
            default:
                break;
        }
    }

    /**
     * 获取当前百分比价格或数量
     */
    private String getPercentPriceOrAmount(double percent) {
        /*限价买入，已知可用计价币，求交易币数量，perCoinAvail/unitPrice*percent;
        限价卖出，已知可用交易币，求交易币数量，tradeCoinAvail*percent;
        市价买入，已知可用计价币，求交易币数量，perCoinAvail*percent;
        市价卖出，已知可用交易币，求交易币数量，tradeCoinAvail*percent;*/
        double currentAmount = fragmentTradeGoodsLimitPrice.getCurrentAmount();
        LogUtils.w(TAG, "currentAmount:" + currentAmount);
        double unitPrice = (tradePos == LIMIT) ? currentAmount : 1;
        double currentPercent;
        if (isBuy) {
            currentPercent = (unitPrice <= 0 ? 0.00 : (perCoinAvail * percent / unitPrice));
        } else {
            currentPercent = tradeCoinAvail * percent;
        }
        return MathUtils.formatDoubleNumber(currentPercent);
    }

    /**
     * ViewPager中fragment的声明周期，切换显示不显示，刷新界面和数据接收
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isViewCreated) {
            return;
        }
        LogUtils.w("circle", "goodsBuy----setUserVisibleHint:" + isVisibleToUser);
        /*切换界面时*/
        if (isVisibleToUser) {
            LogUtils.w("fragment", "刷新买入界面");
            LogUtils.w("switch", "goodsBuyVisible");
            getListDataFromNet();
        } else {
            hideSoftKeyboard(TradeGoodsFragment.this.getParentFragment().getActivity());
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 去服务器获取列表数据
     * 参数有四个：
     * tradeCoin 交易币
     * perCoin 计价币
     * gearLevel 档位
     * deepPos 深度
     */
    private void getListDataFromNet() {
        LogUtils.w("switch", "getUserVisibleHint:" + getUserVisibleHint() + ",isViewCreated:" + isViewCreated);
        if (!getUserVisibleHint() || !isViewCreated) {
            LogUtils.w("switch", "goodsSale界面不显示，不予切换场景");
            return;
        }
        String gearLevel = isGearTen ? "10" : "50";
        boolean isNotSameScene = switchScene(new PushData("350", goodsPerCoin + "", goodsTradeCoin + "", gearLevel));
        if (isNotSameScene) {
            /*初始化长连接场景，清空列表、设置默认价格、清空K线图*/
            saleListAdapter.clear();
            buyListAdapter.clear();
            showCurrentPrice(0.00);
//            fragmentTradeGoodsKline.clear();
            LogUtils.w("switch", "clear and get goodsSaleGetListData");
            isFirstPrice = true;
        }
    }

    /*买入列表监听*/
    @Override
    public void onBuyListCallback(List<BuyOrSale.ListBean> buyList) {
        buyListAdapter.clear();
        if (buyList != null && buyList.size() > 0) {
            buyListAdapter.addAll(buyList);
            /*获取买一价*/
            BuyOrSale.ListBean item = buyListAdapter.getItem(0);
            firstBuyPrice = (item == null ? "" : item.getPrice());
        } else {
            firstBuyPrice = "";
        }
        if (!isBuy && isFirstPrice) {
            fragmentTradeGoodsLimitPrice.setInput(firstBuyPrice);
            isFirstPrice = false;
        }
    }

    /*卖出列表监听*/
    @Override
    public void onSaleListCallback(List<BuyOrSale.ListBean> saleList) {
        saleListAdapter.clear();
        if (saleList != null && saleList.size() > 0) {
            saleListAdapter.addAll(saleList);
            /*设置滑到最底部*/
            int lastIndex = saleListAdapter.getCount() - 1;
            fragmentTradeGoodsListSale.scrollToPosition(lastIndex);
            BuyOrSale.ListBean item = saleListAdapter.getItem(lastIndex);
            firstSalePrice = (item == null ? "" : item.getPrice());
        } else {
            firstSalePrice = "";
        }
        if (isBuy && isFirstPrice) {
            fragmentTradeGoodsLimitPrice.setInput(firstSalePrice);
            isFirstPrice = false;
        }
    }

    /**
     * 当前最新成交价格监听
     */
    @Override
    public void onPriceCallback(Double price) {
        showCurrentPrice(price);
    }

    /**
     * 显示当前价格
     */
    private void showCurrentPrice(Double price) {
        String showPrice = MathUtils.formatDoubleNumber(price);
        fragmentTradeGoodsCurrentPrice.setText(showPrice);
        fragmentTradeGoodsCurrentPriceCny.setText("≈" + MathUtils.formatdoubleNumber(price * rateOfCny) + "CNY");
    }

    @Override
    public void onLastDealListCallback(List<RecordsBean> lastDealList) {

    }

    /**
     * 根据数据更新k线图
     */
    @Override
    public void onKLineListCallback(List<KLineBean.ListBean> kLineList) {
        if (kLineList == null || kLineList.size() <= 0) {
            return;
        }
//        LineData lineData = getLineData(kLineList);
//        fragmentTradeGoodsKline.clear();
//        fragmentTradeGoodsKline.setData(lineData);
//        fragmentTradeGoodsKline.notifyDataSetChanged();
//        int size = kLineList.size();
//        if (size <= 3){
//            return;
//        }
//        KLineBean.ListBean firstBean = kLineList.get(0);
//        KLineBean.ListBean lastBean = kLineList.get(size - 1);
//        KLineBean.ListBean middleBean = kLineList.get((size / 2 == 0) ? (size / 2) : (size - 1) / 2);
//        fragmentTradeGoodsKlineTimeFirst.setText(firstBean!=null?TimeUtils.dateToString(firstBean.getTimestamp(),"HH:mm"):"");
//        fragmentTradeGoodsKlineTimeSecond.setText(middleBean!=null?TimeUtils.dateToString(middleBean.getTimestamp(),"HH:mm"):"");
//        fragmentTradeGoodsKlineTimeThird.setText(lastBean!=null?TimeUtils.dateToString(lastBean.getTimestamp(),"HH:mm"):"");
    }

    @Override
    public void onTradeCoinMarketCallback(List<TradeCoinMarketBean> marketBeanList) {
        if (coinMarketPopupWindown != null) {
            coinMarketPopupWindown.updateMarketList(marketBeanList);
        }
    }

    /**
     * 交易方式的列表监听
     */
    private void setSpinnerListener() {
        tradeItemListener = new spinnerSingleChooseListener() {
            @Override
            public void onItemClickListener(int position) {
                //点击Item时的操作,存储选中的位置、更改文字、重新获取列表数据
                if (tradePos == position) {
                    return;
                }
                String selectTrade = tradeList.get(position);
                if (!TextUtils.isEmpty(selectTrade)) {
                    tradePos = position;
                    fragmentTradeGoodsLimitOrMarket.setText(selectTrade + (isBuy ? "买入" : "卖出"));
                    switchLimitOrMarketPrice();
                }
            }
        };
        coinItemListener = new spinnerSecondaryChooseListener() {
            @Override
            public void onItemClickListener(int perCoin, int tradeCoin) {
                if (perCoin == goodsPerCoin && tradeCoin == goodsTradeCoin) {
                    LogUtils.w("coin", "不做操作");
                    return;
                }
                goodsPerCoin = perCoin;
                goodsTradeCoin = tradeCoin;
                Preferences.setGoodsPerCoin(perCoin);
                Preferences.setGoodsTradeCoin(tradeCoin);
                switchCoinFrame();
            }
        };
    }

    /**
     * 限价交易时交易的输入监听
     */
    private class LimitPriceTradeWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*获取输入的委托价格和数量*/
            limitPriceEntrustPrice = fragmentTradeGoodsLimitPrice.getCurrentAmount();
            limitPriceEntrustAmount = fragmentTradeGoodsAmount.getCurrentAmount();
            LogUtils.w(TAG, "价格:" + limitPriceEntrustPrice + ",数量:" + limitPriceEntrustAmount);
            /*约合成人民币价格*/
            fragmentTradeGoodsPriceOfCny.setText("≈" + MathUtils.formatdoubleNumber(limitPriceEntrustPrice * rateOfCny) + "CNY");
            /*得到交易额*/
            Double tradeAmount = limitPriceEntrustPrice * limitPriceEntrustAmount;
            /*限价交易*/
            if (tradeAmount > 0) {
                String limitPriceTradeAmountStr = MathUtils.formatDoubleNumber(tradeAmount);
                LogUtils.w(TAG, "限价交易额：" + limitPriceTradeAmountStr);
                fragmentTradeGoodsTradeAmount.setText(limitPriceTradeAmountStr + currentPerCoinName);
            } else {
                fragmentTradeGoodsTradeAmount.setText("0.00" + currentPerCoinName);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    /**
     * 清除百分比选择
     */
    private void clearPercentGroupAndInput() {
        fragmentTradeGoodsPercentGroupFirst.setChecked(false);
        fragmentTradeGoodsPercentGroupSecond.setChecked(false);
        fragmentTradeGoodsPercentGroupThird.setChecked(false);
        fragmentTradeGoodsPercentGroupForth.setChecked(false);
        fragmentTradeGoodsAmount.setInput("");
    }


    public void setTypes(int t) {
        type = t;
    }

    /**
     * 根据现货币对币的值，设置左侧界面文字
     */
    private void switchCoinFrameByEvent() {
        /*根据C2C交易币和计价币设置不同的文字*/
        currentPerCoinName = getCoinName(goodsPerCoin);
        currentTradeCoinName = getCoinName(goodsTradeCoin);
        fragmentTradeGoodsC2cName.setText(currentTradeCoinName + "/" + currentPerCoinName);
        fragmentTradeGoodsLimitPrice.setInputHint("价格(" + currentPerCoinName + ")");
        switchGearFrame();
        switchLimitOrMarketPrice();
        refreshBottomList();
        clearPercentGroupAndInput();
    }

    //广播改变现实页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goodsFragmentChangeEvent(EventGoodsChange event) {
        LogUtils.e(TAG, "type=接收到改变广播");
        if (event != null) {
            if (event.getType() == 0) {
                if (!isBuy) {
                    fragmentTradeGoodsGroup.check(R.id.fragment_tradeGoods_group_buy);
                }
            } else {
                if (isBuy) {
                    fragmentTradeGoodsGroup.check(R.id.fragment_tradeGoods_group_sale);
                }
            }
            if (Preferences.getGoodsPerCoin() != goodsPerCoin || Preferences.getGoodsTradeCoin() != goodsTradeCoin) {
                goodsPerCoin = Preferences.getGoodsPerCoin();
                goodsTradeCoin = Preferences.getGoodsTradeCoin();
                switchCoinFrameByEvent();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SwichSceneEvent(EventMarketScene event) {
        if (event == null) {
            return;
        }
        LogUtils.w("switch", "TradeGoodsFragment----event:" + getUserVisibleHint());
        if (getUserVisibleHint() && event.getType() == EventMarketScene.TYPE_TRADE_GOODS) {
            getListDataFromNet();
        }
    }
}
