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
  `name_en` varchar(50) NOT NULL,
  `name_ru` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_custom_action`
--

LOCK TABLES `config_custom_action` WRITE;
/*!40000 ALTER TABLE `config_custom_action` DISABLE KEYS */;
INSERT INTO `config_custom_action` VALUES (1,'custom11','кастомное1',1),(2,'custom22','кастомное2',0),(3,'custom33','кастомное3',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_authorities`
--

LOCK TABLES `group_authorities` WRITE;
/*!40000 ALTER TABLE `group_authorities` DISABLE KEYS */;
INSERT INTO `group_authorities` VALUES (2,1,'EDIT_USERS'),(3,1,'MANAGE_TASK'),(4,1,'EDIT_TASKS'),(5,1,'STAT_ONLINE'),(8,2,'MANAGE_TASK'),(9,1,'EDIT_GROUPS'),(10,1,'STAT_ALL'),(11,1,'EDIT_SITES'),(12,1,'ASSIGN_TASKS'),(25,6,'EDIT_USERS'),(26,6,'MANAGE_TASK'),(27,6,'EDIT_TASKS'),(28,6,'STAT_ONLINE'),(29,6,'STAT_ALL'),(30,6,'EDIT_GROUPS'),(31,6,'EDIT_SITES'),(32,6,'ASSIGN_TASKS'),(33,1,'CHANGE_CUSTOM'),(34,1,'CANCEL_TIME'),(35,1,'EDIT_USER_ADMIN');
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
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_task`
--

LOCK TABLES `site_task` WRITE;
/*!40000 ALTER TABLE `site_task` DISABLE KEYS */;
INSERT INTO `site_task` VALUES (1,1,1,0),(2,1,2,0),(3,1,11,0),(6,1,13,0),(7,1,16,0),(8,1,20,0),(9,3,1,0),(11,3,20,0),(13,1,22,0),(16,3,25,0),(31,1,21,0),(36,1,24,0),(39,1,23,0),(40,2,1,0),(41,2,21,0),(42,2,22,0),(43,2,23,0),(44,2,24,0),(45,10,26,0),(46,10,25,0);
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
INSERT INTO `task` VALUES (1,'Нат1','p',1100.00,1,0,1,0),(2,'Печать1','t',250.00,1,0,1,NULL),(11,'Списание часов 2_removed','p',100.22,1,1,1,NULL),(13,'Танцы_removed','t',1232.22,1,1,1,NULL),(16,'час_removed','c',100.00,1,1,1,0),(20,'Охранаа_removed','p',11.00,1,1,0,0),(21,'Окс2','p',1000.00,1,0,1,NULL),(22,'Женя','p',1100.00,1,0,1,NULL),(23,'нат2','p',1100.00,1,0,1,NULL),(24,'женя2','p',1100.00,1,0,1,NULL),(25,'тест2','p',1100.00,1,0,1,NULL),(26,'тест2','p',1100.00,1,0,1,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_action`
--

LOCK TABLES `user_action` WRITE;
/*!40000 ALTER TABLE `user_action` DISABLE KEYS */;
INSERT INTO `user_action` VALUES (1,1,'2013-04-01 07:20:57','r',NULL),(2,1,'2013-04-01 07:21:01','1',NULL),(3,1,'2013-04-01 07:21:05','e',NULL),(4,1,'2013-04-01 07:21:08','c',NULL),(5,1,'2013-04-01 07:21:11','3',NULL),(6,1,'2013-04-01 07:21:12','c',NULL),(7,1,'2013-04-01 07:21:17','s',NULL),(8,2,'2013-04-01 07:21:24','r',NULL),(9,3,'2013-04-01 07:21:26','r',NULL),(10,4,'2013-04-01 07:21:28','r',NULL),(11,5,'2013-04-01 07:21:30','r',NULL),(12,6,'2013-04-01 07:21:31','r',NULL),(13,7,'2013-04-01 07:21:36','r',NULL),(14,8,'2013-04-01 07:21:40','r',NULL),(15,9,'2013-04-01 07:22:18','r',NULL),(16,10,'2013-04-01 07:22:20','r',NULL),(17,3,'2013-04-01 07:23:26','f',NULL),(18,4,'2013-04-01 07:23:28','f',NULL),(19,2,'2013-04-01 07:33:24','f',NULL),(20,11,'2013-04-01 07:48:35','r',NULL),(21,9,'2013-04-01 07:52:18','f',NULL),(22,10,'2013-04-01 07:52:20','f',NULL),(23,11,'2013-04-01 08:48:35','f',NULL),(24,12,'2013-04-01 12:10:20','r',NULL),(25,13,'2013-04-01 12:10:23','r',NULL),(26,13,'2013-04-01 12:10:25','2',NULL),(27,12,'2013-04-01 12:11:20','f',NULL),(28,13,'2013-04-01 12:11:25','c',NULL),(29,13,'2013-04-01 12:12:23','f',NULL),(30,14,'2013-04-02 08:52:00','r',NULL),(31,14,'2013-04-02 09:52:00','f',NULL),(32,15,'2013-04-03 11:44:37','r',NULL),(33,15,'2013-04-03 11:44:39','2',NULL),(34,15,'2013-04-03 11:44:42','e',NULL),(35,15,'2013-04-03 11:44:45','c',NULL),(36,15,'2013-04-03 11:44:48','3',NULL),(37,15,'2013-04-03 11:44:53','c',NULL),(38,15,'2013-04-03 11:44:57','2',NULL),(39,15,'2013-04-03 11:45:00','c',NULL),(40,15,'2013-04-03 11:45:03','s',NULL),(41,16,'2013-04-10 08:25:36','r',NULL),(42,17,'2013-04-10 08:31:11','r',NULL),(43,16,'2013-04-10 09:25:36','f',NULL),(44,17,'2013-04-10 09:31:11','f',NULL),(45,18,'2013-04-10 09:42:51','r',NULL),(46,19,'2013-04-10 09:42:54','r',NULL),(47,18,'2013-04-10 10:42:51','f',NULL),(48,19,'2013-04-10 10:42:54','f',NULL),(49,20,'2013-04-15 14:03:10','r',NULL),(50,20,'2013-04-15 14:03:16','c',NULL),(51,20,'2013-04-15 14:03:16','f',NULL),(52,21,'2013-04-15 14:03:20','r',NULL),(53,21,'2013-04-15 14:03:24','1',NULL),(54,21,'2013-04-15 14:03:26','c',NULL),(55,21,'2013-04-15 14:03:28','s',NULL),(56,22,'2013-04-18 16:58:53','r',NULL),(57,22,'2013-04-18 17:58:53','f',NULL),(58,23,'2013-04-18 18:43:26','r',NULL),(59,23,'2013-04-18 19:13:26','f',NULL),(60,24,'2013-04-18 20:27:15','r',NULL),(61,24,'2013-04-18 20:57:15','f',NULL),(62,25,'2013-04-19 08:35:28','r',NULL),(63,25,'2013-04-19 08:35:30','1',NULL),(64,25,'2013-04-19 08:35:35','e',NULL),(65,25,'2013-04-19 08:35:37','c',NULL),(66,25,'2013-04-19 08:35:38','s',NULL),(67,26,'2013-04-19 08:36:11','r',NULL),(68,26,'2013-04-19 08:36:13','c',NULL),(69,26,'2013-04-19 08:36:13','f',NULL),(70,26,'2013-04-19 08:36:14','s',NULL),(71,27,'2013-04-19 08:39:09','r',NULL),(72,27,'2013-04-19 08:39:12','s',NULL),(73,28,'2013-04-19 08:39:22','r',NULL),(74,28,'2013-04-19 08:39:36','1',NULL),(75,28,'2013-04-19 08:40:29','e',NULL),(76,28,'2013-04-19 08:51:59','c',NULL),(77,28,'2013-04-19 08:52:00','s',NULL),(78,29,'2013-04-19 08:52:02','r',NULL),(79,29,'2013-04-19 08:52:03','c',NULL),(80,29,'2013-04-19 08:52:03','f',NULL),(81,30,'2013-04-19 08:52:08','r',NULL),(82,30,'2013-04-19 08:52:10','e',NULL),(83,30,'2013-04-19 08:52:11','1',NULL),(84,30,'2013-04-19 08:52:12','c',NULL),(85,30,'2013-04-19 08:52:14','s',NULL),(86,31,'2013-04-20 08:34:50','r',NULL),(87,31,'2013-04-20 09:34:50','f',NULL),(88,32,'2013-04-21 13:39:18','r',NULL),(89,32,'2013-04-21 13:39:29','1',NULL),(90,32,'2013-04-21 13:39:34','c',NULL),(91,32,'2013-04-21 13:39:40','1',NULL),(92,32,'2013-04-21 13:39:41','c',NULL),(93,32,'2013-04-21 13:39:43','s',NULL),(94,33,'2013-04-26 12:52:38','r',NULL),(95,33,'2013-04-26 12:52:41','1',NULL),(96,33,'2013-04-26 12:52:44','c',NULL),(97,33,'2013-04-26 12:52:45','s',NULL),(98,34,'2013-04-26 12:52:55','r',NULL),(99,34,'2013-04-26 12:53:22','s',NULL),(100,35,'2013-04-26 13:03:44','r',NULL),(101,35,'2013-04-26 13:03:51','s',NULL),(102,36,'2013-04-26 13:06:54','r',NULL),(103,36,'2013-04-26 13:06:56','s',NULL),(104,37,'2013-04-26 13:06:58','r',NULL),(105,37,'2013-04-26 13:07:58','f',NULL),(106,38,'2013-04-26 13:09:36','r',NULL),(107,38,'2013-04-26 13:09:37','s',NULL),(108,39,'2013-04-26 13:09:39','r',NULL),(109,39,'2013-04-26 13:09:42','e',NULL),(110,39,'2013-04-26 13:09:46','1',NULL),(111,39,'2013-04-26 13:09:50','c',NULL),(112,39,'2013-04-26 13:09:52','1',NULL),(113,39,'2013-04-26 13:09:54','c',NULL),(114,39,'2013-04-26 13:09:56','e',NULL),(115,39,'2013-04-26 13:09:59','e',NULL),(116,39,'2013-04-26 13:10:01','1',NULL),(117,39,'2013-04-26 13:10:08','c',NULL),(118,39,'2013-04-26 13:10:10','s',NULL),(119,40,'2013-04-26 13:12:34','r',NULL),(120,41,'2013-04-26 13:13:44','r',NULL),(121,42,'2013-04-26 13:13:46','r',NULL),(122,43,'2013-04-26 13:13:47','r',NULL),(123,44,'2013-04-26 13:13:56','r',NULL),(124,45,'2013-04-26 13:14:04','r',NULL),(125,46,'2013-04-26 13:14:05','r',NULL),(126,47,'2013-04-26 13:14:05','r',NULL),(127,48,'2013-04-26 13:14:06','r',NULL),(128,49,'2013-04-26 13:14:06','r',NULL),(129,50,'2013-04-28 19:03:11','r',NULL),(130,50,'2013-04-28 19:03:13','s',NULL),(131,51,'2013-04-28 19:03:25','r',NULL),(132,51,'2013-04-28 19:03:29','e',NULL),(133,51,'2013-04-28 19:03:30','c',NULL),(134,51,'2013-04-28 19:03:30','f',NULL),(135,52,'2013-04-28 19:08:29','r',NULL),(136,53,'2013-04-28 19:08:33','r',NULL),(137,54,'2013-04-28 19:08:34','r',NULL),(138,55,'2013-04-28 19:08:36','r',NULL),(139,56,'2013-04-28 19:08:38','r',NULL),(140,52,'2013-04-28 19:38:29','f',NULL),(141,53,'2013-04-28 19:38:33','f',NULL),(142,54,'2013-04-28 19:38:34','f',NULL),(143,55,'2013-04-28 19:38:36','f',NULL),(144,56,'2013-04-28 19:38:38','f',NULL),(145,57,'2013-04-29 07:32:14','r',NULL),(146,58,'2013-04-29 07:33:54','r',NULL),(147,59,'2013-04-29 07:33:59','r',NULL),(148,58,'2013-04-29 07:36:32','s',NULL),(149,59,'2013-04-29 07:36:34','s',NULL),(150,57,'2013-04-29 08:02:14','f',NULL),(151,60,'2013-04-29 10:51:44','r',NULL),(152,60,'2013-04-29 10:52:04','s',NULL),(153,61,'2013-04-29 14:05:38','r',NULL),(154,62,'2013-04-29 14:05:43','r',NULL),(155,62,'2013-04-29 14:06:43','f',NULL),(156,63,'2013-04-29 14:13:10','r',NULL),(157,64,'2013-04-29 14:13:12','r',NULL),(158,61,'2013-04-29 14:13:14','e',NULL),(159,65,'2013-04-29 14:13:16','r',NULL),(160,66,'2013-04-29 14:13:18','r',NULL),(161,63,'2013-04-29 14:43:10','c',NULL),(162,63,'2013-04-29 14:43:10','f',NULL),(163,64,'2013-04-29 14:43:12','c',NULL),(164,64,'2013-04-29 14:43:12','f',NULL),(165,65,'2013-04-29 14:43:16','f',NULL),(166,66,'2013-04-29 14:43:18','f',NULL),(167,67,'2013-04-29 14:57:28','r',NULL),(168,68,'2013-04-29 14:57:30','r',NULL),(169,67,'2013-04-29 15:00:20','s',NULL),(170,68,'2013-04-29 15:00:21','s',NULL),(171,69,'2013-04-29 15:00:24','r',NULL),(172,69,'2013-04-29 15:00:26','c',NULL),(173,69,'2013-04-29 15:00:26','f',NULL),(174,70,'2013-04-29 15:00:41','r',NULL),(175,70,'2013-04-29 15:00:42','c',NULL),(176,70,'2013-04-29 15:00:42','f',NULL),(177,71,'2013-04-29 15:00:57','r',NULL),(178,71,'2013-04-29 15:00:58','s',NULL),(179,61,'2013-04-29 15:05:38','f',NULL);
/*!40000 ALTER TABLE `user_action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_admin`
--

DROP TABLE IF EXISTS `user_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_admin` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `admin_type` char(1) NOT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `end_time` timestamp NULL DEFAULT NULL,
  `site_id` bigint(20) unsigned DEFAULT NULL,
  `task_id` bigint(20) unsigned DEFAULT NULL,
  `value` decimal(10,2) NOT NULL,
  `value_type` char(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_admin_site_idx` (`site_id`),
  KEY `fk_admin_task_idx` (`task_id`),
  KEY `fk_admin_user_idx` (`username`),
  CONSTRAINT `fk_admin_site` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`),
  CONSTRAINT `fk_admin_task` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`),
  CONSTRAINT `fk_admin_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_admin`
--

LOCK TABLES `user_admin` WRITE;
/*!40000 ALTER TABLE `user_admin` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_admin` ENABLE KEYS */;
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
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_change_site_idx` (`site_id`),
  KEY `fk_user_change_user_idx` (`username`),
  CONSTRAINT `fk_user_change_site` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`),
  CONSTRAINT `fk_user_change_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_change`
--

LOCK TABLES `user_change` WRITE;
/*!40000 ALTER TABLE `user_change` DISABLE KEYS */;
INSERT INTO `user_change` VALUES (17,'2013-03-30 18:42:51','2013-03-30 18:42:53',1,'admin'),(18,'2013-03-30 18:42:55','2013-03-30 18:42:58',2,'admin'),(19,'2013-03-30 18:43:00','2013-04-10 07:29:59',1,'admin'),(20,'2013-04-01 07:22:13','2013-04-01 12:13:43',3,'1'),(21,'2013-04-10 07:30:02',NULL,1,'admin'),(22,'2013-04-10 08:25:28',NULL,1,'Озерки1 оператор'),(23,'2013-04-20 08:32:49','2013-04-21 13:37:24',1,'Васиостровская оператор'),(24,'2013-04-21 13:37:28','2013-04-29 07:33:46',1,'Васиостровская оператор'),(25,'2013-04-26 11:42:15',NULL,2,'Васиостровская оператор2'),(26,'2013-04-29 07:33:49','2013-04-29 15:00:52',1,'Васиостровская оператор'),(27,'2013-04-29 15:00:54','2013-04-29 15:01:02',10,'Васиостровская оператор');
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
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_site_task`
--

LOCK TABLES `user_site_task` WRITE;
/*!40000 ALTER TABLE `user_site_task` DISABLE KEYS */;
INSERT INTO `user_site_task` VALUES (1,'admin',1,'s','2013-04-29 10:52:04','2013-03-30 09:14:55',0,NULL),(2,'admin',2,'c','2013-04-26 13:14:06','2013-03-30 09:14:58',0,NULL),(3,'admin',3,'c','2013-04-01 07:23:28','2013-03-30 09:15:02',0,NULL),(6,'admin',6,'c','2013-04-01 07:21:31','2013-03-30 17:35:32',0,NULL),(7,'admin',7,'c','2013-04-01 07:21:40','2013-03-30 17:35:39',0,NULL),(8,'admin',8,'s','2013-04-03 11:45:03','2013-03-30 17:35:41',0,NULL),(9,'1',9,'c','2013-04-01 12:11:20','2013-04-01 07:22:02',0,NULL),(11,'1',11,'c','2013-04-01 12:12:23','2013-04-01 07:22:09',0,NULL),(12,'Озерки1 оператор',1,'c','2013-04-15 09:36:25','2013-04-10 08:20:36',0,NULL),(16,'Озерки1 оператор',31,'c','2013-04-10 10:42:51','2013-04-10 08:24:44',0,NULL),(18,'Васиостровская оператор',1,'u','2013-04-15 09:22:25','2013-04-15 09:22:25',1,NULL),(20,'Васиостровская оператор',36,'c','2013-04-26 11:40:04','2013-04-20 08:34:36',1,NULL),(22,'Васиостровская оператор',13,'s','2013-04-29 15:00:20','2013-04-21 13:37:48',0,NULL),(23,'Васиостровская оператор',31,'c','2013-04-29 15:00:42','2013-04-21 13:37:56',0,NULL),(25,'Васиостровская оператор2',44,'c','2013-04-29 14:43:18','2013-04-26 11:41:28',0,NULL),(26,'Васиостровская оператор2',43,'c','2013-04-29 14:43:12','2013-04-26 11:41:32',0,NULL),(27,'Васиостровская оператор2',42,'c','2013-04-29 14:43:10','2013-04-26 11:41:36',0,NULL),(28,'Васиостровская оператор2',41,'c','2013-04-29 14:43:16','2013-04-26 11:41:39',0,NULL),(29,'Васиостровская оператор2',40,'c','2013-04-29 15:05:38','2013-04-26 11:41:42',0,NULL),(30,'admin',13,'c','2013-04-28 19:38:36','2013-04-26 11:43:30',0,NULL),(31,'admin',31,'c','2013-04-28 19:38:29','2013-04-26 11:43:37',0,NULL),(32,'admin',36,'c','2013-04-28 19:38:33','2013-04-26 11:43:40',0,NULL),(33,'admin',39,'c','2013-04-28 19:38:34','2013-04-26 11:43:44',0,NULL),(34,'Васиостровская оператор',46,'s','2013-04-29 15:00:58','2013-04-29 14:56:59',0,NULL),(35,'Васиостровская оператор2',46,'u','2013-04-29 14:57:00','2013-04-29 14:57:00',0,NULL),(36,'Васиостровская оператор',45,'u','2013-04-29 14:57:01','2013-04-29 14:57:01',0,NULL),(37,'Васиостровская оператор2',45,'u','2013-04-29 14:57:02','2013-04-29 14:57:02',0,NULL);
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
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `time_seq_id_UNIQUE` (`time_seq_id`),
  KEY `fk_user_task_idx` (`user_task_id`),
  KEY `FK_TIME_SEQ_idx` (`time_seq_id`),
  KEY `fk_time_change_idx` (`user_change_id`),
  KEY `fk_user_change_idx` (`user_change_id`),
  CONSTRAINT `fk_task_time_user_change` FOREIGN KEY (`user_change_id`) REFERENCES `user_change` (`id`),
  CONSTRAINT `FK_TIME_SEQ` FOREIGN KEY (`time_seq_id`) REFERENCES `user_task_time_seq` (`id`),
  CONSTRAINT `fk_time_task` FOREIGN KEY (`user_task_id`) REFERENCES `user_site_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task_time`
--

LOCK TABLES `user_task_time` WRITE;
/*!40000 ALTER TABLE `user_task_time` DISABLE KEYS */;
INSERT INTO `user_task_time` VALUES (1,1,'2013-04-01 07:20:57','2013-04-01 07:21:17','2013-04-01 07:22:05','2013-04-01 07:23:01',NULL,'2013-04-01 07:22:11',1,12,7,NULL,1,100.00,0.33,19,0),(2,1,'2013-04-01 07:21:24','2013-04-01 07:33:24','2013-04-01 07:33:24',NULL,NULL,NULL,6,720,NULL,NULL,NULL,100.00,20.00,19,1),(3,8,'2013-04-01 07:21:26','2013-04-01 07:23:26','2013-04-01 07:23:26',NULL,NULL,NULL,7,120,NULL,NULL,NULL,11.00,0.37,19,0),(4,3,'2013-04-01 07:21:28','2013-04-01 07:23:28','2013-04-01 07:23:28',NULL,NULL,NULL,8,120,NULL,NULL,NULL,100.22,3.34,19,0),(5,2,'2013-04-01 07:21:30','2013-04-01 07:21:30','2013-04-01 07:21:30',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,19,0),(6,6,'2013-04-01 07:21:31','2013-04-01 07:21:31','2013-04-01 07:21:31',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,1232.22,1232.22,19,0),(7,7,'2013-04-01 07:21:36','2013-04-01 07:21:36','2013-04-01 07:21:36',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,123.00,123.00,19,0),(8,7,'2013-04-01 07:21:40','2013-04-01 07:21:40','2013-04-01 07:21:40',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,333.00,333.00,19,0),(9,11,'2013-04-01 07:22:18','2013-04-01 07:52:18','2013-04-01 07:52:18',NULL,NULL,NULL,9,1800,NULL,NULL,NULL,11.00,5.50,20,0),(10,9,'2013-04-01 07:22:20','2013-04-01 07:52:20','2013-04-01 07:52:20',NULL,NULL,NULL,10,1800,NULL,NULL,NULL,100.00,50.00,20,0),(11,1,'2013-04-01 07:48:35','2013-04-01 08:48:35','2013-04-01 08:48:35',NULL,NULL,NULL,11,3600,NULL,NULL,NULL,100.00,100.00,19,0),(12,9,'2013-04-01 12:10:20','2013-04-01 12:11:20','2013-04-01 12:11:20',NULL,NULL,NULL,12,60,NULL,NULL,NULL,1000.00,16.67,20,0),(13,11,'2013-04-01 12:10:23','2013-04-01 12:12:23','2013-04-01 12:12:23',NULL,'2013-04-01 12:11:25',NULL,13,60,NULL,60,NULL,11.00,0.18,20,0),(14,1,'2013-04-02 08:52:00','2013-04-02 09:52:00','2013-04-02 09:52:00',NULL,NULL,NULL,16,3600,NULL,NULL,NULL,1000.00,1000.00,19,0),(15,8,'2013-04-03 11:44:37','2013-04-03 11:45:03','2013-04-03 11:45:51',NULL,'2013-04-03 11:46:57','2013-04-03 11:46:48',17,12,NULL,9,5,11.00,0.04,19,0),(16,12,'2013-04-10 08:25:36','2013-04-10 09:25:36','2013-04-10 09:25:36',NULL,NULL,NULL,24,3600,NULL,NULL,NULL,1100.00,1100.00,22,0),(17,16,'2013-04-10 08:31:11','2013-04-10 09:31:11','2013-04-10 09:31:11',NULL,NULL,NULL,25,3600,NULL,NULL,NULL,1000.00,1000.00,22,0),(18,16,'2013-04-10 09:42:51','2013-04-10 10:42:51','2013-04-10 10:42:51',NULL,NULL,NULL,26,3600,NULL,NULL,NULL,1000.00,1000.00,22,0),(19,12,'2013-04-10 09:42:54','2013-04-10 10:42:54','2013-04-10 10:42:54',NULL,NULL,NULL,27,3600,NULL,NULL,NULL,1100.00,1100.00,22,0),(20,1,'2013-04-15 14:03:10','2013-04-15 14:03:16','2013-04-15 14:03:16','2013-04-15 14:33:10',NULL,NULL,28,0,6,NULL,NULL,1100.00,0.00,21,0),(21,1,'2013-04-15 14:03:20','2013-04-15 14:03:28','2013-04-15 14:33:22','2013-04-15 14:33:24',NULL,NULL,30,6,2,NULL,NULL,1100.00,1.83,21,0),(22,1,'2013-04-18 16:58:53','2013-04-18 17:58:53','2013-04-18 17:58:53',NULL,NULL,NULL,33,3600,NULL,NULL,NULL,1100.00,1100.00,21,0),(23,1,'2013-04-18 18:43:26','2013-04-18 19:13:26','2013-04-18 19:13:26',NULL,NULL,NULL,34,1800,NULL,NULL,NULL,1100.00,550.00,21,0),(24,1,'2013-04-18 20:27:15','2013-04-18 20:57:15','2013-04-18 20:57:15',NULL,NULL,NULL,35,1800,NULL,NULL,NULL,1100.00,550.00,21,0),(25,1,'2013-04-19 08:35:28','2013-04-19 08:35:38','2013-04-19 09:05:35','2013-04-19 09:35:30',NULL,NULL,36,3,7,NULL,NULL,1100.00,0.92,21,0),(26,1,'2013-04-19 08:36:11','2013-04-19 08:36:14','2013-04-19 08:36:13','2013-04-19 09:06:11',NULL,NULL,39,1,2,NULL,NULL,1100.00,0.31,21,0),(27,1,'2013-04-19 08:39:09','2013-04-19 08:39:12','2013-04-19 09:09:09',NULL,NULL,NULL,41,3,NULL,NULL,NULL,1100.00,0.92,21,0),(28,1,'2013-04-19 08:39:22','2013-04-19 08:52:00','2013-04-19 09:21:45','2013-04-19 09:39:36',NULL,NULL,42,15,743,NULL,NULL,1100.00,4.58,21,0),(29,1,'2013-04-19 08:52:02','2013-04-19 08:52:03','2013-04-19 08:52:03','2013-04-19 09:22:02',NULL,NULL,45,0,1,NULL,NULL,1100.00,0.00,21,0),(30,1,'2013-04-19 08:52:08','2013-04-19 08:52:14','2013-04-19 09:52:09','2013-04-19 09:22:11',NULL,NULL,47,5,1,NULL,NULL,1100.00,1.53,21,0),(31,20,'2013-04-20 08:34:50','2013-04-20 09:34:50','2013-04-20 09:34:50',NULL,NULL,NULL,50,3600,NULL,NULL,NULL,1100.00,1100.00,23,0),(32,22,'2013-04-21 13:39:18','2013-04-21 13:39:43','2013-04-21 14:09:24','2013-04-21 14:09:40',NULL,NULL,51,19,6,NULL,NULL,1100.00,5.81,24,0),(33,1,'2013-04-26 12:52:38','2013-04-26 12:52:45','2013-04-26 13:22:41','2013-04-26 13:22:41',NULL,NULL,57,4,3,NULL,NULL,1100.00,1.22,21,0),(34,1,'2013-04-26 12:52:55','2013-04-26 12:53:22','2013-04-26 13:22:55',NULL,NULL,NULL,60,27,NULL,NULL,NULL,1100.00,8.25,21,0),(35,31,'2013-04-26 13:03:44','2013-04-26 13:03:51','2013-04-26 13:33:44',NULL,NULL,NULL,61,7,NULL,NULL,NULL,1000.00,1.94,21,0),(36,1,'2013-04-26 13:06:54','2013-04-26 13:06:56','2013-04-26 13:36:54',NULL,NULL,NULL,62,2,NULL,NULL,NULL,1100.00,0.61,21,0),(37,1,'2013-04-26 13:06:58','2013-04-26 13:07:58','2013-04-26 13:07:58',NULL,NULL,NULL,63,60,NULL,NULL,NULL,1100.00,18.33,21,0),(38,31,'2013-04-26 13:09:36','2013-04-26 13:09:37','2013-04-26 13:39:36',NULL,NULL,NULL,64,1,NULL,NULL,NULL,1000.00,0.28,21,0),(39,31,'2013-04-26 13:09:39','2013-04-26 13:10:10','2013-04-26 15:09:52','2013-04-26 13:40:01',NULL,NULL,65,18,13,NULL,NULL,1000.00,5.00,21,0),(40,2,'2013-04-26 13:12:34','2013-04-26 13:12:34','2013-04-26 13:12:34',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(41,2,'2013-04-26 13:13:44','2013-04-26 13:13:44','2013-04-26 13:13:44',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(42,2,'2013-04-26 13:13:46','2013-04-26 13:13:46','2013-04-26 13:13:46',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(43,2,'2013-04-26 13:13:47','2013-04-26 13:13:47','2013-04-26 13:13:47',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(44,2,'2013-04-26 13:13:56','2013-04-26 13:13:56','2013-04-26 13:13:56',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(45,2,'2013-04-26 13:14:04','2013-04-26 13:14:04','2013-04-26 13:14:04',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(46,2,'2013-04-26 13:14:05','2013-04-26 13:14:05','2013-04-26 13:14:05',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(47,2,'2013-04-26 13:14:05','2013-04-26 13:14:05','2013-04-26 13:14:05',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(48,2,'2013-04-26 13:14:06','2013-04-26 13:14:06','2013-04-26 13:14:06',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(49,2,'2013-04-26 13:14:06','2013-04-26 13:14:06','2013-04-26 13:14:06',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,250.00,250.00,21,0),(50,1,'2013-04-28 19:03:11','2013-04-28 19:03:13','2013-04-28 19:33:11',NULL,NULL,NULL,72,2,NULL,NULL,NULL,1100.00,0.61,21,0),(51,30,'2013-04-28 19:03:25','2013-04-28 19:03:30','2013-04-28 19:03:30','2013-04-28 20:03:25',NULL,NULL,73,0,5,NULL,NULL,1100.00,0.00,21,0),(52,31,'2013-04-28 19:08:29','2013-04-28 19:38:29','2013-04-28 19:38:29',NULL,NULL,NULL,75,1800,NULL,NULL,NULL,1000.00,500.00,21,0),(53,32,'2013-04-28 19:08:33','2013-04-28 19:38:33','2013-04-28 19:38:33',NULL,NULL,NULL,76,1800,NULL,NULL,NULL,1100.00,550.00,21,0),(54,33,'2013-04-28 19:08:34','2013-04-28 19:38:34','2013-04-28 19:38:34',NULL,NULL,NULL,77,1800,NULL,NULL,NULL,1100.00,550.00,21,0),(55,30,'2013-04-28 19:08:36','2013-04-28 19:38:36','2013-04-28 19:38:36',NULL,NULL,NULL,78,1800,NULL,NULL,NULL,1100.00,550.00,21,0),(56,1,'2013-04-28 19:08:38','2013-04-28 19:38:38','2013-04-28 19:38:38',NULL,NULL,NULL,79,1800,NULL,NULL,NULL,1100.00,550.00,21,0),(57,1,'2013-04-29 07:32:14','2013-04-29 08:02:14','2013-04-29 08:02:14',NULL,NULL,NULL,80,1800,NULL,NULL,NULL,1100.00,550.00,21,0),(58,22,'2013-04-29 07:33:54','2013-04-29 07:36:32','2013-04-29 08:03:54',NULL,NULL,NULL,81,158,NULL,NULL,NULL,1100.00,48.28,26,0),(59,23,'2013-04-29 07:33:59','2013-04-29 07:36:34','2013-04-29 08:03:59',NULL,NULL,NULL,82,155,NULL,NULL,NULL,1000.00,43.06,26,0),(60,1,'2013-04-29 10:51:44','2013-04-29 10:52:04','2013-04-29 11:21:44',NULL,NULL,NULL,83,20,NULL,NULL,NULL,1100.00,6.11,21,0),(61,29,'2013-04-29 14:05:38','2013-04-29 15:05:38','2013-04-29 15:05:38',NULL,NULL,NULL,84,3600,NULL,NULL,NULL,1100.00,1100.00,25,0),(62,27,'2013-04-29 14:05:43','2013-04-29 14:06:43','2013-04-29 14:06:43',NULL,NULL,NULL,85,60,NULL,NULL,NULL,1100.00,18.33,25,0),(63,27,'2013-04-29 14:13:10','2013-04-29 14:43:10','2013-04-29 14:43:10','2013-04-29 14:43:10',NULL,NULL,86,0,1800,NULL,NULL,1100.00,0.00,25,0),(64,26,'2013-04-29 14:13:12','2013-04-29 14:43:12','2013-04-29 14:43:12','2013-04-29 14:43:12',NULL,NULL,87,0,1800,NULL,NULL,1100.00,0.00,25,0),(65,28,'2013-04-29 14:13:16','2013-04-29 14:43:16','2013-04-29 14:43:16',NULL,NULL,NULL,88,1800,NULL,NULL,NULL,1000.00,500.00,25,0),(66,25,'2013-04-29 14:13:18','2013-04-29 14:43:18','2013-04-29 14:43:18',NULL,NULL,NULL,89,1800,NULL,NULL,NULL,1100.00,550.00,25,0),(67,22,'2013-04-29 14:57:28','2013-04-29 15:00:20','2013-04-29 15:27:28',NULL,NULL,NULL,92,172,NULL,NULL,NULL,1100.00,52.56,26,0),(68,23,'2013-04-29 14:57:30','2013-04-29 15:00:21','2013-04-29 15:27:30',NULL,NULL,NULL,93,171,NULL,NULL,NULL,1000.00,47.50,26,0),(69,23,'2013-04-29 15:00:24','2013-04-29 15:00:26','2013-04-29 15:00:26','2013-04-29 15:30:24',NULL,NULL,94,0,2,NULL,NULL,1000.00,0.00,26,0),(70,23,'2013-04-29 15:00:41','2013-04-29 15:00:42','2013-04-29 15:00:42','2013-04-29 15:30:41',NULL,NULL,96,0,1,NULL,NULL,1000.00,0.00,26,0),(71,34,'2013-04-29 15:00:57','2013-04-29 15:00:58','2013-04-29 15:30:57',NULL,NULL,NULL,98,1,NULL,NULL,NULL,1100.00,0.31,27,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_task_time_seq`
--

LOCK TABLES `user_task_time_seq` WRITE;
/*!40000 ALTER TABLE `user_task_time_seq` DISABLE KEYS */;
INSERT INTO `user_task_time_seq` VALUES (1,NULL,2,'2013-04-01 07:20:57','2013-04-01 07:21:01','r'),(2,1,3,'2013-04-01 07:21:01','2013-04-01 07:21:08','1'),(3,2,4,'2013-04-01 07:21:08','2013-04-01 07:21:11','r'),(4,3,5,'2013-04-01 07:21:11','2013-04-01 07:21:12','3'),(5,4,NULL,'2013-04-01 07:21:12','2013-04-01 07:21:17','r'),(6,NULL,NULL,'2013-04-01 07:21:24','2013-04-01 07:33:24','r'),(7,NULL,NULL,'2013-04-01 07:21:26','2013-04-01 07:23:26','r'),(8,NULL,NULL,'2013-04-01 07:21:28','2013-04-01 07:23:28','r'),(9,NULL,NULL,'2013-04-01 07:22:18','2013-04-01 07:52:18','r'),(10,NULL,NULL,'2013-04-01 07:22:20','2013-04-01 07:52:20','r'),(11,NULL,NULL,'2013-04-01 07:48:35','2013-04-01 08:48:35','r'),(12,NULL,NULL,'2013-04-01 12:10:20','2013-04-01 12:11:20','r'),(13,NULL,14,'2013-04-01 12:10:23','2013-04-01 12:10:25','r'),(14,13,15,'2013-04-01 12:10:25','2013-04-01 12:11:25','2'),(15,14,NULL,'2013-04-01 12:11:25','2013-04-01 12:12:23','r'),(16,NULL,NULL,'2013-04-02 08:52:00','2013-04-02 09:52:00','r'),(17,NULL,18,'2013-04-03 11:44:37','2013-04-03 11:44:39','r'),(18,17,19,'2013-04-03 11:44:39','2013-04-03 11:44:45','2'),(19,18,20,'2013-04-03 11:44:45','2013-04-03 11:44:48','r'),(20,19,21,'2013-04-03 11:44:48','2013-04-03 11:44:53','3'),(21,20,22,'2013-04-03 11:44:53','2013-04-03 11:44:57','r'),(22,21,23,'2013-04-03 11:44:57','2013-04-03 11:45:00','2'),(23,22,NULL,'2013-04-03 11:45:00','2013-04-03 11:45:03','r'),(24,NULL,NULL,'2013-04-10 08:25:36','2013-04-10 09:25:36','r'),(25,NULL,NULL,'2013-04-10 08:31:11','2013-04-10 09:31:11','r'),(26,NULL,NULL,'2013-04-10 09:42:51','2013-04-10 10:42:51','r'),(27,NULL,NULL,'2013-04-10 09:42:54','2013-04-10 10:42:54','r'),(28,NULL,29,'2013-04-15 14:03:10','2013-04-15 14:03:16','1'),(29,28,NULL,'2013-04-15 14:03:16','2013-04-15 14:03:16','r'),(30,NULL,31,'2013-04-15 14:03:20','2013-04-15 14:03:24','r'),(31,30,32,'2013-04-15 14:03:24','2013-04-15 14:03:26','1'),(32,31,NULL,'2013-04-15 14:03:26','2013-04-15 14:03:28','r'),(33,NULL,NULL,'2013-04-18 16:58:53','2013-04-18 17:58:53','r'),(34,NULL,NULL,'2013-04-18 18:43:26','2013-04-18 19:13:26','r'),(35,NULL,NULL,'2013-04-18 20:27:15','2013-04-18 20:57:15','r'),(36,NULL,37,'2013-04-19 08:35:28','2013-04-19 08:35:30','r'),(37,36,38,'2013-04-19 08:35:30','2013-04-19 08:35:37','1'),(38,37,NULL,'2013-04-19 08:35:37','2013-04-19 08:35:38','r'),(39,NULL,40,'2013-04-19 08:36:11','2013-04-19 08:36:13','1'),(40,39,NULL,'2013-04-19 08:36:13','2013-04-19 08:36:14','r'),(41,NULL,NULL,'2013-04-19 08:39:09','2013-04-19 08:39:12','r'),(42,NULL,43,'2013-04-19 08:39:22','2013-04-19 08:39:36','r'),(43,42,44,'2013-04-19 08:39:36','2013-04-19 08:51:59','1'),(44,43,NULL,'2013-04-19 08:51:59','2013-04-19 08:52:00','r'),(45,NULL,46,'2013-04-19 08:52:02','2013-04-19 08:52:03','1'),(46,45,NULL,'2013-04-19 08:52:03','2013-04-19 08:52:03','r'),(47,NULL,48,'2013-04-19 08:52:08','2013-04-19 08:52:11','r'),(48,47,49,'2013-04-19 08:52:11','2013-04-19 08:52:12','1'),(49,48,NULL,'2013-04-19 08:52:12','2013-04-19 08:52:14','r'),(50,NULL,NULL,'2013-04-20 08:34:50','2013-04-20 09:34:50','r'),(51,NULL,52,'2013-04-21 13:39:18','2013-04-21 13:39:29','r'),(52,51,53,'2013-04-21 13:39:29','2013-04-21 13:39:34','1'),(53,52,54,'2013-04-21 13:39:34','2013-04-21 13:39:40','r'),(54,53,55,'2013-04-21 13:39:40','2013-04-21 13:39:41','1'),(55,54,NULL,'2013-04-21 13:39:41','2013-04-21 13:39:43','r'),(57,NULL,58,'2013-04-26 12:52:38','2013-04-26 12:52:41','r'),(58,57,59,'2013-04-26 12:52:41','2013-04-26 12:52:44','1'),(59,58,NULL,'2013-04-26 12:52:44','2013-04-26 12:52:45','r'),(60,NULL,NULL,'2013-04-26 12:52:55','2013-04-26 12:53:22','r'),(61,NULL,NULL,'2013-04-26 13:03:44','2013-04-26 13:03:51','r'),(62,NULL,NULL,'2013-04-26 13:06:54','2013-04-26 13:06:56','r'),(63,NULL,NULL,'2013-04-26 13:06:58','2013-04-26 13:07:58','r'),(64,NULL,NULL,'2013-04-26 13:09:36','2013-04-26 13:09:37','r'),(65,NULL,66,'2013-04-26 13:09:39','2013-04-26 13:09:46','r'),(66,65,67,'2013-04-26 13:09:46','2013-04-26 13:09:50','1'),(67,66,68,'2013-04-26 13:09:50','2013-04-26 13:09:52','r'),(68,67,69,'2013-04-26 13:09:52','2013-04-26 13:09:54','1'),(69,68,70,'2013-04-26 13:09:54','2013-04-26 13:10:01','r'),(70,69,71,'2013-04-26 13:10:01','2013-04-26 13:10:08','1'),(71,70,NULL,'2013-04-26 13:10:08','2013-04-26 13:10:10','r'),(72,NULL,NULL,'2013-04-28 19:03:11','2013-04-28 19:03:13','r'),(73,NULL,74,'2013-04-28 19:03:25','2013-04-28 19:03:30','1'),(74,73,NULL,'2013-04-28 19:03:30','2013-04-28 19:03:30','r'),(75,NULL,NULL,'2013-04-28 19:08:29','2013-04-28 19:38:29','r'),(76,NULL,NULL,'2013-04-28 19:08:33','2013-04-28 19:38:33','r'),(77,NULL,NULL,'2013-04-28 19:08:34','2013-04-28 19:38:34','r'),(78,NULL,NULL,'2013-04-28 19:08:36','2013-04-28 19:38:36','r'),(79,NULL,NULL,'2013-04-28 19:08:38','2013-04-28 19:38:38','r'),(80,NULL,NULL,'2013-04-29 07:32:14','2013-04-29 08:02:14','r'),(81,NULL,NULL,'2013-04-29 07:33:54','2013-04-29 07:36:32','r'),(82,NULL,NULL,'2013-04-29 07:33:59','2013-04-29 07:36:34','r'),(83,NULL,NULL,'2013-04-29 10:51:44','2013-04-29 10:52:04','r'),(84,NULL,NULL,'2013-04-29 14:05:38','2013-04-29 15:05:38','r'),(85,NULL,NULL,'2013-04-29 14:05:43','2013-04-29 14:06:43','r'),(86,NULL,90,'2013-04-29 14:13:10','2013-04-29 14:43:10','1'),(87,NULL,91,'2013-04-29 14:13:12','2013-04-29 14:43:12','1'),(88,NULL,NULL,'2013-04-29 14:13:16','2013-04-29 14:43:16','r'),(89,NULL,NULL,'2013-04-29 14:13:18','2013-04-29 14:43:18','r'),(90,86,NULL,'2013-04-29 14:43:10','2013-04-29 14:43:10','r'),(91,87,NULL,'2013-04-29 14:43:12','2013-04-29 14:43:12','r'),(92,NULL,NULL,'2013-04-29 14:57:28','2013-04-29 15:00:20','r'),(93,NULL,NULL,'2013-04-29 14:57:30','2013-04-29 15:00:21','r'),(94,NULL,95,'2013-04-29 15:00:24','2013-04-29 15:00:26','1'),(95,94,NULL,'2013-04-29 15:00:26','2013-04-29 15:00:26','r'),(96,NULL,97,'2013-04-29 15:00:41','2013-04-29 15:00:42','1'),(97,96,NULL,'2013-04-29 15:00:42','2013-04-29 15:00:42','r'),(98,NULL,NULL,'2013-04-29 15:00:57','2013-04-29 15:00:58','r');
/*!40000 ALTER TABLE `user_task_time_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
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
INSERT INTO `users` VALUES ('1','b5dfdbb15151b2b6db8f313236e967d8ef9a1654f7aa7bca1ee29397dbc3da27a092cf9f8d83886f',1,'','','','','','','','','',NULL,0),('admin','db9fb416a8d325e4e439a4758f35fe8ccd8213d89aed51181eb8cb3aac39bf467b06e8326d36147a',1,'','','','','(123) 333-3333',NULL,'','','23',21,0),('test','6887c45e2666bfd1b45a340a124880a0f4edf2332c6b197c93f90961e30360e73d6ad62deaf65cdd',0,'','','','','','','','','',NULL,0),('Васиостровская оператор','c15a864607e657176154ee9f0e13cea4c2629797abe56d9c2ea1962032fe20f8052714c60e08573f',1,'','','','','','','','','',NULL,0),('Васиостровская оператор2','6ee19b1f9f4477c920e30d3a20ce54387105d5ea898cea5ccdebd80380c083ba4101c66e364bcfd2',1,'','','','','','','','','',25,0),('Гороховая оператор','79a33219770e0ab738b8187d94bf5cd98624c4a79d80c4d3b366f55e8f6fe8c4bd6c9b5223897e6b',1,'','','','','','','','','',NULL,0),('Гороховая оператор2','123',1,'','','','','','','','','',NULL,0),('Декабристов оператор','ed037aa7f2636f65edfe9b41d42f33f08de0294f5363c6febedfcbc1689d9acea77e077d2c695b99',1,'','','','','','','','','',NULL,0),('Декабристов оператор2','4d43d216582acaf4a8fe22153d528d6e024387dc80cac496531dd9473b26e7dc3b5b46328cdc06c0',1,'','','','','','','','','',NULL,0),('Елизаровская оператор','fa601f9f01c74ac60bdbb92ea94bdeee89c26e8493b1a580eaea81f33c518266bb69b58bdf919136',1,'','','','','','','','','',NULL,0),('Елизаровская оператор2','de429806c20d777d8d442ec76afaff19d96816e616002b4021abba00f268122db15bf0e7159993a6',1,'','','','','','','','','',NULL,0),('Московская оператор','3805c0a90059eeda0759793cd97c04033bed2b6716d6c32dffbcc90ca7d44642faa00cab82de11f4',1,'','','','','','','','','',NULL,0),('Московская оператор2','123',1,'','','','','','','','','',NULL,0),('Озерки1 оператор','d55638b06a0689aa39360c7b8f1b22c55ee015955c3b81383c500c045d85f74da7d93c777287f37e',1,'','','','','','','','','',22,0),('Озерки1 оператор2','85bee5ca7b9b12199e745426c5a307caef1350340b1d21ab0111d077fa55a0a33749de715a6c8591',1,'','','','','','','','','',NULL,0),('Озерки2 оператор','05990fb93baab3523e80dc317abb9df05de10ce84716957ff34e658971fababb024dfc1a7f5db075',1,'','','','','','','','','',NULL,0),('Озерки2 оператор2','479ce2852d60e7baf71b174b7ebcc20e11cead507bc01949f28db74343c49e8aad29f527e80bd72d',1,'','','','','','','','','',NULL,0);
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

-- Dump completed on 2013-04-30 15:58:53
