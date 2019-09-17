package huoli.com.pgy.Interfaces;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: 获取网络返回json的接口
 * @author 徐庆重
 */

public interface getBeanCallback<T> {
    void onSuccess(T t);
    void onError(int errorCode, String reason);
}
