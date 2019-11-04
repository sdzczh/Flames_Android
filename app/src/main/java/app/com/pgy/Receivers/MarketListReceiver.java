package app.com.pgy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import app.com.pgy.Constants.Constants;
import app.com.pgy.Models.Beans.PushMarketBean;
import app.com.pgy.Models.Beans.PushBean.SocketResponseBean;
import app.com.pgy.Utils.JsonUtils;
import app.com.pgy.Utils.LogUtils;

/**
 * 行情列表数据监听，包括COIN和主流
 * @author 徐庆重
 */
public class MarketListReceiver extends BroadcastReceiver {
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
                /*COIN行情*/
                case 3511:
                    List<PushMarketBean.ListBean> marketOneListStr = JsonUtils.stringToArray(response.getInfo(),PushMarketBean.ListBean [].class);
                    if (marketOneListStr == null){
                        marketOneListStr = new ArrayList<>();
                        LogUtils.w("marketOneList","PGY行情解析失败");
                    }else {
                        LogUtils.w("marketOneList",marketOneListStr.toString());
                    }
                    if (listCallback != null) {
                        LogUtils.w("marketOneList","size:"+marketOneListStr.size());
                        listCallback.onMarketOneListCallback(marketOneListStr);
                    }
                    break;
                    /*主流行情*/
                case 3512:
                    //List<PushMarketBean.ListBean> marketMainListStr = (List<PushMarketBean.ListBean>) response.getInfo();
                    List<PushMarketBean.ListBean> marketMainListStr = JsonUtils.stringToArray(response.getInfo(),PushMarketBean.ListBean [].class);
                    if (marketMainListStr == null){
                        marketMainListStr = new ArrayList<>();
                        LogUtils.w("marketMainList","主流行情解析失败");
                    }else {
                        LogUtils.w("marketMainList",marketMainListStr.toString());
                    }

//                    PushMarketBean marketMainBean = new PushMarketBean();
//                    marketMainBean.setList(marketMainListStr);
//                    List<PushMarketBean.ListBean> mainList = marketMainBean.getList();
//                    if (mainList == null){
//                        mainList = new ArrayList<>();
//                    }
                    if (listCallback != null) {
                        listCallback.onMarketMainListCallback(marketMainListStr);
                    }
                    break;
            }
        }

    }

    public interface onListCallback{
        void onMarketOneListCallback(List<PushMarketBean.ListBean> marketList);
        void onMarketMainListCallback(List<PushMarketBean.ListBean> marketList);
    }

    public void setListCallback(onListCallback listCallback) {
        this.listCallback = listCallback;
    }
}
