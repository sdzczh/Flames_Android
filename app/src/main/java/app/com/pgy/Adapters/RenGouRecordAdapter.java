package app.com.pgy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Models.RenGouRecord;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
import butterknife.BindView;

/**
 * Created by YX on 2018/7/16.
 */

public class RenGouRecordAdapter extends RecyclerArrayAdapter<RenGouRecord> {

    public RenGouRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new RenGouRecordViewHolder(parent);
    }


    class RenGouRecordViewHolder extends BaseViewHolder<RenGouRecord> {
        TextView itemRenGouRecordFlames;
        TextView itemRenGouRecordResult;
        TextView itemRenGouRecordContains;
        TextView itemRenGouRecordTime;

        public RenGouRecordViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_rengou_record);
            itemRenGouRecordFlames = $(R.id.item_renGouRecord_flames);
            itemRenGouRecordResult = $(R.id.item_renGouRecord_result);
            itemRenGouRecordContains = $(R.id.item_renGouRecord_contains);
            itemRenGouRecordTime = $(R.id.item_renGouRecord_time);
        }

        @Override
        public void setData(RenGouRecord data) {
            int amount = data.getAmount();
            itemRenGouRecordFlames.setText(MathUtils.int2String(amount).concat("Flames"));

            String createTime = data.getCreateTime();
            itemRenGouRecordTime.setText(createTime);

            int state = data.getState();//状态 0处理中 1成功
            if (state == 0){
                itemRenGouRecordResult.setText("系统处理中");
                itemRenGouRecordResult.setTextColor(Color.parseColor("#EE6560"));
            }else{
                itemRenGouRecordResult.setText("成功");
                itemRenGouRecordResult.setTextColor(Color.parseColor("#4DB872"));
            }

            int usdtAmount = data.getUsdtAmount();
            if (usdtAmount > 0){
                itemRenGouRecordContains.setVisibility(View.VISIBLE);
                int realityAmount = data.getRealityAmount();
                itemRenGouRecordContains.setText(MathUtils.int2String(realityAmount).concat(" Flames ").concat(MathUtils.int2String(usdtAmount).concat(" USDT")));
            }else{
                itemRenGouRecordContains.setVisibility(View.INVISIBLE);
            }
        }

    }
}
