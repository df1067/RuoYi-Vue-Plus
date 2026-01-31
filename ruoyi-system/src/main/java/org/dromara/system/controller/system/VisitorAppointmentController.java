package org.dromara.system.controller.system;

import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.security.annotation.RequiresPermissions;
import org.dromara.system.domain.bo.VisitorAppointmentBo;
import org.dromara.system.domain.vo.VisitorAppointmentVo;
import org.dromara.system.service.IVisitorAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 访客预约登记Controller
 * 
 * @author ruoyi
 * @date 2026-01-31
 */
@Tag(name = "访客预约登记")
@RestController
@RequestMapping("/system/visitor-appointment")
@RequiredArgsConstructor
public class VisitorAppointmentController {

    private final IVisitorAppointmentService visitorAppointmentService;

    /**
     * 查询访客预约登记列表
     */
    @Operation(summary = "查询访客预约登记列表")
    @RequiresPermissions("system:visitor-appointment:list")
    @GetMapping("/list")
    public R<IPage<VisitorAppointmentVo>> list(VisitorAppointmentBo visitorAppointmentBo) {
        IPage<VisitorAppointmentVo> page = visitorAppointmentService.selectVisitorAppointmentPage(
                visitorAppointmentService.buildPage(), visitorAppointmentBo);
        return R.ok(page);
    }

    /**
     * 获取访客预约登记详细信息
     */
    @Operation(summary = "获取访客预约登记详细信息")
    @RequiresPermissions("system:visitor-appointment:query")
    @GetMapping(value = "/{appointmentId}")
    public R<VisitorAppointmentVo> getInfo(@NotNull(message = "预约ID不能为空") @PathVariable("appointmentId") Long appointmentId) {
        return R.ok(visitorAppointmentService.selectVisitorAppointmentByAppointmentId(appointmentId));
    }

    /**
     * 新增访客预约登记
     */
    @Operation(summary = "新增访客预约登记")
    @RequiresPermissions("system:visitor-appointment:add")
    @Log(title = "访客预约登记", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody VisitorAppointmentBo visitorAppointmentBo) {
        visitorAppointmentService.insertVisitorAppointment(visitorAppointmentBo);
        return R.ok();
    }

    /**
     * 修改访客预约登记
     */
    @Operation(summary = "修改访客预约登记")
    @RequiresPermissions("system:visitor-appointment:edit")
    @Log(title = "访客预约登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody VisitorAppointmentBo visitorAppointmentBo) {
        visitorAppointmentService.updateVisitorAppointment(visitorAppointmentBo);
        return R.ok();
    }

    /**
     * 确认访客预约
     */
    @Operation(summary = "确认访客预约")
    @RequiresPermissions("system:visitor-appointment:confirm")
    @Log(title = "访客预约登记", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm/{appointmentId}")
    public R<Void> confirm(@NotNull(message = "预约ID不能为空") @PathVariable("appointmentId") Long appointmentId) {
        visitorAppointmentService.confirmVisitorAppointment(appointmentId);
        return R.ok();
    }

    /**
     * 拒绝访客预约
     */
    @Operation(summary = "拒绝访客预约")
    @RequiresPermissions("system:visitor-appointment:reject")
    @Log(title = "访客预约登记", businessType = BusinessType.UPDATE)
    @PutMapping("/reject/{appointmentId}")
    public R<Void> reject(@NotNull(message = "预约ID不能为空") @PathVariable("appointmentId") Long appointmentId, @RequestParam String rejectReason) {
        visitorAppointmentService.rejectVisitorAppointment(appointmentId, rejectReason);
        return R.ok();
    }

    /**
     * 删除访客预约登记
     */
    @Operation(summary = "删除访客预约登记")
    @RequiresPermissions("system:visitor-appointment:remove")
    @Log(title = "访客预约登记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appointmentIds}")
    public R<Void> remove(@NotEmpty(message = "预约ID不能为空") @PathVariable Long[] appointmentIds) {
        visitorAppointmentService.deleteVisitorAppointmentByAppointmentIds(appointmentIds);
        return R.ok();
    }
}