package app.com.pgy.Constants;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import app.com.pgy.Activitys.BindAliActivity;
import app.com.pgy.Activitys.BindBankActivity;
import app.com.pgy.Activitys.BindWeixinActivity;
import app.com.pgy.Activitys.ChangeTradePwActivity;
import app.com.pgy.Activitys.PersonalRenZhengActivity;
import app.com.pgy.Activitys.SecuritycenterActivity;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Services.MyWebSocket;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.ToastUtils;
import app.com.pgy.Utils.Utils;
import app.com.pgy.Widgets.ExitDialog;
import app.com.pgy.Widgets.MyToast;

import static app.com.pgy.Constants.Constants.DEBUG;
import static app.com.pgy.Constants.StaticDatas.ALIPAY;
import static app.com.pgy.Constants.StaticDatas.BANKCARD;
import static app.com.pgy.Constants.StaticDatas.WECHART;

/**
 * @author xuqingzhong
 *         错误处理
 */

public class ErrorHandler {
    /**
     * 服务器端返回码
     */
    public static final int RESPONSE_SUCCESS = 10000;        //返回成功
    public static final int RESPONSE_ERROR = 0;            //成功响应，但返回的json为空时
    /**
     * 参数错误：10001-19999
     */
    public static final int RESPONSE_ERROR_PARAMS_INVALID = 10001;    //参数无效
    public static final int RESPONSE_ERROR_PARAMS_NULL = 10002;    //参数为空
    public static final int RESPONSE_ERROR_PARAMS_ERROR = 10003;    //参数类型错误
    /**
     * 用户错误：20001-29999
     */
    public static final int RESPONSE_ERROR_ANDROID_UNLOGIN = 20000; //前端判断token为空，未登录
    public static final int RESPONSE_ERROR_USER_UNLOGIN = 20001;    //用户未登录
    public static final int RESPONSE_ERROR_USER_ACCOUNTORPWDERROR = 20002;    //用户名或密码错误
    public static final int RESPONSE_ERROR_USER_ACCOUNTFROZEN = 20003;    //账号已被冻结
    public static final int RESPONSE_ERROR_USER_ACCOUNTNOTEXIT = 20004;    //用户不存在
    public static final int RESPONSE_ERROR_USER_ACCOUNTHASEXIT = 20005;    //用户已存在
    public static final int RESPONSE_ERROR_USER_ACCOUNTCANCELED = 20006;    //账号已被注销
    public static final int RESPONSE_ERROR_USER_ACCOUNTNOTREALNAME = 20007;    //用户未实名
    /**
     * 业务错误：30001-39999
     */
    public static final int RESPONSE_ERROR_BUSINESS_SMSEXCEPTION = 30001;    //短信接口异常
    public static final int RESPONSE_ERROR_BUSINESS_PHONELIMIT = 30002;    //手机号频繁限制
    public static final int RESPONSE_ERROR_BUSINESS_VERIFICATIONERROR = 30003;    //验证码错误
    public static final int RESPONSE_ERROR_BUSINESS_VERIFICATIONOVERDUE = 30004;    //验证码已过期
    public static final int RESPONSE_ERROR_BUSINESS_VERIFICATIONLIMIT = 30005;    //验证码使用超出次数限制
    public static final int RESPONSE_ERROR_BUSINESS_REFEREESNOTEXIT = 30006;    //推荐人账号不存在
    public static final int RESPONSE_ERROR_BUSINESS_REFEREESFROZEN = 30007;    //推荐人账号已被冻结
    public static final int RESPONSE_ERROR_BUSINESS_REFEREESCANCELED = 30008;    //推荐人账号已被注销
    public static final int RESPONSE_ERROR_BUSINESS_ORDERHASEXIT = 30009;    //订单号已存在
    public static final int RESPONSE_ERROR_BUSINESS_BANKCARDNOTEXIT = 30010;    //银行卡不存在
    public static final int RESPONSE_ERROR_BUSINESS_BANKCARDDELETEFAIL = 30011;    //银行卡删除失败
    public static final int RESPONSE_ERROR_BUSINESS_INPUTAMOUNTERROR = 30012;    //金额输入不正确
    public static final int RESPONSE_ERROR_BUSINESS_TRADEPWDLOCKED = 30013;    //交易密码已锁定
    public static final int RESPONSE_ERROR_BUSINESS_TRADEPWDERROR = 30014;    //交易密码不正确
    public static final int RESPONSE_ERROR_BUSINESS_LACKOFBALANCE = 30015;    //余额不足
    public static final int RESPONSE_ERROR_BUSINESS_TARGETACCOUNTNOTEXIST = 30016;    //对方用户不存在
    public static final int RESPONSE_ERROR_BUSINESS_TARGETACCOUNTFOZEN = 30017;    //对方账号已被冻结
    public static final int RESPONSE_ERROR_BUSINESS_TARGETACCOUNTREGISTED = 30018;    //对方账号已被注销
    public static final int RESPONSE_ERROR_BUSINESS_TARGETUSERNOTCERI = 30019;    //对方用户未实名
    public static final int RESPONSE_ERROR_BUSINESS_WITHDRAWPERDAYOVERTIMES = 30020;    //日提现次数超出限制
    public static final int RESPONSE_ERROR_BUSINESS_WITHDRAWPERDAYOVERAMOUT = 30021;    //日提现金额超出限制
    public static final int RESPONSE_ERROR_BUSINESS_TRANSFERTOSELFTERROR = 30022;    //不能给自己转账
    public static final int RESPONSE_ERROR_BUSINESS_TARGETWALLETNOTEXSIT = 30023;    //对方钱包信息不存在
    public static final int RESPONSE_ERROR_BUSINESS_OLDPWDERROR = 30024;    //旧密码错误
    public static final int RESPONSE_ERROR_BUSINESS_TRADEPWDNOTEXSIT = 30025;    //未设置交易密码
    public static final int RESPONSE_ERROR_BUSINESS_FILTTYPEERROR = 30026;    //文件类型错误
    public static final int RESPONSE_ERROR_BUSINESS_FILEUPLOADERROR = 30027;    //文件上传失败
    public static final int RESPONSE_ERROR_BUSINESS_ORDERSATEILLEGAL = 30028;    //订单状态不合法
    public static final int RESPONSE_ERROR_BUSINESS_ALIPAYACCOUNTBINDED = 30029;    //支付宝账号已绑定
    public static final int RESPONSE_ERROR_BUSINESS_TRADEPWDEXSIT = 30030;    //交易密码已设置
    public static final int RESPONSE_ERROR_BUSINESS_USERNOTEXSIT = 30031;    //平台用户不存在
    public static final int RESPONSE_ERROR_BUSINESS_DIGINGHLC = 30032;    //挖火蚁正在有效期中，无需充值
    public static final int RESPONSE_ERROR_BUSINESS_GEAROVERMAX = 30033;    //档位超出最大限制
    public static final int RESPONSE_ERROR_BUSINESS_DIGINGHYCFAIL = 30034;    //挖火蚁服务已失效
    public static final int RESPONSE_ERROR_BUSINESS_HADREALNAME = 30035;    //用户已完成实名认证
    public static final int RESPONSE_ERROR_BUSINESS_REALNAMEINITFAIL = 30036;    //实名认证初始化失败
    public static final int RESPONSE_ERROR_BUSINESS_UNREALNAME = 30037;    //未进行实人认证
    public static final int RESPONSE_ERROR_BUSINESS_REALNAMEING = 30038;    //认证中
    public static final int RESPONSE_ERROR_BUSINESS_REALNAMEFAIL = 30039;    //实名认证失败
    public static final int RESPONSE_ERROR_BUSINESS_REALNAMEAGEILLEGAL = 30040;    //实名认年龄不合法
    public static final int RESPONSE_ERROR_BUSINESS_REALNAMEIDEXIT = 30041;    //身份证号已经在其他账号认证
    public static final int RESPONSE_ERROR_BUSINESS_REALNAMELIMIT = 30042;    //实名认证限制中
    public static final int RESPONSE_ERROR_BUSINESS_REGISTERFAIL = 30043;    //注册失败
    public static final int RESPONSE_ERROR_BUSINESS_DIGINGHYC = 30044;    //验证码获取失败
    public static final int RESPONSE_ERROR_BUSINESS_UNBINGALIPAY = 30045;    //未绑定支付宝,弹出对话框跳转
    public static final int RESPONSE_ERROR_BUSINESS_UNBINGWECHARTPAY = 30046;    //未绑定微信，弹出对话框跳转
    public static final int RESPONSE_ERROR_BUSINESS_UNBINGCARDPAY = 30047;    //未绑定银行卡，弹出对话框
    public static final int RESPONSE_ERROR_BUSINESS_ORDERDEALED = 30048;    //订单已成交
    public static final int RESPONSE_ERROR_BUSINESS_STOPRECEIPTORDER = 30049;    //商户已停止接单
    public static final int RESPONSE_ERROR_BUSINESS_ORDERNUMBERNOTENOUGH = 30050;    //商家交易数量不足
    public static final int RESPONSE_ERROR_BUSINESS_CANNOTTRADEWITHSELF = 30051;    //无法跟自己交易
    public static final int RESPONSE_ERROR_BUSINESS_ORDERNOTEXIST = 30053;    //不存在匹配交易
    public static final int RESPONSE_ERROR_BUSINESS_BINDNULL = 30054;    //未绑定支付信息
    public static final int RESPONSE_ERROR_BUSINESS_WITHOUTPERMISSION = 30055;    //操作无权限
    public static final int RESPONSE_ERROR_BUSINESS_BINGEDWECHART = 30056;    //微信已绑定
    public static final int RESPONSE_ERROR_BUSINESS_BINGEDCARD = 30057;    //银行卡已绑定
    public static final int RESPONSE_ERROR_BUSINESS_TODAYBUYNUMBERSTOP = 30058;    //今日买入取消次数已达上限
    public static final int RESPONSE_ERROR_BUSINESS_DEALAMOUNTOUTOFLIMIT = 30059;    //成交额不在商家要求范围内
    public static final int RESPONSE_ERROR_BUSINESS_EXITNOTDEALEDORDER = 30060;    //存在未完成的订单
    public static final int RESPONSE_ERROR_BUSINESS_BUYORDERNUMBERTOP = 30067;    //买入订单数量已到上限

    public static final int RESPONSE_ERROR_BUSINESS_FINISHED = 30072;    //这个任务今天已经完成啦，请明天再来

    /**
     * 系统错误：40001-49999
     */
    public static final int RESPONSE_ERROR_SYSTEM_ERROR = 40001;    //系统异常
    public static final int RESPONSE_ERROR_SYSTEM_PARAMSERROR = 40002;    //系统参数未配置

    /**
     * 数据错误：50001-599999
     */
    public static final int RESPONSE_ERROR_DATA_NOTFOUND = 50001;    //数据未找到
    public static final int RESPONSE_ERROR_DATA_ERROR = 50002;    //数据有误
    public static final int RESPONSE_ERROR_DATA_HASEXIT = 50003;    //数据已存在

    /**
     * 接口错误：60001-69999
     */
    public static final int RESPONSE_ERROR_INTERFACE_ILLEGAL = 60001;    //接口签名认证错误
    public static final int RESPONSE_ERROR_INTERFACE_ENCODERERROR = 60002;    //接口解密失败
    public static final int RESPONSE_ERROR_INTERFACE_FORBIDDEN = 60003;    //该接口禁止访问
    public static final int RESPONSE_ERROR_INTERFACE_ADDRESSINVALID = 60004;    //接口地址无效
    public static final int RESPONSE_ERROR_INTERFACE_TIMEOUT = 60005;    //接口请求超时

    /**
     * 权限错误：70001-79999
     */
    public static final int RESPONSE_ERROR_PERMISSION_CLOSED = 70001;    //功能已关闭

    /**
     * 前端和交互错误
     */
    public static final int RESPONSE_ERROR_ANDROID_REQUESTTIMEOUT = 402;    //Android端请求超时
    public static final int RESPONSE_ERROR_IDOVERTIME = 403;    //登录授权失败
    public static final int RESPONSE_ERROR_DATANOTEXIT = 404;    //服务器返回data为空
    public static final int RESPONSE_ERROR_ANDROID_PARSING = 600;        //服务器返回data在Android端解析为对象时错误

    private Context mContext;

    public ErrorHandler(Context context) {
        this.mContext = context;
    }

    public void handlerError(int errorCode, String reason) {
        LogUtils.w("MyError", "errorCode:"+errorCode+",reason:"+reason);
        switch (errorCode) {
            default:
                showToast(reason);
                break;
            /*android端token为空*/
            case RESPONSE_ERROR_ANDROID_UNLOGIN:
                if (DEBUG){
                    showToast("token为空");
                }
                break;
            /*以下为需要退出登录的，强退并刷新页面*/
            case RESPONSE_ERROR_USER_ACCOUNTFROZEN:
                showToast("账户已冻结");
                forceLogout(errorCode,reason);
                break;
            case RESPONSE_ERROR_USER_UNLOGIN:
                showToast("请先登录");
                forceLogout(errorCode,reason);
                break;
            case RESPONSE_ERROR_USER_ACCOUNTNOTEXIT:
                showToast("用户不存在");
                forceLogout(errorCode,reason);
                break;
            case RESPONSE_ERROR_USER_ACCOUNTCANCELED:
                showToast("违规操作，帐号已注销");
                forceLogout(errorCode,reason);
                break;
            case RESPONSE_ERROR_BUSINESS_WITHOUTPERMISSION:
                showToast("登录异常，请重新登录");
                forceLogout(errorCode,reason);
                break;
            case RESPONSE_ERROR_INTERFACE_ILLEGAL:
                showToast("系统错误，请联系客服处理！");
                forceLogout(errorCode,reason);
                break;
            case RESPONSE_ERROR_INTERFACE_ENCODERERROR:
                showToast("系统错误，请联系客服处理！");
                forceLogout(errorCode,reason);
                break;
            /*跳转到绑定支付宝界面*/
            case RESPONSE_ERROR_BUSINESS_UNBINGALIPAY:
                showJump2Bind("支付宝",ALIPAY);
                break;
            /*跳转到绑定微信界面*/
            case RESPONSE_ERROR_BUSINESS_UNBINGWECHARTPAY:
                showJump2Bind("微信",WECHART);
                break;
            /*跳转到绑定银行卡界面*/
            case RESPONSE_ERROR_BUSINESS_UNBINGCARDPAY:
                showJump2Bind("银行卡",BANKCARD);
                break;
            /*未匹配到绑定信息，去绑定*/
            case RESPONSE_ERROR_BUSINESS_BINDNULL:
                showSetPayTypeDialog();
                break;
            case RESPONSE_ERROR:
                showToast("请检查网络");
                break;
            case RESPONSE_ERROR_PARAMS_INVALID:
                showToast("信息有误，请重试");
                break;
            case RESPONSE_ERROR_PARAMS_NULL:
                showToast("信息有误，请重试");
                break;
            case RESPONSE_ERROR_PARAMS_ERROR:
                showToast("信息有误，请重试");
                break;
            case RESPONSE_ERROR_USER_ACCOUNTORPWDERROR:
                showToast("用户名或密码错误");
                break;
            case RESPONSE_ERROR_USER_ACCOUNTHASEXIT:
                showToast("用户已存在，请前去登录");
                break;
            case RESPONSE_ERROR_USER_ACCOUNTNOTREALNAME:
                showToast("请完成实名认证");
                showSetRealNameFirstDialog();
                break;
            case RESPONSE_ERROR_BUSINESS_SMSEXCEPTION:
                showToast("网络异常，请重试");
                break;
            case RESPONSE_ERROR_BUSINESS_PHONELIMIT:
                showToast("您的请求太频繁了，请稍后再试");
                break;
            case RESPONSE_ERROR_BUSINESS_VERIFICATIONERROR:
                showToast("验证码有误");
                break;
            case RESPONSE_ERROR_BUSINESS_VERIFICATIONOVERDUE:
                showToast("验证码已过期");
                break;
            case RESPONSE_ERROR_BUSINESS_VERIFICATIONLIMIT:
                showToast("验证码使用超出次数限制");
                break;
            case RESPONSE_ERROR_BUSINESS_REFEREESNOTEXIT:
                showToast("推荐人账号不存在");
                break;
            case RESPONSE_ERROR_BUSINESS_REFEREESFROZEN:
                showToast("推荐人账号已冻结");
                break;
            case RESPONSE_ERROR_BUSINESS_REFEREESCANCELED:
                showToast("推荐人账号已注销");
                break;
            case RESPONSE_ERROR_BUSINESS_ORDERHASEXIT:
                showToast("订单号已存在");
                break;
            case RESPONSE_ERROR_BUSINESS_BANKCARDNOTEXIT:
                showToast("银行卡不存在，请核对后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_BANKCARDDELETEFAIL:
                showToast("银行卡删除失败，请重试");
                break;
            case RESPONSE_ERROR_BUSINESS_INPUTAMOUNTERROR:
                //showToast("金额输入不正确，请核对后重试");
                showToast(reason);
                break;
            case RESPONSE_ERROR_BUSINESS_TRADEPWDLOCKED:
                showToast(reason);
                /*交易密码错误，清空本地密码*/
                Preferences.setLocalTradePwd("");
                break;
            case RESPONSE_ERROR_BUSINESS_TRADEPWDERROR:
                showToast(reason);
                /*交易密码错误，清空本地密码*/
                Preferences.setLocalTradePwd("");
                break;
            case RESPONSE_ERROR_BUSINESS_LACKOFBALANCE:
                showToast("余额不足");
                break;
            case RESPONSE_ERROR_BUSINESS_TARGETACCOUNTNOTEXIST:
                showToast("对方用户不存在，请核对后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_TARGETACCOUNTFOZEN:
                showToast("对方账号已冻结，请核对后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_TARGETACCOUNTREGISTED:
                showToast("对方账号已注销，请核对后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_TARGETUSERNOTCERI:
                showToast("对方帐号未实名");
                break;
            case RESPONSE_ERROR_BUSINESS_WITHDRAWPERDAYOVERTIMES:
                showToast("今日提现次数超出限制");
                break;
            case RESPONSE_ERROR_BUSINESS_WITHDRAWPERDAYOVERAMOUT:
                showToast("今日提现金额超出限制");
                break;
            case RESPONSE_ERROR_BUSINESS_TRANSFERTOSELFTERROR:
                showToast("转入账户有误，不能给自己转账");
                break;
            case RESPONSE_ERROR_BUSINESS_TARGETWALLETNOTEXSIT:
                showToast("转入账户有误，钱包信息不存在");
                break;
            case RESPONSE_ERROR_BUSINESS_OLDPWDERROR:
                showToast("旧密码错误，请重试！");
                break;
            case RESPONSE_ERROR_BUSINESS_TRADEPWDNOTEXSIT:
                showSetTradePwdFirstDialog();
                break;
            case RESPONSE_ERROR_BUSINESS_FILTTYPEERROR:
                showToast("请上传正确的图片类型");
                break;
            case RESPONSE_ERROR_BUSINESS_FILEUPLOADERROR:
                showToast("文件上传失败，请重试");
                break;
            case RESPONSE_ERROR_BUSINESS_ORDERSATEILLEGAL:
                showToast("订单状态有误，请重试");
                break;
            case RESPONSE_ERROR_BUSINESS_ALIPAYACCOUNTBINDED:
                showToast("该帐号已被绑定，请更换后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_TRADEPWDEXSIT:
                showToast("交易密码已设置，无需重复设置");
                break;
            case RESPONSE_ERROR_BUSINESS_USERNOTEXSIT:
                showToast("平台用户不存在");
                break;
            case RESPONSE_ERROR_BUSINESS_DIGINGHLC:
                showToast("服务正在有效期中，无需充值");
                break;
            case RESPONSE_ERROR_BUSINESS_GEAROVERMAX:
                showToast("系统错误，请联系客服处理！");
                break;
            case RESPONSE_ERROR_BUSINESS_DIGINGHYCFAIL:
                showToast("服务已过期，请开通后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_HADREALNAME:
                showToast("认证成功");
                break;
            case RESPONSE_ERROR_BUSINESS_REALNAMEINITFAIL:
                showToast("初始化失败，请重试");
                break;
            case RESPONSE_ERROR_BUSINESS_UNREALNAME:
                showToast("请先完成实名认证");
                break;
            case RESPONSE_ERROR_BUSINESS_REALNAMEING:
                showToast("认证中，请稍候");
                break;
            case RESPONSE_ERROR_BUSINESS_REALNAMEFAIL:
                showToast("认证失败，请重试");
                break;
            case RESPONSE_ERROR_BUSINESS_REALNAMEAGEILLEGAL:
                showToast("实名认证年龄不合法");
                break;
            case RESPONSE_ERROR_BUSINESS_REALNAMEIDEXIT:
                showToast("身份证号已被使用");
                break;
            case RESPONSE_ERROR_BUSINESS_REALNAMELIMIT:
                showToast("当天认证次数已到上限");
                break;
            case RESPONSE_ERROR_BUSINESS_REGISTERFAIL:
                showToast("注册失败");
                break;
            case RESPONSE_ERROR_BUSINESS_DIGINGHYC:
                showToast("验证码获取失败");
                break;
            case RESPONSE_ERROR_BUSINESS_ORDERDEALED:
                showToast("来晚一步，订单被抢啦");
                break;
            case RESPONSE_ERROR_BUSINESS_STOPRECEIPTORDER:
                showToast("该商户已停止接单");
                break;
            case RESPONSE_ERROR_BUSINESS_ORDERNUMBERNOTENOUGH:
                showToast("该商户可交易的数量不足，请更换商户后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_CANNOTTRADEWITHSELF:
                showToast("自己无法跟自己交易哦");
                break;
            case RESPONSE_ERROR_BUSINESS_ORDERNOTEXIST:
                showToast("商家繁忙，请稍后再试");
                break;
            case RESPONSE_ERROR_BUSINESS_BINGEDWECHART:
                showToast("该微信已被绑定，请更换后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_BINGEDCARD:
                showToast("该银行卡已被绑定，请更换后重试");
                break;
            case RESPONSE_ERROR_BUSINESS_TODAYBUYNUMBERSTOP:
                showToast("因操作违规，已限制今天的买入功能");
                break;
            case RESPONSE_ERROR_BUSINESS_DEALAMOUNTOUTOFLIMIT:
                showToast("交易金额超出范围，请重新输入");
                break;
            case RESPONSE_ERROR_BUSINESS_EXITNOTDEALEDORDER:
                showToast("请在所有订单都完成后进行此操作");
                break;
            case RESPONSE_ERROR_BUSINESS_FINISHED:
                showToast("这个任务今天已经完成啦，请明天再来");
                break;

            case RESPONSE_ERROR_SYSTEM_ERROR:
                showToast("系统小憩了一下，请重试！");
                break;
            case RESPONSE_ERROR_SYSTEM_PARAMSERROR:
                showToast("系统错误，请联系客服处理！");
                break;
            case RESPONSE_ERROR_DATA_NOTFOUND:
                showToast("数据未找到");
                break;
            case RESPONSE_ERROR_DATA_ERROR:
                showToast("数据有误");
                break;
            case RESPONSE_ERROR_DATA_HASEXIT:
                //showToast("数据已存在");
                break;
            case RESPONSE_ERROR_INTERFACE_FORBIDDEN:
                showToast("系统错误，请联系客服处理！");
                break;
            case RESPONSE_ERROR_INTERFACE_ADDRESSINVALID:
                showToast("系统错误，请联系客服处理！");
                break;
            case RESPONSE_ERROR_INTERFACE_TIMEOUT:
                showToast("网络连接失败，请重试！");
                break;
            case RESPONSE_ERROR_PERMISSION_CLOSED:
                showToast("功能已关闭");
                break;
            case RESPONSE_ERROR_BUSINESS_BUYORDERNUMBERTOP:
                showToast("买入订单数量已到上限");
                break;

        }
    }


    private void showJump2Bind(String name, final int payType) {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("您还没有绑定"+name+"支付");
        builder.setCancelable(true);

        builder.setPositiveButton("立即绑定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2Bind = null;
                        switch (payType){
                            case ALIPAY:
                                intent2Bind = new Intent(mContext,BindAliActivity.class);
                                break;
                            case WECHART:
                                intent2Bind = new Intent(mContext,BindWeixinActivity.class);
                                break;
                            case BANKCARD:
                                intent2Bind = new Intent(mContext, BindBankActivity.class);
                                break;
                            default:break;

                        }
                        if (intent2Bind != null){
                            mContext.startActivity(intent2Bind);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("暂不绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**弹出先实名认证对话框*/
    protected void showSetRealNameFirstDialog(){
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("请先去实名认证");
        builder.setCancelable(true);

        builder.setPositiveButton("去设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.IntentUtils(mContext, PersonalRenZhengActivity.class);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**弹出先设置交易密码对话框*/
    protected void showSetTradePwdFirstDialog(){
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("请先设置交易密码");
        builder.setCancelable(true);

        builder.setPositiveButton("去设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.IntentUtils(mContext,ChangeTradePwActivity.class);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**弹出先实名认证对话框*/
    protected void showSetPayTypeDialog(){
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("请先绑定支付方式");
        builder.setCancelable(true);

        builder.setPositiveButton("去绑定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.IntentUtils(mContext,SecuritycenterActivity.class);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**被动退出登录*/
    private void forceLogout(int errorCode ,String reason) {
        /*强制退出登录*/
        LogUtils.w("logout","Fragment挤掉:"+errorCode+",reason:"+reason);
        if (Preferences.isLogin()&&Preferences.clearAllUserData()) {
            EventBus.getDefault().post(new EventLoginState(false));
            MyWebSocket.getMyWebSocket().stopSocket();
        }
    }

    private void showToast(String message) {
        if (mContext == null) {
            return;
        }
        if (TextUtils.isEmpty(message)) {
            ToastUtils.ShortToast(mContext, "错误为空字符串");
        } else {
            MyToast.showToast(mContext,message);
        }
    }

    private void showLongToast(String message) {
        if (mContext == null) {
            return;
        }
        if (TextUtils.isEmpty(message)) {
            ToastUtils.LongToast(mContext, "错误为空字符串");
        } else {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}
