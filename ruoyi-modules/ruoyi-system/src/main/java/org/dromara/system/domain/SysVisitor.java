package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 访客登记实体类 sys_visitor
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_visitor")
public class SysVisitor extends TenantEntity {

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
    private String phone;

    /**
     * 访问事由
     */
    private String reason;

    /**
     * 预约部门ID
     */
    private Long deptId;

    /**
     * 预约到访时间
     */
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