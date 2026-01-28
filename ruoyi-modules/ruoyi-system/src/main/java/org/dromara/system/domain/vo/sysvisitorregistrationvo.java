package org.dromara.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.dromara.system.domain.SysVisitorRegistration;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 访客预约登记视图对象 sys_visitor_registration
 *
 * @author System
 */
@Data
@AutoMapper(target = SysVisitorRegistration.class)
public class SysVisitorRegistrationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 访客ID
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentTime;

    /**
     * 实际到访时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualArrivalTime;

    /**
     * 实际离开时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    private String deptName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}