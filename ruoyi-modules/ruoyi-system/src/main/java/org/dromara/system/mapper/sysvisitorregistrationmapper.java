package org.dromara.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysVisitorRegistration;
import org.dromara.system.domain.vo.SysVisitorRegistrationVo;
import org.dromara.system.domain.bo.SysVisitorRegistrationBo;

/**
 * 访客预约登记Mapper接口
 *
 * @author System
 */
public interface SysVisitorRegistrationMapper extends BaseMapperPlus<SysVisitorRegistration, SysVisitorRegistrationVo> {

    /**
     * 分页查询访客预约登记列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询条件
     * @return 分页的访客预约登记信息
     */
    default Page<SysVisitorRegistrationVo> selectPageVisitorList(Page<SysVisitorRegistration> page, Wrapper<SysVisitorRegistration> queryWrapper) {
        return this.selectVoPage(page, queryWrapper);
    }
}