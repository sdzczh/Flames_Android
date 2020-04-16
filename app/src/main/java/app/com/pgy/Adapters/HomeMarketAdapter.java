package app.com.pgy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.HomeMarketBean;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
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

    class HomeMarketViewHolder extends BaseViewHolder<HomeMarketBean> {
        TextView tvCoin, tvPrice, tvRose, tv_item_home_new_price;

        public HomeMarketViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_home_market_view);
            tvCoin = $(R.id.tv_item_home_market_coin);
            tvPrice = $(R.id.tv_item_home_market_price);
            tvRose = $(R.id.tv_item_home_market_rise);
            tv_item_home_new_price = $(R.id.tv_item_home_new_price);

        }

        @Override
        public void setData(HomeMarketBean data) {
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(MathUtils.string2Integer(data.getCoinType()));
            tvCoin.setText(coinInfo.getCoinname());
            tvPrice.setText(data.getNewPrice());
            tv_item_home_new_price.setText("≈￥" + data.getNewPriceCNY());
            tvRose.setText(data.getChgPrice() + "%");
            String chgPrice = data.getChgPrice();
            double rateOrFall = MathUtils.string2Double(chgPrice);
            if (rateOrFall > 0) {
                /*涨*/
                tvRose.setBackgroundResource(R.drawable.bg_corners_52cca3);
                tvRose.setText("+" + chgPrice + "%");
                tvPrice.setTextColor(Color.parseColor("#32CD85"));
            } else if (rateOrFall < 0) {
                /*跌*/
                tvRose.setBackgroundResource(R.drawable.bg_corners_fff766);
                tvRose.setText(chgPrice + "%");
                tvPrice.setTextColor(Color.parseColor("#F7251E"));
            } else {
                /*平*/
                tvRose.setBackgroundResource(R.drawable.bg_corners_c4c7cc);
                tvPrice.setTextColor(Color.parseColor("#c4c7cc"));
                tvRose.setText("+0.00%");
            }
        }
    }

}
