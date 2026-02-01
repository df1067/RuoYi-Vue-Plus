-- 多功能厅预约模块数据库设计
-- 基于RuoYi-Vue-Plus架构

-- 会议室表
CREATE TABLE `meeting_room` (
  `room_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会议室ID',
  `room_name` varchar(100) NOT NULL COMMENT '会议室名称',
  `room_type` char(1) NOT NULL COMMENT '会议室类型（L大型 S小型）',
  `capacity` int(11) NOT NULL COMMENT '容纳人数',
  `location` varchar(200) DEFAULT NULL COMMENT '位置描述',
  `equipment` varchar(500) DEFAULT NULL COMMENT '设备配置',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室表';

-- 预约记录表
CREATE TABLE `meeting_appointment` (
  `appointment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `appointment_no` varchar(64) NOT NULL COMMENT '预约单号',
  `room_id` bigint(20) NOT NULL COMMENT '会议室ID',
  `user_id` bigint(20) NOT NULL COMMENT '预约用户ID',
  `user_name` varchar(64) NOT NULL COMMENT '预约用户姓名',
  `dept_id` bigint(20) NOT NULL COMMENT '用户部门ID',
  `dept_name` varchar(100) NOT NULL COMMENT '用户部门名称',
  `meeting_title` varchar(200) NOT NULL COMMENT '会议主题',
  `meeting_desc` text COMMENT '会议描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `attendees` int(11) NOT NULL COMMENT '参会人数',
  `status` char(2) NOT NULL DEFAULT '01' COMMENT '状态（01草稿 02已提交 03部门审核中 04行政复核中 05已批准 06已拒绝 07已取消）',
  `cancel_reason` varchar(500) DEFAULT NULL COMMENT '取消原因',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`appointment_id`),
  KEY `idx_room_time` (`room_id`,`start_time`,`end_time`),
  KEY `idx_user_status` (`user_id`,`status`),
  KEY `idx_status_time` (`status`,`start_time`),
  KEY `idx_appointment_no` (`appointment_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约记录表';

-- 审批流程记录表
CREATE TABLE `meeting_approval` (
  `approval_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '审批ID',
  `appointment_id` bigint(20) NOT NULL COMMENT '预约ID',
  `appointment_no` varchar(64) NOT NULL COMMENT '预约单号',
  `approval_level` tinyint(4) NOT NULL COMMENT '审批级别（1部门审批 2行政审批）',
  `approver_id` bigint(20) NOT NULL COMMENT '审批人ID',
  `approver_name` varchar(64) NOT NULL COMMENT '审批人姓名',
  `approver_dept_id` bigint(20) NOT NULL COMMENT '审批人部门ID',
  `approver_role` varchar(100) DEFAULT NULL COMMENT '审批人角色',
  `approval_status` char(2) NOT NULL COMMENT '审批状态（01待审批 02已通过 03已拒绝）',
  `approval_opinion` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`approval_id`),
  KEY `idx_appointment_level` (`appointment_id`,`approval_level`),
  KEY `idx_approver` (`approver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批流程记录表';

-- 插入测试数据
INSERT INTO `meeting_room` VALUES 
(1, '第一会议室', 'L', 80, 'A栋3楼', '投影仪、音响、白板', '0', '大型会议室', '0', 'admin', NOW(), 'admin', NOW()),
(2, '第二会议室', 'L', 60, 'A栋4楼', '投影仪、音响', '0', '大型会议室', '0', 'admin', NOW(), 'admin', NOW()),
(3, '小型会议室A', 'S', 20, 'B栋1楼', '投影仪', '0', '小型会议室', '0', 'admin', NOW(), 'admin', NOW()),
(4, '小型会议室B', 'S', 15, 'B栋2楼', '白板', '0', '小型会议室', '0', 'admin', NOW(), 'admin', NOW());