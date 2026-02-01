package org.dromara.meetingroom.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.security.annotation.RequiresPermissions;
import org.dromara.meetingroom.domain.bo.MeetingRoomApplyBo;
import org.dromara.meetingroom.domain.vo.MeetingRoomApplyVo;
import org.dromara.meetingroom.service.IMeetingRoomApplyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 多功能厅预约申请Controller
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meeting-room/apply")
public class MeetingRoomApplyController {

    private final IMeetingRoomApplyService meetingRoomApplyService;

    /**
     * 查询多功能厅预约申请列表
     */
    @RequiresPermissions("meeting-room:apply:list")
    @GetMapping("/list")
    public R<List<MeetingRoomApplyVo>> list(MeetingRoomApplyBo meetingRoomApplyBo) {
        List<MeetingRoomApplyVo> list = meetingRoomApplyService.selectMeetingRoomApplyList(meetingRoomApplyBo);
        return R.ok(list);
    }

    /**
     * 查询多功能厅预约申请分页列表
     */
    @RequiresPermissions("meeting-room:apply:list")
    @GetMapping("/page")
    public R<Page<MeetingRoomApplyVo>> page(MeetingRoomApplyBo meetingRoomApplyBo, Page<MeetingRoomApplyVo> page) {
        return R.ok(meetingRoomApplyService.selectPageMeetingRoomApplyList(meetingRoomApplyBo, page));
    }

    /**
     * 获取多功能厅预约申请详细信息
     */
    @RequiresPermissions("meeting-room:apply:query")
    @GetMapping("/{applyId}")
    public R<MeetingRoomApplyVo> getInfo(@PathVariable("applyId") Long applyId) {
        return R.ok(meetingRoomApplyService.selectMeetingRoomApplyById(applyId));
    }

    /**
     * 新增多功能厅预约申请
     */
    @RequiresPermissions("meeting-room:apply:add")
    @Log(title = "多功能厅预约申请", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MeetingRoomApplyBo meetingRoomApplyBo) {
        return R.toBool(meetingRoomApplyService.insertMeetingRoomApply(meetingRoomApplyBo));
    }

    /**
     * 修改多功能厅预约申请
     */
    @RequiresPermissions("meeting-room:apply:edit")
    @Log(title = "多功能厅预约申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MeetingRoomApplyBo meetingRoomApplyBo) {
        return R.toBool(meetingRoomApplyService.updateMeetingRoomApply(meetingRoomApplyBo));
    }

    /**
     * 删除多功能厅预约申请
     */
    @RequiresPermissions("meeting-room:apply:remove")
    @Log(title = "多功能厅预约申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applyIds}")
    public R<Void> remove(@PathVariable Long[] applyIds) {
        return R.toBool(meetingRoomApplyService.deleteMeetingRoomApplyByIds(List.of(applyIds)));
    }

    /**
     * 提交多功能厅预约申请
     */
    @RequiresPermissions("meeting-room:apply:submit")
    @Log(title = "多功能厅预约申请", businessType = BusinessType.UPDATE)
    @PutMapping("/submit/{applyId}")
    public R<Void> submit(@PathVariable("applyId") Long applyId) {
        return R.toBool(meetingRoomApplyService.submitMeetingRoomApply(applyId));
    }

    /**
     * 部门审核多功能厅预约申请
     */
    @RequiresPermissions("meeting-room:apply:dept-approve")
    @Log(title = "多功能厅预约申请", businessType = BusinessType.UPDATE)
    @PutMapping("/dept-approve/{applyId}")
    public R<Void> deptApprove(@PathVariable("applyId") Long applyId, @RequestParam("approveOpinion") String approveOpinion, @RequestParam("approveStatus") String approveStatus) {
        return R.toBool(meetingRoomApplyService.deptApproveMeetingRoomApply(applyId, approveOpinion, approveStatus));
    }

    /**
     * 行政复核多功能厅预约申请
     */
    @RequiresPermissions("meeting-room:apply:admin-approve")
    @Log(title = "多功能厅预约申请", businessType = BusinessType.UPDATE)
    @PutMapping("/admin-approve/{applyId}")
    public R<Void> adminApprove(@PathVariable("applyId") Long applyId, @RequestParam("approveOpinion") String approveOpinion, @RequestParam("approveStatus") String approveStatus) {
        return R.toBool(meetingRoomApplyService.adminApproveMeetingRoomApply(applyId, approveOpinion, approveStatus));
    }

    /**
     * 取消多功能厅预约申请
     */
    @RequiresPermissions("meeting-room:apply:cancel")
    @Log(title = "多功能厅预约申请", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{applyId}")
    public R<Void> cancel(@PathVariable("applyId") Long applyId, @RequestParam("cancelReason") String cancelReason) {
        return R.toBool(meetingRoomApplyService.cancelMeetingRoomApply(applyId, cancelReason));
    }

    /**
     * 统计本周内各类会议室的预约成功率
     */
    @RequiresPermissions("meeting-room:apply:success-rate")
    @GetMapping("/success-rate")
    public R<List<MeetingRoomApplyVo>> successRate() {
        List<MeetingRoomApplyVo> list = meetingRoomApplyService.countWeeklySuccessRate();
        return R.ok(list);
    }
}