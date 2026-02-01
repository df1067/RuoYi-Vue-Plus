package org.dromara.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会议室类型枚举
 *
 * @author Lion Li
 */
@Getter
@AllArgsConstructor
public enum MeetingRoomType {

    LARGE("L", "大型厅"),
    SMALL("S", "小型厅");

    private final String code;
    private final String info;

    public static MeetingRoomType getByCode(String code) {
        for (MeetingRoomType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}