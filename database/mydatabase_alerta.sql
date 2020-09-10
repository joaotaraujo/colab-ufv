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
-- Table structure for table `alerta`
--

DROP TABLE IF EXISTS `alerta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alerta` (
  `id` int(11) NOT NULL,
  `dataEmissao` date DEFAULT NULL,
  `descricao` varchar(500) DEFAULT NULL,
  `idAutor` int(11) DEFAULT NULL,
  `foiVisto` enum('0','1') DEFAULT NULL,
  `idProjeto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_idAutor_idx` (`idAutor`),
  KEY `FK_idPrOjeto_idx_idx` (`idProjeto`),
  CONSTRAINT `FK_idAutor_idx` FOREIGN KEY (`idAutor`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_idPrOjeto_idx_idx` FOREIGN KEY (`idProjeto`) REFERENCES `projeto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alerta`
--

LOCK TABLES `alerta` WRITE;
/*!40000 ALTER TABLE `alerta` DISABLE KEYS */;
INSERT INTO `alerta` VALUES (0,'2016-01-01','João, você se esqueceu de olhar o Banco...',0,'1',0),(1,'2016-01-01','João, faça a atividade referente às Classes de Entidade',7,'1',0),(2,'2016-01-01','glaucia faça atividade',0,'1',0),(3,'2016-01-01','AEEE DEU CERTO',0,'1',0),(4,'2016-10-03','aeee',7,'1',0),(5,'2016-10-03','cole jovem',7,'1',0),(6,'2016-10-03','teste',7,'1',0),(7,'2016-10-03','teste',7,'1',0),(8,'2016-10-03','tete',7,'1',0),(9,'2016-10-03','aeaeee',7,'1',0),(10,'2016-10-07','aeeee',0,'1',0),(11,'2016-10-18','oi',0,'1',0),(12,'2016-10-19','SDF',7,'0',0),(13,'2016-10-19','ASFFA',7,'0',0),(14,'2016-10-19','AF',7,'0',0),(15,'2016-10-19','AASFAF',7,'0',0),(16,'2016-10-19','AGGAG',7,'0',0),(17,'2016-10-19','ASDGSADG',7,'0',0),(18,'2016-10-19','ASGASG',7,'0',0),(19,'2016-10-31','cole',7,'1',0),(20,'2016-11-04','Testando alerta',7,'1',0),(21,'2016-11-04','teste ',7,'1',0);
/*!40000 ALTER TABLE `alerta` ENABLE KEYS */;
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
