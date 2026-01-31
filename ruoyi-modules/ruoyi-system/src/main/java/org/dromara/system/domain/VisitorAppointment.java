package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 访客预约对象 visitor_appointment
 *
 * @author System
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("visitor_appointment")
public class VisitorAppointment extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @TableId(value = "appointment_id")
    private Long appointmentId;

    /**
     * 访客姓名
     */
    private String visitorName;

    /**
     * 访客手机号
     */
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
    private String visitPurpose;

    /**
     * 预约部门ID
     */
    private Long deptId;

    /**
     * 对接人用户ID
     */
    private Long contactUserId;

    /**
     * 预约开始时间
     */
    private Date appointmentStartTime;

    /**
     * 预约结束时间
     */
    private Date appointmentEndTime;

    /**
     * 预约状态（0待确认 1已确认 2已拒绝 3已取消）
     */
    private String status;

    /**
     * 预约人用户ID
     */
    private Long createBy;

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