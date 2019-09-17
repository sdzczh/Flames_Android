package huoli.com.pgy.Widgets;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/11/17 0017.
 * 自定义k线图data数据，保留两位小数
 */
public class MyValueFormatter implements ValueFormatter {
    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("######0.00"); // use one
        /* NumberFormat 是以每三位數加上逗號的格式化
         * DecimalFormat 是可以自定義顯示的數字格式
         * # : digit 不顯示零
         * 0 : digit 會補零
         * @ : 顯示幾位, ex. 12345[@@@]=12300
         * format 用法可以參考以下網址
         * http://developer.android.com/intl/zh-tw/reference/java/text/DecimalFormat.html
         */
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        /* ValueFormatter 一定要實作此方法
        * 此方法會把 value 跟 entry 傳過來，在這邊可以做一些邏輯上的處理
        * 回傳的 format 字串將會顯示在 Label 上 */
        return mFormat.format(value);
    }
}
