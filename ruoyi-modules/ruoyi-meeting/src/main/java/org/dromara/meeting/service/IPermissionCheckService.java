package org.dromara.meeting.service;

/**
 * 权限校验服务
 *
 * @author Lion Li
 */
public interface IPermissionCheckService {

    /**
     * 检查用户是否有大型厅预约权限
     * 
     * @param userId 用户ID
     * @return true:有权限 false:无权限
     */
    boolean hasLargeRoomPermission(Long userId);

    /**
     * 检查用户是否超过最大待审批预约数量
     * 
     * @param userId 用户ID
     * @return true:未超过 false:已超过
     */
    boolean checkMaxPendingAppointments(Long userId);

    /**
     * 检查用户是否为部门总监及以上级别
     * 
     * @param userId 用户ID
     * @return true:是总监及以上 false:不是
     */
    boolean isDirectorOrAbove(Long userId);

    /**
     * 检查用户是否为行政部人员
     * 
     * @param userId 用户ID
     * @return true:是行政部人员 false:不是
     */
    boolean isAdminPersonnel(Long userId);

    /**
     * 获取用户的直属上级ID
     * 
     * @param userId 用户ID
     * @return 直属上级ID，如果没有返回null
     */
    Long getDirectSupervisorId(Long userId);
}