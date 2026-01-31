package org.dromara.system.service.impl;

import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.system.domain.VisitorAppointment;
import org.dromara.system.domain.bo.VisitorAppointmentBo;
import org.dromara.system.domain.vo.VisitorAppointmentVo;
import org.dromara.system.mapper.VisitorAppointmentMapper;
import org.dromara.system.service.IVisitorAppointmentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 访客预约登记Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-31
 */
@Service
public class VisitorAppointmentServiceImpl extends ServiceImpl<VisitorAppointmentMapper, VisitorAppointment> implements IVisitorAppointmentService {

    @Override
    public VisitorAppointmentVo selectVisitorAppointmentByAppointmentId(Long appointmentId) {
        return baseMapper.selectVisitorAppointmentList(new VisitorAppointmentBo() {{ setAppointmentId(appointmentId); }}).stream().findFirst().orElse(null);
    }

    @Override
    public List<VisitorAppointmentVo> selectVisitorAppointmentList(VisitorAppointmentBo visitorAppointmentBo) {
        return baseMapper.selectVisitorAppointmentList(visitorAppointmentBo);
    }

    @Override
    public IPage<VisitorAppointmentVo> selectVisitorAppointmentPage(Page<VisitorAppointmentVo> page, VisitorAppointmentBo visitorAppointmentBo) {
        return baseMapper.selectVisitorAppointmentPage(page, visitorAppointmentBo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertVisitorAppointment(VisitorAppointmentBo visitorAppointmentBo) {
        // 检查时间冲突
        if (checkTimeConflict(visitorAppointmentBo)) {
            throw new RuntimeException("该时间段存在预约冲突，请选择其他时间");
        }

        visitorAppointmentBo.setStatus("0"); // 初始状态为待确认
        visitorAppointmentBo.setCreateTime(DateUtils.getNowDate());
        return save(visitorAppointmentBo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVisitorAppointment(VisitorAppointmentBo visitorAppointmentBo) {
        // 检查时间冲突
        if (checkTimeConflict(visitorAppointmentBo)) {
            throw new RuntimeException("该时间段存在预约冲突，请选择其他时间");
        }

        visitorAppointmentBo.setUpdateTime(DateUtils.getNowDate());
        return updateById(visitorAppointmentBo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmVisitorAppointment(Long appointmentId) {
        VisitorAppointment appointment = getById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        if (!"0".equals(appointment.getStatus())) {
            throw new RuntimeException("只能确认待确认的预约");
        }

        appointment.setStatus("1"); // 已确认
        appointment.setUpdateTime(DateUtils.getNowDate());
        return updateById(appointment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectVisitorAppointment(Long appointmentId, String rejectReason) {
        VisitorAppointment appointment = getById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        if (!"0".equals(appointment.getStatus())) {
            throw new RuntimeException("只能拒绝待确认的预约");
        }

        appointment.setStatus("2"); // 已拒绝
        appointment.setRejectReason(rejectReason);
        appointment.setUpdateTime(DateUtils.getNowDate());
        return updateById(appointment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVisitorAppointmentByAppointmentIds(Long[] appointmentIds) {
        for (Long appointmentId : appointmentIds) {
            deleteVisitorAppointmentByAppointmentId(appointmentId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVisitorAppointmentByAppointmentId(Long appointmentId) {
        return removeById(appointmentId);
    }

    @Override
    public boolean checkTimeConflict(VisitorAppointmentBo visitorAppointmentBo) {
        // 检查访客是否存在时间冲突
        int visitorConflict = baseMapper.countConflictByVisitorPhone(
                visitorAppointmentBo.getVisitorPhone(),
                visitorAppointmentBo.getStartTime(),
                visitorAppointmentBo.getEndTime(),
                visitorAppointmentBo.getAppointmentId()
        );
        if (visitorConflict > 0) {
            return true;
        }

        // 检查对接人是否存在时间冲突
        int contactConflict = baseMapper.countConflictByContactUserId(
                visitorAppointmentBo.getContactUserId(),
                visitorAppointmentBo.getStartTime(),
                visitorAppointmentBo.getEndTime(),
                visitorAppointmentBo.getAppointmentId()
        );
        return contactConflict > 0;
    }
}