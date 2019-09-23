package app.com.pgy.Utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: Json解析工具类
 *
 * @author 徐庆重
 */
public class JsonUtils {

    private static Gson mGson = new Gson();

    /**
     * 将对象准换为json字符串
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String serialize(T object) {
        return mGson.toJson(object);
    }

    /**
     * 将json字符串转换为对象
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Class<T> clz){
        try {
            return mGson.fromJson(json, clz);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json对象转换为实体对象
     * @param json
     * @param clz
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T deserialize(JsonObject json, Class<T> clz) throws JsonSyntaxException {
        return mGson.fromJson(json, clz);
    }

    /**
     * 将json字符串转换为对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Type type){
        try{
            return mGson.fromJson(json, type);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //json字符串转列表

    public static <T> List<T> stringToArray(String s,Class<T[]> clazz ) {
        if (TextUtils.isEmpty(s)){
            return null;
        }
        T[] arr = mGson.fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

}
