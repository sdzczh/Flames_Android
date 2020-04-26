package app.com.pgy.Activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import app.com.pgy.Adapters.KLineShenduListAdapter;
import app.com.pgy.Models.Beans.ShenduBean;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseWebViewActivity;
import app.com.pgy.Adapters.KLineDealListAdapter;
import app.com.pgy.Adapters.KLineTimeAdapter;
import app.com.pgy.Constants.Constants;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.EventBean.EventKLineSceneDestroy;
import app.com.pgy.Models.Beans.KLineBean;
import app.com.pgy.Models.Beans.KLineTime;
import app.com.pgy.Models.Beans.PushBean.PushData;
import app.com.pgy.Models.Beans.PushBean.RecordsBean;
import app.com.pgy.Models.Beans.PushBean.SocketRequestBean;
import app.com.pgy.R;
import app.com.pgy.Receivers.KLineMessageReceiver;
import app.com.pgy.Services.MyWebSocket;
import app.com.pgy.Utils.JsonUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import static app.com.pgy.Constants.StaticDatas.MARKET_ONECOIN;

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
    @BindView(R.id.activity_kLine_top_price_img)
    ImageView activityKLineTopPriceImg;
    @BindView(R.id.activity_kLine_title_title)
    TextView activityKLineTitleTitle;
    @BindView(R.id.activity_kLine_bottom_dealList)
    RecyclerView recyclerView;
    @BindView(R.id.activity_kLIne_scrollView)
    NestedScrollView scrollView;
    private KLineDealListAdapter dealListAdapter;
    @BindView(R.id.activity_kLine_bottom_descWebView)
    WebView webView;
    private String coinIntroUrl;
    @BindView(R.id.activity_kLine_buyOrSaleFrame)
    LinearLayout activityKLineBuyOrSaleFrame;
    @BindView(R.id.activity_kLine_bottom_tab)
    TabLayout activityKLineBottomTab;

    @BindView(R.id.webView)
    WebView webViewK;
    @BindView(R.id.rv_shendu)
    RecyclerView rvShendu;
    private KLineTimeAdapter adapter;
    private List<KLineTime> kLineTimes;

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
        kLineDatas = new ArrayList<>();
        switchTime();
        initWebview();
    }

    private void initWebview() {

        webViewK.getSettings().setJavaScriptEnabled(true);
//        webViewK.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webViewK.getSettings().setUseWideViewPort(true);
//        webViewK.getSettings().setLoadWithOverviewMode(true);
//        webViewK.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webViewK.getSettings().setTextZoom(100);
//        webViewK.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // TODO Auto-generated method stub
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
////                    super.onReceivedSslError(view, handler, error);
//            }
//        });
        webViewK.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webViewK.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webViewK.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        String info = "<body style=\"margin:0;padding:0\">" +
                "<div class=\"tradingview-widget-container\">\n" +
                "  <div id=\"tradingview_fc1f4\"></div>\n" +
//                "  <div class=\"tradingview-widget-copyright\"><a href=\"https://cn.tradingview.com/symbols/HUOBI-BTCUSDT/\" rel=\"noopener\" target=\"_blank\"><span class=\"blue-text\">BTCUSDT图表</span></a>由TradingView提供</div>\n" +
                "  <script type=\"text/javascript\" src=\"https://s3.tradingview.com/tv.js\"></script>\n" +
                "  <script type=\"text/javascript\">\n" +
                "  new TradingView.widget(\n" +
                "  {\n" +
                "  \"width\": %d,\n" +
                "  \"height\": %d,\n" +
//                "  \"autosize\":true,\n" +
                "  \"symbol\": \"OKEX:%sUSDT\",\n" +
                "  \"interval\": \"60\",\n" +
                "  \"timezone\": \"Asia/Hong_Kong\",\n" +
                "  \"theme\": \"Dark\",\n" +
                "  \"style\": \"1\",\n" +
                "  \"locale\": \"zh_CN\",\n" +
                "  \"toolbar_bg\": \"#f1f3f6\",\n" +
                "  \"enable_publishing\": false,\n" +
                "  \"hide_legend\": true,\n" +
                "  \"save_image\": false,\n" +
                "  \"container_id\": \"tradingview_fc1f4\"\n" +
                "}\n" +
                "  );\n" +
                "  </script>\n" +
                "</div>\n" +
                "</body>";
        String klineCoin = TextUtils.isEmpty(getCoinInfo(tradeCoin).getRelycoin()) ? getCoinName(tradeCoin) : getCoinInfo(tradeCoin).getRelycoin();
        String data = String.format(info, MathUtils.getWidthInDp(this), 320, klineCoin);//
        webViewK.loadData(data, "text/html; charset=utf-8", "utf-8");
//        webViewK.loadUrl("http://47.56.87.149:8888/static/demo.html");
    }

    /**
     * 从服务器获取K线图数据
     */
    private void getKLineDataFromNet() {
        switchScene(new PushData((mType == MARKET_ONECOIN ? "3521" : "3522"), perCoin + "", tradeCoin + "", currentDate.getTime() + ""));
//        switchScene(new PushData("3523", perCoin + "", tradeCoin + "", currentDate.getTime() + ""));
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

    @Override
    protected void initView(Bundle savedInstanceState) {
        String tradeCoinName = getCoinName(tradeCoin);
        String perCoinName = getCoinName(perCoin);
        activityKLineTitleTitle.setText(tradeCoinName + "/" + perCoinName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter.setGetPositionCallback(this);
        adapter.setSelect(currentDate.getTime() - 1);
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
                        switchScene(new PushData((mType == MARKET_ONECOIN ? "3521" : "3522"), perCoin + "", tradeCoin + "", currentDate.getTime() + ""));
                        /*选中交易*/
                        rvShendu.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                        break;
                    case 1:
                        switchScene(new PushData("3523", perCoin + "", tradeCoin + "", currentDate.getTime() + ""));
                        rvShendu.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        webView.setVisibility(View.GONE);


                        break;
                    case 2:
                        /*选中简介*/
                        rvShendu.setVisibility(View.GONE);
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

    @Override
    public void getKLShenDuBean(ShenduBean shenduBean) {

        if (shenduBean != null) {
            List<List<String>> asks = shenduBean.asks;
            rvShendu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });
            KLineShenduListAdapter adapterShendu = new KLineShenduListAdapter();
            adapterShendu.setData(asks, shenduBean.bids);
            rvShendu.setAdapter(adapterShendu);
        }
    }

    /**
     * 设置顶部视图数据
     */
    private void initTopView(KLineBean.MarketBean market) {
        LogUtils.w(TAG, "market:" + market.toString());
        String newPrice = market.getNewPrice();
        activityKLineTopPrice.setText("$" + newPrice);
        activityKLineTopCny.setText("￥" + market.getNewPriceCNY());
        /*获取增跌幅*/
        String chgPrice = market.getChgPrice();
        if (MathUtils.string2Double(chgPrice) >= 0) {
            activityKLineTopPrice.setTextColor(getResources().getColor(R.color.txt_green));
            activityKLineTopPriceImg.setImageResource(R.mipmap.arrow_rise);
            activityKLineTopPercent.setText("+" + chgPrice + "%");
            activityKLineTopPercent.setBackgroundColor(getResources().getColor(R.color.txt_green));
        } else {
            activityKLineTopPrice.setTextColor(getResources().getColor(R.color.txt_red));
            activityKLineTopPriceImg.setImageResource(R.mipmap.arrow_fall);
            activityKLineTopPercent.setText(chgPrice + "%");
            activityKLineTopPercent.setBackgroundColor(getResources().getColor(R.color.txt_red));
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
