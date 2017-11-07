/*
Navicat MySQL Data Transfer

Source Server         : x3408
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : kaohe

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-11-07 20:17:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `chapter`
-- ----------------------------
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
  `chapter_id` smallint(5) NOT NULL AUTO_INCREMENT,
  `chapter_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`chapter_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chapter
-- ----------------------------
INSERT INTO `chapter` VALUES ('1', '第一章');
INSERT INTO `chapter` VALUES ('2', '第二章');
INSERT INTO `chapter` VALUES ('3', '第三章');
INSERT INTO `chapter` VALUES ('4', '第四章');
INSERT INTO `chapter` VALUES ('5', '第五章');
INSERT INTO `chapter` VALUES ('6', '第六章');
INSERT INTO `chapter` VALUES ('7', '第七章');
INSERT INTO `chapter` VALUES ('8', '第八章');
INSERT INTO `chapter` VALUES ('9', '第九章');
INSERT INTO `chapter` VALUES ('10', '第十章');

-- ----------------------------
-- Table structure for `class`
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `class_id` smallint(5) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES ('1', '软工1班');
INSERT INTO `class` VALUES ('2', '软工2班');
INSERT INTO `class` VALUES ('3', '计算机科学与技术1班');
INSERT INTO `class` VALUES ('4', '电商1班');

-- ----------------------------
-- Table structure for `content`
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content` (
  `content_id` smallint(5) NOT NULL AUTO_INCREMENT,
  `content_title` varchar(20) DEFAULT NULL,
  `content_illustrate` varchar(40) DEFAULT NULL,
  `content_subject` mediumblob,
  `content_answer` blob,
  `scope_id` set('评分项1','评分项2','评分项3') DEFAULT NULL,
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES ('45', 'Java第一章作业', '说明', null, null, '评分项1,评分项2');
INSERT INTO `content` VALUES ('46', 'C++', '说明', null, null, '评分项1');

-- ----------------------------
-- Table structure for `course`
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `course_id` smallint(5) NOT NULL AUTO_INCREMENT,
  `course_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', 'Java');
INSERT INTO `course` VALUES ('2', 'C++');
INSERT INTO `course` VALUES ('3', '数据结构');
INSERT INTO `course` VALUES ('4', '离散数学');
INSERT INTO `course` VALUES ('5', 'JavaWeb');

-- ----------------------------
-- Table structure for `homework`
-- ----------------------------
DROP TABLE IF EXISTS `homework`;
CREATE TABLE `homework` (
  `id` smallint(5) NOT NULL AUTO_INCREMENT,
  `course_id` smallint(5) DEFAULT NULL,
  `chapter_id` smallint(5) DEFAULT NULL,
  `content_id` smallint(5) DEFAULT NULL,
  `teacher_id` smallint(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of homework
-- ----------------------------
INSERT INTO `homework` VALUES ('16', '1', '1', '45', '1');
INSERT INTO `homework` VALUES ('17', '2', '1', '46', '2');

-- ----------------------------
-- Table structure for `sbhm`
-- ----------------------------
DROP TABLE IF EXISTS `sbhm`;
CREATE TABLE `sbhm` (
  `id` smallint(5) DEFAULT NULL,
  `student_id` smallint(5) DEFAULT NULL,
  `stime` date DEFAULT NULL,
  `homeworkContent` varchar(255) DEFAULT NULL,
  `score` smallint(4) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sbhm
-- ----------------------------
INSERT INTO `sbhm` VALUES ('16', '1', '2017-11-06', '内容', null, null);
INSERT INTO `sbhm` VALUES ('16', '2', null, null, null, null);
INSERT INTO `sbhm` VALUES ('16', '3', null, null, null, null);
INSERT INTO `sbhm` VALUES ('16', '4', null, null, null, null);
INSERT INTO `sbhm` VALUES ('17', '1', null, null, null, null);
INSERT INTO `sbhm` VALUES ('17', '2', null, null, null, null);
INSERT INTO `sbhm` VALUES ('17', '3', null, null, null, null);
INSERT INTO `sbhm` VALUES ('17', '4', null, null, null, null);

-- ----------------------------
-- Table structure for `scope2`
-- ----------------------------
DROP TABLE IF EXISTS `scope2`;
CREATE TABLE `scope2` (
  `content_id` smallint(5) DEFAULT NULL,
  `class_id` smallint(5) DEFAULT NULL,
  `showtime` date DEFAULT NULL,
  `submittime` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of scope2
-- ----------------------------
INSERT INTO `scope2` VALUES ('45', '1', '2017-10-16', '2017-11-11');
INSERT INTO `scope2` VALUES ('46', '1', '2017-10-16', '2017-11-16');

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `student_id` smallint(5) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `dept` varchar(20) DEFAULT NULL,
  `grade` varchar(10) DEFAULT NULL,
  `class_id` smallint(4) DEFAULT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', '张三', '计算机', '一年级', '1');
INSERT INTO `student` VALUES ('2', '李四', '计算机', '一年级', '1');
INSERT INTO `student` VALUES ('3', '王五', '计算机', '一年级', '2');
INSERT INTO `student` VALUES ('4', '赵六', '计算机', '一年级', '2');

-- ----------------------------
-- Table structure for `teacher`
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `teacher_id` smallint(5) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1', '老师1');
INSERT INTO `teacher` VALUES ('2', '老师2');
DROP TRIGGER IF EXISTS `tg_del`;
DELIMITER ;;
CREATE TRIGGER `tg_del` AFTER DELETE ON `homework` FOR EACH ROW BEGIN
DELETE from content WHERE old.content_id=content.content_id;
DELETE FROM SCOPE2 WHERE old.content_id=scope2.content_id;
delete from sbhm where old.id=sbhm.id;
END
;;
DELIMITER ;
