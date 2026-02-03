package org.dromara.meetingroom.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.meetingroom.domain.MeetingRoomApply;
import org.dromara.meetingroom.domain.vo.MeetingRoomApplyVo;

import java.util.Date;
import java.util.List;

/**
 * 多功能厅预约申请Mapper接口
 *
 * @author Lion Li
 */
public interface MeetingRoomApplyMapper extends BaseMapperPlus<MeetingRoomApply, MeetingRoomApplyVo> {

    /**
     * 检查会议室时间冲突
     *
     * @param roomId     会议室ID
     * @param meetingDate 会议日期
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param applyId     申请ID（用于排除自身）
     * @return 冲突的申请数量
     */
    int checkTimeConflict(@Param("roomId") Long roomId, @Param("meetingDate") Date meetingDate, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("applyId") Long applyId);

    /**
     * 查询用户待审批的预约数量
     *
     * @param userId 用户ID
     * @return 待审批的预约数量
     */
    int countPendingApplies(@Param("userId") Long userId);

    /**
     * 查询用户在同一时间段内待审批的预约数量
     *
     * @param userId 用户ID
     * @param meetingDate 会议日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param applyId 申请ID（更新时排除自身）
     * @return 待审批的预约数量
     */
    int countOverlappingPendingApplies(@Param("userId") Long userId, @Param("meetingDate") Date meetingDate, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("applyId") Long applyId);

    /**
     * 统计本周内各类会议室的预约成功率
     *
     * @param startDate 本周开始日期
     * @param endDate   本周结束日期
     * @return 预约成功率统计
     */
    List<MeetingRoomApplyVo> countWeeklySuccessRate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}