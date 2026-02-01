package org.dromara.meeting.service;

/**
 * 审批流程服务
 *
 * @author Lion Li
 */
public interface IMeetingApprovalService {

    /**
     * 部门审批
     * 
     * @param appointmentId 预约ID
     * @param approved 是否通过
     * @param opinion 审批意见
     * @return 审批结果
     */
    Boolean deptApprove(Long appointmentId, Boolean approved, String opinion);

    /**
     * 行政审批
     * 
     * @param appointmentId 预约ID
     * @param approved 是否通过
     * @param opinion 审批意见
     * @return 审批结果
     */
    Boolean adminApprove(Long appointmentId, Boolean approved, String opinion);

    /**
     * 获取预约的审批记录
     * 
     * @param appointmentId 预约ID
     * @return 审批记录列表
     */
    List<MeetingApprovalVo> getApprovalRecords(Long appointmentId);
}