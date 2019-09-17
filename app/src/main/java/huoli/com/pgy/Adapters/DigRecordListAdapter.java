package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Models.Beans.DigRecord;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * 挖矿记录适配器
 *
 * @author xuqingzhong
 */
public class DigRecordListAdapter extends RecyclerArrayAdapter<DigRecord.ListBean> {
    public DigRecordListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeeRecordViewHolder(parent);
    }

    class FeeRecordViewHolder extends BaseViewHolder<DigRecord.ListBean> {
        private TextView digRecordItemAmount;
        private TextView digRecordItemCoinType;
        private TextView digRecordItemTime;

        FeeRecordViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_dig_record);
            digRecordItemAmount = $(R.id.item_digRecord_amount);
            digRecordItemCoinType = $(R.id.item_digRecord_coinType);
            digRecordItemTime = $(R.id.item_digRecord_time);
        }

        @Override
        public void setData(DigRecord.ListBean digRecord) {
            digRecordItemAmount.setText(digRecord.getAmount());
            digRecordItemCoinType.setText(ToolsUtils.getCoinName(digRecord.getCoinType()));
            digRecordItemTime.setText(digRecord.getCreateTime());
        }
    }
}