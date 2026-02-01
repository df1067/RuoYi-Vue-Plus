package org.dromara.meeting.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.meeting.enums.AppointmentStatus;
import org.dromara.meeting.mapper.MeetingAppointmentMapper;
import org.dromara.meeting.service.IPermissionCheckService;
import org.dromara.system.domain.SysRole;
import org.dromara.system.domain.SysUser;
import org.dromara.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 权限校验服务实现类
 *
 * @author Lion Li
 */
@Service
@RequiredArgsConstructor
public class PermissionCheckServiceImpl implements IPermissionCheckService {

    private final ISysUserService userService;
    private final MeetingAppointmentMapper appointmentMapper;

    /**
     * 总监及以上角色标识
     */
    private static final List<String> DIRECTOR_ROLES = Arrays.asList("director", "manager", "ceo", "coo");
    
    /**
     * 行政部角色标识
     */
    private static final String ADMIN_DEPT_CODE = "admin";
    
    /**
     * 最大待审批预约数量
     */
    private static final int MAX_PENDING_APPOINTMENTS = 2;

    @Override
    public boolean hasLargeRoomPermission(Long userId) {
        // 超级管理员有权限
        if (LoginHelper.isSuperAdmin(userId)) {
            return true;
        }
        
        // 检查是否为总监及以上级别
        return isDirectorOrAbove(userId);
    }

    @Override
    public boolean checkMaxPendingAppointments(Long userId) {
        // 获取用户待审批的预约数量（包括已提交、部门审核中、行政复核中状态）
        List<String> pendingStatuses = Arrays.asList(
            AppointmentStatus.SUBMITTED.getCode(),
            AppointmentStatus.DEPT_APPROVING.getCode(),
            AppointmentStatus.ADMIN_APPROVING.getCode()
        );
        
        long count = appointmentMapper.selectCount(
            appointmentMapper.lambdaQuery()
                .eq(MeetingAppointment::getUserId, userId)
                .in(MeetingAppointment::getStatus, pendingStatuses)
                .eq(MeetingAppointment::getDelFlag, "0")
        );
        
        return count < MAX_PENDING_APPOINTMENTS;
    }

    @Override
    public boolean isDirectorOrAbove(Long userId) {
        try {
            SysUser user = userService.selectUserById(userId);
            if (user == null) {
                return false;
            }
            
            // 获取用户角色列表
            List<SysRole> roles = user.getRoles();
            if (roles == null || roles.isEmpty()) {
                return false;
            }
            
            // 检查角色权限标识
            return roles.stream()
                .anyMatch(role -> DIRECTOR_ROLES.contains(role.getRoleKey()));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isAdminPersonnel(Long userId) {
        try {
            SysUser user = userService.selectUserById(userId);
            if (user == null || user.getDept() == null) {
                return false;
            }
            
            // 检查部门类别编码是否为行政部
            return ADMIN_DEPT_CODE.equals(user.getDept().getDeptCategory());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long getDirectSupervisorId(Long userId) {
        try {
            SysUser user = userService.selectUserById(userId);
            if (user == null || user.getDept() == null) {
                return null;
            }
            
            // 获取部门负责人
            Long leaderId = user.getDept().getLeader();
            if (leaderId != null && !leaderId.equals(userId)) {
                return leaderId;
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}