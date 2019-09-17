package huoli.com.pgy.Utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import huoli.com.pgy.Widgets.MyToast;

/**
 * Created by YX on 2018/7/30.
 */

public class EdittextUtils {

    public static InputFilter getNoEmoji(final Context context){
        InputFilter inputFilter=new InputFilter() {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher=  pattern.matcher(charSequence);
                if(!matcher.find()){
                    return null;
                }else{
                    MyToast.showToast(context,"只能输入汉字,英文，数字");
                    return "";
                }

            }
        };
        return inputFilter;
    }

    public static InputFilter getNoEmojiNoCh(final Context context){
        InputFilter inputFilter=new InputFilter() {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher=  pattern.matcher(charSequence);
                if(!matcher.find()){
                    return null;
                }else{
                    MyToast.showToast(context,"只能输入英文，数字");
                    return "";
                }

            }
        };
        return inputFilter;
    }

    public static InputFilter getAli(final Context context){
        InputFilter inputFilter=new InputFilter() {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9@.]");
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher=  pattern.matcher(charSequence);
                if(!matcher.find()){
                    return null;
                }else{
                    MyToast.showToast(context,"只能输入英文，数字");
                    return "";
                }

            }
        };
        return inputFilter;
    }
    public static InputFilter getWechat(final Context context){
        InputFilter inputFilter=new InputFilter() {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！￥\\-_]");
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher=  pattern.matcher(charSequence);
                if(!matcher.find()){
                    return null;
                }else{
                    MyToast.showToast(context,"只能输入英文，数字");
                    return "";
                }

            }
        };
        return inputFilter;
    }
}
