package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

/**
 * 访客预约登记实体类
 * 
 * @author ruoyi
 * @date 2026-01-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_visitor_appointment")
public class VisitorAppointment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @TableId(value = "appointment_id", type = IdType.AUTO)
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
     * 访问事由
     */
    private String visitReason;

    /**
     * 预约部门ID
     */
    private Long deptId;

    /**
     * 预约部门名称
     */
    private String deptName;

    /**
     * 对接人ID
     */
    private Long contactUserId;

    /**
     * 对接人姓名
     */
    private String contactUserName;

    /**
     * 预约开始时间
     */
    private Date startTime;

    /**
     * 预约结束时间
     */
    private Date endTime;

    /**
     * 预约状态（0：待确认，1：已确认，2：已拒绝，3：已完成，4：已取消）
     */
    private String status;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}