package app.com.pgy.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.Entrust;
import app.com.pgy.R;
import app.com.pgy.Utils.ToolsUtils;

/**
 * 当前委托和历史委托适配器
 * @author xuqingzhong
 */
public class GoodsEntrustListAdapter extends RecyclerArrayAdapter<Entrust.ListBean> {
    private getPositionCallback undoCallback;
    private getPositionCallback statusCallback;
    /**为true时，为当前委托，为false时为历史委托*/
    private boolean isShow;

    public void setUndoCallback(getPositionCallback undoCallback) {
        this.undoCallback = undoCallback;
    }

    public void setStatusCallback(getPositionCallback statusCallback) {
        this.statusCallback = statusCallback;
    }

    public GoodsEntrustListAdapter(Context context, boolean isShow) {
        super(context);
        this.isShow = isShow;
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentEntrustViewHolder(parent);
    }

    class CurrentEntrustViewHolder extends BaseViewHolder<Entrust.ListBean> implements View.OnClickListener {
        private ImageView currentEntrustItemTypeIcon;//买入还是卖出
        private TextView currentEntrustItemType;//买入还是卖出
        private TextView currentEntrustItemC2c; //C2C名字
        private TextView currentEntrustItemSubmitTime;//时间
        private TextView currentEntrustItemUndo;   //撤销或者状态
        private TextView currentEntrustItemStatus;   //更多
        private TextView currentEntrustItemEntrustPriceTitle;   //价格标题
        private TextView currentEntrustItemEntrustPrice;        //价格
        private TextView currentEntrustItemEntrustNumberTitle;  //数量标题
        private TextView currentEntrustItemEntrustNumber;       //数量
        private TextView currentEntrustItemDealNumberTitle;     //成交量标题
        private TextView currentEntrustItemDealNumber;          //成交量

        CurrentEntrustViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_current_entrust);
            currentEntrustItemTypeIcon = $(R.id.current_entrust_item_type_icon);
            currentEntrustItemType = $(R.id.current_entrust_item_type);
            currentEntrustItemC2c = $(R.id.current_entrust_item_c2c);
            currentEntrustItemDealNumberTitle = $(R.id.current_entrust_item_dealNumberTitle);
            currentEntrustItemEntrustPriceTitle = $(R.id.current_entrust_item_entrustPriceTitle);
            currentEntrustItemEntrustNumberTitle = $(R.id.current_entrust_item_entrustNumberTitle);
            currentEntrustItemEntrustNumber = $(R.id.current_entrust_item_entrustNumber);
            currentEntrustItemDealNumber = $(R.id.current_entrust_item_dealNumber);
            currentEntrustItemEntrustPrice = $(R.id.current_entrust_item_entrustPrice);
            currentEntrustItemSubmitTime = $(R.id.current_entrust_item_submitTime);
            currentEntrustItemUndo = $(R.id.current_entrust_item_undo);
            currentEntrustItemStatus = $(R.id.current_entrust_item_status);
            currentEntrustItemUndo.setOnClickListener(this);
            currentEntrustItemStatus.setOnClickListener(this);
        }

        @Override
        public void setData(Entrust.ListBean entrust) {
            if (isShow) {
                /*当前委托,不显示状态，显示撤销按钮*/
                currentEntrustItemStatus.setVisibility(View.GONE);
                currentEntrustItemUndo.setVisibility(View.VISIBLE);
            }else {
                /*历史委托，显示状态，不显示撤销按钮*/
                currentEntrustItemStatus.setVisibility(View.VISIBLE);
                currentEntrustItemUndo.setVisibility(View.GONE);
                //交易状态
                int state = entrust.getState();
                String stateName="";
                int stateColor = R.color.txt_808DAC;
                Drawable drawable = getContext().getResources().getDrawable(R.mipmap.more);
                switch (state) {
                    default:break;
                    case 0:
                        stateName = "未成交";
                        stateColor = R.color.txt_808DAC;
                        break;
                    case 1:
                        stateName = "已成交";
                        stateColor = R.color.txt_app;
                        drawable = getContext().getResources().getDrawable(R.mipmap.more_blue);
                        break;
                    case 2:
                        stateName = "已取消";
                        stateColor = R.color.txt_808DAC;
                        break;
                    case 3:
                        stateName = "交易失败";
                        stateColor = R.color.txt_808DAC;
                        break;
                }
                currentEntrustItemStatus.setText(stateName);
                currentEntrustItemStatus.setTextColor(getContext().getResources().getColor(stateColor));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                currentEntrustItemStatus.setCompoundDrawables(null,null,drawable,null);
            }
            int perCoin = entrust.getUnitCoinType();
            int tradeCoin = entrust.getOrderCoinType();
            String perCoinName= ToolsUtils.getCoinName(perCoin);
            String tradeCoinName = ToolsUtils.getCoinName(tradeCoin);
            int buyOrSale = entrust.getType();   //0买入 1卖出
            int tradeType = entrust.getOrderType();// 0市阶交易 1限阶交易
            String createTime = entrust.getCreateTime();    // 时间
            String price = entrust.getPrice();  //  委托价
            String amount = entrust.getAmount();    //委托量
            String dealAmount = entrust.getDealAmount();    //成交量
            currentEntrustItemC2c.setText(tradeCoinName+"/"+perCoinName);
            currentEntrustItemEntrustPriceTitle.setText("价格("+perCoinName+")");
            currentEntrustItemDealNumberTitle.setText("成交量("+tradeCoinName+")");
            /*当市价买入时，中间显示为市价委托额，icon为计价币KN*/
            if (tradeType == 0 && buyOrSale == 0){
                currentEntrustItemEntrustNumberTitle.setText("委托额("+perCoinName+")");
                currentEntrustItemEntrustNumber.setText(price);
            }else{
                currentEntrustItemEntrustNumberTitle.setText("委托量("+tradeCoinName+")");
                currentEntrustItemEntrustNumber.setText(amount);
            }
            switch (buyOrSale){
                default:break;
                case 0:
                    currentEntrustItemTypeIcon.setImageResource(R.mipmap.mairu);
//                    currentEntrustItemType.setText("买入");
//                    currentEntrustItemType.setTextColor(getContext().getResources().getColor(R.color.txt_green));
                    break;
                case 1:
                    currentEntrustItemTypeIcon.setImageResource(R.mipmap.maichu);
//                    currentEntrustItemType.setText("卖出");
//                    currentEntrustItemType.setTextColor(getContext().getResources().getColor(R.color.txt_red));
                    break;
            }
            /*限价买入、限价卖出、市价买入、市价卖出*/
            currentEntrustItemSubmitTime.setText(createTime);
            /*委托价：当为市价交易时显示市价，当为限价交易时显示为价格*/
            currentEntrustItemEntrustPrice.setText(tradeType==0?"市价":price);
            currentEntrustItemDealNumber.setText(dealAmount);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.current_entrust_item_undo:
                    if (undoCallback != null){
                        undoCallback.getPosition(getPosition());
                    }
                    break;
                case R.id.current_entrust_item_status:
                    if (statusCallback != null){
                        statusCallback.getPosition(getPosition());
                    }
                    break;
                    default:break;
            }
        }
    }
}