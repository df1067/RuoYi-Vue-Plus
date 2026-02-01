package org.dromara.meeting.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 预约记录表 meeting_appointment
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("meeting_appointment")
public class MeetingAppointment extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @TableId(value = "appointment_id")
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
     * 预约用户姓名
     */
    private String userName;

    /**
     * 用户部门ID
     */
    private Long deptId;

    /**
     * 用户部门名称
     */
    private String deptName;

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

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;
}