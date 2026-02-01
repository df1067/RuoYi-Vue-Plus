package org.dromara.meeting.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meeting.domain.bo.MeetingAppointmentBo;
import org.dromara.meeting.domain.vo.MeetingAppointmentVo;
import org.dromara.meeting.domain.vo.MeetingStatisticsVo;
import org.dromara.meeting.service.IMeetingAppointmentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.excel.utils.ExcelUtil;

import java.util.List;

/**
 * 预约记录Controller
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meeting/appointment")
public class MeetingAppointmentController {

    private final IMeetingAppointmentService meetingAppointmentService;

    /**
     * 查询预约记录列表
     */
    @SaCheckPermission("meeting:appointment:list")
    @GetMapping("/list")
    public TableDataInfo<MeetingAppointmentVo> list(MeetingAppointmentBo bo, PageQuery pageQuery) {
        return meetingAppointmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出预约记录列表
     */
    @SaCheckPermission("meeting:appointment:export")
    @PostMapping("/export")
    public void export(MeetingAppointmentBo bo, HttpServletResponse response) {
        List<MeetingAppointmentVo> list = meetingAppointmentService.queryList(bo);
        ExcelUtil.exportExcel(list, "预约记录数据", MeetingAppointmentVo.class, response);
    }

    /**
     * 获取预约记录详细信息
     */
    @SaCheckPermission("meeting:appointment:query")
    @GetMapping("/{appointmentId}")
    public R<MeetingAppointmentVo> getInfo(@PathVariable Long appointmentId) {
        return R.ok(meetingAppointmentService.queryById(appointmentId));
    }

    /**
     * 新增预约记录
     */
    @SaCheckPermission("meeting:appointment:add")
    @PostMapping()
    public R<Void> add(@Validated @RequestBody MeetingAppointmentBo bo) {
        return toAjax(meetingAppointmentService.insertByBo(bo));
    }

    /**
     * 修改预约记录
     */
    @SaCheckPermission("meeting:appointment:edit")
    @PutMapping()
    public R<Void> edit(@Validated @RequestBody MeetingAppointmentBo bo) {
        return toAjax(meetingAppointmentService.updateByBo(bo));
    }

    /**
     * 删除预约记录
     */
    @SaCheckPermission("meeting:appointment:remove")
    @DeleteMapping("/{appointmentIds}")
    public R<Void> remove(@PathVariable Long[] appointmentIds) {
        return toAjax(meetingAppointmentService.deleteWithValidByIds(List.of(appointmentIds), true));
    }

    /**
     * 提交预约申请
     */
    @SaCheckPermission("meeting:appointment:submit")
    @PostMapping("/submit/{appointmentId}")
    public R<Void> submit(@PathVariable Long appointmentId) {
        return toAjax(meetingAppointmentService.submitAppointment(appointmentId));
    }

    /**
     * 取消预约
     */
    @SaCheckPermission("meeting:appointment:cancel")
    @PostMapping("/cancel/{appointmentId}")
    public R<Void> cancel(@PathVariable Long appointmentId, @RequestParam(required = false) String cancelReason) {
        return toAjax(meetingAppointmentService.cancelAppointment(appointmentId, cancelReason));
    }

    /**
     * 获取预约统计信息
     */
    @SaCheckPermission("meeting:appointment:statistics")
    @GetMapping("/statistics")
    public R<List<MeetingStatisticsVo>> getStatistics() {
        return R.ok(meetingAppointmentService.getStatistics());
    }

    private R<Void> toAjax(Boolean success) {
        return success ? R.ok() : R.fail();
    }
}