package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.SysVisitorRegistration;

import java.util.Date;

/**
 * 访客预约登记业务对象 sys_visitor_registration
 *
 * @author System
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysVisitorRegistration.class, reverseConvertGenerate = false)
public class SysVisitorRegistrationBo extends BaseEntity {

    /**
     * 访客ID
     */
    private Long visitorId;

    /**
     * 访客姓名
     */
    @NotBlank(message = "访客姓名不能为空")
    @Size(min = 0, max = 50, message = "访客姓名长度不能超过{max}个字符")
    private String visitorName;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Size(min = 0, max = 20, message = "联系电话长度不能超过{max}个字符")
    private String phoneNumber;

    /**
     * 访问事由
     */
    @NotBlank(message = "访问事由不能为空")
    @Size(min = 0, max = 200, message = "访问事由长度不能超过{max}个字符")
    private String visitPurpose;

    /**
     * 预约访问部门ID
     */
    @NotNull(message = "预约访问部门不能为空")
    private Long deptId;

    /**
     * 预约到访时间
     */
    @NotNull(message = "预约到访时间不能为空")
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
    @Size(min = 0, max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;
}