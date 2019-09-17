package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.Models.Beans.BlockAssetFlow;
import huoli.com.pgy.R;

/**
 * Created by YX on 2018/7/18.
 */

public class BlockAssetsCoinFlowAdapter extends RecyclerArrayAdapter<BlockAssetFlow.ListBean> {
    private getPositionCallback getPositionCallback;
    public BlockAssetsCoinFlowAdapter(Context context) {
        super(context);
    }

    public void setGetPositionCallback(getPositionCallback getPositionCallback) {
        this.getPositionCallback = getPositionCallback;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BlockCoinFlowViewHolder(parent);
    }

    class BlockCoinFlowViewHolder extends BaseViewHolder<BlockAssetFlow.ListBean> implements View.OnClickListener{
        TextView tv_title;
        ImageView iv_question;
        TextView tv_score;
        TextView tv_time;
        TextView tv_state;
        public BlockCoinFlowViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_mywallet_flow_record_view);
            tv_title = $(R.id.tv_item_flow_record_title);
            iv_question = $(R.id.iv_item_flow_record_question);
            tv_score = $(R.id.tv_item_flow_record_score);
            tv_time = $(R.id.tv_item_flow_record_time);
            tv_state = $(R.id.tv_item_flow_record_desc);
            iv_question.setOnClickListener(this);
        }

        @Override
        public void setData(BlockAssetFlow.ListBean data) {
            switch (data.getType()){
                case StaticDatas.MYASSETS_WITHDRAWAL_OTHER:
                    tv_title.setText("提取到其他钱包");
                    iv_question.setVisibility(View.VISIBLE);
                    break;
                case StaticDatas.MYASSETS_WITHDRAWAL_GOODS:
                    tv_title.setText("提取到币币钱包");
                    iv_question.setVisibility(View.GONE);
                    break;
                default:break;
            }
            tv_score.setText(data.getAmount());
            tv_time.setText(data.getCreatetime());
            String stateName;
            int stateColor;
            switch (data.getState()) {
                default:
                    stateName = "状态";
                    stateColor = R.color.color_c4c7cc;
                    break;
                case 0:
                    stateName = "确认中";
                    stateColor = R.color.color_4d94ff;
                    break;
                case 1:
                    stateName = "提取成功";
                    stateColor = R.color.color_c4c7cc;
                    break;
                case 2:
                    stateName = "已撤销";
                    stateColor = R.color.color_ff9933;
                    break;
//                case 3:
//                    stateName = "提取失败-"+data.getRemark();
//                    stateColor = R.color.color_ff9933;
//                    break;
            }
            tv_state.setText(stateName);
            tv_state.setTextColor(getContext().getResources().getColor(stateColor));
        }

        @Override
        public void onClick(View v) {
            if (getPositionCallback != null){
                getPositionCallback.getPosition(getDataPosition());
            }
        }
    }
}
