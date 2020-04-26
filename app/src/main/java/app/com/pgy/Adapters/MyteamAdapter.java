package app.com.pgy.Adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import app.com.pgy.Models.Beans.MyteamBean;
import app.com.pgy.Models.Beans.MyteamBean2;
import app.com.pgy.R;

public class MyteamAdapter extends BaseQuickAdapter<MyteamBean2.RecordsBean, BaseViewHolder> {
    public MyteamAdapter(int layoutResId, @Nullable List<MyteamBean2.RecordsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyteamBean2.RecordsBean item) {
        helper.setText(R.id.tv_myteam_phone,item.getNickname());
        helper.setText(R.id.tv_myteam_time,item.getCreateTime());
        DecimalFormat df = new DecimalFormat("0.00");
        helper.setText(R.id.tv_myteam_profit,df.format(item.getAmount()) + "Flames");
    }
}
