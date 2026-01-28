-- 访客预约登记表
CREATE TABLE `sys_visitor_registration` (
  `visitor_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访客ID',
  `visitor_name` varchar(50) NOT NULL COMMENT '访客姓名',
  `phone_number` varchar(20) NOT NULL COMMENT '联系电话',
  `visit_purpose` varchar(200) NOT NULL COMMENT '访问事由',
  `dept_id` bigint NOT NULL COMMENT '预约访问部门ID',
  `appointment_time` datetime NOT NULL COMMENT '预约到访时间',
  `actual_arrival_time` datetime DEFAULT NULL COMMENT '实际到访时间',
  `actual_departure_time` datetime DEFAULT NULL COMMENT '实际离开时间',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0预约中 1已签到 2已签离 3已取消）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`visitor_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_appointment_time` (`appointment_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客预约登记表';

-- 添加外键约束
ALTER TABLE `sys_visitor_registration` 
ADD CONSTRAINT `fk_visitor_dept` 
FOREIGN KEY (`dept_id`) REFERENCES `sys_dept`(`dept_id`);