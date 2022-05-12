CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` int NOT NULL DEFAULT '1' COMMENT '账号状态,1为使用中,0为禁用中',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_employee_id` bigint NOT NULL COMMENT '创建人id',
  `update_employee_id` bigint NOT NULL COMMENT '更新人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1516268531346337794 DEFAULT CHARSET=utf8mb3 COMMENT='商家员工信息表';