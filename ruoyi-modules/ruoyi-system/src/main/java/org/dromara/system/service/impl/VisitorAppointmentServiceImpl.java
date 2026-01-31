package org.dromara.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.VisitorAppointment;
import org.dromara.system.domain.bo.VisitorAppointmentBo;
import org.dromara.system.domain.vo.VisitorAppointmentVo;
import org.dromara.system.mapper.VisitorAppointmentMapper;
import org.dromara.system.service.IVisitorAppointmentService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 访客预约Service业务层处理
 *
 * @author System
 */
@RequiredArgsConstructor
@Service
public class VisitorAppointmentServiceImpl implements IVisitorAppointmentService {

    private final VisitorAppointmentMapper baseMapper;

    /**
     * 查询访客预约
     */
    @Override
    public VisitorAppointmentVo queryById(Long appointmentId) {
        return baseMapper.selectVoById(appointmentId);
    }

    /**
     * 查询访客预约列表
     */
    @Override
    public TableDataInfo<VisitorAppointmentVo> queryPageList(VisitorAppointmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<VisitorAppointment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoPage(pageQuery.build(), lqw, VisitorAppointmentVo.class);
    }

    /**
     * 查询访客预约列表
     */
    @Override
    public List<VisitorAppointmentVo> queryList(VisitorAppointmentBo bo) {
        LambdaQueryWrapper<VisitorAppointment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw, VisitorAppointmentVo.class);
    }

    private LambdaQueryWrapper<VisitorAppointment> buildQueryWrapper(VisitorAppointmentBo bo) {
        LambdaQueryWrapper<VisitorAppointment> lqw = Wrappers.lambdaQuery();
        lqw.like(bo.getVisitorName() != null, VisitorAppointment::getVisitorName, bo.getVisitorName());
        lqw.eq(bo.getVisitorPhone() != null, VisitorAppointment::getVisitorPhone, bo.getVisitorPhone());
        lqw.eq(bo.getDeptId() != null, VisitorAppointment::getDeptId, bo.getDeptId());
        lqw.eq(bo.getContactUserId() != null, VisitorAppointment::getContactUserId, bo.getContactUserId());
        lqw.eq(bo.getStatus() != null, VisitorAppointment::getStatus, bo.getStatus());
        lqw.between(bo.getAppointmentStartTime() != null && bo.getAppointmentEndTime() != null,
                VisitorAppointment::getAppointmentStartTime, bo.getAppointmentStartTime(), bo.getAppointmentEndTime());
        lqw.orderByDesc(VisitorAppointment::getCreateTime);
        return lqw;
    }

    /**
     * 新增访客预约
     */
    @Override
    public Boolean insertByBo(VisitorAppointmentBo bo) {
        // 检查预约冲突
        if (checkAppointmentConflict(bo)) {
            throw new ServiceException("预约时间冲突，该访客在同一时间段内已有其他预约");
        }

        VisitorAppointment add = BeanUtil.toBean(bo, VisitorAppointment.class);
        add.setStatus("0"); // 默认待确认状态
        add.setCreateBy(LoginHelper.getUserId());
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setAppointmentId(add.getAppointmentId());
        }
        return flag;
    }

    /**
     * 修改访客预约
     */
    @Override
    public Boolean updateByBo(VisitorAppointmentBo bo) {
        // 检查预约冲突
        if (checkAppointmentConflict(bo)) {
            throw new ServiceException("预约时间冲突，该访客在同一时间段内已有其他预约");
        }

        VisitorAppointment update = BeanUtil.toBean(bo, VisitorAppointment.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(VisitorAppointment entity) {
        // 校验预约时间是否合理
        if (entity.getAppointmentStartTime() != null && entity.getAppointmentEndTime() != null) {
            if (entity.getAppointmentStartTime().after(entity.getAppointmentEndTime())) {
                throw new ServiceException("预约开始时间不能晚于结束时间");
            }
            // 预约时间不能早于当前时间
            if (entity.getAppointmentStartTime().before(new Date())) {
                throw new ServiceException("预约开始时间不能早于当前时间");
            }
        }
    }

    /**
     * 批量删除访客预约
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验，判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 确认预约
     */
    @Override
    public Boolean confirmAppointment(Long appointmentId) {
        VisitorAppointment appointment = baseMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new ServiceException("预约记录不存在");
        }
        if (!"0".equals(appointment.getStatus())) {
            throw new ServiceException("只能确认待确认的预约");
        }

        appointment.setStatus("1");
        appointment.setConfirmBy(LoginHelper.getUserId());
        appointment.setConfirmTime(new Date());
        return baseMapper.updateById(appointment) > 0;
    }

    /**
     * 拒绝预约
     */
    @Override
    public Boolean rejectAppointment(Long appointmentId, String rejectReason) {
        VisitorAppointment appointment = baseMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new ServiceException("预约记录不存在");
        }
        if (!"0".equals(appointment.getStatus())) {
            throw new ServiceException("只能拒绝待确认的预约");
        }

        appointment.setStatus("2");
        appointment.setRejectReason(rejectReason);
        appointment.setConfirmBy(LoginHelper.getUserId());
        appointment.setConfirmTime(new Date());
        return baseMapper.updateById(appointment) > 0;
    }

    /**
     * 取消预约
     */
    @Override
    public Boolean cancelAppointment(Long appointmentId) {
        VisitorAppointment appointment = baseMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new ServiceException("预约记录不存在");
        }
        if (!"0".equals(appointment.getStatus()) && !"1".equals(appointment.getStatus())) {
            throw new ServiceException("只能取消待确认或已确认的预约");
        }

        appointment.setStatus("3");
        return baseMapper.updateById(appointment) > 0;
    }

    /**
     * 检查预约冲突
     */
    @Override
    public Boolean checkAppointmentConflict(VisitorAppointmentBo bo) {
        LambdaQueryWrapper<VisitorAppointment> lqw = Wrappers.lambdaQuery();
        lqw.eq(VisitorAppointment::getVisitorPhone, bo.getVisitorPhone());
        lqw.ne(bo.getAppointmentId() != null, VisitorAppointment::getAppointmentId, bo.getAppointmentId());
        lqw.in(VisitorAppointment::getStatus, "0", "1"); // 只检查待确认和已确认的预约
        
        // 时间重叠检查：新预约的开始时间 < 现有预约的结束时间 且 新预约的结束时间 > 现有预约的开始时间
        lqw.lt(VisitorAppointment::getAppointmentStartTime, bo.getAppointmentEndTime());
        lqw.gt(VisitorAppointment::getAppointmentEndTime, bo.getAppointmentStartTime());
        
        return baseMapper.selectCount(lqw) > 0;
    }
}