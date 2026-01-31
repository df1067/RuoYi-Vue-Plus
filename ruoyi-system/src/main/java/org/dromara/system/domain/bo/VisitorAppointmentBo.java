package org.dromara.system.domain.bo;

import org.dromara.system.domain.VisitorAppointment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 访客预约登记业务对象
 * 
 * @author ruoyi
 * @date 2026-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisitorAppointmentBo extends VisitorAppointment {
    private static final long serialVersionUID = 1L;

    /**
     * 开始时间范围（查询用）
     */
    private Date startTimeRangeStart;

    /**
     * 结束时间范围（查询用）
     */
    private Date startTimeRangeEnd;
}