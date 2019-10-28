package app.com.pgy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.TradeCoinMarketBean;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;

/**
 * Create by Android on 2019/10/25 0025
 */
public class TradeCoinMarketOrderCoinAdapter extends RecyclerArrayAdapter<TradeCoinMarketBean> {

    private getPositionCallback mOnItemClickListener;
    private int selectcoin = -1;

    public TradeCoinMarketOrderCoinAdapter(Context context) {
        super(context);
    }

    public void setOnItemClickListener(getPositionCallback listener) {
        this.mOnItemClickListener = listener;
    }

    public void setSelectcoin(int selectcoin) {
        this.selectcoin = selectcoin;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    class OrderCoinViewHolder extends BaseViewHolder<TradeCoinMarketBean> implements View.OnClickListener{
        TextView tvCoin,tvPrice,tvRang;
        LinearLayout llBg;
        public OrderCoinViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_trade_coin_market);
            llBg = $(R.id.ll_bg);
            tvCoin = $(R.id.tv_trade_coin);
            tvPrice = $(R.id.tv_trade_price);
            tvRang = $(R.id.tv_trade_rang);
            llBg.setOnClickListener(this);
        }

        @Override
        public void setData(TradeCoinMarketBean data) {
            if (data != null){
                tvCoin.setText(data.getOrderCoinName()+"/"+data.getUnitCoinName());
                tvPrice.setText(data.getNewPrice());
                String chgPrice = data.getChgPrice();
                double rateOrFall = MathUtils.string2Double(chgPrice);
                if (rateOrFall > 0){
                    /*涨*/
                    tvRang.setTextColor(Color.parseColor("#4DB872"));
                    tvRang.setText("+"+chgPrice+"%");
                }else if (rateOrFall < 0){
                    /*跌*/
                    tvRang.setTextColor(Color.parseColor("#F66950"));
                    tvRang.setText(chgPrice+"%");
                }else{
                    /*平*/
                    tvRang.setTextColor(Color.parseColor("#c4c7cc"));
                    tvRang.setText("+0.00%");
                }
                if (data.getOrderCoinType() == selectcoin){
                    tvCoin.setTextColor(Color.parseColor("#098DE6"));
                }else {
                    tvCoin.setTextColor(Color.parseColor("#333333"));
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null){
                mOnItemClickListener.getPosition(getAdapterPosition());
            }
        }
    }
}
