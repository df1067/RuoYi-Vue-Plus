package org.dromara.meeting.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meeting.domain.MeetingRoom;
import org.dromara.meeting.domain.bo.MeetingRoomBo;
import org.dromara.meeting.domain.vo.MeetingRoomVo;
import org.dromara.meeting.mapper.MeetingRoomMapper;
import org.dromara.meeting.service.IMeetingRoomService;
import org.springframework.stereotype.Service;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.utils.BeanCopyUtils;

import java.util.Collection;
import java.util.List;

/**
 * 会议室Service业务层处理
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class MeetingRoomServiceImpl implements IMeetingRoomService {

    private final MeetingRoomMapper baseMapper;

    @Override
    public MeetingRoomVo queryById(Long roomId) {
        MeetingRoom room = baseMapper.selectById(roomId);
        return BeanCopyUtils.copy(room, MeetingRoomVo.class);
    }

    @Override
    public TableDataInfo<MeetingRoomVo> queryPageList(MeetingRoomBo bo, PageQuery pageQuery) {
        var lqw = buildQueryWrapper(bo);
        TableDataInfo<MeetingRoom> meetingRoomTableDataInfo = baseMapper.selectPage(pageQuery.build(), lqw);
        return BeanCopyUtils.copyPage(meetingRoomTableDataInfo, MeetingRoomVo.class);
    }

    @Override
    public List<MeetingRoomVo> queryList(MeetingRoomBo bo) {
        var lqw = buildQueryWrapper(bo);
        List<MeetingRoom> meetingRoomList = baseMapper.selectList(lqw);
        return BeanCopyUtils.copyList(meetingRoomList, MeetingRoomVo.class);
    }

    private LambdaQueryWrapper<MeetingRoom> buildQueryWrapper(MeetingRoomBo bo) {
        LambdaQueryWrapper<MeetingRoom> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(bo.getRoomName()), MeetingRoom::getRoomName, bo.getRoomName());
        lqw.eq(StringUtils.isNotBlank(bo.getRoomType()), MeetingRoom::getRoomType, bo.getRoomType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MeetingRoom::getStatus, bo.getStatus());
        lqw.eq(MeetingRoom::getDelFlag, "0");
        lqw.orderByAsc(MeetingRoom::getRoomId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(MeetingRoomBo bo) {
        MeetingRoom meetingRoom = BeanCopyUtils.copy(bo, MeetingRoom.class);
        return baseMapper.insert(meetingRoom) > 0;
    }

    @Override
    public Boolean updateByBo(MeetingRoomBo bo) {
        MeetingRoom meetingRoom = BeanCopyUtils.copy(bo, MeetingRoom.class);
        return baseMapper.updateById(meetingRoom) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 检查是否可以删除
            // TODO: 检查是否有相关的预约记录
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}