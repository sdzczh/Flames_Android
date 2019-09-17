package huoli.com.pgy.Utils;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import huoli.com.pgy.Constants.MyApplication;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Models.Beans.Configuration;


/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: 工具类，获取宽高 dp px格式
 *
 * @author 徐庆重
 */

public class ToolsUtils {

    /**
     * 获取params
     * 参数列表，Base64加密后的参数字符串
     */
    public static String getBase64Params(Map<String, Object> map) {
        JSONObject json = new JSONObject(map);
        LogUtils.w("params","Base64加密前参数:"+json.toString());
        String encoder = BASE64.encoder(json.toString().getBytes());
        return TextUtils.isEmpty(encoder)?"":encoder;
    }

    /**
     * 获取params
     * 参数列表，AES加密后的参数字符串
     */
    public static String getAESParams(Map<String, Object> map,String key) {
        JSONObject json = new JSONObject(map);
        LogUtils.w("params","AES加密前参数:"+json.toJSONString());
        LogUtils.w("params","AESKey:"+key);
        return AES2.encrypt(json.toString(),key);
    }

    /**
     * 生成要上传的参数key
     * AES密钥，RSA加密后的字符串
     */
    public static String getUpLoadKey(String key) {
        //String publicKey = "MIHfMA0GCSqGSIb3DQEBAQUAA4HNADCByQKBwQCZE6s54Pr4lmiEnsIR3pEB51r1SdFFnyDSjKGpX1zdtRWif0ErcPu3QS9p4a2AK8p1bZkFgWCkQKA0EC8dOLalTFkZWBGr/Grq/nSZVuRjtnhBGFAZwp7iHPJg7fOhLLJgu/PUPlD1vzhDODCkhjJGKdPr6yyhg7+4uPVvgH153DdZ8ftncepMbZeh2B4yTlqG4HcCZ0w7OSqDnzugLy13ChbGzH3OYjlk++ThQ0+bYpIh28BpYhRkHTkl0/QqzLkCAwEAAQ==";
        //String publicKey = "MIHfMA0GCSqGSIb3DQEBAQUAA4HNADCByQKBwQCwD9oRTjZZ3q7fkP2I4cmvFCiZ6SsNrkq1yTXi6L/AXc5yFlerHPLxrZsLa/MBNTRGOO91WvoZBJUzuQkeFneVWvl/75xzaUEWmCBoh3kgRCy7QnyUhIey46ceRblPjXfV4TD1BIDICcbSIifP248STjPVBWSgjo3Hrw8IVWiVBhuiNSI5x0olgDpTFxTRnqmVCmhMCJNONKDyO6FU01zdZaQisC2O8sqFVK7K6VD2EByl5r7aNEI3hML9QSE7pgkCAwEAAQ==";
        String publicKey = "MIHfMA0GCSqGSIb3DQEBAQUAA4HNADCByQKBwQCwD9oRTjZZ3q7fkP2I4cmvFCiZ6SsNrkq1yTXi6L/AXc5yFlerHPLxrZsLa/MBNTRGOO91WvoZBJUzuQkeFneVWvl/75xzaUEWmCBoh3kgRCy7QnyUhIey46ceRblPjXfV4TD1BIDICcbSIifP248STjPVBWSgjo3Hrw8IVWiVBhuiNSI5x0olgDpTFxTRnqmVCmhMCJNONKDyO6FU01zdZaQisC2O8sqFVK7K6VD2EByl5r7aNEI3hML9QSE7pgkCAwEAAQ==";
        //String key = getMyLoginKey();
        Log.w("key", key);
        String upKey = "";
        try {
            upKey = RSA.encode(key, RSA.getPublikKey(BASE64.decoderByte(publicKey)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upKey;
    }

    /**
     * 生成自己的key,只在登录时使用，每次登录更新
     * 限定16位
     */
    public static String getMyLoginKey() {
        /*try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            //要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            return byteToHexString(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }*/
        /*GUID是一个128位长的数字，一般用16进制表示。算法的核心思想是结合机器的网卡、当地时间、一个随机数来生成GUID。从理论上讲，如果一台机器每秒产生10000000个GUID，则可以保证（概率意义上）3240年不重复。
        UUID是1.5中新增的一个类，在java.util下，用它可以产生一个号称全球唯一的ID：*/
        UUID uuid = UUID.randomUUID();
        String UUidStr = uuid.toString().replace("-", "").substring(0, 16);
        LogUtils.w("key",UUidStr);
        return UUidStr;
    }

    private static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    /**获取上传签名sign*/
    public static String getUploadSign(Map<String, Object> params){
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String sign = "";
        Set<String> keySet = params.keySet();
        /*keySet.remove("deviceNum");
        keySet.remove("systemType");*/
        Object[] names = keySet.toArray();
        Arrays.sort(names);
        StringBuffer signBuf = new StringBuffer();
        boolean first = true;
        for (Object name : names) {
            if (first) {
                first = false;
            } else {
                signBuf.append("&");
            }
            signBuf.append(name).append("=");
            Object value = params.get(name);
            if (null != value) {
                try {
                    signBuf.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!TextUtils.isEmpty(key)) {
            signBuf.append("&key=").append(key);
        }
        try {
            sign = MD5.getMd5(signBuf.toString()).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        LogUtils.w("params","sign:"+sign);
        return sign;
    }


    /*
    String secretKey = RSA.encode(key, RSA.getPublikKey(BASE64.decoderByte(publicKey)));
    try {
            System.out.println("param==>"+AES.encrypt(json.toJSONString(), key));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    /**验证是否为手机号*/
    public static boolean isPhone(String userPhone){
        String regexPhone = "^1\\d{10}$";
        Pattern p = Pattern.compile(regexPhone);
        Matcher m = p.matcher(userPhone);
        return m.matches();
    }

    /**验证是否为6-18位数字和字母的组合*/
    public static boolean isDigitalAndWord(String userInput){
        if (TextUtils.isEmpty(userInput)){
            return false;
        }
        //用户代码必须为数字或字母，只能为6~18位
        String regexLoginPwd = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,18}$";
        return userInput.matches(regexLoginPwd);
    }

    /**验证是否为6位数字的交易密码*/
    public static boolean isTradePwd(String userPwd){
        if (TextUtils.isEmpty(userPwd)){
            return false;
        }
        String regexTradePwd = "^\\d{6}$";
        return userPwd.matches(regexTradePwd);
    }

    /**验证是否为6位数字的验证码*/
    public static boolean isVerificationCode(String verificationCode){
        if (TextUtils.isEmpty(verificationCode)){
            return false;
        }
        String regexTradePwd = "^\\d{6}$";
        return verificationCode.matches(regexTradePwd);
    }

    /**获取币种信息*/
    public static Configuration.CoinInfo getCoinInfo(int coinType){
        /*初始化配置文件*/
        //Preferences.init(context);
        Configuration configuration = Preferences.getConfiguration();
        Map<Integer, Configuration.CoinInfo> coinInfoMap = configuration.getCoinInfo();
        if (coinInfoMap == null){
            return new Configuration.CoinInfo();
        }
        Configuration.CoinInfo coinInfo = coinInfoMap.get(coinType);
        if (coinInfo == null){
            return new Configuration.CoinInfo();
        }
        return coinInfo;
    }

    /**根据coin的int值获取name*/
    public static String getCoinName(int type) {
        String coinName = getCoinInfo(type).getCoinname();
        return TextUtils.isEmpty(coinName)?"":coinName;
    }

    /**获取C2C，HLC/CNY*/
    public static String getGoodsC2CName(int tradeCoin, int perCoin){
        String perCoinName = getCoinName(perCoin);
        String tradeCoinName = getCoinName(tradeCoin);
        if (TextUtils.isEmpty(perCoinName) && TextUtils.isEmpty(tradeCoinName)){
            return "";
        }
        return tradeCoinName +"/"+ perCoinName;
    }

    public static void showPayType(int payType, List<View> views) {
        LogUtils.w("payType","payType:"+payType);
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
        if (payType <= 0 || payType >7 ){
            return;
        }
        String binaryString = Integer.toBinaryString(payType);
        for (int i = 0; i < binaryString.getBytes().length; i++){
            if (i >= views.size()){
                return;
            }
            views.get(i).setVisibility(get(payType, i)>0? View.VISIBLE:View.GONE);
        }
    }

    /**
     * @param num:要获取二进制值的数
     * @param index:倒数第一位为0，依次类推
     */
    private static int get(int num, int index){
        return (num & (0x1 << index)) >> index;
    }

    /**c2c界面中的状态，列表和详情*/
    public static String getC2cEntrustStateName(int state) {
        String stateStr;
        switch (state){
            default:
                stateStr = "订单状态";
                break;
            case 0:
                stateStr = "待付款";
                break;
            case 1:
                stateStr = "待确认";
                break;
            case 2:
                stateStr = "客诉处理中";
                break;
            case 3:
                stateStr = "已完成";
                break;
            case 4:
                stateStr = "已取消";
                break;
            case 5:
                stateStr = "超时未支付，自动取消";
                break;
        }
        return stateStr;
    }

    /**充值和提现列表和详情中的状态*/
    public static String getOrderStateName(int state) {
        String stateStr;
        switch (state){
            default:
                stateStr = "记录状态";
                break;
            case 0:
                stateStr = "正在处理";
                break;
            case 1:
                stateStr = "成功";
                break;
            case 2:
                stateStr = "已撤销";
                break;
            case 3:
                stateStr = "已失败";
                break;
        }
        return stateStr;
    }
}
