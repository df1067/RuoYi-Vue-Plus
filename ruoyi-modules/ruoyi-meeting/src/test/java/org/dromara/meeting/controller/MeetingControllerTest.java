package org.dromara.meeting.controller;

import org.dromara.meeting.domain.bo.MeetingAppointmentBo;
import org.dromara.meeting.domain.bo.MeetingRoomBo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 控制器测试类
 *
 * @author Lion Li
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMeetingRoomCRUD() throws Exception {
        // 创建会议室
        MeetingRoomBo roomBo = new MeetingRoomBo();
        roomBo.setRoomName("测试会议室");
        roomBo.setRoomType("L");
        roomBo.setCapacity(50);
        roomBo.setLocation("测试位置");
        roomBo.setStatus("0");

        // 注意：这里需要实际的权限认证支持
        // 在真实环境中，需要先登录获取token
        
        mockMvc.perform(post("/meeting/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""" 
                    {
                        "roomName": "测试会议室",
                        "roomType": "L",
                        "capacity": 50,
                        "location": "测试位置",
                        "status": "0"
                    }
                """))
                .andExpect(status().isOk());
    }

    @Test
    public void testAppointmentCRUD() throws Exception {
        // 创建预约
        MeetingAppointmentBo appointmentBo = new MeetingAppointmentBo();
        appointmentBo.setRoomId(1L);
        appointmentBo.setMeetingTitle("测试会议");
        appointmentBo.setMeetingDesc("测试会议描述");
        appointmentBo.setStartTime(new Date(System.currentTimeMillis() + 86400000)); // 明天
        appointmentBo.setEndTime(new Date(System.currentTimeMillis() + 90000000)); // 明天+1小时
        appointmentBo.setAttendees(10);

        // 注意：这里需要实际的权限认证支持
        // 在真实环境中，需要先登录获取token
        
        mockMvc.perform(post("/meeting/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "roomId": 1,
                        "meetingTitle": "测试会议",
                        "meetingDesc": "测试会议描述",
                        "startTime": "2024-12-31T09:00:00",
                        "endTime": "2024-12-31T10:00:00",
                        "attendees": 10
                    }
                """))
                .andExpect(status().isOk());
    }
}