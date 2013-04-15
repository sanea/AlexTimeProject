-- MySQL dump 10.13  Distrib 5.5.24, for Win64 (x86)
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
-- Current Database: `webapp`
--

/*!40000 DROP DATABASE IF EXISTS `webapp`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `webapp` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `webapp`;

--
-- Table structure for table `config_custom_action`
--

DROP TABLE IF EXISTS `config_custom_action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_custom_action` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_custom_action`
--

LOCK TABLES `config_custom_action` WRITE;
/*!40000 ALTER TABLE `config_custom_action` DISABLE KEYS */;
INSERT INTO `config_custom_action` VALUES (1,'custom1',1),(2,'custom2',1),(3,'custom3',1);
/*!40000 ALTER TABLE `config_custom_action` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_authorities`
--

LOCK TABLES `group_authorities` WRITE;
/*!40000 ALTER TABLE `group_authorities` DISABLE KEYS */;
INSERT INTO `group_authorities` VALUES (2,1,'EDIT_USERS'),(3,1,'MANAGE_TASK'),(4,1,'EDIT_TASKS'),(5,1,'STAT_ONLINE'),(8,2,'MANAGE_TASK'),(9,1,'EDIT_GROUPS'),(10,1,'STAT_ALL'),(11,1,'EDIT_SITES'),(12,1,'ASSIGN_TASKS'),(25,6,'EDIT_USERS'),(26,6,'MANAGE_TASK'),(27,6,'EDIT_TASKS'),(28,6,'STAT_ONLINE'),(29,6,'STAT_ALL'),(30,6,'EDIT_GROUPS'),(31,6,'EDIT_SITES'),(32,6,'ASSIGN_TASKS'),(33,1,'CHANGE_CUSTOM');
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
  `username` varchar(50) BINARY NOT NULL,
  `group_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_members_username` (`username`,`group_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `idx_members_group_id` (`group_id`),
  CONSTRAINT `fk_members_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `fk_members_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_members`
--

LOCK TABLES `group_members` WRITE;
/*!40000 ALTER TABLE `group_members` DISABLE KEYS */;
INSERT INTO `group_members` VALUES (5,'1',1),(1,'admin',1),(19,'Васиостровская оператор',2),(18,'Васиостровская оператор2',2),(13,'Гороховая оператор',2),(14,'Гороховая оператор2',2),(15,'Декабристов оператор',2),(16,'Декабристов оператор2',2),(22,'Елизаровская оператор',2),(23,'Елизаровская оператор2',2),(20,'Московская оператор',2),(21,'Московская оператор2',2),(9,'Озерки1 оператор',2),(10,'Озерки1 оператор2',2),(11,'Озерки2 оператор',2),(12,'Озерки2 оператор2',2);
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
  `group_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (1,'admin'),(2,'operator'),(6,'test');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site`
--

LOCK TABLES `site` WRITE;
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` VALUES (1,'Озерки','','','',0),(2,'Озерки2','','','',0),(3,'Елизаровская ','','','',0),(7,'Декабристов','','','',0),(8,'Московский','-','-','-',0),(9,'Гороховая','-','-','-',0),(10,'Василеостровская','-','-','-',0);
/*!40000 ALTER TABLE `site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_task`
--

DROP TABLE IF EXISTS `site_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `site_id` bigint(20) unsigned NOT NULL,
  `task_id` bigint(20) unsigned NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_site_task` (`site_id`,`task_id`),
  KEY `fk_site_task_idx1` (`site_id`),
  KEY `fk_site_task_idx2` (`task_id`),
  CONSTRAINT `fk_site_task1` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`),
  CONSTRAINT `fk_site_task2` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_task`
--

LOCK TABLES `site_task` WRITE;
/*!40000 ALTER TABLE `site_task` DISABLE KEYS */;
INSERT INTO `site_task` VALUES (1,1,1,0),(2,1,2,0),(3,1,11,0),(6,1,13,0),(7,1,16,0),(8,1,20,0),(9,3,1,0),(11,3,20,0),(13,1,22,0),(14,2,23,0),(15,2,24,0),(16,3,25,0),(31,1,21,0),(33,1,23,0);
/*!40000 ALTER TABLE `site_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` char(1) NOT NULL,
  `price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `income` tinyint(1) NOT NULL DEFAULT '1',
  `repeat_task` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'Нат1','p',1100.00,1,0,1,0),(2,'Печать1_removed','t',250.00,1,1,1,NULL),(11,'Списание часов 2_removed','p',100.22,1,1,1,NULL),(13,'Танцы_removed','t',1232.22,1,1,1,NULL),(16,'час_removed','c',100.00,1,1,1,0),(20,'Охранаа_removed','p',11.00,1,1,0,0),(21,'Окс2','p',1000.00,1,0,1,NULL),(22,'Женя','p',1100.00,1,0,1,NULL),(23,'нат2','p',1100.00,1,0,1,NULL),(24,'женя2','p',1100.00,1,0,1,NULL),(25,'тест2','p',1100.00,1,0,1,NULL),(26,'тест2','p',1100.00,1,0,1,NULL);
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
  `params` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TASK_TIME_idx` (`task_time_id`),
  CONSTRAINT `FK_TASK_TIME` FOREIGN KEY (`task_time_id`) REFERENCES `user_task_time` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_action`
--

LOCK TABLES `user_action` WRITE;
/*!40000 ALTER TABLE `user_action` DISABLE KEYS */;
INSERT INTO `user_action` VALUES (1,1,'2013-04-01 07:20:57','r',NULL),(2,1,'2013-04-01 07:21:01','1',NULL),(3,1,'2013-04-01 07:21:05','e',NULL),(4,1,'2013-04-01 07:21:08','c',NULL),(5,1,'2013-04-01 07:21:11','3',NULL),(6,1,'2013-04-01 07:21:12','c',NULL),(7,1,'2013-04-01 07:21:17','s',NULL),(8,2,'2013-04-01 07:21:24','r',NULL),(9,3,'2013-04-01 07:21:26','r',NULL),(10,4,'2013-04-01 07:21:28','r',NULL),(11,5,'2013-04-01 07:21:30','r',NULL),(12,6,'2013-04-01 07:21:31','r',NULL),(13,7,'2013-04-01 07:21:36','r',NULL),(14,8,'2013-04-01 07:21:40','r',NULL),(15,9,'2013-04-01 07:22:18','r',NULL),(16,10,'2013-04-01 07:22:20','r',NULL),(17,3,'2013-04-01 07:23:26','f',NULL),(18,4,'2013-04-01 07:23:28','f',NULL),(19,2,'2013-04-01 07:33:24','f',NULL),(20,11,'2013-04-01 07:48:35','r',NULL),(21,9,'2013-04-01 07:52:18','f',NULL),(22,10,'2013-04-01 07:52:20','f',NULL),(23,11,'2013-04-01 08:48:35','f',NULL),(24,12,'2013-04-01 12:10:20','r',NULL),(25,13,'2013-04-01 12:10:23','r',NULL),(26,13,'2013-04-01 12:10:25','2',NULL),(27,12,'2013-04-01 12:11:20','f',NULL),(28,13,'2013-04-01 12:11:25','c',NULL),(29,13,'2013-04-01 12:12:23','f',NULL),(30,14,'2013-04-02 08:52:00','r',NULL),(31,14,'2013-04-02 09:52:00','f',NULL),(32,15,'2013-04-03 11:44:37','r',NULL),(33,15,'2013-04-03 11:44:39','2',NULL),(34,15,'2013-04-03 11:44:42','e',NULL),(35,15,'2013-04-03 11:44:45','c',NULL),(36,15,'2013-04-03 11:44:48','3',NULL),(37,15,'2013-04-03 11:44:53','c',NULL),(38,15,'2013-04-03 11:44:57','2',NULL),(39,15,'2013-04-03 11:45:00','c',NULL),(40,15,'2013-04-03 11:45:03','s',NULL),(41,16,'2013-04-10 08:25:36','r',NULL),(42,17,'2013-04-10 08:31:11','r',NULL),(43,16,'2013-04-10 09:25:36','f',NULL),(44,17,'2013-04-10 09:31:11','f',NULL),(45,18,'2013-04-10 09:42:51','r',NULL),(46,19,'2013-04-10 09:42:54','r',NULL),(47,18,'2013-04-10 10:42:51','f',NULL),(48,19,'2013-04-10 10:42:54','f',NULL);
/*!40000 ALTER TABLE `user_action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_change`
--

DROP TABLE IF EXISTS `user_change`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_change` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` timestamp NULL DEFAULT NULL,
  `site_id` bigint(20) unsigned NOT NULL,
  `username` varchar(50) BINARY NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_change_site_idx` (`site_id`),
  KEY `fk_user_change_user_idx` (`username`),
  CONSTRAINT `fk_user_change_site` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`),
  CONSTRAINT `fk_user_change_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_change`
--

LOCK TABLES `user_change` WRITE;
/*!40000 ALTER TABLE `user_change` DISABLE KEYS */;
INSERT INTO `user_change` VALUES (17,'2013-03-30 18:42:51','2013-03-30 18:42:53',1,'admin'),(18,'2013-03-30 18:42:55','2013-03-30 18:42:58',2,'admin'),(19,'2013-03-30 18:43:00','2013-04-10 07:29:59',1,'admin'),(20,'2013-04-01 07:22:13','2013-04-01 12:13:43',3,'1'),(21,'2013-04-10 07:30:02',NULL,1,'admin'),(22,'2013-04-10 08:25:28',NULL,1,'Озерки1 оператор');
/*!40000 ALTER TABLE `user_change` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_site_task`
--

DROP TABLE IF EXISTS `user_site_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_site_task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) BINARY NOT NULL,
  `site_task_id` bigint(20) unsigned NOT NULL,
  `status` char(1) NOT NULL DEFAULT 'u',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `current_time` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_taks` (`username`,`site_task_id`),
  UNIQUE KEY `current_time_UNIQUE` (`current_time`),
  KEY `fk_user_task_cur_time_idx` (`current_time`),
  KEY `fk_user_task_site_task_idx` (`site_task_id`),
  CONSTRAINT `fk_user_task_cur_time` FOREIGN KEY (`current_time`) REFERENCES `user_task_time` (`id`),
  CONSTRAINT `fk_user_task_site_task` FOREIGN KEY (`site_task_id`) REFERENCES `site_task` (`id`),
  CONSTRAINT `fk_user_task_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_site_task`
--

LOCK TABLES `user_site_task` WRITE;
/*!40000 ALTER TABLE `user_site_task` DISABLE KEYS */;
INSERT INTO `user_site_task` VALUES (1,'admin',1,'c','2013-04-15 09:36:22','2013-03-30 09:14:55',0,NULL),(2,'admin',2,'c','2013-04-01 07:21:30','2013-03-30 09:14:58',0,NULL),(3,'admin',3,'c','2013-04-01 07:23:28','2013-03-30 09:15:02',0,NULL),(6,'admin',6,'c','2013-04-01 07:21:31','2013-03-30 17:35:32',0,NULL),(7,'admin',7,'c','2013-04-01 07:21:40','2013-03-30 17:35:39',0,NULL),(8,'admin',8,'s','2013-04-03 11:45:03','2013-03-30 17:35:41',0,NULL),(9,'1',9,'c','2013-04-01 12:11:20','2013-04-01 07:22:02',0,NULL),(11,'1',11,'c','2013-04-01 12:12:23','2013-04-01 07:22:09',0,NULL),(12,'Озерки1 оператор',1,'c','2013-04-15 09:36:25','2013-04-10 08:20:36',0,NULL),(16,'Озерки1 оператор',31,'c','2013-04-10 10:42:51','2013-04-10 08:24:44',0,NULL),(17,'1',1,'u','2013-04-15 09:36:22','2013-04-15 09:22:24',0,NULL),(18,'Васиостровская оператор',1,'u','2013-04-15 09:22:25','2013-04-15 09:22:25',1,NULL);
/*!40000 ALTER TABLE `user_site_task` ENABLE KEYS */;
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
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `finish_time` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `finish_time_play` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `finish_time_custom1` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `finish_time_custom2` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `finish_time_custom3` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `time_seq_id` bigint(20) unsigned DEFAULT NULL,
  `duration_play` int(11) NOT NULL DEFAULT '0',
  `duration_custom1` int(11) DEFAULT NULL,
  `duration_custom2` int(11) DEFAULT NULL,
  `duration_custom3` int(11) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `user_change_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `time_seq_id_UNIQUE` (`time_seq_id`),
  KEY `fk_user_task_idx` (`user_task_id`),
  KEY `FK_TIME_SEQ_idx` (`time_seq_id`),
  KEY `fk_time_change_idx` (`user_change_id`),
  KEY `fk_user_change_idx` (`user_change_id`),
  CONSTRAINT `fk_task_time_user_change` FOREIGN KEY (`user_change_id`) REFERENCES `user_change` (`id`),
  CONSTRAINT `FK_TIME_SEQ` FOREIGN KEY (`time_seq_id`) REFERENCES `user_task_time_seq` (`id`),
  CONSTRAINT `fk_time_task` FOREIGN KEY (`user_task_id`) REFERENCES `user_site_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task_time`
--

LOCK TABLES `user_task_time` WRITE;
/*!40000 ALTER TABLE `user_task_time` DISABLE KEYS */;
INSERT INTO `user_task_time` VALUES (1,1,'2013-04-01 07:20:57','2013-04-01 07:21:17','2013-04-01 07:22:05','2013-04-01 07:23:01',NULL,'2013-04-01 07:22:11',1,12,7,NULL,1,100.00,0.33,19),(2,1,'2013-04-01 07:21:24','2013-04-01 07:33:24','2013-04-01 07:33:24',NULL,NULL,NULL,6,720,NULL,NULL,NULL,100.00,20.00,19),(3,8,'2013-04-01 07:21:26','2013-04-01 07:23:26','2013-04-01 07:23:26',NULL,NULL,NULL,7,120,NULL,NULL,NULL,11.00,0.37,19),(4,3,'2013-04-01 07:21:28','2013-04-01 07:23:28','2013-04-01 07:23:28',NULL,NULL,NULL,8,120,NULL,NULL,NULL,100.22,3.34,19),(5,2,'2013-04-01 07:21:30','2013-04-01 07:21:30','2013-04-01 07:21:30',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,19),(6,6,'2013-04-01 07:21:31','2013-04-01 07:21:31','2013-04-01 07:21:31',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,1232.22,1232.22,19),(7,7,'2013-04-01 07:21:36','2013-04-01 07:21:36','2013-04-01 07:21:36',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,123.00,123.00,19),(8,7,'2013-04-01 07:21:40','2013-04-01 07:21:40','2013-04-01 07:21:40',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,333.00,333.00,19),(9,11,'2013-04-01 07:22:18','2013-04-01 07:52:18','2013-04-01 07:52:18',NULL,NULL,NULL,9,1800,NULL,NULL,NULL,11.00,5.50,20),(10,9,'2013-04-01 07:22:20','2013-04-01 07:52:20','2013-04-01 07:52:20',NULL,NULL,NULL,10,1800,NULL,NULL,NULL,100.00,50.00,20),(11,1,'2013-04-01 07:48:35','2013-04-01 08:48:35','2013-04-01 08:48:35',NULL,NULL,NULL,11,3600,NULL,NULL,NULL,100.00,100.00,19),(12,9,'2013-04-01 12:10:20','2013-04-01 12:11:20','2013-04-01 12:11:20',NULL,NULL,NULL,12,60,NULL,NULL,NULL,1000.00,16.67,20),(13,11,'2013-04-01 12:10:23','2013-04-01 12:12:23','2013-04-01 12:12:23',NULL,'2013-04-01 12:11:25',NULL,13,60,NULL,60,NULL,11.00,0.18,20),(14,1,'2013-04-02 08:52:00','2013-04-02 09:52:00','2013-04-02 09:52:00',NULL,NULL,NULL,16,3600,NULL,NULL,NULL,1000.00,1000.00,19),(15,8,'2013-04-03 11:44:37','2013-04-03 11:45:03','2013-04-03 11:45:51',NULL,'2013-04-03 11:46:57','2013-04-03 11:46:48',17,12,NULL,9,5,11.00,0.04,19),(16,12,'2013-04-10 08:25:36','2013-04-10 09:25:36','2013-04-10 09:25:36',NULL,NULL,NULL,24,3600,NULL,NULL,NULL,1100.00,1100.00,22),(17,16,'2013-04-10 08:31:11','2013-04-10 09:31:11','2013-04-10 09:31:11',NULL,NULL,NULL,25,3600,NULL,NULL,NULL,1000.00,1000.00,22),(18,16,'2013-04-10 09:42:51','2013-04-10 10:42:51','2013-04-10 10:42:51',NULL,NULL,NULL,26,3600,NULL,NULL,NULL,1000.00,1000.00,22),(19,12,'2013-04-10 09:42:54','2013-04-10 10:42:54','2013-04-10 10:42:54',NULL,NULL,NULL,27,3600,NULL,NULL,NULL,1100.00,1100.00,22);
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
  `prev_id` bigint(20) unsigned DEFAULT NULL,
  `next_id` bigint(20) unsigned DEFAULT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` timestamp NULL DEFAULT NULL,
  `task_status` char(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_time_seq_prev_idx` (`prev_id`),
  KEY `fk_time_seq_next_idx` (`next_id`),
  CONSTRAINT `fk_time_seq_next` FOREIGN KEY (`next_id`) REFERENCES `user_task_time_seq` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_time_seq_prev` FOREIGN KEY (`prev_id`) REFERENCES `user_task_time_seq` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task_time_seq`
--

LOCK TABLES `user_task_time_seq` WRITE;
/*!40000 ALTER TABLE `user_task_time_seq` DISABLE KEYS */;
INSERT INTO `user_task_time_seq` VALUES (1,NULL,2,'2013-04-01 07:20:57','2013-04-01 07:21:01','r'),(2,1,3,'2013-04-01 07:21:01','2013-04-01 07:21:08','1'),(3,2,4,'2013-04-01 07:21:08','2013-04-01 07:21:11','r'),(4,3,5,'2013-04-01 07:21:11','2013-04-01 07:21:12','3'),(5,4,NULL,'2013-04-01 07:21:12','2013-04-01 07:21:17','r'),(6,NULL,NULL,'2013-04-01 07:21:24','2013-04-01 07:33:24','r'),(7,NULL,NULL,'2013-04-01 07:21:26','2013-04-01 07:23:26','r'),(8,NULL,NULL,'2013-04-01 07:21:28','2013-04-01 07:23:28','r'),(9,NULL,NULL,'2013-04-01 07:22:18','2013-04-01 07:52:18','r'),(10,NULL,NULL,'2013-04-01 07:22:20','2013-04-01 07:52:20','r'),(11,NULL,NULL,'2013-04-01 07:48:35','2013-04-01 08:48:35','r'),(12,NULL,NULL,'2013-04-01 12:10:20','2013-04-01 12:11:20','r'),(13,NULL,14,'2013-04-01 12:10:23','2013-04-01 12:10:25','r'),(14,13,15,'2013-04-01 12:10:25','2013-04-01 12:11:25','2'),(15,14,NULL,'2013-04-01 12:11:25','2013-04-01 12:12:23','r'),(16,NULL,NULL,'2013-04-02 08:52:00','2013-04-02 09:52:00','r'),(17,NULL,18,'2013-04-03 11:44:37','2013-04-03 11:44:39','r'),(18,17,19,'2013-04-03 11:44:39','2013-04-03 11:44:45','2'),(19,18,20,'2013-04-03 11:44:45','2013-04-03 11:44:48','r'),(20,19,21,'2013-04-03 11:44:48','2013-04-03 11:44:53','3'),(21,20,22,'2013-04-03 11:44:53','2013-04-03 11:44:57','r'),(22,21,23,'2013-04-03 11:44:57','2013-04-03 11:45:00','2'),(23,22,NULL,'2013-04-03 11:45:00','2013-04-03 11:45:03','r'),(24,NULL,NULL,'2013-04-10 08:25:36','2013-04-10 09:25:36','r'),(25,NULL,NULL,'2013-04-10 08:31:11','2013-04-10 09:31:11','r'),(26,NULL,NULL,'2013-04-10 09:42:51','2013-04-10 10:42:51','r'),(27,NULL,NULL,'2013-04-10 09:42:54','2013-04-10 10:42:54','r');
/*!40000 ALTER TABLE `user_task_time_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(50) BINARY NOT NULL,
  `password` varchar(80) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `phone1` varchar(20) DEFAULT NULL,
  `phone2` varchar(20) DEFAULT NULL,
  `e_mail` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `current_change` bigint(20) unsigned DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`username`),
  UNIQUE KEY `uq_user_change` (`current_change`),
  KEY `fk_user_change_idx` (`current_change`),
  CONSTRAINT `fk_user_change` FOREIGN KEY (`current_change`) REFERENCES `user_change` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('1','b5dfdbb15151b2b6db8f313236e967d8ef9a1654f7aa7bca1ee29397dbc3da27a092cf9f8d83886f',1,'','','','','','','','','',NULL,0),('admin','db9fb416a8d325e4e439a4758f35fe8ccd8213d89aed51181eb8cb3aac39bf467b06e8326d36147a',1,'','','','','(123) 333-3333',NULL,'','','23',21,0),('test','6887c45e2666bfd1b45a340a124880a0f4edf2332c6b197c93f90961e30360e73d6ad62deaf65cdd',0,'','','','','','','','','',NULL,0),('Васиостровская оператор','f65346713b5af483f8ff78d37b47ef8082ee28024b9ca74d6dcfa2880a44114d9bbcde4bbbfee942',1,'','','','','','','','','',NULL,0),('Васиостровская оператор2','73c0ccbef54c5882308e9949d9d23a9b2d155018e969bc9ce1f07f5045191dedef75394250557a2d',1,'','','','','','','','','',NULL,0),('Гороховая оператор','79a33219770e0ab738b8187d94bf5cd98624c4a79d80c4d3b366f55e8f6fe8c4bd6c9b5223897e6b',1,'','','','','','','','','',NULL,0),('Гороховая оператор2','123',1,'','','','','','','','','',NULL,0),('Декабристов оператор','ed037aa7f2636f65edfe9b41d42f33f08de0294f5363c6febedfcbc1689d9acea77e077d2c695b99',1,'','','','','','','','','',NULL,0),('Декабристов оператор2','4d43d216582acaf4a8fe22153d528d6e024387dc80cac496531dd9473b26e7dc3b5b46328cdc06c0',1,'','','','','','','','','',NULL,0),('Елизаровская оператор','fa601f9f01c74ac60bdbb92ea94bdeee89c26e8493b1a580eaea81f33c518266bb69b58bdf919136',1,'','','','','','','','','',NULL,0),('Елизаровская оператор2','de429806c20d777d8d442ec76afaff19d96816e616002b4021abba00f268122db15bf0e7159993a6',1,'','','','','','','','','',NULL,0),('Московская оператор','3805c0a90059eeda0759793cd97c04033bed2b6716d6c32dffbcc90ca7d44642faa00cab82de11f4',1,'','','','','','','','','',NULL,0),('Московская оператор2','123',1,'','','','','','','','','',NULL,0),('Озерки1 оператор','d55638b06a0689aa39360c7b8f1b22c55ee015955c3b81383c500c045d85f74da7d93c777287f37e',1,'','','','','','','','','',22,0),('Озерки1 оператор2','85bee5ca7b9b12199e745426c5a307caef1350340b1d21ab0111d077fa55a0a33749de715a6c8591',1,'','','','','','','','','',NULL,0),('Озерки2 оператор','05990fb93baab3523e80dc317abb9df05de10ce84716957ff34e658971fababb024dfc1a7f5db075',1,'','','','','','','','','',NULL,0),('Озерки2 оператор2','479ce2852d60e7baf71b174b7ebcc20e11cead507bc01949f28db74343c49e8aad29f527e80bd72d',1,'','','','','','','','','',NULL,0);
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

-- Dump completed on 2013-04-15 14:06:58
