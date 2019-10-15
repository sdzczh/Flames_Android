package app.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.C2cNormalEntrust;
import app.com.pgy.R;
import app.com.pgy.Utils.ToolsUtils;

/**
 * Create by Android on 2019/10/15 0015
 */
public class C2CTradeOrderListNewAdapter extends RecyclerArrayAdapter<C2cNormalEntrust.ListBean>{

    public C2CTradeOrderListNewAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentEntrustViewHolder(parent);
    }

    class CurrentEntrustViewHolder extends BaseViewHolder<C2cNormalEntrust.ListBean> {
        private TextView tvitemCcTradeOrderType;
//        private TextView itemCcTradeOrderType;
        private TextView itemCcTradeOrderCoinName;
        private TextView itemCcTradeOrderSubmitTime;
        private TextView itemCcTradeOrderState;
        private TextView itemCcTradeOrderPrice;
        private TextView itemCcTradeOrderNumber;
        private TextView itemCcTradeOrderDealAmount;
        private TextView itemCcTradeOrderInactiveTime;

        CurrentEntrustViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_cc_trade_order_new);
            tvitemCcTradeOrderType = $(R.id.tv_item_ccTradeOrder_type);
//            itemCcTradeOrderType = $(R.id.item_ccTradeOrder_type);
            itemCcTradeOrderCoinName = $(R.id.item_ccTradeOrder_coinName);
            itemCcTradeOrderSubmitTime = $(R.id.item_ccTradeOrder_submitTime);
            itemCcTradeOrderState = $(R.id.item_ccTradeOrder_state);
            itemCcTradeOrderPrice = $(R.id.item_ccTradeOrder_price);
            itemCcTradeOrderNumber = $(R.id.item_ccTradeOrder_number);
            itemCcTradeOrderDealAmount = $(R.id.item_ccTradeOrder_dealAmount);
            itemCcTradeOrderInactiveTime = $(R.id.item_ccTradeOrder_inactive_time);
        }

        @Override
        public void setData(C2cNormalEntrust.ListBean entrust) {
            /*买入或者卖出*/
            int buyOrSale = entrust.getOrderType();
            switch (buyOrSale) {
                default:
                    break;
                case 0:
//                    itemCcTradeOrderType.setText("买入");
//                    itemCcTradeOrderType.setTextColor(getContext().getResources().getColor(R.color.txt_green));
                    tvitemCcTradeOrderType.setText("买");
                    tvitemCcTradeOrderType.setBackgroundResource(R.mipmap.trade_buy_bg);
                    break;
                case 1:
//                    itemCcTradeOrderType.setText("卖出");
//                    itemCcTradeOrderType.setTextColor(getContext().getResources().getColor(R.color.txt_red));
                    tvitemCcTradeOrderType.setText("卖");
                    tvitemCcTradeOrderType.setBackgroundResource(R.mipmap.trade_sale_bg);

                    break;
            }
            String coinName = ToolsUtils.getCoinName(entrust.getCoinType());
            itemCcTradeOrderCoinName.setText(coinName);
            itemCcTradeOrderSubmitTime.setText(entrust.getCreateTime());
            /*状态 0代付款 1待确认 2冻结 3已完成 4已取消 5超时取消*/
            int state = entrust.getState();
            String stateName = "";
            int stateColor = R.color.txt_808DAC;
            switch (state) {
                default:
                    break;
                case 0:
                    stateName = "待付款";
                    stateColor = R.color.txt_app;
                    break;
                case 1:
                    stateName = "待确认";
                    stateColor = R.color.txt_app;
                    break;
                case 2:
                    stateName = "申诉中";
                    stateColor = R.color.txt_F66950;
                    break;
                case 3:
                    stateName = "已成交";
                    stateColor = R.color.txt_01C18B;
                    break;
                case 4:
                    stateName = "已取消";
                    stateColor = R.color.txt_808DAC;
                    break;
                case 5:
                    stateName = "超时取消";
                    stateColor = R.color.txt_808DAC;
                    break;
            }
            itemCcTradeOrderState.setText(stateName);
            itemCcTradeOrderState.setTextColor(getContext().getResources().getColor(stateColor));
            itemCcTradeOrderPrice.setText(entrust.getPrice()+"CNY");
            itemCcTradeOrderNumber.setText(entrust.getAmount()+coinName);
            itemCcTradeOrderDealAmount.setText(entrust.getTotal()+"CNY");
            itemCcTradeOrderInactiveTime.setText(entrust.getInactiveTime());
        }
    }
}
