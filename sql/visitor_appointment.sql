-- 访客预约表
CREATE TABLE `visitor_appointment` (
  `appointment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `visitor_name` varchar(50) NOT NULL COMMENT '访客姓名',
  `visitor_phone` varchar(20) NOT NULL COMMENT '访客手机号',
  `visitor_id_card` varchar(18) DEFAULT NULL COMMENT '访客身份证号',
  `visitor_company` varchar(100) DEFAULT NULL COMMENT '访客单位/公司',
  `visit_purpose` varchar(500) NOT NULL COMMENT '访问事由',
  `dept_id` bigint NOT NULL COMMENT '预约部门ID',
  `contact_user_id` bigint NOT NULL COMMENT '对接人用户ID',
  `appointment_start_time` datetime NOT NULL COMMENT '预约开始时间',
  `appointment_end_time` datetime NOT NULL COMMENT '预约结束时间',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '预约状态（0待确认 1已确认 2已拒绝 3已取消）',
  `create_by` bigint DEFAULT NULL COMMENT '预约人用户ID',
  `confirm_by` bigint DEFAULT NULL COMMENT '确认人用户ID',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `reject_reason` varchar(500) DEFAULT NULL COMMENT '拒绝原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新者',
  `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`appointment_id`),
  KEY `idx_visitor_phone` (`visitor_phone`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_contact_user_id` (`contact_user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_appointment_time` (`appointment_start_time`,`appointment_end_time`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客预约表';

-- 添加菜单和权限
INSERT INTO `sys_menu` VALUES 
('2000', '访客预约', '2000', '1', 'visitorAppointment', 'system/visitorAppointment/index', '', '1', '0', 'C', '0', '0', 'build', 'admin', '2024-01-31 10:00:00', '', NULL, ''),
('2001', '访客预约查询', '2000', '1', '', '', '', '1', '0', 'F', '0', '0', '', 'admin', '2024-01-31 10:00:00', '', NULL, 'system:visitorAppointment:query'),
('2002', '访客预约新增', '2000', '2', '', '', '', '1', '0', 'F', '0', '0', '', 'admin', '2024-01-31 10:00:00', '', NULL, 'system:visitorAppointment:add'),
('2003', '访客预约修改', '2000', '3', '', '', '', '1', '0', 'F', '0', '0', '', 'admin', '2024-01-31 10:00:00', '', NULL, 'system:visitorAppointment:edit'),
('2004', '访客预约删除', '2000', '4', '', '', '', '1', '0', 'F', '0', '0', '', 'admin', '2024-01-31 10:00:00', '', NULL, 'system:visitorAppointment:remove'),
('2005', '访客预约导出', '2000', '5', '', '', '', '1', '0', 'F', '0', '0', '', 'admin', '2024-01-31 10:00:00', '', NULL, 'system:visitorAppointment:export'),
('2006', '访客预约确认', '2000', '6', '', '', '', '1', '0', 'F', '0', '0', '', 'admin', '2024-01-31 10:00:00', '', NULL, 'system:visitorAppointment:confirm'),
('2007', '访客预约拒绝', '2000', '7', '', '', '', '1', '0', 'F', '0', '0', '', 'admin', '2024-01-31 10:00:00', '', NULL, 'system:visitorAppointment:reject'),
('2008', '访客预约取消', '2000', '8', '', '', '', '1', '0', 'F', '0', '0', '', 'admin', '2024-01-31 10:00:00', '', NULL, 'system:visitorAppointment:cancel');

-- 给管理员角色添加权限
INSERT INTO `sys_role_menu` SELECT '1', '2000', 'admin', '2024-01-31 10:00:00' UNION ALL
SELECT '1', '2001', 'admin', '2024-01-31 10:00:00' UNION ALL
SELECT '1', '2002', 'admin', '2024-01-31 10:00:00' UNION ALL
SELECT '1', '2003', 'admin', '2024-01-31 10:00:00' UNION ALL
SELECT '1', '2004', 'admin', '2024-01-31 10:00:00' UNION ALL
SELECT '1', '2005', 'admin', '2024-01-30 10:00:00' UNION ALL
SELECT '1', '2006', 'admin', '2024-01-31 10:00:00' UNION ALL
SELECT '1', '2007', 'admin', '2024-01-31 10:00:00' UNION ALL
SELECT '1', '2008', 'admin', '2024-01-31 10:00:00';