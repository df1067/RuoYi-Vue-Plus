package org.dromara.system.domain.vo;

import org.dromara.system.domain.VisitorAppointment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 访客预约登记视图对象
 * 
 * @author ruoyi
 * @date 2026-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisitorAppointmentVo extends VisitorAppointment {
    private static final long serialVersionUID = 1L;
}