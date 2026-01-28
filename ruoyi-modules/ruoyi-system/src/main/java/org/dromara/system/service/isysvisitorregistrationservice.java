package org.dromara.system.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.bo.SysVisitorRegistrationBo;
import org.dromara.system.domain.vo.SysVisitorRegistrationVo;

import java.util.List;

/**
 * 访客预约登记Service接口
 *
 * @author System
 */
public interface ISysVisitorRegistrationService {

    /**
     * 查询访客预约登记分页列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 访客预约登记分页列表
     */
    TableDataInfo<SysVisitorRegistrationVo> selectPageVisitorList(SysVisitorRegistrationBo bo, PageQuery pageQuery);

    /**
     * 查询访客预约登记列表
     *
     * @param bo 查询条件
     * @return 访客预约登记列表
     */
    List<SysVisitorRegistrationVo> selectVisitorList(SysVisitorRegistrationBo bo);

    /**
     * 根据ID查询访客预约登记
     *
     * @param visitorId 访客ID
     * @return 访客预约登记信息
     */
    SysVisitorRegistrationVo selectVisitorById(Long visitorId);

    /**
     * 新增访客预约登记
     *
     * @param bo 访客预约登记信息
     * @return 结果
     */
    int insertVisitor(SysVisitorRegistrationBo bo);

    /**
     * 修改访客预约登记
     *
     * @param bo 访客预约登记信息
     * @return 结果
     */
    int updateVisitor(SysVisitorRegistrationBo bo);

    /**
     * 删除访客预约登记
     *
     * @param visitorIds 访客ID数组
     * @return 结果
     */
    int deleteVisitorByIds(Long[] visitorIds);

    /**
     * 访客签到
     *
     * @param visitorId 访客ID
     * @return 结果
     */
    int checkIn(Long visitorId);

    /**
     * 访客签离
     *
     * @param visitorId 访客ID
     * @return 结果
     */
    int checkOut(Long visitorId);
}