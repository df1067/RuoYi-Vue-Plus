package org.dromara.meeting.service;

/**
 * 冲突检测服务
 *
 * @author Lion Li
 */
public interface IConflictCheckService {

    /**
     * 检查会议室时间冲突
     * 
     * @param roomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param excludeAppointmentId 排除的预约ID（更新时用到）
     * @return true:无冲突 false:有冲突
     */
    boolean checkTimeConflict(Long roomId, Date startTime, Date endTime, Long excludeAppointmentId);

    /**
     * 检查大型厅24小时内取消限制
     * 
     * @param appointmentId 预约ID
     * @return true:可以取消 false:不可取消
     */
    boolean checkLargeRoomCancelRestriction(Long appointmentId);
}