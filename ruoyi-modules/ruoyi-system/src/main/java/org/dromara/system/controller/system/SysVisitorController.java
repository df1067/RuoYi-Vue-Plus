package org.dromara.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.bo.SysVisitorBo;
import org.dromara.system.domain.vo.SysVisitorVo;
import org.dromara.system.service.ISysVisitorService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 访客登记Controller
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/visitor")
public class SysVisitorController extends BaseController {

    private final ISysVisitorService visitorService;

    /**
     * 获取访客登记列表
     */
    @SaCheckPermission("system:visitor:list")
    @GetMapping("/list")
    public TableDataInfo<SysVisitorVo> list(SysVisitorBo visitorBo, PageQuery pageQuery) {
        return visitorService.selectPageVisitorList(visitorBo, pageQuery);
    }

    /**
     * 查询访客登记信息
     */
    @SaCheckPermission("system:visitor:query")
    @GetMapping("/{visitorId}")
    public R<SysVisitorVo> getInfo(@PathVariable Long visitorId) {
        return R.ok(visitorService.selectVisitorById(visitorId));
    }

    /**
     * 新增访客登记
     */
    @SaCheckPermission("system:visitor:add")
    @Log(title = "访客登记", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysVisitorBo visitorBo) {
        return toAjax(visitorService.insertVisitor(visitorBo));
    }

    /**
     * 修改访客登记
     */
    @SaCheckPermission("system:visitor:edit")
    @Log(title = "访客登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysVisitorBo visitorBo) {
        return toAjax(visitorService.updateVisitor(visitorBo));
    }

    /**
     * 访客签到
     */
    @SaCheckPermission("system:visitor:signIn")
    @Log(title = "访客登记", businessType = BusinessType.UPDATE)
    @PutMapping("/signIn/{visitorId}")
    public R<Void> signIn(@PathVariable Long visitorId) {
        return toAjax(visitorService.signIn(visitorId));
    }

    /**
     * 访客签离
     */
    @SaCheckPermission("system:visitor:signOut")
    @Log(title = "访客登记", businessType = BusinessType.UPDATE)
    @PutMapping("/signOut/{visitorId}")
    public R<Void> signOut(@PathVariable Long visitorId) {
        return toAjax(visitorService.signOut(visitorId));
    }

    /**
     * 删除访客登记
     */
    @SaCheckPermission("system:visitor:remove")
    @Log(title = "访客登记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{visitorIds}")
    public R<Void> remove(@PathVariable Long[] visitorIds) {
        return toAjax(visitorService.deleteVisitorByIds(visitorIds));
    }
}