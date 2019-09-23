package app.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/4/25.
 */

public class ForceUpBean {

    /**
     * joinPublicTask : {"finished":false,"soulForce":0}
     * qzoneShareTask : {"finished":false,"soulForce":10}
     * inviteTask : {"finished":false,"soulForce":15}
     * qqShareTask : {"finished":false,"soulForce":280}
     * circleShareTask : {"finished":false,"soulForce":0}
     * joinQGroupTask : {"finished":false,"soulForce":0}
     * monthSignTask : {"finished":false,"soulForce":50}
     * daySignTask : {"finished":false,"soulForce":1}
     * tenSignTask : {"finished":false,"soulForce":10}
     * wechatShareTask : {"finished":false,"soulForce":0}
     * realNameTask : {"finished":true,"soulForce":20}
     * dealTask : {"finished":false,"soulForce":0}
     * joinWechatTask : {"finished":false,"soulForce":30}
     * signDays : 0
     * banners : [{"createtime":1532154289000,"address":"","bannertype":0,"imgpath":"http","id":5,"state":1,"title":"","type":2,"updatetime":1532154289000},{"createtime":1532154258000,"address":"www.baidu.com","bannertype":0,"imgpath":"http","id":4,"state":1,"title":"百度","type":2,"updatetime":1532154258000}]
     * shareTitle : COIN挖矿活动进行中
     * instruUrl : http
     * shareDes : 加入史莱克学院，开启挖矿之旅 3种矿池免费等你来挖
     * shareUrl : http
     */

    private TaskBean joinPublicTask;//关注公众号任务
    private TaskBean qzoneShareTask;//分享QQ空间
    private TaskBean inviteTask;//邀请好友
    private TaskBean qqShareTask;//分享QQ好友
    private TaskBean circleShareTask;//分享朋友圈
    private TaskBean joinQGroupTask;//加入QQ群
    private TaskBean monthSignTask;//连续登录30天
    private TaskBean daySignTask;//每日签到
    private TaskBean tenSignTask;//连续登录10天
    private TaskBean wechatShareTask;//分享微信好友
    private TaskBean realNameTask;//实名认证
    private TaskBean dealTask;//现货/法币交易
    private TaskBean joinWechatTask;//加入微信群
    private int signDays;//加入微信群
    private String shareTitle;//分享标题
    private String instruUrl;// 规则介绍
    private String shareDes;// 分享描述
    private String shareUrl;//分享地址
    private List<BannerInfo> banners;//banner

    public TaskBean getJoinPublicTask() {
        return joinPublicTask;
    }

    public void setJoinPublicTask(TaskBean joinPublicTask) {
        this.joinPublicTask = joinPublicTask;
    }

    public TaskBean getQzoneShareTask() {
        return qzoneShareTask;
    }

    public void setQzoneShareTask(TaskBean qzoneShareTask) {
        this.qzoneShareTask = qzoneShareTask;
    }

    public TaskBean getInviteTask() {
        return inviteTask;
    }

    public void setInviteTask(TaskBean inviteTask) {
        this.inviteTask = inviteTask;
    }

    public TaskBean getQqShareTask() {
        return qqShareTask;
    }

    public void setQqShareTask(TaskBean qqShareTask) {
        this.qqShareTask = qqShareTask;
    }

    public TaskBean getCircleShareTask() {
        return circleShareTask;
    }

    public void setCircleShareTask(TaskBean circleShareTask) {
        this.circleShareTask = circleShareTask;
    }

    public TaskBean getJoinQGroupTask() {
        return joinQGroupTask;
    }

    public void setJoinQGroupTask(TaskBean joinQGroupTask) {
        this.joinQGroupTask = joinQGroupTask;
    }

    public TaskBean getMonthSignTask() {
        return monthSignTask;
    }

    public void setMonthSignTask(TaskBean monthSignTask) {
        this.monthSignTask = monthSignTask;
    }

    public TaskBean getDaySignTask() {
        return daySignTask;
    }

    public void setDaySignTask(TaskBean daySignTask) {
        this.daySignTask = daySignTask;
    }

    public TaskBean getTenSignTask() {
        return tenSignTask;
    }

    public void setTenSignTask(TaskBean tenSignTask) {
        this.tenSignTask = tenSignTask;
    }

    public TaskBean getWechatShareTask() {
        return wechatShareTask;
    }

    public void setWechatShareTask(TaskBean wechatShareTask) {
        this.wechatShareTask = wechatShareTask;
    }

    public TaskBean getRealNameTask() {
        return realNameTask;
    }

    public void setRealNameTask(TaskBean realNameTask) {
        this.realNameTask = realNameTask;
    }

    public TaskBean getDealTask() {
        return dealTask;
    }

    public void setDealTask(TaskBean dealTask) {
        this.dealTask = dealTask;
    }

    public TaskBean getJoinWechatTask() {
        return joinWechatTask;
    }

    public void setJoinWechatTask(TaskBean joinWechatTask) {
        this.joinWechatTask = joinWechatTask;
    }

    public int getSignDays() {
        return signDays;
    }

    public void setSignDays(int signDays) {
        this.signDays = signDays;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getInstruUrl() {
        return instruUrl;
    }

    public void setInstruUrl(String instruUrl) {
        this.instruUrl = instruUrl;
    }

    public String getShareDes() {
        return shareDes;
    }

    public void setShareDes(String shareDes) {
        this.shareDes = shareDes;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public List<BannerInfo> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerInfo> banners) {
        this.banners = banners;
    }

    public static class TaskBean {
        /**
         * finished : false
         * soulForce : 30
         */

        private boolean finished;
        private int soulForce;

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public int getSoulForce() {
            return soulForce;
        }

        public void setSoulForce(int soulForce) {
            this.soulForce = soulForce;
        }
    }
}
