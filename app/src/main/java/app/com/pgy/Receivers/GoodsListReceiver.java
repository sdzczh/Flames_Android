package app.com.pgy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import app.com.pgy.Constants.Constants;
import app.com.pgy.Models.Beans.BuyOrSale;
import app.com.pgy.Models.Beans.KLineBean;
import app.com.pgy.Models.Beans.PushBean.RecordsBean;
import app.com.pgy.Models.Beans.PushBean.SocketResponseBean;
import app.com.pgy.Utils.JsonUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;

/**
 * 现货数据监听，包括买入卖出列表、最新成交价格
 * @author 徐庆重
 */
public class GoodsListReceiver extends BroadcastReceiver {
    private static final String TAG = "GoodsListReceiver";
    private onListCallback listCallback;
    private Double currentPrice = 0.00;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Constants.SOCKET_ACTION)){
            SocketResponseBean response = (SocketResponseBean) intent.getSerializableExtra("response");
            if (response == null){
                response = new SocketResponseBean();
            }
            int scene = response.getScene();
            switch (scene){
                default:break;
                /*拦截现货交易数据*/
                case 350:
                    String infoStr = response.getInfo();
                    LogUtils.w("GoodsReceiver",infoStr);
                    BuyOrSale buyOrSaleList = JsonUtils.deserialize(infoStr,BuyOrSale.class);
                    if (buyOrSaleList == null){
                        buyOrSaleList = new BuyOrSale();
                    }
                    if (listCallback == null){
                        return;
                    }
                    /*实时价格不为空，则回调*/
                    if (buyOrSaleList.getPrice() != null) {
                        currentPrice = MathUtils.string2Double(buyOrSaleList.getPrice());
                        if (currentPrice != null) {
                        /*回调市价*/
                            listCallback.onPriceCallback(currentPrice);
                        } else {
                            listCallback.onPriceCallback(0.00);
                        }
                    }
                    List<BuyOrSale.ListBean> buyOrders = buyOrSaleList.getBuys();
                    if (buyOrders != null){
                        listCallback.onBuyListCallback(buyOrders);
                    }
                    List<BuyOrSale.ListBean> saleOrders = buyOrSaleList.getSales();
                    if (saleOrders != null){
                        listCallback.onSaleListCallback(saleOrders);
                    }
                    List<RecordsBean> lastDeals = buyOrSaleList.getRecords();
                    if (lastDeals != null){
                        listCallback.onLastDealListCallback(lastDeals);
                    }
                    List<KLineBean.ListBean> kline = buyOrSaleList.getKline();
                    if (kline != null){
                        listCallback.onKLineListCallback(kline);
                    }
                    break;
            }
        }

    }

    public interface onListCallback{
        void onBuyListCallback(List<BuyOrSale.ListBean> buyList);
        void onSaleListCallback(List<BuyOrSale.ListBean> saleList);
        void onPriceCallback(Double price);
        void onLastDealListCallback(List<RecordsBean> lastDealList);
        void onKLineListCallback(List<KLineBean.ListBean> kLineList);
    }

    public void setListCallback(onListCallback listCallback) {
        this.listCallback = listCallback;
    }
}
