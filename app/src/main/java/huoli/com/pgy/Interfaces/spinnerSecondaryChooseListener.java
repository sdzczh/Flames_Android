package huoli.com.pgy.Interfaces;

/**
 * Created by Administrator on 2018/2/8 0008.
 * 二级联动点击监听
 * @author xuqingzhong
 */

public interface spinnerSecondaryChooseListener {
    /**条目点击监听,第一个选中的是计价币，第二个选中的是交易币*/
    void onItemClickListener(int perCoin ,int tradeCoin);
}
