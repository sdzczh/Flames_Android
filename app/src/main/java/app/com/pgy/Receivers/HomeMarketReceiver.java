package app.com.pgy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Constants.Constants;
import app.com.pgy.Models.Beans.HomeMarketBean;
import app.com.pgy.Models.Beans.PushBean.SocketResponseBean;
import app.com.pgy.Utils.JsonUtils;
import app.com.pgy.Utils.LogUtils;

/**
 * Create by Android on 2019/10/11 0011
 */
public class HomeMarketReceiver extends BroadcastReceiver {
    private onListCallback listCallback;
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
                /*pgy行情*/
                case 354:
                    List<HomeMarketBean> marketOneListStr = JsonUtils.stringToArray(response.getInfo(),HomeMarketBean [].class);
                    if (marketOneListStr == null){
                        marketOneListStr = new ArrayList<>();
                        LogUtils.w("marketOneList","pgy行情解析失败");
                    }else {
                        LogUtils.w("marketOneList",marketOneListStr.toString());
                    }
                    if (listCallback != null) {
                        LogUtils.w("marketOneList","size:"+marketOneListStr.size());
                        listCallback.onMarketPgyCallback(marketOneListStr);
                    }
                    break;
                /*24H涨幅行情*/
                case 355:
                    //List<PushMarketBean.ListBean> marketMainListStr = (List<PushMarketBean.ListBean>) response.getInfo();
                    List<HomeMarketBean> marketMainListStr = JsonUtils.stringToArray(response.getInfo(),HomeMarketBean [].class);
                    if (marketMainListStr == null){
                        marketMainListStr = new ArrayList<>();
                        LogUtils.w("marketMainList","24h行情解析失败");
                    }else {
                        LogUtils.w("marketMainList",marketMainListStr.toString());
                    }

                    if (listCallback != null) {
                        listCallback.onMarket24HCallback(marketMainListStr);
                    }
                    break;
            }
        }
    }


    public interface onListCallback{
        void onMarketPgyCallback(List<HomeMarketBean> marketList);
        void onMarket24HCallback(List<HomeMarketBean> marketList);
    }

    public void setListCallback(onListCallback listCallback) {
        this.listCallback = listCallback;
    }
}
