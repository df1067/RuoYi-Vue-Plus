package org.dromara.system.mapper;

import org.dromara.system.domain.VisitorAppointment;
import org.dromara.system.domain.bo.VisitorAppointmentBo;
import org.dromara.system.domain.vo.VisitorAppointmentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 访客预约登记Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-31
 */
public interface VisitorAppointmentMapper extends BaseMapper<VisitorAppointment> {

    /**
     * 查询访客预约登记列表
     * 
     * @param visitorAppointmentBo 访客预约登记业务对象
     * @return 访客预约登记集合
     */
    List<VisitorAppointmentVo> selectVisitorAppointmentList(@Param("bo") VisitorAppointmentBo visitorAppointmentBo);

    /**
     * 查询访客预约登记分页列表
     * 
     * @param page 分页对象
     * @param visitorAppointmentBo 访客预约登记业务对象
     * @return 访客预约登记分页集合
     */
    IPage<VisitorAppointmentVo> selectVisitorAppointmentPage(Page<VisitorAppointmentVo> page, @Param("bo") VisitorAppointmentBo visitorAppointmentBo);

    /**
     * 查询访客是否存在时间冲突的预约
     * 
     * @param visitorPhone 访客手机号
     * @param startTime 预约开始时间
     * @param endTime 预约结束时间
     * @param excludeAppointmentId 排除的预约ID（用于更新时）
     * @return 冲突数量
     */
    int countConflictByVisitorPhone(@Param("visitorPhone") String visitorPhone, @Param("startTime") java.util.Date startTime, @Param("endTime") java.util.Date endTime, @Param("excludeAppointmentId") Long excludeAppointmentId);

    /**
     * 查询对接人是否存在时间冲突的预约
     * 
     * @param contactUserId 对接人ID
     * @param startTime 预约开始时间
     * @param endTime 预约结束时间
     * @param excludeAppointmentId 排除的预约ID（用于更新时）
     * @return 冲突数量
     */
    int countConflictByContactUserId(@Param("contactUserId") Long contactUserId, @Param("startTime") java.util.Date startTime, @Param("endTime") java.util.Date endTime, @Param("excludeAppointmentId") Long excludeAppointmentId);
}