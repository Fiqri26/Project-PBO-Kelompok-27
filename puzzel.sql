-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2024 at 03:23 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `puzzel`
--

-- --------------------------------------------------------

--
-- Table structure for table `pictzzel`
--

CREATE TABLE `pictzzel` (
  `Id` int(11) NOT NULL,
  `Level` int(5) NOT NULL,
  `ImagePath` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pictzzel`
--

INSERT INTO `pictzzel` (`Id`, `Level`, `ImagePath`) VALUES
(1111, 1, 'src/images/level1.jpg'),
(1112, 2, 'src/images/level2.jpg'),
(1113, 3, 'src/images/level3.jpg'),
(1114, 4, 'src/images/level4.jpg'),
(1115, 5, 'src/images/level5.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pictzzel`
--
ALTER TABLE `pictzzel`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pictzzel`
--
ALTER TABLE `pictzzel`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1116;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
