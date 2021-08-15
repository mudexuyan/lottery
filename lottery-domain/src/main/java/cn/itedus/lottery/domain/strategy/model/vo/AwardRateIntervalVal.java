package cn.itedus.lottery.domain.strategy.model.vo;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack)
 *
 * 奖品概率区间值
 */
public class AwardRateIntervalVal {

    // 奖品ID
    private String awardId;
    // 区间开始值
    private int begin;
    // 区间结尾值
    private int end;

    public AwardRateIntervalVal() {
    }

    public AwardRateIntervalVal(String awardId, int begin, int end) {
        this.awardId = awardId;
        this.begin = begin;
        this.end = end;
    }

    public String getAwardId() {
        return awardId;
    }

    public void setAwardId(String awardId) {
        this.awardId = awardId;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
