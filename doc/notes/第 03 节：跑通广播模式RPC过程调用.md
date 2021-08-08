# 第 03 节：跑通广播模式RPC过程调用

<div align="center">
    <img src="https://codechina.csdn.net/KnowledgePlanet/Lottery/-/raw/master/doc/assets/img/3-00.png" width="128">
</div>

- 分支：[210804_xfg_buildFramework](https://codechina.csdn.net/KnowledgePlanet/Lottery/-/tree/210804_xfg_buildFramework)
- 描述：构建工程完成RPC接口的实现和调用

当基础的工程模块创建完成以后，还需要给整个工程注入`灵魂`，就是让它可以跑通。这个过程包括一个简单的 RPC 接口功能实现和测试调用，那么这里为了让功能体现出一个完整度，还会创建出一个库表在 RPC 调用的时候查询出库表中的数据并🔙返回结果。那么在这个分支上我们就先来完成这样一个内容的实现。

## 创建抽奖活动表

在抽奖活动的设计和开发过程中，会涉及到的表信息包括：活动表、奖品表、策略表、规则表、用户参与表、中奖信息表等，这些都会在我们随着开发抽奖的过程中不断的添加出来这些表的创建。

那么目前我们为了先把程序跑通，可以先简单的创建出一个活动表，用于实现系统对数据库的CRUD操作，也就可以被RPC接口调用。在后面陆续实现的过程中可能会有一些不断优化和调整的点，用于满足系统对需求功能的实现。

**活动表(activity)**

| 字段          | 类型         | 描述                                                     |
| ------------- | ------------ | -------------------------------------------------------- |
| id            | bigint(20)   | 自增ID                                                   |
| activityId    | bigint(20)   | 活动ID                                                   |
| activityName  | varchar(64)  | 活动名称                                                 |
| activityDesc  | varchar(128) | 活动描述                                                 |
| beginDateTime | datetime     | 开始时间                                                 |
| endDateTime   | datetime     | 结束时间                                                 |
| stockAllTotal | int(11)      | 库存(总)                                                 |
| stockDayTotal | int(11)      | 库存(日)                                                 |
| takeAllCount  | int(11)      | 每人可参与次数(总)                                       |
| takeDayCount  | int(11)      | 每人可参与次数(日)                                       |
| state         | int(4)       | 活动状态：编辑、提审、撤审、通过、运行、拒绝、关闭、开启 |
| creator       | varchar(64)  | 创建人                                                   |
| createTime    | datetime     | 创建时间                                                 |
| updateTime    | datetime     | 修改时间                                                 |

- 活动表：是一个用于配置抽奖活动的总表，用于存放活动信息，包括：ID、名称、描述、时间、库存、参与次数等。

## 