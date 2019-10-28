package app.com.pgy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.R;
import app.com.pgy.Utils.ToolsUtils;

/**
 * 二级选择币种适配器
 * @author xuqingzhong
 */
public class LeftSecondarySpinnerAdapter extends RecyclerArrayAdapter<Integer> {
    private getPositionCallback mOnItemClickListener;
    private int selectCoin;
    public LeftSecondarySpinnerAdapter(Context context) {
        super(context);
    }
    public void setOnItemClickListener(getPositionCallback listener) {
        this.mOnItemClickListener = listener;
    }

    public void setSelectCoin(int selectCoin) {
        this.selectCoin = selectCoin;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuyOrSaleViewHolder(parent);
    }

    class BuyOrSaleViewHolder extends BaseViewHolder<Integer> implements OnClickListener{
        private TextView coinName;

        BuyOrSaleViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_leftsecondaryspinner);
            coinName = $(R.id.item_leftSecondarySpinner_coinName);
            coinName.setOnClickListener(this);
        }

        @Override
        public void setData(Integer coinType) {
            String coinName1 = ToolsUtils.getCoinName(coinType);
            this.coinName.setText(coinName1);
            if (selectCoin == coinType){
                coinName.setTextColor(Color.parseColor("#098DE6"));
            }else {
                coinName.setTextColor(Color.parseColor("#333333"));
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.getPosition(getPosition());
            }
        }
    }

}