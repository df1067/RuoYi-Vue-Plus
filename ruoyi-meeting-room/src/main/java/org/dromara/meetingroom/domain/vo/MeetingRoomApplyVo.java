package org.dromara.meetingroom.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelIgnoreUnannotated;
import org.dromara.common.excel.annotation.ExcelProperty;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.common.excel.format.ExcelDictFormat;
import org.dromara.common.core.annotation.Translation;
import org.dromara.common.core.constant.TransConstant;
import org.dromara.meetingroom.domain.MeetingRoomApply;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 多功能厅预约申请视图对象 meeting_room_apply
 *
 * @author Lion Li
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MeetingRoomApply.class)
public class MeetingRoomApplyVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 申请ID
     */
    @ExcelProperty(value = "申请ID")
    private Long applyId;

    /**
     * 会议室ID
     */
    @ExcelProperty(value = "会议室ID")
    private Long roomId;

    /**
     * 申请人ID
     */
    @ExcelProperty(value = "申请人ID")
    private Long applyUserId;

    /**
     * 申请人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "applyUserId")
    private String applyUserName;

    /**
     * 申请部门ID
     */
    @ExcelProperty(value = "申请部门ID")
    private Long applyDeptId;

    /**
     * 申请部门名称
     */
    @Translation(type = TransConstant.DEPT_ID_TO_NAME, mapper = "applyDeptId")
    private String applyDeptName;

    /**
     * 申请时间
     */
    @ExcelProperty(value = "申请时间")
    private Date applyTime;

    /**
     * 会议日期
     */
    @ExcelProperty(value = "会议日期")
    private Date meetingDate;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 会议主题
     */
    @ExcelProperty(value = "会议主题")
    private String meetingTopic;

    /**
     * 参会人数
     */
    @ExcelProperty(value = "参会人数")
    private Integer participantCount;

    /**
     * 参会人员列表
     */
    @ExcelProperty(value = "参会人员列表")
    private String participantList;

    /**
     * 申请状态（0草稿 1已提交 2部门审核中 3行政复核中 4已批准 5已拒绝 6已取消）
     */
    @ExcelProperty(value = "申请状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=草稿,1=已提交,2=部门审核中,3=行政复核中,4=已批准,5=已拒绝,6=已取消")
    private String status;

    /**
     * 部门审核人ID
     */
    @ExcelProperty(value = "部门审核人ID")
    private Long deptApproverId;

    /**
     * 部门审核人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "deptApproverId")
    private String deptApproverName;

    /**
     * 部门审核时间
     */
    @ExcelProperty(value = "部门审核时间")
    private Date deptApproveTime;

    /**
     * 部门审核意见
     */
    @ExcelProperty(value = "部门审核意见")
    private String deptApproveOpinion;

    /**
     * 行政复核人ID
     */
    @ExcelProperty(value = "行政复核人ID")
    private Long adminApproverId;

    /**
     * 行政复核人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "adminApproverId")
    private String adminApproverName;

    /**
     * 行政复核时间
     */
    @ExcelProperty(value = "行政复核时间")
    private Date adminApproveTime;

    /**
     * 行政复核意见
     */
    @ExcelProperty(value = "行政复核意见")
    private String adminApproveOpinion;

    /**
     * 取消时间
     */
    @ExcelProperty(value = "取消时间")
    private Date cancelTime;

    /**
     * 取消原因
     */
    @ExcelProperty(value = "取消原因")
    private String cancelReason;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}