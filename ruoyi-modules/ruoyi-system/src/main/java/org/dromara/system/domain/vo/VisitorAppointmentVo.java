package org.dromara.system.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 访客预约视图对象 visitor_appointment
 *
 * @author System
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisitorAppointmentVo extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
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
     * 部门名称
     */
    private String deptName;

    /**
     * 对接人用户ID
     */
    private Long contactUserId;

    /**
     * 对接人姓名
     */
    private String contactUserName;

    /**
     * 对接人昵称
     */
    private String contactNickName;

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
     * 预约状态名称
     */
    private String statusName;

    /**
     * 预约人用户ID
     */
    private Long createBy;

    /**
     * 预约人姓名
     */
    private String createByName;

    /**
     * 预约人昵称
     */
    private String createByNickName;

    /**
     * 确认人用户ID
     */
    private Long confirmBy;

    /**
     * 确认人姓名
     */
    private String confirmByName;

    /**
     * 确认人昵称
     */
    private String confirmByNickName;

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