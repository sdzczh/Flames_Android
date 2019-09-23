package app.com.pgy.Utils;

import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Activitys.BlockActivity;
import app.com.pgy.Activitys.BlockTradeActviity;
import app.com.pgy.Activitys.C2CTradeActivity;
import app.com.pgy.Activitys.ForceScoreUpActivity;
import app.com.pgy.Activitys.InVitationActivity;
import app.com.pgy.Activitys.QiangGouViewActivity;
import app.com.pgy.Activitys.YubibaoActivity;
import app.com.pgy.Activitys.ZhuanPanViewActivity;
import app.com.pgy.Models.Beans.BannerInfo;
import app.com.pgy.Models.Beans.EventBean.EventForceUpBanner;

/**
 * Created by YX on 2018/7/26.
 */

public class BannerIntentUtils {
    public static boolean bannerToActivity(Context context,BannerInfo bannerInfo){
        if (context == null ||bannerInfo == null){
            return false;
        }
        Context mContext = context;
        int needFinish = 0;//针对提升算力页面banner
        Intent intent = null;
        //跳转类型 0网站 1余币宝 2交易挖矿 3法币 4币币 5史莱克挖矿
        switch (bannerInfo.getType()){
            case 0:
                intent = new Intent(mContext, WebDetailActivity.class);
                intent.putExtra("title", ""+bannerInfo.getTitle());
                intent.putExtra("url", bannerInfo.getAddress());
                break;
            case 1:
                if (LoginUtils.isLogin(mContext)){
                    intent = new Intent(mContext, YubibaoActivity.class);
                    needFinish = EventForceUpBanner.TYPE_ALL;
                }
                break;
            case 2:
                if (LoginUtils.isLogin(mContext)){
                    intent = new Intent(mContext, BlockTradeActviity.class);
                    needFinish = EventForceUpBanner.TYPE_ALL;
                }
                break;
            case 3:
                intent = new Intent(mContext, C2CTradeActivity.class);
                needFinish = EventForceUpBanner.TYPE_ALL;
                break;
//            case 4:
//                EventBus.getDefault().post(new EventMainChangeState(EventMainChangeState.CHANGE_TO_GOODS));
//                if (context instanceof ForceScoreUpActivity){
//                    EventBus.getDefault().post(new EventForceUpBanner(EventForceUpBanner.TYPE_ALL));
//                    ((ForceScoreUpActivity)context).finish();
//                }
//                break;
            case 4:
                intent = new Intent(mContext, BlockActivity.class);
                needFinish = EventForceUpBanner.TYPE_WITHOUTBLOCK;
                break;
            case 5:
                intent = new Intent(mContext, ZhuanPanViewActivity.class);
                needFinish = EventForceUpBanner.TYPE_WITHOUTBLOCK;
                break;
            case 6:
                intent = new Intent(mContext, InVitationActivity.class);
                needFinish = EventForceUpBanner.TYPE_ALL;
                break;
            case 7:
                intent = new Intent(mContext, QiangGouViewActivity.class);
                needFinish = EventForceUpBanner.TYPE_ALL;
                break;

        }
        if (intent != null){
            context.startActivity(intent);
            if (context instanceof ForceScoreUpActivity  && needFinish > 0){
                EventBus.getDefault().post(new EventForceUpBanner(needFinish));
                ((ForceScoreUpActivity)context).finish();
            }
        }
        return false;
    }
}
