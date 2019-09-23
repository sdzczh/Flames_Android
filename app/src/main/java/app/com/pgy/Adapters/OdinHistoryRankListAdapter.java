package app.com.pgy.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.OdinHistoryRankBean;
import app.com.pgy.R;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/23 8:38
 */
public class OdinHistoryRankListAdapter extends RecyclerArrayAdapter<OdinHistoryRankBean> {
    public OdinHistoryRankListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyOdinHistoryRankHolder(parent);
    }

    class MyOdinHistoryRankHolder extends BaseViewHolder<OdinHistoryRankBean>{

        TextView tv_title;
        TextView tv_time;

        TextView tv_rank1_tel;
        TextView tv_rank1_num;

        TextView tv_rank2_tel;
        TextView tv_rank2_num;

        TextView tv_rank3_tel;
        TextView tv_rank3_num;

        TextView tv_myRank;

        public MyOdinHistoryRankHolder(ViewGroup parent) {
            super(parent, R.layout.item_invitation_history_rank);
            tv_title = $(R.id.tv_item_invitation_rank_title);
            tv_time = $(R.id.tv_item_invitation_rank_time);
            tv_rank1_tel = $(R.id.tv_item_invitation_rank1_tel);
            tv_rank1_num = $(R.id.tv_item_invitation_rank1_num);

            tv_rank2_tel = $(R.id.tv_item_invitation_rank2_tel);
            tv_rank2_num = $(R.id.tv_item_invitation_rank2_num);

            tv_rank3_tel = $(R.id.tv_item_invitation_rank3_tel);
            tv_rank3_num = $(R.id.tv_item_invitation_rank3_num);

            tv_myRank =  $(R.id.tv_item_invitation_my_rank);
        }

        @Override
        public void setData(OdinHistoryRankBean data) {
            if (data == null){
                return;
            }
            tv_title.setText("第"+data.getNumber()+"期邀请支付榜排名");
            tv_time.setText(data.getTime()+"结算");

            tv_rank1_tel.setText(data.getOnePhone());
            tv_rank1_num.setText(data.getOneAmount());

            tv_rank2_tel.setText(data.getTwoPhone());
            tv_rank2_num.setText(data.getTwoAmount());

            tv_rank3_tel.setText(data.getThreePhone());
            tv_rank3_num.setText(data.getThreeAmount());

            tv_myRank.setText(TextUtils.isDigitsOnly(data.getRank()) ? "第"+data.getRank()+"名": "暂无排名" );
        }
    }
}
