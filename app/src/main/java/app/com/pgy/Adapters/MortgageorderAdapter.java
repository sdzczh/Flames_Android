package app.com.pgy.Adapters;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import app.com.pgy.Models.Beans.MortgageorderBean;
import app.com.pgy.R;
import app.com.pgy.Widgets.TextProgressBar;

public class MortgageorderAdapter extends BaseQuickAdapter<MortgageorderBean, BaseViewHolder> {
    public MortgageorderAdapter(int layoutResId, @Nullable List<MortgageorderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MortgageorderBean item) {
//        DecimalFormat df = new DecimalFormat("0.00");
        helper.setText(R.id.tv_diyaorder_num,item.getAmount()+"PGY");
        helper.setText(R.id.tv_diyaorder_diyadate,"抵押日期："+item.getStartTime());
        helper.setText(R.id.tv_diyaorder_releasedate,"释放日期："+item.getEndTime());
        helper.setText(R.id.tv_diyaorder_rate,"日利率："+item.getRate()*100+"%");
        TextProgressBar progressbar = (TextProgressBar) helper.itemView.findViewById(R.id.progressbar);
        progressbar.setProgress(Double.valueOf(item.getPercentage()).intValue());
//        progressbar.setProgress(Double.valueOf(12.6).intValue());
//        progressbar.setState(102);
//        progressbar.setProgress(50);

    }
}
