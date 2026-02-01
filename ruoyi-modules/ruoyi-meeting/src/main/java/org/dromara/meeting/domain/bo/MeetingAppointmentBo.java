package org.dromara.meeting.domain.bo;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 预约记录业务对象 meeting_appointment
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingAppointmentBo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    private Long appointmentId;

    /**
     * 预约单号
     */
    private String appointmentNo;

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 预约用户ID
     */
    private Long userId;

    /**
     * 会议主题
     */
    private String meetingTitle;

    /**
     * 会议描述
     */
    private String meetingDesc;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 参会人数
     */
    private Integer attendees;

    /**
     * 状态（01草稿 02已提交 03部门审核中 04行政复核中 05已批准 06已拒绝 07已取消）
     */
    private String status;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 提交时间
     */
    private Date submitTime;
}