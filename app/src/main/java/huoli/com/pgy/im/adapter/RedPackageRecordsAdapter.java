package huoli.com.pgy.im.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ToolsUtils;
import huoli.com.pgy.im.server.response.RedPackageRecord;

/**
 * 红包记录适配器
 *
 * @author xuqingzhong
 */
public class RedPackageRecordsAdapter extends RecyclerArrayAdapter<RedPackageRecord.ListBean> {
    public RedPackageRecordsAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeeRecordViewHolder(parent);
    }

    class FeeRecordViewHolder extends BaseViewHolder<RedPackageRecord.ListBean> {
        private TextView itemImRedPackageRecordName;
        private TextView itemImRedPackageRecordAmount;
        private TextView itemImRedPackageRecordTime;
        private TextView itemImRedPackageRecordPriceOfCNY;

        FeeRecordViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_im_redpackage_record);
            itemImRedPackageRecordName = $(R.id.item_im_redPackage_record_name);
            itemImRedPackageRecordAmount = $(R.id.item_im_redPackage_record_amount);
            itemImRedPackageRecordTime = $(R.id.item_im_redPackage_record_time);
            itemImRedPackageRecordPriceOfCNY = $(R.id.item_im_redPackage_record_priceOfCNY);
        }

        @Override
        public void setData(RedPackageRecord.ListBean record) {
            String amount = record.getAmount() + ToolsUtils.getCoinName(record.getCoinType());
            itemImRedPackageRecordAmount.setText(amount);
            itemImRedPackageRecordName.setText(record.getName());
            itemImRedPackageRecordTime.setText(record.getCreateTime());
            itemImRedPackageRecordPriceOfCNY.setText("≈¥"+record.getAmtOfCny());
        }
    }
}