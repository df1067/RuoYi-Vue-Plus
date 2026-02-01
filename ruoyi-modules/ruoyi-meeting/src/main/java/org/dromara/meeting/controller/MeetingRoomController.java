package org.dromara.meeting.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meeting.domain.bo.MeetingRoomBo;
import org.dromara.meeting.domain.vo.MeetingRoomVo;
import org.dromara.meeting.service.IMeetingRoomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.excel.utils.ExcelUtil;

import java.util.List;

/**
 * 会议室Controller
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meeting/room")
public class MeetingRoomController {

    private final IMeetingRoomService meetingRoomService;

    /**
     * 查询会议室列表
     */
    @SaCheckPermission("meeting:room:list")
    @GetMapping("/list")
    public TableDataInfo<MeetingRoomVo> list(MeetingRoomBo bo, PageQuery pageQuery) {
        return meetingRoomService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出会议室列表
     */
    @SaCheckPermission("meeting:room:export")
    @PostMapping("/export")
    public void export(MeetingRoomBo bo, HttpServletResponse response) {
        List<MeetingRoomVo> list = meetingRoomService.queryList(bo);
        ExcelUtil.exportExcel(list, "会议室数据", MeetingRoomVo.class, response);
    }

    /**
     * 获取会议室详细信息
     */
    @SaCheckPermission("meeting:room:query")
    @GetMapping("/{roomId}")
    public R<MeetingRoomVo> getInfo(@PathVariable Long roomId) {
        return R.ok(meetingRoomService.queryById(roomId));
    }

    /**
     * 新增会议室
     */
    @SaCheckPermission("meeting:room:add")
    @PostMapping()
    public R<Void> add(@Validated @RequestBody MeetingRoomBo bo) {
        return toAjax(meetingRoomService.insertByBo(bo));
    }

    /**
     * 修改会议室
     */
    @SaCheckPermission("meeting:room:edit")
    @PutMapping()
    public R<Void> edit(@Validated @RequestBody MeetingRoomBo bo) {
        return toAjax(meetingRoomService.updateByBo(bo));
    }

    /**
     * 删除会议室
     */
    @SaCheckPermission("meeting:room:remove")
    @DeleteMapping("/{roomIds}")
    public R<Void> remove(@PathVariable Long[] roomIds) {
        return toAjax(meetingRoomService.deleteWithValidByIds(List.of(roomIds), true));
    }

    private R<Void> toAjax(Boolean success) {
        return success ? R.ok() : R.fail();
    }
}