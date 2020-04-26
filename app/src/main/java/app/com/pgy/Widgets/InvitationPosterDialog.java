package app.com.pgy.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.SimpleUtils;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/22 11:40
 */
public class InvitationPosterDialog extends Dialog {
    public InvitationPosterDialog(@NonNull Context context) {
        super(context);
    }

    public InvitationPosterDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        Context context;
        InvitationPosterDialogOnClick onClickListener;
        String invitationCode;
        String qrUrl;

        public Builder(Context context){
            this.context = context;
        }

        public Builder setInvitationCode(String invitationCode) {
            this.invitationCode = invitationCode;
            return this;
        }

        public Builder setQrUrl(String qrUrl) {
            this.qrUrl = qrUrl;
            return this;
        }

        public Builder setOnClickListener(InvitationPosterDialogOnClick onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public InvitationPosterDialog create(){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final InvitationPosterDialog dialog = new InvitationPosterDialog(context, R.style.LeverDialog);
            //获得dialog的window窗口
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            //获得window窗口的属性
            android.view.WindowManager.LayoutParams lp = window.getAttributes();
            //设置窗口宽度为充满全屏
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            //设置窗口高度为包裹内容
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.horizontalMargin = 0;
            //将设置好的属性set回去
            window.setAttributes(lp);
            window.getDecorView().setMinimumWidth(context.getResources().getDisplayMetrics().widthPixels);
//            window.getDecorView().setBackgroundColor(Color.GREEN);
            View layout = inflater.inflate(R.layout.dialog_invitation_poster, null);
            TextView tv_code = layout.findViewById(R.id.tv_invitation_dialog_code);
            tv_code.setText(invitationCode);
            ImageView iv_qr = layout.findViewById(R.id.iv_invitation_dialog_qrurl);
            ImageLoaderUtils.display(context,iv_qr,qrUrl);
            final LinearLayout ll_view = layout.findViewById(R.id.ll_save_view);

            // 保存bitmap到sd卡
            layout.findViewById(R.id.tv_invitation_dialog_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null){
                        onClickListener.savePic(true);
                    }
                    Bitmap cacheBitmapFromView = SimpleUtils.getCacheBitmapFromView(ll_view);
                    boolean isSave = SimpleUtils.saveBitmapToSdCard(context,cacheBitmapFromView,"odin_shareImg_"+invitationCode);
                    if (onClickListener != null){
                        onClickListener.savePic(false);
                    }
                    if (isSave){
                        MyToast.showToast(context,"保存成功："+ Environment.getExternalStorageDirectory() + "/1000ttt/odin_shareImg_"+invitationCode+".jpg");
                    }else {
                        MyToast.showToast(context,"保存失败");
                    }
                }
            });

            layout.findViewById(R.id.tv_invitation_dialog_share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null){
                        Bitmap cacheBitmapFromView = SimpleUtils.getCacheBitmapFromView(ll_view);
                        onClickListener.sharePic(cacheBitmapFromView);
                    }
                }
            });

            dialog.setCancelable(true);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return dialog;
        }
    }

    public interface InvitationPosterDialogOnClick{
        void savePic(boolean show);
        void sharePic(Bitmap bitmap);
    }

}
