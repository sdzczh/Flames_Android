package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import java.util.List;
import huoli.com.pgy.Models.Beans.PushBean.RecordsBean;
import huoli.com.pgy.R;

/**
 * K线图成交列表适配器
 *
 * @author xuqingzhong
 */
public class KLineDealListAdapter extends RecyclerArrayAdapter<RecordsBean> {
    public KLineDealListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BankCardViewHolder(parent);
    }

    public void addList(List<RecordsBean> list){
        if (list == null || list.size()<=0){
            return;
        }
        List<RecordsBean> allData = getAllData();
        clear();
        allData.addAll(0,list);
        if (allData.size()>=20) {
            allData = allData.subList(0, 20);
        }
        addAll(allData);
        //notifyDataSetChanged();
    }

    class BankCardViewHolder extends BaseViewHolder<RecordsBean> {
        private TextView itemKLineDealTime;
        private TextView itemKLineDealDeal;
        private TextView itemKLineDealPrice;
        private TextView itemKLineDealAmount;

        BankCardViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_kline_deal);
            itemKLineDealTime = $(R.id.item_kLineDeal_time);
            itemKLineDealDeal = $(R.id.item_kLineDeal_deal);
            itemKLineDealPrice = $(R.id.item_kLineDeal_price);
            itemKLineDealAmount = $(R.id.item_kLineDeal_amount);
        }

        @Override
        public void setData(RecordsBean deal) {
            itemKLineDealTime.setText(deal.getCreateTime());
            itemKLineDealPrice.setText(deal.getPrice());
            itemKLineDealAmount.setText(deal.getAmount());
            switch (deal.getOrderType()){
                default:
                    /*买入*/
                case 0:
                    itemKLineDealDeal.setText("买入");
                    itemKLineDealDeal.setTextColor(getContext().getResources().getColor(R.color.txt_green));
                    break;
                    /*卖出*/
                case 1:
                    itemKLineDealDeal.setText("卖出");
                    itemKLineDealDeal.setTextColor(getContext().getResources().getColor(R.color.txt_red));
                    break;
            }
        }

    }
}