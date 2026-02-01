package org.dromara.meetingroom.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 多功能厅预约申请表 meeting_room_apply
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("meeting_room_apply")
public class MeetingRoomApply extends TenantEntity {

    /**
     * 申请ID
     */
    @TableId(value = "apply_id")
    private Long applyId;

    /**
     * 会议室ID
     */
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
    private Date meetingDate;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 会议主题
     */
    private String meetingTopic;

    /**
     * 参会人数
     */
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
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    public MeetingRoomApply(Long applyId) {
        this.applyId = applyId;
    }
}