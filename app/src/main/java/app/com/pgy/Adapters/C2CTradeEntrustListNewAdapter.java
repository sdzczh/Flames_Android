package app.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.C2cBusinessEntrust;
import app.com.pgy.R;
import app.com.pgy.Utils.ToolsUtils;

/**
 * Create by Android on 2019/10/18 0018
 */
public class C2CTradeEntrustListNewAdapter extends RecyclerArrayAdapter<C2cBusinessEntrust.ListBean> {

    private getPositionCallback undoOrderCallback;
    private getPositionCallback cancelOrderCallback;

    public C2CTradeEntrustListNewAdapter(Context context) {
        super(context);
    }

    public void setUndoOrderCallback(getPositionCallback undoOrderCallback) {
        this.undoOrderCallback = undoOrderCallback;
    }

    public void setCancelOrderCallback(getPositionCallback cancelOrderCallback) {
        this.cancelOrderCallback = cancelOrderCallback;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CurrentEntrustViewHolder(viewGroup);
    }


    class CurrentEntrustViewHolder extends BaseViewHolder<C2cBusinessEntrust.ListBean> implements View.OnClickListener {

        private TextView itemCcTradeEntrustType;
        private TextView itemCcTradeEntrustCoinName;
        private TextView itemCcTradeEntrustSubmitTime;
        private TextView itemCcTradeEntrustState;
        private TextView itemCcTradeEntrustPriceTitle;
        private TextView itemCcTradeEntrustPrice;
        private TextView itemCcTradeEntrustNumberTitle;
        private TextView itemCcTradeEntrustNumber;
        private TextView itemCcTradeEntrustDealAmountTitle;
        private TextView itemCcTradeEntrustDealAmount;
        private TextView itemCcTradeEntrustLimitPriceTitle;
        private TextView itemCcTradeEntrustLimitPrice;
        private TextView itemCcTradeEntrustRemainNumberTitle;
        private TextView itemCcTradeEntrustRemainNumber;
        private ImageView itemCcTradeEntrustPayAli;
        private ImageView itemCcTradeEntrustPayWx;
        private ImageView itemCcTradeEntrustPayBank;
        private TextView itemCcTradeEntrustUndoOrder;
        private TextView itemCcTradeEntrustStartOrCancelOrder;
        private LinearLayout itemCcTradeEntrustBottomFrame;

        CurrentEntrustViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_cc_trade_entrust_new);
            itemCcTradeEntrustType = $(R.id.item_ccTradeEntrust_type);
            itemCcTradeEntrustCoinName = $(R.id.item_ccTradeEntrust_coinName);
            itemCcTradeEntrustSubmitTime = $(R.id.item_ccTradeEntrust_submitTime);
            itemCcTradeEntrustState = $(R.id.item_ccTradeEntrust_state);
            itemCcTradeEntrustPriceTitle = $(R.id.item_ccTradeEntrust_priceTitle);
            itemCcTradeEntrustPrice = $(R.id.item_ccTradeEntrust_price);
            itemCcTradeEntrustNumberTitle = $(R.id.item_ccTradeEntrust_numberTitle);
            itemCcTradeEntrustNumber = $(R.id.item_ccTradeEntrust_number);
            itemCcTradeEntrustDealAmountTitle = $(R.id.item_ccTradeEntrust_dealAmountTitle);
            itemCcTradeEntrustDealAmount = $(R.id.item_ccTradeEntrust_dealAmount);
            itemCcTradeEntrustLimitPriceTitle = $(R.id.item_ccTradeEntrust_limitPriceTitle);
            itemCcTradeEntrustLimitPrice = $(R.id.item_ccTradeEntrust_limitPrice);
            itemCcTradeEntrustRemainNumberTitle = $(R.id.item_ccTradeEntrust_remainNumberTitle);
            itemCcTradeEntrustRemainNumber = $(R.id.item_ccTradeEntrust_remainNumber);
            itemCcTradeEntrustPayAli = $(R.id.item_ccTradeEntrust_payAli);
            itemCcTradeEntrustPayWx = $(R.id.item_ccTradeEntrust_payWx);
            itemCcTradeEntrustPayBank = $(R.id.item_ccTradeEntrust_payBank);
            itemCcTradeEntrustUndoOrder = $(R.id.item_ccTradeEntrust_undoOrder);
            itemCcTradeEntrustStartOrCancelOrder = $(R.id.item_ccTradeEntrust_startOrCancelOrder);
            itemCcTradeEntrustBottomFrame = $(R.id.item_ccTradeEntrust_bottomFrame);
            /*给撤销订单、取消订单添加监听*/
            itemCcTradeEntrustUndoOrder.setOnClickListener(this);
            itemCcTradeEntrustStartOrCancelOrder.setOnClickListener(this);
        }

        @Override
        public void setData(C2cBusinessEntrust.ListBean entrust) {
            /*状态，分为未完成、已完成、已撤销*/
            int state = entrust.getState();
            String stateStr;
            int stateColor;
            switch (state) {
                /*未完成*/
                default:
                case 0:
                    /*显示底部按钮*/
                    itemCcTradeEntrustBottomFrame.setVisibility(View.VISIBLE);
                    stateStr = "未完成";
                    stateColor = R.color.txt_app;
                    if (entrust.isOrderFlag()) {
                        itemCcTradeEntrustStartOrCancelOrder.setText("挂起委托");
                    } else {
                        itemCcTradeEntrustStartOrCancelOrder.setText("开始委托");
                    }
                    itemCcTradeEntrustState.setVisibility(View.GONE);
                    break;
                /*已完成*/
                case 1:
                    itemCcTradeEntrustBottomFrame.setVisibility(View.GONE);
                    stateStr = "已完成";
//                    stateColor = R.color.txt_01C18B;
                    itemCcTradeEntrustState.setVisibility(View.VISIBLE);
                    break;
                /*已撤销*/
                case 2:
                    itemCcTradeEntrustBottomFrame.setVisibility(View.GONE);
                    stateStr = "已撤销";
                    stateColor = R.color.txt_808DAC;
                    itemCcTradeEntrustState.setVisibility(View.VISIBLE);
                    break;
            }
            itemCcTradeEntrustState.setText(stateStr);
//            itemCcTradeEntrustState.setTextColor(getContext().getResources().getColor(stateColor));
            /*买入或者卖出*/
            switch (entrust.getOrderType()) {
                default:
                    break;
                case StaticDatas.BUY:
                    itemCcTradeEntrustType.setText("买");
                    itemCcTradeEntrustType.setBackgroundResource(R.mipmap.trade_buy_bg);
                    break;
                case StaticDatas.SALE:
                    itemCcTradeEntrustType.setText("卖");
                    itemCcTradeEntrustType.setBackgroundResource(R.mipmap.trade_sale_bg);
                    break;
            }
            String coinName = ToolsUtils.getCoinName(entrust.getCoinType());
            itemCcTradeEntrustCoinName.setText(coinName);
            itemCcTradeEntrustSubmitTime.setText(entrust.getCreateTime());
            itemCcTradeEntrustPrice.setText(entrust.getPrice()+"CNY");
            itemCcTradeEntrustNumber.setText(entrust.getAmount()+coinName);
            itemCcTradeEntrustDealAmount.setText(entrust.getTotal()+"CNY");
            itemCcTradeEntrustLimitPrice.setText(entrust.getTotalMin() + "-" + entrust.getTotalMax());
            itemCcTradeEntrustRemainNumber.setText(entrust.getRemain()+coinName);
            /*根据可支付类型，确定显示哪种支付方式*/
//            int payType = entrust.getPayType();
//            List<View> views = new ArrayList<>();
//            views.add(itemCcTradeEntrustPayBank);
//            views.add(itemCcTradeEntrustPayWx);
//            views.add(itemCcTradeEntrustPayAli);
//            ToolsUtils.showPayType(payType, views);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_ccTradeEntrust_undoOrder:
                    if (undoOrderCallback != null) {
                        undoOrderCallback.getPosition(getPosition());
                    }
                    break;
                case R.id.item_ccTradeEntrust_startOrCancelOrder:
                    if (cancelOrderCallback != null) {
                        cancelOrderCallback.getPosition(getPosition());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
