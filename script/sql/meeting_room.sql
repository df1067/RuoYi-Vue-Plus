-- ----------------------------
-- 多功能厅表
-- ----------------------------
create table meeting_room (
    room_id           bigint(20)      not null                   comment '会议室ID',
    tenant_id         varchar(20)     default '000000'           comment '租户编号',
    room_name         varchar(30)     not null                   comment '会议室名称',
    room_type         char(1)         not null                   comment '会议室类型（0小型 1大型）',
    capacity          int(4)          not null                   comment '容纳人数',
    location          varchar(100)    default ''                 comment '会议室位置',
    equipment         varchar(200)    default ''                 comment '配备设备',
    status            char(1)         default '0'                comment '会议室状态（0可用 1停用）',
    del_flag          char(1)         default '0'                comment '删除标志（0代表存在 1代表删除）',
    create_dept       bigint(20)      default null               comment '创建部门',
    create_by         bigint(20)      default null               comment '创建者',
    create_time       datetime                                   comment '创建时间',
    update_by         bigint(20)      default null               comment '更新者',
    update_time       datetime                                   comment '更新时间',
    remark            varchar(500)    default null               comment '备注',
    primary key (room_id)
) engine=innodb comment = '多功能厅表';

-- ----------------------------
-- 多功能厅预约申请表
-- ----------------------------
create table meeting_room_apply (
    apply_id          bigint(20)      not null                   comment '申请ID',
    tenant_id         varchar(20)     default '000000'           comment '租户编号',
    room_id           bigint(20)      not null                   comment '会议室ID',
    apply_user_id     bigint(20)      not null                   comment '申请人ID',
    apply_dept_id     bigint(20)      not null                   comment '申请部门ID',
    apply_time        datetime                                   comment '申请时间',
    meeting_date      date                                     comment '会议日期',
    start_time        time                                     comment '开始时间',
    end_time          time                                     comment '结束时间',
    meeting_topic     varchar(200)    not null                   comment '会议主题',
    participant_count int(4)          not null                   comment '参会人数',
    participant_list  varchar(1000)   default ''                 comment '参会人员列表',
    status            char(1)         default '0'                comment '申请状态（0草稿 1已提交 2部门审核中 3行政复核中 4已批准 5已拒绝 6已取消）',
    dept_approver_id  bigint(20)      default null               comment '部门审核人ID',
    dept_approve_time datetime                                   comment '部门审核时间',
    dept_approve_opinion varchar(500) default ''                 comment '部门审核意见',
    admin_approver_id bigint(20)      default null               comment '行政复核人ID',
    admin_approve_time datetime                                   comment '行政复核时间',
    admin_approve_opinion varchar(500) default ''                 comment '行政复核意见',
    cancel_time       datetime                                   comment '取消时间',
    cancel_reason     varchar(500)    default ''                 comment '取消原因',
    del_flag          char(1)         default '0'                comment '删除标志（0代表存在 1代表删除）',
    create_dept       bigint(20)      default null               comment '创建部门',
    create_by         bigint(20)      default null               comment '创建者',
    create_time       datetime                                   comment '创建时间',
    update_by         bigint(20)      default null               comment '更新者',
    update_time       datetime                                   comment '更新时间',
    remark            varchar(500)    default null               comment '备注',
    primary key (apply_id)
) engine=innodb comment = '多功能厅预约申请表';

-- ----------------------------
-- 初始化-多功能厅表数据
-- ----------------------------
insert into meeting_room values(1, '000000', '一号大型厅', '1', 100, '一楼A区', '投影仪、音响、白板', '0', '0', 103, 1, sysdate(), null, null, '可容纳100人，适合大型会议');
insert into meeting_room values(2, '000000', '二号大型厅', '1', 80, '一楼B区', '投影仪、音响、白板', '0', '0', 103, 1, sysdate(), null, null, '可容纳80人，适合大型会议');
insert into meeting_room values(3, '000000', '一号小型厅', '0', 20, '二楼A区', '投影仪、白板', '0', '0', 103, 1, sysdate(), null, null, '可容纳20人，适合小型会议');
insert into meeting_room values(4, '000000', '二号小型厅', '0', 15, '二楼B区', '投影仪、白板', '0', '0', 103, 1, sysdate(), null, null, '可容纳15人，适合小型会议');