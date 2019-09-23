package app.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.ForceRankInfo;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;

/**
 * Created by YX on 2018/7/16.
 */

public class ForceRankListAdapter extends RecyclerArrayAdapter<ForceRankInfo.ListBean> {
    public ForceRankListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForceRankViewHolder(parent);
    }

    class ForceRankViewHolder extends BaseViewHolder<ForceRankInfo.ListBean>{
        TextView tv_num;
        TextView tv_tel;
        TextView tv_force;
        TextView tv_title;
        ImageView iv_titlebg;
        public ForceRankViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_force_rank_view);
            tv_num = $(R.id.tv_item_force_rank_num);
            tv_tel = $(R.id.tv_item_force_rank_tel);
            tv_force = $(R.id.tv_item_force_rank_force);
            tv_title = $(R.id.tv_item_force_rank_name);
            iv_titlebg = $(R.id.iv_item_force_rank_namebg);
        }

        @Override
        public void setData(ForceRankInfo.ListBean data) {
            if (data.getRank() <= 3){

                iv_titlebg.setVisibility(View.VISIBLE);
                if (data.getRank() == 1){
                    iv_titlebg.setImageResource(R.mipmap.force_rank1);
                    tv_num.setBackground(getContext().getResources().getDrawable(R.mipmap.force_ranknum1));
                }
                if (data.getRank() == 2){
                    iv_titlebg.setImageResource(R.mipmap.force_rank2);
                    tv_num.setBackground(getContext().getResources().getDrawable(R.mipmap.force_ranknum2));
                }
                if (data.getRank() == 3){
                    iv_titlebg.setImageResource(R.mipmap.force_rank3);
                    tv_num.setBackground(getContext().getResources().getDrawable(R.mipmap.force_ranknum3));
                }
            }else{
                tv_num.setBackground(getContext().getResources().getDrawable(R.mipmap.force_ranknum));
                iv_titlebg.setVisibility(View.GONE);
            }
            tv_num.setText(MathUtils.int2String(data.getRank()));
            tv_tel.setText(data.getAccount());
            tv_force.setText(MathUtils.int2String(data.getForceNumber()));
            tv_title.setText(data.getHonorName());
        }
    }
}
