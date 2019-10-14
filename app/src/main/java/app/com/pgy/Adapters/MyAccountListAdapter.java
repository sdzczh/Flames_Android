package app.com.pgy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.MyWallet;
import app.com.pgy.R;
import app.com.pgy.Utils.ToolsUtils;

/**
 * Create by Android on 2019/10/14 0014
 */
public class MyAccountListAdapter extends RecyclerArrayAdapter<MyWallet.ListBean> {
    private getPositionCallback callback;

    public MyAccountListAdapter(Context context) {
        super(context);
    }

    public void setCallback(getPositionCallback callback) {
        this.callback = callback;
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    class MyAccountViewHolder extends BaseViewHolder<MyWallet.ListBean> implements View.OnClickListener {
        LinearLayout llBg;
        TextView tvName,tvAmount,tvFrozen;
        public MyAccountViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_my_account_coin_view);
            llBg = $(R.id.ll_item_my_account);
            tvName = $(R.id.tv_item_my_account_name);
            tvAmount = $(R.id.tv_item_my_account_amount);
            tvFrozen = $(R.id.tv_item_my_account_frozen);
            llBg.setOnClickListener(this);
        }

        @Override
        public void setData(MyWallet.ListBean myWallet) {
            Configuration.CoinInfo coinInfo = ToolsUtils.getCoinInfo(myWallet.getCoinType());
            tvAmount.setText(coinInfo.getCoinname());

            /*可用余额*/
            tvAmount.setText(myWallet.getTotalBalance());
            /*折合人民币*/
//            tv_total_cny.setText(myWallet.getTotalOfCny());

        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.getPosition(getPosition());
            }
        }
    }
}
