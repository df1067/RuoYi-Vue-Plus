package org.dromara.meetingroom.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 多功能厅表 meeting_room
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("meeting_room")
public class MeetingRoom extends TenantEntity {

    /**
     * 会议室ID
     */
    @TableId(value = "room_id")
    private Long roomId;

    /**
     * 会议室名称
     */
    private String roomName;

    /**
     * 会议室类型（0小型 1大型）
     */
    private String roomType;

    /**
     * 容纳人数
     */
    private Integer capacity;

    /**
     * 会议室位置
     */
    private String location;

    /**
     * 配备设备
     */
    private String equipment;

    /**
     * 会议室状态（0可用 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    public MeetingRoom(Long roomId) {
        this.roomId = roomId;
    }
}