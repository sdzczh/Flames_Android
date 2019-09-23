package app.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Interfaces.clickHeadCallback;
import app.com.pgy.Models.Beans.C2cNormalBusiness;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.ToolsUtils;

/**
 * C2C普通用户购买列表适配器
 *
 * @author xuqingzhong
 */
public class C2CNormalBuyAdapter extends RecyclerArrayAdapter<C2cNormalBusiness.ListBean> {
    private clickHeadCallback positionCallback;
    private boolean isBuy;
    private int coinType;

    public void setPositionCallback(clickHeadCallback positionCallback) {
        this.positionCallback = positionCallback;
    }

    public C2CNormalBuyAdapter(Context context, boolean isBuy, int coinType) {
        super(context);
        this.isBuy = isBuy;
        this.coinType = coinType;
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentEntrustViewHolder(parent);
    }

    class CurrentEntrustViewHolder extends BaseViewHolder<C2cNormalBusiness.ListBean> implements View.OnClickListener {
        private View ccNormalBuyItemTopLine;
        private View ccNormalBuyItemBottomLine;
        private ImageView ccNormalBuyItemIcon;
        private TextView ccNormalBuyItemUserName;
        private TextView ccNormalBuyItemAllOrders;
        private ImageView ccNormalBuyItemPayAli;
        private ImageView ccNormalBuyItemPayWx;
        private ImageView ccNormalBuyItemPayBank;
        private TextView ccNormalBuyItemBuy;
        private TextView ccNormalBuyItemPrice;
        private TextView ccNormalBuyItemNumber;
        private TextView ccNormalBuyItemLimitAmount;

        CurrentEntrustViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_cc_normal_trade);
            ccNormalBuyItemBottomLine = $(R.id.ccNormal_buy_item_bottomLine);
            ccNormalBuyItemTopLine = $(R.id.ccNormal_buy_item_topLine);
            ccNormalBuyItemIcon = $(R.id.ccNormal_buy_item_icon);
            ccNormalBuyItemUserName = $(R.id.ccNormal_buy_item_userName);
            ccNormalBuyItemAllOrders = $(R.id.ccNormal_buy_item_allOrders);
            ccNormalBuyItemPayAli = $(R.id.ccNormal_buy_item_payAli);
            ccNormalBuyItemPayWx = $(R.id.ccNormal_buy_item_payWx);
            ccNormalBuyItemPayBank = $(R.id.ccNormal_buy_item_payBank);
            ccNormalBuyItemBuy = $(R.id.ccNormal_buy_item_buy);
            ccNormalBuyItemPrice = $(R.id.ccNormal_buy_item_price);
            ccNormalBuyItemNumber = $(R.id.ccNormal_buy_item_number);
            ccNormalBuyItemLimitAmount = $(R.id.ccNormal_buy_item_limitAmount);
            ccNormalBuyItemIcon.setOnClickListener(this);
            ccNormalBuyItemBuy.setOnClickListener(this);
        }

        @Override
        public void setData(C2cNormalBusiness.ListBean c2cNormalBusiness) {
            ccNormalBuyItemTopLine.setVisibility(View.GONE);
            ccNormalBuyItemBottomLine.setVisibility(View.GONE);
            if (isBuy) {
                ccNormalBuyItemBuy.setText("购买");
                ccNormalBuyItemBuy.setTextColor(getContext().getResources().getColor(R.color.txt_01C18B));
            } else {
                ccNormalBuyItemBuy.setText("出售");
                ccNormalBuyItemBuy.setTextColor(getContext().getResources().getColor(R.color.txt_F66950));
            }
            ImageLoaderUtils.displayCircle(getContext(), ccNormalBuyItemIcon, c2cNormalBusiness.getHeadPath());
            ccNormalBuyItemUserName.setText(c2cNormalBusiness.getUserName());
            ccNormalBuyItemAllOrders.setText(c2cNormalBusiness.getQuantity() + "单");
            /*根据可支付类型，确定显示哪种支付方式*/
            int payType = c2cNormalBusiness.getPayType();
            List<View> views = new ArrayList<>();
            views.add(ccNormalBuyItemPayBank);
            views.add(ccNormalBuyItemPayWx);
            views.add(ccNormalBuyItemPayAli);
            ToolsUtils.showPayType(payType, views);
            LogUtils.w("payType", "商家列表adapter:" + payType);
            ccNormalBuyItemPrice.setText("¥  " + c2cNormalBusiness.getPrice());
            String coinName = ToolsUtils.getCoinName(coinType);
            ccNormalBuyItemNumber.setText(c2cNormalBusiness.getRemain()+" "+coinName);
            ccNormalBuyItemLimitAmount.setText( c2cNormalBusiness.getTotalMin() + "-" + c2cNormalBusiness.getTotalMax()+" CNY");
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ccNormal_buy_item_icon:
                    positionCallback.clickHead(getPosition());
                    break;
                case R.id.ccNormal_buy_item_buy:
                    positionCallback.clickItem(getPosition());
                    break;
                default:
                    break;
            }
        }
    }
}