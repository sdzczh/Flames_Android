package app.com.pgy.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import app.com.pgy.R;

/**
 * Created by xuqingzhong on 2016/10/15.
 * 图片加载工具类，本次用的是Glide,缓存为默认的
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error,int x,int y) {
        if(imageView == null || context == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(url).placeholder(placeholder).error(error)
                /**缓存策略：
                 all:缓存源资源和转换后的资源
                 none:不作任何磁盘缓存
                 source:缓存源资源
                 result：缓存转换后的资源*/
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                /**图片缩放:
                 * CenterCrop：等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。
                 * FitCenter：等比例缩放图片，宽或者是高等于ImageView的宽或者是高。
                 * */
                .centerCrop()
                /**Glide 会根据ImageView的大小，自动限制图片缓存和内存中的大小，
                 * 当然也可以通过调用override(horizontalSize, verticalSize)限制图片的大小：*/
                .override(x,y)  //设置图片尺寸
                .crossFade()        //设置淡入淡出效果，默认300ms，可以传参
                .thumbnail(0.1f)    //加载缩略图
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url,int x,int y) {
        if(imageView == null || context == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(R.color.txt_all9)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .override(x,y)//article 240,140 video375 ,210
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url,int placeOrError) {
        if(imageView == null || context == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeOrError).error(placeOrError)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if(imageView == null || context == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(R.color.txt_all9).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }
    public static void displayWithCache(Context context, ImageView imageView, String url) {
        if(imageView == null || context == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(R.color.txt_all9).dontAnimate().error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }
    public static void displayWithCache(Context context, ImageView imageView, String url,int placeOrError) {
        if(imageView == null || context == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeOrError).dontAnimate().error(placeOrError)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }
    public static void displayWithCacheNoError(Context context, ImageView imageView, String url) {
        if(imageView == null || context == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }
    public static void displayCircle(final Context context,final ImageView imageView, String url){
        if(imageView == null || context == null ||TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.mipmap.touxiang);
        }else {
            Glide.with(context).load(url).asBitmap().error(R.mipmap.touxiang).placeholder(R.mipmap.touxiang).diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }
    public static void display(Context context, ImageView imageView, int resourceId) {
        if(imageView == null || context == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(resourceId).placeholder(R.color.txt_all9).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

}
