package org.dromara.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.bo.SysVisitorRegistrationBo;
import org.dromara.system.domain.vo.SysVisitorRegistrationVo;
import org.dromara.system.service.ISysVisitorRegistrationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 访客预约登记控制器
 *
 * @author System
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/visitor")
public class SysVisitorRegistrationController extends BaseController {

    private final ISysVisitorRegistrationService visitorRegistrationService;

    /**
     * 查询访客预约登记列表
     */
    @SaCheckPermission("system:visitor:list")
    @GetMapping("/list")
    public TableDataInfo<SysVisitorRegistrationVo> list(SysVisitorRegistrationBo bo, PageQuery pageQuery) {
        return visitorRegistrationService.selectPageVisitorList(bo, pageQuery);
    }

    /**
     * 获取访客预约登记详细信息
     *
     * @param visitorId 访客ID
     */
    @SaCheckPermission("system:visitor:query")
    @GetMapping("/{visitorId}")
    public R<SysVisitorRegistrationVo> getInfo(@NotNull(message = "访客ID不能为空") @PathVariable Long visitorId) {
        return R.ok(visitorRegistrationService.selectVisitorById(visitorId));
    }

    /**
     * 新增访客预约登记
     */
    @SaCheckPermission("system:visitor:add")
    @Log(title = "访客预约登记", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysVisitorRegistrationBo bo) {
        visitorRegistrationService.insertVisitor(bo);
        return R.ok();
    }

    /**
     * 修改访客预约登记
     */
    @SaCheckPermission("system:visitor:edit")
    @Log(title = "访客预约登记", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysVisitorRegistrationBo bo) {
        visitorRegistrationService.updateVisitor(bo);
        return R.ok();
    }

    /**
     * 删除访客预约登记
     *
     * @param visitorIds 访客ID数组
     */
    @SaCheckPermission("system:visitor:remove")
    @Log(title = "访客预约登记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{visitorIds}")
    public R<Void> remove(@NotNull(message = "访客ID不能为空") @PathVariable Long[] visitorIds) {
        visitorRegistrationService.deleteVisitorByIds(visitorIds);
        return R.ok();
    }

    /**
     * 访客签到
     *
     * @param visitorId 访客ID
     */
    @SaCheckPermission("system:visitor:checkin")
    @Log(title = "访客预约登记", businessType = BusinessType.UPDATE)
    @PutMapping("/checkin/{visitorId}")
    public R<Void> checkIn(@NotNull(message = "访客ID不能为空") @PathVariable Long visitorId) {
        visitorRegistrationService.checkIn(visitorId);
        return R.ok();
    }

    /**
     * 访客签离
     *
     * @param visitorId 访客ID
     */
    @SaCheckPermission("system:visitor:checkout")
    @Log(title = "访客预约登记", businessType = BusinessType.UPDATE)
    @PutMapping("/checkout/{visitorId}")
    public R<Void> checkOut(@NotNull(message = "访客ID不能为空") @PathVariable Long visitorId) {
        visitorRegistrationService.checkOut(visitorId);
        return R.ok();
    }
}