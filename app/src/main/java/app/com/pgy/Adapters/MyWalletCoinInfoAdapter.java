package app.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.Beans.CoinFlowDetail;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.ToolsUtils;

/**
 * Created by YX on 2018/7/17.
 */

public class MyWalletCoinInfoAdapter extends RecyclerArrayAdapter<CoinFlowDetail.ListBean> {
    private int coinType;
    public MyWalletCoinInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CoinInfoViewHolder(parent);
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    class CoinInfoViewHolder extends BaseViewHolder<CoinFlowDetail.ListBean>{
        TextView tv_title;
        TextView tv_score;
        TextView tv_time;
        TextView tv_desc;
        public CoinInfoViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_mywallet_flow_record_view);
            tv_title = $(R.id.tv_item_flow_record_title);
            tv_score = $(R.id.tv_item_flow_record_score);
            tv_time = $(R.id.tv_item_flow_record_time);
            tv_desc = $(R.id.tv_item_flow_record_desc);
        }

        @Override
        public void setData(CoinFlowDetail.ListBean data) {

            tv_title.setText(""+data.getOpertype());
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(coinType);
            tv_desc.setText(coinInfo.getCoinname()+"");
            tv_time.setText(data.getTime());
            Double aDouble = MathUtils.string2Double(data.getResultAmount());
            if (aDouble >= 0){
                tv_score.setTextColor(getContext().getResources().getColor(R.color.txt_01C18B));
                tv_score.setText("+"+data.getResultAmount());
            }else {
                tv_score.setTextColor(getContext().getResources().getColor(R.color.txt_F66950));
                tv_score.setText(data.getResultAmount());
            }
        }
    }
}
