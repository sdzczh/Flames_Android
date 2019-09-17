package huoli.com.pgy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import huoli.com.pgy.Constants.Constants;
import huoli.com.pgy.Models.Beans.BuyOrSale;
import huoli.com.pgy.Models.Beans.PushBean.SocketResponseBean;
import huoli.com.pgy.Utils.JsonUtils;
import huoli.com.pgy.Utils.MathUtils;

/**
 * Created by YX on 2018/5/24.
 */

public class LeverGoodsListReceiver extends BroadcastReceiver {
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
                    /*kn/hlc行情5档*/
                case 151:
                    /*kn/hlc行情10档*/
                case 152:
                    /*kn/hlc行情20档*/
                case 153:
                    /*kn/hlc行情50档*/
                case 154:
                    /*kn/hlc行情100档*/
                case 155:
                        /*kn/hyc行情5档*/
                case 161:
                    /*kn/hyc行情10档*/
                case 162:
                    /*kn/hyc行情20档*/
                case 163:
                    /*kn/hyc行情50档*/
                case 164:
                    /*kn/hyc行情100档*/
                    //case 165:
                case 150:
//                    String goodsListStr = response.getInfo();
//                    LogUtils.w("levergoodsList",goodsListStr);
                    //BuyOrSale buyOrSaleList = (BuyOrSale) response.getInfo();
                    BuyOrSale buyOrSaleList = JsonUtils.deserialize(response.getInfo(), BuyOrSale.class);
                    if (buyOrSaleList == null){
                        buyOrSaleList = new BuyOrSale();
                    }
                    currentPrice = MathUtils.string2Double(buyOrSaleList.getPrice());
                    if (currentPrice != null){
                        /*回调市价*/
                        listCallback.onPriceCallback(currentPrice);
                    }else {
                        listCallback.onPriceCallback(0.00);
                    }
                    List<BuyOrSale.ListBean> buyOrders = buyOrSaleList.getBuys();
                    if (buyOrders == null){
                        buyOrders = new ArrayList<>();
                    }
                    List<BuyOrSale.ListBean> saleOrders = buyOrSaleList.getSales();
                    if (saleOrders == null){
                        saleOrders = new ArrayList<>();
                    }
                    listCallback.onBuyListCallback(buyOrders);
                    listCallback.onSaleListCallback(saleOrders);
                    break;
            }
        }

    }

    public interface onListCallback{
        void onBuyListCallback(List<BuyOrSale.ListBean> buyList);
        void onSaleListCallback(List<BuyOrSale.ListBean> saleList);
        void onPriceCallback(Double price);
    }

    public void setListCallback(onListCallback listCallback) {
        this.listCallback = listCallback;
    }
}
