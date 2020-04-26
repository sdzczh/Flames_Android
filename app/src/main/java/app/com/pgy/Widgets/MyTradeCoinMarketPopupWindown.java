package app.com.pgy.Widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.List;

import app.com.pgy.Adapters.LeftSecondarySpinnerAdapter;
import app.com.pgy.Adapters.TradeCoinMarketOrderCoinAdapter;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Interfaces.spinnerSecondaryChooseListener;
import app.com.pgy.Models.Beans.TradeCoinMarketBean;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;

/**
 * Create by Android on 2019/10/25 0025
 */
public class MyTradeCoinMarketPopupWindown extends PopupWindow {

    private Context context;
    private View contentView;

    private RecyclerView rvCoinList;
    private LeftSecondarySpinnerAdapter coinAdapter;
    private RecyclerView rvMarketList;
    private TradeCoinMarketOrderCoinAdapter marketAdapter;
    private View bottomView;

    private int selectedPerCoinType = -1;
    private int selectedTradeCoinType = -1;
    private getPositionCallback perChangeListener;
    private spinnerSecondaryChooseListener listener;
    private List<Integer> tradeCoins;

    public MyTradeCoinMarketPopupWindown(Context context,List<Integer> tradeCoins){
        super(context);
        this.context = context;
        if (tradeCoins.size() <= 0) {
            return;
        }
        this.tradeCoins = tradeCoins;
        initView();
        initData();
        setPopConfig();
    }
    public void setListener(spinnerSecondaryChooseListener listener) {
        this.listener = listener;
    }

    public void setPerChangeListener(getPositionCallback perChangeListener) {
        this.perChangeListener = perChangeListener;
    }

    private void initView(){
        contentView = View.inflate(context,R.layout.popuwin_trade_coin_market,null);
        rvCoinList = contentView.findViewById(R.id.rv_coin_list);
        rvMarketList = contentView.findViewById(R.id.rv_coin_market_list);
        bottomView = contentView.findViewById(R.id.bottomView);
        bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(contentView);
    }

    private void initData(){
        rvCoinList.setLayoutManager(new LinearLayoutManager(context));
        coinAdapter = new LeftSecondarySpinnerAdapter(context);
        coinAdapter.setOnItemClickListener(new getPositionCallback() {
            @Override
            public void getPosition(int pos) {
                if (selectedPerCoinType != tradeCoins.get(pos)){
                    selectedPerCoinType = tradeCoins.get(pos);
                    coinAdapter.setSelectCoin(selectedPerCoinType);
                    coinAdapter.notifyDataSetChanged();
                    //切换场景
                    if (perChangeListener != null){
                        perChangeListener.getPosition(selectedPerCoinType);
                    }
                }

            }
        });
        coinAdapter.setSelectCoin(selectedPerCoinType);
        rvCoinList.setAdapter(coinAdapter);
        coinAdapter.addAll(tradeCoins);

        rvMarketList.setLayoutManager(new LinearLayoutManager(context));
        marketAdapter = new TradeCoinMarketOrderCoinAdapter(context);
        marketAdapter.setOnItemClickListener(new getPositionCallback() {
            @Override
            public void getPosition(int pos) {
                selectedTradeCoinType = marketAdapter.getItem(pos).getOrderCoinType();
                if (selectedTradeCoinType != -1){
                    marketAdapter.setSelectcoin(selectedTradeCoinType);
                    marketAdapter.notifyDataSetChanged();
                }
                if (listener != null) {
                    listener.onItemClickListener(selectedPerCoinType, selectedTradeCoinType);
                }
               dismiss();
            }
        });
        rvMarketList.setAdapter(marketAdapter);
    }

    public void updateMarketList(List<TradeCoinMarketBean> list){
        if (list == null || list.size() < 1){
            marketAdapter.clear();
            return;
        }else {
            if (marketAdapter != null){
                if (selectedTradeCoinType != -1){
                    marketAdapter.setSelectcoin(selectedTradeCoinType);
                }
                marketAdapter.clear();
                marketAdapter.addAll(list);
            }
        }
    }

    public void setSelectedPerCoinType(int perCoinType){
        selectedPerCoinType = perCoinType;
        if (coinAdapter!= null){
            coinAdapter.setSelectCoin(selectedPerCoinType);
            coinAdapter.notifyDataSetChanged();
        }
    }

    public int getSelectedPerCoinType() {
        return selectedPerCoinType;
    }

    public void setSelectedTradeCoinType(int selectedTradeCoinType) {
        this.selectedTradeCoinType = selectedTradeCoinType;
        if (marketAdapter != null){
            marketAdapter.setSelectcoin(selectedTradeCoinType);
            marketAdapter.notifyDataSetChanged();
        }
    }

    public int getSelectedTradeCoinType() {
        return selectedTradeCoinType;
    }

    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        //获取自身的长宽高
//        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(null);
        this.setClippingEnabled(false);
        //setWindowFullScreen(true);
        this.setAnimationStyle(R.style.AnimationLeftMove);;
        //消失监听
    }
}
