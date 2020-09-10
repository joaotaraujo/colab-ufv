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
-- Table structure for table `tarefa`
--

DROP TABLE IF EXISTS `tarefa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tarefa` (
  `id` int(11) NOT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `dataInicio` date DEFAULT NULL,
  `dataTermino` date DEFAULT NULL,
  `descricao` varchar(500) DEFAULT NULL,
  `FK_responsavel` int(11) DEFAULT NULL,
  `FK_idAtividade` int(11) DEFAULT NULL,
  `complexidade` enum('Simples','Media','Complexa') DEFAULT NULL,
  `prioridade` int(11) DEFAULT NULL,
  `status` enum('Nova tarefa','Em andamento','Concluida','Reaberta','Cancelada','Atrasada') DEFAULT NULL,
  `idTarefaAnterior` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_responsavel_idx` (`FK_responsavel`),
  KEY `FK_idTarefa_idx_idx` (`FK_idAtividade`),
  CONSTRAINT `FK_idAtividade_idx` FOREIGN KEY (`FK_idAtividade`) REFERENCES `atividade` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_responsavel_idx` FOREIGN KEY (`FK_responsavel`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarefa`
--

LOCK TABLES `tarefa` WRITE;
/*!40000 ALTER TABLE `tarefa` DISABLE KEYS */;
INSERT INTO `tarefa` VALUES (0,'Montar Tabelas','2016-04-01','2016-06-15','asdas',0,3,'Simples',1,'Atrasada',-1),(1,'Montar Atributos','2016-04-16','2016-12-29','asd',0,3,'Simples',1,'Concluida',-1),(4,'atvxx','2017-05-31','2016-03-02','aefae',0,16,'Simples',1,'Atrasada',-1),(5,'atvz','2017-05-31','2016-03-02','aedae',0,16,'Simples',1,'Atrasada',-1),(6,'atzzz','2017-05-31','2016-03-02','aedae',0,16,'Simples',1,'Atrasada',-1),(12,'Montar Relações','2016-05-01','2016-05-15','aedaae',0,3,'Simples',1,'Atrasada',-1),(14,'Validar Banco','2016-05-15','2016-05-31','AEAEGAE',0,3,'Simples',1,'Atrasada',-1),(16,'Tarefa Teste','2017-05-31','2016-03-02','ae',0,16,'Simples',1,'Atrasada',-1),(19,'Implementar classes de entidade','2016-11-19','2016-11-25','aeeee',0,13,'Simples',1,'Concluida',-1),(20,'Implementar classes controladoras','2016-11-25','2016-12-01','qe',0,13,'Simples',1,'Concluida',19),(21,'tarefaNNNN','2016-01-31','2016-02-20','aedeadae',0,20,'Simples',1,'Atrasada',-1),(22,'Implementar classes de visão','2016-12-01','2016-12-03','asd',15,13,'Simples',1,'Em andamento',-1),(23,'Pesquisar ferramenta','2016-11-19','2016-11-21',NULL,15,15,'Simples',1,'Concluida',-1),(24,'Auxiliar testadores','2016-11-21','2016-11-24',NULL,15,15,'Simples',1,'Concluida',-1);
/*!40000 ALTER TABLE `tarefa` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-28 17:40:37
