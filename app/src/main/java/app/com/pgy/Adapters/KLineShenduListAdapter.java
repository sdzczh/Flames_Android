package app.com.pgy.Adapters;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.security.biometrics.face.auth.util.DisplayUtil;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.com.pgy.BR;
import app.com.pgy.Models.Beans.PushBean.RecordsBean;
import app.com.pgy.R;

/**
 * K线图成交列表适配器
 *
 * @author xuqingzhong
 */
public class KLineShenduListAdapter extends RecyclerView.Adapter<KLineShenduListAdapter.BankCardViewHolder> {
    private List<List<String>> list = new ArrayList<>();
    private List<List<String>> list2 = new ArrayList<>();

    public void setData(List<List<String>> lists, List<List<String>> list2) {
        this.list = lists;
        this.list2 = list2;
        notifyDataSetChanged();
    }

    @Override
    public KLineShenduListAdapter.BankCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BankCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shendu, parent, false));
    }


    @Override
    public void onBindViewHolder(KLineShenduListAdapter.BankCardViewHolder holder, int position) {
        View bL = holder.itemView.findViewById(R.id.view_bottom);
        View bR = holder.itemView.findViewById(R.id.view_bottom_r);

        ViewDataBinding bind = DataBindingUtil.bind(holder.itemView);
        List<String> listL = new ArrayList<>();
        List<String> listR = new ArrayList<>();
        if (position < list2.size()) {
            listR = list2.get(position);
            if (listR.size() < 4) {
                listR.add(0, (position + 1) + "");
            }
        } else {
            listR.add("");
            listR.add("");
            listR.add("");
            listR.add("0");
        }

        if (position < list.size()) {
            listL = list.get(position);
            if (listL.size() < 4) {
                listL.add(0, (position + 1) + "");
            }
        } else {
            listL.add("");
            listL.add("");
            listL.add("");
            listL.add("0");
        }

        bL.getLayoutParams().width = DisplayUtil.getScreenMetrics(holder.itemView.getContext()).x * Integer.parseInt(listL.get(3)) / 10;
        bR.getLayoutParams().width = DisplayUtil.getScreenMetrics(holder.itemView.getContext()).x * Integer.parseInt(listR.get(3)) / 10;
        bind.setVariable(BR.item, listL);
        bind.setVariable(BR.itemRight, listR);
        bind.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size() > list2.size() ? list.size() : list2.size();
    }

    class BankCardViewHolder extends RecyclerView.ViewHolder {

        public BankCardViewHolder(View itemView) {
            super(itemView);
        }
    }
}