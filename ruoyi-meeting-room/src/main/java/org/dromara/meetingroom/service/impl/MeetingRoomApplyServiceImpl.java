package org.dromara.meetingroom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.security.utils.LoginHelper;
import org.dromara.meetingroom.domain.MeetingRoom;
import org.dromara.meetingroom.domain.MeetingRoomApply;
import org.dromara.meetingroom.domain.bo.MeetingRoomApplyBo;
import org.dromara.meetingroom.domain.vo.MeetingRoomApplyVo;
import org.dromara.meetingroom.mapper.MeetingRoomApplyMapper;
import org.dromara.meetingroom.mapper.MeetingRoomMapper;
import org.dromara.meetingroom.service.IMeetingRoomApplyService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 多功能厅预约申请Service业务层处理
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class MeetingRoomApplyServiceImpl extends ServiceImpl<MeetingRoomApplyMapper, MeetingRoomApply> implements IMeetingRoomApplyService {

    private final MeetingRoomApplyMapper baseMapper;
    private final MeetingRoomMapper meetingRoomMapper;

    @Override
    public MeetingRoomApplyVo selectMeetingRoomApplyById(Long applyId) {
        return baseMapper.selectVoById(applyId);
    }

    @Override
    public TableDataInfo<MeetingRoomApplyVo> selectPageMeetingRoomApplyList(MeetingRoomApplyBo meetingRoomApplyBo, PageQuery pageQuery) {
        LambdaQueryWrapper<MeetingRoomApply> lqw = buildQueryWrapper(meetingRoomApplyBo);
        Page<MeetingRoomApplyVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    @Override
    public List<MeetingRoomApplyVo> selectMeetingRoomApplyList(MeetingRoomApplyBo meetingRoomApplyBo) {
        LambdaQueryWrapper<MeetingRoomApply> lqw = buildQueryWrapper(meetingRoomApplyBo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MeetingRoomApply> buildQueryWrapper(MeetingRoomApplyBo meetingRoomApplyBo) {
        LambdaQueryWrapper<MeetingRoomApply> lqw = Wrappers.lambdaQuery();
        lqw.eq(BeanUtil.isNotEmpty(meetingRoomApplyBo.getRoomId()), MeetingRoomApply::getRoomId, meetingRoomApplyBo.getRoomId());
        lqw.eq(BeanUtil.isNotEmpty(meetingRoomApplyBo.getApplyUserId()), MeetingRoomApply::getApplyUserId, meetingRoomApplyBo.getApplyUserId());
        lqw.eq(BeanUtil.isNotEmpty(meetingRoomApplyBo.getApplyDeptId()), MeetingRoomApply::getApplyDeptId, meetingRoomApplyBo.getApplyDeptId());
        lqw.eq(BeanUtil.isNotEmpty(meetingRoomApplyBo.getMeetingDate()), MeetingRoomApply::getMeetingDate, meetingRoomApplyBo.getMeetingDate());
        lqw.eq(BeanUtil.isNotEmpty(meetingRoomApplyBo.getStatus()), MeetingRoomApply::getStatus, meetingRoomApplyBo.getStatus());
        lqw.eq(MeetingRoomApply::getDelFlag, "0");
        return lqw;
    }

    @Override
    public int insertMeetingRoomApply(MeetingRoomApplyBo meetingRoomApplyBo) {
        // 检查用户权限
        checkUserPermission(meetingRoomApplyBo.getRoomId());

        // 检查时间冲突
        checkTimeConflict(meetingRoomApplyBo);

        // 检查用户待审批的预约数量
        checkPendingApplies(LoginHelper.getUserId());

        MeetingRoomApply meetingRoomApply = BeanUtil.toBean(meetingRoomApplyBo, MeetingRoomApply.class);
        meetingRoomApply.setApplyUserId(LoginHelper.getUserId());
        meetingRoomApply.setApplyDeptId(LoginHelper.getDeptId());
        meetingRoomApply.setApplyTime(new Date());
        meetingRoomApply.setStatus("0"); // 初始状态为草稿
        return baseMapper.insert(meetingRoomApply);
    }

    @Override
    public int updateMeetingRoomApply(MeetingRoomApplyBo meetingRoomApplyBo) {
        // 检查时间冲突
        checkTimeConflict(meetingRoomApplyBo);

        MeetingRoomApply meetingRoomApply = BeanUtil.toBean(meetingRoomApplyBo, MeetingRoomApply.class);
        return baseMapper.updateById(meetingRoomApply);
    }

    @Override
    public int deleteMeetingRoomApplyById(Long applyId) {
        return baseMapper.deleteById(applyId);
    }

    @Override
    public int deleteMeetingRoomApplyByIds(List<Long> applyIds) {
        return baseMapper.deleteBatchIds(applyIds);
    }

    @Override
    public int submitMeetingRoomApply(Long applyId) {
        MeetingRoomApply meetingRoomApply = baseMapper.selectById(applyId);
        if (meetingRoomApply == null) {
            throw new ServiceException("预约申请不存在");
        }

        // 检查用户权限
        checkUserPermission(meetingRoomApply.getRoomId());

        // 检查时间冲突
        MeetingRoomApplyBo meetingRoomApplyBo = BeanUtil.toBean(meetingRoomApply, MeetingRoomApplyBo.class);
        checkTimeConflict(meetingRoomApplyBo);

        // 检查用户待审批的预约数量
        checkPendingApplies(LoginHelper.getUserId());

        meetingRoomApply.setStatus("1"); // 状态变为已提交
        return baseMapper.updateById(meetingRoomApply);
    }

    @Override
    public int deptApproveMeetingRoomApply(Long applyId, String approveOpinion, String approveStatus) {
        MeetingRoomApply meetingRoomApply = baseMapper.selectById(applyId);
        if (meetingRoomApply == null) {
            throw new ServiceException("预约申请不存在");
        }

        meetingRoomApply.setDeptApproverId(LoginHelper.getUserId());
        meetingRoomApply.setDeptApproveTime(new Date());
        meetingRoomApply.setDeptApproveOpinion(approveOpinion);

        if ("0".equals(approveStatus)) {
            // 部门审核通过，状态变为行政复核中
            meetingRoomApply.setStatus("3");
        } else {
            // 部门审核拒绝，流程终止
            meetingRoomApply.setStatus("5");
        }

        return baseMapper.updateById(meetingRoomApply);
    }

    @Override
    public int adminApproveMeetingRoomApply(Long applyId, String approveOpinion, String approveStatus) {
        MeetingRoomApply meetingRoomApply = baseMapper.selectById(applyId);
        if (meetingRoomApply == null) {
            throw new ServiceException("预约申请不存在");
        }

        meetingRoomApply.setAdminApproverId(LoginHelper.getUserId());
        meetingRoomApply.setAdminApproveTime(new Date());
        meetingRoomApply.setAdminApproveOpinion(approveOpinion);

        if ("0".equals(approveStatus)) {
            // 行政复核通过，状态变为已批准
            meetingRoomApply.setStatus("4");
        } else {
            // 行政复核拒绝，流程终止
            meetingRoomApply.setStatus("5");
        }

        return baseMapper.updateById(meetingRoomApply);
    }

    @Override
    public int cancelMeetingRoomApply(Long applyId, String cancelReason) {
        MeetingRoomApply meetingRoomApply = baseMapper.selectById(applyId);
        if (meetingRoomApply == null) {
            throw new ServiceException("预约申请不存在");
        }

        // 检查大型厅24小时内不可取消
        MeetingRoom meetingRoom = meetingRoomMapper.selectById(meetingRoomApply.getRoomId());
        if (meetingRoom != null && "1".equals(meetingRoom.getRoomType())) {
            Date meetingTime = DateUtil.parse(DateUtil.format(meetingRoomApply.getMeetingDate(), "yyyy-MM-dd") + " " + DateUtil.format(meetingRoomApply.getStartTime(), "HH:mm:ss"));
            long hours = DateUtil.between(new Date(), meetingTime, DateUnit.HOUR);
            if (hours < 24) {
                throw new ServiceException("大型厅在会议开始前24小时内不可取消");
            }
        }

        meetingRoomApply.setStatus("6"); // 状态变为已取消
        meetingRoomApply.setCancelTime(new Date());
        meetingRoomApply.setCancelReason(cancelReason);
        return baseMapper.updateById(meetingRoomApply);
    }

    @Override
    public List<MeetingRoomApplyVo> countWeeklySuccessRate() {
        // 获取本周开始和结束日期
        Date startDate = DateUtil.beginOfWeek(new Date());
        Date endDate = DateUtil.endOfWeek(new Date());
        return baseMapper.countWeeklySuccessRate(startDate, endDate);
    }

    /**
     * 检查用户权限
     *
     * @param roomId 会议室ID
     */
    private void checkUserPermission(Long roomId) {
        MeetingRoom meetingRoom = meetingRoomMapper.selectById(roomId);
        if (meetingRoom != null && "1".equals(meetingRoom.getRoomType())) {
            // 大型厅仅限部门总监及以上权限人员申请
            // 这里需要根据实际权限体系进行校验，示例中假设部门总监的角色ID为2
            if (!LoginHelper.isSuperAdmin() && !LoginHelper.hasRole("2")) {
                throw new ServiceException("大型厅仅限部门总监及以上权限人员申请");
            }
        }
    }

    /**
     * 检查时间冲突
     *
     * @param meetingRoomApplyBo 预约申请
     */
    private void checkTimeConflict(MeetingRoomApplyBo meetingRoomApplyBo) {
        int conflictCount = baseMapper.checkTimeConflict(meetingRoomApplyBo.getRoomId(), meetingRoomApplyBo.getMeetingDate(), meetingRoomApplyBo.getStartTime(), meetingRoomApplyBo.getEndTime(), meetingRoomApplyBo.getApplyId());
        if (conflictCount > 0) {
            throw new ServiceException("该会议室在指定时间内已有预约");
        }
    }

    /**
     * 检查用户待审批的预约数量
     *
     * @param userId 用户ID
     */
    private void checkPendingApplies(Long userId) {
        int pendingCount = baseMapper.countPendingApplies(userId);
        if (pendingCount >= 2) {
            throw new ServiceException("同一用户在同一时间段内，最多只能有2个待审批的预约");
        }
    }
}