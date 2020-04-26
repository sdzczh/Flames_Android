package app.com.pgy.Adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import app.com.pgy.Models.Beans.MortgageBean;
import app.com.pgy.Models.Beans.MortgageinfoBean;
import app.com.pgy.R;

import static app.com.pgy.Utils.Utils.longToString;

public class MortgagedateAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public MortgagedateAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_diya_tate, item+ "天");
    }

}
