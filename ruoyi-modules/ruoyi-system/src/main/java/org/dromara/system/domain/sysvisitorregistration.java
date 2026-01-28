package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.Date;

/**
 * 访客预约登记对象 sys_visitor_registration
 *
 * @author System
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_visitor_registration")
public class SysVisitorRegistration extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 访客ID
     */
    @TableId(value = "visitor_id")
    private Long visitorId;

    /**
     * 访客姓名
     */
    private String visitorName;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 访问事由
     */
    private String visitPurpose;

    /**
     * 预约访问部门ID
     */
    private Long deptId;

    /**
     * 预约到访时间
     */
    private Date appointmentTime;

    /**
     * 实际到访时间
     */
    private Date actualArrivalTime;

    /**
     * 实际离开时间
     */
    private Date actualDepartureTime;

    /**
     * 状态（0预约中 1已签到 2已签离 3已取消）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;
}