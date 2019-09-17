package huoli.com.pgy.Constants;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:
 *
 * @author 徐庆重
 */
public class StaticDatas {

    /**列表请求数、起始页*/
    public static final int PAGE_SIZE = 20;
    public static final int PAGE_START = 0;
    /**火粒行情中的交易*/
    public static final int HLQUOTATIONS_TRANSACT_KN = 1;
    public static final int HLQUOTATIONS_TRANSACT_HLC = 2;
    public static final int HLQUOTATIONS_TRANSACT_BTC = 3;
    public static final int HLQUOTATIONS_TRANSACT_USDT = 4;

    /**全球行情中的交易*/
    public static final int QUOTATIONS_TRANSACT_SELF = 0;
    public static final int QUOTATIONS_TRANSACT_KN = 1;
    public static final int QUOTATIONS_TRANSACT_HLC = 2;
    public static final int QUOTATIONS_TRANSACT_BTC = 3;
    public static final int QUOTATIONS_TRANSACT_LTC = 4;
    public static final int QUOTATIONS_TRANSACT_ETH = 5;
    public static final int QUOTATIONS_TRANSACT_BTS = 6;

    public static final int LANGUAGE_CHINESE = 0;
    public static final int LANGUAGE_ENGLISH = 1;

    /**充值方式，支付宝或微信*/
    public static final int ALIPAY = 0;
    public static final int WECHART = 1;
    public static final int BANKCARD = 2;
    /**充值、提现、转账*/
    public static final int RECHARGE = 0;
    public static final int WITHDRAWAL = 1;
    public static final int TRANSFER = 2;

    /**转账方式 0：手机号 1：钱包地址*/
    /*public static final int TRANSFER_TYPE_PHONE = 0;
    public static final int TRANSFER_TYPE_ADDRESS = 1;*/

    /**行情 0COIN 1主流*/
    public static final int MARKET_ONECOIN = 0;
    public static final int MARKET_MAIN = 1;

    public static final int SYSTEMTYPE_ANDROID = 1;

    /**充值时，币种-1:全部 0:CNY 1:BTC 2:USDT 3:LTC 4:ETH 5:ETC 6:HLC 7:HYC*/
    public static final int COIN_ALL = -1;
    public static final int COIN_KN = 0;
    public static final int COIN_BTC = 1;
    public static final int COIN_USDT = 2;
    public static final int COIN_LTC = 3;
    public static final int COIN_ETH = 4;
    public static final int COIN_ETC = 5;
    public static final int COIN_HLC = 6;
    public static final int COIN_HYC = 7;

    /**现货列表，买卖*/
    public static final int BUY = 0;
    public static final int SALE = 1;

    /**限价OR市价*/
    public static final int TRADE_MARKET = 0;
    public static final int TRADE_LIMIT = 1;

    /**普通用户、商家*/
    public static final int NORMAL = 0;
    public static final int BUSINESS = 1;

    /**交易类型 0市价 1限价*/
    public static final int PRICE_MARKET = 0;
    public static final int PRICE_LIMIT = 1;

    /**最低提现金额、充值金额*/
    /*public static final double MIN_WITHDRAW_AMOUT = 100.00;
    public static final double MIN_RECHARGE_AMOUT = 100.00;*/

    /**各种动态页面url*/
    /*public static final String URL_EXCHANGERATE = "";   *//*费率*//*
    public static final String URL_RATEDETAILS = "";   *//*汇率*//*
    public static final String URL_CONTACTUS = "";      *//*联系我们*//*
    public static final String URL_COMPANYDES = "";     *//*公司介绍*//*
    public static final String URL_AGREENMENT = "";     *//*协议*//*
    public static final String URL_ALIPAYHELPER = "";     *//*支付宝订单号查询*/

    /**账户类型 0为C2C账户，1为现货账户，2为挖矿账户,4为余币宝账户*/
    public static final int ACCOUNT_C2C = 0;
    public static final int ACCOUNT_GOODS = 1;
    public static final int ACCOUNT_DIG = 2;
    public static final int ACCOUNT_LEVER = 3;//杠杆
    public static final int ACCOUNT_YUBIBAO = 4;


    /**挖矿账户资产提现类型*/
    public static final int MYASSETS_WITHDRAWAL_OTHER = 0;
    public static final int MYASSETS_WITHDRAWAL_GOODS = 1;

    /**群组类型 0：聊天室 1群组*/
    public static final int IMGROUP_CHATROOM = 0;
    public static final int IMGROUP_GROUP = 1;
}
