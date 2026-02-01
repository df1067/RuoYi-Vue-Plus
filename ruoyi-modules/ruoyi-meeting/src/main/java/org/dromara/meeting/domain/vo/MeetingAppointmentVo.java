package org.dromara.meeting.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 预约记录视图对象 meeting_appointment
 *
 * @author Lion Li
 */
@Data
public class MeetingAppointmentVo {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    private Long appointmentId;

    /**
     * 预约单号
     */
    private String appointmentNo;

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 会议室名称
     */
    private String roomName;

    /**
     * 会议室类型
     */
    private String roomType;

    /**
     * 预约用户ID
     */
    private Long userId;

    /**
     * 预约用户姓名
     */
    private String userName;

    /**
     * 用户部门ID
     */
    private Long deptId;

    /**
     * 用户部门名称
     */
    private String deptName;

    /**
     * 会议主题
     */
    private String meetingTitle;

    /**
     * 会议描述
     */
    private String meetingDesc;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 参会人数
     */
    private Integer attendees;

    /**
     * 状态（01草稿 02已提交 03部门审核中 04行政复核中 05已批准 06已拒绝 07已取消）
     */
    private String status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否可以取消（大型厅24小时内不可取消）
     */
    private Boolean canCancel;
}