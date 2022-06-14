/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost:3306
 Source Schema         : quick-work

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 14/06/2022 10:54:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for code_column_info
-- ----------------------------
DROP TABLE IF EXISTS `code_column_info`;
CREATE TABLE `code_column_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
  `column_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名',
  `column_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `dict_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `extra` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附加',
  `form_show` bit(1) NULL DEFAULT NULL COMMENT '表单显示',
  `form_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单类型',
  `key_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主键类型',
  `list_show` bit(1) NULL DEFAULT NULL COMMENT '列表显示',
  `not_null` bit(1) NULL DEFAULT NULL COMMENT '非空',
  `query_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `date_annotation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日期注解',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段信息存储表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of code_column_info
-- ----------------------------
INSERT INTO `code_column_info` VALUES (1, 'mnt_database', 'id', 'varchar', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, '主键', NULL);
INSERT INTO `code_column_info` VALUES (2, 'mnt_database', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', 'Like', '名称', NULL);
INSERT INTO `code_column_info` VALUES (3, 'mnt_database', 'jdbc_url', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, 'jdbc地址', NULL);
INSERT INTO `code_column_info` VALUES (4, 'mnt_database', 'username', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '账号', NULL);
INSERT INTO `code_column_info` VALUES (5, 'mnt_database', 'password', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '密码', NULL);
INSERT INTO `code_column_info` VALUES (6, 'mnt_database', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建者', NULL);
INSERT INTO `code_column_info` VALUES (7, 'mnt_database', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改者', NULL);
INSERT INTO `code_column_info` VALUES (8, 'mnt_database', 'create_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', 'BetWeen', '创建时间', NULL);
INSERT INTO `code_column_info` VALUES (9, 'mnt_database', 'update_time', 'datetime', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改时间', NULL);

-- ----------------------------
-- Table structure for code_gen_config
-- ----------------------------
DROP TABLE IF EXISTS `code_gen_config`;
CREATE TABLE `code_gen_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者邮箱',
  `comment` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注释',
  `cover` bit(1) NULL DEFAULT NULL COMMENT '是否覆盖',
  `module_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `pack` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后端代码生成的包路径',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端view代码生成的路径',
  `api_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端api代码生成的路径',
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表前缀',
  `api_alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成配置表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of code_gen_config
-- ----------------------------
INSERT INTO `code_gen_config` VALUES (1, 'mnt_database', 'zwk', 'zbcbbs@163.com', '数据库管理', b'0', 'qlq-application', 'com.dongzz.rdp.modules.mnt', 'src/views', 'src/api', 'mnt_', '运维：数据库管理');

-- ----------------------------
-- Table structure for mnt_database
-- ----------------------------
DROP TABLE IF EXISTS `mnt_database`;
CREATE TABLE `mnt_database`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `jdbc_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'jdbc地址',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据库管理表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of mnt_database
-- ----------------------------
INSERT INTO `mnt_database` VALUES ('1d989ef3b8d24b8296f21ec052e4521a', 'qlq-test', 'jdbc:mysql://127.0.0.1:3306/qlq-test?useSSL=false', 'root', '123456', 'admin', 'admin', '2022-05-19 18:01:07', '2022-05-20 13:52:15');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('schedulerFactoryBean', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('schedulerFactoryBean', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('schedulerFactoryBean', 'WIN-M6UI94OR87L1653981020131', 1653981533504, 15000);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `pid` int(11) NULL DEFAULT NULL COMMENT '上级部门',
  `sub_count` int(5) NULL DEFAULT 0 COMMENT '子部门数目',
  `enabled` bit(1) NOT NULL COMMENT '状态',
  `sort` int(5) NULL DEFAULT 999 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (2, '研发部', 7, 1, b'1', 3, '2019-03-25 09:15:32', '2020-08-02 14:48:47');
INSERT INTO `sys_dept` VALUES (5, '运维部', 7, 0, b'1', 4, '2019-03-25 09:20:44', '2020-05-17 14:27:27');
INSERT INTO `sys_dept` VALUES (6, '测试部', 8, 0, b'1', 6, '2019-03-25 09:52:18', '2020-06-08 11:59:21');
INSERT INTO `sys_dept` VALUES (7, '华南分部', 0, 2, b'1', 0, '2019-03-25 11:04:50', '2020-06-08 12:08:56');
INSERT INTO `sys_dept` VALUES (8, '华北分部', 0, 2, b'1', 1, '2019-03-25 11:04:53', '2020-05-14 12:54:00');
INSERT INTO `sys_dept` VALUES (15, 'UI部门', 8, 0, b'1', 7, '2020-05-13 22:56:53', '2020-05-14 12:54:13');
INSERT INTO `sys_dept` VALUES (17, '研发一组', 2, 0, b'0', 8, '2020-08-02 14:49:07', '2020-08-02 14:49:07');
INSERT INTO `sys_dept` VALUES (18, '总裁办', 0, 1, b'1', 999, '2021-11-26 00:30:04', '2021-11-27 01:40:58');
INSERT INTO `sys_dept` VALUES (22, '行政部', 18, 0, b'1', 999, '2021-11-27 14:18:14', '2021-11-27 14:20:17');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典编码',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '逻辑删除 \'1\':已删除 \'0\':未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, '用户状态', 'userStatus', '1', '用户状态', '2019-12-17 22:47:26', '2019-12-19 13:07:57', '0');
INSERT INTO `sys_dict` VALUES (2, '逻辑删除', 'delStatus', '1', '逻辑删除', '2019-12-17 22:56:35', '2019-12-17 22:56:37', '0');
INSERT INTO `sys_dict` VALUES (3, '性别', 'sex', '1', '性别', '2019-12-17 23:03:47', '2019-12-19 17:16:32', '0');
INSERT INTO `sys_dict` VALUES (4, '字典类型', 'dictType', '1', '字典类型', '2019-12-17 23:08:51', '2019-12-19 16:56:07', '0');
INSERT INTO `sys_dict` VALUES (6, '日志类型', 'logsType', '1', '日志类型', '2019-12-19 17:18:41', '2019-12-19 17:21:55', '0');
INSERT INTO `sys_dict` VALUES (7, '角色类型', 'roleType', '1', '角色类型', '2021-01-05 20:07:53', '2021-01-05 20:07:56', '0');
INSERT INTO `sys_dict` VALUES (8, '登陆方式', 'loginMethod', '1', '登陆方式', '2021-01-05 20:11:00', '2021-01-05 20:11:03', '0');
INSERT INTO `sys_dict` VALUES (9, '账号类型', 'accountType', '1', '账号类型', '2021-01-05 20:13:31', '2021-01-05 20:13:33', '0');
INSERT INTO `sys_dict` VALUES (10, '系统标识', 'systemFlag', '1', '系统标识', '2021-01-06 14:28:00', '2021-11-27 16:36:31', '0');
INSERT INTO `sys_dict` VALUES (11, '部门状态', 'deptStatus', '1', '部门状态', '2021-11-25 23:19:18', '2021-11-25 23:19:21', '0');
INSERT INTO `sys_dict` VALUES (12, '订单状态', 'orderStatus', '4', '订单状态', '2021-11-27 17:02:28', '2021-11-27 17:02:28', '0');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典编码 关联字典表\'code\'',
  `item_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典项编码',
  `item_value` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典项名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '逻辑删除 \'1\':已删除 \'0\':未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典项表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 'userStatus', '1', '启用', '启用', '2019-12-17 22:49:02', '2019-12-17 22:49:04', '0');
INSERT INTO `sys_dict_item` VALUES (2, 'userStatus', '0', '禁用', '禁用', '2019-12-17 22:49:33', '2019-12-17 22:49:35', '0');
INSERT INTO `sys_dict_item` VALUES (3, 'userStatus', '2', '锁定', '锁定', '2019-12-17 22:49:51', '2019-12-17 22:49:53', '0');
INSERT INTO `sys_dict_item` VALUES (4, 'delStatus', '1', '已删除', '已删除', '2019-12-17 22:56:58', '2019-12-17 22:57:00', '0');
INSERT INTO `sys_dict_item` VALUES (5, 'delStatus', '0', '未删除', '未删除', '2019-12-17 22:57:16', '2019-12-17 22:57:18', '0');
INSERT INTO `sys_dict_item` VALUES (6, 'sex', '1', '男', '男', '2019-12-17 23:05:11', '2019-12-17 23:05:14', '0');
INSERT INTO `sys_dict_item` VALUES (7, 'sex', '0', '女', '女', '2019-12-17 23:05:29', '2019-12-17 23:05:31', '0');
INSERT INTO `sys_dict_item` VALUES (8, 'sex', '2', '保密', '保密', '2019-12-17 23:05:50', '2019-12-17 23:05:52', '0');
INSERT INTO `sys_dict_item` VALUES (9, 'dictType', '1', '系统字典', '系统字典', '2019-12-17 23:09:20', '2019-12-17 23:09:23', '0');
INSERT INTO `sys_dict_item` VALUES (10, 'dictType', '2', '会员字典', '会员字典', '2019-12-17 23:12:59', '2019-12-17 23:13:01', '0');
INSERT INTO `sys_dict_item` VALUES (11, 'dictType', '3', '支付字典', '支付字典', '2019-12-17 23:13:20', '2019-12-17 23:13:22', '0');
INSERT INTO `sys_dict_item` VALUES (12, 'dictType', '4', '订单字典', '备注', '2019-12-19 16:05:59', '2019-12-19 16:54:38', '0');
INSERT INTO `sys_dict_item` VALUES (13, 'logsType', '1', '系统日志', '备注', '2019-12-19 17:24:52', '2019-12-19 17:24:52', '0');
INSERT INTO `sys_dict_item` VALUES (14, 'logsType', '2', '支付日志', '备注', '2019-12-19 17:25:45', '2019-12-19 17:26:18', '0');
INSERT INTO `sys_dict_item` VALUES (15, 'roleType', '1', '管理员角色', '管理员角色', '2021-01-05 20:08:39', '2021-01-05 20:08:42', '0');
INSERT INTO `sys_dict_item` VALUES (16, 'roleType', '2', '会员角色', '会员角色', '2021-01-05 20:09:16', '2021-01-05 20:09:14', '0');
INSERT INTO `sys_dict_item` VALUES (17, 'loginMethod', '1', '账密登陆', '账密登陆', '2021-01-05 20:11:54', '2021-01-05 20:11:57', '0');
INSERT INTO `sys_dict_item` VALUES (18, 'loginMethod', '2', '短信登陆', '短信登陆', '2021-01-05 20:12:18', '2021-01-05 20:12:21', '0');
INSERT INTO `sys_dict_item` VALUES (19, 'accountType', '1', '管理员', '管理员', '2021-01-05 20:15:01', '2021-01-05 20:15:04', '0');
INSERT INTO `sys_dict_item` VALUES (20, 'accountType', '2', '会员', '会员', '2021-01-05 20:15:20', '2021-01-05 20:15:22', '0');
INSERT INTO `sys_dict_item` VALUES (21, 'systemFlag', 'HPKOFJ', '安卓端', '安卓端', '2021-01-06 14:28:56', '2021-01-06 14:28:58', '0');
INSERT INTO `sys_dict_item` VALUES (22, 'systemFlag', 'LNCXSQ', '苹果端', '苹果端', '2021-01-06 14:29:34', '2021-01-06 14:29:36', '0');
INSERT INTO `sys_dict_item` VALUES (23, 'systemFlag', 'AWSGIH', '小程序端', '小程序端', '2021-01-06 16:10:05', '2021-01-06 16:10:12', '0');
INSERT INTO `sys_dict_item` VALUES (24, 'systemFlag', 'FFAAVD', 'PC后台', 'PC后台', '2021-01-06 16:10:08', '2021-01-06 16:10:14', '0');
INSERT INTO `sys_dict_item` VALUES (25, 'systemFlag', 'WVMUEY', 'PC前台', 'PC前台', '2021-01-06 16:10:10', '2021-01-06 16:10:16', '0');
INSERT INTO `sys_dict_item` VALUES (26, 'deptStatus', 'true', '正常', '正常', '2021-11-25 23:19:55', '2021-11-25 23:19:58', '0');
INSERT INTO `sys_dict_item` VALUES (27, 'deptStatus', 'false', '禁用', '禁用', '2021-11-25 23:20:24', '2021-11-27 16:58:58', '0');
INSERT INTO `sys_dict_item` VALUES (28, 'orderStatus', '0', '待付款', '待付款', '2021-11-27 17:05:52', '2021-11-27 17:06:16', '0');
INSERT INTO `sys_dict_item` VALUES (29, 'orderStatus', '1', '待发货', '待发货\n', '2021-11-27 17:06:52', '2021-11-27 17:08:19', '0');
INSERT INTO `sys_dict_item` VALUES (30, 'orderStatus', '2', '已发货', '已发货', '2021-11-27 17:08:42', '2021-11-27 17:08:42', '0');
INSERT INTO `sys_dict_item` VALUES (31, 'orderStatus', '3', '已收货', '已收货', '2021-11-27 17:10:23', '2021-11-27 17:10:23', '0');

-- ----------------------------
-- Table structure for sys_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_logs`;
CREATE TABLE `sys_logs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作者',
  `module` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `clazz` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类路径',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名',
  `ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口请求路径',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `return_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口返回数据',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '日志状态 \'0\':操作异常 \'1\':操作成功',
  `time` datetime(0) NULL DEFAULT NULL COMMENT '日志记录时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '接口请求时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '接口返回时间',
  `total_time` int(11) NULL DEFAULT NULL COMMENT '总消耗时间',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除 \'1\':已删除 \'0\':未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_logs
-- ----------------------------
INSERT INTO `sys_logs` VALUES (1, 'admin', '系统', '强制下线', 'com.dongzz.rdp.security.controller.OnlineController', 'delete', '192.168.56.1', 'http://127.0.0.1:8000/auth/online', '{}', '{\"code\":200,\"message\":\"OK\"}', '1', '2022-05-19 10:16:00', '2022-05-19 10:16:00', '2022-05-19 10:16:00', 2, '0');

-- ----------------------------
-- Table structure for sys_member
-- ----------------------------
DROP TABLE IF EXISTS `sys_member`;
CREATE TABLE `sys_member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `head_img_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态 \'1\':启用 \'0\':禁用 \'2\':锁定',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '性别 \'0\':女 \'1\':男',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `qq` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'QQ',
  `wechat` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `alipay` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝',
  `balance` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `score` double(30, 1) NULL DEFAULT 0.0 COMMENT '积分',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '上次登录时间',
  `last_ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上次登录ip',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除 \'1\':已删除 \'0\':未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_member
-- ----------------------------
INSERT INTO `sys_member` VALUES (1, 'zbc', '$2a$10$sqfibHjEx9sKvlITrw87ru2I.X8XDn4J9nIKzR1HmqcOqV1WTDexC', '北辰', NULL, '1', '0', '1992-07-21', '2317739191@qq.com', '0532-88888888', '15064236988', '2317739191', 'QLKJ20190520', NULL, 0.00, 0.0, '2020-12-12 13:21:29', '2020-12-12 13:21:31', '2020-12-12 13:22:45', ' 39.89.185.21', '0');

-- ----------------------------
-- Table structure for sys_member_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_member_role`;
CREATE TABLE `sys_member_role`  (
  `member_id` int(11) NOT NULL COMMENT '会员ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`member_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员角色关联表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_member_role
-- ----------------------------
INSERT INTO `sys_member_role` VALUES (1, 3);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级资源ID',
  `sub_count` int(5) NULL DEFAULT 0 COMMENT '子资源数目',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源类型 \'1\':目录 \'2\':菜单 \'3\':按钮',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件名称',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件',
  `sort` int(5) NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `href` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `i_frame` bit(1) NULL DEFAULT b'0' COMMENT '是否外链',
  `cache` bit(1) NULL DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) NULL DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限码',
  `source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统标记',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除 \'1\':已删除 \'0\':未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限资源表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 0, 5, '1', '平台', NULL, NULL, 1, 'monitor', 'platform', b'0', b'0', b'0', NULL, NULL, '2021-11-15 21:26:39', '2021-11-15 21:26:50', '0');
INSERT INTO `sys_permission` VALUES (2, 1, 6, '1', '系统管理', NULL, NULL, 2, 'system', 'system', b'0', b'0', b'0', NULL, NULL, '2021-11-15 21:31:20', '2021-11-23 21:13:49', '0');
INSERT INTO `sys_permission` VALUES (3, 2, 5, '2', '用户管理', 'User', 'system/user/index', 3, 'peoples', 'user', b'0', b'0', b'0', '', NULL, '2021-11-15 21:35:55', '2022-05-20 23:26:38', '0');
INSERT INTO `sys_permission` VALUES (4, 2, 6, '2', '角色管理', 'Role', 'system/role/index', 4, 'role', 'role', b'0', b'0', b'0', '', NULL, '2021-11-15 21:37:22', '2022-05-21 14:36:18', '0');
INSERT INTO `sys_permission` VALUES (5, 2, 5, '2', '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', '', NULL, '2021-11-15 21:39:20', '2022-05-21 14:36:31', '0');
INSERT INTO `sys_permission` VALUES (6, 2, 6, '2', '任务管理', 'Timing', 'system/timing/index', 6, 'timing', 'timing', b'0', b'0', b'0', '', NULL, '2021-11-15 21:41:16', '2022-05-21 14:36:42', '0');
INSERT INTO `sys_permission` VALUES (7, 2, 5, '2', '部门管理', 'Dept', 'system/dept/index', 7, 'dept', 'dept', b'0', b'0', b'0', '', NULL, '2021-11-15 21:42:55', '2022-05-21 14:36:57', '0');
INSERT INTO `sys_permission` VALUES (8, 2, 9, '2', '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', b'0', b'0', b'0', '', NULL, '2021-11-15 21:45:59', '2022-05-21 14:37:08', '0');
INSERT INTO `sys_permission` VALUES (9, 1, 7, '1', '系统工具', NULL, NULL, 9, 'sys-tools', 'sys-tools', b'0', b'0', b'0', NULL, NULL, '2021-11-15 21:53:44', '2021-11-15 21:53:46', '0');
INSERT INTO `sys_permission` VALUES (10, 9, 2, '2', '邮件工具', 'Email', 'tools/email/index', 10, 'email', 'email', b'0', b'0', b'0', NULL, NULL, '2021-11-15 21:56:02', '2021-11-15 21:56:05', '0');
INSERT INTO `sys_permission` VALUES (11, 9, 10, '2', '存储管理', 'Storage', 'tools/storage/index', 11, 'qiniu', 'storage', b'0', b'0', b'0', '', NULL, '2021-11-15 22:01:16', '2022-05-21 14:35:40', '0');
INSERT INTO `sys_permission` VALUES (12, 9, 0, '2', '支付宝', 'AliPay', 'tools/aliPay/index', 12, 'alipay', 'aliPay', b'0', b'0', b'1', NULL, NULL, '2021-11-15 22:02:45', '2022-05-22 15:55:07', '0');
INSERT INTO `sys_permission` VALUES (13, 9, 6, '2', '代码生成', 'GeneratorIndex', 'generator/index', 13, 'dev', 'generator', b'0', b'1', b'0', NULL, NULL, '2021-11-15 22:04:19', '2021-11-15 22:04:21', '0');
INSERT INTO `sys_permission` VALUES (14, 9, 0, '2', '接口文档', 'Swagger', 'tools/swagger/index', 14, 'swagger', 'http://127.0.0.1:8090/doc.html', b'1', b'0', b'0', NULL, NULL, '2021-11-15 22:05:41', '2022-05-21 16:34:12', '0');
INSERT INTO `sys_permission` VALUES (15, 9, 0, '2', '生成配置', 'GeneratorConfig', 'generator/config', 15, 'dev', 'config/:tableName', b'0', b'1', b'1', NULL, NULL, '2021-11-15 22:07:57', '2021-11-15 22:08:00', '0');
INSERT INTO `sys_permission` VALUES (16, 9, 0, '2', '生成预览', 'Preview', 'generator/preview', 16, 'java', 'preview/:tableName', b'0', b'1', b'1', NULL, NULL, '2021-11-15 22:10:33', '2021-11-15 22:10:37', '0');
INSERT INTO `sys_permission` VALUES (17, 1, 5, '1', '运维管理', 'Mnt', NULL, 17, 'mnt', 'mnt', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:15:40', '2021-11-15 22:15:42', '0');
INSERT INTO `sys_permission` VALUES (18, 17, 0, '2', '服务器', 'ServerDeploy', 'mnt/server/index', 18, 'server', 'mnt/serverDeploy', b'0', b'0', b'1', 'sys:server:list', NULL, '2021-11-15 22:18:23', '2022-05-23 08:52:39', '0');
INSERT INTO `sys_permission` VALUES (19, 17, 0, '2', '应用管理', 'App', 'mnt/app/index', 19, 'app', 'mnt/app', b'0', b'0', b'1', 'sys:app:list', NULL, '2021-11-15 22:20:10', '2022-05-23 08:52:49', '0');
INSERT INTO `sys_permission` VALUES (20, 17, 0, '2', '部署管理', 'Deploy', 'mnt/deploy/index', 20, 'deploy', 'mnt/deploy', b'0', b'0', b'1', 'sys:deploy:list', NULL, '2021-11-15 22:21:42', '2022-05-23 08:53:00', '0');
INSERT INTO `sys_permission` VALUES (21, 17, 0, '2', '部署备份', 'DeployHistory', 'mnt/deployHistory/index', 21, 'backup', 'mnt/deployHistory', b'0', b'0', b'1', 'sys:deployHistory:list', NULL, '2021-11-15 22:23:14', '2022-05-23 08:53:13', '0');
INSERT INTO `sys_permission` VALUES (22, 17, 6, '2', '数据库管理', 'Database', 'mnt/database/index', 22, 'database', 'mnt/database', b'0', b'0', b'0', 'sys:database:list', NULL, '2021-11-15 22:24:36', '2021-11-15 22:24:39', '0');
INSERT INTO `sys_permission` VALUES (23, 1, 5, '1', '系统监控', NULL, NULL, 23, 'monitor', 'monitor', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:30:45', '2021-11-15 22:30:48', '0');
INSERT INTO `sys_permission` VALUES (24, 23, 3, '2', '操作日志', 'Log', 'monitor/log/index', 24, 'log', 'logs', b'0', b'1', b'0', NULL, NULL, '2021-11-15 22:29:52', '2021-11-15 22:29:55', '0');
INSERT INTO `sys_permission` VALUES (25, 23, 0, '2', 'SQL监控', 'Sql', 'monitor/sql/index', 25, 'sqlMonitor', 'http://127.0.0.1:8090/druid/index.html', b'1', b'0', b'0', NULL, NULL, '2021-11-15 22:33:57', '2022-05-23 17:19:12', '0');
INSERT INTO `sys_permission` VALUES (26, 23, 0, '2', '异常日志', 'ErrorLog', 'monitor/log/errorLog', 26, 'error', 'errorLog', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:35:02', '2021-11-15 22:35:04', '0');
INSERT INTO `sys_permission` VALUES (27, 23, 3, '2', '在线用户', 'OnlineUser', 'monitor/online/index', 27, 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:36:24', '2021-11-15 22:36:27', '0');
INSERT INTO `sys_permission` VALUES (28, 23, 0, '2', '服务监控', 'ServerMonitor', 'monitor/server/index', 28, 'codeConsole', 'server', b'0', b'0', b'0', 'sys:monitor:list', NULL, '2021-11-15 22:37:53', '2021-11-15 22:37:56', '0');
INSERT INTO `sys_permission` VALUES (29, 1, 5, '1', '组件管理', NULL, NULL, 29, 'zujian', 'components', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:41:00', '2021-11-15 22:41:02', '0');
INSERT INTO `sys_permission` VALUES (30, 29, 0, '2', '图标库', 'Icons', 'components/icons/index', 30, 'icon', 'icon', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:42:05', '2021-11-15 22:42:07', '0');
INSERT INTO `sys_permission` VALUES (31, 29, 0, '2', '富文本', 'Editor', 'components/Editor', 31, 'fwb', 'tinymce', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:43:10', '2021-11-15 22:43:12', '0');
INSERT INTO `sys_permission` VALUES (32, 29, 0, '2', 'Markdown', 'Markdown', 'components/MarkDown', 32, 'markdown', 'markdown', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:44:21', '2021-11-15 22:44:23', '0');
INSERT INTO `sys_permission` VALUES (33, 29, 0, '2', 'Yaml编辑器', 'YamlEditor', 'components/YamlEdit', 33, 'dev', 'yaml', b'0', b'0', b'0', NULL, NULL, '2021-11-15 22:45:34', '2022-05-22 15:35:26', '0');
INSERT INTO `sys_permission` VALUES (34, 29, 0, '2', '图表库', 'Echarts', 'components/Echarts', 34, 'chart', 'echarts', b'0', b'1', b'0', NULL, NULL, '2021-11-15 22:47:29', '2021-11-15 22:47:31', '0');
INSERT INTO `sys_permission` VALUES (35, 0, 1, '1', '内容管理', NULL, NULL, 35, 'app', 'site', b'0', b'0', b'0', NULL, NULL, '2021-11-23 21:17:22', '2021-11-23 21:17:22', '0');
INSERT INTO `sys_permission` VALUES (36, 35, 0, '1', '文章管理', NULL, NULL, 36, 'log', 'article', b'0', b'0', b'0', NULL, NULL, '2021-11-27 14:59:22', '2021-11-27 14:59:22', '0');
INSERT INTO `sys_permission` VALUES (37, 3, 0, '3', '用户新增', NULL, NULL, 37, NULL, NULL, b'0', b'0', b'0', 'sys:user:add', NULL, '2022-05-21 01:45:42', '2022-05-21 01:45:42', '0');
INSERT INTO `sys_permission` VALUES (38, 3, 0, '3', '用户修改', NULL, NULL, 38, NULL, NULL, b'0', b'0', b'0', 'sys:user:edit', NULL, '2022-05-21 01:48:53', '2022-05-21 01:48:53', '0');
INSERT INTO `sys_permission` VALUES (39, 3, 0, '3', '用户删除', NULL, NULL, 39, NULL, NULL, b'0', b'0', b'0', 'sys:user:del', NULL, '2022-05-21 01:49:58', '2022-05-21 01:49:58', '0');
INSERT INTO `sys_permission` VALUES (40, 3, 0, '3', '用户查询', NULL, NULL, 40, NULL, NULL, b'0', b'0', b'0', 'sys:user:query', NULL, '2022-05-21 01:51:15', '2022-05-21 01:51:15', '0');
INSERT INTO `sys_permission` VALUES (41, 3, 0, '3', '用户导出', NULL, NULL, 41, NULL, NULL, b'0', b'0', b'0', 'sys:user:excel', NULL, '2022-05-21 01:53:16', '2022-05-21 01:53:16', '0');
INSERT INTO `sys_permission` VALUES (42, 4, 0, '3', '角色新增', NULL, NULL, 42, NULL, NULL, b'0', b'0', b'0', 'sys:role:add', NULL, '2022-05-21 02:05:55', '2022-05-21 02:05:55', '0');
INSERT INTO `sys_permission` VALUES (43, 4, 0, '3', '角色修改', NULL, NULL, 43, NULL, NULL, b'0', b'0', b'0', 'sys:role:edit', NULL, '2022-05-21 02:06:47', '2022-05-21 02:06:47', '0');
INSERT INTO `sys_permission` VALUES (44, 4, 0, '3', '角色删除', NULL, NULL, 44, NULL, NULL, b'0', b'0', b'0', 'sys:role:del', NULL, '2022-05-21 02:07:36', '2022-05-21 02:07:36', '0');
INSERT INTO `sys_permission` VALUES (45, 4, 0, '3', '角色查询', NULL, NULL, 45, NULL, NULL, b'0', b'0', b'0', 'sys:role:query', NULL, '2022-05-21 02:08:35', '2022-05-21 02:08:35', '0');
INSERT INTO `sys_permission` VALUES (46, 4, 0, '3', '角色导出', NULL, NULL, 46, NULL, NULL, b'0', b'0', b'0', 'sys:role:excel', NULL, '2022-05-21 02:10:56', '2022-05-21 02:10:56', '0');
INSERT INTO `sys_permission` VALUES (47, 4, 0, '3', '角色授权', NULL, NULL, 47, NULL, NULL, b'0', b'0', b'0', 'sys:role:grant', NULL, '2022-05-21 02:13:36', '2022-05-21 02:13:36', '0');
INSERT INTO `sys_permission` VALUES (48, 5, 0, '3', '菜单新增', NULL, NULL, 48, NULL, NULL, b'0', b'0', b'0', 'sys:menu:add', NULL, '2022-05-21 02:16:37', '2022-05-21 02:16:37', '0');
INSERT INTO `sys_permission` VALUES (49, 5, 0, '3', '菜单修改', NULL, NULL, 49, NULL, NULL, b'0', b'0', b'0', 'sys:menu:edit', NULL, '2022-05-21 02:17:55', '2022-05-21 02:17:55', '0');
INSERT INTO `sys_permission` VALUES (50, 5, 0, '3', '菜单删除', NULL, NULL, 50, NULL, NULL, b'0', b'0', b'0', 'sys:menu:del', NULL, '2022-05-21 02:18:42', '2022-05-21 02:18:42', '0');
INSERT INTO `sys_permission` VALUES (51, 5, 0, '3', '菜单查询', NULL, NULL, 51, NULL, NULL, b'0', b'0', b'0', 'sys:menu:query', NULL, '2022-05-21 02:19:49', '2022-05-21 02:19:49', '0');
INSERT INTO `sys_permission` VALUES (52, 5, 0, '3', '菜单导出', NULL, NULL, 52, NULL, NULL, b'0', b'0', b'0', 'sys:menu:excel', NULL, '2022-05-21 02:20:56', '2022-05-21 02:20:56', '0');
INSERT INTO `sys_permission` VALUES (53, 6, 0, '3', '任务新增', NULL, NULL, 53, NULL, NULL, b'0', b'0', b'0', 'sys:job:add', NULL, '2022-05-21 02:33:20', '2022-05-21 02:33:20', '0');
INSERT INTO `sys_permission` VALUES (54, 6, 0, '3', '任务修改', NULL, NULL, 54, NULL, NULL, b'0', b'0', b'0', 'sys:job:edit', NULL, '2022-05-21 02:33:54', '2022-05-21 02:33:54', '0');
INSERT INTO `sys_permission` VALUES (55, 6, 0, '3', '任务删除', NULL, NULL, 55, NULL, NULL, b'0', b'0', b'0', 'sys:job:del', NULL, '2022-05-21 02:34:49', '2022-05-21 02:34:49', '0');
INSERT INTO `sys_permission` VALUES (56, 6, 0, '3', '任务查询', NULL, NULL, 56, NULL, NULL, b'0', b'0', b'0', 'sys:job:query', NULL, '2022-05-21 02:35:29', '2022-05-21 02:35:29', '0');
INSERT INTO `sys_permission` VALUES (57, 6, 0, '3', '任务导出', NULL, NULL, 57, NULL, NULL, b'0', b'0', b'0', 'sys:job:excel', NULL, '2022-05-21 02:36:04', '2022-05-21 02:36:04', '0');
INSERT INTO `sys_permission` VALUES (58, 6, 0, '3', '任务日志', NULL, NULL, 58, NULL, NULL, b'0', b'0', b'0', 'sys:job:log', NULL, '2022-05-21 02:37:46', '2022-05-21 02:37:46', '0');
INSERT INTO `sys_permission` VALUES (59, 7, 0, '3', '部门新增', NULL, NULL, 59, NULL, NULL, b'0', b'0', b'0', 'sys:dept:add', NULL, '2022-05-21 02:53:21', '2022-05-21 02:53:21', '0');
INSERT INTO `sys_permission` VALUES (60, 7, 0, '3', '部门修改', NULL, NULL, 60, NULL, NULL, b'0', b'0', b'0', 'sys:dept:edit', NULL, '2022-05-21 02:53:50', '2022-05-21 02:53:50', '0');
INSERT INTO `sys_permission` VALUES (61, 7, 0, '3', '部门删除', NULL, NULL, 61, NULL, NULL, b'0', b'0', b'0', 'sys:dept:del', NULL, '2022-05-21 02:54:17', '2022-05-21 02:54:17', '0');
INSERT INTO `sys_permission` VALUES (62, 7, 0, '3', '部门查询', NULL, NULL, 62, NULL, NULL, b'0', b'0', b'0', 'sys:dept:query', NULL, '2022-05-21 02:54:41', '2022-05-21 02:54:41', '0');
INSERT INTO `sys_permission` VALUES (63, 7, 0, '3', '部门导出', NULL, NULL, 63, NULL, NULL, b'0', b'0', b'0', 'sys:dept:excel', NULL, '2022-05-21 02:55:07', '2022-05-21 02:55:07', '0');
INSERT INTO `sys_permission` VALUES (64, 8, 0, '3', '字典新增', NULL, NULL, 64, NULL, NULL, b'0', b'0', b'0', 'sys:dict:add', NULL, '2022-05-21 09:05:16', '2022-05-21 09:05:16', '0');
INSERT INTO `sys_permission` VALUES (65, 8, 0, '3', '字典修改', NULL, NULL, 65, NULL, NULL, b'0', b'0', b'0', 'sys:dict:edit', NULL, '2022-05-21 09:05:48', '2022-05-21 09:05:48', '0');
INSERT INTO `sys_permission` VALUES (66, 8, 0, '3', '字典删除', NULL, NULL, 66, NULL, NULL, b'0', b'0', b'0', 'sys:dict:del', NULL, '2022-05-21 09:06:09', '2022-05-21 09:06:09', '0');
INSERT INTO `sys_permission` VALUES (67, 8, 0, '3', '字典查询', NULL, NULL, 67, NULL, NULL, b'0', b'0', b'0', 'sys:dict:query', NULL, '2022-05-21 09:07:03', '2022-05-21 09:07:03', '0');
INSERT INTO `sys_permission` VALUES (68, 8, 0, '3', '字典导出', NULL, NULL, 68, NULL, NULL, b'0', b'0', b'0', 'sys:dict:excel', NULL, '2022-05-21 09:07:34', '2022-05-21 09:07:34', '0');
INSERT INTO `sys_permission` VALUES (69, 8, 0, '3', '字典项新增', NULL, NULL, 69, NULL, NULL, b'0', b'0', b'0', 'sys:dict:item:add', NULL, '2022-05-21 10:30:19', '2022-05-21 10:30:19', '0');
INSERT INTO `sys_permission` VALUES (70, 8, 0, '3', '字典项修改', NULL, NULL, 70, NULL, NULL, b'0', b'0', b'0', 'sys:dict:item:edit', NULL, '2022-05-21 10:31:27', '2022-05-21 10:31:27', '0');
INSERT INTO `sys_permission` VALUES (71, 8, 0, '3', '字典项删除', NULL, NULL, 71, NULL, NULL, b'0', b'0', b'0', 'sys:dict:item:del', NULL, '2022-05-21 10:32:37', '2022-05-21 10:32:37', '0');
INSERT INTO `sys_permission` VALUES (72, 8, 0, '3', '字典项查询', NULL, NULL, 72, NULL, NULL, b'0', b'0', b'0', 'sys:dict:item:query', NULL, '2022-05-21 10:34:39', '2022-05-21 10:34:39', '0');
INSERT INTO `sys_permission` VALUES (73, 10, 0, '3', '邮箱配置', NULL, NULL, 73, NULL, NULL, b'0', b'0', b'0', 'tool:email:config', NULL, '2022-05-21 14:14:15', '2022-05-21 14:14:15', '0');
INSERT INTO `sys_permission` VALUES (74, 10, 0, '3', '邮件发送', NULL, NULL, 74, NULL, NULL, b'0', b'0', b'0', 'tool:email:send', NULL, '2022-05-21 14:16:45', '2022-05-21 14:16:45', '0');
INSERT INTO `sys_permission` VALUES (75, 11, 0, '3', '本地上传', NULL, NULL, 75, NULL, NULL, b'0', b'0', b'0', 'tool:file:local:upload', NULL, '2022-05-21 14:31:50', '2022-05-21 14:31:50', '0');
INSERT INTO `sys_permission` VALUES (76, 11, 0, '3', '本地修改', NULL, NULL, 76, NULL, NULL, b'0', b'0', b'0', 'tool:file:local:edit', NULL, '2022-05-21 14:33:13', '2022-05-21 14:33:13', '0');
INSERT INTO `sys_permission` VALUES (77, 11, 0, '3', '本地删除', NULL, NULL, 77, NULL, NULL, b'0', b'0', b'0', 'tool:file:local:del', NULL, '2022-05-21 14:33:46', '2022-05-21 14:33:46', '0');
INSERT INTO `sys_permission` VALUES (78, 11, 0, '3', '本地查询', NULL, NULL, 78, NULL, NULL, b'0', b'0', b'0', 'tool:file:local:query', NULL, '2022-05-21 14:34:17', '2022-05-21 14:34:17', '0');
INSERT INTO `sys_permission` VALUES (79, 11, 0, '3', '本地导出', NULL, NULL, 79, NULL, NULL, b'0', b'0', b'0', 'tool:file:local:excel', NULL, '2022-05-21 14:35:17', '2022-05-21 14:35:17', '0');
INSERT INTO `sys_permission` VALUES (80, 11, 0, '3', '腾讯上传', NULL, NULL, 80, NULL, NULL, b'0', b'0', b'0', 'tool:file:cos:upload', NULL, '2022-05-21 14:39:49', '2022-05-21 14:39:49', '0');
INSERT INTO `sys_permission` VALUES (81, 11, 0, '3', '腾讯配置', NULL, NULL, 81, NULL, NULL, b'0', b'0', b'0', 'tool:file:cos:config', NULL, '2022-05-21 14:40:58', '2022-05-21 14:40:58', '0');
INSERT INTO `sys_permission` VALUES (82, 11, 0, '3', '腾讯删除', NULL, NULL, 82, NULL, NULL, b'0', b'0', b'0', 'tool:file:cos:del', NULL, '2022-05-21 14:41:41', '2022-05-21 14:41:41', '0');
INSERT INTO `sys_permission` VALUES (83, 11, 0, '3', '腾讯查询', NULL, NULL, 83, NULL, NULL, b'0', b'0', b'0', 'tool:file:cos:query', NULL, '2022-05-21 14:42:18', '2022-05-21 14:42:18', '0');
INSERT INTO `sys_permission` VALUES (84, 11, 0, '3', '腾讯导出', NULL, NULL, 84, NULL, NULL, b'0', b'0', b'0', 'tool:file:cos:excel', NULL, '2022-05-21 14:42:48', '2022-05-21 14:42:48', '0');
INSERT INTO `sys_permission` VALUES (85, 13, 0, '3', '表信息查询', NULL, NULL, 85, NULL, NULL, b'0', b'0', b'0', 'tool:code:query', NULL, '2022-05-21 15:07:17', '2022-05-21 15:09:32', '0');
INSERT INTO `sys_permission` VALUES (86, 13, 0, '3', '表信息同步', NULL, NULL, 86, NULL, NULL, b'0', b'0', b'0', 'tool:code:sync', NULL, '2022-05-21 15:07:56', '2022-05-21 15:07:56', '0');
INSERT INTO `sys_permission` VALUES (87, 13, 0, '3', '代码配置', NULL, NULL, 87, NULL, NULL, b'0', b'0', b'0', 'tool:code:config', NULL, '2022-05-21 15:11:12', '2022-05-21 15:11:12', '0');
INSERT INTO `sys_permission` VALUES (88, 13, 0, '3', '代码预览', NULL, NULL, 88, NULL, NULL, b'0', b'0', b'0', 'tool:code:preview', NULL, '2022-05-21 15:12:30', '2022-05-21 15:12:30', '0');
INSERT INTO `sys_permission` VALUES (89, 13, 0, '3', '代码下载', NULL, NULL, 89, NULL, NULL, b'0', b'0', b'0', 'tool:code:download', NULL, '2022-05-21 15:13:02', '2022-05-21 15:13:02', '0');
INSERT INTO `sys_permission` VALUES (90, 13, 0, '3', '代码生成', NULL, NULL, 90, NULL, NULL, b'0', b'0', b'0', 'tool:code:generator', NULL, '2022-05-21 15:13:41', '2022-05-21 15:13:41', '0');
INSERT INTO `sys_permission` VALUES (91, 24, 0, '3', '日志清空', NULL, NULL, 91, NULL, NULL, b'0', b'0', b'0', 'monitor:log:clear', NULL, '2022-05-21 17:36:19', '2022-05-21 17:36:19', '0');
INSERT INTO `sys_permission` VALUES (92, 24, 0, '3', '日志查询', NULL, NULL, 92, NULL, NULL, b'0', b'0', b'0', 'monitor:log:query', NULL, '2022-05-21 17:37:09', '2022-05-21 17:37:09', '0');
INSERT INTO `sys_permission` VALUES (93, 24, 0, '3', '日志导出', NULL, NULL, 93, NULL, NULL, b'0', b'0', b'0', 'monitor:log:excel', NULL, '2022-05-21 17:39:05', '2022-05-21 17:39:05', '0');
INSERT INTO `sys_permission` VALUES (94, 27, 0, '3', '用户踢出', NULL, NULL, 94, NULL, NULL, b'0', b'0', b'0', 'monitor:user:kick', NULL, '2022-05-21 17:49:52', '2022-05-21 17:49:52', '0');
INSERT INTO `sys_permission` VALUES (95, 27, 0, '3', '用户查询', NULL, NULL, 95, NULL, NULL, b'0', b'0', b'0', 'monitor:user:query', NULL, '2022-05-21 17:50:38', '2022-05-21 17:50:38', '0');
INSERT INTO `sys_permission` VALUES (96, 27, 0, '3', '用户导出', NULL, NULL, 96, NULL, NULL, b'0', b'0', b'0', 'monitor:user:excel', NULL, '2022-05-21 17:51:26', '2022-05-21 17:51:26', '0');
INSERT INTO `sys_permission` VALUES (97, 22, 0, '3', '数据库新增', NULL, NULL, 97, NULL, NULL, b'0', b'0', b'0', 'mnt:db:add', NULL, '2022-05-22 20:39:00', '2022-05-22 20:39:00', '0');
INSERT INTO `sys_permission` VALUES (98, 22, 0, '3', '数据库修改', NULL, NULL, 98, NULL, NULL, b'0', b'0', b'0', 'mnt:db:edit', NULL, '2022-05-22 20:39:46', '2022-05-22 20:39:46', '0');
INSERT INTO `sys_permission` VALUES (99, 22, 0, '3', '数据库删除', NULL, NULL, 99, NULL, NULL, b'0', b'0', b'0', 'mnt:db:del', NULL, '2022-05-22 20:40:28', '2022-05-22 20:40:28', '0');
INSERT INTO `sys_permission` VALUES (100, 22, 0, '3', '数据库查询', NULL, NULL, 100, NULL, NULL, b'0', b'0', b'0', 'mnt:db:query', NULL, '2022-05-22 20:41:13', '2022-05-22 20:41:13', '0');
INSERT INTO `sys_permission` VALUES (101, 22, 0, '3', '数据库导出', NULL, NULL, 101, NULL, NULL, b'0', b'0', b'0', 'mnt:db:excel', NULL, '2022-05-22 20:42:56', '2022-05-22 20:42:56', '0');
INSERT INTO `sys_permission` VALUES (102, 22, 0, '3', '数据脚本执行', NULL, NULL, 102, NULL, NULL, b'0', b'0', b'0', 'mnt:db:script', NULL, '2022-05-22 20:44:25', '2022-05-22 20:44:25', '0');

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `cron` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表达式',
  `spring_bean_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'spring bean 名称',
  `method_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '方法名',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `is_sys_job` tinyint(1) NOT NULL COMMENT '是否系统任务',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '任务状态',
  `person_in_charge` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `pause_after_failure` tinyint(1) NULL DEFAULT NULL COMMENT '任务失败后是否暂停',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警邮箱',
  `sub_task` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子任务ID',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_quartz_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_log`;
CREATE TABLE `sys_quartz_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `bean_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean 名称',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `cron` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表达式',
  `time` bigint(20) NULL DEFAULT NULL COMMENT '执行耗时',
  `is_success` tinyint(1) NULL DEFAULT NULL COMMENT '状态 1：成功 0：异常',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务日志表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色码',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色类型 \'1\':管理员角色 \'2\':会员角色',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NOT NULL COMMENT '添加时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除 \'1\':已删除 \'0\':未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ROLE_ADMIN', '1', '拥有至高无上的权限', '2019-12-03 15:33:57', '2020-05-22 15:25:09', '0');
INSERT INTO `sys_role` VALUES (2, '普通用户', 'ROLE_USER', '1', '拥有普通用户权限', '2019-12-10 16:33:50', '2019-12-13 18:59:27', '0');
INSERT INTO `sys_role` VALUES (3, '普通会员', 'ROLE_MEM', '2', '拥有普通会员权限', '2019-12-13 18:57:46', '2019-12-13 18:58:27', '0');
INSERT INTO `sys_role` VALUES (4, '普通用户1', 'ROLE_USER1', '1', '普通用户1', '2021-11-23 17:29:17', '2021-11-23 17:32:13', '0');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色资源关联表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1);
INSERT INTO `sys_role_permission` VALUES (1, 2);
INSERT INTO `sys_role_permission` VALUES (1, 3);
INSERT INTO `sys_role_permission` VALUES (1, 4);
INSERT INTO `sys_role_permission` VALUES (1, 5);
INSERT INTO `sys_role_permission` VALUES (1, 6);
INSERT INTO `sys_role_permission` VALUES (1, 7);
INSERT INTO `sys_role_permission` VALUES (1, 8);
INSERT INTO `sys_role_permission` VALUES (1, 9);
INSERT INTO `sys_role_permission` VALUES (1, 10);
INSERT INTO `sys_role_permission` VALUES (1, 11);
INSERT INTO `sys_role_permission` VALUES (1, 12);
INSERT INTO `sys_role_permission` VALUES (1, 13);
INSERT INTO `sys_role_permission` VALUES (1, 14);
INSERT INTO `sys_role_permission` VALUES (1, 15);
INSERT INTO `sys_role_permission` VALUES (1, 16);
INSERT INTO `sys_role_permission` VALUES (1, 17);
INSERT INTO `sys_role_permission` VALUES (1, 18);
INSERT INTO `sys_role_permission` VALUES (1, 19);
INSERT INTO `sys_role_permission` VALUES (1, 20);
INSERT INTO `sys_role_permission` VALUES (1, 21);
INSERT INTO `sys_role_permission` VALUES (1, 22);
INSERT INTO `sys_role_permission` VALUES (1, 23);
INSERT INTO `sys_role_permission` VALUES (1, 24);
INSERT INTO `sys_role_permission` VALUES (1, 25);
INSERT INTO `sys_role_permission` VALUES (1, 26);
INSERT INTO `sys_role_permission` VALUES (1, 27);
INSERT INTO `sys_role_permission` VALUES (1, 28);
INSERT INTO `sys_role_permission` VALUES (1, 29);
INSERT INTO `sys_role_permission` VALUES (1, 30);
INSERT INTO `sys_role_permission` VALUES (1, 31);
INSERT INTO `sys_role_permission` VALUES (1, 32);
INSERT INTO `sys_role_permission` VALUES (1, 33);
INSERT INTO `sys_role_permission` VALUES (1, 34);
INSERT INTO `sys_role_permission` VALUES (1, 35);
INSERT INTO `sys_role_permission` VALUES (1, 36);
INSERT INTO `sys_role_permission` VALUES (1, 37);
INSERT INTO `sys_role_permission` VALUES (1, 38);
INSERT INTO `sys_role_permission` VALUES (1, 39);
INSERT INTO `sys_role_permission` VALUES (1, 40);
INSERT INTO `sys_role_permission` VALUES (1, 41);
INSERT INTO `sys_role_permission` VALUES (1, 42);
INSERT INTO `sys_role_permission` VALUES (1, 43);
INSERT INTO `sys_role_permission` VALUES (1, 44);
INSERT INTO `sys_role_permission` VALUES (1, 45);
INSERT INTO `sys_role_permission` VALUES (1, 46);
INSERT INTO `sys_role_permission` VALUES (1, 47);
INSERT INTO `sys_role_permission` VALUES (1, 48);
INSERT INTO `sys_role_permission` VALUES (1, 49);
INSERT INTO `sys_role_permission` VALUES (1, 50);
INSERT INTO `sys_role_permission` VALUES (1, 51);
INSERT INTO `sys_role_permission` VALUES (1, 52);
INSERT INTO `sys_role_permission` VALUES (1, 53);
INSERT INTO `sys_role_permission` VALUES (1, 54);
INSERT INTO `sys_role_permission` VALUES (1, 55);
INSERT INTO `sys_role_permission` VALUES (1, 56);
INSERT INTO `sys_role_permission` VALUES (1, 57);
INSERT INTO `sys_role_permission` VALUES (1, 58);
INSERT INTO `sys_role_permission` VALUES (1, 59);
INSERT INTO `sys_role_permission` VALUES (1, 60);
INSERT INTO `sys_role_permission` VALUES (1, 61);
INSERT INTO `sys_role_permission` VALUES (1, 62);
INSERT INTO `sys_role_permission` VALUES (1, 63);
INSERT INTO `sys_role_permission` VALUES (1, 64);
INSERT INTO `sys_role_permission` VALUES (1, 65);
INSERT INTO `sys_role_permission` VALUES (1, 66);
INSERT INTO `sys_role_permission` VALUES (1, 67);
INSERT INTO `sys_role_permission` VALUES (1, 68);
INSERT INTO `sys_role_permission` VALUES (1, 69);
INSERT INTO `sys_role_permission` VALUES (1, 70);
INSERT INTO `sys_role_permission` VALUES (1, 71);
INSERT INTO `sys_role_permission` VALUES (1, 72);
INSERT INTO `sys_role_permission` VALUES (1, 73);
INSERT INTO `sys_role_permission` VALUES (1, 74);
INSERT INTO `sys_role_permission` VALUES (1, 75);
INSERT INTO `sys_role_permission` VALUES (1, 76);
INSERT INTO `sys_role_permission` VALUES (1, 77);
INSERT INTO `sys_role_permission` VALUES (1, 78);
INSERT INTO `sys_role_permission` VALUES (1, 79);
INSERT INTO `sys_role_permission` VALUES (1, 80);
INSERT INTO `sys_role_permission` VALUES (1, 81);
INSERT INTO `sys_role_permission` VALUES (1, 82);
INSERT INTO `sys_role_permission` VALUES (1, 83);
INSERT INTO `sys_role_permission` VALUES (1, 84);
INSERT INTO `sys_role_permission` VALUES (1, 85);
INSERT INTO `sys_role_permission` VALUES (1, 86);
INSERT INTO `sys_role_permission` VALUES (1, 87);
INSERT INTO `sys_role_permission` VALUES (1, 88);
INSERT INTO `sys_role_permission` VALUES (1, 89);
INSERT INTO `sys_role_permission` VALUES (1, 90);
INSERT INTO `sys_role_permission` VALUES (1, 91);
INSERT INTO `sys_role_permission` VALUES (1, 92);
INSERT INTO `sys_role_permission` VALUES (1, 93);
INSERT INTO `sys_role_permission` VALUES (1, 94);
INSERT INTO `sys_role_permission` VALUES (1, 95);
INSERT INTO `sys_role_permission` VALUES (1, 96);
INSERT INTO `sys_role_permission` VALUES (1, 97);
INSERT INTO `sys_role_permission` VALUES (1, 98);
INSERT INTO `sys_role_permission` VALUES (1, 99);
INSERT INTO `sys_role_permission` VALUES (1, 100);
INSERT INTO `sys_role_permission` VALUES (1, 101);
INSERT INTO `sys_role_permission` VALUES (1, 102);
INSERT INTO `sys_role_permission` VALUES (2, 1);
INSERT INTO `sys_role_permission` VALUES (2, 2);
INSERT INTO `sys_role_permission` VALUES (2, 3);
INSERT INTO `sys_role_permission` VALUES (4, 1);
INSERT INTO `sys_role_permission` VALUES (4, 2);
INSERT INTO `sys_role_permission` VALUES (4, 3);

-- ----------------------------
-- Table structure for sys_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_token`;
CREATE TABLE `sys_token`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `val` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'LoginUser的json串',
  `expire_time` datetime(0) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录令牌表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `head_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `telephone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '性别 \'1\':男 \'0\':女',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态 \'1\':启用 \'0\':禁用 \'2\':锁定',
  `create_time` datetime(0) NOT NULL COMMENT '添加时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `last_ip` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上次登录IP',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '上次登录时间',
  `count` int(11) NULL DEFAULT 0 COMMENT '登录次数',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除 \'1\':已删除 \'0\':未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$L/u6y1CogiwnJq2dmNG5/.elzpjOLF8IbQQ4KKlqwTGLSQiJaILra', '管理员', 5, 'https://test-1305827789.cos.ap-shanghai.myqcloud.com/image/2022/05/31/02cf12e4b0734e99bef7aac254d1a1bb.png', '15064236988', '0532-5770480', 'zbcbbs@163.com', '1992-07-21', '备注', '1', '1', '2019-12-03 15:31:46', '2022-05-30 21:09:43', '169.254.244.100', '2020-10-17 12:31:46', 82, '0');
INSERT INTO `sys_user` VALUES (2, 'zhangsan', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '张三', 5, NULL, '18310646733', '0537-5334520', '2317739191@163.com', '1993-07-16', '备注', '1', '1', '2019-12-08 02:11:35', '2019-12-08 02:11:38', NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (6, 'lisi', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '九黎', 6, NULL, '18270824147', '0537-5770480', '927364932@qq.com', '1992-07-21', '备注1', '1', '0', '2019-12-10 23:40:40', '2019-12-12 04:30:19', NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (7, 'wangwu', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '王五', 6, NULL, '15064236988', NULL, '2317730101@qq.com', '1992-07-23', '备注', '1', '1', '2019-12-10 23:42:56', '2019-12-10 23:42:56', NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (8, 'xiaoli', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '小丽', 6, NULL, '18270824147', NULL, '242424242@qq.com', '1991-10-11', '备注', '1', '1', '2019-12-10 23:46:54', '2019-12-10 23:46:54', NULL, NULL, NULL, '1');
INSERT INTO `sys_user` VALUES (9, 'xiaowu', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '小五', 15, NULL, '15467456789', NULL, '32455655@qq.com', '1987-12-14', '备注', '1', '1', '2019-12-10 23:49:32', '2019-12-10 23:49:32', NULL, NULL, NULL, '1');
INSERT INTO `sys_user` VALUES (10, 'xiaoya', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '小雅', 15, NULL, '18310646733', NULL, '456987123@163.com', '1988-07-21', '备注', '0', '1', '2019-12-20 13:13:06', '2019-12-20 13:13:06', NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (11, 'xiaomi', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '小米', 15, NULL, '18270824147', NULL, '23567845121@qq.com', '1989-08-22', '备注', '0', '2', '2019-12-20 13:44:25', '2019-12-20 13:44:25', NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (12, 'wenkai', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', 'wkk', 5, NULL, '18270824147', NULL, 'wenkai0537@163.com', '1996-08-23', '备注', '0', '1', '2019-12-20 18:24:40', '2019-12-20 18:24:40', NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (13, 'xiaohe', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '小何', 6, NULL, '18270824147', NULL, '511212112@qq.com', '1995-02-23', '备注', '0', '1', '2019-12-21 01:43:35', '2019-12-21 01:43:35', NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (14, 'hehe', '$2a$10$MaVrAI2Yr2cC/p8Io4VN6.dUc6guksWwEzu3nZ8yi2j0BME.Ept.e', '呵呵', 6, NULL, '18310646733', '', '987564125@qq.com', '1996-07-25', '备注', '0', '1', '2019-12-21 01:59:01', '2019-12-21 02:06:34', NULL, NULL, NULL, '0');
INSERT INTO `sys_user` VALUES (15, 'lkx', '$2a$10$scdpQ.lYmKvDhOB0zWJCLuxxeRHhreMWZJzCCTJk7vKcT8jKvkc2y', '烈焰', 6, NULL, '15064236988', NULL, '23564545@qq.com', NULL, NULL, '1', '1', '2021-11-22 18:34:57', '2021-11-22 18:34:57', NULL, NULL, 0, '0');
INSERT INTO `sys_user` VALUES (16, 'lkk', '$2a$10$Y15FcBmBFs1BgdcBM8dVjuWGYhnCEhamw7LiiEhkF3ugenShNf23m', '百万', 5, NULL, '15326321241', NULL, '123456@qq.com', NULL, NULL, '1', '1', '2021-11-22 18:43:00', '2021-11-22 18:43:00', NULL, NULL, 0, '0');
INSERT INTO `sys_user` VALUES (17, 'zff', '$2a$10$gDXTKBubPJRJXxHPVe3QSuntyXA.qv2Gwshv6STA7Pu8f7KTal.fO', '纷纷', 6, NULL, '15064236988', NULL, '12356887@qq.com', NULL, NULL, '1', '1', '2021-11-22 18:49:41', '2021-11-22 22:43:28', NULL, NULL, 0, '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (6, 2);
INSERT INTO `sys_user_role` VALUES (7, 2);
INSERT INTO `sys_user_role` VALUES (8, 1);
INSERT INTO `sys_user_role` VALUES (9, 2);
INSERT INTO `sys_user_role` VALUES (10, 2);
INSERT INTO `sys_user_role` VALUES (11, 2);
INSERT INTO `sys_user_role` VALUES (12, 2);
INSERT INTO `sys_user_role` VALUES (13, 2);
INSERT INTO `sys_user_role` VALUES (14, 2);
INSERT INTO `sys_user_role` VALUES (15, 1);
INSERT INTO `sys_user_role` VALUES (16, 1);
INSERT INTO `sys_user_role` VALUES (17, 2);

-- ----------------------------
-- Table structure for tool_cos_config
-- ----------------------------
DROP TABLE IF EXISTS `tool_cos_config`;
CREATE TABLE `tool_cos_config`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储桶',
  `secret_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密钥',
  `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APPID',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问域名',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '腾讯云上传配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tool_cos_config
-- ----------------------------
INSERT INTO `tool_cos_config` VALUES (1, 'test', 'AKIDnPkpdARRAbleGbeRwA7nDMEp9CyTheKk', 'C7k6GCr44oHTO3ScXDeplRs3xax3wYuz', '1305827789', 'https://test-1305827789.cos.ap-shanghai.myqcloud.com', 'ap-shanghai');

-- ----------------------------
-- Table structure for tool_cos_file
-- ----------------------------
DROP TABLE IF EXISTS `tool_cos_file`;
CREATE TABLE `tool_cos_file`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键，MD5值',
  `name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '名称',
  `cache_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储名称',
  `cache_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储路径',
  `bucket` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储桶',
  `content_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容类型',
  `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问路径',
  `path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相对路径',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '腾讯云上传存储表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tool_cos_file
-- ----------------------------
INSERT INTO `tool_cos_file` VALUES ('87a10b1f5ac4e1b7a410ab7ad1cda711', 'avatar.png', '02cf12e4b0734e99bef7aac254d1a1bb.png', 'image/2022/05/31/02cf12e4b0734e99bef7aac254d1a1bb.png', 'test-1305827789', 'image/png', '84.28KB   ', 'https://test-1305827789.cos.ap-shanghai.myqcloud.com/image/2022/05/31/02cf12e4b0734e99bef7aac254d1a1bb.png', '/image/2022/05/31/02cf12e4b0734e99bef7aac254d1a1bb.png', 'image', '2022-05-31 15:11:00', '2022-05-31 15:11:00');
INSERT INTO `tool_cos_file` VALUES ('e8f09a94752be8133ca6d7fec9853f84', 'image_2.jpeg', 'a02eb58e401947be9d032b7928ce502b.jpeg', 'image/2022/05/31/a02eb58e401947be9d032b7928ce502b.jpeg', 'test-1305827789', 'image/jpeg', '71.77KB   ', 'https://test-1305827789.cos.ap-shanghai.myqcloud.com/image/2022/05/31/a02eb58e401947be9d032b7928ce502b.jpeg', '/image/2022/05/31/a02eb58e401947be9d032b7928ce502b.jpeg', 'image', '2022-05-31 14:34:52', '2022-05-31 14:34:52');

-- ----------------------------
-- Table structure for tool_local_file
-- ----------------------------
DROP TABLE IF EXISTS `tool_local_file`;
CREATE TABLE `tool_local_file`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键，MD5值',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `cache_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储名称',
  `cache_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储路径',
  `content_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容类型',
  `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问路径',
  `path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相对路径',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '本地上传存储表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tool_local_file
-- ----------------------------
INSERT INTO `tool_local_file` VALUES ('2a37a8b1d40741425ed44a96e9eaf0ab', 'image_1.jpeg', '9cc555e48195478a9d71ed9f7976b912.jpeg', 'D:\\quick\\file\\image\\2022\\05\\31\\9cc555e48195478a9d71ed9f7976b912.jpeg', 'image/jpeg', '59.66KB   ', 'http://127.0.0.1:8090/file/image/2022/05/31/9cc555e48195478a9d71ed9f7976b912.jpeg', '\\image\\2022\\05\\31\\9cc555e48195478a9d71ed9f7976b912.jpeg', 'image', '2022-05-31 14:33:58', '2022-05-31 14:33:58');

-- ----------------------------
-- Table structure for tool_mail
-- ----------------------------
DROP TABLE IF EXISTS `tool_mail`;
CREATE TABLE `tool_mail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '发件者',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '正文',
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tool_mail
-- ----------------------------
INSERT INTO `tool_mail` VALUES (1, 1, '一封家书', '<p>这是一封测试邮件，不必大惊小怪</p>', '2022-05-31 14:39:39', '2022-05-31 14:39:39');

-- ----------------------------
-- Table structure for tool_mail_config
-- ----------------------------
DROP TABLE IF EXISTS `tool_mail_config`;
CREATE TABLE `tool_mail_config`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `from_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件者邮箱',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '端口',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件者用户名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮箱配置表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tool_mail_config
-- ----------------------------
INSERT INTO `tool_mail_config` VALUES (1, 'zbcbbs@163.com', 'smtp.163.com', 'MGSOLUKYLAIVDXCO', '465', '北辰');

-- ----------------------------
-- Table structure for tool_mail_to
-- ----------------------------
DROP TABLE IF EXISTS `tool_mail_to`;
CREATE TABLE `tool_mail_to`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mail_id` int(11) NOT NULL,
  `to_user` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1：成功，0：异常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件发送表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tool_mail_to
-- ----------------------------
INSERT INTO `tool_mail_to` VALUES (1, 1, 'zwk20190520@163.com', 1);

-- ----------------------------
-- Event structure for e_delete_expire_token
-- ----------------------------
DROP EVENT IF EXISTS `e_delete_expire_token`;
delimiter ;;
CREATE EVENT `e_delete_expire_token`
ON SCHEDULE
EVERY '30' SECOND STARTS '2020-12-29 11:22:30'
ON COMPLETION PRESERVE
DO delete from sys_token where expire_time < (CURRENT_TIMESTAMP() + INTERVAL +60 MINUTE)
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
