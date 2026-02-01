package org.dromara.meeting.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.meeting.domain.MeetingAppointment;
import org.dromara.meeting.enums.AppointmentStatus;
import org.dromara.meeting.enums.MeetingRoomType;
import org.dromara.meeting.mapper.MeetingAppointmentMapper;
import org.dromara.meeting.mapper.MeetingRoomMapper;
import org.dromara.meeting.service.IConflictCheckService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * 冲突检测服务实现类
 *
 * @author Lion Li
 */
@Service
@RequiredArgsConstructor
public class ConflictCheckServiceImpl implements IConflictCheckService {

    private final MeetingAppointmentMapper appointmentMapper;
    private final MeetingRoomMapper roomMapper;

    @Override
    public boolean checkTimeConflict(Long roomId, Date startTime, Date endTime, Long excludeAppointmentId) {
        if (startTime == null || endTime == null) {
            return false;
        }
        
        // 检查时间合理性
        if (startTime.after(endTime) || startTime.equals(endTime)) {
            return false;
        }
        
        // 查询冲突的预约记录
        var query = appointmentMapper.lambdaQuery()
            .eq(MeetingAppointment::getRoomId, roomId)
            .ne(MeetingAppointment::getStatus, AppointmentStatus.CANCELLED.getCode())
            .ne(MeetingAppointment::getStatus, AppointmentStatus.REJECTED.getCode())
            .and(wrapper -> wrapper
                // 情况1：新预约的开始时间在已有预约的时间范围内
                .or(w -> w.le(MeetingAppointment::getStartTime, startTime)
                    .ge(MeetingAppointment::getEndTime, startTime))
                // 情况2：新预约的结束时间在已有预约的时间范围内
                .or(w -> w.le(MeetingAppointment::getStartTime, endTime)
                    .ge(MeetingAppointment::getEndTime, endTime))
                // 情况3：新预约完全包含已有预约
                .or(w -> w.ge(MeetingAppointment::getStartTime, startTime)
                    .le(MeetingAppointment::getEndTime, endTime))
            );
        
        // 如果是更新操作，排除当前预约记录
        if (excludeAppointmentId != null) {
            query.ne(MeetingAppointment::getAppointmentId, excludeAppointmentId);
        }
        
        return appointmentMapper.selectCount(query) == 0;
    }

    @Override
    public boolean checkLargeRoomCancelRestriction(Long appointmentId) {
        // 获取预约记录
        MeetingAppointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            return false;
        }
        
        // 获取会议室信息
        var room = roomMapper.selectById(appointment.getRoomId());
        if (room == null || !MeetingRoomType.LARGE.getCode().equals(room.getRoomType())) {
            // 不是大型厅，可以取消
            return true;
        }
        
        // 检查是否在会议开始前24小时内
        Date now = new Date();
        Date startTime = appointment.getStartTime();
        
        if (startTime == null) {
            return false;
        }
        
        // 计算24小时前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.HOUR_OF_DAY, -24);
        Date twentyFourHoursBefore = calendar.getTime();
        
        // 如果当前时间距离会议开始时间不足24小时，则不可取消
        return now.before(twentyFourHoursBefore);
    }
}