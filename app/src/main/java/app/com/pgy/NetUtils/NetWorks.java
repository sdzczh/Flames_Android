package app.com.pgy.NetUtils;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Constants.MyApplication;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.BannerInfo;
import app.com.pgy.Models.Beans.BindInfo;
import app.com.pgy.Models.Beans.BlockAssetFlow;
import app.com.pgy.Models.Beans.BlockCollection;
import app.com.pgy.Models.Beans.BlockNoticeBean;
import app.com.pgy.Models.Beans.BlockTradeInfo;
import app.com.pgy.Models.Beans.C2CBusinessCoinAvail;
import app.com.pgy.Models.Beans.C2CEntrustComplaintBean;
import app.com.pgy.Models.Beans.C2CEntrustDetails;
import app.com.pgy.Models.Beans.C2CPersonalMessage;
import app.com.pgy.Models.Beans.C2cBusinessEntrust;
import app.com.pgy.Models.Beans.C2cNormalBusiness;
import app.com.pgy.Models.Beans.C2cNormalEntrust;
import app.com.pgy.Models.Beans.CalculateDetail;
import app.com.pgy.Models.Beans.CoinAvailbalance;
import app.com.pgy.Models.Beans.CoinFlowDetail;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.DigPageInfo;
import app.com.pgy.Models.Beans.DigRecord;
import app.com.pgy.Models.Beans.DigResult;
import app.com.pgy.Models.Beans.Entrust;
import app.com.pgy.Models.Beans.EntrustDetails;
import app.com.pgy.Models.Beans.FeeRecord;
import app.com.pgy.Models.Beans.FinancialBean;
import app.com.pgy.Models.Beans.FinancialInfoBean;
import app.com.pgy.Models.Beans.ForceRankInfo;
import app.com.pgy.Models.Beans.ForceUpBean;
import app.com.pgy.Models.Beans.GoodsWallet;
import app.com.pgy.Models.Beans.GroupsInfo;
import app.com.pgy.Models.Beans.HomeInfo;
import app.com.pgy.Models.Beans.ImSystemMessage;
import app.com.pgy.Models.Beans.Invitation;
import app.com.pgy.Models.Beans.KLineBean;
import app.com.pgy.Models.Beans.LeverAvailBalanceBean;
import app.com.pgy.Models.Beans.LeverEntrust;
import app.com.pgy.Models.Beans.LeverEntrustDetails;
import app.com.pgy.Models.Beans.LeverManagerInfo;
import app.com.pgy.Models.Beans.LeverPagerInfo;
import app.com.pgy.Models.Beans.LeverRepaymentInfo;
import app.com.pgy.Models.Beans.LeverTransferOutbean;
import app.com.pgy.Models.Beans.LoanPageInfo;
import app.com.pgy.Models.Beans.MainDialogBean;
import app.com.pgy.Models.Beans.MuteResult;
import app.com.pgy.Models.Beans.MyAccount;
import app.com.pgy.Models.Beans.MyAssets;
import app.com.pgy.Models.Beans.Notice;
import app.com.pgy.Models.Beans.BankCard;
import app.com.pgy.Models.Beans.OdinHistoryRankBean;
import app.com.pgy.Models.Beans.OdinMyFollowBean;
import app.com.pgy.Models.Beans.OrderDetail;
import app.com.pgy.Models.Beans.OrderIdBean;
import app.com.pgy.Models.Beans.OrdinRewardInfo;
import app.com.pgy.Models.Beans.PushMarketBean;
import app.com.pgy.Models.Beans.RealNameResult;
import app.com.pgy.Models.Beans.RechargeBean;
import app.com.pgy.Models.Beans.RenZhengBean;
import app.com.pgy.Models.Beans.ResponseBean.LastDealBean;
import app.com.pgy.Models.Beans.ShareInfo;
import app.com.pgy.Models.Beans.StringBean;
import app.com.pgy.Models.Beans.StringNameBean;
import app.com.pgy.Models.Beans.TradeMessage;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.Models.Beans.MyWallet;
import app.com.pgy.Models.Beans.TransferOrder;
import app.com.pgy.Models.Beans.WithdrawBean;
import app.com.pgy.Models.Beans.YubibaoAvailbalance;
import app.com.pgy.Models.Beans.YubibaoFlow;
import app.com.pgy.Models.Beans.PushBean.SocketBlockList;
import app.com.pgy.Models.Beans.verficationCode;
import app.com.pgy.Models.Beans.version;
import app.com.pgy.Models.Beans.MyPersonal;
import app.com.pgy.Models.Beans.CircleBanner;
import app.com.pgy.Models.ListBean;
import app.com.pgy.Models.MyforceInfo;
import app.com.pgy.Models.ResultBean;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.ToolsUtils;
import app.com.pgy.Constants.ErrorHandler;
import app.com.pgy.im.server.response.GetFriendInfoByIDResponse;
import app.com.pgy.im.server.response.GetGroupDetailsResponse;
import app.com.pgy.im.server.response.GetGroupMemberResponse;
import app.com.pgy.im.server.response.GetGroupResponse;
import app.com.pgy.im.server.response.GetRedPacketStateResponse;
import app.com.pgy.im.server.response.GetSendRedPacketResponse;
import app.com.pgy.im.server.response.GetTransferStateResponse;
import app.com.pgy.im.server.response.GetUserInfoByPhoneResponse;
import app.com.pgy.im.server.response.GetUserInfosResponse;
import app.com.pgy.im.server.response.RedPackageRecord;
import app.com.pgy.im.server.response.UnReadMessageCount;
import app.com.pgy.im.server.response.UserRelationshipResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

import static app.com.pgy.Constants.ErrorHandler.RESPONSE_ERROR_ANDROID_UNLOGIN;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: Retrofit接口
 *
 * @author 徐庆重
 */

public class NetWorks extends RetrofitUtils {
    private static final String TAG = "NetWorks";
    //private  Context context = MyApplication.getInstance().getApplicationContext();
    /**
     * 创建实现接口调用
     */
    private static final NetService service = getRetrofit().create(NetService.class);

    //设缓存有效期
    private static final long CACHE_STALE_SEC = 60 * 5;
    /**查询缓存的Cache-Control设置，使用缓存*/
    //private static final String CACHE_CONTROL_CACHE = "Cache-Control: public, max-age=" + CACHE_STALE_SEC+"";
    /**
     * 查询网络的Cache-Control设置。不使用缓存
     */
    private static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=0";

    /**
     * 创建业务请求接口
     */
    public interface NetService {

        /**
         * 获取最新版本信息
         */
        @GET("/system/update.action")
        Call<ResultBean<version>> getLastVersion(@Query("params") String params);

        /**
         * 获取配置参数
         */
        @GET("/system/config.action")
        Call<ResultBean<Configuration>> getConfig(@Query("params") String params);

        /**
         * 获取验证码
         */
        @FormUrlEncoded
        @POST("/sms/smscode.action")
        Call<ResultBean<verficationCode>> getVerificationCode(@Field("params") String params);

        /**
         * 用户注册
         */
        @FormUrlEncoded
        @POST("/user/register.action")
        Call<ResultBean> register(@Field("params") String params, @Field("key") String key);
        /**
         * 校验推荐人id是否存在
         */
        @FormUrlEncoded
        @POST("/user/checkUuid.action")
        Call<ResultBean> checkUuid(@Field("params") String params);

        /**
         * 忘记密码
         */
        @FormUrlEncoded
        @POST("/pwd/login/getback.action")
        Call<ResultBean> forgotPwd(@Field("params") String params, @Field("key") String key);

        /**
         * 修改密码
         */
        @FormUrlEncoded
        @POST("/pwd/login/update.action")
        Call<ResultBean> updatePwd(@Header("token") String token, @Field("params") String params);

        /**
         * 设置交易密码
         */
        @FormUrlEncoded
        @POST("/pwd/order/set.action")
        Call<ResultBean> setWithdrawPwd(@Header("token") String token, @Field("params") String params);

        /**
         * 验证用户实名和交易密码状态
         */
        @FormUrlEncoded
        @POST("/order/checkRealNameAndOrderPassWord.action")
        Call<ResultBean> isOrderPwdFlag(@Header("token") String token, @Field("params") String params);

        /**
         * 用户登录
         */
        @FormUrlEncoded
        @POST("/user/login.action")
        Call<ResultBean<User>> login(@Field("params") String params, @Field("key") String key);

        /**
         * 用户验证码登录
         */
        @FormUrlEncoded
        @POST("/user/login/phone.action")
        Call<ResultBean<User>> loginByCode(@Field("params") String params, @Field("key") String key);

        /**
         * 充值提交
         */
        @FormUrlEncoded
        @POST("/wallets/recharge/apply")
        Call<ResultBean> submitRecharge(@Header("token") String token, @Field("params") String params);

        /**
         * 获取充值页信息
         *///
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/recharge/info.action")
        Call<ResultBean<RechargeBean>> getRecharge(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 充值订单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/recharges/v2/list.action")
        Call<ResultBean<OrderDetail>> getRechargeOrderList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 挖矿缴费列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/recharges/diglist.action")
        Call<ResultBean<FeeRecord>> getDigFeeRecordList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 挖矿付费
         */
        @FormUrlEncoded
        @POST("/recharges/dig.action")
        Call<ResultBean> digRecharge(@Header("token") String token, @Field("params") String params);


        /**
         * 转账提交
         */
        @FormUrlEncoded
        @POST("/transfers/apply.action")
        Call<ResultBean> submitTransfer(@Header("token") String token, @Field("params") String params);

        /**
         * 转账订单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/transfers/list.action")
        Call<ResultBean<TransferOrder>> getTransferOrderList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 提现页面信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/withDraw/info.action")
        Call<ResultBean<WithdrawBean>> getWithdrawBean(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 提现提交
         */
        @FormUrlEncoded
        @POST("/wallets/withDraw/apply.action")
        Call<ResultBean> submitWithdraw(@Header("token") String token, @Field("params") String params);

        /**
         * 提现订单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/withDraw/list.action")
        Call<ResultBean<BlockAssetFlow>> getBlockWithdrawOrderList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 钱包总览初始化
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/init.action")
        Call<ResultBean<MyAccount>> getMyWalletAccount(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 我的钱包列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/list.action")
        Call<ResultBean<MyWallet>> getMyWalletList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 币种余额,和人民币的兑换率
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/availBalance.action")
        Call<ResultBean<CoinAvailbalance>> getCoinAvailbalance(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 法币交易发布交易界面
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/c2c/maker/walletinfo.action")
        Call<ResultBean<C2CBusinessCoinAvail>> getCoinAvail(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 资金划转提交
         */
        @FormUrlEncoded
        @POST("/wallets/transfer.action")
        Call<ResultBean> submitAssetsTransfer(@Header("token") String token, @Field("params") String params);

        /**
         * 我的现货余额，加入请求缓存
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/wallet.action")
        Call<ResultBean<GoodsWallet>> getMyGoodsWallet(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 现货交易--获取交易界面场景信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/order/mainpageInfo.action")
        Call<ResultBean<TradeMessage>> getTradeMessage(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);


        /**
         * 公告列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/notice/list.action")
        Call<ResultBean<Notice>> getNoticeList(@Query("params") String params);

        /**
         * 公告列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/market/marketInit.action")
        Call<ResultBean<PushMarketBean>> getMarketData(@Query("params") String params);

        /**
         * im系统通知列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/msg/systemNoticeList.action")
        Call<ResultBean<ImSystemMessage>> getImSystemMessageList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 设置头像
         */
        @FormUrlEncoded
        @POST("/user/headimg.action")
        Call<ResultBean> setUserIcon(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 限价买入
         */
        @FormUrlEncoded
        @POST("/order/limitPriceBuy.action")
        Call<ResultBean> limitPriceBuy(@Header("token") String token, @Field("params") String params);

        /**
         * 市价买入
         */
        @FormUrlEncoded
        @POST("/order/marketPriceBuy.action")
        Call<ResultBean> marketPriceBuy(@Header("token") String token, @Field("params") String params);

        /**
         * 限价卖出
         */
        @FormUrlEncoded
        @POST("/order/limitPriceSale.action")
        Call<ResultBean> limitPriceSale(@Header("token") String token, @Field("params") String params);

        /**
         * 市价卖出
         */
        @FormUrlEncoded
        @POST("/order/marketPriceSale.action")
        Call<ResultBean> marketPriceSale(@Header("token") String token, @Field("params") String params);

        /**
         * 我的委托单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/order/orderRecord.action")
        Call<ResultBean<Entrust>> getMyEntrustList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 最新成交列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/orders/last.action")
        Call<ResultBean<LastDealBean>> getLastDealList(@Query("params") String params, @Query("sign") String sign);


        /**
         * 现货一键撤销
         */
        @FormUrlEncoded
        @POST("/orders/cancelall.action")
        Call<ResultBean> canceledAllEntrust(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 现货单个撤销
         */
        @FormUrlEncoded
        @POST("/order/orderCancel.action")
        Call<ResultBean> canceledOneEntrust(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 现货交易订单详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/order/orderDetail.action")
        Call<ResultBean<EntrustDetails>> getEntrustDetails(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 获取C2C用户详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/c2c/userinfo.action")
        Call<ResultBean<C2CPersonalMessage>> getC2CPersonalBusiness(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 充值订单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/dig.action")
        Call<ResultBean> getDigPermission(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 获取实名认证token
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/realname/init.action")
        Call<ResultBean<RealNameResult>> getRealNameToken(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 获取实名认证状态
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/realname/status.action")
        Call<ResultBean<StringNameBean>> getRealNameStatus(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        @Multipart
        @POST("/file/upload.action")
        Call<ResultBean<StringBean>> uploadImage(@Part("fileDir") RequestBody description, @Part() MultipartBody.Part file);

        /**
         * 获取未登录的出矿信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/info/notlogged.action")
        Call<ResultBean<SocketBlockList>> getUnLoggedBlock(@Query("params") String params);

        /**
         * 初始化登录后的挖矿信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/info/logged.action")
        Call<ResultBean<SocketBlockList>> getLoggedBlock(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 上传log
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/system/log.action")
        Call<ResultBean> uploadLog(@Query("params") String params);

        /**
         * C2C商家发布交易
         */
        @FormUrlEncoded
        @POST("/c2c/maker/order.action")
        Call<ResultBean> c2cPublishTrade(@Header("token") String token, @Field("params") String params);

        /**
         * C2C商家委托单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/c2c/maker/list.action")
        Call<ResultBean<C2cBusinessEntrust>> getC2CBusinessEntrustList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * C2C商家委托单个撤销
         */
        @FormUrlEncoded
        @POST("/c2c/maker/cancel.action")
        Call<ResultBean> cancelC2CBusinessEntrust(@Header("token") String token, @Field("params") String params);

        /**
         * C2C商家委托单个开始接单或取消接单
         */
        @FormUrlEncoded
        @POST("/c2c/maker/receipt/one.action")
        Call<ResultBean> receiptC2CBusinessEntrust(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * C2C商家委托全部开始接单或取消接单
         */
        @FormUrlEncoded
        @POST("/c2c/maker/receipt/all.action")
        Call<ResultBean> receiptAllC2CBusinessEntrust(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * C2C普通用户商家列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/c2c/makerlist.action")
        Call<ResultBean<C2cNormalBusiness>> getC2CNormalBusinessEntrustList(@Query("params") String params);

        /**
         * C2C普通用户买入
         */
        @FormUrlEncoded
        @POST("/c2c/taker/buy.action")
        Call<ResultBean<OrderIdBean>> c2cNormalBuy(@Header("token") String token, @Field("params") String params);

        /**
         * C2C普通用户卖出
         */
        @FormUrlEncoded
        @POST("/c2c/taker/sale.action")
        Call<ResultBean<OrderIdBean>> c2cNormalSale(@Header("token") String token, @Field("params") String params);

        /**
         * 订单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/c2c/taker/orderlist.action")
        Call<ResultBean<C2cNormalEntrust>> getC2CEntrustList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 绑定支付宝
         */
        @FormUrlEncoded
        @POST("/user/bindalipay.action")
        Call<ResultBean> bindAliPay(@Header("token") String token, @Field("params") String params);

        /**
         * 绑定微信
         */
        @FormUrlEncoded
        @POST("/user/bindwechat.action")
        Call<ResultBean> bindWxPay(@Header("token") String token, @Field("params") String params);

        /**
         * 绑定银行卡
         */
        @FormUrlEncoded
        @POST("/user/bindcard.action")
        Call<ResultBean> bindCard(@Header("token") String token, @Field("params") String params);

        /**
         * 银行卡列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/bank/list.action")
        Call<ResultBean<BankCard>> getBankCardList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 获取c2c订单详情(改)
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/c2c/taker/orderinfo.action")
        Call<ResultBean<C2CEntrustDetails>> getC2CEntrustDetails(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * c2c详情页确认支付
         */
        @FormUrlEncoded
        @POST("/c2c/taker/confirm-pay.action")
        Call<ResultBean> c2cEntrustConfirmPayed(@Header("token") String token, @Field("params") String params);

        /**
         * c2c详情页确认收款，放币
         */
        @FormUrlEncoded
        @POST("/c2c/taker/confirm-receipt.action")
        Call<ResultBean> c2cEntrustConfirmReceipt(@Header("token") String token, @Field("params") String params);

        /**
         * 取消订单
         */
        @FormUrlEncoded
        @POST("/c2c/taker/cancel.action")
        Call<ResultBean> c2cEntrustCancelOrder(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 申诉初始化
         */
        @FormUrlEncoded
        @POST("/c2c/appealInfo.action")
        Call<ResultBean<C2CEntrustComplaintBean>> c2cEntrustComplaint(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 申诉客服
         */
        @FormUrlEncoded
        @POST("/c2c/taker/appeal.action")
        Call<ResultBean> c2cEntrustAppeal(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 我的邀请信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/commissions/commission.action")
        Call<ResultBean<Invitation>> getMyInvitation(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 挖矿列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/list.action")
        Call<ResultBean<DigRecord>> getDigRecordList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 我的算力详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/soul/detail.action")
        Call<ResultBean<MyforceInfo>> getMyCalculateForce(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);
        /**
         * 我的算力详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/soul/rank.action")
        Call<ResultBean<ForceRankInfo>> getForceRank(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 领取算力奖励
         */
        @FormUrlEncoded
        @POST("/dig/soul/sign.action")
        Call<ResultBean> getCalculateForceGift(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);
        /**
         * 算力流水
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/soul/flow.action")
        Call<ResultBean<CalculateDetail>> getCalculateDetailList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);
        /**
         * 我的挖矿资产列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/list.action")
        Call<ResultBean<MyAssets>> getMyAssetsList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 挖矿资产提现提交
         */
        @FormUrlEncoded
        @POST("/dig/withdraw.action")
        Call<ResultBean> submitMyAssetsWithdraw(@Header("token") String token, @Field("params") String params);

        /**
         * 自动挖矿
         */
        @FormUrlEncoded
        @POST("/dig/dig.action")
        Call<ResultBean<DigResult>> autoDig(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 收矿
         */
        @FormUrlEncoded
        @POST("/dig/collect.action")
        Call<ResultBean<BlockCollection>> getBlock(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);
        /**
         * 交易挖矿
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/order/dealDigRecordList.action")
        Call<ResultBean<BlockTradeInfo>> getBlockTrade(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);
        /**
         * 单个币种详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/accountDetails.action")
        Call<ResultBean<CoinFlowDetail>> getCoinDetail(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * k线图数据
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/bank/list.action")
        Call<ResultBean<KLineBean>> getKLineDatas(@Query("params") String params, @Query("sign") String sign);

        /**
         * 挖矿算力奖励活动页
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/soul/pageInfo.action")
        Call<ResultBean<ForceUpBean>> getForceUpData(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 查看是否可领取活动奖励算力
         */
        @FormUrlEncoded
        @POST("/dig/soul/share/result.action")
        Call<ResultBean> submitForceShare(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 获取挖矿主页场景信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/digPageInfo.action")
        Call<ResultBean<DigPageInfo>> getDigPageInfo(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /***
         * 大文件下载
         * @param fileUrl
         * @return
         */
        @Streaming
        @GET
        Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);

        /**
         * 获取最新公告
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/dig/getNewAnnouncement.action")
        Call<ResultBean<BlockNoticeBean>> getNewAnnouncement();


        /**
         * 获取挖矿主页场景信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/system/notice.action")
        Call<ResultBean<MainDialogBean>> getMainDialog();

        /**
         * 查找好友
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/friends/find.action")
        Call<ResultBean<GetUserInfoByPhoneResponse>> findIMFriend(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 获取im中用户信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/friends/detail.action")
        Call<ResultBean<GetFriendInfoByIDResponse>> getIMUserDetail(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 添加好友
         */
        @FormUrlEncoded
        @POST("/talk/friends/add.action")
        Call<ResultBean> addIMFriend(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 删除好友
         */
        @FormUrlEncoded
        @POST("/talk/friends/delete.action")
        Call<ResultBean> deleteIMFriend(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 群组聊天室列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/group/list.action ")
        Call<ResultBean<GetGroupResponse>> getChatRoomList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 进入群组聊天室
         */
        @FormUrlEncoded
        @POST("/talk/group/join.action")
        Call<ResultBean> joinInGroup(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 退出群组聊天室
         */
        @FormUrlEncoded
        @POST("/talk/group/leave.action")
        Call<ResultBean> exitGroup(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 群组聊天室列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/friends/addlist.action")
        Call<ResultBean<UserRelationshipResponse>> getNewFriendList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 退出群组聊天室
         */
        @FormUrlEncoded
        @POST("/talk/friends/check.action")
        Call<ResultBean> checkImFriend(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 群组聊天室详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/group/detail.action")
        Call<ResultBean<GetGroupDetailsResponse>> getChatRoomDetails(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 群组聊天室列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/group/users.action")
        Call<ResultBean<GetGroupMemberResponse>> getChatRoomMembers(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 取消订单
         */
        @FormUrlEncoded
        @POST("/talk/friends/remarkname/update.action")
        Call<ResultBean> updateIMFriendRemarkName(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 我的好友列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/friends/list.action")
        Call<ResultBean<GetUserInfosResponse>> getMyFriendList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 获取im中红包详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/redpacket/detail.action")
        Call<ResultBean<GetRedPacketStateResponse>> getRedPacketDetail(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 获取im中转账详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/transfer/detail.action")
        Call<ResultBean<GetTransferStateResponse>> getTransferDetail(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);


        /**
         * 发送红包
         */
        @FormUrlEncoded
        @POST("/talk/redpacket/send.action")
        Call<ResultBean<GetSendRedPacketResponse>> sendRedPacket(@Header("token") String token, @Field("params") String params);

        /**
         * 修改昵称
         */
        @FormUrlEncoded
        @POST("/user/nickname/update.action")
        Call<ResultBean> changeNickName(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 领取红包
         */
        @FormUrlEncoded
        @POST("/talk/redpacket/recive.action")
        Call<ResultBean<GetRedPacketStateResponse>> receiveRedPacket(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * IM红包记录
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/redpacket/list.action")
        Call<ResultBean<RedPackageRecord>> getRedPacketRecordList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * IM转账记录
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/transfer/list.action")
        Call<ResultBean<RedPackageRecord>> getTransferRecordList(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 消息免打扰
         */
        @FormUrlEncoded
        @POST("/talk/group/mute.action")
        Call<ResultBean<MuteResult>> imTalkMute(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);


        /**
         * 发送转账
         */
        @FormUrlEncoded
        @POST("/talk/transfer/send.action")
        Call<ResultBean<GetSendRedPacketResponse>> sendTransfer(@Header("token") String token, @Field("params") String params);

        /**
         * I系统消息、交易通知未读消息数
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/talk/menu/msgMenu.action")
        Call<ResultBean<UnReadMessageCount>> getIMMessageCount(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 提现订单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/yubi/flows.action")
        Call<ResultBean<YubibaoFlow>> getYubiBaoFlows(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 余币宝资金划转提交
         */
        @FormUrlEncoded
        @POST("/yubi/transfer.action ")
        Call<ResultBean> submitYubibaoTransfer(@Header("token") String token, @Field("params") String params);

        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/yubi/balance.action")
        Call<ResultBean<YubibaoAvailbalance>> getYubibaoAvailbalance(@Header("token") String token, @Query("params") String params, @Query("sign") String sign);

        /**
         * 杠杆交易交易界面信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/getTradePageInfo.action")
        Call<ResultBean<LeverPagerInfo>> getLeverPageInf(@Header("token") String token, @Query("params") String params);

        /**
         * 杠杆交易买入
         */
        @FormUrlEncoded
        @POST("/leverage/limitbuy.action")
        Call<ResultBean> leverGoodsBuy(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 杠杆交易卖出
         */
        @FormUrlEncoded
        @POST("/leverage/limitsale.action")
        Call<ResultBean> leverGoodsSale(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 杠杆交易--委托单列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/orderList.action")
        Call<ResultBean<LeverEntrust>> getLeverEntrustList(@Header("token") String token, @Query("params") String params);

        /**
         * 杠杆交易--撤销全部委托单
         */
        @FormUrlEncoded
        @POST("/leverage/cancelall.action")
        Call<ResultBean> canceledAllLeverEntrust(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 杠杆交易--撤销单个委托单
         */
        @FormUrlEncoded
        @POST("/leverage/cancelone.action")
        Call<ResultBean> canceledOneLeverEntrust(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 杠杆交易--委托单详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/orderDetail.action")
        Call<ResultBean<LeverEntrustDetails>> getLeverEntrustDetails(@Query("params") String params);

        /**
         * 杠杠--交易管理页面信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/getManagePageInfo.action")
        Call<ResultBean<LeverManagerInfo>> getLeverManagerInfo(@Header("token") String token, @Query("params") String params);

        /**
         * 杠杠--获取用户某钱包某币种可用数量
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/getAvailBalance.action")
        Call<ResultBean<LeverAvailBalanceBean>> getLeverAvasilBalance(@Header("token") String token, @Query("params") String params);

        /**
         * 杠杆交易--资产转入
         */
        @FormUrlEncoded
        @POST("/leverage/transferToLev.action")
        Call<ResultBean> leverTransferIn(@Header("token") String token, @Field("params") String params);

        /**
         * 杠杠--获取用户某钱包某币种可用数量
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/lendPageInfo.action")
        Call<ResultBean<LoanPageInfo>> getLoanPageInfo(@Header("token") String token, @Query("params") String params);

        /**
         * 杠杆交易--借款
         */
        @FormUrlEncoded
        @POST("/leverage/lend.action")
        Call<ResultBean> leverLoan(@Header("token") String token, @Field("params") String params);

        /**
         * 杠杆交易--查询还款记录
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/getRepaymentRecord.action")
        Call<ResultBean<LeverRepaymentInfo>> getLeverRepaymentList(@Header("token") String token, @Query("params") String params);

        /**
         * 杠杆交易--还款记录详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/getRepaymentDetail.action")
        Call<ResultBean<LeverRepaymentInfo.ListBean>> getLeverRepaymentInfo(@Query("params") String params);

        /**
         * 杠杆交易--还款
         */
        @FormUrlEncoded
        @POST("/leverage/repay.action")
        Call<ResultBean> leverRepayment(@Header("token") String token, @Field("params") String params);

        /**
         * 杠杆交易--转出
         */
        @FormUrlEncoded
        @POST("/leverage/transferOut.action")
        Call<ResultBean> leverTransferOut(@Header("token") String token, @Field("params") String params);

        /**
         * 杠杆交易--查询杠杆账户可转出数量
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/leverage/getLevTranferAvail.action")
        Call<ResultBean<LeverTransferOutbean>> getLeverTransferOutBean(@Header("token") String token, @Query("params") String params);

        /**
         * 财务中心--首页资产信息
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/center.action")
        Call<ResultBean<FinancialInfoBean>> getFinancialCenterInfo(@Header("token") String token, @Query("params") String params);
        /**
         * 财务中心--c2c钱包详情
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/wallets/c2cDetail.action")
        Call<ResultBean<FinancialBean>> getFinancialC2CList(@Header("token") String token, @Query("params") String params);

        /**
         * 获取加入社群列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/system/aboutInfo.action")
        Call<ResultBean<GroupsInfo>> getGroupList(@Query("params") String params);
        /**
         * 首页（未登录）
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/homePage/out/init.action")
        Call<ResultBean<HomeInfo>> getHomeInfoUnLogin(@Query("params") String params);
        /**
         *首页（已登录）
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/homePage/in/init.action")
        Call<ResultBean<HomeInfo>> getHomeInfoLogin(@Header("token") String token,@Query("params") String params);
        /**
         * 获取个人信息性别和算力
         */
        @FormUrlEncoded
        @POST("/user/getCalcul.action")
        Call<ResultBean<MyPersonal>> getPersionalInfo(@Header("token") String token, @Field("params") String params);

        /**
         * 获取邀请海报
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/system/poster.action")
        Call<ResultBean<ShareInfo>> getPosters(@Header("token") String token,@Query("params") String params);

        /**
         *Banner
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/homePage/banner.action")
        Call<ResultBean<CircleBanner>> getCircleBanner(@Query("params") String params);

        /**
         * 邀请奖励页面初始化
         */
        @FormUrlEncoded
        @POST("/odingbuying/reward.action")
        Call<ResultBean<OrdinRewardInfo>> getOdingRewardInfo(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 邀请页面--更多历史排行
         */
        @FormUrlEncoded
        @POST("/odingbuying/moreRank.action")
        Call<ResultBean<List<OdinHistoryRankBean>>> getOdinHistoryRankList(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);

        /**
         * 邀请页面--个人推广记录
         */
        @FormUrlEncoded
        @POST("/odingbuying/inviteList.action")
        Call<ResultBean<List<OdinMyFollowBean>>> getOdinMyFollowList(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);


        /**
         * 立即购买
         */
        @FormUrlEncoded
        @POST("/odingbuying/buy.action")
        Call<ResultBean> buyOdin(@Header("token") String token, @Field("params") String params, @Field("sign") String sign);


        /**
         * 提取冻结资产（加密方式同资金划转）
         */
        @FormUrlEncoded
        @POST("/yubi/withdrawFrozen.action")
        Call<ResultBean> submitWithdrawFrozen(@Header("token") String token, @Field("params") String params);


        /**
         * 修改市场情绪（每人每天只能选择一个 点击一次 点击后按钮失效）
         */
        @FormUrlEncoded
        @POST("/homePage/changeMood.action")
        Call<ResultBean<HomeInfo.MoodBean>> submitChangeMarket(@Header("token") String token, @Field("params") String params);


        /**
         * 身份认证页面信息初始化
         */
        @FormUrlEncoded
        @POST("/realname/getInfo.action")
        Call<ResultBean<RenZhengBean>> initRenZheng(@Header("token") String token, @Field("params") String params);


        /**
         * 获取banner列表
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/homePage/banner.action")
        Call<ResultBean<ListBean<BannerInfo>>> getBannerList(@Query("params") String params);

        /**
         * 获取用户绑定收款信息
         */
        @FormUrlEncoded
        @POST("/user/getBindInfo.action")
        Call<ResultBean<List<BindInfo>>> getBindList(@Header("token") String token, @Field("params") String params);

        /**
         * 获取二维码内容
         */
        @Headers(CACHE_CONTROL_NETWORK)
        @GET("/web/downloadInfo.action")
        Call<ResultBean<String>> getShareQrCode(@Query("params") String params);
    }

    /**
     * 获取最新版本
     */
    public static void getConfig(Map<String, Object> maps, final getBeanCallback<Configuration> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<Configuration>> resultBeanCall = service.getConfig(params);
        resultBeanCall.enqueue(new Callback<ResultBean<Configuration>>() {
            @Override
            public void onResponse(Call<ResultBean<Configuration>> call, Response<ResultBean<Configuration>> response) {
                ResultBean<Configuration> responseBody = response.body();
                setResponse(Configuration.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<Configuration>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取最新版本
     */
    public static void getLastVersion(Map<String, Object> maps, final getBeanCallback<version> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<version>> resultBeanCall = service.getLastVersion(params);
        resultBeanCall.enqueue(new Callback<ResultBean<version>>() {
            @Override
            public void onResponse(Call<ResultBean<version>> call, Response<ResultBean<version>> response) {
                ResultBean<version> responseBody = response.body();
                setResponse(version.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<version>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取验证码
     */
    public static void getVerificationCode(Map<String, Object> maps, final getBeanCallback<verficationCode> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<verficationCode>> resultBeanCall = service.getVerificationCode(params);
        resultBeanCall.enqueue(new Callback<ResultBean<verficationCode>>() {
            @Override
            public void onResponse(Call<ResultBean<verficationCode>> call, Response<ResultBean<verficationCode>> response) {
                ResultBean<verficationCode> responseBody = response.body();
                setResponse(verficationCode.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<verficationCode>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 用户注册
     */
    public static void register(Map<String, Object> maps, final getBeanCallback callback) {
        String myKey = ToolsUtils.getMyLoginKey();
        String upLoadKey = ToolsUtils.getUpLoadKey(myKey);
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.register(params, upLoadKey);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 校验推荐人id是否存在
     */
    public static void checkUuid(Map<String, Object> maps, final getBeanCallback callback) {
        String params = ToolsUtils.getBase64Params(maps);

        Call<ResultBean> resultBeanCall = service.checkUuid(params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 用户忘记密码
     */
    public static void forgotPwd(Map<String, Object> maps, final getBeanCallback callback) {
        String myKey = ToolsUtils.getMyLoginKey();
        String upLoadKey = ToolsUtils.getUpLoadKey(myKey);
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.forgotPwd(params, upLoadKey);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 用户修改密码
     */
    public static void updatePwd(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.updatePwd(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 用户设置交易密码
     */
    public static void setWithdrawPwd(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        //Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.setWithdrawPwd(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 验证用户实名和交易密码状态
     */
    public static void isOrderPwdFlag(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean> resultBeanCall = service.isOrderPwdFlag(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 用户登录
     */
    public static void login(Map<String, Object> maps, final getBeanCallback<User> callback) {
        String myKey = ToolsUtils.getMyLoginKey();
        String upLoadKey = ToolsUtils.getUpLoadKey(myKey);
        String params = ToolsUtils.getAESParams(maps, myKey);

        Preferences.init(MyApplication.getInstance().getApplicationContext());
        Preferences.setLocalKey(myKey);
        Call<ResultBean<User>> resultBeanCall = service.login(params, upLoadKey);
        resultBeanCall.enqueue(new Callback<ResultBean<User>>() {
            @Override
            public void onResponse(Call<ResultBean<User>> call, Response<ResultBean<User>> response) {
                ResultBean<User> responseBody = response.body();
                setResponse(User.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<User>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 用户验证码登录
     */
    public static void loginByCode(Map<String, Object> maps, final getBeanCallback<User> callback) {
        String myKey = ToolsUtils.getMyLoginKey();
        String upLoadKey = ToolsUtils.getUpLoadKey(myKey);
        String params = ToolsUtils.getAESParams(maps, myKey);

        Preferences.init(MyApplication.getInstance().getApplicationContext());
        Preferences.setLocalKey(myKey);
        Call<ResultBean<User>> resultBeanCall = service.loginByCode(params, upLoadKey);
        resultBeanCall.enqueue(new Callback<ResultBean<User>>() {
            @Override
            public void onResponse(Call<ResultBean<User>> call, Response<ResultBean<User>> response) {
                ResultBean<User> responseBody = response.body();
                setResponse(User.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<User>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 充值提交
     */
    public static void submitRecharge(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
//        String params = ToolsUtils.getBase64Params(maps);
//        String sign = ToolsUtils.getUploadSign(maps);
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean> resultBeanCall = service.submitRecharge(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 充值提交
     */
    public static void getRecharge(String token, Map<String, Object> maps, final getBeanCallback<RechargeBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<RechargeBean>> resultBeanCall = service.getRecharge(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<RechargeBean>>() {
            @Override
            public void onResponse(Call<ResultBean<RechargeBean>> call, Response<ResultBean<RechargeBean>> response) {
                ResultBean<RechargeBean> responseBody = response.body();
                setResponse(RechargeBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<RechargeBean>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取用户充值订单列表
     */
    public static void getRechargeOrderList(String token, Map<String, Object> maps, final getBeanCallback<OrderDetail> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<OrderDetail>> resultBeanCall = service.getRechargeOrderList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<OrderDetail>>() {
            @Override
            public void onResponse(Call<ResultBean<OrderDetail>> call, Response<ResultBean<OrderDetail>> response) {
                ResultBean<OrderDetail> responseBody = response.body();
                setResponse(OrderDetail.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<OrderDetail>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取用户挖火蚁充值列表
     */
    public static void getDigFeeRecordList(String token, Map<String, Object> maps, final getBeanCallback<FeeRecord> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<FeeRecord>> resultBeanCall = service.getDigFeeRecordList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<FeeRecord>>() {
            @Override
            public void onResponse(Call<ResultBean<FeeRecord>> call, Response<ResultBean<FeeRecord>> response) {
                ResultBean<FeeRecord> responseBody = response.body();
                setResponse(FeeRecord.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<FeeRecord>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 挖矿充值
     */
    public static void digRecharge(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean> resultBeanCall = service.digRecharge(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 转账提交
     */
    public static void submitTransfer(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean> resultBeanCall = service.submitTransfer(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取用户转账订单列表
     */
    public static void getTransferOrderList(String token, Map<String, Object> maps, final getBeanCallback<TransferOrder> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<TransferOrder>> resultBeanCall = service.getTransferOrderList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<TransferOrder>>() {
            @Override
            public void onResponse(Call<ResultBean<TransferOrder>> call, Response<ResultBean<TransferOrder>> response) {
                ResultBean<TransferOrder> responseBody = response.body();
                setResponse(TransferOrder.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<TransferOrder>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 提现页面信息
     */
    public static void getWithdrawBean(String token, Map<String, Object> maps, final getBeanCallback<WithdrawBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<WithdrawBean>> resultBeanCall = service.getWithdrawBean(token, params,sign);
        resultBeanCall.enqueue(new Callback<ResultBean<WithdrawBean>>() {
            @Override
            public void onResponse(Call<ResultBean<WithdrawBean>> call, Response<ResultBean<WithdrawBean>> response) {
                ResultBean<WithdrawBean> responseBody = response.body();
                setResponse(WithdrawBean.class,responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<WithdrawBean>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 提现提交
     */
    public static void submitWithdraw(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean> resultBeanCall = service.submitWithdraw(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 获取用户提现订单列表
     */
    public static void getBlockWithdrawOrderList(String token, Map<String, Object> maps, final getBeanCallback<BlockAssetFlow> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<BlockAssetFlow>> resultBeanCall = service.getBlockWithdrawOrderList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<BlockAssetFlow>>() {
            @Override
            public void onResponse(Call<ResultBean<BlockAssetFlow>> call, Response<ResultBean<BlockAssetFlow>> response) {
                ResultBean<BlockAssetFlow> responseBody = response.body();
                setResponse(BlockAssetFlow.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<BlockAssetFlow>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取余币宝流水
     */
    public static void getYubiBaoFlows(String token, Map<String, Object> maps, final getBeanCallback<YubibaoFlow> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<YubibaoFlow>> resultBeanCall = service.getYubiBaoFlows(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<YubibaoFlow>>() {
            @Override
            public void onResponse(Call<ResultBean<YubibaoFlow>> call, Response<ResultBean<YubibaoFlow>> response) {
                ResultBean<YubibaoFlow> responseBody = response.body();
                setResponse(YubibaoFlow.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<YubibaoFlow>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 获取用户钱包初始化
     */
    public static void getMyWalletAccount(String token, Map<String, Object> maps, final getBeanCallback<MyAccount> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<MyAccount>> resultBeanCall = service.getMyWalletAccount(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<MyAccount>>() {
            @Override
            public void onResponse(Call<ResultBean<MyAccount>> call, Response<ResultBean<MyAccount>> response) {
                ResultBean<MyAccount> responseBody = response.body();
                setResponse(MyAccount.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<MyAccount>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取用户钱包列表
     */
    public static void getMyWalletList(String token, Map<String, Object> maps, final getBeanCallback<MyWallet> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<MyWallet>> resultBeanCall = service.getMyWalletList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<MyWallet>>() {
            @Override
            public void onResponse(Call<ResultBean<MyWallet>> call, Response<ResultBean<MyWallet>> response) {
                ResultBean<MyWallet> responseBody = response.body();
                setResponse(MyWallet.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<MyWallet>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取用户货币余额
     */
    public static void getCoinAvailbalance(String token, Map<String, Object> maps, final getBeanCallback<CoinAvailbalance> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<CoinAvailbalance>> resultBeanCall = service.getCoinAvailbalance(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<CoinAvailbalance>>() {
            @Override
            public void onResponse(Call<ResultBean<CoinAvailbalance>> call, Response<ResultBean<CoinAvailbalance>> response) {
                ResultBean<CoinAvailbalance> responseBody = response.body();
                setResponse(CoinAvailbalance.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<CoinAvailbalance>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**法币交易发布交易界面*/
    public static void getCoinAvail(String token, Map<String, Object> maps, final getBeanCallback<C2CBusinessCoinAvail> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<C2CBusinessCoinAvail>> resultBeanCall = service.getCoinAvail(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<C2CBusinessCoinAvail>>() {
            @Override
            public void onResponse(Call<ResultBean<C2CBusinessCoinAvail>> call, Response<ResultBean<C2CBusinessCoinAvail>> response) {
                ResultBean<C2CBusinessCoinAvail> responseBody = response.body();
                setResponse(C2CBusinessCoinAvail.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<C2CBusinessCoinAvail>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取余币宝余额
     */
    public static void getYubibaoAvailbalance(String token, Map<String, Object> maps, final getBeanCallback<YubibaoAvailbalance> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<YubibaoAvailbalance>> resultBeanCall = service.getYubibaoAvailbalance(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<YubibaoAvailbalance>>() {
            @Override
            public void onResponse(Call<ResultBean<YubibaoAvailbalance>> call, Response<ResultBean<YubibaoAvailbalance>> response) {
                ResultBean<YubibaoAvailbalance> responseBody = response.body();
                setResponse(YubibaoAvailbalance.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<YubibaoAvailbalance>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 资金划转提交
     */
    public static void submitAssetsTransfer(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean> resultBeanCall = service.submitAssetsTransfer(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 余币宝资金划转提交
     */
    public static void submitYubibaoTransfer(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean> resultBeanCall = service.submitYubibaoTransfer(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取用户现货余额
     */
    public static void getMyGoodsWallet(String token, Map<String, Object> maps, final getBeanCallback<GoodsWallet> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GoodsWallet>> resultBeanCall = service.getMyGoodsWallet(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GoodsWallet>>() {
            @Override
            public void onResponse(Call<ResultBean<GoodsWallet>> call, Response<ResultBean<GoodsWallet>> response) {
                ResultBean<GoodsWallet> responseBody = response.body();
                setResponse(GoodsWallet.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GoodsWallet>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    public static void getTradeMessage(String token, Map<String, Object> maps, final getBeanCallback<TradeMessage> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<TradeMessage>> resultBeanCall = service.getTradeMessage(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<TradeMessage>>() {
            @Override
            public void onResponse(Call<ResultBean<TradeMessage>> call, Response<ResultBean<TradeMessage>> response) {
                ResultBean<TradeMessage> responseBody = response.body();
                setResponse(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<TradeMessage>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取公告列表
     */
    public static void getNoticeList(Map<String, Object> maps, final getBeanCallback<Notice> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<Notice>> resultBeanCall = service.getNoticeList(params);
        resultBeanCall.enqueue(new Callback<ResultBean<Notice>>() {
            @Override
            public void onResponse(Call<ResultBean<Notice>> call, Response<ResultBean<Notice>> response) {
                ResultBean<Notice> responseBody = response.body();
                setResponse(Notice.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<Notice>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取行情初始化列表
     */
    public static void getMarketData(Map<String, Object> maps, final getBeanCallback<PushMarketBean> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<PushMarketBean>> resultBeanCall = service.getMarketData(params);
        resultBeanCall.enqueue(new Callback<ResultBean<PushMarketBean>>() {
            @Override
            public void onResponse(Call<ResultBean<PushMarketBean>> call, Response<ResultBean<PushMarketBean>> response) {
                ResultBean<PushMarketBean> responseBody = response.body();
                setResponse(PushMarketBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<PushMarketBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * im系统通知列表
     */
    public static void getImSystemMessageList(String token, Map<String, Object> maps, final getBeanCallback<ImSystemMessage> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<ImSystemMessage>> resultBeanCall = service.getImSystemMessageList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<ImSystemMessage>>() {
            @Override
            public void onResponse(Call<ResultBean<ImSystemMessage>> call, Response<ResultBean<ImSystemMessage>> response) {
                ResultBean<ImSystemMessage> responseBody = response.body();
                setResponse(ImSystemMessage.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<ImSystemMessage>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 设置头像
     */
    public static void setUserIcon(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.setUserIcon(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 限价交易
     */
    public static void limitPriceTrade(String token,boolean isBuy, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = isBuy?service.limitPriceBuy(token, params):service.limitPriceSale(token,params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 市价交易
     */
    public static void marketPriceTrade(String token,boolean isBuy, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = isBuy?service.marketPriceBuy(token, params):service.marketPriceSale(token,params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 限价卖出
     */
    public static void limitPriceSale(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.limitPriceSale(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 市价卖出
     */
    public static void marketPriceSale(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.marketPriceSale(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取我的当前委托和历史委托列表
     */
    public static void getMyEntrustList(String token, Map<String, Object> maps, final getBeanCallback<Entrust> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN,"未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<Entrust>> resultBeanCall = service.getMyEntrustList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<Entrust>>() {
            @Override
            public void onResponse(Call<ResultBean<Entrust>> call, Response<ResultBean<Entrust>> response) {
                ResultBean<Entrust> responseBody = response.body();
                setResponse(Entrust.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<Entrust>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 最新成交列表
     */
    public static void getLastDealList(Map<String, Object> maps, final getBeanCallback<LastDealBean> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<LastDealBean>> resultBeanCall = service.getLastDealList(params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<LastDealBean>>() {
            @Override
            public void onResponse(Call<ResultBean<LastDealBean>> call, Response<ResultBean<LastDealBean>> response) {
                ResultBean<LastDealBean> responseBody = response.body();
                setResponse(LastDealBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LastDealBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 一键撤销所有当前委托
     */
    public static void canceledAllEntrust(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.canceledAllEntrust(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 撤销选中当前委托
     */
    public static void canceledOneEntrust(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.canceledOneEntrust(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取委托详情
     */
    public static void getEntrustDetails(String token, Map<String, Object> maps, final getBeanCallback<EntrustDetails> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN,"未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<EntrustDetails>> resultBeanCall = service.getEntrustDetails(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<EntrustDetails>>() {
            @Override
            public void onResponse(Call<ResultBean<EntrustDetails>> call, Response<ResultBean<EntrustDetails>> response) {
                ResultBean<EntrustDetails> responseBody = response.body();
                setResponse(EntrustDetails.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<EntrustDetails>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取C2C个人信息详情
     */
    public static void getC2CPersonalBusiness(String token, Map<String, Object> maps, final getBeanCallback<C2CPersonalMessage> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN,"未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<C2CPersonalMessage>> resultBeanCall = service.getC2CPersonalBusiness(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<C2CPersonalMessage>>() {
            @Override
            public void onResponse(Call<ResultBean<C2CPersonalMessage>> call, Response<ResultBean<C2CPersonalMessage>> response) {
                ResultBean<C2CPersonalMessage> responseBody = response.body();
                setResponse(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<C2CPersonalMessage>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取用户挖矿权限
     */
    public static void getDigPermission(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.getDigPermission(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取实名认证token
     */
    public static void getRealNameToken(String token, Map<String, Object> maps, final getBeanCallback<RealNameResult> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<RealNameResult>> resultBeanCall = service.getRealNameToken(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<RealNameResult>>() {
            @Override
            public void onResponse(Call<ResultBean<RealNameResult>> call, Response<ResultBean<RealNameResult>> response) {
                ResultBean<RealNameResult> responseBody = response.body();
                setResponse(RealNameResult.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<RealNameResult>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取实名认证状态
     */
    public static void getRealNameStatus(String token, Map<String, Object> maps, final getBeanCallback<StringNameBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<StringNameBean>> resultBeanCall = service.getRealNameStatus(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<StringNameBean>>() {
            @Override
            public void onResponse(Call<ResultBean<StringNameBean>> call, Response<ResultBean<StringNameBean>> response) {
                ResultBean<StringNameBean> responseBody = response.body();
                setResponse(StringNameBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<StringNameBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 上传图片到服务器
     */
    public static void uploadImage(File file, String fileDir, final getBeanCallback<StringBean> callback) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        final MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody fileDirs = RequestBody.create(MediaType.parse("multipart/form-data"), fileDir);

        Call<ResultBean<StringBean>> resultBeanCall = service.uploadImage(fileDirs, body);
        resultBeanCall.enqueue(new Callback<ResultBean<StringBean>>() {
            @Override
            public void onResponse(Call<ResultBean<StringBean>> call, Response<ResultBean<StringBean>> response) {
                ResultBean responseBody = response.body();
                setResponse(StringBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<StringBean>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });

    }

    /**
     * 获取未登录时的挖矿信息
     */
    public static void getUnLoggedBlock(Map<String, Object> maps, final getBeanCallback<SocketBlockList> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<SocketBlockList>> resultBeanCall = service.getUnLoggedBlock(params);
        resultBeanCall.enqueue(new Callback<ResultBean<SocketBlockList>>() {
            @Override
            public void onResponse(Call<ResultBean<SocketBlockList>> call, Response<ResultBean<SocketBlockList>> response) {
                ResultBean<SocketBlockList> responseBody = response.body();
                setResponse(SocketBlockList.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<SocketBlockList>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取已登录出矿信息
     */
    public static void getLoggedBlock(String token, Map<String, Object> maps, final getBeanCallback<SocketBlockList> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<SocketBlockList>> resultBeanCall = service.getLoggedBlock(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<SocketBlockList>>() {
            @Override
            public void onResponse(Call<ResultBean<SocketBlockList>> call, Response<ResultBean<SocketBlockList>> response) {
                ResultBean<SocketBlockList> responseBody = response.body();
                setResponse(SocketBlockList.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<SocketBlockList>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 上传log到服务器
     */
    public static void uploadLog(String text, final getBeanCallback callback) {
        Map maps = new HashMap();
        maps.put("token", Preferences.getAccessToken());
        maps.put("text", text);
        maps.put("deviceNum", Preferences.getDeviceId());
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean> resultBeanCall = service.uploadLog(params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 商家发布交易
     */
    public static void c2cPublishTrade(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.c2cPublishTrade(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取商家委托单列表
     */
    public static void getC2CBusinessEntrustList(String token, Map<String, Object> maps, final getBeanCallback<C2cBusinessEntrust> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN,"未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<C2cBusinessEntrust>> resultBeanCall = service.getC2CBusinessEntrustList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<C2cBusinessEntrust>>() {
            @Override
            public void onResponse(Call<ResultBean<C2cBusinessEntrust>> call, Response<ResultBean<C2cBusinessEntrust>> response) {
                ResultBean<C2cBusinessEntrust> responseBody = response.body();
                setResponse(C2cBusinessEntrust.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<C2cBusinessEntrust>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 撤销C2C商家委托列表单个委托
     */
    public static void cancelC2CBusinessEntrust(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.cancelC2CBusinessEntrust(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 撤销C2C商家委托列表开始接单或取消接单
     */
    public static void receiptC2CBusinessEntrust(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.receiptC2CBusinessEntrust(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 一键撤销所有C2C商家委托列表开接单
     */
    public static void receiptAllC2CBusinessEntrust(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.receiptAllC2CBusinessEntrust(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取c2c普通用户商家列表
     */
    public static void getC2CNormalBusinessEntrustList(Map<String, Object> maps, final getBeanCallback<C2cNormalBusiness> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<C2cNormalBusiness>> resultBeanCall = service.getC2CNormalBusinessEntrustList(params);
        resultBeanCall.enqueue(new Callback<ResultBean<C2cNormalBusiness>>() {
            @Override
            public void onResponse(Call<ResultBean<C2cNormalBusiness>> call, Response<ResultBean<C2cNormalBusiness>> response) {
                ResultBean<C2cNormalBusiness> responseBody = response.body();
                setResponse(C2cNormalBusiness.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<C2cNormalBusiness>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 普通用户买入
     */
    public static void c2cNormalBuy(String token, Map<String, Object> maps, final getBeanCallback<OrderIdBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean<OrderIdBean>> resultBeanCall = service.c2cNormalBuy(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<OrderIdBean>>() {
            @Override
            public void onResponse(Call<ResultBean<OrderIdBean>> call, Response<ResultBean<OrderIdBean>> response) {
                ResultBean responseBody = response.body();
                setResponse(OrderIdBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<OrderIdBean>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 普通用户卖出
     */
    public static void c2cNormalSale(String token, Map<String, Object> maps, final getBeanCallback<OrderIdBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean<OrderIdBean>> resultBeanCall = service.c2cNormalSale(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<OrderIdBean>>() {
            @Override
            public void onResponse(Call<ResultBean<OrderIdBean>> call, Response<ResultBean<OrderIdBean>> response) {
                ResultBean responseBody = response.body();
                setResponse(OrderIdBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<OrderIdBean>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取我的当前委托和历史委托列表
     */
    public static void getC2CEntrustList(String token, Map<String, Object> maps, final getBeanCallback<C2cNormalEntrust> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN,"未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<C2cNormalEntrust>> resultBeanCall = service.getC2CEntrustList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<C2cNormalEntrust>>() {
            @Override
            public void onResponse(Call<ResultBean<C2cNormalEntrust>> call, Response<ResultBean<C2cNormalEntrust>> response) {
                ResultBean<C2cNormalEntrust> responseBody = response.body();
                setResponse(C2cNormalEntrust.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<C2cNormalEntrust>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 绑定支付宝
     */
    public static void bindAliPay(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.bindAliPay(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 绑定微信
     */
    public static void bindWxPay(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.bindWxPay(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 绑定银行卡
     */
    public static void bindCard(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.bindCard(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取银行卡列表
     */
    public static void getBankCards(String token, Map<String, Object> maps, final getBeanCallback<BankCard> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<BankCard>> resultBeanCall = service.getBankCardList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<BankCard>>() {
            @Override
            public void onResponse(Call<ResultBean<BankCard>> call, Response<ResultBean<BankCard>> response) {
                ResultBean<BankCard> responseBody = response.body();
                setResponse(BankCard.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<BankCard>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取C2C委托详情
     */
    public static void getC2CEntrustDetails(String token, Map<String, Object> maps, final getBeanCallback<C2CEntrustDetails> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<C2CEntrustDetails>> resultBeanCall = service.getC2CEntrustDetails(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<C2CEntrustDetails>>() {
            @Override
            public void onResponse(Call<ResultBean<C2CEntrustDetails>> call, Response<ResultBean<C2CEntrustDetails>> response) {
                ResultBean<C2CEntrustDetails> responseBody = response.body();
                setResponse(C2CEntrustDetails.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<C2CEntrustDetails>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * c2c详情界面确认支付
     */
    public static void c2cEntrustConfirmPayed(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.c2cEntrustConfirmPayed(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * c2c详情界面确认收款。放币
     */
    public static void c2cEntrustConfirmReceipt(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.c2cEntrustConfirmReceipt(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * c2c详情界面取消订单
     */
    public static void c2cEntrustCancelOrder(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.c2cEntrustCancelOrder(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * c2c详情界面申诉初始化
     */
    public static void c2cEntrustComplaint(String token, Map<String, Object> maps, final getBeanCallback<C2CEntrustComplaintBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<C2CEntrustComplaintBean>> resultBeanCall = service.c2cEntrustComplaint(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<C2CEntrustComplaintBean>>() {
            @Override
            public void onResponse(Call<ResultBean<C2CEntrustComplaintBean>> call, Response<ResultBean<C2CEntrustComplaintBean>> response) {
                ResultBean<C2CEntrustComplaintBean> responseBody = response.body();
                setResponse(C2CEntrustComplaintBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<C2CEntrustComplaintBean>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * c2c详情界面申诉客服
     */
    public static void c2cEntrustAppeal(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.c2cEntrustAppeal(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取我的邀请好友信息
     */
    public static void getMyInvitation(String token, Map<String, Object> maps, final getBeanCallback<Invitation> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<Invitation>> resultBeanCall = service.getMyInvitation(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<Invitation>>() {
            @Override
            public void onResponse(Call<ResultBean<Invitation>> call, Response<ResultBean<Invitation>> response) {
                ResultBean<Invitation> responseBody = response.body();
                setResponse(Invitation.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<Invitation>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取用户挖矿列表
     */
    public static void getDigRecordList(String token, Map<String, Object> maps, final getBeanCallback<DigRecord> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<DigRecord>> resultBeanCall = service.getDigRecordList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<DigRecord>>() {
            @Override
            public void onResponse(Call<ResultBean<DigRecord>> call, Response<ResultBean<DigRecord>> response) {
                ResultBean<DigRecord> responseBody = response.body();
                setResponse(DigRecord.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<DigRecord>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取我的算力详情
     */
    public static void getMyCalculateForce(String token, Map<String, Object> maps, final getBeanCallback<MyforceInfo> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<MyforceInfo>> resultBeanCall = service.getMyCalculateForce(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<MyforceInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<MyforceInfo>> call, Response<ResultBean<MyforceInfo>> response) {
                ResultBean<MyforceInfo> responseBody = response.body();
                setResponse(MyforceInfo.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<MyforceInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 获取算力排名
     */
    public static void getForceRank(String token, Map<String, Object> maps, final getBeanCallback<ForceRankInfo> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<ForceRankInfo>> resultBeanCall = service.getForceRank(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<ForceRankInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<ForceRankInfo>> call, Response<ResultBean<ForceRankInfo>> response) {
                ResultBean<ForceRankInfo> responseBody = response.body();
                setResponse(ForceRankInfo.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<ForceRankInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 领取算力奖励
     */
    public static void getCalculateForceGift(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.getCalculateForceGift(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 获取用户算力详情列表
     */
    public static void getCalculateDetailList(String token, Map<String, Object> maps, final getBeanCallback<CalculateDetail> callback) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<CalculateDetail>> resultBeanCall = service.getCalculateDetailList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<CalculateDetail>>() {
            @Override
            public void onResponse(Call<ResultBean<CalculateDetail>> call, Response<ResultBean<CalculateDetail>> response) {
                ResultBean<CalculateDetail> responseBody = response.body();
                setResponse(CalculateDetail.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<CalculateDetail>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }



    /**
     * 我的挖矿资产列表
     */
    public static void getMyAssetsList(String token, Map<String, Object> maps, final getBeanCallback<MyAssets> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<MyAssets>> resultBeanCall = service.getMyAssetsList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<MyAssets>>() {
            @Override
            public void onResponse(Call<ResultBean<MyAssets>> call, Response<ResultBean<MyAssets>> response) {
                ResultBean<MyAssets> responseBody = response.body();
                setResponse(MyAssets.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<MyAssets>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 提现提交
     */
    public static void submitMyAssetsWithdraw(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean> resultBeanCall = service.submitMyAssetsWithdraw(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * c自动挖矿
     */
    public static void autoDig(String token, Map<String, Object> maps, final getBeanCallback<DigResult> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<DigResult>> resultBeanCall = service.autoDig(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<DigResult>>() {
            @Override
            public void onResponse(Call<ResultBean<DigResult>> call, Response<ResultBean<DigResult>> response) {
                ResultBean<DigResult> responseBody = response.body();
                setResponse(DigResult.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<DigResult>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * c2c详情界面申诉客服
     */
    public static void getBlock(String token, Map<String, Object> maps, final getBeanCallback<BlockCollection> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<BlockCollection>> resultBeanCall = service.getBlock(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<BlockCollection>>() {
            @Override
            public void onResponse(Call<ResultBean<BlockCollection>> call, Response<ResultBean<BlockCollection>> response) {
                ResultBean<BlockCollection> responseBody = response.body();
                setResponse(BlockCollection.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<BlockCollection>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 交易挖矿
     */
    public static void getBlockTrade(String token, Map<String, Object> maps, final getBeanCallback<BlockTradeInfo> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<BlockTradeInfo>> resultBeanCall = service.getBlockTrade(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<BlockTradeInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<BlockTradeInfo>> call, Response<ResultBean<BlockTradeInfo>> response) {
                ResultBean<BlockTradeInfo> responseBody = response.body();
                setResponse(BlockTradeInfo.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<BlockTradeInfo>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取单个币种详情
     */
    public static void getCoinDetail(String token, Map<String, Object> maps, final getBeanCallback<CoinFlowDetail> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<CoinFlowDetail>> resultBeanCall = service.getCoinDetail(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<CoinFlowDetail>>() {
            @Override
            public void onResponse(Call<ResultBean<CoinFlowDetail>> call, Response<ResultBean<CoinFlowDetail>> response) {
                ResultBean<CoinFlowDetail> responseBody = response.body();
                setResponse(CoinFlowDetail.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<CoinFlowDetail>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取k线图数据
     */
    public static void getKLineData(Map<String, Object> maps, final getBeanCallback<KLineBean> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<KLineBean>> resultBeanCall = service.getKLineDatas(params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<KLineBean>>() {
            @Override
            public void onResponse(Call<ResultBean<KLineBean>> call, Response<ResultBean<KLineBean>> response) {
                ResultBean<KLineBean> responseBody = response.body();
                setResponse(KLineBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<KLineBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 挖矿算力奖励活动页
     */
    public static void getForceUpData(String token, Map<String, Object> maps, final getBeanCallback<ForceUpBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<ForceUpBean>> resultBeanCall = service.getForceUpData(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<ForceUpBean>>() {
            @Override
            public void onResponse(Call<ResultBean<ForceUpBean>> call, Response<ResultBean<ForceUpBean>> response) {
                ResultBean<ForceUpBean> responseBody = response.body();
                setResponse(ForceUpBean.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<ForceUpBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 挖矿算力奖励活动页
     */
    public static void submitForceShare(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.submitForceShare(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取挖矿主页场景信息
     */
    public static void getDigPageInfo(String token, Map<String, Object> maps, final getBeanCallback<DigPageInfo> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<DigPageInfo>> resultBeanCall = service.getDigPageInfo(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<DigPageInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<DigPageInfo>> call, Response<ResultBean<DigPageInfo>> response) {
                ResultBean<DigPageInfo> body = response.body();
                setResponse(DigPageInfo.class, body, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<DigPageInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取最新公告
     */
    public static void getNewAnnouncement(final getBeanCallback<BlockNoticeBean> callback) {

        Call<ResultBean<BlockNoticeBean>> resultBeanCall = service.getNewAnnouncement();
        resultBeanCall.enqueue(new Callback<ResultBean<BlockNoticeBean>>() {
            @Override
            public void onResponse(Call<ResultBean<BlockNoticeBean>> call, Response<ResultBean<BlockNoticeBean>> response) {
                ResultBean<BlockNoticeBean> body = response.body();
                setResponse(BlockNoticeBean.class, body, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<BlockNoticeBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取首页弹窗消息
     */
    public static void getMainDialog(final getBeanCallback<MainDialogBean> callback) {

        Call<ResultBean<MainDialogBean>> resultBeanCall = service.getMainDialog();
        resultBeanCall.enqueue(new Callback<ResultBean<MainDialogBean>>() {
            @Override
            public void onResponse(Call<ResultBean<MainDialogBean>> call, Response<ResultBean<MainDialogBean>> response) {
                ResultBean<MainDialogBean> body = response.body();
                setResponse(MainDialogBean.class, body, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<MainDialogBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * IM查找好友
     */
    public static void imFindFriend(String token, Map<String, Object> maps, final getBeanCallback<GetUserInfoByPhoneResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetUserInfoByPhoneResponse>> resultBeanCall = service.findIMFriend(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetUserInfoByPhoneResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetUserInfoByPhoneResponse>> call, Response<ResultBean<GetUserInfoByPhoneResponse>> response) {
                ResultBean responseBody = response.body();
                setResponse(GetUserInfoByPhoneResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetUserInfoByPhoneResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * IM添加好友
     */
    public static void getImUserDetail(String token, Map<String, Object> maps, final getBeanCallback<GetFriendInfoByIDResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetFriendInfoByIDResponse>> resultBeanCall = service.getIMUserDetail(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetFriendInfoByIDResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetFriendInfoByIDResponse>> call, Response<ResultBean<GetFriendInfoByIDResponse>> response) {
                ResultBean responseBody = response.body();
                setResponse(GetFriendInfoByIDResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetFriendInfoByIDResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 获取我的好友列表
     */
    public static void getMyFriendList(String token, Map<String, Object> maps, final getBeanCallback<GetUserInfosResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetUserInfosResponse>> resultBeanCall = service.getMyFriendList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetUserInfosResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetUserInfosResponse>> call, Response<ResultBean<GetUserInfosResponse>> response) {
                ResultBean<GetUserInfosResponse> responseBody = response.body();
                setResponse(GetUserInfosResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetUserInfosResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 添加IM好友
     */
    public static void addIMFriend(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.addIMFriend(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 添加IM好友
     */
    public static void deleteIMFriend(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.deleteIMFriend(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 获取群组聊天室列表
     */
    public static void getChatRoomList(String token, Map<String, Object> maps, final getBeanCallback<GetGroupResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetGroupResponse>> resultBeanCall = service.getChatRoomList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetGroupResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetGroupResponse>> call, Response<ResultBean<GetGroupResponse>> response) {
                ResultBean<GetGroupResponse> responseBody = response.body();
                setResponse(GetGroupResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetGroupResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 加入群组聊天室
     */
    public static void joinInGroup(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.joinInGroup(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 获取群组聊天室详情
     */
    public static void getChatRoomDetails(String token, Map<String, Object> maps, final getBeanCallback<GetGroupDetailsResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetGroupDetailsResponse>> resultBeanCall = service.getChatRoomDetails(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetGroupDetailsResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetGroupDetailsResponse>> call, Response<ResultBean<GetGroupDetailsResponse>> response) {
                ResultBean<GetGroupDetailsResponse> responseBody = response.body();
                setResponse(GetGroupDetailsResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetGroupDetailsResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 退出群组聊天室
     */
    public static void exitGroup(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.exitGroup(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 审核好友申请
     */
    public static void checkIMFriend(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.checkImFriend(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 修改好友备注
     */
    public static void updateIMFriendRemarkName(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.updateIMFriendRemarkName(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }






    /**
     * 修改昵称
     */
    public static void changeName(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.changeNickName(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }


            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 获取红包列表
     */
    public static void getRedPackageRecordList(String token, Map<String, Object> maps, final getBeanCallback<RedPackageRecord> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<RedPackageRecord>> resultBeanCall = service.getRedPacketRecordList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<RedPackageRecord>>() {
            @Override
            public void onResponse(Call<ResultBean<RedPackageRecord>> call, Response<ResultBean<RedPackageRecord>> response) {
                ResultBean<RedPackageRecord> responseBody = response.body();
                setResponse(RedPackageRecord.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<RedPackageRecord>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * IM获取红包详情
     */
    public static void getRedPacketDetail(String token, Map<String, Object> maps, final getBeanCallback<GetRedPacketStateResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetRedPacketStateResponse>> resultBeanCall = service.getRedPacketDetail(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetRedPacketStateResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetRedPacketStateResponse>> call, Response<ResultBean<GetRedPacketStateResponse>> response) {
                ResultBean responseBody = response.body();
                setResponse(GetRedPacketStateResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetRedPacketStateResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * IM领取红包
     */
    public static void receiveRedPacket(String token, Map<String, Object> maps, final getBeanCallback<GetRedPacketStateResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetRedPacketStateResponse>> resultBeanCall = service.receiveRedPacket(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetRedPacketStateResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetRedPacketStateResponse>> call, Response<ResultBean<GetRedPacketStateResponse>> response) {
                ResultBean responseBody = response.body();
                setResponse(GetRedPacketStateResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetRedPacketStateResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * IM获取转账详情
     */
    public static void getTransferDetail(String token, Map<String, Object> maps, final getBeanCallback<GetTransferStateResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetTransferStateResponse>> resultBeanCall = service.getTransferDetail(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetTransferStateResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetTransferStateResponse>> call, Response<ResultBean<GetTransferStateResponse>> response) {
                ResultBean responseBody = response.body();
                setResponse(GetTransferStateResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetTransferStateResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 获取转账记录列表
     */
    public static void getTransferRecordList(String token, Map<String, Object> maps, final getBeanCallback<RedPackageRecord> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<RedPackageRecord>> resultBeanCall = service.getTransferRecordList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<RedPackageRecord>>() {
            @Override
            public void onResponse(Call<ResultBean<RedPackageRecord>> call, Response<ResultBean<RedPackageRecord>> response) {
                ResultBean<RedPackageRecord> responseBody = response.body();
                setResponse(RedPackageRecord.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<RedPackageRecord>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 发送红包
     */
    public static void sendRedPacket(String token, Map<String, Object> maps, final getBeanCallback<GetSendRedPacketResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean<GetSendRedPacketResponse>> resultBeanCall = service.sendRedPacket(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<GetSendRedPacketResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetSendRedPacketResponse>> call, Response<ResultBean<GetSendRedPacketResponse>> response) {
                ResultBean responseBody = response.body();
                setResponse(GetSendRedPacketResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetSendRedPacketResponse>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取群组聊天室成员列表
     */
    public static void getChatRoomMembers(String token, Map<String, Object> maps, final getBeanCallback<GetGroupMemberResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<GetGroupMemberResponse>> resultBeanCall = service.getChatRoomMembers(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<GetGroupMemberResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetGroupMemberResponse>> call, Response<ResultBean<GetGroupMemberResponse>> response) {
                ResultBean<GetGroupMemberResponse> responseBody = response.body();
                setResponse(GetGroupMemberResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetGroupMemberResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 转账
     */
    public static void sendTransfer(String token, Map<String, Object> maps, final getBeanCallback<GetSendRedPacketResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean<GetSendRedPacketResponse>> resultBeanCall = service.sendTransfer(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<GetSendRedPacketResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<GetSendRedPacketResponse>> call, Response<ResultBean<GetSendRedPacketResponse>> response) {
                ResultBean responseBody = response.body();
                setResponse(GetSendRedPacketResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GetSendRedPacketResponse>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取好友申请列表
     */
    public static void getNewFriendList(String token, Map<String, Object> maps, final getBeanCallback<UserRelationshipResponse> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<UserRelationshipResponse>> resultBeanCall = service.getNewFriendList(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<UserRelationshipResponse>>() {
            @Override
            public void onResponse(Call<ResultBean<UserRelationshipResponse>> call, Response<ResultBean<UserRelationshipResponse>> response) {
                ResultBean<UserRelationshipResponse> responseBody = response.body();
                setResponse(UserRelationshipResponse.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<UserRelationshipResponse>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 杠杆交易--交易界面信息
     */
    public static void getLeverPageInfo(String token, Map<String, Object> maps, final getBeanCallback<LeverPagerInfo> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LeverPagerInfo>> resultBeanCall = service.getLeverPageInf(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<LeverPagerInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<LeverPagerInfo>> call, Response<ResultBean<LeverPagerInfo>> response) {
                ResultBean<LeverPagerInfo> responseBody = response.body();
                setResponse(LeverPagerInfo.class, responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LeverPagerInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--买入
     */
    public static void leverGoodsBuy(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.leverGoodsBuy(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 杠杆交易--卖出
     */
    public static void leverGoodsSale(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.leverGoodsSale(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--委托单列表
     */

    public static void getLeverEntrustList(String token, Map<String, Object> maps, final getBeanCallback<LeverEntrust> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LeverEntrust>> resultBeanCall = service.getLeverEntrustList(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<LeverEntrust>>() {
            @Override
            public void onResponse(Call<ResultBean<LeverEntrust>> call, Response<ResultBean<LeverEntrust>> response) {
                ResultBean<LeverEntrust> resultBean = response.body();
                setResponse(LeverEntrust.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LeverEntrust>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--取消所有委托单
     */
    public static void cancelLeverAllEntrust(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.canceledAllLeverEntrust(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                setResponseWithNoData(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--取消一个委托单
     */
    public static void cancelLeverOneEntrust(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);

        Call<ResultBean> resultBeanCall = service.canceledOneLeverEntrust(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                setResponseWithNoData(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--获取委托详情
     */
    public static void getLeverEntrustDetails(Map<String, Object> maps, final getBeanCallback<LeverEntrustDetails> callback) {
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LeverEntrustDetails>> resultBeanCall = service.getLeverEntrustDetails(params);
        resultBeanCall.enqueue(new Callback<ResultBean<LeverEntrustDetails>>() {
            @Override
            public void onResponse(Call<ResultBean<LeverEntrustDetails>> call, Response<ResultBean<LeverEntrustDetails>> response) {
                ResultBean<LeverEntrustDetails> resultBean = response.body();
                setResponse(LeverEntrustDetails.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LeverEntrustDetails>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--管理界面信息
     */
    public static void getLeverManagerInfo(String token, Map<String, Object> maps, final getBeanCallback<LeverManagerInfo> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LeverManagerInfo>> resultBeanCall = service.getLeverManagerInfo(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<LeverManagerInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<LeverManagerInfo>> call, Response<ResultBean<LeverManagerInfo>> response) {
                ResultBean<LeverManagerInfo> resultBean = response.body();
                setResponse(LeverManagerInfo.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LeverManagerInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--获取用户某钱包某币种可用数量
     */
    public static void getLeverAvailBalance(String token, Map<String, Object> maps, final getBeanCallback<LeverAvailBalanceBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LeverAvailBalanceBean>> resultBeanCall = service.getLeverAvasilBalance(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<LeverAvailBalanceBean>>() {
            @Override
            public void onResponse(Call<ResultBean<LeverAvailBalanceBean>> call, Response<ResultBean<LeverAvailBalanceBean>> response) {
                ResultBean<LeverAvailBalanceBean> resultBean = response.body();
                setResponse(LeverAvailBalanceBean.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LeverAvailBalanceBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--资产转入
     */
    public static void leverTransferIn(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }

        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        final Call<ResultBean> resultBeanCall = service.leverTransferIn(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                setResponseWithNoData(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--借款界面信息
     */
    public static void getLoanPageInfo(String token, Map<String, Object> maps, final getBeanCallback<LoanPageInfo> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LoanPageInfo>> resultBeanCall = service.getLoanPageInfo(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<LoanPageInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<LoanPageInfo>> call, Response<ResultBean<LoanPageInfo>> response) {
                ResultBean<LoanPageInfo> resultBean = response.body();
                setResponse(LoanPageInfo.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LoanPageInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    public static void imTalkMute(String token, Map<String, Object> maps, final getBeanCallback<MuteResult> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<MuteResult>> resultBeanCall = service.imTalkMute(token, params, sign);
        resultBeanCall.enqueue(new Callback<ResultBean<MuteResult>>() {
            @Override
            public void onResponse(Call<ResultBean<MuteResult>> call, Response<ResultBean<MuteResult>> response) {
                ResultBean resultBean = response.body();
                setResponse(MuteResult.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<MuteResult>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--借款
     */
    public static void leverLoan(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.leverLoan(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                setResponseWithNoData(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--查询还款记录
     */
    public static void getLeverRepaymentList(String token, Map<String, Object> maps, final getBeanCallback<LeverRepaymentInfo> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LeverRepaymentInfo>> resultBeanCall = service.getLeverRepaymentList(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<LeverRepaymentInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<LeverRepaymentInfo>> call, Response<ResultBean<LeverRepaymentInfo>> response) {
                ResultBean<LeverRepaymentInfo> resultBean = response.body();
                setResponse(LeverRepaymentInfo.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LeverRepaymentInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆--还款记录详情
     */
    public static void getLeverRepaymentInfo(Map<String, Object> maps, final getBeanCallback<LeverRepaymentInfo.ListBean> callback) {

        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LeverRepaymentInfo.ListBean>> resultBeanCall = service.getLeverRepaymentInfo(params);
        resultBeanCall.enqueue(new Callback<ResultBean<LeverRepaymentInfo.ListBean>>() {
            @Override
            public void onResponse(Call<ResultBean<LeverRepaymentInfo.ListBean>> call, Response<ResultBean<LeverRepaymentInfo.ListBean>> response) {
                ResultBean<LeverRepaymentInfo.ListBean> resultBean = response.body();
                setResponse(LeverRepaymentInfo.ListBean.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LeverRepaymentInfo.ListBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆--还款
     */
    public static void leverRepayment(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.leverRepayment(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                setResponseWithNoData(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆--资产转出
     */
    public static void leverTrabsferOut(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String myKey = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, myKey);
        Call<ResultBean> resultBeanCall = service.leverTransferOut(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                setResponseWithNoData(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 杠杆交易--查询杠杆账户可转出数量
     */
    public static void getLeverTransferOutbean(String token, Map<String, Object> maps, final getBeanCallback<LeverTransferOutbean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<LeverTransferOutbean>> resultBeanCall = service.getLeverTransferOutBean(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<LeverTransferOutbean>>() {
            @Override
            public void onResponse(Call<ResultBean<LeverTransferOutbean>> call, Response<ResultBean<LeverTransferOutbean>> response) {
                ResultBean<LeverTransferOutbean> resultBean = response.body();
                setResponse(LeverTransferOutbean.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<LeverTransferOutbean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 财务中心--首页资产信息
     */
    public static void getFinancialCenterInfo(String token, Map<String, Object> maps, final getBeanCallback<FinancialInfoBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<FinancialInfoBean>> resultBeanCall = service.getFinancialCenterInfo(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<FinancialInfoBean>>() {
            @Override
            public void onResponse(Call<ResultBean<FinancialInfoBean>> call, Response<ResultBean<FinancialInfoBean>> response) {
                ResultBean<FinancialInfoBean> resultBean = response.body();
                setResponse(FinancialInfoBean.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<FinancialInfoBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 财务中心--c2c钱包详情
     */
    public static void getFinancialC2CListFiC2CList(String token, Map<String, Object> maps, final getBeanCallback<FinancialBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<FinancialBean>> resultBeanCall = service.getFinancialC2CList(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<FinancialBean>>() {
            @Override
            public void onResponse(Call<ResultBean<FinancialBean>> call, Response<ResultBean<FinancialBean>> response) {
                ResultBean<FinancialBean> resultBean = response.body();
                setResponse(FinancialBean.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<FinancialBean>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取加入社群列表
     */
    public static void getGroupList( Map<String, Object> maps, final getBeanCallback<GroupsInfo> callback) {

        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<GroupsInfo>> resultBeanCall = service.getGroupList(params);
        resultBeanCall.enqueue(new Callback<ResultBean<GroupsInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<GroupsInfo>> call, Response<ResultBean<GroupsInfo>> response) {
                ResultBean<GroupsInfo> resultBean = response.body();
                setResponse(GroupsInfo.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<GroupsInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }
    /**
     * 获取首页
     */
    public static void getHome(String token, Map<String, Object> maps, final getBeanCallback<HomeInfo> callback) {

        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<HomeInfo>> resultBeanCall = null;
        if (TextUtils.isEmpty(token)){
            resultBeanCall     = service.getHomeInfoUnLogin(params);
        }else {
            resultBeanCall     = service.getHomeInfoLogin(token,params);
        }
        resultBeanCall.enqueue(new Callback<ResultBean<HomeInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<HomeInfo>> call, Response<ResultBean<HomeInfo>> response) {
                ResultBean<HomeInfo> resultBean = response.body();
                setResponse(HomeInfo.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<HomeInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 获取个人信息性别/算力
     */
    public static void getPersionalInfo(String token, Map<String, Object> maps, final getBeanCallback<MyPersonal> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<MyPersonal>> resultBeanCall = service.getPersionalInfo(token,params);

        resultBeanCall.enqueue(new Callback<ResultBean<MyPersonal>>() {
            @Override
            public void onResponse(Call<ResultBean<MyPersonal>> call, Response<ResultBean<MyPersonal>> response) {
                ResultBean<MyPersonal> resultBean = response.body();
                setResponse(MyPersonal.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<MyPersonal>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    public static void getPosters(String token, Map<String,Object> maps, final getBeanCallback<ShareInfo> callback){
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }

        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<ShareInfo>> resultBeanCall = service.getPosters(token,params);

        resultBeanCall.enqueue(new Callback<ResultBean<ShareInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<ShareInfo>> call, Response<ResultBean<ShareInfo>> response) {
                ResultBean<ShareInfo> resultBean = response.body();
                setResponse(ShareInfo.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<ShareInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    public static void getCircleBanners(Map<String,Object> maps, final getBeanCallback<CircleBanner> callback){
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<CircleBanner>> resultBeanCall = service.getCircleBanner(params);

        resultBeanCall.enqueue(new Callback<ResultBean<CircleBanner>>() {
            @Override
            public void onResponse(Call<ResultBean<CircleBanner>> call, Response<ResultBean<CircleBanner>> response) {
                ResultBean<CircleBanner> resultBean = response.body();
                setResponse(CircleBanner.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<CircleBanner>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    public static void getOrdinRewardInfo(String token, Map<String,Object> maps, final getBeanCallback<OrdinRewardInfo> callback){
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }

        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<OrdinRewardInfo>> resultBeanCall = service.getOdingRewardInfo(token,params,sign);

        resultBeanCall.enqueue(new Callback<ResultBean<OrdinRewardInfo>>() {
            @Override
            public void onResponse(Call<ResultBean<OrdinRewardInfo>> call, Response<ResultBean<OrdinRewardInfo>> response) {
                ResultBean<OrdinRewardInfo> resultBean = response.body();
                setResponse(OrdinRewardInfo.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<OrdinRewardInfo>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    public static void getOdinHistoryRankList(String token, Map<String,Object> maps, final getBeanCallback<List<OdinHistoryRankBean>> callback){
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }

        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<List<OdinHistoryRankBean>>> resultBeanCall = service.getOdinHistoryRankList(token,params,sign);

        resultBeanCall.enqueue(new Callback<ResultBean<List<OdinHistoryRankBean>>>() {
            @Override
            public void onResponse(Call<ResultBean<List<OdinHistoryRankBean>>> call, Response<ResultBean<List<OdinHistoryRankBean>>> response) {
                ResultBean<List<OdinHistoryRankBean>> resultBean = response.body();
                setResponse(OdinHistoryRankBean.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<List<OdinHistoryRankBean>>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    public static void getOdinMyFollowList(String token, Map<String,Object> maps, final getBeanCallback<List<OdinMyFollowBean>> callback){
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }

        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean<List<OdinMyFollowBean>>> resultBeanCall = service.getOdinMyFollowList(token,params,sign);

        resultBeanCall.enqueue(new Callback<ResultBean<List<OdinMyFollowBean>>>() {
            @Override
            public void onResponse(Call<ResultBean<List<OdinMyFollowBean>>> call, Response<ResultBean<List<OdinMyFollowBean>>> response) {
                ResultBean<List<OdinMyFollowBean>> resultBean = response.body();
                setResponse(OdinMyFollowBean.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<List<OdinMyFollowBean>>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    public static void buyOdin(String token, Map<String,Object> maps, final getBeanCallback callback){
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "用户未登录");
            return;
        }

        String params = ToolsUtils.getBase64Params(maps);
        String sign = ToolsUtils.getUploadSign(maps);
        Call<ResultBean> resultBeanCall = service.buyOdin(token,params,sign);

        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                setResponseWithNoData(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 资金划转提交
     */
    public static void submitWithdrawFrozen(String token, Map<String, Object> maps, final getBeanCallback callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean> resultBeanCall = service.submitWithdrawFrozen(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean responseBody = response.body();
                setResponseWithNoData(responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 资金划转修改市场情绪（每人每天只能选择一个 点击一次 点击后按钮失效）提交
     */
    public static void submitChangeMarket(String token, Map<String, Object> maps, final getBeanCallback<HomeInfo.MoodBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean<HomeInfo.MoodBean>> resultBeanCall = service.submitChangeMarket(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<HomeInfo.MoodBean>>() {
            @Override
            public void onResponse(Call<ResultBean<HomeInfo.MoodBean>> call, Response<ResultBean<HomeInfo.MoodBean>> response) {
                ResultBean<HomeInfo.MoodBean> responseBody = response.body();
                setResponse(HomeInfo.MoodBean.class,responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<HomeInfo.MoodBean>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    /**
     * 资金划转修改市场情绪（每人每天只能选择一个 点击一次 点击后按钮失效）提交
     */
    public static void initRenZheng(String token, Map<String, Object> maps, final getBeanCallback<RenZhengBean> callback) {
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        Preferences.init(MyApplication.getInstance().getApplicationContext());
        String key = Preferences.getLocalKey();
        String params = ToolsUtils.getAESParams(maps, key);
        Call<ResultBean<RenZhengBean>> resultBeanCall = service.initRenZheng(token, params);
        resultBeanCall.enqueue(new Callback<ResultBean<RenZhengBean>>() {
            @Override
            public void onResponse(Call<ResultBean<RenZhengBean>> call, Response<ResultBean<RenZhengBean>> response) {
                ResultBean<RenZhengBean> responseBody = response.body();
                setResponse(RenZhengBean.class,responseBody, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<RenZhengBean>> call, Throwable t) {
                /*获取失败，可能是网络未连接，总之是未与服务器连接*/
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    public static void getBannerList( Map<String, Object> maps, final getBeanCallback<ListBean<BannerInfo>> callback){
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<ListBean<BannerInfo>>> resultBeanCall = service.getBannerList(params);
        resultBeanCall.enqueue(new Callback<ResultBean<ListBean<BannerInfo>>>() {
            @Override
            public void onResponse(Call<ResultBean<ListBean<BannerInfo>>> call, Response<ResultBean<ListBean<BannerInfo>>> response) {
                ResultBean<ListBean<BannerInfo>> resultBean = response.body();
                setResponse(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<ListBean<BannerInfo>>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }

    public static void getBindList( String token,Map<String, Object> maps, final getBeanCallback<List<BindInfo>> callback){
        if (TextUtils.isEmpty(token)) {
            callback.onError(RESPONSE_ERROR_ANDROID_UNLOGIN, "未登录");
            return;
        }
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<List<BindInfo>>> resultBeanCall = service.getBindList(token,params);
        resultBeanCall.enqueue(new Callback<ResultBean<List<BindInfo>>>() {
            @Override
            public void onResponse(Call<ResultBean<List<BindInfo>>> call, Response<ResultBean<List<BindInfo>>> response) {
                ResultBean<List<BindInfo>> resultBean = response.body();
                setResponse(BindInfo.class, resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<List<BindInfo>>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }



    public static void getShareQrCode( Map<String, Object> maps, final getBeanCallback<String> callback){
        String params = ToolsUtils.getBase64Params(maps);
        Call<ResultBean<String>> resultBeanCall = service.getShareQrCode(params);
        resultBeanCall.enqueue(new Callback<ResultBean<String>>() {
            @Override
            public void onResponse(Call<ResultBean<String>> call, Response<ResultBean<String>> response) {
                ResultBean<String> resultBean = response.body();
                setResponse(resultBean, callback);
            }

            @Override
            public void onFailure(Call<ResultBean<String>> call, Throwable t) {
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
            }
        });
    }


    /**
     * 获取大文件
     */
    public static void getFileFromNet(final String fileUrl, final getBeanCallback<ResponseBody> callback) {
        new AsyncTask<Void, Long, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Call<ResponseBody> call = service.downloadFileWithDynamicUrlAsync(fileUrl);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(response.body());
                            /*LogUtils.w(TAG, "server contacted and has file");
                            boolean writtenToDisk = FileUtils.writeResponseBodyToDisk(response.body());
                            LogUtils.w(TAG, "file download was a success? " + writtenToDisk);*/
                        } else {
                            LogUtils.w(TAG, "server contact failed");
                            callback.onError(0, "数据错误");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT, t.toString());
                    }
                });
                return null;
            }
        }.execute();
    }

    private static void setResponse(ResultBean responseBody, getBeanCallback callback) {
        setResponse(null,responseBody,callback);
    }

    /**
     * 将返回内容设置到接口getBeanCallback中
     */
    private static void setResponse(Class classType, ResultBean responseBody, getBeanCallback callback) {
        if (responseBody == null) {
            /*若返回值为空，说明访问路径错误或连接发生错误*/
            responseBody = new ResultBean<>();
        }
        /*成功返回*/
        if (responseBody.getCode() == ErrorHandler.RESPONSE_SUCCESS) {
            /*服务器返回data为空*/
            if (responseBody.getData() == null) {
                /*服务器返回的data转成对象为空*/
                LogUtils.w(TAG,"服务器返回data解析为对象时错误");
                callback.onError(ErrorHandler.RESPONSE_ERROR_ANDROID_PARSING, "解析错误");
                return;
            }
            /*data对象不为空，则成功*/
            callback.onSuccess(responseBody.getData());
        } else {
            LogUtils.w(TAG,"服务器返回错误码、错误原因" + responseBody.getMsg());
            callback.onError(responseBody.getCode(), responseBody.getMsg());
        }
    }

    /**
     * 将返回内容设置到接口getBeanCallback中
     */
    private static void setResponseWithNoData(ResultBean responseBody, getBeanCallback callback) {
        if (responseBody == null) {
            /*若返回值为空，说明访问路径错误或连接发生错误*/
            responseBody = new ResultBean<>();
        }
        /*成功返回*/
        if (responseBody.getCode() == ErrorHandler.RESPONSE_SUCCESS) {
            /*code正确，则成功*/
            callback.onSuccess(null);
        } else {
            callback.onError(responseBody.getCode(), responseBody.getMsg());
        }
    }
}
