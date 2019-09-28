package app.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.HuiLvBean;
import app.com.pgy.R;

/**
 * Create by YX on 2019/9/28 0028
 */
public class HuiLvHistoryAdapter extends RecyclerArrayAdapter<HuiLvBean> {
    public HuiLvHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HuiLvBaseViewHolder(parent);
    }

    class HuiLvBaseViewHolder extends BaseViewHolder<HuiLvBean>{
        TextView tv_time,tv_fee,tv_weeks;

        public HuiLvBaseViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_history_huilv_view);
            tv_time = $(R.id.tv_item_huilv_history_time);
            tv_fee = $(R.id.tv_item_huilv_history_value);
            tv_weeks = $(R.id.tv_item_huilv_history_weeks);
        }

        @Override
        public void setData(HuiLvBean data) {
            if (data == null){
                return;
            }

            tv_time.setText(data.getTime());
            tv_fee.setText(data.getFee());
            tv_weeks.setText(data.getWeekFee());
        }
    }
}
