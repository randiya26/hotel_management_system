-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 08, 2025 at 09:28 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hotel_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `id` int(11) NOT NULL,
  `room_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `from_date` date DEFAULT NULL,
  `to_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`id`, `room_id`, `customer_id`, `from_date`, `to_date`) VALUES
(7, 5, 2, '2025-10-04', '2025-10-05'),
(8, 11, 3, '2025-10-04', '2025-10-05'),
(9, 16, 1, '2025-10-04', '2025-10-05'),
(10, 19, 1, '2025-10-04', '2025-10-05'),
(11, 7, 2, '2025-10-04', '2025-10-05');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `nic` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`id`, `name`, `phone`, `nic`) VALUES
(1, 'Shehara', '0740149592', NULL),
(2, 'Dinithi', '074272890', '123456788v'),
(3, 'Randiya Manamperi', '0712345672', '234567890v');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `id` int(11) NOT NULL,
  `type` varchar(50) NOT NULL,
  `price` double NOT NULL,
  `available` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id`, `type`, `price`, `available`) VALUES
(1, 'Single', 5000, 0),
(2, 'Double', 8000, 0),
(3, 'Suite', 15000, 0),
(4, 'Single', 7000, 0),
(5, 'Single', 5000, 0),
(6, 'Double', 8000, 0),
(7, 'Suite', 15000, 0),
(8, 'Single', 5000, 0),
(9, 'Double', 8000, 1),
(10, 'Suite', 15000, 1),
(11, 'Single', 5000, 0),
(12, 'Double', 8000, 0),
(13, 'Suite', 15000, 0),
(14, 'Single', 5000, 0),
(15, 'Double', 8000, 0),
(16, 'Suite', 15000, 0),
(17, 'Single', 5000, 1),
(18, 'Double', 8000, 1),
(19, 'Suite', 15000, 0),
(20, 'Single', 5000, 1),
(21, 'Double', 8000, 1),
(22, 'Suite', 15000, 1),
(23, 'Single', 5000, 1),
(24, 'Double', 8000, 1),
(25, 'Suite', 15000, 1),
(26, 'Single', 5000, 1),
(27, 'Double', 8000, 1),
(28, 'Suite', 15000, 1),
(29, 'Single', 5000, 1),
(30, 'Double', 8000, 1),
(31, 'Suite', 15000, 1),
(32, 'Single', 5000, 1),
(33, 'Double', 8000, 1),
(34, 'Suite', 15000, 1),
(35, 'Single', 5000, 1),
(36, 'Double', 8000, 1),
(37, 'Suite', 15000, 1),
(38, 'Single', 5000, 1),
(39, 'Double', 8000, 1),
(40, 'Suite', 15000, 1),
(41, 'Single', 5000, 1),
(42, 'Double', 8000, 1),
(43, 'Suite', 15000, 1),
(44, 'Single', 5000, 1),
(45, 'Double', 8000, 1),
(46, 'Suite', 15000, 1),
(47, 'Single', 5000, 1),
(48, 'Double', 8000, 1),
(49, 'Suite', 15000, 1),
(50, 'Single', 5000, 1),
(51, 'Double', 8000, 1),
(52, 'Suite', 15000, 1),
(53, 'Single', 5000, 1),
(54, 'Double', 8000, 1),
(55, 'Suite', 15000, 1),
(56, 'Single', 5000, 1),
(57, 'Double', 8000, 1),
(58, 'Suite', 15000, 1),
(59, 'Single', 5000, 1),
(60, 'Double', 8000, 1),
(61, 'Suite', 15000, 1),
(62, 'Single', 5000, 1),
(63, 'Double', 8000, 1),
(64, 'Suite', 15000, 1),
(65, 'Single', 5000, 1),
(66, 'Double', 8000, 1),
(67, 'Suite', 15000, 1),
(68, 'Single', 5000, 1),
(69, 'Double', 8000, 1),
(70, 'Suite', 15000, 1),
(71, 'Single', 5000, 1),
(72, 'Double', 8000, 1),
(73, 'Suite', 15000, 1),
(74, 'Single', 5000, 1),
(75, 'Double', 8000, 1),
(76, 'Suite', 15000, 1),
(77, 'Single', 5000, 1),
(78, 'Double', 8000, 1),
(79, 'Suite', 15000, 1),
(80, 'Single', 5000, 1),
(81, 'Double', 8000, 1),
(82, 'Suite', 15000, 1),
(83, 'Single', 5000, 1),
(84, 'Double', 8000, 1),
(85, 'Suite', 15000, 1),
(86, 'Single', 5000, 1),
(87, 'Double', 8000, 1),
(88, 'Suite', 15000, 1),
(89, 'Single', 5000, 1),
(90, 'Double', 8000, 1),
(91, 'Suite', 15000, 1),
(92, 'Single', 5000, 1),
(93, 'Double', 8000, 1),
(94, 'Suite', 15000, 1),
(95, 'Single', 5000, 1),
(96, 'Double', 8000, 1),
(97, 'Suite', 15000, 1),
(98, 'Single', 5000, 1),
(99, 'Double', 8000, 1),
(100, 'Suite', 15000, 1),
(101, 'Single', 5000, 1),
(102, 'Double', 8000, 1),
(103, 'Suite', 15000, 1),
(104, 'Single', 5000, 1),
(105, 'Double', 8000, 1),
(106, 'Suite', 15000, 1),
(107, 'Single', 5000, 1),
(108, 'Double', 8000, 1),
(109, 'Suite', 15000, 1),
(110, 'Single', 5000, 1),
(111, 'Double', 8000, 1),
(112, 'Suite', 15000, 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'admin', 'admin'),
(2, 'admin2', '123456');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `room_id` (`room_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=113;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
