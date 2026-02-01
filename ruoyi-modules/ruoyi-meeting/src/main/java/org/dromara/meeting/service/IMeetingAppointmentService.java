package org.dromara.meeting.service;

import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.meeting.domain.bo.MeetingAppointmentBo;
import org.dromara.meeting.domain.vo.MeetingAppointmentVo;
import org.dromara.meeting.domain.vo.MeetingStatisticsVo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 预约记录Service接口
 *
 * @author Lion Li
 */
public interface IMeetingAppointmentService {

    /**
     * 查询预约记录
     */
    MeetingAppointmentVo queryById(Long appointmentId);

    /**
     * 查询预约记录列表
     */
    TableDataInfo<MeetingAppointmentVo> queryPageList(MeetingAppointmentBo bo, PageQuery pageQuery);

    /**
     * 查询预约记录列表
     */
    List<MeetingAppointmentVo> queryList(MeetingAppointmentBo bo);

    /**
     * 新增预约记录
     */
    Boolean insertByBo(MeetingAppointmentBo bo);

    /**
     * 修改预约记录
     */
    Boolean updateByBo(MeetingAppointmentBo bo);

    /**
     * 提交预约申请
     */
    Boolean submitAppointment(Long appointmentId);

    /**
     * 取消预约
     */
    Boolean cancelAppointment(Long appointmentId, String cancelReason);

    /**
     * 校验并批量删除预约记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取预约统计信息
     */
    List<MeetingStatisticsVo> getStatistics();
}