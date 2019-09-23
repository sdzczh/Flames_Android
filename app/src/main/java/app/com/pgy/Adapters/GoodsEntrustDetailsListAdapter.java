package app.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.EntrustDetails;
import app.com.pgy.R;
import app.com.pgy.Utils.ToolsUtils;

/**
 * 委托详情列表适配器
 *
 * @author xuqingzhong
 */
public class GoodsEntrustDetailsListAdapter extends RecyclerArrayAdapter<EntrustDetails.ListBean> {
    private static final String TAG = "GoodsEntrustDetailsList";

    public GoodsEntrustDetailsListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntrustDetailsViewHolder(parent);
    }

    class EntrustDetailsViewHolder extends BaseViewHolder<EntrustDetails.ListBean> {
        private TextView entrustDetailsItemTime;
        private TextView entrustDetailsItemDealPriceTitle;
        private TextView entrustDetailsItemDealPriceContent;
        private TextView entrustDetailsItemDealNumberTitle;
        private TextView entrustDetailsItemDealNumberContent;
        private TextView entrustDetailsItemDealAmountTitle;
        private TextView entrustDetailsItemDealAmountContent;

        EntrustDetailsViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_entrust_details);
            entrustDetailsItemTime = $(R.id.entrust_details_item_time);
            entrustDetailsItemDealPriceTitle = $(R.id.entrust_details_item_dealPrice_title);
            entrustDetailsItemDealPriceContent = $(R.id.entrust_details_item_dealPrice_content);
            entrustDetailsItemDealNumberTitle = $(R.id.entrust_details_item_dealNumber_title);
            entrustDetailsItemDealNumberContent = $(R.id.entrust_details_item_dealNumber_content);
            entrustDetailsItemDealAmountTitle = $(R.id.entrust_details_item_dealAmount_title);
            entrustDetailsItemDealAmountContent = $(R.id.entrust_details_item_dealAmount_content);
        }

        @Override
        public void setData(EntrustDetails.ListBean entrustDetails) {
            String perCoinName = ToolsUtils.getCoinName(entrustDetails.getUnitCoinType());
            String tradeCoinName = ToolsUtils.getCoinName(entrustDetails.getOrderCoinType());
            /*时间、成交价格、成交量、成交总额*/
            entrustDetailsItemTime.setText(entrustDetails.getCreateTime());
            entrustDetailsItemDealPriceTitle.setText("成交价("+perCoinName+")");
            entrustDetailsItemDealPriceContent.setText(entrustDetails.getPrice());
            entrustDetailsItemDealNumberTitle.setText("成交量("+tradeCoinName+")");
            entrustDetailsItemDealNumberContent.setText(entrustDetails.getAmount());
            entrustDetailsItemDealAmountTitle.setText("成交总价("+perCoinName+")");
            entrustDetailsItemDealAmountContent.setText(entrustDetails.getTotal());
        }

    }
}