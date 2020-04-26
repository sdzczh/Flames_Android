package app.com.pgy.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.Models.Beans.PushBean.PushData;
import app.com.pgy.Utils.JsonUtils;
import app.com.pgy.Utils.LogUtils;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * Perferences类，将登录的用户信息保存在本地，用来判断用户是否登录，版本是否最新等
 *
 * @author 徐庆重
 */
public final class Preferences {
    public static SharedPreferences mSharedPreferences = null;
    private static Context mAppContext;
    private static String mDeviceId;
    private static String mVersionName;
    /*配置文件*/
    private static String CONFIG = "configuration";
    //本地sp中保存的所有字段数据
    /*用户信息，token、key、name、phone、logo、is_tradepwd、is_realname、alipay*/
    private static String ACCESS_TOKEN = "access_token";    //用户token
    private static String ACCESS_KEY = "access_key";    //请求时使用的key，AES密钥，RSA加密后的字符串
    private static String USER_NAME = "user_name";    //用户姓名
    private static String USER_PHONE = "user_phone";    //用户手机号
    private static String USER_LOGO = "user_logo";    //用户logo
    private static String HAS_TRADEPWD = "isHas_tradepwd";    //用户是否设置了交易密码
    private static String HAS_REALNAME = "isHas_realname";    //用户是否已经实名认证
    private static String USER_ALIPAY = "user_alipay";    //用户绑定的支付宝账号
    private static String USER_WALLET_ADDRESS = "user_wallet_address";
    private static String USER_TALK_TOKEN = "user_talk_token";
    private static String USER_PAY_ALI = "user_pay_ali";
    private static String USER_PAY_WECHART = "user_pay_wechart";
    private static String USER_PAY_CARD = "user_pay_card";
    private static String USER_STATE_DIG = "user_state_dig";
    private static String USER_FORCE_LEVLE = "user_force_level";
    private static String USER_SEX = "user_sex";
    private static String USER_BIRTHDAY = "user_birthday";
    private static String USER_UUID = "user_uuid";
    private static String USER_MARKET = "user_markets";
    private static String USER_IDSTATUS = "user_idstatus";
    private static String USER_REFERSTATUS = "user_referstatus";
    private static String USER_REALNAME = "real_name";    //用户姓名

    private static String ONLY_WIFI = "only_wifi";      //仅在WiFi下
    private static String VERSION_CODE = "version_code";    //当前版本信息
    private static String LASTJUMPVERSIONCODE = "versionCode_lastJump";//最近一次跳过的版本号
    private static String DELTA_TIME = "delta_time"; //时间差
    private static String GOODS_COIN_TRADE = "c2c_coin_trade"; //c2c交易币
    private static String GOODS_COIN_PER = "c2c_coin_per"; //c2c计价币
    private static String C2C_COIN = "c2c_coin"; //c2c当前币种
    private static String DIYA_COIN = "diya_coin"; //当前抵押币种
    private static String DOWNLOAD_REFERNECE ="download_reference"; //下载更新apk
    private static String CURRENT_ENTRUST_REFRESH = "currentEntrust_hasNewData";    //是否有新当前委托数据
    private static String HISTORY_ENTRUST_REFRESH = "historyEntrust_hasNewData";    //是否有新历史委托数据
    private static String LEVER_COIN_TRADE = "lever_coin_trade"; //杠杆交易币
    private static String LEVER_COIN_PER = "lever_coin_per"; //杠杆计价币
    private static String LEVER_AGREEMENT_READED = "lever_agreemnet_readed"; //杠杆交易协议已同意

    private static String BLOCK_BG_MUSIC = "block_bg_music"; //挖矿背景音乐开关
    private static String BLOCK_COLLECTION_MUSIC = "block_collection_music"; //收矿背景音乐开关
    private static String YUBIBAO_YUE_VISIBLE = "yubibao_yue_visible"; //余币宝余额可见

    private static String WEBVIEW_COOKIES = "webview-cookies"; //cookies
    private static final String TRADE_PWD = "trade_password";

    /**初始化*/
    public static void init(Context context) {
        if (mSharedPreferences == null) {
            mAppContext = context.getApplicationContext();
            mSharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(mAppContext);
        }
    }

    /**存储配置文件*/
    public static boolean setConfiguration(Configuration configuration){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CONFIG, JsonUtils.serialize(configuration));
        return editor.commit();
    }

    /**获取配置文件*/
    public static Configuration getConfiguration(){
        String defaultConfig = JsonUtils.serialize(new Configuration());
        String configuration = mSharedPreferences.getString(CONFIG, defaultConfig);
        Configuration config = JsonUtils.deserialize(configuration, Configuration.class);
        if (config == null){
            config = new Configuration();
        }
        return config;
    }

    /**
     * 获取本地access_token
     * @return 本地token 可能为空
     */
    public static String getAccessToken(){
        return mSharedPreferences.getString(ACCESS_TOKEN, "");
    }

    /**判断当前用户是否登录
     * 从app服务器获取access_token，保存在sp中
     * 若本地没有该access_token，说明未登录
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(getAccessToken());
    }

    /**设置本地Token，用来将服务器返回的Token存储在本地
     * @param access_token 从服务器获取的access_token
     *@return boolean 返回是否保存成功
     */
    public static boolean setAccessToken(String access_token){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(ACCESS_TOKEN, access_token);
        return editor.commit();
    }

    /**获取聊天token*/
    public static String getTalkToken() {
        return mSharedPreferences.getString(USER_TALK_TOKEN,"");
        //return "xi8fxConE0KCfpBpJAp6fZyTH/Or+95OYVKiJ4mVVEw9eqUej5hl3tUYAVAN0C+H2xg/73VNHRfPqqhkVUTWuYZ5wypm3iYH";
    }

    /**存储用户是否开启自动挖矿,退出需清空*/
    public static boolean isDigFlag(boolean isDig){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(USER_STATE_DIG, isDig);
        return editor.commit();
    }

    /**获取用户自动挖矿状态*/
    public static boolean getDigFlag(){
        return mSharedPreferences.getBoolean(USER_STATE_DIG, false);
    }


    /**获取用户最新的算力等级*/
    public static int getCurrentLevel(){
        return mSharedPreferences.getInt(USER_FORCE_LEVLE, 0);
    }

    /**设置用户最新的算力等级到本地,退出需清空
     */
    public static boolean setCurrentLevel(int level){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(USER_FORCE_LEVLE, level);
        return editor.commit();
    }

    /**
     * 获取本地access_key
     * @return 本地key 可能为空
     */
    public static String getLocalKey(){
        return mSharedPreferences.getString(ACCESS_KEY, "");
    }

    /**设置本地加密字符串Key，用来向服务器请求时使用，每次登录重新存储刷新
     * 退出需清空
     */
    public static boolean setLocalKey(String accessKey){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACCESS_KEY, accessKey);
        return editor.commit();
    }

    /**退出需清空*/
    public static boolean setLocalUser(User user){
        if (user == null){
            user = new User();
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACCESS_TOKEN, user.getToken());
        editor.putBoolean(USER_STATE_DIG, user.isDigFlag());
        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_REALNAME, user.getUsername());
        editor.putString(USER_PHONE, user.getPhone());
        editor.putString(USER_LOGO, user.getHeadImg());
        editor.putString(USER_ALIPAY, user.getAlipayNum());
        editor.putBoolean(HAS_TRADEPWD, user.isOrderPwdFlag());
        editor.putBoolean(HAS_REALNAME, user.isIdCheckFlag());
        editor.putString(USER_WALLET_ADDRESS,user.getAddress());
        editor.putString(USER_TALK_TOKEN,user.getTalkToken());
        editor.putString(USER_BIRTHDAY,user.getBirthday());
        editor.putString(USER_UUID,user.getUuid());
        editor.putInt(USER_IDSTATUS,user.getIdStatus());
        editor.putInt(USER_REFERSTATUS,user.getReferStatus());
        editor.putInt(USER_SEX,user.getSex());
        editor.putInt(USER_MARKET,-1);
        Map<Integer, User.BindInfoModel> bindInfo = user.getBindInfo();
        if (bindInfo == null){
            bindInfo = new HashMap<>();
        }
        /*保存绑定的支付宝、微信、银行卡信息*/
        saveUserPay_Ali(bindInfo.get(StaticDatas.ALIPAY));
        saveUserPay_Wechart(bindInfo.get(StaticDatas.WECHART));
        saveUserPay_Card(bindInfo.get(StaticDatas.BANKCARD));
        return editor.commit();
    }

    /**将绑定的支付宝信息保存在本地*/
    public static boolean saveUserPay_Ali(User.BindInfoModel bindInfoModel){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_PAY_ALI, bindInfoModel==null?"":JsonUtils.serialize(bindInfoModel));
        return editor.commit();
    }
    /**将绑定的微信信息保存在本地*/
    public static boolean saveUserPay_Wechart(User.BindInfoModel bindInfoModel){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_PAY_WECHART,bindInfoModel==null?"":JsonUtils.serialize(bindInfoModel));
        return editor.commit();
    }
    /**将绑定的银行卡信息保存在本地*/
    public static boolean saveUserPay_Card(User.BindInfoModel bindInfoModel){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_PAY_CARD,bindInfoModel==null?"":JsonUtils.serialize(bindInfoModel));
        return editor.commit();
    }

    /**根据类型，获取支付信息*/
    public static User.BindInfoModel getUserPayInfo(int type){
        String payInfoStr = "";
        switch (type){
            case StaticDatas.ALIPAY:
                payInfoStr = mSharedPreferences.getString(USER_PAY_ALI,"");
                break;
            case StaticDatas.WECHART:
                payInfoStr = mSharedPreferences.getString(USER_PAY_WECHART,"");
                break;
            case StaticDatas.BANKCARD:
                payInfoStr = mSharedPreferences.getString(USER_PAY_CARD,"");
                break;
            default:break;
        }
        if (TextUtils.isEmpty(payInfoStr)){
            return null;
        }else{
            return JsonUtils.deserialize(payInfoStr, User.BindInfoModel.class);
        }
    }

    /**获取本地个人信息*/
    public static User getLocalUser(){
        User user = new User();
        user.setHeadImg(mSharedPreferences.getString(USER_LOGO,""));
        user.setName(mSharedPreferences.getString(USER_NAME,""));
        user.setUsername(mSharedPreferences.getString(USER_REALNAME,""));
        user.setPhone(mSharedPreferences.getString(USER_PHONE,""));
        user.setAlipayNum(mSharedPreferences.getString(USER_ALIPAY,""));
        user.setIdCheckFlag(mSharedPreferences.getBoolean(HAS_REALNAME,false));
        user.setOrderPwdFlag(mSharedPreferences.getBoolean(HAS_TRADEPWD,false));
        user.setAddress(mSharedPreferences.getString(USER_WALLET_ADDRESS,""));
        user.setBirthday(mSharedPreferences.getString(USER_BIRTHDAY,""));
        user.setUuid(mSharedPreferences.getString(USER_UUID,""));
        user.setSex(mSharedPreferences.getInt(USER_SEX,0));
        user.setIdStatus(mSharedPreferences.getInt(USER_IDSTATUS,0));
        user.setReferStatus(mSharedPreferences.getInt(USER_REFERSTATUS,0));
        return user;
    }

    /**将个人信息昵称缓存在本地*/
    public static boolean saveUserRealName(String realName){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_REALNAME,realName);
        return editor.commit();
    }
    public static String getUserRealName(){
        return mSharedPreferences.getString(USER_REALNAME,"");
    }

    public static boolean saveUserName(String nikeName){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_NAME,nikeName);
        return editor.commit();
    }
    public static String getUserName(){
        return mSharedPreferences.getString(USER_NAME,"");
    }


    /**将个人信息昵称缓存在本地*/
    public static boolean saveUserIdStatus(int id){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(USER_IDSTATUS,id);
        return editor.commit();
    }
    public static int getUserIdStatus(){
        return mSharedPreferences.getInt(USER_IDSTATUS, 0);
    }


    /**将个人信息手机号缓存在本地*/
    public static boolean saveUserPhone(String phone){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_PHONE,phone);
        return editor.commit();
    }

    /**设置logo*/
    public static boolean setUserLogo(String logoUrl){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_LOGO,logoUrl);
        return editor.commit();
    }

    /**设置是否已经设置了交易密码,退出需清空*/
    public static boolean setIsHasTradePwd(boolean isHasTradePwd){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(HAS_TRADEPWD,isHasTradePwd);
        return editor.commit();
    }

    /**设置实名认证boolean*/
    public static boolean setIsHasRealName(boolean isRealName){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(HAS_REALNAME,isRealName);
        return editor.commit();
    }

    /**设置绑定支付宝账号*/
    public static boolean setUserAliPayAccount(String aliPay){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_ALIPAY,aliPay);
        return editor.commit();
    }

    /**设置下载进度*/
    public static boolean setDownLoadApk(long refernece){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(DOWNLOAD_REFERNECE, refernece);
        return editor.commit();
    }

    /**获取下载进度*/
    public static Long getDownLoadApk(){
        return mSharedPreferences.getLong(DOWNLOAD_REFERNECE, 0);
    }


    public static int getUserMarket(){
        return mSharedPreferences.getInt(USER_MARKET,-1);
    }

    public static boolean setUserMarket(int flag){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(USER_MARKET, flag);
        return editor.commit();
    }
    /**
     * 判断是否显示欢迎
     * @return
     */
    public static boolean isShowWelcome() {
        try {
            PackageInfo info = mAppContext.getPackageManager().getPackageInfo(mAppContext.getPackageName(), 0);
            // 当前版本的版本号
            int versionCode = info.versionCode;
            int saveVersionCode = mSharedPreferences.getInt(VERSION_CODE, 0);
            LogUtils.w("version","当前版本号："+versionCode+",保存的版本号："+saveVersionCode);
            //版本不同时，显示欢迎界面,并更新版本
             if (versionCode != saveVersionCode){
                 updateVersionCode();
                 return true;
             }else {
                 return false;
             }
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }


    /**获取最近一次跳过的版本号*/
    public static boolean getLastJumpVersion(int version){
        int localVersionCode = getVersionCode();
        int lastJumpVersionCode = mSharedPreferences.getInt(LASTJUMPVERSIONCODE, localVersionCode);
        /*用本地版本号和存储的最新跳过的版本号较大的那个和服务器端的版本号进行比较,如果服务器端的大说明又有新版本了*/
        return version > Math.max(lastJumpVersionCode,localVersionCode);
//        return version > localVersionCode;
    }
    /**
     *设置最近一次跳过的版本号
     */
    public static boolean setLastJumpVersion(int version){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(LASTJUMPVERSIONCODE,version);
        return editor.commit();
    }
    /**
     * 保存本地的当前版本号
     */
    public static boolean updateVersionCode() {
        try {
            PackageInfo info = mAppContext.getPackageManager().getPackageInfo(mAppContext.getPackageName(), 0);
            // 当前版本的版本号
            int versionCode = info.versionCode;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(VERSION_CODE, versionCode);
            return editor.commit();
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * 获取DeviceId
     */
    public static String getDeviceId() {
        if (mDeviceId == null) {
            //mDeviceId = ((TelephonyManager) mAppContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            mDeviceId = android.os.Build.MODEL;
        }
        return TextUtils.isEmpty(mDeviceId)?"":mDeviceId;
    }

    /**
     * 获取本地的当前版本的名
     */
    public static String getVersionName() {
            try {
                PackageInfo info = mAppContext.getPackageManager().getPackageInfo(mAppContext.getPackageName(),0);
                // 当前应用的版本名称
                mVersionName = info.versionName;
            } catch (Exception e) {
                return "";
            }
        return mVersionName;
    }
    /**
     * 获取本地的当前版本的vVrsionCode
     */
    public static int getVersionCode() {
        int mVersionCode = 0;
        try {
            PackageInfo info = mAppContext.getPackageManager().getPackageInfo(mAppContext.getPackageName(),0);
            // 当前应用的版本名称
            mVersionCode = info.versionCode;
            // 当前版本的包名
            // String packageNames = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return mVersionCode;
    }

    /**
     * 清除用户的所有本地信息和key
     * @return
     */
    public static boolean clearAllUserData() {
        /*退出umeng用户统计*/
        MobclickAgent.onProfileSignOff();
//        RongIM.getInstance().logout();
        //BroadcastManager.getInstance(mAppContext).sendBroadcast(SealConst.EXIT);
        LogUtils.w("exit","用户退出");
        return setLocalUser(null) && setLocalKey("") && setLocalTradePwd("")&&setCurrentLevel(0);
    }

    /**
     * 从本地获取本地现货交易币
     */
    public static int getGoodsTradeCoin() {
        return mSharedPreferences.getInt(GOODS_COIN_TRADE, StaticDatas.COIN_ALL);
    }

    /**
     *将现货界面现货交易币存在本地
     */
    public static boolean setGoodsTradeCoin(int tradeCoin){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(GOODS_COIN_TRADE,tradeCoin);
        return editor.commit();
    }

    /**
     * 从本地获取本地现货计价币
     */
    public static int getGoodsPerCoin() {
        return mSharedPreferences.getInt(GOODS_COIN_PER, StaticDatas.COIN_ALL);
    }

    /**
     *将现货界面现货计价币存在本地
     */
    public static boolean setGoodsPerCoin(int perCoin){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(GOODS_COIN_PER,perCoin);
        return editor.commit();
    }

    /**
     * 从本地获取本地C2C币种
     */
    public static int getC2CCoin() {
        return mSharedPreferences.getInt(C2C_COIN, StaticDatas.COIN_ALL);
    }

    /**
     *将当前C2C币种存在本地
     */
    public static boolean setC2CCoin(int c2cCoin){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(C2C_COIN,c2cCoin);
        return editor.commit();
    }

    /**
     * 从本地获取本地抵押币种
     */
    public static int getDiyaCoin() {
        return mSharedPreferences.getInt(DIYA_COIN, StaticDatas.COIN_ALL);
    }

    /**
     *将当前抵押币种存在本地
     */
    public static boolean setDiyaCoin(int diyaCoin){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(DIYA_COIN,diyaCoin);
        return editor.commit();
    }

    /**
     *将时间差存在本地
     */
    public static boolean saveDeltaTime(int deltaTime){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(DELTA_TIME,deltaTime);
        return editor.commit();
    }

    /**
     * 从本地获取时间差
     */
    public static int getDeltaTime() {
        return mSharedPreferences.getInt(DELTA_TIME,0);
    }

    /**
     *将个人偏好，是否在仅在WiFi下观看视频
     */
    public static boolean setIsOnlyWifi(boolean isonly){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ONLY_WIFI,isonly);
        return editor.commit();
    }

    /**
     * 获取偏好设置
     */
    public static boolean isOnlyWifi() {
        return  mSharedPreferences.getBoolean(ONLY_WIFI, false);
    }


    /**设置是否有新委托数据
     * 当委托买入成功、卖出成功时，设置为true
     * 表示有新数据，则去刷新当前委托委托界面
     * */
    public static boolean setRefreshCurrentEntrust(boolean isHasNewData) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(CURRENT_ENTRUST_REFRESH, isHasNewData);
        return editor.commit();
    }

    /**获取是否有新委托数据
     * 当前委托委托界面获取
     * 如果为true则强制刷新
     * */
    public static boolean isRefreshCurrentEntrust() {
        return mSharedPreferences.getBoolean(CURRENT_ENTRUST_REFRESH,false);
    }

    /**设置是否有新历史委托数据
     * 当撤销成功、全部撤销成功时，设置为true
     * 表示有新数据，则去刷新历史委托界面
     * */
    public static boolean setRefreshHistoryEntrust(boolean isHasNewData) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(HISTORY_ENTRUST_REFRESH, isHasNewData);
        return editor.commit();
    }

    /**获取是否有新委托数据
     * 历史委托界面获取
     * 如果为true则强制刷新
     * */
    public static boolean isRefreshHistoryEntrust() {
        return mSharedPreferences.getBoolean(HISTORY_ENTRUST_REFRESH,false);
    }

    /**获取本地交易密码*/
    public static String getLocalTradePwd() {
      return mSharedPreferences.getString(TRADE_PWD,"");
    }

    /**设置交易密码,退出需清空*/
    public static boolean setLocalTradePwd(String pwd){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(TRADE_PWD, pwd);
        return editor.commit();
    }

    /**存储当前场景*/
    public static boolean setCurrentPushData(PushData pushData){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("pushData", JsonUtils.serialize(pushData));
        return editor.commit();
    }

    /**获取当前场景*/
    public static PushData getCurrentPushData(){
        String nullPushData = JsonUtils.serialize(new PushData());
        String currentPushData = mSharedPreferences.getString("pushData", nullPushData);
        PushData config = JsonUtils.deserialize(currentPushData, PushData.class);
        if (config == null){
            return null;
        }
        return config;
    }

    /**IM是否有新的好友请求*/
    public static boolean isHasNewFriend() {
        return mSharedPreferences.getBoolean("IM_HAS_FRIEND_REQUEST",false);
    }

    /**设置IM是否有新的好友请求*/
    public static boolean setHasNewFriend(boolean isHave){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("IM_HAS_FRIEND_REQUEST", isHave);
        return editor.commit();
    }

    /**获取是否在挖火蚁*/
    public static boolean isHYCDiging() {
        return mSharedPreferences.getBoolean("HYC_DIG_STATE",false);
    }

    /**设置是否在挖火蚁*/
    public static boolean setHYCDiging(boolean isDiging){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("HYC_DIG_STATE", isDiging);
        return editor.commit();
    }

    /**当点击底部现货时，为了接收买入界面的长连接，在GoodsFragment的onCreate时先设置为可见*/
    public static boolean setBuyFragmentVisible(boolean isVisible) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("GOODS_NUY_VISIBLE", isVisible);
        return editor.commit();
    }

    /**获取现货买入界面的对用户可见*/
    public static boolean isBuyFragmentVisible() {
        return mSharedPreferences.getBoolean("GOODS_NUY_VISIBLE",false);
    }


    /**
     * 从本地获取本地杠杆交易币
     */
    public static int getLeverTradeCoin() {
        return mSharedPreferences.getInt(LEVER_COIN_TRADE, StaticDatas.COIN_ALL);
    }

    /**
     *将杠杆界面现货交易币存在本地
     */
    public static boolean setLeverTradeCoin(int tradeCoin){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(LEVER_COIN_TRADE,tradeCoin);
        return editor.commit();
    }

    /**
     * 从本地获取本地杠杆计价币
     */
    public static int getLeverPerCoin() {
        return mSharedPreferences.getInt(LEVER_COIN_PER, StaticDatas.COIN_ALL);
    }

    /**
     *将杠杆界面现货计价币存在本地
     */
    public static boolean setLeverPerCoin(int perCoin){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(LEVER_COIN_PER,perCoin);
        return editor.commit();
    }

    public static boolean setLeverAgreement(boolean read){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LEVER_AGREEMENT_READED, read);
        return editor.commit();
    }

    public static boolean isLeverAgreement(){
        return mSharedPreferences.getBoolean(LEVER_AGREEMENT_READED,false);
    }

    public static boolean setBlockBgMusic(boolean open){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(BLOCK_BG_MUSIC, open);
        return editor.commit();
    }

    public static boolean isOpenBlockBgMusic(){
        return mSharedPreferences.getBoolean(BLOCK_BG_MUSIC,true);
    }

    public static boolean setYubibaoVisible(boolean visible){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(YUBIBAO_YUE_VISIBLE, visible);
        return editor.commit();
    }

    public static boolean isYubibaoVisible(){
        return mSharedPreferences.getBoolean(YUBIBAO_YUE_VISIBLE,true);
    }


    /**获取本地交易密码*/
    public static String getCookies() {
        return mSharedPreferences.getString(WEBVIEW_COOKIES,"");
    }

    /**设置交易密码,退出需清空*/
    public static boolean setCookies(String pwd){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(WEBVIEW_COOKIES, pwd);
        return editor.commit();
    }
}