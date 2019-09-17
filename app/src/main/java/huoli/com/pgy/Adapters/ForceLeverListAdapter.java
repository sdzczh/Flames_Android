package huoli.com.pgy.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;

/**
 * Created by YX on 2018/7/16.
 */

public class ForceLeverListAdapter extends RecyclerArrayAdapter<Configuration.CalculateForceLevel> {
    public ForceLeverListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForceLeverListHolder(parent);
    }

    class ForceLeverListHolder extends BaseViewHolder<Configuration.CalculateForceLevel>{
        private ImageView iv_icon;
        private TextView tv_values;
        public ForceLeverListHolder(ViewGroup parent) {
            super(parent, R.layout.item_force_lever_view);
            iv_icon = $(R.id.iv_item_force_lever_icon);
            tv_values = $(R.id.tv_item_force_lever_values);
        }

        @Override
        public void setData(Configuration.CalculateForceLevel data) {
            ImageLoaderUtils.display(getContext(),iv_icon,data.getRolepicurl(),R.mipmap.level_1);
            tv_values.setText(data.getSoulminforce()+"~"+data.getSoulmaxforce());
        }
    }
}
