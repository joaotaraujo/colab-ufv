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
-- Table structure for table `mensagem`
--

DROP TABLE IF EXISTS `mensagem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mensagem` (
  `dataEnvio` date DEFAULT NULL,
  `idRemetente` int(11) DEFAULT NULL,
  `escopo` varchar(700) DEFAULT NULL,
  `id` int(11) NOT NULL,
  `idChat` int(11) DEFAULT NULL,
  `horaEnvio` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_idxRemetente_idx` (`idRemetente`),
  KEY `FK_idxChat_idx` (`idChat`),
  CONSTRAINT `FK_idxChat` FOREIGN KEY (`idChat`) REFERENCES `chat` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_idxRemetente` FOREIGN KEY (`idRemetente`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mensagem`
--

LOCK TABLES `mensagem` WRITE;
/*!40000 ALTER TABLE `mensagem` DISABLE KEYS */;
INSERT INTO `mensagem` VALUES (NULL,0,NULL,0,0,NULL),(NULL,1,NULL,1,0,NULL),(NULL,0,NULL,2,0,NULL),(NULL,1,NULL,3,0,NULL),('2016-10-25',7,'aee',4,0,'16:24:53'),('2016-10-26',7,'fgfg',5,0,'14:30:17'),('2016-11-03',7,'AEEE',6,0,'20:07:15'),('2016-11-03',0,'KKKKKKKKKKKKKKKKKKKKKKKKKKKKKK',7,0,'20:07:28'),('2016-11-03',0,'MEU DEUS FUNFO',8,0,'20:07:31'),('2016-11-03',0,'TUDO ESTA SENDO GRAVADO NO BANCO',9,0,'20:07:36'),('2016-11-03',0,'volta la nos relatórios do projeto, vai em relatório geral, c vai ver nos gráfico que até o numero de mensagem aumento kkk',10,0,'20:08:06'),('2016-11-03',7,'aeee',11,0,'20:09:37'),('2016-11-03',7,'deuu certo',12,0,'20:09:39'),('2016-11-03',7,'pqp em neguin',13,0,'20:09:41'),('2016-11-03',7,'ai se frito pra kraio',14,0,'20:09:44'),('2016-11-03',0,'no fi',15,0,'20:09:49'),('2016-11-03',0,'n usa acento pq buga',16,0,'20:09:53'),('2016-11-03',0,'fiz esse trem sozim',17,0,'20:09:59'),('2016-11-03',0,'imagina o tanto de linha de código',18,0,'20:10:02'),('2016-11-03',0,'ae',19,0,'20:10:44'),('2016-11-03',0,'n usa acento plx',20,0,'20:10:52'),('2016-11-03',0,'ta chegando ai?',21,0,'20:10:59'),('2016-11-03',7,'ta sim',22,0,'20:12:01'),('2016-11-03',7,'to vendo',23,0,'20:12:03'),('2016-11-03',0,'entao ta funfando',24,0,'20:12:43'),('2016-11-03',0,'as mensagem tao tudo indo pro banco de boassa',25,0,'20:12:49'),('2016-11-03',7,'ta sim',26,0,'20:12:53'),('2016-11-03',7,'pode fica tranquilo',27,0,'20:12:56'),('2016-11-03',7,'Teste!',28,0,'20:32:39'),('2016-11-03',7,'Teste - Elena',29,0,'20:33:00'),('2016-11-04',7,'Testando o chat.',30,0,'18:04:57'),('2016-11-04',7,'Depois temos que testar a conversa por aqui João.',31,0,'18:05:25');
/*!40000 ALTER TABLE `mensagem` ENABLE KEYS */;
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
