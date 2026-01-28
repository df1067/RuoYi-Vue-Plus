package org.dromara.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysDept;
import org.dromara.system.domain.SysVisitor;
import org.dromara.system.domain.bo.SysVisitorBo;
import org.dromara.system.domain.vo.SysVisitorVo;
import org.dromara.system.mapper.SysDeptMapper;
import org.dromara.system.mapper.SysVisitorMapper;
import org.dromara.system.service.ISysVisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 访客登记Service业务层处理
 *
 * @author Lion Li
 */
@Service
public class SysVisitorServiceImpl extends ServiceImpl<SysVisitorMapper, SysVisitor> implements ISysVisitorService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public TableDataInfo<SysVisitorVo> selectPageVisitorList(SysVisitorBo visitorBo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysVisitor> lqw = buildQueryWrapper(visitorBo);
        IPage<SysVisitorVo> page = baseMapper.selectVisitorPage(pageQuery.build(), lqw);
        // 填充部门名称
        fillDeptNames(page.getRecords());
        return TableDataInfo.build(page);
    }

    @Override
    public List<SysVisitorVo> selectVisitorList(SysVisitorBo visitorBo) {
        LambdaQueryWrapper<SysVisitor> lqw = buildQueryWrapper(visitorBo);
        List<SysVisitorVo> list = baseMapper.selectVoList(lqw);
        // 填充部门名称
        fillDeptNames(list);
        return list;
    }

    private LambdaQueryWrapper<SysVisitor> buildQueryWrapper(SysVisitorBo visitorBo) {
        LambdaQueryWrapper<SysVisitor> lqw = new LambdaQueryWrapper<>();
        lqw.like(visitorBo.getVisitorName() != null, SysVisitor::getVisitorName, visitorBo.getVisitorName());
        lqw.eq(visitorBo.getDeptId() != null, SysVisitor::getDeptId, visitorBo.getDeptId());
        lqw.eq(visitorBo.getStatus() != null, SysVisitor::getStatus, visitorBo.getStatus());
        lqw.between(visitorBo.getVisitTime() != null, SysVisitor::getVisitTime, visitorBo.getVisitTime(), new Date());
        lqw.orderByDesc(SysVisitor::getCreateTime);
        return lqw;
    }

    private void fillDeptNames(List<SysVisitorVo> visitorList) {
        if (visitorList == null || visitorList.isEmpty()) {
            return;
        }
        List<Long> deptIds = visitorList.stream()
                .map(SysVisitorVo::getDeptId)
                .distinct()
                .collect(Collectors.toList());
        List<SysDept> deptList = sysDeptMapper.selectBatchIds(deptIds);
        Map<Long, String> deptNameMap = deptList.stream()
                .collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));
        visitorList.forEach(visitor -> visitor.setDeptName(deptNameMap.get(visitor.getDeptId())));
    }

    @Override
    public SysVisitorVo selectVisitorById(Long visitorId) {
        SysVisitorVo visitorVo = baseMapper.selectVoById(visitorId);
        if (visitorVo != null) {
            SysDept dept = sysDeptMapper.selectById(visitorVo.getDeptId());
            if (dept != null) {
                visitorVo.setDeptName(dept.getDeptName());
            }
        }
        return visitorVo;
    }

    @Override
    public int insertVisitor(SysVisitorBo visitorBo) {
        SysVisitor visitor = BeanUtil.toBean(visitorBo, SysVisitor.class);
        visitor.setStatus("0"); // 初始状态为预约中
        return baseMapper.insert(visitor);
    }

    @Override
    public int updateVisitor(SysVisitorBo visitorBo) {
        SysVisitor visitor = BeanUtil.toBean(visitorBo, SysVisitor.class);
        return baseMapper.updateById(visitor);
    }

    @Override
    public int signIn(Long visitorId) {
        SysVisitor visitor = baseMapper.selectById(visitorId);
        if (visitor == null) {
            throw new ServiceException("访客不存在");
        }
        if ("1".equals(visitor.getStatus()) || "2".equals(visitor.getStatus())) {
            throw new ServiceException("访客已签到或已离开");
        }
        visitor.setStatus("1");
        visitor.setActualVisitTime(new Date());
        return baseMapper.updateById(visitor);
    }

    @Override
    public int signOut(Long visitorId) {
        SysVisitor visitor = baseMapper.selectById(visitorId);
        if (visitor == null) {
            throw new ServiceException("访客不存在");
        }
        if (!"1".equals(visitor.getStatus())) {
            throw new ServiceException("访客未签到");
        }
        visitor.setStatus("2");
        visitor.setActualLeaveTime(new Date());
        return baseMapper.updateById(visitor);
    }

    @Override
    public int deleteVisitorByIds(Long[] visitorIds) {
        return baseMapper.deleteBatchIds(List.of(visitorIds));
    }

    @Override
    public int deleteVisitorById(Long visitorId) {
        return baseMapper.deleteById(visitorId);
    }
}