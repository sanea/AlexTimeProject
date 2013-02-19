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
  `task_type` int(1) NOT NULL,
  `task_price` decimal(10,2) NOT NULL,
  `task_enabled` char(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'Списание Часов',1,100.00,''),(2,'Печать',2,250.00,'');
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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_action`
--

LOCK TABLES `user_action` WRITE;
/*!40000 ALTER TABLE `user_action` DISABLE KEYS */;
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
INSERT INTO `user_task` VALUES (1,'user1',1,'u','2013-02-18 13:16:24','2013-02-18 13:16:31'),(2,'user1',2,'u','2013-02-18 13:16:24','2013-02-18 13:16:31'),(3,'user2',1,'u','2013-02-18 13:16:24','2013-02-18 13:16:31');
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
  `time_spent` int(11) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;
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
  `next_id` bigint(20) unsigned DEFAULT NULL,
  `prev_id` bigint(20) unsigned DEFAULT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TIME_SEQ_NEXT_idx` (`next_id`),
  KEY `FK_TIME_SEQ_PREV_idx` (`prev_id`),
  CONSTRAINT `FK_TIME_SEQ_NEXT` FOREIGN KEY (`next_id`) REFERENCES `user_task_time_seq` (`id`),
  CONSTRAINT `FK_TIME_SEQ_PREV` FOREIGN KEY (`prev_id`) REFERENCES `user_task_time_seq` (`id`)
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

-- Dump completed on 2013-02-19 16:44:14
