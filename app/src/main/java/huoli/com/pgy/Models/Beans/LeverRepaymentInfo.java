package huoli.com.pgy.Models.Beans;

import java.util.List;

/**
 * Created by YX on 2018/5/31.
 */

public class LeverRepaymentInfo {
    String risk;//"163.0%", 当前账户风险率 String
    List<ListBean> list;

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getRisk() {
        return risk;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean{
        int id;// 借款记录的id int
        String repaymentAmount;// 应还款数量 String
        String lendAmount;// 借款数量 String
        String rate;// 利息 String
        String repaymentRMB;// 应还款数量（RMB）String
        int status;// 当前还款状态 1：待还款 2：已还款 3：需要平仓 4：平仓还款 5：自动还款 int
        int coinType;// 借入币种 int
        String interest;// 总计利息 String
        String createDate;// "2018-06-04", 借款日期
        int deadline;//剩余还款日（天）int

        String risk;//"163.0%", 当前账户风险率 String
        String availBalance;// "2500", 账户可用金额 String
        String arrears;//"1500", 账户未还金额 String
        String frozenBalance;// "0", 账户冻结金额 String
        String repaymentLimit;//  1, 最大还款天数 int

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setRepaymentAmount(String repaymentAmount) {
            this.repaymentAmount = repaymentAmount;
        }

        public String getRepaymentAmount() {
            return repaymentAmount;
        }

        public void setLendAmount(String lendAmount) {
            this.lendAmount = lendAmount;
        }

        public String getLendAmount() {
            return lendAmount;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getRate() {
            return rate;
        }

        public void setRepaymentRMB(String repaymentRMB) {
            this.repaymentRMB = repaymentRMB;
        }

        public String getRepaymentRMB() {
            return repaymentRMB;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }

        public int getCoinType() {
            return coinType;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getInterest() {
            return interest;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setDeadline(int deadline) {
            this.deadline = deadline;
        }

        public int getDeadline() {
            return deadline;
        }

        public void setRisk(String risk) {
            this.risk = risk;
        }

        public String getRisk() {
            return risk;
        }

        public void setAvailBalance(String availBalance) {
            this.availBalance = availBalance;
        }

        public String getAvailBalance() {
            return availBalance;
        }

        public void setArrears(String arrears) {
            this.arrears = arrears;
        }

        public String getArrears() {
            return arrears;
        }

        public void setFrozenBalance(String frozenBalance) {
            this.frozenBalance = frozenBalance;
        }

        public String getFrozenBalance() {
            return frozenBalance;
        }

        public void setRepaymentLimit(String repaymentLimit) {
            this.repaymentLimit = repaymentLimit;
        }

        public String getRepaymentLimit() {
            return repaymentLimit;
        }
    }
}
