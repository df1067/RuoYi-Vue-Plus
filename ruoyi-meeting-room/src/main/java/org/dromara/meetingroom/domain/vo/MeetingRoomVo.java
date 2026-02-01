package org.dromara.meetingroom.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelIgnoreUnannotated;
import org.dromara.common.excel.annotation.ExcelProperty;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.common.excel.format.ExcelDictFormat;
import org.dromara.meetingroom.domain.MeetingRoom;

import java.io.Serial;
import java.io.Serializable;

/**
 * 多功能厅视图对象 meeting_room
 *
 * @author Lion Li
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MeetingRoom.class)
public class MeetingRoomVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会议室ID
     */
    @ExcelProperty(value = "会议室ID")
    private Long roomId;

    /**
     * 会议室名称
     */
    @ExcelProperty(value = "会议室名称")
    private String roomName;

    /**
     * 会议室类型（0小型 1大型）
     */
    @ExcelProperty(value = "会议室类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=小型,1=大型")
    private String roomType;

    /**
     * 容纳人数
     */
    @ExcelProperty(value = "容纳人数")
    private Integer capacity;

    /**
     * 会议室位置
     */
    @ExcelProperty(value = "会议室位置")
    private String location;

    /**
     * 配备设备
     */
    @ExcelProperty(value = "配备设备")
    private String equipment;

    /**
     * 会议室状态（0可用 1停用）
     */
    @ExcelProperty(value = "会议室状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=可用,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}