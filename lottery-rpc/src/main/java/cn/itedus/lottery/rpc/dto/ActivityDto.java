package cn.itedus.lottery.rpc.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack)
 */
public class ActivityDto implements Serializable {

    // 活动ID
    private Long activityId;

    // 活动名称
    private String activityName;

    // 活动描述
    private String activityDesc;

    // 开始时间
    private Date beginDateTime;

    // 结束时间
    private Date endDateTime;

    // 库存(总)
    private Integer stockAllTotal;

    // 库存(日)
    private Integer stockDayTotal;

    // 每人可参与次数(总)
    private Integer takeAllCount;

    // 每人可参与次数(日)
    private Integer takeDayCount;

    // 活动状态：编辑、提审、撤审、通过、运行、拒绝、关闭、开启
    private Integer state;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public Date getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(Date beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Integer getStockAllTotal() {
        return stockAllTotal;
    }

    public void setStockAllTotal(Integer stockAllTotal) {
        this.stockAllTotal = stockAllTotal;
    }

    public Integer getStockDayTotal() {
        return stockDayTotal;
    }

    public void setStockDayTotal(Integer stockDayTotal) {
        this.stockDayTotal = stockDayTotal;
    }

    public Integer getTakeAllCount() {
        return takeAllCount;
    }

    public void setTakeAllCount(Integer takeAllCount) {
        this.takeAllCount = takeAllCount;
    }

    public Integer getTakeDayCount() {
        return takeDayCount;
    }

    public void setTakeDayCount(Integer takeDayCount) {
        this.takeDayCount = takeDayCount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
