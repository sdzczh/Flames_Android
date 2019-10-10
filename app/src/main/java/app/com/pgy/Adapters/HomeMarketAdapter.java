package app.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.HomeMarketBean;
import app.com.pgy.R;
import app.com.pgy.Utils.ToolsUtils;

/**
 * Create by Android on 2019/10/10 0010
 */
public class HomeMarketAdapter extends RecyclerArrayAdapter<HomeMarketBean> {
    public HomeMarketAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeMarketViewHolder(parent);
    }

    class HomeMarketViewHolder extends BaseViewHolder<HomeMarketBean>{
        TextView tvCoin,tvPrice,tvRose;
        public HomeMarketViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_home_market_view);
            tvCoin = $(R.id.tv_item_home_market_coin);
            tvPrice = $(R.id.tv_item_home_market_price);
            tvRose = $(R.id.tv_item_home_market_rise);
        }

        @Override
        public void setData(HomeMarketBean data) {
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(Integer.parseInt(data.getCoinType()));
            tvCoin.setText(coinInfo.getCoinname());
            tvPrice.setText("￥"+data.getNewPriceCNY());
            tvRose.setText(data.getChgPrice()+"%");
        }
    }

}
