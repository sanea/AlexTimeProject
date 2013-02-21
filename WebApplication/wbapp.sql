CREATE DATABASE  IF NOT EXISTS `webapp` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `webapp`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: webapp
-- ------------------------------------------------------
-- Server version	5.1.66-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `group_authorities`
--

DROP TABLE IF EXISTS `group_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_authorities` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL,
  `authority` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `fk_authorities_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_authorities`
--

LOCK TABLES `group_authorities` WRITE;
/*!40000 ALTER TABLE `group_authorities` DISABLE KEYS */;
INSERT INTO `group_authorities` VALUES (2,1,'EDIT_USERS'),(3,1,'MANAGE_TASK'),(4,1,'EDIT_TASKS'),(5,1,'STAT'),(8,2,'MANAGE_TASK');
/*!40000 ALTER TABLE `group_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_members`
--

DROP TABLE IF EXISTS `group_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_members` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `group_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  KEY `username` (`username`),
  CONSTRAINT `fk_members_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `fk_members_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_members`
--

LOCK TABLES `group_members` WRITE;
/*!40000 ALTER TABLE `group_members` DISABLE KEYS */;
INSERT INTO `group_members` VALUES (1,'admin',1),(2,'user1',2),(3,'user2',2);
/*!40000 ALTER TABLE `group_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (1,'admin'),(2,'operator');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `task_name` varchar(50) NOT NULL,
  `task_type` char(1) NOT NULL,
  `task_price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `task_enabled` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'Списание Часов','p',100.00,1),(2,'Печать','t',250.00,1);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_action`
--

DROP TABLE IF EXISTS `user_action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_action` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `task_time_id` bigint(20) unsigned NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `action` char(1) NOT NULL,
  `time_seconds` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TASK_TIME_idx` (`task_time_id`),
  CONSTRAINT `FK_TASK_TIME` FOREIGN KEY (`task_time_id`) REFERENCES `user_task_time` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=298 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_action`
--

LOCK TABLES `user_action` WRITE;
/*!40000 ALTER TABLE `user_action` DISABLE KEYS */;
INSERT INTO `user_action` VALUES (123,112,'2013-02-20 11:22:37','r',1800),(124,112,'2013-02-20 11:23:01','e',NULL),(125,112,'2013-02-20 11:23:25','e',NULL),(126,112,'2013-02-20 11:23:29','e',NULL),(127,112,'2013-02-20 11:24:29','f',NULL),(128,113,'2013-02-20 11:50:10','r',60),(129,113,'2013-02-20 11:50:45','e',NULL),(130,113,'2013-02-20 11:51:07','e',NULL),(131,113,'2013-02-20 11:51:10','p',NULL),(132,113,'2013-02-20 11:51:16','c',NULL),(133,113,'2013-02-20 11:51:19','p',NULL),(134,113,'2013-02-20 11:51:21','c',NULL),(135,113,'2013-02-20 11:51:24','p',NULL),(136,113,'2013-02-20 11:51:25','c',NULL),(137,113,'2013-02-20 11:51:26','p',NULL),(138,113,'2013-02-20 11:51:27','c',NULL),(139,113,'2013-02-20 11:51:28','p',NULL),(140,113,'2013-02-20 11:51:29','c',NULL),(141,113,'2013-02-20 11:51:30','p',NULL),(142,113,'2013-02-20 11:51:31','c',NULL),(143,113,'2013-02-20 11:51:32','p',NULL),(144,113,'2013-02-20 11:51:34','c',NULL),(145,113,'2013-02-20 11:51:35','p',NULL),(146,113,'2013-02-20 11:51:36','c',NULL),(147,113,'2013-02-20 11:51:37','p',NULL),(148,113,'2013-02-20 11:51:38','c',NULL),(149,113,'2013-02-20 11:51:39','p',NULL),(150,113,'2013-02-20 11:51:41','c',NULL),(151,113,'2013-02-20 11:51:42','p',NULL),(152,113,'2013-02-20 11:51:44','c',NULL),(153,113,'2013-02-20 11:51:45','p',NULL),(154,113,'2013-02-20 11:51:46','c',NULL),(155,113,'2013-02-20 11:51:46','p',NULL),(156,113,'2013-02-20 11:51:48','c',NULL),(157,113,'2013-02-20 11:51:49','p',NULL),(158,113,'2013-02-20 11:51:50','c',NULL),(159,113,'2013-02-20 11:51:51','p',NULL),(160,113,'2013-02-20 11:53:32','c',NULL),(161,113,'2013-02-20 11:53:34','p',NULL),(162,113,'2013-02-20 11:53:35','c',NULL),(163,113,'2013-02-20 11:53:37','p',NULL),(164,113,'2013-02-20 11:53:38','c',NULL),(165,113,'2013-02-20 11:53:39','s',NULL),(166,114,'2013-02-20 11:53:47','r',NULL),(167,115,'2013-02-20 11:53:48','r',NULL),(168,116,'2013-02-20 11:53:48','r',NULL),(169,117,'2013-02-20 11:56:34','r',NULL),(170,118,'2013-02-20 11:56:35','r',NULL),(171,119,'2013-02-20 11:56:35','r',NULL),(172,120,'2013-02-20 11:56:35','r',NULL),(173,121,'2013-02-20 11:56:35','r',NULL),(174,122,'2013-02-20 11:56:36','r',NULL),(175,123,'2013-02-20 11:56:38','r',NULL),(176,124,'2013-02-20 12:31:04','r',600),(177,124,'2013-02-20 12:31:12','p',NULL),(178,124,'2013-02-20 12:31:15','c',NULL),(179,124,'2013-02-20 12:31:17','p',NULL),(180,124,'2013-02-20 12:31:18','c',NULL),(181,124,'2013-02-20 12:31:21','p',NULL),(182,124,'2013-02-20 12:31:22','c',NULL),(183,124,'2013-02-20 12:31:23','p',NULL),(184,124,'2013-02-20 12:31:24','c',NULL),(185,124,'2013-02-20 12:31:29','p',NULL),(186,124,'2013-02-20 12:34:59','c',NULL),(187,124,'2013-02-20 12:35:06','p',NULL),(188,124,'2013-02-20 12:35:07','c',NULL),(189,124,'2013-02-20 12:35:08','p',NULL),(190,124,'2013-02-20 12:35:09','c',NULL),(191,124,'2013-02-20 12:35:10','p',NULL),(192,124,'2013-02-20 12:35:11','c',NULL),(193,124,'2013-02-20 12:35:12','p',NULL),(194,124,'2013-02-20 12:35:13','c',NULL),(195,124,'2013-02-20 12:35:14','p',NULL),(196,124,'2013-02-20 12:35:15','c',NULL),(197,124,'2013-02-20 12:35:16','p',NULL),(198,124,'2013-02-20 12:35:17','c',NULL),(199,124,'2013-02-20 12:35:23','e',NULL),(200,124,'2013-02-20 12:35:37','e',NULL),(201,124,'2013-02-20 12:35:52','p',NULL),(202,124,'2013-02-20 12:35:53','c',NULL),(203,124,'2013-02-20 12:35:55','p',NULL),(204,124,'2013-02-20 12:35:56','c',NULL),(205,124,'2013-02-20 12:35:56','p',NULL),(206,124,'2013-02-20 12:35:57','c',NULL),(207,124,'2013-02-20 12:35:58','p',NULL),(208,124,'2013-02-20 12:35:59','c',NULL),(209,124,'2013-02-20 12:36:00','p',NULL),(210,124,'2013-02-20 12:36:02','c',NULL),(211,124,'2013-02-20 12:36:04','p',NULL),(212,124,'2013-02-20 12:36:06','c',NULL),(213,124,'2013-02-20 12:36:07','p',NULL),(214,124,'2013-02-20 12:36:08','c',NULL),(215,124,'2013-02-20 12:36:09','p',NULL),(216,124,'2013-02-20 12:36:10','c',NULL),(217,124,'2013-02-20 12:36:12','p',NULL),(218,124,'2013-02-20 12:36:13','c',NULL),(219,124,'2013-02-20 12:36:14','p',NULL),(220,124,'2013-02-20 12:36:17','c',NULL),(221,124,'2013-02-20 12:36:40','e',NULL),(222,124,'2013-02-20 12:36:54','s',NULL),(223,125,'2013-02-20 12:37:23','r',NULL),(224,126,'2013-02-20 12:37:23','r',NULL),(225,127,'2013-02-20 12:40:34','r',60),(226,127,'2013-02-20 12:41:34','f',NULL),(227,128,'2013-02-20 14:58:14','r',60),(228,128,'2013-02-20 14:58:18','e',NULL),(229,128,'2013-02-20 14:58:20','e',NULL),(230,128,'2013-02-20 14:58:22','s',NULL),(231,129,'2013-02-20 14:58:26','r',60),(232,129,'2013-02-20 14:58:27','p',NULL),(233,129,'2013-02-20 14:58:28','c',NULL),(234,129,'2013-02-20 14:58:30','e',NULL),(235,129,'2013-02-20 14:58:31','p',NULL),(236,129,'2013-02-20 14:58:32','s',NULL),(237,130,'2013-02-20 14:59:01','r',1800),(238,130,'2013-02-20 14:59:13','s',NULL),(239,131,'2013-02-20 14:59:26','r',60),(240,131,'2013-02-20 14:59:28','e',NULL),(241,131,'2013-02-20 14:59:39','p',NULL),(242,131,'2013-02-20 14:59:41','c',NULL),(243,131,'2013-02-20 14:59:42','s',NULL),(244,132,'2013-02-20 14:59:44','r',60),(245,132,'2013-02-20 14:59:46','s',NULL),(246,133,'2013-02-20 15:00:19','r',1800),(247,134,'2013-02-21 07:13:05','r',NULL),(248,135,'2013-02-21 07:13:08','r',NULL),(249,136,'2013-02-21 07:30:14','r',60),(250,136,'2013-02-21 07:31:14','f',NULL),(251,137,'2013-02-21 07:47:21','r',60),(252,137,'2013-02-21 07:48:21','f',NULL),(253,138,'2013-02-21 07:54:19','r',60),(254,138,'2013-02-21 07:55:19','f',NULL),(255,139,'2013-02-21 08:02:11','r',60),(256,139,'2013-02-21 08:03:11','f',NULL),(257,140,'2013-02-21 08:03:23','r',NULL),(258,141,'2013-02-21 08:03:25','r',NULL),(259,142,'2013-02-21 12:31:12','r',60),(260,142,'2013-02-21 12:31:17','p',NULL),(261,142,'2013-02-21 12:31:19','c',NULL),(262,142,'2013-02-21 12:31:24','e',NULL),(263,142,'2013-02-21 12:31:26','p',NULL),(264,142,'2013-02-21 12:31:29','c',NULL),(265,142,'2013-02-21 12:31:31','p',NULL),(266,142,'2013-02-21 12:31:59','c',NULL),(267,142,'2013-02-21 12:32:03','p',NULL),(268,142,'2013-02-21 12:32:06','c',NULL),(269,142,'2013-02-21 12:32:07','p',NULL),(270,142,'2013-02-21 12:32:14','c',NULL),(271,142,'2013-02-21 12:32:15','p',NULL),(272,142,'2013-02-21 12:32:16','c',NULL),(273,142,'2013-02-21 12:32:20','p',NULL),(274,142,'2013-02-21 12:32:20','c',NULL),(275,142,'2013-02-21 12:32:22','p',NULL),(276,142,'2013-02-21 12:32:23','c',NULL),(277,142,'2013-02-21 12:32:23','p',NULL),(278,142,'2013-02-21 12:32:24','c',NULL),(279,142,'2013-02-21 12:32:25','p',NULL),(280,142,'2013-02-21 12:32:25','c',NULL),(281,142,'2013-02-21 12:32:26','p',NULL),(282,142,'2013-02-21 13:29:06','c',NULL),(283,142,'2013-02-21 13:29:10','p',NULL),(284,142,'2013-02-21 13:29:14','c',NULL),(285,142,'2013-02-21 13:29:16','p',NULL),(286,142,'2013-02-21 13:31:00','c',NULL),(287,142,'2013-02-21 13:31:03','p',NULL),(288,142,'2013-02-21 13:31:06','c',NULL),(289,142,'2013-02-21 13:31:25','p',NULL),(290,142,'2013-02-21 13:31:28','c',NULL),(291,142,'2013-02-21 13:32:35','p',NULL),(292,142,'2013-02-21 13:32:42','c',NULL),(293,142,'2013-02-21 13:32:47','s',NULL),(294,143,'2013-02-21 13:33:11','r',60),(295,143,'2013-02-21 13:34:11','f',NULL),(296,144,'2013-02-21 14:35:28','r',60),(297,144,'2013-02-21 14:35:58','s',NULL);
/*!40000 ALTER TABLE `user_action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_task`
--

DROP TABLE IF EXISTS `user_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `task_id` bigint(20) unsigned NOT NULL,
  `status` char(1) NOT NULL DEFAULT 'u',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_taks` (`username`,`task_id`),
  KEY `idx_task_id` (`task_id`),
  CONSTRAINT `fk_user_task_task` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`),
  CONSTRAINT `fk_user_task_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task`
--

LOCK TABLES `user_task` WRITE;
/*!40000 ALTER TABLE `user_task` DISABLE KEYS */;
INSERT INTO `user_task` VALUES (1,'user1',1,'s','2013-02-21 14:35:58','2013-02-18 13:16:31'),(2,'user1',2,'c','2013-02-21 08:03:25','2013-02-18 13:16:31'),(3,'user2',1,'r','2013-02-20 15:00:19','2013-02-18 13:16:31');
/*!40000 ALTER TABLE `user_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_task_time`
--

DROP TABLE IF EXISTS `user_task_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_task_time` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_task_id` bigint(20) unsigned NOT NULL,
  `duration` int(11) NOT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `finish_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `current` tinyint(1) NOT NULL DEFAULT '0',
  `time_seq_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `time_seq_id_UNIQUE` (`time_seq_id`),
  KEY `fk_user_task_idx` (`user_task_id`),
  KEY `FK_TIME_SEQ_idx` (`time_seq_id`),
  CONSTRAINT `FK_TIME_SEQ` FOREIGN KEY (`time_seq_id`) REFERENCES `user_task_time_seq` (`id`),
  CONSTRAINT `FK_USER_TASK1` FOREIGN KEY (`user_task_id`) REFERENCES `user_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task_time`
--

LOCK TABLES `user_task_time` WRITE;
/*!40000 ALTER TABLE `user_task_time` DISABLE KEYS */;
INSERT INTO `user_task_time` VALUES (112,1,112,'2013-02-20 11:22:37','2013-02-20 11:24:29',0,17),(113,1,82,'2013-02-20 11:50:10','2013-02-20 12:31:55',0,18),(114,2,0,'2013-02-20 11:53:47','2013-02-20 11:53:47',0,NULL),(115,2,0,'2013-02-20 11:53:48','2013-02-20 11:53:48',0,NULL),(116,2,0,'2013-02-20 11:53:48','2013-02-20 11:53:48',0,NULL),(117,2,0,'2013-02-20 11:56:34','2013-02-20 11:56:34',0,NULL),(118,2,0,'2013-02-20 11:56:35','2013-02-20 11:56:35',0,NULL),(119,2,0,'2013-02-20 11:56:35','2013-02-20 11:56:35',0,NULL),(120,2,0,'2013-02-20 11:56:35','2013-02-20 11:56:35',0,NULL),(121,2,0,'2013-02-20 11:56:35','2013-02-20 11:56:35',0,NULL),(122,2,0,'2013-02-20 11:56:36','2013-02-20 11:56:36',0,NULL),(123,2,0,'2013-02-20 11:56:38','2013-02-20 11:56:38',0,NULL),(124,1,114,'2013-02-20 12:31:04','2013-02-20 14:15:00',0,36),(125,2,0,'2013-02-20 12:37:23','2013-02-20 12:37:23',0,NULL),(126,2,0,'2013-02-20 12:37:23','2013-02-20 12:37:23',0,NULL),(127,1,60,'2013-02-20 12:40:34','2013-02-20 12:41:34',0,58),(128,1,8,'2013-02-20 14:58:14','2013-02-20 14:58:22',0,59),(129,1,4,'2013-02-20 14:58:26','2013-02-20 14:58:32',0,60),(130,1,12,'2013-02-20 14:59:01','2013-02-20 14:59:13',0,62),(131,3,14,'2013-02-20 14:59:26','2013-02-20 14:59:42',0,63),(132,3,2,'2013-02-20 14:59:44','2013-02-20 14:59:46',0,65),(133,3,1800,'2013-02-20 15:00:19','2013-02-20 15:30:19',1,66),(134,2,0,'2013-02-21 07:13:05','2013-02-21 07:13:05',0,NULL),(135,2,0,'2013-02-21 07:13:08','2013-02-21 07:13:08',0,NULL),(136,1,60,'2013-02-21 07:30:14','2013-02-21 07:31:14',0,67),(137,1,60,'2013-02-21 07:47:21','2013-02-21 07:48:21',0,68),(138,1,60,'2013-02-21 07:54:19','2013-02-21 07:55:19',0,69),(139,1,60,'2013-02-21 08:02:11','2013-02-21 08:03:11',0,70),(140,2,0,'2013-02-21 08:03:23','2013-02-21 08:03:23',0,NULL),(141,2,0,'2013-02-21 08:03:25','2013-02-21 08:03:25',0,NULL),(142,1,128,'2013-02-21 12:31:12','2013-02-21 13:32:47',0,71),(143,1,60,'2013-02-21 13:33:11','2013-02-21 13:34:11',0,88),(144,1,30,'2013-02-21 14:35:28','2013-02-21 14:35:58',0,89);
/*!40000 ALTER TABLE `user_task_time` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_task_time_seq`
--

DROP TABLE IF EXISTS `user_task_time_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_task_time_seq` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `next_id` bigint(20) unsigned DEFAULT NULL,
  `prev_id` bigint(20) unsigned DEFAULT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TIME_SEQ_NEXT_idx` (`next_id`),
  KEY `FK_TIME_SEQ_PREV_idx` (`prev_id`),
  CONSTRAINT `FK_TIME_SEQ_NEXT` FOREIGN KEY (`next_id`) REFERENCES `user_task_time_seq` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_TIME_SEQ_PREV` FOREIGN KEY (`prev_id`) REFERENCES `user_task_time_seq` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task_time_seq`
--

LOCK TABLES `user_task_time_seq` WRITE;
/*!40000 ALTER TABLE `user_task_time_seq` DISABLE KEYS */;
INSERT INTO `user_task_time_seq` VALUES (17,NULL,NULL,'2013-02-20 11:22:37','2013-02-20 11:24:29'),(18,19,NULL,'2013-02-20 11:50:10','2013-02-20 11:51:10'),(19,20,18,'2013-02-20 11:51:16','2013-02-20 11:51:19'),(20,21,19,'2013-02-20 11:51:21','2013-02-20 11:51:24'),(21,22,20,'2013-02-20 11:51:25','2013-02-20 11:51:26'),(22,23,21,'2013-02-20 11:51:27','2013-02-20 11:51:28'),(23,24,22,'2013-02-20 11:51:29','2013-02-20 11:51:30'),(24,25,23,'2013-02-20 11:51:31','2013-02-20 11:51:32'),(25,26,24,'2013-02-20 11:51:34','2013-02-20 11:51:35'),(26,27,25,'2013-02-20 11:51:36','2013-02-20 11:51:37'),(27,28,26,'2013-02-20 11:51:38','2013-02-20 11:51:39'),(28,29,27,'2013-02-20 11:51:41','2013-02-20 11:51:42'),(29,30,28,'2013-02-20 11:51:44','2013-02-20 11:51:45'),(30,31,29,'2013-02-20 11:51:46','2013-02-20 11:51:46'),(31,32,30,'2013-02-20 11:51:48','2013-02-20 11:51:49'),(32,33,31,'2013-02-20 11:51:50','2013-02-20 11:51:51'),(33,34,32,'2013-02-20 11:53:32','2013-02-20 11:53:34'),(34,35,33,'2013-02-20 11:53:35','2013-02-20 11:53:37'),(35,NULL,34,'2013-02-20 11:53:38','2013-02-20 11:53:39'),(36,37,NULL,'2013-02-20 12:31:04','2013-02-20 12:31:12'),(37,38,36,'2013-02-20 12:31:15','2013-02-20 12:31:17'),(38,39,37,'2013-02-20 12:31:18','2013-02-20 12:31:21'),(39,40,38,'2013-02-20 12:31:22','2013-02-20 12:31:23'),(40,41,39,'2013-02-20 12:31:24','2013-02-20 12:31:29'),(41,42,40,'2013-02-20 12:34:59','2013-02-20 12:35:06'),(42,43,41,'2013-02-20 12:35:07','2013-02-20 12:35:08'),(43,44,42,'2013-02-20 12:35:09','2013-02-20 12:35:10'),(44,45,43,'2013-02-20 12:35:11','2013-02-20 12:35:12'),(45,46,44,'2013-02-20 12:35:13','2013-02-20 12:35:14'),(46,47,45,'2013-02-20 12:35:15','2013-02-20 12:35:16'),(47,48,46,'2013-02-20 12:35:17','2013-02-20 12:35:52'),(48,49,47,'2013-02-20 12:35:53','2013-02-20 12:35:55'),(49,50,48,'2013-02-20 12:35:56','2013-02-20 12:35:56'),(50,51,49,'2013-02-20 12:35:57','2013-02-20 12:35:58'),(51,52,50,'2013-02-20 12:35:59','2013-02-20 12:36:00'),(52,53,51,'2013-02-20 12:36:02','2013-02-20 12:36:04'),(53,54,52,'2013-02-20 12:36:06','2013-02-20 12:36:07'),(54,55,53,'2013-02-20 12:36:08','2013-02-20 12:36:09'),(55,56,54,'2013-02-20 12:36:10','2013-02-20 12:36:12'),(56,57,55,'2013-02-20 12:36:13','2013-02-20 12:36:14'),(57,NULL,56,'2013-02-20 12:36:17','2013-02-20 12:36:54'),(58,NULL,NULL,'2013-02-20 12:40:34','2013-02-20 12:41:34'),(59,NULL,NULL,'2013-02-20 14:58:14','2013-02-20 14:58:22'),(60,61,NULL,'2013-02-20 14:58:26','2013-02-20 14:58:27'),(61,NULL,60,'2013-02-20 14:58:28','2013-02-20 14:58:31'),(62,NULL,NULL,'2013-02-20 14:59:01','2013-02-20 14:59:13'),(63,64,NULL,'2013-02-20 14:59:26','2013-02-20 14:59:39'),(64,NULL,63,'2013-02-20 14:59:41','2013-02-20 14:59:42'),(65,NULL,NULL,'2013-02-20 14:59:44','2013-02-20 14:59:46'),(66,NULL,NULL,'2013-02-20 15:00:19',NULL),(67,NULL,NULL,'2013-02-21 07:30:14','2013-02-21 07:31:14'),(68,NULL,NULL,'2013-02-21 07:47:21','2013-02-21 07:48:21'),(69,NULL,NULL,'2013-02-21 07:54:19','2013-02-21 07:55:19'),(70,NULL,NULL,'2013-02-21 08:02:11','2013-02-21 08:03:11'),(71,72,NULL,'2013-02-21 12:31:12','2013-02-21 12:31:17'),(72,73,71,'2013-02-21 12:31:19','2013-02-21 12:31:26'),(73,74,72,'2013-02-21 12:31:29','2013-02-21 12:31:31'),(74,75,73,'2013-02-21 12:31:59','2013-02-21 12:32:03'),(75,76,74,'2013-02-21 12:32:06','2013-02-21 12:32:07'),(76,77,75,'2013-02-21 12:32:14','2013-02-21 12:32:15'),(77,78,76,'2013-02-21 12:32:16','2013-02-21 12:32:20'),(78,79,77,'2013-02-21 12:32:20','2013-02-21 12:32:22'),(79,80,78,'2013-02-21 12:32:23','2013-02-21 12:32:23'),(80,81,79,'2013-02-21 12:32:24','2013-02-21 12:32:25'),(81,82,80,'2013-02-21 12:32:25','2013-02-21 12:32:26'),(82,83,81,'2013-02-21 13:29:06','2013-02-21 13:29:10'),(83,84,82,'2013-02-21 13:29:14','2013-02-21 13:29:16'),(84,85,83,'2013-02-21 13:31:00','2013-02-21 13:31:03'),(85,86,84,'2013-02-21 13:31:06','2013-02-21 13:31:25'),(86,87,85,'2013-02-21 13:31:28','2013-02-21 13:32:35'),(87,NULL,86,'2013-02-21 13:32:42','2013-02-21 13:32:47'),(88,NULL,NULL,'2013-02-21 13:33:11','2013-02-21 13:34:11'),(89,NULL,NULL,'2013-02-21 14:35:28','2013-02-21 14:35:58');
/*!40000 ALTER TABLE `user_task_time_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('admin','1',1),('user1','1',1),('user2','1',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-02-21 20:49:28
