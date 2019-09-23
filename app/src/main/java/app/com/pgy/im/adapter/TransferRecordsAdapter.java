package app.com.pgy.im.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.R;
import app.com.pgy.Utils.ToolsUtils;
import app.com.pgy.im.db.TransferRecord;

/**
 * 红包记录适配器
 *
 * @author xuqingzhong
 */
public class TransferRecordsAdapter extends RecyclerArrayAdapter<TransferRecord.ListBean>{
    public TransferRecordsAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransferRecordViewHolder(parent);
    }

    class TransferRecordViewHolder extends BaseViewHolder<TransferRecord.ListBean> {
        private TextView itemImRedPackageRecordName;
        private TextView itemImRedPackageRecordAmount;
        private TextView itemImRedPackageRecordTime;
        private TextView itemImRedPackageRecordPriceOfCNY;

        TransferRecordViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_im_redpackage_record);
            itemImRedPackageRecordName = $(R.id.item_im_redPackage_record_name);
            itemImRedPackageRecordAmount = $(R.id.item_im_redPackage_record_amount);
            itemImRedPackageRecordTime = $(R.id.item_im_redPackage_record_time);
            itemImRedPackageRecordPriceOfCNY = $(R.id.item_im_redPackage_record_priceOfCNY);
        }

        @Override
        public void setData(TransferRecord.ListBean record) {
            String amount = record.getAmount() + ToolsUtils.getCoinName(record.getCoinType());
            itemImRedPackageRecordAmount.setText(amount);
            itemImRedPackageRecordName.setText(record.getName());
            itemImRedPackageRecordTime.setText(record.getCreateTime());
            itemImRedPackageRecordPriceOfCNY.setText(record.getPriceOfCNy());
        }
    }
}