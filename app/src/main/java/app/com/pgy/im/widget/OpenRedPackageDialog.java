package app.com.pgy.im.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.im.server.response.GetRedPacketStateResponse;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:打开红包对话框
 * @author 徐庆重
 */
public class OpenRedPackageDialog extends Dialog {

    public OpenRedPackageDialog(Context context) {
        super(context);
    }

    public OpenRedPackageDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String iconUrl;
        private String userName;
        private String content;
        private boolean cancelable;
        private OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public void setPositiveButtonClickListener(OnClickListener positiveButtonClickListener) {
            this.positiveButtonClickListener = positiveButtonClickListener;
        }

        public Builder setRedPacket(GetRedPacketStateResponse redPacket) {
            this.iconUrl = redPacket.getHeadPath();
            this.userName = redPacket.getName();
            this.content = redPacket.getNote();
            return this;
        }

        public OpenRedPackageDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final OpenRedPackageDialog dialog = new OpenRedPackageDialog(context, R.style.transferDialog);
            View layout = inflater.inflate(R.layout.layout_dialog_open_redpackage, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            final ImageView open = layout.findViewById(R.id.dialog_openRedPackage_open);
            // set the confirm button
                if (positiveButtonClickListener != null) {
                    open.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ObjectAnimator anim = ObjectAnimator.ofFloat(open, "rotationY", 0f, 360f);
                                    // 动画的持续时间，执行多久？
                                    anim.setDuration(1000);
                                    anim.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            open.setClickable(false);
                                            positiveButtonClickListener.onClick(dialog,
                                                    DialogInterface.BUTTON_POSITIVE);
                                            super.onAnimationStart(animation);
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            open.setClickable(true);
                                            dialog.dismiss();
                                            super.onAnimationEnd(animation);
                                        }
                                    });
                                    anim.start();
                                }
                            });
                }

            ImageView icon = layout.findViewById(R.id.dialog_openRedPackage_icon);
            ImageLoaderUtils.displayCircle(context,icon,iconUrl);
            ((TextView) layout.findViewById(R.id.dialog_openRedPackage_userName)).setText(userName);
            ((TextView) layout.findViewById(R.id.dialog_openRedPackage_content)).setText(content);
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
