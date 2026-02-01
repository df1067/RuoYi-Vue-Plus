package org.dromara.meetingroom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meetingroom.domain.MeetingRoom;
import org.dromara.meetingroom.domain.bo.MeetingRoomBo;
import org.dromara.meetingroom.domain.vo.MeetingRoomVo;
import org.dromara.meetingroom.mapper.MeetingRoomMapper;
import org.dromara.meetingroom.service.IMeetingRoomService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 多功能厅Service业务层处理
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    private final MeetingRoomMapper baseMapper;

    @Override
    public MeetingRoomVo selectMeetingRoomById(Long roomId) {
        return baseMapper.selectVoById(roomId);
    }

    @Override
    public TableDataInfo<MeetingRoomVo> selectPageMeetingRoomList(MeetingRoomBo meetingRoomBo, PageQuery pageQuery) {
        LambdaQueryWrapper<MeetingRoom> lqw = buildQueryWrapper(meetingRoomBo);
        Page<MeetingRoomVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    @Override
    public List<MeetingRoomVo> selectMeetingRoomList(MeetingRoomBo meetingRoomBo) {
        LambdaQueryWrapper<MeetingRoom> lqw = buildQueryWrapper(meetingRoomBo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MeetingRoom> buildQueryWrapper(MeetingRoomBo meetingRoomBo) {
        LambdaQueryWrapper<MeetingRoom> lqw = Wrappers.lambdaQuery();
        lqw.like(BeanUtil.isNotEmpty(meetingRoomBo.getRoomName()), MeetingRoom::getRoomName, meetingRoomBo.getRoomName());
        lqw.eq(BeanUtil.isNotEmpty(meetingRoomBo.getRoomType()), MeetingRoom::getRoomType, meetingRoomBo.getRoomType());
        lqw.eq(BeanUtil.isNotEmpty(meetingRoomBo.getStatus()), MeetingRoom::getStatus, meetingRoomBo.getStatus());
        lqw.eq(MeetingRoom::getDelFlag, "0");
        return lqw;
    }

    @Override
    public int insertMeetingRoom(MeetingRoomBo meetingRoomBo) {
        MeetingRoom meetingRoom = BeanUtil.toBean(meetingRoomBo, MeetingRoom.class);
        return baseMapper.insert(meetingRoom);
    }

    @Override
    public int updateMeetingRoom(MeetingRoomBo meetingRoomBo) {
        MeetingRoom meetingRoom = BeanUtil.toBean(meetingRoomBo, MeetingRoom.class);
        return baseMapper.updateById(meetingRoom);
    }

    @Override
    public int deleteMeetingRoomById(Long roomId) {
        return baseMapper.deleteById(roomId);
    }

    @Override
    public int deleteMeetingRoomByIds(List<Long> roomIds) {
        return baseMapper.deleteBatchIds(roomIds);
    }
}