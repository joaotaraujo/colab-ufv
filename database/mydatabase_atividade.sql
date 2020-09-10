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
-- Table structure for table `atividade`
--

DROP TABLE IF EXISTS `atividade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atividade` (
  `id` int(11) NOT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `dataInicio` date DEFAULT NULL,
  `dataTermino` date DEFAULT NULL,
  `descricao` varchar(500) DEFAULT NULL,
  `FK_responsavel` int(11) DEFAULT NULL,
  `FK_idProjeto` int(11) DEFAULT NULL,
  `complexidade` enum('Simples','Media','Complexa') DEFAULT NULL,
  `prioridade` int(11) DEFAULT NULL,
  `status` enum('Nova Atividade','Em andamento','Concluida','Reaberta','Cancelada','Atrasada') DEFAULT NULL,
  `idAtividadeAnterior` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_responsavel_idx` (`FK_responsavel`),
  KEY `FK_idProjeto_idx` (`FK_idProjeto`),
  CONSTRAINT `FK_idProjeto` FOREIGN KEY (`FK_idProjeto`) REFERENCES `projeto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_responsavel` FOREIGN KEY (`FK_responsavel`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atividade`
--

LOCK TABLES `atividade` WRITE;
/*!40000 ALTER TABLE `atividade` DISABLE KEYS */;
INSERT INTO `atividade` VALUES (1,'Coordenar KeySoft (GEP001)','2016-11-17','2016-12-10','232323',2,0,'Simples',1,'Concluida',-1),(2,'Instalar e configurar o MySQL (DBA001)','2016-11-17','2016-11-19','11111',2,0,'Simples',1,'Concluida',-1),(3,' Modelar, implementar e manter banco (DBA002)','2016-11-24','2016-11-26','Esta atividade engloba toda a parte de modelagem do banco de dados.',0,0,'Simples',1,'Concluida',3),(5,'Avaliar o uso das ferramentas (GCS001)','2016-11-17','2016-11-19','lucas',0,0,'Complexa',5,'Concluida',-1),(6,'Cadastrar e configurar projeto (GCS002)','2016-11-19','2016-12-03','123412352',0,0,'Simples',3,'Concluida',-1),(8,'Modelar classes (MOD001)','2016-11-17','2016-11-24','1111',2,0,'Simples',1,'Concluida',-1),(10,'Elaboração do modelo de dados (MOD002)','2016-11-24','2016-11-26','stuart',1,0,'Simples',1,'Concluida',-1),(11,'Prototipar interfaces (DES001)','2016-11-17','2016-11-24','ae',13,0,'Simples',1,'Concluida',-1),(12,' Projetar arquitetura (DES002)','2016-11-24','2016-11-26','aeaf',13,0,'Simples',1,'Concluida',-1),(13,'Implementar sistema','2016-11-19','2016-12-03','asvawef',13,0,'Simples',1,'Em andamento',-1),(15,'Utilizar Junit','2016-11-19','2016-11-24','aedaedad',0,0,'Simples',1,'Concluida',-1),(16,'Integração KeySoft com o MySQL (COD003)','2016-11-26','2016-12-03','aefaf',0,0,'Simples',1,'Concluida',-1),(17,'Realizar testes (TES001)','2016-12-01','2016-12-03','aedaed',1,0,'Simples',1,'Em andamento',-1),(19,'Interagir com desenvolvedores (TES002)','2016-12-01','2016-12-03','aefae',0,0,'Simples',1,'Concluida',-1),(20,'Registrar os resultados obtidos (TES003)','2016-12-01','2016-12-01','aedaedae',0,0,'Simples',1,'Em andamento',-1),(21,'Submissão de artigo','2016-06-15','2016-07-15','afaf',0,11,'Simples',11,'Cancelada',-1);
/*!40000 ALTER TABLE `atividade` ENABLE KEYS */;
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
