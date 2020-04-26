package app.com.pgy.Widgets;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.R;
import app.com.pgy.Utils.ToastUtils;

/**
 * Create by YX on 2019/8/29 0029
 */
public class InputNumDialog extends Dialog {
    public InputNumDialog(@NonNull Context context) {
        super(context);
    }

    public InputNumDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected InputNumDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public static class Builder {
        private Context context;
        private String title;
        private String hint;
        private boolean cancelable;
        private getStringCallback passwordListener;
        private String positiveButtonText;
        private String negativeButtonText;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public Builder setFinishListener(getStringCallback listener) {
            this.passwordListener = listener;
            return this;
        }

        public InputNumDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final InputNumDialog dialog = new InputNumDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.layout_dialog_input, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //弹出软键盘， AlertDialog中一般的显示方法不起作用
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            final InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            final EditText input = layout.findViewById(R.id.dialog_exit_input);
            if (!TextUtils.isEmpty(hint)){
                input.setHint(hint);
            }
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.dialog_exit_positiveButton))
                        .setText(positiveButtonText);
                layout.findViewById(R.id.dialog_exit_positiveButton)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(input.getText().toString().trim())){
                                    ToastUtils.ShortToast(context,"金额不能为空");
                                    return;
                                }
                                if (imm.isActive()){
                                    if (imm.isActive()) {
                                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                                    }
                                }
                                if (passwordListener !=null){
                                    passwordListener.getString(input.getText().toString().trim());
                                }
                                dialog.dismiss();
                            }
                        });
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.dialog_exit_positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.dialog_exit_negativeButton))
                        .setText(negativeButtonText);
                layout.findViewById(R.id.dialog_exit_negativeButton)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (imm.isActive()){
                                    if (imm.isActive()) {
                                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                                    }
                                }
                                dialog.dismiss();
                            }
                        });
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.dialog_exit_negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content title
            if (title != null) {
                ((TextView) layout.findViewById(R.id.dialog_exit_title)).setText(title);
            }else{
                layout.findViewById(R.id.dialog_exit_title).setVisibility(View.GONE);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
