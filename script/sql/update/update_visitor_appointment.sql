-- ----------------------------
-- 新增访客预约登记菜单
-- ----------------------------
insert into sys_menu values('100000', '访客预约登记', '1', '10', 'visitor-appointment', 'system/visitor-appointment/index', '', 1, 0, 'C', '0', '0', 'system:visitor-appointment:list', 'user', 103, 1, sysdate(), null, null, '访客预约登记菜单');

-- ----------------------------
-- 新增访客预约登记按钮权限
-- ----------------------------
insert into sys_menu values('100001', '新增', '100000', '1', 'add', '', '', 1, 0, 'F', '0', '0', 'system:visitor-appointment:add', '', 103, 1, sysdate(), null, null, '新增访客预约');
insert into sys_menu values('100002', '修改', '100000', '2', 'edit', '', '', 1, 0, 'F', '0', '0', 'system:visitor-appointment:edit', '', 103, 1, sysdate(), null, null, '修改访客预约');
insert into sys_menu values('100003', '删除', '100000', '3', 'remove', '', '', 1, 0, 'F', '0', '0', 'system:visitor-appointment:remove', '', 103, 1, sysdate(), null, null, '删除访客预约');
insert into sys_menu values('100004', '确认', '100000', '4', 'confirm', '', '', 1, 0, 'F', '0', '0', 'system:visitor-appointment:confirm', '', 103, 1, sysdate(), null, null, '确认访客预约');
insert into sys_menu values('100005', '拒绝', '100000', '5', 'reject', '', '', 1, 0, 'F', '0', '0', 'system:visitor-appointment:reject', '', 103, 1, sysdate(), null, null, '拒绝访客预约');
insert into sys_menu values('100006', '导出', '100000', '6', 'export', '', '', 1, 0, 'F', '0', '0', 'system:visitor-appointment:export', '', 103, 1, sysdate(), null, null, '导出访客预约');

-- ----------------------------
-- 为管理员角色分配访客预约登记权限
-- ----------------------------
insert into sys_role_menu values('1', '100000');
insert into sys_role_menu values('1', '100001');
insert into sys_role_menu values('1', '100002');
insert into sys_role_menu values('1', '100003');
insert into sys_role_menu values('1', '100004');
insert into sys_role_menu values('1', '100005');
insert into sys_role_menu values('1', '100006');

-- ----------------------------
-- 创建访客预约登记表
-- ----------------------------
CREATE TABLE `sys_visitor_appointment` (
  `appointment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `visitor_name` varchar(50) NOT NULL COMMENT '访客姓名',
  `visitor_phone` varchar(20) NOT NULL COMMENT '访客手机号',
  `visitor_id_card` varchar(18) DEFAULT NULL COMMENT '访客身份证号',
  `visit_reason` varchar(200) NOT NULL COMMENT '访问事由',
  `dept_id` bigint NOT NULL COMMENT '预约部门ID',
  `dept_name` varchar(50) NOT NULL COMMENT '预约部门名称',
  `contact_user_id` bigint NOT NULL COMMENT '对接人ID',
  `contact_user_name` varchar(50) NOT NULL COMMENT '对接人姓名',
  `start_time` datetime NOT NULL COMMENT '预约开始时间',
  `end_time` datetime NOT NULL COMMENT '预约结束时间',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '预约状态（0：待确认，1：已确认，2：已拒绝，3：已完成，4：已取消）',
  `reject_reason` varchar(200) DEFAULT NULL COMMENT '拒绝原因',
  `create_by` varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`appointment_id`),
  KEY `idx_visitor_phone` (`visitor_phone`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_contact_user_id` (`contact_user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='访客预约登记表';