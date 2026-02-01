package org.dromara.meetingroom.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.security.annotation.RequiresPermissions;
import org.dromara.meetingroom.domain.bo.MeetingRoomBo;
import org.dromara.meetingroom.domain.vo.MeetingRoomVo;
import org.dromara.meetingroom.service.IMeetingRoomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 多功能厅Controller
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meeting-room")
public class MeetingRoomController {

    private final IMeetingRoomService meetingRoomService;

    /**
     * 查询多功能厅列表
     */
    @RequiresPermissions("meeting-room:list")
    @GetMapping("/list")
    public R<List<MeetingRoomVo>> list(MeetingRoomBo meetingRoomBo) {
        List<MeetingRoomVo> list = meetingRoomService.selectMeetingRoomList(meetingRoomBo);
        return R.ok(list);
    }

    /**
     * 查询多功能厅分页列表
     */
    @RequiresPermissions("meeting-room:list")
    @GetMapping("/page")
    public R<Page<MeetingRoomVo>> page(MeetingRoomBo meetingRoomBo, Page<MeetingRoomVo> page) {
        return R.ok(meetingRoomService.selectPageMeetingRoomList(meetingRoomBo, page));
    }

    /**
     * 获取多功能厅详细信息
     */
    @RequiresPermissions("meeting-room:query")
    @GetMapping("/{roomId}")
    public R<MeetingRoomVo> getInfo(@PathVariable("roomId") Long roomId) {
        return R.ok(meetingRoomService.selectMeetingRoomById(roomId));
    }

    /**
     * 新增多功能厅
     */
    @RequiresPermissions("meeting-room:add")
    @Log(title = "多功能厅", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MeetingRoomBo meetingRoomBo) {
        return R.toBool(meetingRoomService.insertMeetingRoom(meetingRoomBo));
    }

    /**
     * 修改多功能厅
     */
    @RequiresPermissions("meeting-room:edit")
    @Log(title = "多功能厅", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MeetingRoomBo meetingRoomBo) {
        return R.toBool(meetingRoomService.updateMeetingRoom(meetingRoomBo));
    }

    /**
     * 删除多功能厅
     */
    @RequiresPermissions("meeting-room:remove")
    @Log(title = "多功能厅", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roomIds}")
    public R<Void> remove(@PathVariable Long[] roomIds) {
        return R.toBool(meetingRoomService.deleteMeetingRoomByIds(List.of(roomIds)));
    }
}