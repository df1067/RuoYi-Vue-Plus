package org.dromara.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysVisitorRegistration;
import org.dromara.system.domain.bo.SysVisitorRegistrationBo;
import org.dromara.system.domain.vo.SysVisitorRegistrationVo;
import org.dromara.system.mapper.SysVisitorRegistrationMapper;
import org.dromara.system.service.ISysVisitorRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 访客预约登记Service实现类
 *
 * @author System
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysVisitorRegistrationServiceImpl implements ISysVisitorRegistrationService {

    private final SysVisitorRegistrationMapper baseMapper;

    @Override
    public TableDataInfo<SysVisitorRegistrationVo> selectPageVisitorList(SysVisitorRegistrationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysVisitorRegistration> lqw = buildQueryWrapper(bo);
        Page<SysVisitorRegistrationVo> page = baseMapper.selectPageVisitorList(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    @Override
    public List<SysVisitorRegistrationVo> selectVisitorList(SysVisitorRegistrationBo bo) {
        LambdaQueryWrapper<SysVisitorRegistration> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public SysVisitorRegistrationVo selectVisitorById(Long visitorId) {
        return baseMapper.selectVoById(visitorId);
    }

    @Override
    @Transactional
    public int insertVisitor(SysVisitorRegistrationBo bo) {
        SysVisitorRegistration visitor = BeanUtil.toBean(bo, SysVisitorRegistration.class);
        visitor.setStatus("0"); // 默认状态为预约中
        return baseMapper.insert(visitor);
    }

    @Override
    @Transactional
    public int updateVisitor(SysVisitorRegistrationBo bo) {
        SysVisitorRegistration visitor = BeanUtil.toBean(bo, SysVisitorRegistration.class);
        return baseMapper.updateById(visitor);
    }

    @Override
    @Transactional
    public int deleteVisitorByIds(Long[] visitorIds) {
        return baseMapper.deleteBatchIds(List.of(visitorIds));
    }

    @Override
    @Transactional
    public int checkIn(Long visitorId) {
        SysVisitorRegistration visitor = baseMapper.selectById(visitorId);
        if (visitor == null) {
            throw new ServiceException("访客记录不存在");
        }
        if (!"0".equals(visitor.getStatus())) {
            throw new ServiceException("当前状态不允许签到");
        }
        visitor.setStatus("1"); // 已签到
        visitor.setActualArrivalTime(new Date());
        return baseMapper.updateById(visitor);
    }

    @Override
    @Transactional
    public int checkOut(Long visitorId) {
        SysVisitorRegistration visitor = baseMapper.selectById(visitorId);
        if (visitor == null) {
            throw new ServiceException("访客记录不存在");
        }
        if (!"1".equals(visitor.getStatus())) {
            throw new ServiceException("当前状态不允许签离");
        }
        visitor.setStatus("2"); // 已签离
        visitor.setActualDepartureTime(new Date());
        return baseMapper.updateById(visitor);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<SysVisitorRegistration> buildQueryWrapper(SysVisitorRegistrationBo bo) {
        LambdaQueryWrapper<SysVisitorRegistration> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(bo.getVisitorName()), SysVisitorRegistration::getVisitorName, bo.getVisitorName());
        lqw.eq(StringUtils.isNotBlank(bo.getPhoneNumber()), SysVisitorRegistration::getPhoneNumber, bo.getPhoneNumber());
        lqw.like(StringUtils.isNotBlank(bo.getVisitPurpose()), SysVisitorRegistration::getVisitPurpose, bo.getVisitPurpose());
        lqw.eq(bo.getDeptId() != null, SysVisitorRegistration::getDeptId, bo.getDeptId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysVisitorRegistration::getStatus, bo.getStatus());
        
        // 预约时间范围查询
        if (bo.getParams() != null && bo.getParams().get("beginTime") != null) {
            lqw.ge(SysVisitorRegistration::getAppointmentTime, bo.getParams().get("beginTime"));
        }
        if (bo.getParams() != null && bo.getParams().get("endTime") != null) {
            lqw.le(SysVisitorRegistration::getAppointmentTime, bo.getParams().get("endTime"));
        }
        
        return lqw;
    }
}