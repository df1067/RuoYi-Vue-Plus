package org.dromara.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 预约状态枚举
 *
 * @author Lion Li
 */
@Getter
@AllArgsConstructor
public enum AppointmentStatus {

    DRAFT("01", "草稿"),
    SUBMITTED("02", "已提交"),
    DEPT_APPROVING("03", "部门审核中"),
    ADMIN_APPROVING("04", "行政复核中"),
    APPROVED("05", "已批准"),
    REJECTED("06", "已拒绝"),
    CANCELLED("07", "已取消");

    private final String code;
    private final String info;

    public static AppointmentStatus getByCode(String code) {
        for (AppointmentStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}