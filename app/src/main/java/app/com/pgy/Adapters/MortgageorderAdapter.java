package app.com.pgy.Adapters;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
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
        double mul = mul(item.getRate(), 100.0);
        helper.setText(R.id.tv_diyaorder_rate,"日利率："+mul+"%");
        TextProgressBar progressbar = (TextProgressBar) helper.itemView.findViewById(R.id.progressbar);
        progressbar.setProgress(Double.valueOf(item.getPercentage()).intValue());

    }

    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
}
