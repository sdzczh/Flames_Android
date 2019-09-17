package huoli.com.pgy.Activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseWebViewActivity;
import huoli.com.pgy.Adapters.KLineDealListAdapter;
import huoli.com.pgy.Adapters.KLineTimeAdapter;
import huoli.com.pgy.Constants.Constants;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.Models.Beans.EventBean.EventKLineSceneDestroy;
import huoli.com.pgy.Models.Beans.KLineBean;
import huoli.com.pgy.Models.Beans.KLineTime;
import huoli.com.pgy.Models.Beans.PushBean.PushData;
import huoli.com.pgy.Models.Beans.PushBean.RecordsBean;
import huoli.com.pgy.Models.Beans.PushBean.SocketRequestBean;
import huoli.com.pgy.R;
import huoli.com.pgy.Receivers.KLineMessageReceiver;
import huoli.com.pgy.Services.MyWebSocket;
import huoli.com.pgy.Utils.JsonUtils;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.VolFormatter;
import huoli.com.pgy.Widgets.mychart.CoupleChartGestureListener;
import huoli.com.pgy.Widgets.mychart.DataParse;
import huoli.com.pgy.Widgets.mychart.MyBottomMarkerView;
import huoli.com.pgy.Widgets.mychart.MyCombinedChart;
import huoli.com.pgy.Widgets.mychart.MyHMarkerView;
import huoli.com.pgy.Widgets.mychart.MyLeftMarkerView;

import static huoli.com.pgy.Constants.StaticDatas.MARKET_ONECOIN;

/**
 * k线图，分时图
 *
 * @author xuqingzhong
 */

public class KLineActivity extends BaseWebViewActivity implements getPositionCallback, KLineMessageReceiver.KLineMessageListener {
    private static final String TAG = "KLineActivity";
    /**
     * 顶部数据
     */
    @BindView(R.id.activity_kLine_top_price)
    TextView activityKLineTopPrice;
    @BindView(R.id.activity_kLine_top_cny)
    TextView activityKLineTopCny;
    @BindView(R.id.activity_kLine_top_percent)
    TextView activityKLineTopPercent;
    @BindView(R.id.activity_kLine_top_max)
    TextView activityKLineTopMax;
    @BindView(R.id.activity_kLine_top_min)
    TextView activityKLineTopMin;
    @BindView(R.id.activity_kLine_top_amount)
    TextView activityKLineTopAmount;
    @BindView(R.id.layout_klineChart_market_open)
    TextView layoutKlineChartMarketOpen;
    @BindView(R.id.layout_klineChart_market_max)
    TextView layoutKlineChartMarketMax;
    @BindView(R.id.layout_klineChart_market_close)
    TextView layoutKlineChartMarketClose;
    @BindView(R.id.layout_klineChart_market_min)
    TextView layoutKlineChartMarketMin;
    /**
     * 顶部成交价蜡烛图、K线图
     */
    @BindView(R.id.activity_kLine_chart_price)
    MyCombinedChart activityKLineChartPrice;
    /**
     * 底部柱状图、K线图
     */
    @BindView(R.id.activity_kLine_chart_amount)
    MyCombinedChart activityKLineChartAmount;
    @BindView(R.id.activity_kLine_chart_times)
    RecyclerView activityKLineChartTimes;
    @BindView(R.id.activity_kLine_top_price_img)
    ImageView activityKLineTopPriceImg;
    @BindView(R.id.activity_kLine_title_title)
    TextView activityKLineTitleTitle;
    /*@BindView(R.id.layout_klineChart_priceMa_ma5)
    TextView layoutKlineChartPriceMaMa5;
    @BindView(R.id.layout_klineChart_priceMa_ma10)
    TextView layoutKlineChartPriceMaMa10;
    @BindView(R.id.layout_klineChart_priceMa_ma30)
    TextView layoutKlineChartPriceMaMa30;*/
    @BindView(R.id.layout_klineChart_volMa_vol)
    TextView layoutKlineChartVolMaVol;
    @BindView(R.id.layout_klineChart_volMa_ma5)
    TextView layoutKlineChartVolMaMa5;
    @BindView(R.id.layout_klineChart_volMa_ma10)
    TextView layoutKlineChartVolMaMa10;
    @BindView(R.id.activity_kLine_bottom_dealList)
    RecyclerView recyclerView;
    @BindView(R.id.activity_kLIne_scrollView)
    ScrollView scrollView;
    private KLineDealListAdapter dealListAdapter;
    @BindView(R.id.activity_kLine_bottom_descWebView)
    WebView webView;
    private String coinIntroUrl;
    @BindView(R.id.activity_kLine_buyOrSaleFrame)
    LinearLayout activityKLineBuyOrSaleFrame;
    @BindView(R.id.activity_kLine_bottom_tab)
    TabLayout activityKLineBottomTab;
    private KLineTimeAdapter adapter;
    private List<KLineTime> kLineTimes;
    /**
     * X轴标签的类
     */
    protected XAxis xAxisKline, xAxisVolume;
    /**
     * Y轴左侧的线
     */
    protected YAxis axisLeftKline, axisLeftVolume;
    /**
     * Y轴右侧的线
     */
    protected YAxis axisRightKline, axisRightVolume;
    private Handler handlerAdd = new Handler();
    private Runnable runnable;
    /**
     * 解析数据
     */
    private DataParse mData;
    private KLineMessageReceiver receiver;
    /**
     * K线图数据
     */
    private List<KLineBean.ListBean> kLineDatas;
    /**
     * X轴数据
     */
    private List<String> xVals;
    /**
     * 顶部交易价蜡烛图数据
     */
    private List<CandleEntry> candleEntries;
    /**
     * 底部交易量柱状图
     */
    private List<BarEntry> barEntries;
    /**
     * 当前的类型，计价币交易币
     */
    private int mType, tradeCoin, perCoin;
    private static final int SECOND = 100;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    private static final int WEEK = 7 * DAY;
    private KLineTime currentDate;
    /*private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            activityKLineChartPrice.setAutoScaleMinMaxEnabled(true);
            activityKLineChartAmount.setAutoScaleMinMaxEnabled(true);
            activityKLineChartPrice.notifyDataSetChanged();
            activityKLineChartAmount.notifyDataSetChanged();
            activityKLineChartPrice.invalidate();
            activityKLineChartAmount.invalidate();
        }
    };*/

    @Override
    public int getContentViewId() {
        return R.layout.activity_kline;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.w(TAG, "onResume:" + currentDate.getDelaySeconds());
        if (receiver == null) {
            receiver = new KLineMessageReceiver();
        }
        /*注册监听*/
        IntentFilter filters = new IntentFilter();
        filters.addAction(Constants.SOCKET_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(receiver, filters);
        receiver.setListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        switchScene(null);
        EventBus.getDefault().post(new EventKLineSceneDestroy(true));
        /*解除挖矿状态监听*/
        if (receiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handlerAdd.removeCallbacks(runnable);
        LogUtils.w(TAG, "onPause");
    }

    @Override
    protected void initData() {
        /*初始化最底部的时间选择列表*/
        kLineTimes = new ArrayList<>();
        kLineTimes.add(new KLineTime(1, "分时", MINUTE));
        kLineTimes.add(new KLineTime(2, "5分", 5 * MINUTE));
        kLineTimes.add(new KLineTime(3, "30分", 30 * MINUTE));
        kLineTimes.add(new KLineTime(4, "60分", HOUR));
        kLineTimes.add(new KLineTime(5, "日线", DAY));
        if (adapter == null) {
            adapter = new KLineTimeAdapter(mContext, kLineTimes);
        }
        if (dealListAdapter == null) {
            dealListAdapter = new KLineDealListAdapter(mContext);
        }
        coinIntroUrl = getConfiguration().getCoinIntroUrl();
        currentDate = kLineTimes.get(2);
        /*获取参数*/
        mType = getIntent().getIntExtra("type", MARKET_ONECOIN);
        activityKLineBuyOrSaleFrame.setVisibility(mType == MARKET_ONECOIN ? View.VISIBLE : View.GONE);
        tradeCoin = getIntent().getIntExtra("tradeCoin", 0);
        perCoin = getIntent().getIntExtra("perCoin", 0);
        /*获取K线图数据*/
        mData = new DataParse();
        kLineDatas = new ArrayList<>();
        switchTime();
    }

    /**
     * 从服务器获取K线图数据
     */
    private void getKLineDataFromNet() {
        switchScene(new PushData((mType == MARKET_ONECOIN ? "3521" : "3522"), perCoin + "", tradeCoin + "", currentDate.getTime() + ""));
        showLoading(null);
        kLineDatas.clear();
        dealListAdapter.clear();
        /*Map<String, Object> map = new HashMap<>();
        map.put("tradeCoin", tradeCoin);
        map.put("perCoin", perCoin);
        map.put("time", currentDate.getTime());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getKLineData(map, new getBeanCallback<KLineBean>() {
            @Override

            public void onSuccess(KLineBean kLineBean) {
                hideLoading();
                List<KLineBean.ListBean> list = kLineBean.getList();
                if (list == null || list.size() <= 0) {
                    kLineDatas = mData.getKLineDatas();
                    LogUtils.w(TAG, "获取为空，添加假数据" + TimeUtils.timeStampInt());
                }
                setData2Chart(kLineDatas);
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                kLineDatas = mData.getKLineDatas();
                LogUtils.w(TAG, "获取失败，添加假数据" + TimeUtils.timeStampInt());
                setData2Chart(kLineDatas);
                hideLoading();
            }
        });*/
    }

    /**
     * 将数据设置到表中
     */
    private void setData2Chart() {
         /*获取x轴数据*/
        xVals = mData.getXVals(kLineDatas);
        /*获取蜡烛图数据*/
        candleEntries = mData.getCandleEntries(kLineDatas);
        /*获取柱状图数据*/
        barEntries = mData.getBarEntries(kLineDatas);
        /*初始化表格和数据*/
        setKLineByChart(activityKLineChartPrice);
        setVolumeByChart(activityKLineChartAmount);
        /*初始化公共数据*/
        initCharData();
        /*将视图移动到最后一条数据*/
        int lastData = kLineDatas.size() - 1 > 0 ? kLineDatas.size() - 1 : 0;
        activityKLineChartPrice.moveViewToX(lastData);
        activityKLineChartAmount.moveViewToX(lastData);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String tradeCoinName = getCoinName(tradeCoin);
        String perCoinName = getCoinName(perCoin);
        activityKLineTitleTitle.setText((mType == MARKET_ONECOIN ? "COIN" : "主流") + tradeCoinName + "/" + perCoinName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        /*添加Layout*/
        activityKLineChartTimes.setLayoutManager(layoutManager);
        /*添加加载进度条*/
        activityKLineChartTimes.setAdapter(adapter);
        adapter.setGetPositionCallback(this);
        adapter.setSelect(currentDate.getTime() - 1);
        initChartKline();
        initChartVolume();
        /*添加滑动监听*/
        setChartListener();
        //添加头部
        dealListAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return LayoutInflater.from(mContext).inflate(R.layout.layout_kline_deal_head, parent, false);
            }

            @Override
            public void onBindView(View headerView) {
                TextView dealListHeaderPrice = headerView.findViewById(R.id.item_kLineDealHead_price);
                TextView dealListHeaderAmount = headerView.findViewById(R.id.item_kLineDealHead_amount);
                dealListHeaderPrice.setText("价格(" + getCoinName(perCoin) + ")");
                dealListHeaderAmount.setText("数量(" + getCoinName(tradeCoin) + ")");
            }
        });

        recyclerView.addItemDecoration(new DividerDecoration(R.color.bg_dark_line, MathUtils.dip2px(mContext, 1)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dealListAdapter);
        activityKLineBottomTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabSelected:" + tab.getText());
                switch (tab.getPosition()) {
                    default:
                    case 0:
                        /*选中交易*/
                        recyclerView.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                        break;
                    case 1:
                        /*选中简介*/
                        webView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setWebViewAndUrl(coinIntroUrl + tradeCoin, webView);
    }

    /**
     * 切换时间
     */
    @Override
    public void getPosition(int position) {
        if (currentDate.getTime() == kLineTimes.get(position).getTime()) {
            LogUtils.w(TAG, "相同时间，不切换");
            return;
        }
        currentDate = kLineTimes.get(position);
        switchTime();
    }

    /**
     * 切换倒计时
     */
    private void switchTime() {
        getKLineDataFromNet();
        /*handlerAdd.removeCallbacks(runnable);
        handlerAdd.postDelayed(runnable, currentDate.getDelaySeconds());
        LogUtils.w(TAG, "切换成:" + currentDate.getDelaySeconds());*/
    }

    /**
     * 初始化上面的chart公共属性
     */
    private void initChartKline() {
        activityKLineChartPrice.setScaleEnabled(true);//启用图表缩放事件
        activityKLineChartPrice.setDrawBorders(false);//是否绘制边线
        //activityKLineChartPrice.setBorderWidth(1);//边线宽度，单位dp
        activityKLineChartPrice.setDragEnabled(true);//启用图表拖拽事件
        activityKLineChartPrice.setScaleYEnabled(false);//启用Y轴上的缩放
        //activityKLineChartPrice.setBorderColor(getResources().getColor(R.color.border_color));//边线颜色
        activityKLineChartPrice.setDescription("");//右下角对图表的描述信息
        activityKLineChartPrice.setMinOffset(0f);
        activityKLineChartPrice.setExtraOffsets(5f, 5f, 5f, 5f);

        Legend lineChartLegend = activityKLineChartPrice.getLegend();
        lineChartLegend.setEnabled(false);//是否绘制 Legend 图例
        lineChartLegend.setForm(Legend.LegendForm.CIRCLE);

        //bar x y轴
        xAxisKline = activityKLineChartPrice.getXAxis();
        //xAxisKline.setDrawLabels(false); //是否显示X坐标轴上的刻度，默认是true
        xAxisKline.setDrawGridLines(false);//是否显示X坐标轴上的刻度竖线，默认是true
        xAxisKline.setDrawAxisLine(true); //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        xAxisKline.enableGridDashedLine(10f, 0f, 0f);//虚线表示X轴上的刻度竖线(float lineLength, float spaceLength, float phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标
        xAxisKline.setTextColor(getResources().getColor(R.color.txt_kline));//设置字的颜色
        xAxisKline.setPosition(XAxis.XAxisPosition.BOTTOM);//设置值显示在什么位置
        xAxisKline.setAvoidFirstLastClipping(true);//设置首尾的值是否自动调整，避免被遮挡

        axisLeftKline = activityKLineChartPrice.getAxisLeft();
        axisLeftKline.setDrawLabels(false);
        axisLeftKline.setDrawGridLines(true);//圖表的背景水平線 GridLine
        axisLeftKline.setGridColor(getResources().getColor(R.color.minute_grayLine));//所有水平线的顏色
        axisLeftKline.setDrawAxisLine(false);
        axisLeftKline.setDrawZeroLine(false);
        axisLeftKline.setLabelCount(5, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布

        axisRightKline = activityKLineChartPrice.getAxisRight();
        axisRightKline.setDrawLabels(true);
        axisRightKline.enableGridDashedLine(10f, 0f, 0f);
        axisRightKline.setTextColor(getResources().getColor(R.color.txt_kline));
        axisRightKline.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRightKline.setDrawGridLines(true);//圖表的背景水平線 GridLine
        axisRightKline.setGridColor(getResources().getColor(R.color.minute_grayLine));//所有水平线的顏色
        axisRightKline.setDrawAxisLine(false);
        axisRightKline.setLabelCount(5, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布

       /* float padd = 100;
        axisLeftKline.setSpaceTop(padd);//距离顶部留白
        axisLeftKline.setSpaceBottom(padd);
        axisRightKline.setSpaceTop(padd);
        axisRightKline.setSpaceBottom(padd);*/

       /*axisLeftKline.setStartAtZero(true);
       axisRightKline.setStartAtZero(true);*/
        activityKLineChartPrice.setDragDecelerationEnabled(true);
        activityKLineChartPrice.setDragDecelerationFrictionCoef(0.2f);

        //activityKLineChartPrice.animateXY(2000, 2000);
    }

    /**
     * 初始化下面的chart公共属性
     */
    private void initChartVolume() {
        activityKLineChartAmount.setDrawBorders(false);  //边框是否显示
        //activityKLineChartAmount.setBorderWidth(1);//边框的宽度，float类型，dp单位
        //activityKLineChartAmount.setBorderColor(getResources().getColor(R.color.txt_kline));//边框颜色
        activityKLineChartAmount.setDescription(""); //图表默认右下方的描述，参数是String对象
        activityKLineChartAmount.setDragEnabled(true);// 是否可以拖拽
        activityKLineChartAmount.setScaleYEnabled(false); //是否可以缩放 仅y轴
        activityKLineChartAmount.setMinOffset(0f);
        activityKLineChartAmount.setExtraOffsets(5f, 5f, 5f, 5f);

        Legend combinedchartLegend = activityKLineChartAmount.getLegend(); // 设置比例图标示，就是那个一组y的value的
        combinedchartLegend.setEnabled(false);//是否绘制比例图

        //bar x y轴
        xAxisVolume = activityKLineChartAmount.getXAxis();
        //xAxisVolume.setEnabled(false);
        xAxisVolume.setDrawLabels(false); //是否显示X坐标轴上的刻度，默认是true
        xAxisVolume.setDrawGridLines(false);//是否显示X坐标轴上的刻度竖线，默认是true
//        xAxisVolume.setDrawAxisLine(false); //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
//        xAxisVolume.enableGridDashedLine(10f, 10f, 0f);//虚线表示X轴上的刻度竖线(float lineLength, float spaceLength, float phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标
//        xAxisVolume.setTextColor(getResources().getColor(R.color.text_color_common));//设置字的颜色
//        xAxisVolume.setPosition(XAxis.XAxisPosition.BOTTOM);//设置值显示在什么位置
//        xAxisVolume.setAvoidFirstLastClipping(true);//设置首尾的值是否自动调整，避免被遮挡

        axisLeftVolume = activityKLineChartAmount.getAxisLeft();
        axisLeftVolume.setAxisMinValue(0);//设置Y轴坐标最小为多少
//        axisLeftVolume.setShowOnlyMinMax(true);//设置Y轴坐标最小为多少
        axisLeftVolume.setDrawGridLines(false);
        axisLeftVolume.setDrawAxisLine(false);
//        axisLeftVolume.setShowOnlyMinMax(true);
        axisLeftVolume.setDrawLabels(false);
        //axisLeftVolume.setSpaceTop(0);//距离顶部留白
//        axisLeftVolume.setSpaceBottom(0);//距离顶部留白

        axisRightVolume = activityKLineChartAmount.getAxisRight();
        axisRightVolume.setDrawLabels(true);
        axisRightVolume.enableGridDashedLine(10f, 0f, 0f);
        axisRightVolume.setTextColor(getResources().getColor(R.color.txt_kline));
        axisRightVolume.setDrawGridLines(false);
        axisRightVolume.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRightVolume.setLabelCount(1, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布

        axisRightVolume.setDrawAxisLine(false);

        /*axisLeftVolume.setStartAtZero(true);
        axisRightVolume.setStartAtZero(true);*/
        activityKLineChartAmount.setDragDecelerationEnabled(true);
        activityKLineChartAmount.setDragDecelerationFrictionCoef(0.2f);

        //activityKLineChartAmount.animateXY(2000, 2000);
    }

    private void setChartListener() {
        // 将顶部成交价的滑动事件传递给交易量控件
        activityKLineChartPrice.setOnChartGestureListener(new CoupleChartGestureListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtils.w(TAG, "event1:" + event.getAction());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        }, activityKLineChartPrice, new Chart[]{activityKLineChartAmount}));
        // 将中间成交量的滑动事件传递给K线控件
        activityKLineChartAmount.setOnChartGestureListener(new CoupleChartGestureListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtils.w(TAG, "event2:" + event.getAction());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        }, activityKLineChartAmount, new Chart[]{activityKLineChartPrice}));
        activityKLineChartPrice.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Highlight highlight = new Highlight(h.getXIndex(), h.getValue(), h.getDataIndex(), h.getDataSetIndex());

                float touchY = h.getTouchY() - activityKLineChartPrice.getHeight();
                Highlight h1 = activityKLineChartAmount.getHighlightByTouchPoint(h.getXIndex(), touchY);
                highlight.setTouchY(touchY);
                if (null == h1) {
                    highlight.setTouchYValue(0);
                } else {
                    highlight.setTouchYValue(h1.getTouchYValue());
                }
                activityKLineChartAmount.highlightValues(new Highlight[]{highlight});

                /*Highlight highlight2 = new Highlight(h.getXIndex(), h.getValue(), h.getDataIndex(), h.getDataSetIndex());

                float touchY2 = h.getTouchY() - activityKLineChartPrice.getHeight() - activityKLineChartAmount.getHeight();
                Highlight h2 = mChartCharts.getHighlightByTouchPoint(h.getXIndex(), touchY2);
                highlight2.setTouchY(touchY2);
                if (null == h2) {
                    highlight2.setTouchYValue(0);
                } else {
                    highlight2.setTouchYValue(h2.getTouchYValue());
                }
                mChartCharts.highlightValues(new Highlight[]{highlight2});*/

                updateText(e.getXIndex());
            }

            @Override
            public void onNothingSelected() {
                activityKLineChartAmount.highlightValue(null);
            }
        });

        activityKLineChartAmount.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Highlight highlight = new Highlight(h.getXIndex(), h.getValue(), h.getDataIndex(), h.getDataSetIndex());

                float touchY = h.getTouchY() + activityKLineChartPrice.getHeight();
                Highlight h1 = activityKLineChartPrice.getHighlightByTouchPoint(h.getXIndex(), touchY);
                highlight.setTouchY(touchY);
                if (null == h1) {
                    highlight.setTouchYValue(0);
                } else {
                    highlight.setTouchYValue(h1.getTouchYValue());
                }
                activityKLineChartPrice.highlightValues(new Highlight[]{highlight});

                /*Highlight highlight2 = new Highlight(h.getXIndex(), h.getValue(), h.getDataIndex(), h.getDataSetIndex());

                float touchY2 = h.getTouchY() - activityKLineChartAmount.getHeight();
                Highlight h2 = mChartCharts.getHighlightByTouchPoint(h.getXIndex(), touchY2);
                highlight2.setTouchY(touchY2);
                if (null == h2) {
                    highlight2.setTouchYValue(0);
                } else {
                    highlight2.setTouchYValue(h2.getTouchYValue());
                }
                mChartCharts.highlightValues(new Highlight[]{highlight2});*/

                updateText(e.getXIndex());
            }

            @Override
            public void onNothingSelected() {
                LogUtils.w(TAG, "onNothingSelected");
                activityKLineChartPrice.highlightValue(null);
            }
        });
    }

    /**
     * 初始化公共数据
     */
    private void initCharData() {
        /*初始化markerView*/
        setMarkerViewBottom(kLineDatas, activityKLineChartPrice);
        setMarkerView(kLineDatas, activityKLineChartAmount);
        setOffset();
    }

    /**
     * 设置底部markerView
     */
    private void setMarkerViewBottom(List<KLineBean.ListBean> mData, MyCombinedChart combinedChart) {
        int c2cPriceScale = getCoinInfo(tradeCoin).getC2cPriceScale();
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(mContext, R.layout.mymarkerview,c2cPriceScale);
        MyHMarkerView hMarkerView = new MyHMarkerView(mContext, R.layout.mymarkerview_line);
        MyBottomMarkerView bottomMarkerView = new MyBottomMarkerView(mContext, R.layout.mymarkerview);
        combinedChart.setMarker(leftMarkerView, bottomMarkerView, hMarkerView, mData);
    }

    /**
     * 设置左边markerView
     */
    private void setMarkerView(List<KLineBean.ListBean> mData, MyCombinedChart combinedChart) {
        int c2cPriceScale = getCoinInfo(tradeCoin).getC2cPriceScale();
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(mContext, R.layout.mymarkerview,c2cPriceScale);
        MyHMarkerView hMarkerView = new MyHMarkerView(mContext, R.layout.mymarkerview_line);
        combinedChart.setMarker(leftMarkerView, hMarkerView, mData);
    }

    /**
     * 设置量表对齐
     */
    private void setOffset() {
        float lineLeft = activityKLineChartPrice.getViewPortHandler().offsetLeft();
        float kbLeft = activityKLineChartAmount.getViewPortHandler().offsetLeft();
        float lineRight = activityKLineChartPrice.getViewPortHandler().offsetRight();
        float kbRight = activityKLineChartAmount.getViewPortHandler().offsetRight();
        float kbBottom = activityKLineChartAmount.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (kbLeft < lineLeft) {
           /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barChart.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft;
        } else {
            offsetLeft = Utils.convertPixelsToDp(kbLeft - lineLeft);
            activityKLineChartPrice.setExtraLeftOffset(offsetLeft);
            activityKLineChartAmount.setExtraLeftOffset(offsetLeft);
            //mChartCharts.setExtraLeftOffset(offsetLeft);
            transLeft = kbLeft;
        }
  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (kbRight < lineRight) {
          /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(kbRight);
            activityKLineChartPrice.setExtraRightOffset(offsetRight);
            transRight = kbRight;
        }
        activityKLineChartAmount.setViewPortOffsets(transLeft, 15, transRight, kbBottom);
    }

    /**
     * 给顶部成交价图设置蜡烛图、K线图
     */
    private void setKLineByChart(MyCombinedChart combinedChart) {
        CandleDataSet set = new CandleDataSet(candleEntries, "");
        set.setDrawHorizontalHighlightIndicator(false);
        set.setHighlightEnabled(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowWidth(1f);
        set.setValueTextSize(10f);
        set.setDecreasingColor(getResources().getColor(R.color.decreasing_color));//设置开盘价高于收盘价的颜色
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(getResources().getColor(R.color.increasing_color));//设置开盘价地狱收盘价的颜色
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(getResources().getColor(R.color.decreasing_color));//设置开盘价等于收盘价的颜色
        set.setShadowColorSameAsCandle(true);
        set.setHighlightLineWidth(1f);
        set.setHighLightColor(getResources().getColor(R.color.marker_line_bg));
        set.setDrawValues(true);
        set.setValueTextColor(getResources().getColor(R.color.marker_text_bg));
        CandleData candleData = new CandleData(xVals, set);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        /******此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题******************************/
        sets.add(setMaLine(5, mData.getMaDataL(kLineDatas, 5)));
        sets.add(setMaLine(10, mData.getMaDataL(kLineDatas, 10)));
        //sets.add(setMaLine(20, mData.getMaDataL(kLineDatas, 20)));
        sets.add(setMaLine(30, mData.getMaDataL(kLineDatas, 30)));
        LineData lineData = new LineData(xVals, sets);
        CombinedData combinedData = new CombinedData(xVals);
        /*给顶部成交价图添加k线图*/
        combinedData.setData(lineData);
        /*给顶部成交价图添加蜡烛图*/
        combinedData.setData(candleData);
        /*设置数据到图表*/
        combinedChart.setData(combinedData);
        setHandler(combinedChart);
    }

    /**
     * 给中间成交量图设置柱状图、K线图
     */
    private void setVolumeByChart(MyCombinedChart combinedChart) {
        /*某只股票成交量显示为1万手，就是交易了100万股，1手是100股，这是表示以买卖双方意愿达成的，即：买方买进了1万手，同时卖方卖出了1万手。在计算时成交量是按手计算的。*/
        String unit = MathUtils.getVolUnit(mData.getVolmax(kLineDatas));
        String wan = getString(R.string.wan_unit);
        String yi = getString(R.string.yi_unit);
        int u = 1;
        if (wan.equals(unit)) {
            u = 4;
        } else if (yi.equals(unit)) {
            u = 8;
        }
        //combinedChart.getAxisLeft().setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        combinedChart.getAxisLeft().setValueFormatter(new VolFormatter(u));
        //combinedChart.getAxisLeft().setStartAtZero(true);
        Log.e("@@@", unit + "da");

        BarDataSet set = new BarDataSet(barEntries, "成交量");
        set.setBarSpacePercent(20); //bar空隙
        set.setHighlightEnabled(true);
        set.setHighLightAlpha(255);
        set.setHighLightColor(getResources().getColor(R.color.marker_line_bg));
        set.setDrawValues(false);

        List<Integer> list = new ArrayList<>();
        list.add(getResources().getColor(R.color.increasing_color));
        list.add(getResources().getColor(R.color.decreasing_color));
        set.setColors(list);
        BarData barData = new BarData(xVals, set);
        ArrayList<ILineDataSet> sets = new ArrayList<>();

        /******此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题******************************/
        sets.add(setMaLine(5, mData.getMaDataV(kLineDatas, 5)));
        sets.add(setMaLine(10, mData.getMaDataV(kLineDatas, 10)));
        sets.add(setMaLine(20, mData.getMaDataV(kLineDatas, 20)));
        sets.add(setMaLine(30, mData.getMaDataV(kLineDatas, 30)));

        LineData lineData = new LineData(xVals, sets);

        CombinedData combinedData = new CombinedData(xVals);
        /*设置条形图*/
        combinedData.setData(barData);
        /*设置K线图*/
        combinedData.setData(lineData);
        combinedChart.setData(combinedData);

        setHandler(combinedChart);
    }

    private void setHandler(MyCombinedChart combinedChart) {
        /*获取图标的各种状态*/
        final ViewPortHandler viewPortHandlerBar = combinedChart.getViewPortHandler();
        viewPortHandlerBar.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
        final float xscale = 3;
        touchmatrix.postScale(xscale, 1f);
    }


    private float culcMaxscale(float count) {
        float max = 1;
        max = count / 127 * 5;
        return max;
    }

    @NonNull
    private LineDataSet setMaLine(int ma, List<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
            lineDataSetMa.setHighLightColor(getResources().getColor(R.color.marker_line_bg));
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma == 5) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma5));
        } else if (ma == 10) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma10));
        } else if (ma == 20) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma20));
        } else {
            lineDataSetMa.setColor(getResources().getColor(R.color.ma30));
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);

        lineDataSetMa.setHighlightEnabled(false);
        return lineDataSetMa;
    }

    private void updateText(int index) {
        if (index >= 0 && index < kLineDatas.size()) {
            KLineBean.ListBean klData = kLineDatas.get(index);
            LogUtils.w(TAG, "klData:" + klData.toString());
            layoutKlineChartMarketOpen.setText(klData.getOpenPrice());
            layoutKlineChartMarketClose.setText(klData.getClosePrice());
            layoutKlineChartMarketMax.setText(klData.getMaxPrice());
            layoutKlineChartMarketMin.setText(klData.getMinPrice());

            layoutKlineChartVolMaVol.setText(MathUtils.getVolUnitText(2,klData.getVol()));
        }

        int newIndex = index;
        List<Entry> ma5Data = mData.getMaDataL(kLineDatas, 5);
        if (ma5Data != null && ma5Data.size() > 0) {
            if (newIndex >= 0 && newIndex < ma5Data.size()) {
                float m5Val = ma5Data.get(newIndex).getVal();
                layoutKlineChartVolMaMa5.setText(MathUtils.getVolUnitText(2,m5Val));
            }
        }

        List<Entry> ma10Data = mData.getMaDataL(kLineDatas, 10);
        if (ma10Data != null && ma10Data.size() > 0) {
            if (newIndex >= 0 && newIndex < ma10Data.size()) {
                float m10Val = ma10Data.get(newIndex).getVal();
                layoutKlineChartVolMaMa10.setText(MathUtils.getVolUnitText(2,m10Val));
            }
        }
        /*if (null != mData.getMa20DataL() && mData.getMa20DataL().size() > 0) {
            if (newIndex >= 0 && newIndex < mData.getMa20DataL().size()) {
                viewKlineTvMa20.setText(MathUtils.getDecimalFormatVol(mData.getMa20DataL().get(newIndex).getVal()));
            }
        }
        if (null != mData.getMa30DataL() && mData.getMa30DataL().size() > 0) {
            if (newIndex >= 0 && newIndex < mData.getMa30DataL().size()) {
                viewKlineTvMa30.setText(MathUtils.getDecimalFormatVol(mData.getMa30DataL().get(newIndex).getVal()));
            }
        }*/
    }

    private MyWebSocket myWebSocket;

    /**
     * 发送长连接消息
     */
    protected void sendSocketMessage(SocketRequestBean message) {
        String msg = JsonUtils.serialize(message);
        LogUtils.print("KLineActivity,sendSocketMessage向服务器发送:" + msg);
        if (myWebSocket == null) {
            myWebSocket = MyWebSocket.getMyWebSocket();
        }
        if (myWebSocket != null && myWebSocket.isConnect()) {
            myWebSocket.sendRequestMessage(message);
        } else {
            LogUtils.w("HeartBeat", "KLineActivity未连接");
            myWebSocket.handlerDrops("KLineActivity未连接", false, true);
        }
    }

    /**
     * 切换长连接场景
     */
    protected void switchScene(PushData toData) {
        PushData currentPushData = Preferences.getCurrentPushData();
        if (toData != null && currentPushData != null && toData.equals(currentPushData)) {
            LogUtils.w("switch", "进入场景和当前场景相同不切换");
            return;
        }
        if (currentPushData != null) {
            LogUtils.w("switch", "离开" + currentPushData.toString());
            SocketRequestBean outDigFrame = new SocketRequestBean();
            outDigFrame.setCmsg_code(currentPushData.getScene());
            outDigFrame.setAction("leave");
            outDigFrame.setData(currentPushData);
            sendSocketMessage(outDigFrame);
            /*离开之后，将本地设置为空*/
            Preferences.setCurrentPushData(null);
            //LogUtils.w("switch","本地场景："+ (Preferences.getCurrentPushData() == null?"为空":"不为空"));
        }

        if (toData != null) {
            LogUtils.w("switch", "进入" + toData.toString());
            SocketRequestBean inFrame = new SocketRequestBean();
            inFrame.setCmsg_code(toData.getScene());
            inFrame.setAction("join");
            inFrame.setData(toData);
            sendSocketMessage(inFrame);
            /*进入场景后，设置本地*/
            Preferences.setCurrentPushData(toData);
        }
    }

    @Override
    public void getKLineBean(KLineBean kLineBean) {
        LogUtils.w(TAG, "从服务器获取数据并设置" + kLineBean.toString());
        hideLoading();
        List newkLineDatas = kLineBean.getKline();
        if (newkLineDatas != null && newkLineDatas.size() > 0) {
            LogUtils.w(TAG, "列表长度:" + kLineDatas.size());
            //kLineDatas.clear();
            kLineDatas.addAll(newkLineDatas);
            setData2Chart();
        }
        KLineBean.MarketBean market = kLineBean.getMarket();
        if (market != null) {
            LogUtils.w(TAG, market.toString());
            initTopView(market);
        }
        /*底部成交列表*/
        LogUtils.w(TAG, "beforeSize:" + dealListAdapter.getCount());
        List<RecordsBean> orderRecords = kLineBean.getRecords();
        if (orderRecords != null && orderRecords.size() > 0) {
            LogUtils.w(TAG, "add:" + orderRecords.size());
            //dealListAdapter.addList(orderRecords);
            dealListAdapter.clear();
            dealListAdapter.addAll(orderRecords);
            //recyclerView.setAdapter(dealListAdapter);
        }
        LogUtils.w(TAG, "afterSize:" + dealListAdapter.getCount());
    }

    /**
     * 设置顶部视图数据
     */
    private void initTopView(KLineBean.MarketBean market) {
        LogUtils.w(TAG, "market:" + market.toString());
        String newPrice = market.getNewPrice();
        activityKLineTopPrice.setText(newPrice);
        activityKLineTopCny.setText("≈" + market.getNewPriceCNY() + "CNY");
        /*获取增跌幅*/
        String chgPrice = market.getChgPrice();
        if (MathUtils.string2Double(chgPrice) >= 0) {
            activityKLineTopPrice.setTextColor(getResources().getColor(R.color.txt_green));
            activityKLineTopPriceImg.setImageResource(R.mipmap.arrow_rise);
            activityKLineTopPercent.setText("+" + chgPrice + "%");
            activityKLineTopPercent.setTextColor(getResources().getColor(R.color.txt_green));
        } else {
            activityKLineTopPrice.setTextColor(getResources().getColor(R.color.txt_red));
            activityKLineTopPriceImg.setImageResource(R.mipmap.arrow_fall);
            activityKLineTopPercent.setText(chgPrice + "%");
            activityKLineTopPercent.setTextColor(getResources().getColor(R.color.txt_red));
        }
        activityKLineTopMax.setText(market.getHigh());
        activityKLineTopMin.setText(market.getLow());
        activityKLineTopAmount.setText(market.getSumAmount());
    }

    @OnClick({R.id.activity_kLine_title_back, R.id.activity_kLine_title_change, R.id.activity_kLine_chart_buy, R.id.activity_kLine_chart_sale})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            /*返回*/
            case R.id.activity_kLine_title_back:
                KLineActivity.this.finish();
                break;
                /*切换横屏*/
            case R.id.activity_kLine_title_change:
                /*Intent intent2Horizontal = new Intent(mContext,KLineHorizontalActivity.class);
                intent2Horizontal.putExtra("tradeCoin",tradeCoin);
                intent2Horizontal.putExtra("perCoin",perCoin);
                intent2Horizontal.putExtra("time",currentDate);
                intent2Horizontal.putExtra("kline",)*/
                break;
            /*跳转买入*/
            case R.id.activity_kLine_chart_buy:
                intent = new Intent();
                intent.putExtra("type", 0);
                //防止GoodsFragment 还未创建，接收不到广播，先改变币种信息
                Preferences.setGoodsPerCoin(perCoin);
                Preferences.setGoodsTradeCoin(tradeCoin);
                break;
            /*跳转卖出*/
            case R.id.activity_kLine_chart_sale:
                intent = new Intent();
                intent.putExtra("type", 1);
                //防止GoodsFragment 还未创建，接收不到广播
                Preferences.setGoodsPerCoin(perCoin);
                Preferences.setGoodsTradeCoin(tradeCoin);
                break;
            default:
                break;
        }
        if (intent != null) {
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
