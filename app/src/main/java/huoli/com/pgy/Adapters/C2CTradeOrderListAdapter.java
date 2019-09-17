package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Models.Beans.C2cNormalEntrust;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ToolsUtils;

/**
 * C2C界面普通用户和商家订单列表适配器
 *
 * @author xuqingzhong
 */
public class C2CTradeOrderListAdapter extends RecyclerArrayAdapter<C2cNormalEntrust.ListBean> {

    public C2CTradeOrderListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentEntrustViewHolder(parent);
    }

    class CurrentEntrustViewHolder extends BaseViewHolder<C2cNormalEntrust.ListBean> {
        private ImageView ivitemCcTradeOrderType;
        private TextView itemCcTradeOrderType;
        private TextView itemCcTradeOrderCoinName;
        private TextView itemCcTradeOrderSubmitTime;
        private TextView itemCcTradeOrderState;
        private TextView itemCcTradeOrderPriceTitle;
        private TextView itemCcTradeOrderPrice;
        private TextView itemCcTradeOrderNumberTitle;
        private TextView itemCcTradeOrderNumber;
        private TextView itemCcTradeOrderDealAmountTitle;
        private TextView itemCcTradeOrderDealAmount;

        CurrentEntrustViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_cc_trade_order);
            ivitemCcTradeOrderType = $(R.id.iv_item_ccTradeOrder_type);
            itemCcTradeOrderType = $(R.id.item_ccTradeOrder_type);
            itemCcTradeOrderCoinName = $(R.id.item_ccTradeOrder_coinName);
            itemCcTradeOrderSubmitTime = $(R.id.item_ccTradeOrder_submitTime);
            itemCcTradeOrderState = $(R.id.item_ccTradeOrder_state);
            itemCcTradeOrderPriceTitle = $(R.id.item_ccTradeOrder_priceTitle);
            itemCcTradeOrderPrice = $(R.id.item_ccTradeOrder_price);
            itemCcTradeOrderNumberTitle = $(R.id.item_ccTradeOrder_numberTitle);
            itemCcTradeOrderNumber = $(R.id.item_ccTradeOrder_number);
            itemCcTradeOrderDealAmountTitle = $(R.id.item_ccTradeOrder_dealAmountTitle);
            itemCcTradeOrderDealAmount = $(R.id.item_ccTradeOrder_dealAmount);
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
                    ivitemCcTradeOrderType.setImageResource(R.mipmap.mairu);
                    break;
                case 1:
//                    itemCcTradeOrderType.setText("卖出");
//                    itemCcTradeOrderType.setTextColor(getContext().getResources().getColor(R.color.txt_red));
                    ivitemCcTradeOrderType.setImageResource(R.mipmap.maichu);

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
            //itemCcTradeOrderPriceTitle.setText("价格("+coinName+")");
            itemCcTradeOrderNumberTitle.setText("数量("+coinName+")");
            //itemCcTradeOrderDealAmountTitle.setText("交易总额("+coinName+")");
            itemCcTradeOrderPrice.setText(entrust.getPrice());
            itemCcTradeOrderNumber.setText(entrust.getAmount());
            itemCcTradeOrderDealAmount.setText(entrust.getTotal());
        }
    }
}