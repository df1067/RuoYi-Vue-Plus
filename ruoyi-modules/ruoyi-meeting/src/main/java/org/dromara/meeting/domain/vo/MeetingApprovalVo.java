package org.dromara.meeting.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 审批流程记录视图对象
 *
 * @author Lion Li
 */
@Data
public class MeetingApprovalVo {

    private static final long serialVersionUID = 1L;

    /**
     * 审批ID
     */
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
     * 审批级别名称
     */
    private String approvalLevelName;

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
     * 审批人部门名称
     */
    private String approverDeptName;

    /**
     * 审批人角色
     */
    private String approverRole;

    /**
     * 审批状态（01待审批 02已通过 03已拒绝）
     */
    private String approvalStatus;

    /**
     * 审批状态名称
     */
    private String approvalStatusName;

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