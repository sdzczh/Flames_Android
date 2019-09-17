package huoli.com.pgy.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import huoli.com.pgy.Utils.LogUtils;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:
 * 网络缓存管理工具类
 * 功能：保存来自Request的cookie
 *        获取所有cookie
 * @author 徐庆重
 */
public class MyCookiesManager implements CookieJar {
    /**缓存在内存中*/
    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
    private static MyCookiesManager inst;

    public static MyCookiesManager getInstance() {
        if (inst == null) {
            inst = new MyCookiesManager();
        }
        return inst;
    }


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url, cookies);
        //列举所有的cookie
        for (int i=0;i<cookies.size();i++) {
            LogUtils.w("cookies", cookies.get(i).toString());
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    /**清理所有缓存*/
    public void clearMyCookies(){
        if (cookieStore.size() > 0) {
            cookieStore.clear();
        }
    }
}