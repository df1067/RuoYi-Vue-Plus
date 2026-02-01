package org.dromara.meeting.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.meeting.domain.MeetingAppointment;
import org.dromara.meeting.domain.MeetingApproval;
import org.dromara.meeting.domain.vo.MeetingApprovalVo;
import org.dromara.meeting.enums.AppointmentStatus;
import org.dromara.meeting.enums.ApprovalStatus;
import org.dromara.meeting.mapper.MeetingAppointmentMapper;
import org.dromara.meeting.mapper.MeetingApprovalMapper;
import org.dromara.meeting.service.IMeetingApprovalService;
import org.dromara.meeting.service.IPermissionCheckService;
import org.dromara.system.domain.SysUser;
import org.dromara.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批流程Service业务层处理
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class MeetingApprovalServiceImpl implements IMeetingApprovalService {

    private final MeetingAppointmentMapper appointmentMapper;
    private final MeetingApprovalMapper approvalMapper;
    private final IPermissionCheckService permissionCheckService;
    private final ISysUserService userService;

    @Override
    @Transactional
    public Boolean deptApprove(Long appointmentId, Boolean approved, String opinion) {
        MeetingAppointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new ServiceException("预约记录不存在");
        }
        
        // 检查当前状态是否为部门审核中
        if (!AppointmentStatus.DEPT_APPROVING.getCode().equals(appointment.getStatus())) {
            throw new ServiceException("当前状态不允许部门审批");
        }
        
        // 检查审批权限（必须是申请人的直属上级）
        Long currentUserId = LoginHelper.getUserId();
        Long supervisorId = permissionCheckService.getDirectSupervisorId(appointment.getUserId());
        
        if (!currentUserId.equals(supervisorId) && !LoginHelper.isSuperAdmin()) {
            throw new ServiceException("您不是申请人的直属上级，无法进行部门审批");
        }
        
        // 创建审批记录
        createApprovalRecord(appointment, 1, approved, opinion);
        
        // 更新预约状态
        if (approved) {
            // 部门审批通过，进入行政复核
            appointment.setStatus(AppointmentStatus.ADMIN_APPROVING.getCode());
        } else {
            // 部门审批拒绝，流程终止
            appointment.setStatus(AppointmentStatus.REJECTED.getCode());
        }
        
        appointment.setUpdateTime(new Date());
        appointment.setUpdateBy(LoginHelper.getUsername());
        
        return appointmentMapper.updateById(appointment) > 0;
    }

    @Override
    @Transactional
    public Boolean adminApprove(Long appointmentId, Boolean approved, String opinion) {
        MeetingAppointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new ServiceException("预约记录不存在");
        }
        
        // 检查当前状态是否为行政复核中
        if (!AppointmentStatus.ADMIN_APPROVING.getCode().equals(appointment.getStatus())) {
            throw new ServiceException("当前状态不允许行政审批");
        }
        
        // 检查审批权限（必须是行政部人员）
        Long currentUserId = LoginHelper.getUserId();
        if (!permissionCheckService.isAdminPersonnel(currentUserId) && !LoginHelper.isSuperAdmin()) {
            throw new ServiceException("您不是行政部人员，无法进行行政审批");
        }
        
        // 创建审批记录
        createApprovalRecord(appointment, 2, approved, opinion);
        
        // 更新预约状态
        if (approved) {
            // 行政审批通过，预约成功
            appointment.setStatus(AppointmentStatus.APPROVED.getCode());
        } else {
            // 行政审批拒绝，流程终止
            appointment.setStatus(AppointmentStatus.REJECTED.getCode());
        }
        
        appointment.setUpdateTime(new Date());
        appointment.setUpdateBy(LoginHelper.getUsername());
        
        return appointmentMapper.updateById(appointment) > 0;
    }

    private void createApprovalRecord(MeetingAppointment appointment, Integer approvalLevel, 
                                    Boolean approved, String opinion) {
        Long currentUserId = LoginHelper.getUserId();
        SysUser currentUser = userService.selectUserById(currentUserId);
        
        MeetingApproval approval = new MeetingApproval();
        approval.setAppointmentId(appointment.getAppointmentId());
        approval.setAppointmentNo(appointment.getAppointmentNo());
        approval.setApprovalLevel(approvalLevel);
        approval.setApproverId(currentUserId);
        approval.setApproverName(currentUser.getNickName());
        approval.setApproverDeptId(currentUser.getDeptId());
        approval.setApproverDeptName(currentUser.getDept() != null ? currentUser.getDept().getDeptName() : "");
        
        // 获取用户角色信息
        if (currentUser.getRoles() != null && !currentUser.getRoles().isEmpty()) {
            String roles = currentUser.getRoles().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.joining(","));
            approval.setApproverRole(roles);
        }
        
        approval.setApprovalStatus(approved ? ApprovalStatus.APPROVED.getCode() : ApprovalStatus.REJECTED.getCode());
        approval.setApprovalOpinion(opinion);
        approval.setApprovalTime(new Date());
        approval.setCreateTime(new Date());
        
        approvalMapper.insert(approval);
    }

    @Override
    public List<MeetingApprovalVo> getApprovalRecords(Long appointmentId) {
        List<MeetingApproval> approvals = approvalMapper.selectList(
            approvalMapper.lambdaQuery()
                .eq(MeetingApproval::getAppointmentId, appointmentId)
                .orderByAsc(MeetingApproval::getCreateTime)
        );
        
        return approvals.stream().map(approval -> {
            MeetingApprovalVo vo = new MeetingApprovalVo();
            org.springframework.beans.BeanUtils.copyProperties(approval, vo);
            
            // 设置审批级别名称
            if (approval.getApprovalLevel() == 1) {
                vo.setApprovalLevelName("部门审批");
            } else if (approval.getApprovalLevel() == 2) {
                vo.setApprovalLevelName("行政审批");
            }
            
            // 设置审批状态名称
            ApprovalStatus status = ApprovalStatus.getByCode(approval.getApprovalStatus());
            if (status != null) {
                vo.setApprovalStatusName(status.getInfo());
            }
            
            return vo;
        }).collect(Collectors.toList());
    }
}