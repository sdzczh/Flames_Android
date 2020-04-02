package app.com.pgy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;

import app.com.pgy.Constants.Constants;
import app.com.pgy.Models.Beans.KLineBean;
import app.com.pgy.Models.Beans.PushBean.SocketResponseBean;
import app.com.pgy.Models.Beans.ShenduBean;
import app.com.pgy.Utils.JsonUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.ToastUtils;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:接收k线图详情界面顶部，最新、最高、最低价
 *
 * @author 徐庆重
 */
public class KLineMessageReceiver extends BroadcastReceiver {
    KLineMessageListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.SOCKET_ACTION)) {
            SocketResponseBean response = (SocketResponseBean) intent.getSerializableExtra("response");
            if (response == null) {
                response = new SocketResponseBean();
            }
            int scene = response.getScene();
            switch (scene) {
                case 3523:
                    System.out.println("context = [" + context + "], intent = [" + response.getInfo() + "]");
                    ShenduBean shenDuBean = JsonUtils.deserialize(response.getInfo(), ShenduBean.class);
                    if (shenDuBean == null) {
                        shenDuBean = new ShenduBean();
                        LogUtils.w("KLineBean", "解析错误");
                    }
                    if (listener != null) {
                        listener.getKLShenDuBean(shenDuBean);
                    }
                    break;
                default:
                    break;
                /*k线图详情*/
                case 3521:
                case 3522:
                    KLineBean kLineBean = JsonUtils.deserialize(response.getInfo(), KLineBean.class);
                    if (kLineBean == null) {
                        kLineBean = new KLineBean();
                        LogUtils.w("KLineBean", "解析错误");
                    }
                    if (listener != null) {
                        listener.getKLineBean(kLineBean);
                    }

                    break;
            }
        }
    }

    public interface KLineMessageListener {
        void getKLineBean(KLineBean kLineBean);

        void getKLShenDuBean(ShenduBean shenduBean);
    }

    public void setListener(KLineMessageListener listener) {
        this.listener = listener;
    }
}
