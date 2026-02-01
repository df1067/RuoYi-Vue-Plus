package org.dromara.meeting.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.meeting.domain.MeetingAppointment;
import org.dromara.meeting.domain.MeetingRoom;
import org.dromara.meeting.domain.bo.MeetingAppointmentBo;
import org.dromara.meeting.domain.vo.MeetingAppointmentVo;
import org.dromara.meeting.domain.vo.MeetingStatisticsVo;
import org.dromara.meeting.enums.AppointmentStatus;
import org.dromara.meeting.enums.MeetingRoomType;
import org.dromara.meeting.mapper.MeetingAppointmentMapper;
import org.dromara.meeting.mapper.MeetingRoomMapper;
import org.dromara.meeting.service.IConflictCheckService;
import org.dromara.meeting.service.IMeetingAppointmentService;
import org.dromara.meeting.service.IPermissionCheckService;
import org.dromara.system.domain.SysUser;
import org.dromara.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预约记录Service业务层处理
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class MeetingAppointmentServiceImpl implements IMeetingAppointmentService {

    private final MeetingAppointmentMapper baseMapper;
    private final MeetingRoomMapper roomMapper;
    private final ISysUserService userService;
    private final IPermissionCheckService permissionCheckService;
    private final IConflictCheckService conflictCheckService;

    /**
     * 生成预约单号
     */
    private String generateAppointmentNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        return "YY" + dateStr + timeStr + (int)(Math.random() * 1000);
    }

    /**
     * 填充用户信息
     */
    private void fillUserInfo(MeetingAppointment appointment) {
        Long userId = LoginHelper.getUserId();
        SysUser user = userService.selectUserById(userId);
        
        appointment.setUserId(userId);
        appointment.setUserName(user.getNickName());
        appointment.setDeptId(user.getDeptId());
        appointment.setDeptName(user.getDept() != null ? user.getDept().getDeptName() : "");
    }

    /**
     * 检查预约权限
     */
    private void checkAppointmentPermission(MeetingAppointment appointment) {
        MeetingRoom room = roomMapper.selectById(appointment.getRoomId());
        if (room == null) {
            throw new ServiceException("会议室不存在");
        }
        
        // 大型厅权限检查
        if (MeetingRoomType.LARGE.getCode().equals(room.getRoomType())) {
            if (!permissionCheckService.hasLargeRoomPermission(appointment.getUserId())) {
                throw new ServiceException("只有部门总监及以上级别人员才能预约大型厅");
            }
        }
        
        // 检查待审批预约数量限制
        if (!permissionCheckService.checkMaxPendingAppointments(appointment.getUserId())) {
            throw new ServiceException("您当前已有2个待审批的预约，无法提交新的预约申请");
        }
        
        // 检查时间冲突
        if (!conflictCheckService.checkTimeConflict(appointment.getRoomId(), 
                appointment.getStartTime(), appointment.getEndTime(), null)) {
            throw new ServiceException("该时间段已被其他会议占用");
        }
    }

    @Override
    public MeetingAppointmentVo queryById(Long appointmentId) {
        MeetingAppointment appointment = baseMapper.selectById(appointmentId);
        if (appointment == null) {
            return null;
        }
        
        MeetingAppointmentVo vo = new MeetingAppointmentVo();
        org.springframework.beans.BeanUtils.copyProperties(appointment, vo);
        
        // 填充会议室信息
        MeetingRoom room = roomMapper.selectById(appointment.getRoomId());
        if (room != null) {
            vo.setRoomName(room.getRoomName());
            vo.setRoomType(room.getRoomType());
        }
        
        // 填充状态名称
        AppointmentStatus status = AppointmentStatus.getByCode(appointment.getStatus());
        if (status != null) {
            vo.setStatusName(status.getInfo());
        }
        
        // 检查是否可以取消
        vo.setCanCancel(conflictCheckService.checkLargeRoomCancelRestriction(appointmentId));
        
        return vo;
    }

    @Override
    public TableDataInfo<MeetingAppointmentVo> queryPageList(MeetingAppointmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MeetingAppointment> lqw = buildQueryWrapper(bo);
        TableDataInfo<MeetingAppointment> appointmentTableDataInfo = baseMapper.selectPage(pageQuery.build(), lqw);
        
        List<MeetingAppointmentVo> voList = appointmentTableDataInfo.getRows().stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
        
        TableDataInfo<MeetingAppointmentVo> result = new TableDataInfo<>();
        result.setRows(voList);
        result.setTotal(appointmentTableDataInfo.getTotal());
        return result;
    }

    @Override
    public List<MeetingAppointmentVo> queryList(MeetingAppointmentBo bo) {
        LambdaQueryWrapper<MeetingAppointment> lqw = buildQueryWrapper(bo);
        List<MeetingAppointment> appointmentList = baseMapper.selectList(lqw);
        
        return appointmentList.stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    private LambdaQueryWrapper<MeetingAppointment> buildQueryWrapper(MeetingAppointmentBo bo) {
        LambdaQueryWrapper<MeetingAppointment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(bo.getRoomId() != null, MeetingAppointment::getRoomId, bo.getRoomId());
        lqw.eq(bo.getUserId() != null, MeetingAppointment::getUserId, bo.getUserId());
        lqw.eq(bo.getStatus() != null, MeetingAppointment::getStatus, bo.getStatus());
        lqw.eq(bo.getDeptId() != null, MeetingAppointment::getDeptId, bo.getDeptId());
        
        if (bo.getStartTime() != null) {
            lqw.ge(MeetingAppointment::getStartTime, bo.getStartTime());
        }
        if (bo.getEndTime() != null) {
            lqw.le(MeetingAppointment::getEndTime, bo.getEndTime());
        }
        
        lqw.eq(MeetingAppointment::getDelFlag, "0");
        lqw.orderByDesc(MeetingAppointment::getCreateTime);
        
        return lqw;
    }

    private MeetingAppointmentVo convertToVo(MeetingAppointment appointment) {
        MeetingAppointmentVo vo = new MeetingAppointmentVo();
        org.springframework.beans.BeanUtils.copyProperties(appointment, vo);
        
        // 填充会议室信息
        MeetingRoom room = roomMapper.selectById(appointment.getRoomId());
        if (room != null) {
            vo.setRoomName(room.getRoomName());
            vo.setRoomType(room.getRoomType());
        }
        
        // 填充状态名称
        AppointmentStatus status = AppointmentStatus.getByCode(appointment.getStatus());
        if (status != null) {
            vo.setStatusName(status.getInfo());
        }
        
        // 检查是否可以取消
        vo.setCanCancel(conflictCheckService.checkLargeRoomCancelRestriction(appointment.getAppointmentId()));
        
        return vo;
    }

    @Override
    @Transactional
    public Boolean insertByBo(MeetingAppointmentBo bo) {
        MeetingAppointment appointment = new MeetingAppointment();
        org.springframework.beans.BeanUtils.copyProperties(bo, appointment);
        
        // 填充用户信息
        fillUserInfo(appointment);
        
        // 生成预约单号
        appointment.setAppointmentNo(generateAppointmentNo());
        
        // 设置默认状态为草稿
        appointment.setStatus(AppointmentStatus.DRAFT.getCode());
        
        // 检查预约权限
        checkAppointmentPermission(appointment);
        
        return baseMapper.insert(appointment) > 0;
    }

    @Override
    @Transactional
    public Boolean updateByBo(MeetingAppointmentBo bo) {
        MeetingAppointment appointment = baseMapper.selectById(bo.getAppointmentId());
        if (appointment == null) {
            throw new ServiceException("预约记录不存在");
        }
        
        // 只有草稿状态才能修改
        if (!AppointmentStatus.DRAFT.getCode().equals(appointment.getStatus())) {
            throw new ServiceException("只有草稿状态的预约才能修改");
        }
        
        org.springframework.beans.BeanUtils.copyProperties(bo, appointment, "appointmentId", "appointmentNo", "userId", 
                "userName", "deptId", "deptName", "status", "createTime", "createBy");
        
        // 重新检查预约权限（时间冲突等）
        checkAppointmentPermission(appointment);
        
        return baseMapper.updateById(appointment) > 0;
    }

    @Override
    @Transactional
    public Boolean submitAppointment(Long appointmentId) {
        MeetingAppointment appointment = baseMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new ServiceException("预约记录不存在");
        }
        
        // 只有草稿状态才能提交
        if (!AppointmentStatus.DRAFT.getCode().equals(appointment.getStatus())) {
            throw new ServiceException("只有草稿状态的预约才能提交");
        }
        
        // 检查用户权限
        Long userId = LoginHelper.getUserId();
        if (!userId.equals(appointment.getUserId())) {
            throw new ServiceException("只能提交自己的预约");
        }
        
        // 重新检查预约权限
        checkAppointmentPermission(appointment);
        
        // 更新状态为已提交
        appointment.setStatus(AppointmentStatus.SUBMITTED.getCode());
        appointment.setSubmitTime(new Date());
        
        return baseMapper.updateById(appointment) > 0;
    }

    @Override
    @Transactional
    public Boolean cancelAppointment(Long appointmentId, String cancelReason) {
        MeetingAppointment appointment = baseMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new ServiceException("预约记录不存在");
        }
        
        // 检查用户权限
        Long userId = LoginHelper.getUserId();
        if (!userId.equals(appointment.getUserId()) && !LoginHelper.isSuperAdmin()) {
            throw new ServiceException("只能取消自己的预约");
        }
        
        // 检查状态是否可以取消
        List<String> canCancelStatuses = Arrays.asList(
            AppointmentStatus.SUBMITTED.getCode(),
            AppointmentStatus.DEPT_APPROVING.getCode(),
            AppointmentStatus.ADMIN_APPROVING.getCode(),
            AppointmentStatus.APPROVED.getCode()
        );
        
        if (!canCancelStatuses.contains(appointment.getStatus())) {
            throw new ServiceException("当前状态的预约不能取消");
        }
        
        // 检查大型厅24小时内取消限制
        if (!conflictCheckService.checkLargeRoomCancelRestriction(appointmentId)) {
            throw new ServiceException("大型厅在会议开始前24小时内不可取消");
        }
        
        // 更新状态为已取消
        appointment.setStatus(AppointmentStatus.CANCELLED.getCode());
        appointment.setCancelReason(cancelReason);
        appointment.setCancelTime(new Date());
        
        return baseMapper.updateById(appointment) > 0;
    }

    @Override
    @Transactional
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 检查是否可以删除
            for (Long id : ids) {
                MeetingAppointment appointment = baseMapper.selectById(id);
                if (appointment != null && !AppointmentStatus.DRAFT.getCode().equals(appointment.getStatus())) {
                    throw new ServiceException("只有草稿状态的预约才能删除");
                }
            }
        }
        
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<MeetingStatisticsVo> getStatistics() {
        // 获取本周的开始和结束时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date weekStart = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_WEEK, 7);
        Date weekEnd = calendar.getTime();
        
        // 查询本周的预约统计
        List<Map<String, Object>> statistics = baseMapper.selectMaps(
            new LambdaQueryWrapper<MeetingAppointment>()
                .ge(MeetingAppointment::getCreateTime, weekStart)
                .le(MeetingAppointment::getCreateTime, weekEnd)
                .eq(MeetingAppointment::getDelFlag, "0")
                .groupBy(MeetingAppointment::getRoomId)
        );
        
        // 按会议室类型统计
        Map<String, MeetingStatisticsVo> typeStatistics = new HashMap<>();
        
        for (Map<String, Object> stat : statistics) {
            Long roomId = (Long) stat.get("room_id");
            Long totalCount = (Long) stat.get("total_count");
            Long approvedCount = (Long) stat.get("approved_count");
            
            MeetingRoom room = roomMapper.selectById(roomId);
            if (room != null) {
                String roomType = room.getRoomType();
                MeetingStatisticsVo typeStat = typeStatistics.computeIfAbsent(roomType, k -> {
                    MeetingStatisticsVo vo = new MeetingStatisticsVo();
                    vo.setRoomType(roomType);
                    vo.setRoomTypeName(MeetingRoomType.getByCode(roomType).getInfo());
                    vo.setTotalApplications(0L);
                    vo.setApprovedCount(0L);
                    return vo;
                });
                
                typeStat.setTotalApplications(typeStat.getTotalApplications() + totalCount);
                typeStat.setApprovedCount(typeStat.getApprovedCount() + approvedCount);
            }
        }
        
        // 计算成功率
        List<MeetingStatisticsVo> result = new ArrayList<>(typeStatistics.values());
        for (MeetingStatisticsVo vo : result) {
            if (vo.getTotalApplications() > 0) {
                double successRate = (double) vo.getApprovedCount() / vo.getTotalApplications() * 100;
                vo.setSuccessRate(Math.round(successRate * 100.0) / 100.0);
            } else {
                vo.setSuccessRate(0.0);
            }
        }
        
        return result;
    }
}