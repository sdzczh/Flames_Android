package app.com.pgy.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.com.pgy.Models.Beans.KLineTime;
import app.com.pgy.R;

/**
 * K线图中的时间适配器
 *
 * @author xuqingzhong
 */
public class KLineTimeAdapter extends RecyclerView.Adapter<KLineTimeAdapter.StateHolder>{

    private Context context;
    private List<KLineTime> list;
    private app.com.pgy.Interfaces.getPositionCallback getPositionCallback;

    public void setGetPositionCallback(app.com.pgy.Interfaces.getPositionCallback getPositionCallback) {
        this.getPositionCallback = getPositionCallback;
    }

    public KLineTimeAdapter(Context context, List<KLineTime> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StateHolder(LayoutInflater.from(context).inflate(R.layout.item_kline_time, parent, false));
    }

    @Override
    public void onBindViewHolder(final StateHolder holder, final int position) {
        if (list == null){
            return;
        }
        KLineTime bean = list.get(position);
        if (bean.isSelect()){
            holder.tvState.setTextColor(context.getResources().getColor(R.color.txt_kline_time_select));
        }else{
            //holder.tvState.setBackground(context.getResources().getDrawable(R.color.black));
            holder.tvState.setTextColor(context.getResources().getColor(R.color.txt_kline));
        }
        holder.tvState.setText(list.get(position).getTxt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPositionCallback.getPosition(position);
                setSelect(position);
            }
        });
    }

    /**设置选中项*/
    public void setSelect(int position){
        for (KLineTime bean:list) {
            bean.setSelect(false);
        }
        list.get(position).setSelect(true);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size() <= 0 ? 0 : list.size();
    }
    

    public class StateHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.KLine_item_timeTxt)
        TextView tvState;
        @BindView(R.id.KLine_item_frame)
        FrameLayout tvFrame;

        public StateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}