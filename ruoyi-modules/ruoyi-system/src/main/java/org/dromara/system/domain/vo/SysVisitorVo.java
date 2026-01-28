package org.dromara.system.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;

import java.util.Date;

/**
 * 访客登记视图对象 sys_visitor
 *
 * @author Lion Li
 */
@Data
public class SysVisitorVo {

    private static final long serialVersionUID = 1L;

    /**
     * 访客ID
     */
    @ExcelProperty(value = "访客ID")
    private Long visitorId;

    /**
     * 访客姓名
     */
    @ExcelProperty(value = "访客姓名")
    private String visitorName;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话")
    private String phone;

    /**
     * 访问事由
     */
    @ExcelProperty(value = "访问事由")
    private String reason;

    /**
     * 预约部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    @ExcelProperty(value = "部门名称")
    private String deptName;

    /**
     * 预约到访时间
     */
    @ExcelProperty(value = "预约到访时间")
    private Date visitTime;

    /**
     * 实际到访时间
     */
    @ExcelProperty(value = "实际到访时间")
    private Date actualVisitTime;

    /**
     * 实际离开时间
     */
    @ExcelProperty(value = "实际离开时间")
    private Date actualLeaveTime;

    /**
     * 状态（0：预约中 1：已到访 2：已离开）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_visitor_status")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;
}