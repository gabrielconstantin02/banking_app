-- MariaDB dump 10.19  Distrib 10.5.9-MariaDB, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: bank_db
-- ------------------------------------------------------
-- Server version	10.5.9-MariaDB

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
-- Table structure for table `ACCOUNT`
--

DROP TABLE IF EXISTS `ACCOUNT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ACCOUNT` (
  `iban` varchar(24) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `currency_id` int(11) NOT NULL,
  `bic` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `balance` decimal (10,2) NOT NULL,
  PRIMARY KEY (`iban`),
  KEY `fk_ACCOUNT_1_idx` (`user_id`),
  KEY `FK_currency_account_idx` (`currency_id`),
  CONSTRAINT `FK_currency_account` FOREIGN KEY (`currency_id`) REFERENCES `CURRENCY` (`currency_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_user_account` FOREIGN KEY (`user_id`) REFERENCES `USER` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACCOUNT`
--

LOCK TABLES `ACCOUNT` WRITE;
/*!40000 ALTER TABLE `ACCOUNT` DISABLE KEYS */;
/*!40000 ALTER TABLE `ACCOUNT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CARD`
--

DROP TABLE IF EXISTS `CARD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CARD` (
  `card_number` VARCHAR(16) NOT NULL,
  `iban` varchar(24) COLLATE utf8mb4_unicode_ci NOT NULL,
  `CCV2` varchar(11) NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `valid_thru` datetime NOT NULL,
  PRIMARY KEY (`card_number`),
  KEY `FK_account_card_idx` (`iban`),
  CONSTRAINT `FK_account_card` FOREIGN KEY (`iban`) REFERENCES `ACCOUNT` (`iban`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CARD`
--

LOCK TABLES `CARD` WRITE;
/*!40000 ALTER TABLE `CARD` DISABLE KEYS */;
/*!40000 ALTER TABLE `CARD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CURRENCY`
--

DROP TABLE IF EXISTS `CURRENCY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CURRENCY` (
  `currency_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`currency_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CURRENCY`
--

LOCK TABLES `CURRENCY` WRITE;
/*!40000 ALTER TABLE `CURRENCY` DISABLE KEYS */;
/*!40000 ALTER TABLE `CURRENCY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DEPOSIT`
--

DROP TABLE IF EXISTS `DEPOSIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DEPOSIT` (
  `iban` varchar(24) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nr_months` int(11) NOT NULL,
  `due_date` date NOT NULL,
  `renewal` tinyint(4) NOT NULL,
  PRIMARY KEY (`iban`),
  KEY `FK_deposit_type_deposit_idx` (`nr_months`),
  CONSTRAINT `FK_account_deposit` FOREIGN KEY (`iban`) REFERENCES `ACCOUNT` (`iban`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_deposit_type_deposit` FOREIGN KEY (`nr_months`) REFERENCES `DEPOSIT_TYPE` (`nr_months`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DEPOSIT`
--

LOCK TABLES `DEPOSIT` WRITE;
/*!40000 ALTER TABLE `DEPOSIT` DISABLE KEYS */;
/*!40000 ALTER TABLE `DEPOSIT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DEPOSIT_TYPE`
--

DROP TABLE IF EXISTS `DEPOSIT_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DEPOSIT_TYPE` (
  `nr_months` int(11) NOT NULL,
  `interest_rate` decimal(2,2) NOT NULL,
  PRIMARY KEY (`nr_months`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DEPOSIT_TYPE`
--

LOCK TABLES `DEPOSIT_TYPE` WRITE;
/*!40000 ALTER TABLE `DEPOSIT_TYPE` DISABLE KEYS */;
/*!40000 ALTER TABLE `DEPOSIT_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRANSACTION`
--

DROP TABLE IF EXISTS `TRANSACTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TRANSACTION` (
  `transaction_id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` varchar(24) COLLATE utf8mb4_unicode_ci NOT NULL,
  `receiver_id` varchar(24) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `FK_account1_transatcion_idx` (`sender_id`),
  KEY `FK_account2_transatcion_idx` (`receiver_id`),
  CONSTRAINT `fk_account_transaction1` FOREIGN KEY (`sender_id`) REFERENCES `ACCOUNT` (`iban`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_account_transaction2` FOREIGN KEY (`receiver_id`) REFERENCES `ACCOUNT` (`iban`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRANSACTION`
--

LOCK TABLES `TRANSACTION` WRITE;
/*!40000 ALTER TABLE `TRANSACTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `TRANSACTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cnp` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL CHECK (`cnp` between 1000000000000 and 9999999999999),
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `cnp_UNIQUE` (`cnp`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
INSERT INTO `USER` VALUES (1,'ujhdfs','dgfhgfh','hgjhj','jkjhkjk','1000000000001'),(3,'udfs','dgfhgfh','hgjhj','jkjhkjk','1000000000007');
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-27 21:12:30
