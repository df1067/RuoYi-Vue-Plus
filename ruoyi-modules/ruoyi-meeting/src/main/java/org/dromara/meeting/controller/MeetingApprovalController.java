package org.dromara.meeting.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.meeting.domain.vo.MeetingApprovalVo;
import org.dromara.meeting.service.IMeetingApprovalService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批流程Controller
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meeting/approval")
public class MeetingApprovalController {

    private final IMeetingApprovalService meetingApprovalService;

    /**
     * 部门审批
     */
    @SaCheckPermission("meeting:approval:dept")
    @PostMapping("/dept/{appointmentId}")
    public R<Void> deptApprove(@PathVariable Long appointmentId, 
                                @RequestParam Boolean approved,
                                @RequestParam(required = false) String opinion) {
        return toAjax(meetingApprovalService.deptApprove(appointmentId, approved, opinion));
    }

    /**
     * 行政审批
     */
    @SaCheckPermission("meeting:approval:admin")
    @PostMapping("/admin/{appointmentId}")
    public R<Void> adminApprove(@PathVariable Long appointmentId,
                                 @RequestParam Boolean approved,
                                 @RequestParam(required = false) String opinion) {
        return toAjax(meetingApprovalService.adminApprove(appointmentId, approved, opinion));
    }

    /**
     * 获取预约的审批记录
     */
    @SaCheckPermission("meeting:approval:list")
    @GetMapping("/records/{appointmentId}")
    public R<List<MeetingApprovalVo>> getApprovalRecords(@PathVariable Long appointmentId) {
        return R.ok(meetingApprovalService.getApprovalRecords(appointmentId));
    }

    private R<Void> toAjax(Boolean success) {
        return success ? R.ok() : R.fail();
    }
}