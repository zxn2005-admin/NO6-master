-- 数据库更新脚本
-- 用于添加通知功能所需的表和字段

USE `cl515882190`;

-- 为jiuzhentongzhi表添加新字段
ALTER TABLE `jiuzhentongzhi` 
ADD COLUMN `status` VARCHAR(50) DEFAULT '待发送' COMMENT '发送状态' AFTER `tongzhibeizhu`,
ADD COLUMN `notice_type` VARCHAR(100) DEFAULT NULL COMMENT '通知类型' AFTER `status`;

-- 创建通知发送记录表
DROP TABLE IF EXISTS `tongzhi_record`;
CREATE TABLE `tongzhi_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `jiuzhentongzhi_id` bigint(20) DEFAULT NULL COMMENT '就诊通知ID',
  `tongzhibianhao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知编号',
  `zhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `shouji` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '待发送' COMMENT '状态',
  `retry_count` int(11) DEFAULT 0 COMMENT '重试次数',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知发送记录表';
