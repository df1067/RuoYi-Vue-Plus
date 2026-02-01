package org.dromara.meeting.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 会议室视图对象 meeting_room
 *
 * @author Lion Li
 */
@Data
public class MeetingRoomVo {

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

    /**
     * 创建时间
     */
    private Date createTime;
}