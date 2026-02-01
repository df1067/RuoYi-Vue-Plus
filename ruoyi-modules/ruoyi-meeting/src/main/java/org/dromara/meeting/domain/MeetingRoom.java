package org.dromara.meeting.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

/**
 * 会议室表 meeting_room
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("meeting_room")
public class MeetingRoom extends TenantEntity {

    private static final long serialVersionUID = 1L;

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
     * 会议室类型（L大型 S小型）
     */
    private String roomType;

    /**
     * 容纳人数
     */
    private Integer capacity;

    /**
     * 位置描述
     */
    private String location;

    /**
     * 设备配置
     */
    private String equipment;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;
}