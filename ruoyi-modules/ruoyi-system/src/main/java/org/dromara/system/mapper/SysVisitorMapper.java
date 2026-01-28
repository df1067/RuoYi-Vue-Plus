package org.dromara.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysVisitor;
import org.dromara.system.domain.vo.SysVisitorVo;

/**
 * 访客登记Mapper接口
 *
 * @author Lion Li
 */
public interface SysVisitorMapper extends BaseMapperPlus<SysVisitor, SysVisitorVo> {

    /**
     * 分页查询访客登记列表
     *
     * @param page         分页信息
     * @param queryWrapper 查询条件
     * @return 访客登记列表
     */
    IPage<SysVisitorVo> selectVisitorPage(Page<SysVisitor> page, @Param(Constants.WRAPPER) Wrapper<SysVisitor> queryWrapper);
}