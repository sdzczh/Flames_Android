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

import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.C2CPersonalMessage;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.ToolsUtils;

/**
 * C2C普通用户个人信息买卖列表适配器
 *
 * @author xuqingzhong
 */
public class C2CPersonalBusinessAdapter extends RecyclerArrayAdapter<C2CPersonalMessage.ListBean> {
    private getPositionCallback positionCallback;
    private boolean isBuy;

    public void setPositionCallback(getPositionCallback positionCallback) {
        this.positionCallback = positionCallback;
    }

    public C2CPersonalBusinessAdapter(Context context, boolean isBuy) {
        super(context);
        this.isBuy = isBuy;
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentEntrustViewHolder(parent);
    }

    class CurrentEntrustViewHolder extends BaseViewHolder<C2CPersonalMessage.ListBean> implements View.OnClickListener {
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
        public void setData(C2CPersonalMessage.ListBean bean) {
            ccNormalBuyItemTopLine.setVisibility(View.VISIBLE);
            ccNormalBuyItemBottomLine.setVisibility(View.GONE);
            if (isBuy) {
                ccNormalBuyItemBuy.setText("购买");
                ccNormalBuyItemBuy.setBackground(getContext().getResources().getDrawable(R.drawable.bg_corners_bluesolid));
            } else {
                ccNormalBuyItemBuy.setText("出售");
                ccNormalBuyItemBuy.setBackground(getContext().getResources().getDrawable(R.drawable.bg_corners_darkbluesolid));
            }
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(bean.getCoinType());
            ImageLoaderUtils.displayCircle(getContext(), ccNormalBuyItemIcon, coinInfo.getImgurl());
            String coinname = coinInfo.getCoinname();
            ccNormalBuyItemUserName.setText(coinname);
            ccNormalBuyItemAllOrders.setVisibility(View.INVISIBLE);
            /*根据可支付类型，确定显示哪种支付方式*/
            int payType = bean.getPayType();
            List<View> views = new ArrayList<>();
            views.add(ccNormalBuyItemPayBank);
            views.add(ccNormalBuyItemPayWx);
            views.add(ccNormalBuyItemPayAli);
            ToolsUtils.showPayType(payType, views);
            LogUtils.w("payType", "商家列表adapter:" + payType);
            ccNormalBuyItemPrice.setText("¥" + bean.getPrice());
            ccNormalBuyItemNumber.setText("数量 " + bean.getAmount()+" "+coinname);
            ccNormalBuyItemLimitAmount.setText("限额 ¥" + bean.getTotalMin() + "~¥" + bean.getTotalMax());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ccNormal_buy_item_buy:
                    positionCallback.getPosition(getPosition());
                    break;
                default:
                    break;
            }
        }
    }
}