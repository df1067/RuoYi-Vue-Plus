package org.dromara.meeting.service;

import org.dromara.meeting.domain.MeetingAppointment;
import org.dromara.meeting.domain.MeetingRoom;
import org.dromara.meeting.enums.AppointmentStatus;
import org.dromara.meeting.enums.MeetingRoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 冲突检测服务测试类
 *
 * @author Lion Li
 */
@SpringBootTest
@Transactional
public class ConflictCheckServiceTest {

    @Autowired
    private IConflictCheckService conflictCheckService;

    private MeetingRoom largeRoom;
    private MeetingRoom smallRoom;

    @BeforeEach
    public void setUp() {
        // 创建测试会议室
        largeRoom = new MeetingRoom();
        largeRoom.setRoomId(1L);
        largeRoom.setRoomName("大型会议室A");
        largeRoom.setRoomType(MeetingRoomType.LARGE.getCode());
        largeRoom.setCapacity(80);

        smallRoom = new MeetingRoom();
        smallRoom.setRoomId(2L);
        smallRoom.setRoomName("小型会议室B");
        smallRoom.setRoomType(MeetingRoomType.SMALL.getCode());
        smallRoom.setCapacity(20);
    }

    @Test
    public void testCheckTimeConflict() {
        Calendar calendar = Calendar.getInstance();
        
        // 测试时间段1: 9:00-11:00
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        Date startTime1 = calendar.getTime();
        
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        Date endTime1 = calendar.getTime();

        // 测试时间段2: 10:00-12:00 (与时间段1冲突)
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        Date startTime2 = calendar.getTime();
        
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        Date endTime2 = calendar.getTime();

        // 测试时间段3: 14:00-16:00 (与时间段1不冲突)
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        Date startTime3 = calendar.getTime();
        
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        Date endTime3 = calendar.getTime();

        // 假设没有其他预约，应该无冲突
        assertTrue(conflictCheckService.checkTimeConflict(1L, startTime1, endTime1, null));
        assertTrue(conflictCheckService.checkTimeConflict(1L, startTime3, endTime3, null));

        // 测试null参数
        assertFalse(conflictCheckService.checkTimeConflict(1L, null, endTime1, null));
        assertFalse(conflictCheckService.checkTimeConflict(1L, startTime1, null, null));

        // 测试时间不合理的情况
        assertFalse(conflictCheckService.checkTimeConflict(1L, endTime1, startTime1, null));
    }

    @Test
    public void testCheckLargeRoomCancelRestriction() {
        Calendar calendar = Calendar.getInstance();
        
        // 创建大型厅预约
        MeetingAppointment largeAppointment = new MeetingAppointment();
        largeAppointment.setAppointmentId(1L);
        largeAppointment.setRoomId(1L);
        
        // 设置会议开始时间为当前时间+12小时（不足24小时）
        calendar.add(Calendar.HOUR_OF_DAY, 12);
        largeAppointment.setStartTime(calendar.getTime());

        // 创建小型厅预约
        MeetingAppointment smallAppointment = new MeetingAppointment();
        smallAppointment.setAppointmentId(2L);
        smallAppointment.setRoomId(2L);
        smallAppointment.setStartTime(calendar.getTime());

        // 注意：这里需要实际的数据库支持才能完整测试
        // 在真实环境中，需要先将预约记录插入数据库
        
        // 测试null参数
        assertFalse(conflictCheckService.checkLargeRoomCancelRestriction(null));
    }
}