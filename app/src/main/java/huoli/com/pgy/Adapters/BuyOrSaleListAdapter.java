package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Models.Beans.BuyOrSale;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.MathUtils;

import static huoli.com.pgy.Constants.StaticDatas.BUY;
import static huoli.com.pgy.Constants.StaticDatas.SALE;

/**
 * 买卖档位适配器
 *
 * @author xuqingzhong
 */
public class BuyOrSaleListAdapter extends RecyclerArrayAdapter<BuyOrSale.ListBean> {
    private OnItemClickListener mOnItemClickListener;
    private int buyOrSale;

    public BuyOrSaleListAdapter(Context context, int buyOrSale) {
        super(context);
        this.buyOrSale = buyOrSale;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuyOrSaleViewHolder(parent);
    }

    class BuyOrSaleViewHolder extends BaseViewHolder<BuyOrSale.ListBean> implements OnClickListener {
        private View buyOrSaleItemBgColorLeft;
        private View buyOrSaleItemBgColorRight;
        private FrameLayout item;
        private TextView buyOrSaleItemType;
        private TextView buyOrSaleItemPrice;
        private TextView buyOrSaleItemAmount;

        BuyOrSaleViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_buyorsale);
            item = $(R.id.buyOrSale_item);
            item.setOnClickListener(this);
            buyOrSaleItemBgColorLeft = $(R.id.buyOrSale_item_bgColor_left);
            buyOrSaleItemBgColorRight = $(R.id.buyOrSale_item_bgColor_right);
            buyOrSaleItemType = $(R.id.buyOrSale_item_type);
            buyOrSaleItemPrice = $(R.id.buyOrSale_item_price);
            buyOrSaleItemAmount = $(R.id.buyOrSale_item_amount);
        }

        @Override
        public void setData(BuyOrSale.ListBean buyOrSaleBean) {
            int txtColor,bgColor;
            switch (buyOrSale) {
                default:
                case BUY:
                    txtColor = R.color.txt_00C58E;
//                    bgColor = R.color.bg_green_light;
                    break;
                case SALE:
                    txtColor = R.color.txt_F55B3D;
//                    bgColor = R.color.bg_red_light;
                    break;
            }
//            buyOrSaleItemBgColorRight.setBackgroundResource(bgColor);
            float right = MathUtils.string2float(buyOrSaleBean.getRate());
            LinearLayout.LayoutParams lpRight = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,right);
            buyOrSaleItemBgColorRight.setLayoutParams(lpRight);
            LinearLayout.LayoutParams lpLeft = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100-right);
            buyOrSaleItemBgColorLeft.setLayoutParams(lpLeft);

            buyOrSaleItemPrice.setTextColor(getContext().getResources().getColor(txtColor));
//            buyOrSaleItemType.setText(MathUtils.int2String(buyOrSaleBean.getNum()));
            buyOrSaleItemPrice.setText(buyOrSaleBean.getPrice());
            buyOrSaleItemAmount.setText(buyOrSaleBean.getRemain());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}