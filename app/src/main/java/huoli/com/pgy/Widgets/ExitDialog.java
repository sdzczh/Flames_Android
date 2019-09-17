package huoli.com.pgy.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import huoli.com.pgy.R;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:退出对话框
 * @author 徐庆重
 */
public class ExitDialog extends Dialog {

    public ExitDialog(Context context) {
        super(context);
    }

    public ExitDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String android_msg;
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean cancelable;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

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
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setAndroid_msg(String android_msg) {
            this.android_msg = android_msg;
            return this;
        }

        /**
         * Set the Dialog message from resource
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
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

        public ExitDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ExitDialog dialog = new ExitDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.layout_dialog_exit, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            // set the confirm button
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.dialog_exit_positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.dialog_exit_positiveButton)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.dialog_exit_positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.dialog_exit_negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    layout.findViewById(R.id.dialog_exit_negativeButton)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
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
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.dialog_exit_message)).setText(Html.fromHtml(message));
            }else{
                layout.findViewById(R.id.dialog_exit_message).setVisibility(View.GONE);
                if (android_msg != null) {
                    layout.findViewById(R.id.dialog_exit_message).setVisibility(View.VISIBLE);
                    ((TextView) layout.findViewById(R.id.dialog_exit_message)).setText(android_msg);
                }else{
                    layout.findViewById(R.id.dialog_exit_message).setVisibility(View.GONE);
                }
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
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
