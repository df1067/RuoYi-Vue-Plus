package org.dromara.meetingroom.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meetingroom.domain.MeetingRoomApply;
import org.dromara.meetingroom.domain.bo.MeetingRoomApplyBo;
import org.dromara.meetingroom.domain.vo.MeetingRoomApplyVo;

import java.util.List;

/**
 * 多功能厅预约申请Service接口
 *
 * @author Lion Li
 */
public interface IMeetingRoomApplyService {

    /**
     * 查询多功能厅预约申请
     *
     * @param applyId 多功能厅预约申请主键
     * @return 多功能厅预约申请
     */
    MeetingRoomApplyVo selectMeetingRoomApplyById(Long applyId);

    /**
     * 查询多功能厅预约申请列表
     *
     * @param meetingRoomApplyBo 多功能厅预约申请
     * @param pageQuery          分页参数
     * @return 多功能厅预约申请集合
     */
    TableDataInfo<MeetingRoomApplyVo> selectPageMeetingRoomApplyList(MeetingRoomApplyBo meetingRoomApplyBo, PageQuery pageQuery);

    /**
     * 查询多功能厅预约申请列表
     *
     * @param meetingRoomApplyBo 多功能厅预约申请
     * @return 多功能厅预约申请集合
     */
    List<MeetingRoomApplyVo> selectMeetingRoomApplyList(MeetingRoomApplyBo meetingRoomApplyBo);

    /**
     * 新增多功能厅预约申请
     *
     * @param meetingRoomApplyBo 多功能厅预约申请
     * @return 结果
     */
    int insertMeetingRoomApply(MeetingRoomApplyBo meetingRoomApplyBo);

    /**
     * 修改多功能厅预约申请
     *
     * @param meetingRoomApplyBo 多功能厅预约申请
     * @return 结果
     */
    int updateMeetingRoomApply(MeetingRoomApplyBo meetingRoomApplyBo);

    /**
     * 删除多功能厅预约申请信息
     *
     * @param applyId 多功能厅预约申请主键
     * @return 结果
     */
    int deleteMeetingRoomApplyById(Long applyId);

    /**
     * 批量删除多功能厅预约申请信息
     *
     * @param applyIds 需要删除的多功能厅预约申请主键集合
     * @return 结果
     */
    int deleteMeetingRoomApplyByIds(List<Long> applyIds);

    /**
     * 提交预约申请
     *
     * @param applyId 申请ID
     * @return 结果
     */
    int submitMeetingRoomApply(Long applyId);

    /**
     * 部门审核预约申请
     *
     * @param applyId   申请ID
     * @param approveOpinion 审核意见
     * @param approveStatus 审核状态（0通过 1拒绝）
     * @return 结果
     */
    int deptApproveMeetingRoomApply(Long applyId, String approveOpinion, String approveStatus);

    /**
     * 行政复核预约申请
     *
     * @param applyId   申请ID
     * @param approveOpinion 复核意见
     * @param approveStatus 复核状态（0通过 1拒绝）
     * @return 结果
     */
    int adminApproveMeetingRoomApply(Long applyId, String approveOpinion, String approveStatus);

    /**
     * 取消预约申请
     *
     * @param applyId   申请ID
     * @param cancelReason 取消原因
     * @return 结果
     */
    int cancelMeetingRoomApply(Long applyId, String cancelReason);

    /**
     * 统计本周内各类会议室的预约成功率
     *
     * @return 预约成功率统计
     */
    List<MeetingRoomApplyVo> countWeeklySuccessRate();
}