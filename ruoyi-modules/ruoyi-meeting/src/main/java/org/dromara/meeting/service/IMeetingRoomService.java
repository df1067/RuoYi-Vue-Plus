package org.dromara.meeting.service;

import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meeting.domain.bo.MeetingRoomBo;
import org.dromara.meeting.domain.vo.MeetingRoomVo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 会议室Service接口
 *
 * @author Lion Li
 */
public interface IMeetingRoomService {

    /**
     * 查询会议室
     */
    MeetingRoomVo queryById(Long roomId);

    /**
     * 查询会议室列表
     */
    TableDataInfo<MeetingRoomVo> queryPageList(MeetingRoomBo bo, PageQuery pageQuery);

    /**
     * 查询会议室列表
     */
    List<MeetingRoomVo> queryList(MeetingRoomBo bo);

    /**
     * 新增会议室
     */
    Boolean insertByBo(MeetingRoomBo bo);

    /**
     * 修改会议室
     */
    Boolean updateByBo(MeetingRoomBo bo);

    /**
     * 校验并批量删除会议室信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}