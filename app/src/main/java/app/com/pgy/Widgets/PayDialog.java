package app.com.pgy.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import app.com.pgy.Activitys.ChangeTradePwActivity;
import app.com.pgy.R;
import app.com.pgy.Utils.Utils;


/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:付费对话框
 * @author 徐庆重
 */
public class PayDialog extends Dialog {

    public PayDialog(Context context) {
        super(context);
    }

    public PayDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private boolean cancelable;
        private PayPsdInputView.onPasswordListener passwordListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setFinishListener(PayPsdInputView.onPasswordListener listener) {
            this.passwordListener = listener;
            return this;
        }

        public PayDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PayDialog dialog = new PayDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.layout_dialog_pay, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the confirm button
            layout.findViewById(R.id.dialog_pay_positiveButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Utils.IntentUtils(context, SetTradePasswordActivity.class);
                    Utils.IntentUtils(context, ChangeTradePwActivity.class);
                }
            });
            // set the content title
            if (!TextUtils.isEmpty(title)){
                ((TextView) layout.findViewById(R.id.dialog_pay_title)).setText(title);
                ((TextView) layout.findViewById(R.id.dialog_pay_title)).setTextColor(context.getResources().getColor(R.color.bg_orange));
            }
            PayPsdInputView payPsdInputView = layout.findViewById(R.id.dialog_pay_password);
            //弹出软键盘， AlertDialog中一般的显示方法不起作用
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            final InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            payPsdInputView.getComparePassword(new PayPsdInputView.onPasswordListener() {
                @Override
                public void getInput(String data) {
                    //此步骤要在 dialog.dismiss(); 之前执行，不然会失效
                    if (imm.isActive()){
                        if (imm.isActive()) {
                            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                        }
                    }
                    if (passwordListener != null){
                        passwordListener.getInput(data);
                        dialog.dismiss();
                    }
                    //Toast.makeText(context,"密码:::::"+data, Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
