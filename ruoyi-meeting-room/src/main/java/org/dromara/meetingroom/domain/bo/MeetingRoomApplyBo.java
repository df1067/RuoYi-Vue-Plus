package org.dromara.meetingroom.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.meetingroom.domain.MeetingRoomApply;

import java.util.Date;

/**
 * 多功能厅预约申请业务对象 meeting_room_apply
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MeetingRoomApply.class, reverseConvertGenerate = false)
public class MeetingRoomApplyBo extends BaseEntity {

    /**
     * 申请ID
     */
    private Long applyId;

    /**
     * 会议室ID
     */
    @NotNull(message = "会议室ID不能为空")
    private Long roomId;

    /**
     * 申请人ID
     */
    private Long applyUserId;

    /**
     * 申请部门ID
     */
    private Long applyDeptId;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 会议日期
     */
    @NotNull(message = "会议日期不能为空")
    private Date meetingDate;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 会议主题
     */
    @NotBlank(message = "会议主题不能为空")
    private String meetingTopic;

    /**
     * 参会人数
     */
    @NotNull(message = "参会人数不能为空")
    private Integer participantCount;

    /**
     * 参会人员列表
     */
    private String participantList;

    /**
     * 申请状态（0草稿 1已提交 2部门审核中 3行政复核中 4已批准 5已拒绝 6已取消）
     */
    private String status;

    /**
     * 部门审核人ID
     */
    private Long deptApproverId;

    /**
     * 部门审核时间
     */
    private Date deptApproveTime;

    /**
     * 部门审核意见
     */
    private String deptApproveOpinion;

    /**
     * 行政复核人ID
     */
    private Long adminApproverId;

    /**
     * 行政复核时间
     */
    private Date adminApproveTime;

    /**
     * 行政复核意见
     */
    private String adminApproveOpinion;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 备注
     */
    private String remark;

    public MeetingRoomApplyBo(Long applyId) {
        this.applyId = applyId;
    }
}