package app.com.pgy.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.socialize.bean.SHARE_MEDIA;

import app.com.pgy.R;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/22 17:17
 */
public class ShareDialog extends Dialog {
    public ShareDialog(@NonNull Context context) {
        super(context);
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder{
        Context context;
        Bitmap bitmap;
        ShareOnClick onClick;


        public Builder(Context context){
            this.context = context;
        }

        public Builder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder setOnClick(ShareOnClick onClick) {
            this.onClick = onClick;
            return this;
        }

        public ShareDialog create(){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ShareDialog dialog = new ShareDialog(context, R.style.LeverDialog);
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
            View layout = inflater.inflate(R.layout.dialog_share_view, null);
            layout.findViewById(R.id.iv_share_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.iv_share_wx).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClick != null){
                        onClick.share(bitmap,SHARE_MEDIA.WEIXIN);
                    }
                }
            });
            layout.findViewById(R.id.iv_share_wxcircle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClick != null){
                        onClick.share(bitmap,SHARE_MEDIA.WEIXIN_CIRCLE);
                    }
                }
            });
            dialog.setCancelable(true);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return dialog;
        }
    }

    public interface ShareOnClick{
        void share(Bitmap img,SHARE_MEDIA sharePlatform);
    }
}
