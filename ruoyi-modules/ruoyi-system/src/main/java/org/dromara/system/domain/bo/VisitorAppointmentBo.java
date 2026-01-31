package org.dromara.system.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.tenant.core.TenantEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * 访客预约业务对象 visitor_appointment
 *
 * @author System
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisitorAppointmentBo extends TenantEntity {

    /**
     * 预约ID
     */
    @NotNull(message = "预约ID不能为空", groups = { EditGroup.class })
    private Long appointmentId;

    /**
     * 访客姓名
     */
    @NotBlank(message = "访客姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String visitorName;

    /**
     * 访客手机号
     */
    @NotBlank(message = "访客手机号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String visitorPhone;

    /**
     * 访客身份证号
     */
    private String visitorIdCard;

    /**
     * 访客单位/公司
     */
    private String visitorCompany;

    /**
     * 访问事由
     */
    @NotBlank(message = "访问事由不能为空", groups = { AddGroup.class, EditGroup.class })
    private String visitPurpose;

    /**
     * 预约部门ID
     */
    @NotNull(message = "预约部门不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deptId;

    /**
     * 对接人用户ID
     */
    @NotNull(message = "对接人不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long contactUserId;

    /**
     * 预约开始时间
     */
    @NotNull(message = "预约开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date appointmentStartTime;

    /**
     * 预约结束时间
     */
    @NotNull(message = "预约结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date appointmentEndTime;

    /**
     * 预约状态（0待确认 1已确认 2已拒绝 3已取消）
     */
    private String status;

    /**
     * 确认人用户ID
     */
    private Long confirmBy;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    /**
     * 备注
     */
    private String remark;
}