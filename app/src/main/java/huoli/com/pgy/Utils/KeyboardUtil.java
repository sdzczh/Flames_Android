package huoli.com.pgy.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class KeyboardUtil {
    private KeyboardUtil() {
    }

    public static KeyboardUtil getInstance() {
        if (null == mInstance) {
            mInstance = new KeyboardUtil();
        }
        return mInstance;
    }

    private static KeyboardUtil mInstance;
    private int noKeyboardHeight = 0;
    private Map<Activity, OnSoftKeyboardChangeListener> onSoftKeyboardChangeListeners;
    private Map<Activity, OnGlobalLayoutListener> onGlobalLayoutListeners;
    private boolean isVisible;

    public void registerSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener mOnSoftKeyboardChangeListener) {
        if (null == activity || null == mOnSoftKeyboardChangeListener) {
            return;
        }
        if (null == onSoftKeyboardChangeListeners) {
            onSoftKeyboardChangeListeners = new HashMap<>();
        }
        onSoftKeyboardChangeListeners.put(activity, mOnSoftKeyboardChangeListener);
        final View decorView = activity.getWindow().getDecorView();
        noKeyboardHeight = getStatusBarHeight(activity);
        OnGlobalLayoutListener myLayoutListener = new OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    if (hide) {
                        noKeyboardHeight = keyboardHeight;
                    }
                    if (isVisible != !hide) {
                        mOnSoftKeyboardChangeListener.onSoftKeyBoardChange(keyboardHeight - noKeyboardHeight, !hide);
                        isVisible = !hide;
                    }
                }
                previousKeyboardHeight = height;
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(
                myLayoutListener);
        if (null == onGlobalLayoutListeners) {
            onGlobalLayoutListeners = new HashMap<>();
        }
        onGlobalLayoutListeners.put(activity, myLayoutListener);
    }

    @SuppressLint("NewApi")
    public void unRegisterSoftKeyboard(Activity activity) {
        if (activity != null && onGlobalLayoutListeners != null
                && onGlobalLayoutListeners.get(activity) != null) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                activity.getWindow().getDecorView().getViewTreeObserver()
                        .removeGlobalOnLayoutListener(onGlobalLayoutListeners.get(activity));
            } else {
                activity.getWindow().getDecorView().getViewTreeObserver()
                        .removeOnGlobalLayoutListener(onGlobalLayoutListeners.get(activity));
            }
            onSoftKeyboardChangeListeners.remove(activity);
        }
    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public static void showSoftKeyboard(Context context) {
        // 打开软键盘
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,0);
    }
//    public static void showSoftKeyboard(Activity activity) {
//        if (activity != null){
//            // 打开软键盘
//            InputMethodManager imm = (InputMethodManager) activity
//                    .getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0,0);
//        }
//
//    }


    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
//            if (inputMethodManager.isActive()) {//如果开启
//                inputMethodManager.toggleSoftInput(0,0);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
//            }
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity
                        .getCurrentFocus().getWindowToken(), 0);
            }

        }
    }

    // 获取状态栏高度
    private static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
