package org.dromara.system.service;

import org.dromara.system.domain.VisitorAppointment;
import org.dromara.system.domain.bo.VisitorAppointmentBo;
import org.dromara.system.domain.vo.VisitorAppointmentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 访客预约登记Service接口
 * 
 * @author ruoyi
 * @date 2026-01-31
 */
public interface IVisitorAppointmentService extends IService<VisitorAppointment> {

    /**
     * 查询访客预约登记
     * 
     * @param appointmentId 访客预约登记主键
     * @return 访客预约登记
     */
    VisitorAppointmentVo selectVisitorAppointmentByAppointmentId(Long appointmentId);

    /**
     * 查询访客预约登记列表
     * 
     * @param visitorAppointmentBo 访客预约登记业务对象
     * @return 访客预约登记集合
     */
    List<VisitorAppointmentVo> selectVisitorAppointmentList(VisitorAppointmentBo visitorAppointmentBo);

    /**
     * 查询访客预约登记分页列表
     * 
     * @param page 分页对象
     * @param visitorAppointmentBo 访客预约登记业务对象
     * @return 访客预约登记分页集合
     */
    IPage<VisitorAppointmentVo> selectVisitorAppointmentPage(Page<VisitorAppointmentVo> page, VisitorAppointmentBo visitorAppointmentBo);

    /**
     * 新增访客预约登记
     * 
     * @param visitorAppointmentBo 访客预约登记业务对象
     * @return 结果
     */
    boolean insertVisitorAppointment(VisitorAppointmentBo visitorAppointmentBo);

    /**
     * 修改访客预约登记
     * 
     * @param visitorAppointmentBo 访客预约登记业务对象
     * @return 结果
     */
    boolean updateVisitorAppointment(VisitorAppointmentBo visitorAppointmentBo);

    /**
     * 确认访客预约
     * 
     * @param appointmentId 预约ID
     * @return 结果
     */
    boolean confirmVisitorAppointment(Long appointmentId);

    /**
     * 拒绝访客预约
     * 
     * @param appointmentId 预约ID
     * @param rejectReason 拒绝原因
     * @return 结果
     */
    boolean rejectVisitorAppointment(Long appointmentId, String rejectReason);

    /**
     * 批量删除访客预约登记
     * 
     * @param appointmentIds 需要删除的访客预约登记主键集合
     * @return 结果
     */
    boolean deleteVisitorAppointmentByAppointmentIds(Long[] appointmentIds);

    /**
     * 删除访客预约登记信息
     * 
     * @param appointmentId 访客预约登记主键
     * @return 结果
     */
    boolean deleteVisitorAppointmentByAppointmentId(Long appointmentId);

    /**
     * 检查预约时间冲突
     * 
     * @param visitorAppointmentBo 访客预约登记业务对象
     * @return 是否存在冲突
     */
    boolean checkTimeConflict(VisitorAppointmentBo visitorAppointmentBo);
}