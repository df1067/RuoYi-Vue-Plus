package org.dromara.meetingroom.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.meetingroom.domain.MeetingRoom;

/**
 * 多功能厅业务对象 meeting_room
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MeetingRoom.class, reverseConvertGenerate = false)
public class MeetingRoomBo extends BaseEntity {

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 会议室名称
     */
    @NotBlank(message = "会议室名称不能为空")
    private String roomName;

    /**
     * 会议室类型（0小型 1大型）
     */
    @NotBlank(message = "会议室类型不能为空")
    private String roomType;

    /**
     * 容纳人数
     */
    @NotNull(message = "容纳人数不能为空")
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
     * 备注
     */
    private String remark;

    public MeetingRoomBo(Long roomId) {
        this.roomId = roomId;
    }
}