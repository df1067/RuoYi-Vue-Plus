package org.dromara.meeting.service;

import org.dromara.meeting.enums.AppointmentStatus;
import org.dromara.meeting.enums.ApprovalStatus;
import org.dromara.meeting.enums.MeetingRoomType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 枚举测试类
 *
 * @author Lion Li
 */
@SpringBootTest
public class EnumsTest {

    @Test
    public void testMeetingRoomType() {
        assertEquals("L", MeetingRoomType.LARGE.getCode());
        assertEquals("大型厅", MeetingRoomType.LARGE.getInfo());
        assertEquals("S", MeetingRoomType.SMALL.getCode());
        assertEquals("小型厅", MeetingRoomType.SMALL.getInfo());
        
        assertEquals(MeetingRoomType.LARGE, MeetingRoomType.getByCode("L"));
        assertEquals(MeetingRoomType.SMALL, MeetingRoomType.getByCode("S"));
        assertNull(MeetingRoomType.getByCode("X"));
    }

    @Test
    public void testAppointmentStatus() {
        assertEquals("01", AppointmentStatus.DRAFT.getCode());
        assertEquals("草稿", AppointmentStatus.DRAFT.getInfo());
        assertEquals("05", AppointmentStatus.APPROVED.getCode());
        assertEquals("已批准", AppointmentStatus.APPROVED.getInfo());
        
        assertEquals(AppointmentStatus.DRAFT, AppointmentStatus.getByCode("01"));
        assertEquals(AppointmentStatus.APPROVED, AppointmentStatus.getByCode("05"));
        assertNull(AppointmentStatus.getByCode("99"));
    }

    @Test
    public void testApprovalStatus() {
        assertEquals("01", ApprovalStatus.PENDING.getCode());
        assertEquals("待审批", ApprovalStatus.PENDING.getInfo());
        assertEquals("02", ApprovalStatus.APPROVED.getCode());
        assertEquals("已通过", ApprovalStatus.APPROVED.getInfo());
        
        assertEquals(ApprovalStatus.PENDING, ApprovalStatus.getByCode("01"));
        assertEquals(ApprovalStatus.APPROVED, ApprovalStatus.getByCode("02"));
        assertNull(ApprovalStatus.getByCode("99"));
    }
}