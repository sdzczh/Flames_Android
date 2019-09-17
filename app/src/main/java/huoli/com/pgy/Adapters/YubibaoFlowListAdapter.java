package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Models.Beans.YubibaoFlow;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * Created by YX on 2018/7/18.
 */

public class YubibaoFlowListAdapter extends RecyclerArrayAdapter<YubibaoFlow.ListBean> {
    int coinType = 0;
    public YubibaoFlowListAdapter(Context context,int coinType) {
        super(context);
        this.coinType = coinType;
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new YubibaoFlowViewHolder(parent);
    }

    class YubibaoFlowViewHolder extends BaseViewHolder<YubibaoFlow.ListBean>{
        TextView tv_title;
        TextView tv_score;
        TextView tv_time;
        TextView tv_desc;
        public YubibaoFlowViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_mywallet_flow_record_view);
            tv_title = $(R.id.tv_item_flow_record_title);
            tv_score = $(R.id.tv_item_flow_record_score);
            tv_time = $(R.id.tv_item_flow_record_time);
            tv_desc = $(R.id.tv_item_flow_record_desc);
        }

        @Override
        public void setData(YubibaoFlow.ListBean data) {
            tv_title.setText(data.getOpertype());
            tv_time.setText(data.getTime());
            tv_desc.setText(ToolsUtils.getCoinName(coinType));
            String amount = data.getResultAmount();
            double rateOrFall = MathUtils.string2Double(amount);
            if (rateOrFall >= 0){
                /*涨*/
                tv_score.setTextColor(getContext().getResources().getColor(R.color.txt_01C18B));
                tv_score.setText("+"+amount);
            }else{
                /*跌*/
                tv_score.setTextColor(getContext().getResources().getColor(R.color.txt_F66950));
                tv_score.setText(amount);
            }
        }
    }
}
