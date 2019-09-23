package app.com.pgy.Constants;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Models.Beans.BuyOrSale;
import app.com.pgy.Models.Beans.C2cBusinessEntrust;
import app.com.pgy.Models.Beans.C2cNormalBusiness;
import app.com.pgy.Models.Beans.C2cNormalEntrust;
import app.com.pgy.Models.Beans.Entrust;
import app.com.pgy.Models.Beans.EntrustDetails;
import app.com.pgy.Models.Beans.ResponseBean.LastDealBean;

import static app.com.pgy.Constants.StaticDatas.BUY;

/**
 * 创建日期：2018/4/23 0023 on 下午 6:09
 * 描述:
 *
 * @author 徐庆重
 */

public class DefaultData {

    public static LastDealBean getTradeC2cLastDealList() {
        LastDealBean entrust = new LastDealBean();
        List<LastDealBean.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            LastDealBean.ListBean bean = new LastDealBean.ListBean();
            bean.setOrderType(i%2);
            bean.setAmount("1"+i);
            bean.setPrice("20"+i);
            bean.setCreateTime(i+"00"+i);
            listBeans.add(bean);
        }
        entrust.setList(listBeans);
        return entrust;
    }


    public static LastDealBean getTradeGoodsLastDealList() {
        LastDealBean entrust = new LastDealBean();
        List<LastDealBean.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            LastDealBean.ListBean bean = new LastDealBean.ListBean();
            bean.setOrderType(i%2);
            bean.setAmount("1"+i);
            bean.setPrice("20"+i);
            bean.setCreateTime(i+"00"+i);
            listBeans.add(bean);
        }
        entrust.setList(listBeans);
        return entrust;
    }

    public static List<BuyOrSale.ListBean> getGoodsList() {
        List<BuyOrSale.ListBean> list = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            BuyOrSale.ListBean bean = new BuyOrSale.ListBean();
            bean.setNum(i);
            bean.setPrice("0.00");
            bean.setRemain("0.00");
            list.add(bean);
        }
        return list;
    }

    public static Entrust getTradeGoodsCurrentEntrustList() {
        Entrust entrust = new Entrust();
        List<Entrust.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            Entrust.ListBean bean = new Entrust.ListBean();
            bean.setOrderCoinType(i%8);
            bean.setUnitCoinType(i%8+2);
            bean.setOrderType(i%2);
            bean.setAmount("1"+i);
            bean.setPrice("20"+i);
            bean.setDealAmount(i*10+20+"");
            bean.setCreateTime(i+"00"+i);
            listBeans.add(bean);
        }
        entrust.setList(listBeans);
        return entrust;
    }

    public static EntrustDetails getEntrustDetails(int id) {
        EntrustDetails details = new EntrustDetails();
        details.setAmount("101");
        details.setAverage("55");
        details.setCreateTime("12-12-12");
        details.setDealAmount("22");
        details.setOrderCoinType(4);
        details.setTotal("222");
        details.setUnitCoinType(1);
        details.setType(BUY);
        List<EntrustDetails.ListBean> list = new ArrayList<>();
        for (int i=0;i<13;i++){
            EntrustDetails.ListBean bean = new EntrustDetails.ListBean();
            bean.setAmount(i+"0");
            bean.setCreateTime("12-12-1"+i);
            bean.setOrderCoinType(4);
            bean.setUnitCoinType(1);
            bean.setPrice(i+"0");
            bean.setTotal("20"+i);
            list.add(bean);
        }
        details.setList(list);
        return details;
    }

    public static C2cNormalBusiness getTradeC2cBuyNormalList() {
        C2cNormalBusiness entrust = new C2cNormalBusiness();
        List<C2cNormalBusiness.ListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            C2cNormalBusiness.ListBean bean = new C2cNormalBusiness.ListBean();
            bean.setPayType(i%8);
            bean.setUserName("测试人员"+i);
            bean.setPrice("20"+i);
            bean.setRemain(i*10+20+"");
            bean.setQuantity(i*10+i);
            bean.setTotalMin(i*10+"");
            bean.setTotalMax(i*11+"");
            listBeans.add(bean);
        }
        entrust.setList(listBeans);
        return entrust;
    }

    public static List<C2cNormalEntrust.ListBean> getC2CTradeOrder(int tradeType, int stateType) {
         List<C2cNormalEntrust.ListBean> list = new ArrayList<>();
        for (int i =1;i<20;i++){
            C2cNormalEntrust.ListBean bean = new C2cNormalEntrust.ListBean();
            bean.setPrice("6.70"+i);
            bean.setAmount("100"+i);
            bean.setCreateTime("2018-01-01 15:00:00");
            bean.setTotal("6700.00");
            bean.setId(i);
            if (stateType < 0) {
                bean.setState(i % 6);
            }else{
                bean.setState(tradeType);
            }
            if (tradeType < 0){
                bean.setOrderType(i % 2);
            }else {
                bean.setOrderType(stateType);
            }
            bean.setInactiveTime("16:00");
            list.add(bean);
        }
        return list;
    }

    public static List<C2cBusinessEntrust.ListBean> getC2cEntrustList(int tradeType, int stateType) {
        List<C2cBusinessEntrust.ListBean> list = new ArrayList<>();
        for (int i =1;i<20;i++){
            C2cBusinessEntrust.ListBean bean = new C2cBusinessEntrust.ListBean();
            bean.setPrice("6.70"+i);
            bean.setRemain(i+"00.00");
            bean.setAmount("100"+i);
            bean.setTotalMin(i+"00.00");
            bean.setTotalMax(i+"000.00");
            bean.setCreateTime("2018-01-01 15:00:00");
            bean.setId(i);
            if (stateType<0) {
                bean.setState(i % 3);
            }else{
                bean.setState(stateType);
            }
            if (tradeType<0) {
                bean.setOrderType(i % 2);
            }else {
                bean.setOrderType(tradeType);
            }
            bean.setOrderFlag((i%2)==0);
            bean.setPayType(i%8);
            list.add(bean);
        }
        return list;
    }

   /* *//**获取算力等级划分*//*
    public static List<Configuration.CalculateForceLevel> getForceLevels() {
        List<Configuration.CalculateForceLevel> list = new ArrayList<>();
        for (int i=1;i<=8;i++){
            Configuration.CalculateForceLevel forceLevel = new Configuration.CalculateForceLevel();
            forceLevel.setLevel(i);
            forceLevel.setName("等级"+i);
            forceLevel.setStart(i*100);
            forceLevel.setEnd(i*100+99);
            forceLevel.setHomeName("村落"+i);
            List<Integer> coins = new ArrayList<>();
            for (int j=1;j<=i;j++) {
                coins.add(j);
            }
            forceLevel.setDigCoinType(coins);
            list.add(forceLevel);

        }
        return list;
    }

    public static Map<Integer,Configuration.CalculateForceLevel> getLevelDetailMap() {
        Map<Integer,Configuration.CalculateForceLevel> levelDetailMap = new HashMap<>();
        for (Configuration.CalculateForceLevel level:getForceLevels()) {
            levelDetailMap.put(level.getLevel(),level);
        }
        return levelDetailMap;
    }*/
}
