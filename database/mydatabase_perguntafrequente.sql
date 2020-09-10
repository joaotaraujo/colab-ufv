CREATE DATABASE  IF NOT EXISTS `mydatabase` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mydatabase`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win64 (x86_64)
--
-- Host: localhost    Database: mydatabase
-- ------------------------------------------------------
-- Server version	5.6.21-log

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
-- Table structure for table `perguntafrequente`
--

DROP TABLE IF EXISTS `perguntafrequente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perguntafrequente` (
  `id` int(11) NOT NULL,
  `pergunta` varchar(45) DEFAULT NULL,
  `resposta` varchar(500) DEFAULT NULL,
  `FK_idProjeto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_idProjeto_idx` (`FK_idProjeto`),
  CONSTRAINT `FK_idProjeto_idx` FOREIGN KEY (`FK_idProjeto`) REFERENCES `projeto` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perguntafrequente`
--

LOCK TABLES `perguntafrequente` WRITE;
/*!40000 ALTER TABLE `perguntafrequente` DISABLE KEYS */;
INSERT INTO `perguntafrequente` VALUES (0,'   serio?   ','aeaeae',1),(1,'     aed     ','EDITO sim',1),(2,'Você gosta de pudim?','ASGAEGSSFH',1),(3,'Como faco para enviar um email no ColabUFV?','O ColabUFV disponibiliza a funcionalidade de email a partir do link no canto superior esquerdo na página inicial.',0),(4,'essa da  ','ae',0),(5,'aefaefeafae  ','aefaefae',0),(6,'teste','aefaefae',0),(7,'lol','eagagaga',0),(8,'gaefaefaehe','aeghagd',0);
/*!40000 ALTER TABLE `perguntafrequente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-28 17:40:38
