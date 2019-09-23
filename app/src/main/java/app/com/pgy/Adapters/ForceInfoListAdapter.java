package app.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.CalculateDetail;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;

/**
 * Created by YX on 2018/7/16.
 */

public class ForceInfoListAdapter extends RecyclerArrayAdapter<CalculateDetail.ListBean> {
    public ForceInfoListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForceInfoViewHolder(parent);
    }

    class ForceInfoViewHolder extends BaseViewHolder<CalculateDetail.ListBean> {
        TextView tv_title;
        TextView tv_score;
        TextView tv_time;
        TextView tv_total;
        View line;
        public ForceInfoViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_force_info_list_view);
            tv_title = $(R.id.tv_item_force_info_title);
            tv_score = $(R.id.tv_item_force_info_score);
            tv_time = $(R.id.tv_item_force_info_time);
            tv_total = $(R.id.tv_item_force_info_total);
            line = $(R.id.view_item_force_info_line);
        }

        @Override
        public void setData(CalculateDetail.ListBean data) {
            if (getDataPosition() == getAllData().size()-1){
                line.setVisibility(View.GONE);
            }else {
                line.setVisibility(View.VISIBLE);
            }
            tv_title.setText(data.getType());
 /*操作算力*/
            int calculForce = data.getCalculForce();
            if (calculForce >= 0){
                tv_score.setText("+"+calculForce);
                tv_score.setTextColor(getContext().getResources().getColor(R.color.color_4d94ff));
            }else{
                tv_score.setText(calculForce+"");
                tv_score.setTextColor(getContext().getResources().getColor(R.color.color_ff9933));
            }
            tv_time.setText(data.getCreateTime());
            tv_total.setText(MathUtils.int2String(data.getAllCalculForce()));
        }
    }
}
