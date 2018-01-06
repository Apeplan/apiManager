/*
Navicat MySQL Data Transfer

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2018-01-06 13:07:17
*/

SET FOREIGN_KEY_CHECKS=0;

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
  KEY `pr_id` (`pr_id`),
  CONSTRAINT `fo_forder_ibfk_1` FOREIGN KEY (`pr_id`) REFERENCES `pr_project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of fo_forder
-- ----------------------------
INSERT INTO `fo_forder` VALUES ('44', '小明明', '1515132102', '92');

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
  KEY `hi_histroy_ibfk_2` (`pr_id`),
  KEY `in_id` (`in_id`),
  CONSTRAINT `hi_histroy_ibfk_2` FOREIGN KEY (`pr_id`) REFERENCES `pr_project` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `hi_histroy_ibfk_3` FOREIGN KEY (`in_id`) REFERENCES `in_interface` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of hi_histroy
-- ----------------------------

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
  KEY `fo_id` (`fo_id`),
  CONSTRAINT `in_interface_ibfk_1` FOREIGN KEY (`fo_id`) REFERENCES `fo_forder` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of in_interface
-- ----------------------------
INSERT INTO `in_interface` VALUES ('68', '小接口', null, 'POST', null, null, '44', null, null);

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
  KEY `in_id` (`in_id`),
  CONSTRAINT `pa_param_ibfk_1` FOREIGN KEY (`in_id`) REFERENCES `in_interface` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=285 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of pa_param
-- ----------------------------

-- ----------------------------
-- Table structure for pr_project
-- ----------------------------
DROP TABLE IF EXISTS `pr_project`;
CREATE TABLE `pr_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pr_name` varchar(255) DEFAULT NULL COMMENT '工程名',
  `pr_create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `pr_root_url` varchar(50) DEFAULT NULL COMMENT '工程主路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of pr_project
-- ----------------------------
INSERT INTO `pr_project` VALUES ('92', '测试工程', '1515132088', null);

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
  KEY `in_id` (`in_id`),
  CONSTRAINT `re_response_ibfk_1` FOREIGN KEY (`in_id`) REFERENCES `in_interface` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=432 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of re_response
-- ----------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of us_user
-- ----------------------------
INSERT INTO `us_user` VALUES ('27', null, '7945bd83237335e5376ff44d62e4f0ae', '13800138000', null, '13800138000', null, '1515132050', '1515214958');

-- ----------------------------
-- Table structure for user_project
-- ----------------------------
DROP TABLE IF EXISTS `user_project`;
CREATE TABLE `user_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `us_id` int(11) DEFAULT NULL,
  `pr_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_project_ibfk_1` (`us_id`),
  KEY `user_project_ibfk_2` (`pr_id`),
  CONSTRAINT `user_project_ibfk_1` FOREIGN KEY (`us_id`) REFERENCES `us_user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `user_project_ibfk_2` FOREIGN KEY (`pr_id`) REFERENCES `pr_project` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_project
-- ----------------------------
INSERT INTO `user_project` VALUES ('95', '27', '92');
