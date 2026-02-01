package org.dromara.meeting.domain.vo;

import lombok.Data;

/**
 * 预约统计视图对象
 *
 * @author Lion Li
 */
@Data
public class MeetingStatisticsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 会议室类型（L大型 S小型）
     */
    private String roomType;

    /**
     * 会议室类型名称
     */
    private String roomTypeName;

    /**
     * 总申请数
     */
    private Long totalApplications;

    /**
     * 已批准数
     */
    private Long approvedCount;

    /**
     * 预约成功率（已批准数/总申请数）
     */
    private Double successRate;
}