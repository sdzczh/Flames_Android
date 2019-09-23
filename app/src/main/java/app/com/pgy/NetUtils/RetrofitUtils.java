package app.com.pgy.NetUtils;

import app.com.pgy.Constants.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static app.com.pgy.Constants.Constants.HTTP_URL;
import static app.com.pgy.Constants.Constants.HTTP_URL_DEBUG;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:封装Retrofit工具
 * 功能：获取Retrofit，设置okhttpclient
 * @author 徐庆重
 */

public abstract class RetrofitUtils {
    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;
    /**
     * 获取Retrofit对象
     * @return Retrofit
     */
    public static Retrofit getRetrofit() {
        String webUrl = Constants.DEBUG?HTTP_URL_DEBUG:HTTP_URL;
//        webUrl = "http://172.16.1.188:9090";
        //webUrl = "http://172.16.1.139:8080";
        if (null == mRetrofit) {

            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttp3Utils.getOkHttpClient();
            }
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(webUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }
        return mRetrofit;
    }

}