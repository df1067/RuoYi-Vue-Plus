package org.dromara.system.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 访客登记业务对象 sys_visitor
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysVisitorBo {

    private static final long serialVersionUID = 1L;

    /**
     * 访客ID
     */
    private Long visitorId;

    /**
     * 访客姓名
     */
    @NotBlank(message = "访客姓名不能为空")
    private String visitorName;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    /**
     * 访问事由
     */
    @NotBlank(message = "访问事由不能为空")
    private String reason;

    /**
     * 预约部门ID
     */
    @NotNull(message = "预约部门不能为空")
    private Long deptId;

    /**
     * 预约到访时间
     */
    @NotNull(message = "预约到访时间不能为空")
    private Date visitTime;

    /**
     * 实际到访时间
     */
    private Date actualVisitTime;

    /**
     * 实际离开时间
     */
    private Date actualLeaveTime;

    /**
     * 状态（0：预约中 1：已到访 2：已离开）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}