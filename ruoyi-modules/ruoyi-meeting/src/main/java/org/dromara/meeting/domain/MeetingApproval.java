package org.dromara.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 审批流程记录表 meeting_approval
 *
 * @author Lion Li
 */
@Data
@TableName("meeting_approval")
public class MeetingApproval {

    private static final long serialVersionUID = 1L;

    /**
     * 审批ID
     */
    @TableId(value = "approval_id")
    private Long approvalId;

    /**
     * 预约ID
     */
    private Long appointmentId;

    /**
     * 预约单号
     */
    private String appointmentNo;

    /**
     * 审批级别（1部门审批 2行政审批）
     */
    private Integer approvalLevel;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 审批人部门ID
     */
    private Long approverDeptId;

    /**
     * 审批人角色
     */
    private String approverRole;

    /**
     * 审批状态（01待审批 02已通过 03已拒绝）
     */
    private String approvalStatus;

    /**
     * 审批意见
     */
    private String approvalOpinion;

    /**
     * 审批时间
     */
    private Date approvalTime;

    /**
     * 创建时间
     */
    private Date createTime;
}