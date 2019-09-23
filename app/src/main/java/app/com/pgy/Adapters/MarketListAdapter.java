package app.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.PushMarketBean;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.ToolsUtils;

/**
 * Created by YX on 2018/7/13.
 */

public class MarketListAdapter extends RecyclerArrayAdapter<PushMarketBean.ListBean> {
    private static final String TAG = "MarketListAdapter";
    private getPositionCallback callback;
    public MarketListAdapter(Context context) {
        super(context);
    }
    public void setCallback(getPositionCallback callback) {
        this.callback = callback;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyMarketViewHolder(parent);
    }

    public void setData(List<PushMarketBean.ListBean> marketList) {
        List<PushMarketBean.ListBean> allData = getAllData();
        if (allData.size() <= 0){
            LogUtils.w(TAG,"无数据，添加所有");
            addAll(marketList);
            return;
        }
        for (PushMarketBean.ListBean bean:marketList) {
            List<Integer> coinIds = getCoinIds(getAllData());
            if (coinIds.contains(bean.getOrderCoinType())){
                int index = coinIds.indexOf(bean.getOrderCoinType());
                LogUtils.w(TAG,"存在列表修改："+index+"..."+bean.toString());
                this.notifyItemChanged(index,bean);
                remove(index);
                add(bean);
            } else{
                LogUtils.w(TAG,"不存在列表添加："+bean.toString());
                add(bean);
            }
        }
    }

    private List<Integer> getCoinIds(List<PushMarketBean.ListBean> allData){
        /*获取当前列表中所有的币种id*/
        List<Integer> coinids = new ArrayList<>();
        for (PushMarketBean.ListBean bean : allData) {
            coinids.add(bean.getOrderCoinType());
        }
        return coinids;
    }

    class MyMarketViewHolder extends BaseViewHolder<PushMarketBean.ListBean> implements View.OnClickListener {
        private ImageView iv_coin;
        private TextView tv_zhName;
        private TextView tv_enName;
        private TextView tv_percoin;
        private TextView tv_volum;
        private TextView tv_price;
        private TextView tv_priceCny;
        private TextView tv_rise;
        private LinearLayout ll_bg;

        MyMarketViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_market_list_view);
            ll_bg = $(R.id.ll_item_marker_list_bg);
            iv_coin = $(R.id.iv_item_market_list_icon);
            tv_zhName = $(R.id.tv_item_market_list_zhName);
            tv_enName = $(R.id.tv_item_market_list_enName);
            tv_percoin = $(R.id.tv_item_market_list_percoin);
            tv_volum = $(R.id.tv_item_market_list_volum);
            tv_price = $(R.id.tv_item_market_list_price);
            tv_priceCny = $(R.id.tv_item_market_list_priceCny);
            tv_rise = $(R.id.tv_item_market_list_rise);
            ll_bg.setOnClickListener(this);
        }

        @Override
        public void setData(PushMarketBean.ListBean marketBean) {
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(marketBean.getOrderCoinType());
            ImageLoaderUtils.displayCircle(getContext(),iv_coin,coinInfo.getImgurl());
            tv_zhName.setText(marketBean.getOrderCoinCnName());
            tv_enName.setText(marketBean.getOrderCoinName());
            tv_percoin.setText("/"+marketBean.getUnitCoinName());
            tv_volum.setText(marketBean.getSumAmount());
            tv_priceCny.setText("¥"+marketBean.getNewPriceCNY());
            tv_price.setText(marketBean.getNewPrice());
            String chgPrice = marketBean.getChgPrice();
            double rateOrFall = MathUtils.string2Double(chgPrice);
            if (rateOrFall > 0){
                /*涨*/
                tv_rise.setBackgroundResource(R.drawable.bg_corners_52cca3);
                tv_rise.setText("+"+chgPrice+"%");
            }else if (rateOrFall < 0){
                /*跌*/
                tv_rise.setBackgroundResource(R.drawable.bg_corners_fff766);
                tv_rise.setText(chgPrice+"%");
            }else{
                /*平*/
                tv_rise.setBackgroundResource(R.drawable.bg_corners_c4c7cc);
                tv_rise.setText("+0.00%");
            }
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.getPosition(getPosition());
            }
        }
    }
}
