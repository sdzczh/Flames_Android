package app.com.pgy.Adapters;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import app.com.pgy.Models.Beans.MortgageBean;
import app.com.pgy.Models.Beans.MyteamBean2;
import app.com.pgy.R;

import static app.com.pgy.Utils.Utils.longToString;
import static app.com.pgy.im.utils.DateUtils.dateToString;
import static app.com.pgy.im.utils.DateUtils.stringToDate;

public class DiglistAdapter extends BaseQuickAdapter<MortgageBean.DigListBean, BaseViewHolder> {
    public DiglistAdapter(int layoutResId, @Nullable List<MortgageBean.DigListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MortgageBean.DigListBean item) {
        if (item.getIsTeam()==0) {
            helper.setText(R.id.tv_myteam_info, "个人挖矿收益");
        }
        try {
            helper.setText(R.id.tv_myteam_time, longToString(item.getCreatetime(),"yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        helper.setText(R.id.tv_myteam_profit, "+"+df.format(item.getAmount()) + "ANT");
    }

}
