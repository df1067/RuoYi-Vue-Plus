package org.dromara.system.service;

import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.system.domain.bo.VisitorAppointmentBo;
import org.dromara.system.domain.vo.VisitorAppointmentVo;

import java.util.Collection;
import java.util.List;

/**
 * 访客预约Service接口
 *
 * @author System
 */
public interface IVisitorAppointmentService {

    /**
     * 查询访客预约
     */
    VisitorAppointmentVo queryById(Long appointmentId);

    /**
     * 查询访客预约列表
     */
    TableDataInfo<VisitorAppointmentVo> queryPageList(VisitorAppointmentBo bo, PageQuery pageQuery);

    /**
     * 查询访客预约列表
     */
    List<VisitorAppointmentVo> queryList(VisitorAppointmentBo bo);

    /**
     * 新增访客预约
     */
    Boolean insertByBo(VisitorAppointmentBo bo);

    /**
     * 修改访客预约
     */
    Boolean updateByBo(VisitorAppointmentBo bo);

    /**
     * 批量删除访客预约
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 确认预约
     */
    Boolean confirmAppointment(Long appointmentId);

    /**
     * 拒绝预约
     */
    Boolean rejectAppointment(Long appointmentId, String rejectReason);

    /**
     * 取消预约
     */
    Boolean cancelAppointment(Long appointmentId);

    /**
     * 检查预约冲突
     */
    Boolean checkAppointmentConflict(VisitorAppointmentBo bo);
}