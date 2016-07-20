/*
SQLyog v10.2 
MySQL - 5.6.14 : Database - shiro
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`shiro` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `shiro`;

/*Table structure for table `shiro_permission` */

DROP TABLE IF EXISTS `shiro_permission`;

CREATE TABLE `shiro_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限资源和角色关系编号',
  `resource_id` int(11) NOT NULL COMMENT '权限资源编号',
  `role_id` int(11) NOT NULL COMMENT '权限角色编号',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0、正常 1、删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_role` (`resource_id`,`role_id`),
  KEY `resource_id` (`resource_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `shiro_permission_ibfk_1` FOREIGN KEY (`resource_id`) REFERENCES `shiro_resource` (`id`),
  CONSTRAINT `shiro_permission_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `shiro_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5011 DEFAULT CHARSET=utf8;

/*Data for the table `shiro_permission` */

insert  into `shiro_permission`(`id`,`resource_id`,`role_id`,`status`) values (4999,717,118,0),(5000,716,118,0),(5001,718,118,0),(5002,713,118,0),(5003,712,118,0),(5004,715,118,0),(5005,714,118,0),(5006,711,118,0),(5007,710,118,0),(5008,712,119,0),(5009,715,119,0),(5010,711,119,0);

/*Table structure for table `shiro_resource` */

DROP TABLE IF EXISTS `shiro_resource`;

CREATE TABLE `shiro_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限资源编号',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: struts namespace 受控资源; 1：struts action url、菜单、按钮受控资源',
  `value` varchar(4) NOT NULL COMMENT '四级结构 [0-9a-z]{1,4}, 从左到右分别为 一级菜单、二级菜单、三级菜单、按钮，命名时，先使用0-9，然后使用a-z',
  `description` varchar(20) NOT NULL COMMENT '资源描述',
  `url` varchar(100) DEFAULT NULL COMMENT '访问地址',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0、正常 1、删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `value` (`value`)
) ENGINE=InnoDB AUTO_INCREMENT=719 DEFAULT CHARSET=utf8;

/*Data for the table `shiro_resource` */

insert  into `shiro_resource`(`id`,`type`,`value`,`description`,`url`,`status`) values (710,0,'a','admin','/admin',0),(711,1,'b','权限管理','',0),(712,1,'ba','用户管理','',0),(713,1,'bb','角色管理','/admin/roleManage.action',0),(714,1,'bc','资源管理','',0),(715,1,'baa','用户查询','/admin/userManage.action',0),(716,1,'bab','用户新增','/admin/userAdd.jsp',0),(717,1,'bca','资源查询','/admin/resourceManage.action',0),(718,1,'bcb','资源编辑','/admin/toResourceEdit.action',0);

/*Table structure for table `shiro_role` */

DROP TABLE IF EXISTS `shiro_role`;

CREATE TABLE `shiro_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限角色编号',
  `description` varchar(50) NOT NULL DEFAULT '' COMMENT '角色描述',
  `create_user` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0、正常 1、删除',
  `builtin` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0、非内置的 1、内置的（内置的记录不允许通过程序修改）',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `description` (`description`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8;

/*Data for the table `shiro_role` */

insert  into `shiro_role`(`role_id`,`description`,`create_user`,`create_time`,`status`,`builtin`) values (118,'管理员',8,'2016-07-20 16:57:17',0,1),(119,'客服',8,'2016-07-20 16:57:23',0,1);

/*Table structure for table `shiro_user_role` */

DROP TABLE IF EXISTS `shiro_user_role`;

CREATE TABLE `shiro_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户和角色关系编号',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `role_id` int(11) NOT NULL COMMENT '角色编号',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0、正常 1、删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_role` (`user_id`,`role_id`),
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `shiro_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `shiro_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;

/*Data for the table `shiro_user_role` */

insert  into `shiro_user_role`(`id`,`user_id`,`role_id`,`status`) values (90,9,119,0),(91,8,118,0);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `account` varchar(20) NOT NULL COMMENT '账号',
  `user_name` varchar(20) NOT NULL COMMENT '姓名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `age` tinyint(4) DEFAULT NULL COMMENT '年龄',
  `user_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1、学生  2、老师  3、管理员',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`user_id`,`account`,`user_name`,`password`,`age`,`user_type`) values (1,'zjj','张三','e10adc3949ba59abbe56e057f20f883e',18,1),(2,'xyh','李四','e10adc3949ba59abbe56e057f20f883e',19,1),(3,'ll','王五','e10adc3949ba59abbe56e057f20f883e',20,1),(4,'lm','厘米','e10adc3949ba59abbe56e057f20f883e',16,1),(6,'bb','宝宝','e10adc3949ba59abbe56e057f20f883e',30,1),(7,'teacher','王老师','e10adc3949ba59abbe56e057f20f883e',18,2),(8,'admin','小小管理员','e10adc3949ba59abbe56e057f20f883e',20,3),(9,'cs','客服','e10adc3949ba59abbe56e057f20f883e',20,3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
