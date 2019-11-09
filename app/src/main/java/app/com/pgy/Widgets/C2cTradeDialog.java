package app.com.pgy.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import app.com.pgy.R;
import app.com.pgy.Utils.ToastUtils;

/**
 * Create by Android on 2019/10/22 0022
 */
public class C2cTradeDialog extends Dialog {
    public C2cTradeDialog(@NonNull Context context) {
        super(context);
    }

    public C2cTradeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String orderNo;
        private String coinName;
        private String amount;
        private String action;
        private String desc;
        private String confirmDes;
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean cancelable;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setOrderNo(String orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public Builder setCoinName(String coinName) {
            this.coinName = coinName;
            return this;
        }

        public Builder setAmount(String amount) {
            this.amount = amount;
            return this;
        }

        public Builder setAction(String action) {
            this.action = action;
            return this;
        }

        public Builder setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder setConfirmDes(String confirmDes) {
            this.confirmDes = confirmDes;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public C2cTradeDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final C2cTradeDialog dialog = new C2cTradeDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.layout_dialog_c2c, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            // set the confirm button
            TextView tvOrderNo = layout.findViewById(R.id.tv_dialog_c2c_orderNo);
            TextView tvcoinName = layout.findViewById(R.id.tv_dialog_c2c_coinName);
            TextView tvAmount = layout.findViewById(R.id.tv_dialog_c2c_amount);
            TextView tvAction = layout.findViewById(R.id.tv_dialog_c2c_action);
            TextView tvDes = layout.findViewById(R.id.tv_dialog_c2c_desc);
            TextView tvConfirm = layout.findViewById(R.id.tv_dialog_c2c_confirm);
            final CheckBox cbConfirm = layout.findViewById(R.id.cb_dialog_c2c_confiirm);
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.tv_dialog_c2c_pos))
                        .setText(positiveButtonText);

            }
            if (positiveButtonClickListener != null) {
                layout.findViewById(R.id.tv_dialog_c2c_pos)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(confirmDes)){
                                    if (!cbConfirm.isChecked()){
                                        ToastUtils.LongToast(context,"请先确认");
                                        return;
                                    }
                                }
                                positiveButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.tv_dialog_c2c_nav))
                        .setText(negativeButtonText);

            }
            if (negativeButtonClickListener != null) {
                layout.findViewById(R.id.tv_dialog_c2c_nav)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
            }



            tvOrderNo.setText(TextUtils.isEmpty(orderNo)?"":orderNo);
            tvcoinName.setText(TextUtils.isEmpty(coinName)?"":coinName+"/CNY");
            tvAmount.setText(TextUtils.isEmpty(amount)?"":amount+"CNY");
            tvAction.setText(TextUtils.isEmpty(action)?"":action);
           if (!TextUtils.isEmpty(desc)){
               tvDes.setText(desc);
           }
            if (!TextUtils.isEmpty(confirmDes)){
                tvConfirm.setVisibility(View.VISIBLE);
                cbConfirm.setVisibility(View.VISIBLE);
                tvConfirm.setText(confirmDes);
            }else {
                tvConfirm.setVisibility(View.GONE);
                cbConfirm.setVisibility(View.GONE);
            }
            dialog.setCancelable(isCancelable());
            dialog.setContentView(layout);
            return dialog;
        }

        public boolean isCancelable() {
            return cancelable;
        }

        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }
    }
}
