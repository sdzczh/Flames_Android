package app.com.pgy.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.ImSystemMessage;
import app.com.pgy.R;

/**
 * 交易通知数据适配器
 *
 * @author xuqingzhong
 */
public class TradeMessageListAdapter extends RecyclerArrayAdapter<ImSystemMessage.ListBean> {
    public TradeMessageListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeViewHolder(parent);
    }

    class NoticeViewHolder extends BaseViewHolder<ImSystemMessage.ListBean> {
        private TextView itemImTradeMessageTitle;
        private View itemImTradeMessagePoint;
        private TextView itemImTradeMessageTime;
        private TextView itemImTradeMessageContent;
        private TextView itemImTradeMessageOrderId;

        NoticeViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_im_trademessage);
            itemImTradeMessageTitle = $(R.id.item_im_tradeMessage_title);
            itemImTradeMessagePoint = $(R.id.item_im_tradeMessage_point);
            itemImTradeMessageTime = $(R.id.item_im_tradeMessage_time);
            itemImTradeMessageContent = $(R.id.item_im_tradeMessage_content);
            itemImTradeMessageOrderId = $(R.id.item_im_tradeMessage_orderId);
        }

        @Override
        public void setData(ImSystemMessage.ListBean message) {
            itemImTradeMessageTitle.setText(message.getTitle());
            switch (message.getState()) {
                default:
                    break;
                    /*未读*/
                case 0:
                    itemImTradeMessagePoint.setVisibility(View.VISIBLE);
                    break;
                    /*已读*/
                case 1:
                    itemImTradeMessagePoint.setVisibility(View.GONE);
                    break;

            }
            itemImTradeMessageTime.setText(message.getCreateTime());
            itemImTradeMessageContent.setText(message.getRoundup());
            itemImTradeMessageOrderId.setText("订单编号："+message.getRoundup());
        }
    }
}