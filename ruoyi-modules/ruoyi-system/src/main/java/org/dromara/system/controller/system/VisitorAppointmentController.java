package org.dromara.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.bo.VisitorAppointmentBo;
import org.dromara.system.domain.vo.VisitorAppointmentVo;
import org.dromara.system.service.IVisitorAppointmentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 访客预约
 *
 * @author System
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/visitorAppointment")
public class VisitorAppointmentController extends BaseController {

    private final IVisitorAppointmentService visitorAppointmentService;

    /**
     * 查询访客预约列表
     */
    @SaCheckPermission("system:visitorAppointment:list")
    @GetMapping("/list")
    public TableDataInfo<VisitorAppointmentVo> list(VisitorAppointmentBo bo, PageQuery pageQuery) {
        return visitorAppointmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出访客预约列表
     */
    @SaCheckPermission("system:visitorAppointment:export")
    @Log(title = "访客预约", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(VisitorAppointmentBo bo, HttpServletResponse response) {
        List<VisitorAppointmentVo> list = visitorAppointmentService.queryList(bo);
        ExcelUtil.exportExcel(list, "访客预约", VisitorAppointmentVo.class, response);
    }

    /**
     * 获取访客预约详细信息
     */
    @SaCheckPermission("system:visitorAppointment:query")
    @GetMapping("/{appointmentId}")
    public R<VisitorAppointmentVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long appointmentId) {
        return R.ok(visitorAppointmentService.queryById(appointmentId));
    }

    /**
     * 新增访客预约
     */
    @SaCheckPermission("system:visitorAppointment:add")
    @Log(title = "访客预约", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody VisitorAppointmentBo bo) {
        return toAjax(visitorAppointmentService.insertByBo(bo));
    }

    /**
     * 修改访客预约
     */
    @SaCheckPermission("system:visitorAppointment:edit")
    @Log(title = "访客预约", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody VisitorAppointmentBo bo) {
        return toAjax(visitorAppointmentService.updateByBo(bo));
    }

    /**
     * 删除访客预约
     */
    @SaCheckPermission("system:visitorAppointment:remove")
    @Log(title = "访客预约", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appointmentIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] appointmentIds) {
        return toAjax(visitorAppointmentService.deleteWithValidByIds(List.of(appointmentIds), true));
    }

    /**
     * 确认预约
     */
    @SaCheckPermission("system:visitorAppointment:confirm")
    @Log(title = "访客预约", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/confirm/{appointmentId}")
    public R<Void> confirm(@NotNull(message = "主键不能为空") @PathVariable Long appointmentId) {
        return toAjax(visitorAppointmentService.confirmAppointment(appointmentId));
    }

    /**
     * 拒绝预约
     */
    @SaCheckPermission("system:visitorAppointment:reject")
    @Log(title = "访客预约", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/reject/{appointmentId}")
    public R<Void> reject(@NotNull(message = "主键不能为空") @PathVariable Long appointmentId, 
                         @RequestParam String rejectReason) {
        return toAjax(visitorAppointmentService.rejectAppointment(appointmentId, rejectReason));
    }

    /**
     * 取消预约
     */
    @SaCheckPermission("system:visitorAppointment:cancel")
    @Log(title = "访客预约", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/cancel/{appointmentId}")
    public R<Void> cancel(@NotNull(message = "主键不能为空") @PathVariable Long appointmentId) {
        return toAjax(visitorAppointmentService.cancelAppointment(appointmentId));
    }

    /**
     * 检查预约冲突
     */
    @SaCheckPermission("system:visitorAppointment:list")
    @PostMapping("/checkConflict")
    public R<Boolean> checkConflict(@RequestBody VisitorAppointmentBo bo) {
        return R.ok(visitorAppointmentService.checkAppointmentConflict(bo));
    }
}