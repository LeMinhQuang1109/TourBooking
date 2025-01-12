-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: tour
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `tour_id` bigint NOT NULL,
  `booking_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `number_of_people` int NOT NULL,
  `total_price` double NOT NULL,
  `status` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `tour_id` (`tour_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`tour_id`) REFERENCES `tours` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (5,5,13,'2025-01-12 18:59:40',15,27000000,2);
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Miền Bắc','Các tour du lịch miền Bắc Việt Nam'),(2,'Miền Trung','Các tour du lịch miền Trung Việt Nam'),(3,'Miền Nam','Các tour du lịch miền Nam Việt Nam');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `tour_id` bigint NOT NULL,
  `rating` int DEFAULT NULL,
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `tour_id` (`tour_id`),
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`tour_id`) REFERENCES `tours` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tours`
--

DROP TABLE IF EXISTS `tours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tours` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` double NOT NULL,
  `duration` int DEFAULT NULL,
  `max_people` int DEFAULT '20',
  `available_slots` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int DEFAULT '1',
  `category_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `tours_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tours`
--

LOCK TABLES `tours` WRITE;
/*!40000 ALTER TABLE `tours` DISABLE KEYS */;
INSERT INTO `tours` VALUES (12,'Du lịch Vịnh Hạ Long','https://images.unsplash.com/photo-1528127269322-539801943592?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTJ8fGglRTElQkElQTElMjBsb25nfGVufDB8fDB8fHww','Khám phá Vịnh Hạ Long xinh đẹp trên du thuyền cao cấp. Tham quan các hang động, chèo thuyền kayak và thưởng thức hải sản tươi ngon.',2500000,3,20,20,'2025-01-13','Vịnh Hạ Long',NULL,1,1),(13,'Khám phá Sapa','https://images.unsplash.com/photo-1666277249933-a026bd6a948c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fHNhcGF8ZW58MHx8MHx8fDA%3D','Trekking qua những thửa ruộng bậc thang tuyệt đẹp, gặp gỡ đồng bào dân tộc thiểu số, và trải nghiệm homestay với gia đình địa phương.',1800000,2,15,15,'2025-01-13','Sapa',NULL,1,1),(14,'Khám phá Ninh Bình','https://images.unsplash.com/photo-1564525450279-3c868f65d2bc?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fG5pbmglMjBiaW5ofGVufDB8fDB8fHww','Thăm cố đô, khám phá hang động bằng thuyền ở Tràng An, và đạp xe qua những cảnh đồng quê thơ mộng.',1500000,2,20,20,'2025-01-28','Ninh Bình',NULL,1,1),(15,'Khám phá Hà Nội','https://plus.unsplash.com/premium_photo-1691960159290-6f4ace6e6c4c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aGElMjBub2l8ZW58MHx8MHx8fDA%3D','Khám phá nét quyến rũ của phố cổ Hà Nội, thăm các di tích lịch sử và thưởng thức ẩm thực đường phố.',900000,1,25,25,'2025-01-13','Hà Nội',NULL,1,1),(16,'Khám phá Cố đô Huế','https://images.unsplash.com/photo-1692449452966-661371c6def1?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aHUlRTElQkElQkZ8ZW58MHx8MHx8fDA%3D','Khám phá Kinh thành Huế, du ngoạn sông Hương và thăm các lăng tẩm hoàng gia.',1200000,2,25,25,'2025-01-13','Huế',NULL,1,2),(17,'Phố cổ Hội An','https://plus.unsplash.com/premium_photo-1690960644375-6f2399a08ebc?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aG9pJTIwYW58ZW58MHx8MHx8fDA%3D','Dạo bước qua phố cổ quyến rũ, tham gia lớp học nấu ăn và thăm các làng nghề thủ công lân cận.',1500000,2,20,20,'2025-01-13','Hội An',NULL,1,2),(18,'Nghỉ dưỡng Đà Nẵng','https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8ZGElMjBuYW5nfGVufDB8fDB8fHww','Thư giãn trên bãi biển đẹp, thăm Ngũ Hành Sơn và thưởng thức ẩm thực hải sản.',2000000,3,30,30,'2025-01-29','Đà Nẵng',NULL,1,2),(20,'Khám phá Đồng bằng sông Cửu Long','https://plus.unsplash.com/premium_photo-1661938316964-26c0a1d0f123?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fG1la29uZyUyMGRlbHRhfGVufDB8fDB8fHww','Thăm chợ nổi, đạp xe qua các làng quê và trải nghiệm cuộc sống trên sông nước.',1200000,2,20,20,'2025-01-24','Đồng Bằng sông Mê Kông',NULL,1,3),(21,'Nghỉ dưỡng Phú Quốc','https://images.unsplash.com/photo-1732243395944-cb3ff9311091?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHBodSUyMHF1b2N8ZW58MHx8MHx8fDA%3D','Tận hưởng bãi biển hoang sơ, lặn ngắm san hô và thăm các trang trại ngọc trai, nhà thùng nước mắm.',3500000,4,25,25,'2025-01-13','Phú Quốc',NULL,1,3),(23,'Chợ nổi Cần Thơ','https://images.unsplash.com/photo-1680711211916-195a00308786?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGNhbiUyMHRob3xlbnwwfHwwfHx8MA%3D%3D','Tham quan chợ nổi Cái Răng buổi sáng sớm, đạp xe qua các vườn trái cây.',1100000,2,20,20,'2025-01-24','Cần Thơ',NULL,1,3),(26,'sdfg','12','sdf',1,12,12,0,'2025-01-29','12',NULL,0,1);
/*!40000 ALTER TABLE `tours` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'user11','$2a$10$3pcKj4agAy0iEZNuWYpQ0.l/.uMnhWgLxrVJpyYw4VCeJQ4GbeEhS','user1@example.com','Nguyen Van A','ROLE_USER','2024-11-29 16:19:31'),(3,'user','$2a$10$h2kKZh.RN.Xd66BmMJ2Qi.r7KJeHrw.aYPYgyB8HOSsw4vEkFAxL.','user@example.com','Test User','ROLE_USER',NULL),(4,'admin','$2a$10$Wi/tubBALJ2t8Sp1ubiweuoSlOE1zCAjfsFHIh.DtzQOtCxuHiyL6','admin@example.com','Administrator','ROLE_ADMIN',NULL),(5,'moimoi','$2a$10$PYPSuW94XG9LDaourl8vo.hPqX/Gzxxmj19h8ONmB3HDBkmgHeKvq','qvq78g84159@bcooq.com','moi Nam moi','ROLE_USER','2025-01-12 08:25:45');
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

-- Dump completed on 2025-01-13  2:44:18
