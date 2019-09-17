package huoli.com.pgy.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import huoli.com.pgy.R;

/**
 * Created by YX on 2018/7/18.
 */

public class ShowMessageDialog extends Dialog {
    public ShowMessageDialog(@NonNull Context context) {
        super(context);
    }

    public ShowMessageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean cancelable;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
        public Builder setTitle(String title) {
            this.title = title;
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

        public ShowMessageDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ShowMessageDialog dialog = new ShowMessageDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_show_message, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv_negative = layout.findViewById(R.id.tv_dialog_message_negative);
            if (TextUtils.isEmpty(negativeButtonText)){
                tv_negative.setVisibility(View.GONE);
            }else {
                tv_negative.setVisibility(View.VISIBLE);
                tv_negative.setText(negativeButtonText);
                if (negativeButtonClickListener != null){
                    tv_negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            TextView tv_positive = layout.findViewById(R.id.tv_dialog_message_positive);
            if (TextUtils.isEmpty(positiveButtonText)){
                tv_positive.setVisibility(View.GONE);
            }else {
                tv_positive.setVisibility(View.VISIBLE);
                tv_positive.setText(positiveButtonText);
                if (positiveButtonClickListener != null){
                    tv_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            }
            // set the content message
            TextView tv_title = layout.findViewById(R.id.tv_dialog_message_title);
            if (TextUtils.isEmpty(title)) {
                tv_title.setVisibility(View.GONE);
            }else {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(title);
            }

            TextView tv_message = layout.findViewById(R.id.tv_dialog_message_content);
            if (TextUtils.isEmpty(message)) {
                tv_message.setVisibility(View.GONE);
            }else {
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(message);
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
