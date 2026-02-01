package org.dromara.meetingroom.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meetingroom.domain.MeetingRoom;
import org.dromara.meetingroom.domain.bo.MeetingRoomBo;
import org.dromara.meetingroom.domain.vo.MeetingRoomVo;

import java.util.List;

/**
 * 多功能厅Service接口
 *
 * @author Lion Li
 */
public interface IMeetingRoomService {

    /**
     * 查询多功能厅
     *
     * @param roomId 多功能厅主键
     * @return 多功能厅
     */
    MeetingRoomVo selectMeetingRoomById(Long roomId);

    /**
     * 查询多功能厅列表
     *
     * @param meetingRoomBo 多功能厅
     * @param pageQuery     分页参数
     * @return 多功能厅集合
     */
    TableDataInfo<MeetingRoomVo> selectPageMeetingRoomList(MeetingRoomBo meetingRoomBo, PageQuery pageQuery);

    /**
     * 查询多功能厅列表
     *
     * @param meetingRoomBo 多功能厅
     * @return 多功能厅集合
     */
    List<MeetingRoomVo> selectMeetingRoomList(MeetingRoomBo meetingRoomBo);

    /**
     * 新增多功能厅
     *
     * @param meetingRoomBo 多功能厅
     * @return 结果
     */
    int insertMeetingRoom(MeetingRoomBo meetingRoomBo);

    /**
     * 修改多功能厅
     *
     * @param meetingRoomBo 多功能厅
     * @return 结果
     */
    int updateMeetingRoom(MeetingRoomBo meetingRoomBo);

    /**
     * 删除多功能厅信息
     *
     * @param roomId 多功能厅主键
     * @return 结果
     */
    int deleteMeetingRoomById(Long roomId);

    /**
     * 批量删除多功能厅信息
     *
     * @param roomIds 需要删除的多功能厅主键集合
     * @return 结果
     */
    int deleteMeetingRoomByIds(List<Long> roomIds);
}