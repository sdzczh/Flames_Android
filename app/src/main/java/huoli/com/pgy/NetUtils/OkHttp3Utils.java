package huoli.com.pgy.NetUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import huoli.com.pgy.BuildConfig;
import huoli.com.pgy.Constants.MyApplication;
import huoli.com.pgy.Utils.LogUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: okhttp工具类
 * 功能：封装网络请求，获取okHttpClient供Retrofit使用
 * 设置网络缓存，缓存路径如下
 * @author 徐庆重
 */

public class OkHttp3Utils {
    static HttpLoggingInterceptor loggingInterceptor;
    private static OkHttpClient mOkHttpClient;
    /**
     * 获取OkHttpClient对象
     * @return OkHttpClient
     */
    public static OkHttpClient getOkHttpClient() {
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
        }
        //缓存到SD卡目录下HLCache目录下，缓存文件全是以url的md5加密字段为文件名，每一个response分两个文件保存，以.0和.1结尾的文件区分
        File cacheDirectory = new File(MyApplication.getInstance().getCacheDir(), "HLCache");
        Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);//设置缓存文件为10M

        if (null == mOkHttpClient) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .cookieJar(new MyCookiesManager())//设置一个自动管理cookies的管理器
                    //.addInterceptor(interceptor)  //添加拦截器,日志打印
                    //.addNetworkInterceptor(in)   //添加网络缓存
                    .addInterceptor(new LoggingInterceptor())   //添加拦截器,日志打印
                    //.addNetworkInterceptor(new CookiesInterceptor(MyApplication.getInstance().getApplicationContext()))//添加网络连接器
                    //设置请求读写的超时时间
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .cache(cache)
                    .build();
        }
        return mOkHttpClient;
    }

    /**设置拦截器，有网和没有网都是先读缓存*/
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtils.i("request=", request.toString());
            Response response = chain.proceed(request);
            LogUtils.i("response=", response.toString());

            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                cacheControl = "public, max-age=600";
            }
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
    };

   private static Interceptor in = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            // 有网络时 设置缓存超时时间1个小时,无网络时，设置超时为4周
            int maxAge = 60 * 60;
            int maxStale = 60 * 60 * 24 * 28;
            Request request = chain.request();
            Context context = MyApplication.getInstance().getApplicationContext();
            if (context == null){
                return null;
            }
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo == null){
                return null;
            }
            boolean flag = activeNetworkInfo.isAvailable();
            if (flag) {
                request = request.newBuilder()
                        //有网络时只从网络获取
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            } else {
                request = request.newBuilder()
                        //无网络时只从缓存中读取
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (flag) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    };

    /*拦截json*/
    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间

            String method = request.method();
            if ("POST".equals(method)) {
                StringBuilder sb = new StringBuilder();
                if (request.body() instanceof FormBody) {
                    FormBody body = (FormBody) request.body();
                    for (int i = 0; i < body.size(); i++) {
                        sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                    LogUtils.i("request=",String.format("发送请求 %s on %s %n%s %nRequestParams:{%s}",
                            request.url(), chain.connection(), request.headers(), sb.toString()));
                }
            } else {
                LogUtils.i("request=",String.format("发送请求 %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));
            }
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            LogUtils.i("response=", String.format("接收响应: [%s] %n返回json:【%s】 %.1fms %n%s", response.request().url(), responseBody.string(), (t2 - t1) / 1e6d, response.headers()));
            return response;

        }
    }

}