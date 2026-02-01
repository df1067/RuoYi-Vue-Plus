package org.dromara.meeting.domain.bo;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.meeting.domain.MeetingRoom;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 会议室业务对象 meeting_room
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingRoomBo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 会议室ID
     */
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
}