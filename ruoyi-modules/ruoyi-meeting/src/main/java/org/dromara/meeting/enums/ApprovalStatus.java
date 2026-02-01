package org.dromara.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批状态枚举
 *
 * @author Lion Li
 */
@Getter
@AllArgsConstructor
public enum ApprovalStatus {

    PENDING("01", "待审批"),
    APPROVED("02", "已通过"),
    REJECTED("03", "已拒绝");

    private final String code;
    private final String info;

    public static ApprovalStatus getByCode(String code) {
        for (ApprovalStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}