package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * 二级选择币种适配器
 * @author xuqingzhong
 */
public class LeftSecondarySpinnerAdapter extends RecyclerArrayAdapter<Integer> {
    private getPositionCallback mOnItemClickListener;

    public LeftSecondarySpinnerAdapter(Context context) {
        super(context);
    }
    public void setOnItemClickListener(getPositionCallback listener) {
        this.mOnItemClickListener = listener;
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
            String coinName = ToolsUtils.getCoinName(coinType);
            this.coinName.setText(coinName);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.getPosition(getPosition());
            }
        }
    }

}