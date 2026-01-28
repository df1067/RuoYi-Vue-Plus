package org.dromara.system.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysVisitor;
import org.dromara.system.domain.bo.SysVisitorBo;
import org.dromara.system.domain.vo.SysVisitorVo;

import java.util.List;

/**
 * 访客登记Service接口
 *
 * @author Lion Li
 */
public interface ISysVisitorService {

    /**
     * 分页查询访客登记列表
     *
     * @param visitorBo   访客登记信息
     * @param pageQuery 分页信息
     * @return 访客登记列表
     */
    TableDataInfo<SysVisitorVo> selectPageVisitorList(SysVisitorBo visitorBo, PageQuery pageQuery);

    /**
     * 查询访客登记列表
     *
     * @param visitorBo 访客登记信息
     * @return 访客登记列表
     */
    List<SysVisitorVo> selectVisitorList(SysVisitorBo visitorBo);

    /**
     * 查询访客登记信息
     *
     * @param visitorId 访客登记ID
     * @return 访客登记信息
     */
    SysVisitorVo selectVisitorById(Long visitorId);

    /**
     * 新增访客登记
     *
     * @param visitorBo 访客登记信息
     * @return 结果
     */
    int insertVisitor(SysVisitorBo visitorBo);

    /**
     * 修改访客登记
     *
     * @param visitorBo 访客登记信息
     * @return 结果
     */
    int updateVisitor(SysVisitorBo visitorBo);

    /**
     * 访客签到
     *
     * @param visitorId 访客登记ID
     * @return 结果
     */
    int signIn(Long visitorId);

    /**
     * 访客签离
     *
     * @param visitorId 访客登记ID
     * @return 结果
     */
    int signOut(Long visitorId);

    /**
     * 批量删除访客登记
     *
     * @param visitorIds 需要删除的访客登记ID
     * @return 结果
     */
    int deleteVisitorByIds(Long[] visitorIds);

    /**
     * 删除访客登记信息
     *
     * @param visitorId 访客登记ID
     * @return 结果
     */
    int deleteVisitorById(Long visitorId);
}