package app.com.pgy.Adapters;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import app.com.pgy.Models.Beans.DirectlistBean;
import app.com.pgy.Models.Beans.DirectlistBean2;
import app.com.pgy.R;

public class DirectlistAdapter extends BaseQuickAdapter<DirectlistBean2, BaseViewHolder> {
    public DirectlistAdapter(int layoutResId, @Nullable List<DirectlistBean2> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DirectlistBean2 item) {
        helper.setText(R.id.tv_direct_name, item.getNickname());
        helper.setText(R.id.tv_direct_phone, item.getPhone());
        helper.setText(R.id.tv_direct_time, item.getCreatetime());
        helper.setText(R.id.tv_direct_grade2, "Lv" + item.getReferenceStatus());
        helper.setText(R.id.tv_direct_grade, "V" + item.getIdstatus());
        if (item.getReferenceStatus() == 1) {
            helper.itemView.findViewById(R.id.tv_direct_grade2).setBackground(mContext.getResources().getDrawable(R.drawable.bg_myteam_grade1));
        }else  if (item.getReferenceStatus() == 2){
            helper.itemView.findViewById(R.id.tv_direct_grade2).setBackground(mContext.getResources().getDrawable(R.drawable.bg_myteam_grade2));
        }else {
            helper.itemView.findViewById(R.id.tv_direct_grade2).setBackground(mContext.getResources().getDrawable(R.drawable.bg_myteam_grade3));
        }

        if (item.getIdstatus() == 1) {
            helper.itemView.findViewById(R.id.tv_direct_grade).setBackground(mContext.getResources().getDrawable(R.drawable.bg_myteam_grade1));
        }else  if (item.getIdstatus() == 2){
            helper.itemView.findViewById(R.id.tv_direct_grade).setBackground(mContext.getResources().getDrawable(R.drawable.bg_myteam_grade2));
        }else {
            helper.itemView.findViewById(R.id.tv_direct_grade).setBackground(mContext.getResources().getDrawable(R.drawable.bg_myteam_grade3));
        }
    }
}
