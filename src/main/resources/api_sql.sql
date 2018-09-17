/*
Navicat MySQL Data Transfer

Source Server         : mine
Source Server Version : 50626
Source Host           : 192.168.0.29:3306
Source Database       : api

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2018-09-17 11:21:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bi_bill
-- ----------------------------
DROP TABLE IF EXISTS `bi_bill`;
CREATE TABLE `bi_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_name` int(11) DEFAULT NULL COMMENT '产品名称',
  `shop_type` int(11) DEFAULT NULL COMMENT '产品型号',
  `shop_sport` int(11) DEFAULT NULL COMMENT '加工动作',
  `shop_more` int(11) DEFAULT NULL COMMENT '备注',
  `money_all` double DEFAULT NULL COMMENT '小计',
  `number` int(11) DEFAULT NULL COMMENT '数量',
  `shop_money` double DEFAULT NULL COMMENT '单价',
  `bi_id` int(11) DEFAULT NULL COMMENT '对应bi_order_id',
  PRIMARY KEY (`id`),
  KEY `bi_id` (`bi_id`) USING BTREE,
  CONSTRAINT `bi_bill_ibfk_1` FOREIGN KEY (`bi_id`) REFERENCES `bi_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for bi_order
-- ----------------------------
DROP TABLE IF EXISTS `bi_order`;
CREATE TABLE `bi_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_time` int(11) DEFAULT NULL COMMENT '单据时间',
  `bill_title` varchar(255) DEFAULT NULL COMMENT '单据标题',
  `bill_money` double DEFAULT NULL COMMENT '总金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for dt_datatype
-- ----------------------------
DROP TABLE IF EXISTS `dt_datatype`;
CREATE TABLE `dt_datatype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '类型名称',
  `type` int(11) DEFAULT NULL COMMENT '数据类型:0产品名称,1型号,2加工动作,3备注内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for fo_forder
-- ----------------------------
DROP TABLE IF EXISTS `fo_forder`;
CREATE TABLE `fo_forder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fo_name` varchar(50) DEFAULT NULL COMMENT '文件夹名',
  `fo_create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `pr_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pr_id` (`pr_id`) USING BTREE,
  CONSTRAINT `fo_forder_ibfk_1` FOREIGN KEY (`pr_id`) REFERENCES `pr_project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for hi_histroy
-- ----------------------------
DROP TABLE IF EXISTS `hi_histroy`;
CREATE TABLE `hi_histroy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hi_info` varchar(50) DEFAULT NULL COMMENT '具体操作',
  `hi_time` int(11) DEFAULT NULL COMMENT '此历史时间',
  `in_id` int(11) DEFAULT NULL,
  `pr_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `hi_histroy_ibfk_2` (`pr_id`) USING BTREE,
  KEY `in_id` (`in_id`) USING BTREE,
  CONSTRAINT `hi_histroy_ibfk_1` FOREIGN KEY (`pr_id`) REFERENCES `pr_project` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `hi_histroy_ibfk_2` FOREIGN KEY (`in_id`) REFERENCES `in_interface` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for in_interface
-- ----------------------------
DROP TABLE IF EXISTS `in_interface`;
CREATE TABLE `in_interface` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `in_name` varchar(255) DEFAULT NULL COMMENT '接口名',
  `in_url` varchar(255) DEFAULT NULL COMMENT '接口子路径',
  `in_method` varchar(255) DEFAULT NULL COMMENT '接口请求方式',
  `in_document` longtext COMMENT '接口文档',
  `in_type` int(11) DEFAULT NULL COMMENT '接口类型：0当前，１历史',
  `fo_id` int(11) DEFAULT NULL,
  `in_response_ok` longtext COMMENT '成功时的响应内容',
  `in_response_err` longtext COMMENT '失败时的响应内容',
  PRIMARY KEY (`id`),
  KEY `fo_id` (`fo_id`) USING BTREE,
  CONSTRAINT `in_interface_ibfk_1` FOREIGN KEY (`fo_id`) REFERENCES `fo_forder` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pa_param
-- ----------------------------
DROP TABLE IF EXISTS `pa_param`;
CREATE TABLE `pa_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pa_key` varchar(255) DEFAULT NULL COMMENT '参数名',
  `pa_value` varchar(255) DEFAULT NULL COMMENT '参数值',
  `pa_example` varchar(255) DEFAULT NULL COMMENT '参数示例',
  `in_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `in_id` (`in_id`) USING BTREE,
  CONSTRAINT `pa_param_ibfk_1` FOREIGN KEY (`in_id`) REFERENCES `in_interface` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=655 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pr_project
-- ----------------------------
DROP TABLE IF EXISTS `pr_project`;
CREATE TABLE `pr_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pr_name` varchar(50) DEFAULT NULL COMMENT '工程名',
  `pr_create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `pr_info` varchar(255) DEFAULT NULL COMMENT '工程主路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for re_response
-- ----------------------------
DROP TABLE IF EXISTS `re_response`;
CREATE TABLE `re_response` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `re_content` longtext COMMENT '响应内容',
  `re_type` int(11) DEFAULT NULL COMMENT '响应类型：0，成功，１失败',
  `in_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `in_id` (`in_id`) USING BTREE,
  CONSTRAINT `re_response_ibfk_1` FOREIGN KEY (`in_id`) REFERENCES `in_interface` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for us_user
-- ----------------------------
DROP TABLE IF EXISTS `us_user`;
CREATE TABLE `us_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `us_email` varchar(50) DEFAULT NULL COMMENT '邮箱号',
  `us_pass` varchar(50) DEFAULT NULL COMMENT '密码',
  `us_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `us_icon` varchar(2048) DEFAULT NULL COMMENT '用户头像',
  `us_mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `us_openid` int(11) DEFAULT NULL,
  `us_regest_time` int(11) DEFAULT NULL COMMENT '注册时间',
  `us_last_time` int(11) DEFAULT NULL COMMENT '最后登陆时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_project
-- ----------------------------
DROP TABLE IF EXISTS `user_project`;
CREATE TABLE `user_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `us_id` int(11) DEFAULT NULL,
  `pr_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_project_ibfk_1` (`us_id`) USING BTREE,
  KEY `user_project_ibfk_2` (`pr_id`) USING BTREE,
  CONSTRAINT `user_project_ibfk_1` FOREIGN KEY (`us_id`) REFERENCES `us_user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `user_project_ibfk_2` FOREIGN KEY (`pr_id`) REFERENCES `pr_project` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb4;
DROP TRIGGER IF EXISTS `trigger_test`;
DELIMITER ;;
CREATE TRIGGER `trigger_test` AFTER INSERT ON `us_user` FOR EACH ROW INSERT INTO hi_histroy (hi_info) VALUES ('test_trigger')
;;
DELIMITER ;
