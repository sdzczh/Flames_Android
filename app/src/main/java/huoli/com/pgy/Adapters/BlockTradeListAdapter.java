package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Models.Beans.BlockTradeInfo;
import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * Created by YX on 2018/7/19.
 */

public class BlockTradeListAdapter extends RecyclerArrayAdapter<BlockTradeInfo.ListBean> {
    int coinType = 1;
    public BlockTradeListAdapter(Context context,int coinType) {
        super(context);
        this.coinType = coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BlockTradeViewHolder(parent);
    }

    class BlockTradeViewHolder extends BaseViewHolder<BlockTradeInfo.ListBean>{
        TextView tv_title;
        TextView tv_profit;
        TextView tv_time;
        TextView tv_coinname;
        public BlockTradeViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_flow_record_view);
            tv_title = $(R.id.tv_item_flow_record_title);
            tv_profit = $(R.id.tv_item_flow_record_score);
            tv_time = $(R.id.tv_item_flow_record_time);
            tv_coinname = $(R.id.tv_item_flow_record_desc);
        }

        @Override
        public void setData(BlockTradeInfo.ListBean data) {

            tv_title.setText(data.getOperType()+"");
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(coinType);
            tv_coinname.setText(coinInfo.getCoinname()+"");
            tv_time.setText(data.getCreatetime());
            Double aDouble = MathUtils.string2Double(data.getAmount());
            if (aDouble >= 0){
                tv_profit.setTextColor(getContext().getResources().getColor(R.color.txt_01C18B));
                tv_profit.setText("+"+data.getAmount());
            }else {
                tv_profit.setTextColor(getContext().getResources().getColor(R.color.txt_F55B3D));
                tv_profit.setText(data.getAmount());
            }
        }
    }
}
