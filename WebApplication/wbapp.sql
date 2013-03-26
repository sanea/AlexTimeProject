-- MySQL dump 10.13  Distrib 5.5.25a, for Win64 (x86)
--
-- Host: localhost    Database: webapp
-- ------------------------------------------------------
-- Server version	5.5.25a

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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_authorities`
--

LOCK TABLES `group_authorities` WRITE;
/*!40000 ALTER TABLE `group_authorities` DISABLE KEYS */;
INSERT INTO `group_authorities` VALUES (2,1,'EDIT_USERS'),(3,1,'MANAGE_TASK'),(4,1,'EDIT_TASKS'),(5,1,'STAT_ONLINE'),(8,2,'MANAGE_TASK'),(9,1,'EDIT_GROUPS'),(10,1,'STAT_ALL'),(11,1,'EDIT_SITES'),(12,1,'ASSIGN_TASKS'),(25,6,'EDIT_USERS'),(26,6,'MANAGE_TASK'),(27,6,'EDIT_TASKS'),(28,6,'STAT_ONLINE'),(29,6,'STAT_ALL'),(30,6,'EDIT_GROUPS'),(31,6,'EDIT_SITES'),(32,6,'ASSIGN_TASKS');
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
  UNIQUE KEY `uq_members_username` (`username`,`group_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `idx_members_group_id` (`group_id`),
  CONSTRAINT `fk_members_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `fk_members_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_members`
--

LOCK TABLES `group_members` WRITE;
/*!40000 ALTER TABLE `group_members` DISABLE KEYS */;
INSERT INTO `group_members` VALUES (5,'1',6),(1,'admin',1),(9,'testde',1),(2,'user1',2),(3,'user2',2),(8,'user3',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site`
--

LOCK TABLES `site` WRITE;
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` VALUES (1,'площадка 1',NULL,NULL,NULL,0),(2,'площадка 2','','','',0),(3,'площадка 3','','','',0),(4,'123_removed','','','',1),(5,'133_removed','','','',1),(6,'d_removed','','','',0);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_task`
--

LOCK TABLES `site_task` WRITE;
/*!40000 ALTER TABLE `site_task` DISABLE KEYS */;
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
  `repeat` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'Списание Часов','p',100.00,1,0,1,NULL),(2,'Печать1','t',250.00,1,0,1,NULL),(11,'Списание часов 2','p',100.22,1,0,1,NULL),(13,'Танцы','t',1232.22,1,0,1,NULL),(16,'час','p',100.00,1,0,1,NULL),(20,'Охрана','p',1.00,1,0,0,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_action`
--

LOCK TABLES `user_action` WRITE;
/*!40000 ALTER TABLE `user_action` DISABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_change`
--

LOCK TABLES `user_change` WRITE;
/*!40000 ALTER TABLE `user_change` DISABLE KEYS */;
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
  `username` varchar(50) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_site_task`
--

LOCK TABLES `user_site_task` WRITE;
/*!40000 ALTER TABLE `user_site_task` DISABLE KEYS */;
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
  `finish_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `time_seq_id` bigint(20) unsigned DEFAULT NULL,
  `duration_play` int(11) NOT NULL DEFAULT '0',
  `duration_custom1` int(11) DEFAULT NULL,
  `duration_custom2` int(11) DEFAULT NULL,
  `duration_custom3` int(11) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `total` decimal(10,2) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task_time`
--

LOCK TABLES `user_task_time` WRITE;
/*!40000 ALTER TABLE `user_task_time` DISABLE KEYS */;
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
  `type` char(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_time_seq_prev_idx` (`prev_id`),
  KEY `fk_time_seq_next_idx` (`next_id`),
  CONSTRAINT `fk_time_seq_next` FOREIGN KEY (`next_id`) REFERENCES `user_task_time_seq` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_time_seq_prev` FOREIGN KEY (`prev_id`) REFERENCES `user_task_time_seq` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task_time_seq`
--

LOCK TABLES `user_task_time_seq` WRITE;
/*!40000 ALTER TABLE `user_task_time_seq` DISABLE KEYS */;
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
INSERT INTO `users` VALUES ('1','1',1,'','','','','','','','','',NULL,0),('admin','1',1,'','','','','(123) 333-3333',NULL,'','','23',NULL,0),('asd','1',1,'','','','','','','','','',NULL,0),('test','1',0,'','','','','','','','','',NULL,1),('testde','1',0,'','','','','','','','','',NULL,1),('user1','1',1,'','','','','(111) 111-1111',NULL,'','','',NULL,0),('user2','1',1,'','','','','+7(432) 234-2342',NULL,'','','',NULL,0),('user3','1',1,'','','','','','a@a.com','','','',NULL,0);
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

-- Dump completed on 2013-03-26 21:49:33
