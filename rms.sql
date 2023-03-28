-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 21, 2021 at 10:47 AM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rondo`
--

-- --------------------------------------------------------

--
-- Table structure for table `accommodation_type`
--

CREATE TABLE `accommodation_type` (
  `accommodation_type_id` int(11) NOT NULL,
  `type` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `accommodation_type`
--

INSERT INTO `accommodation_type` (`accommodation_type_id`, `type`) VALUES
(1, 'Full-Board'),
(2, 'Half-Board'),
(3, 'SS Full-Board'),
(4, 'SS Half-Board'),
(5, 'Lunch');

-- --------------------------------------------------------

--
-- Table structure for table `additional_costs`
--

CREATE TABLE `additional_costs` (
  `id` int(4) NOT NULL,
  `reservation_id` int(4) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp(),
  `category` varchar(10) NOT NULL,
  `charges` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `cottage`
--

CREATE TABLE `cottage` (
  `cottage_id` int(11) NOT NULL,
  `cottage_name` varchar(15) NOT NULL,
  `num_of_rooms` int(3) DEFAULT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cottage`
--

INSERT INTO `cottage` (`cottage_id`, `cottage_name`, `num_of_rooms`, `is_available`) VALUES
(1, 'Turaco', 5, 1),
(2, 'Main House', 4, 1),
(3, 'Founders', 2, 1),
(4, 'Colobus', 2, 1),
(5, 'Nandi', 3, 1),
(6, 'Isukha', 4, 1);

-- --------------------------------------------------------

--
-- Table structure for table `guest`
--

CREATE TABLE `guest` (
  `guest_id` int(4) NOT NULL,
  `first_name` varchar(15) NOT NULL,
  `middle_name` varchar(15) NOT NULL,
  `surname` varchar(15) NOT NULL,
  `date_of_birth` date NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `address` varchar(50) NOT NULL,
  `email` varchar(25) NOT NULL,
  `national_id_num` varchar(15) NOT NULL,
  `passport_num` varchar(15) NOT NULL,
  `nationality` varchar(10) NOT NULL,
  `country_of_residence` varchar(20) NOT NULL,
  `occupation` varchar(20) NOT NULL,
  `health_considerations` text NOT NULL,
  `preferred_room` varchar(20) NOT NULL DEFAULT 'None'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `guest_category`
--

CREATE TABLE `guest_category` (
  `category_id` int(11) NOT NULL,
  `guest_category` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `guest_category`
--

INSERT INTO `guest_category` (`category_id`, `guest_category`) VALUES
(1, 'Resident Adult'),
(2, 'Non-resident Adult'),
(3, 'Resident Child U12'),
(4, 'Resident Child 12+'),
(5, 'Non-Resident Child U12'),
(6, 'Non-Resident Child 12+'),
(7, 'TF Staff'),
(8, 'Complementary'),
(9, 'Missionary'),
(10, 'Day/Walk-In');

-- --------------------------------------------------------

--
-- Table structure for table `guest_party`
--

CREATE TABLE `guest_party` (
  `id` int(11) NOT NULL,
  `party_id` int(11) NOT NULL,
  `guest_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE `invoice` (
  `invoice_id` int(11) NOT NULL,
  `reservation_id` int(4) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `total_charges` float NOT NULL,
  `agent_commission` float NOT NULL DEFAULT 0,
  `discount` float NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `party`
--

CREATE TABLE `party` (
  `party_id` int(11) NOT NULL,
  `party_name` varchar(30) NOT NULL,
  `guest_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `payment_id` int(11) NOT NULL,
  `reservation_id` int(4) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `account` varchar(15) NOT NULL,
  `mode_of_payment` varchar(15) NOT NULL,
  `amount` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `phone_numbers`
--

CREATE TABLE `phone_numbers` (
  `phone_id` int(4) NOT NULL,
  `phone_num_guest_id` int(4) NOT NULL,
  `phone_type` varchar(10) NOT NULL,
  `number` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `rates`
--

CREATE TABLE `rates` (
  `rate_id` int(3) NOT NULL,
  `guest_category` int(11) NOT NULL,
  `accommodation_type` int(11) NOT NULL,
  `rate` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rates`
--

INSERT INTO `rates` (`rate_id`, `guest_category`, `accommodation_type`, `rate`) VALUES
(1, 1, 1, 7600),
(2, 1, 2, 7100),
(3, 1, 3, 11000),
(4, 1, 4, 10500),
(5, 2, 1, 12100),
(6, 2, 2, 11000),
(7, 2, 3, 17600),
(8, 2, 4, 16500),
(9, 3, 1, 5200),
(10, 4, 1, 7600),
(11, 5, 1, 9300),
(12, 6, 1, 12100),
(13, 4, 3, 10500),
(14, 4, 3, 10500),
(15, 7, 1, 3500),
(16, 9, 1, 5600);

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

CREATE TABLE `reservation` (
  `reservation_id` int(4) NOT NULL,
  `booking_date` timestamp(6) NOT NULL DEFAULT current_timestamp(6),
  `arrival_date` datetime(6) NOT NULL,
  `departure_date` datetime(6) NOT NULL,
  `num_of_nights` bigint(3) NOT NULL DEFAULT 0,
  `num_of_adults` int(3) NOT NULL DEFAULT 0,
  `num_of_children_u12` int(3) NOT NULL DEFAULT 0,
  `num_of_children_teen` int(3) NOT NULL DEFAULT 0,
  `charges` double NOT NULL DEFAULT 0,
  `status` varchar(25) NOT NULL DEFAULT 'Waiting',
  `guest_category` int(15) NOT NULL,
  `user_id` int(4) NOT NULL,
  `guest_id` int(4) NOT NULL,
  `accommodation_type` int(3) NOT NULL,
  `party_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `reserved_rooms`
--

CREATE TABLE `reserved_rooms` (
  `id` int(11) NOT NULL,
  `reservation_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `room_id` int(4) NOT NULL,
  `room_name` varchar(15) NOT NULL,
  `num_of_beds` int(3) DEFAULT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT 1,
  `cottage_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`room_id`, `room_name`, `num_of_beds`, `is_available`, `cottage_id`) VALUES
(1, 'Kiondo', 2, 1, 5),
(2, 'Kanga', 2, 1, 5),
(3, 'Kikoy', 2, 0, 5),
(4, 'Rosebud', 1, 0, 6),
(5, 'Matatu', 2, 1, 6),
(6, 'Butterfly', 3, 1, 6),
(7, 'Big Five', 2, 1, 2),
(8, 'Bob & Betty', 1, 0, 2),
(9, 'Anita\'s', 2, 1, 2),
(10, 'Connie\'s', 2, 0, 2);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(4) NOT NULL,
  `first_name` varchar(15) NOT NULL,
  `middle_name` varchar(15) DEFAULT NULL,
  `surname` varchar(15) NOT NULL,
  `username` varchar(15) NOT NULL,
  `password` varchar(32) NOT NULL,
  `date_of_birth` date NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `address` varchar(20) DEFAULT NULL,
  `email` varchar(25) DEFAULT NULL,
  `role` varchar(10) NOT NULL,
  `is_admin` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `first_name`, `middle_name`, `surname`, `username`, `password`, `date_of_birth`, `phone_number`, `address`, `email`, `role`, `is_admin`) VALUES
(6, 'Gitu', 'test', 'M', 'gm', '912ec803b2ce49e4a541068d495ab570', '1990-12-11', '+254 712345678', 'PO Box 123456', 'test@email.com', 'Item 2', 1),
(9, 'Viewer', '', 'Audit', 'audit', 'a5a63d9b90e6bfe9261e70b66afec721', '1950-01-01', '', '', '', 'Audit', 0),
(10, 'Admin', '', 'Admin', 'admin', '21232f297a57a5a743894a0e4a801fc3', '1950-01-01', '', '', '', 'Admin', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accommodation_type`
--
ALTER TABLE `accommodation_type`
  ADD PRIMARY KEY (`accommodation_type_id`);

--
-- Indexes for table `additional_costs`
--
ALTER TABLE `additional_costs`
  ADD PRIMARY KEY (`id`,`reservation_id`),
  ADD KEY `reservation_id` (`reservation_id`);

--
-- Indexes for table `cottage`
--
ALTER TABLE `cottage`
  ADD PRIMARY KEY (`cottage_id`);

--
-- Indexes for table `guest`
--
ALTER TABLE `guest`
  ADD PRIMARY KEY (`guest_id`);

--
-- Indexes for table `guest_category`
--
ALTER TABLE `guest_category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `guest_party`
--
ALTER TABLE `guest_party`
  ADD PRIMARY KEY (`id`),
  ADD KEY `party_id` (`party_id`),
  ADD KEY `guest_id` (`guest_id`);

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`invoice_id`,`reservation_id`),
  ADD KEY `reservation_id` (`reservation_id`);

--
-- Indexes for table `party`
--
ALTER TABLE `party`
  ADD PRIMARY KEY (`party_id`),
  ADD KEY `guest_id` (`guest_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `reservation_id` (`reservation_id`);

--
-- Indexes for table `phone_numbers`
--
ALTER TABLE `phone_numbers`
  ADD PRIMARY KEY (`phone_id`,`phone_num_guest_id`),
  ADD KEY `phone_num_guest_id` (`phone_num_guest_id`);

--
-- Indexes for table `rates`
--
ALTER TABLE `rates`
  ADD PRIMARY KEY (`rate_id`),
  ADD KEY `accomodation_type` (`accommodation_type`),
  ADD KEY `guest_category` (`guest_category`);

--
-- Indexes for table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`reservation_id`),
  ADD KEY `fk_to_userID` (`user_id`),
  ADD KEY `fk_to_guestID` (`guest_id`),
  ADD KEY `guest_category` (`guest_category`),
  ADD KEY `reservation_ibfk_1` (`accommodation_type`),
  ADD KEY `party_id` (`party_id`);

--
-- Indexes for table `reserved_rooms`
--
ALTER TABLE `reserved_rooms`
  ADD PRIMARY KEY (`id`),
  ADD KEY `reservation_id` (`reservation_id`),
  ADD KEY `room_id` (`room_id`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`room_id`),
  ADD KEY `cottage_id` (`cottage_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accommodation_type`
--
ALTER TABLE `accommodation_type`
  MODIFY `accommodation_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `additional_costs`
--
ALTER TABLE `additional_costs`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `cottage`
--
ALTER TABLE `cottage`
  MODIFY `cottage_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `guest`
--
ALTER TABLE `guest`
  MODIFY `guest_id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `guest_category`
--
ALTER TABLE `guest_category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `guest_party`
--
ALTER TABLE `guest_party`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `invoice_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `party`
--
ALTER TABLE `party`
  MODIFY `party_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `rates`
--
ALTER TABLE `rates`
  MODIFY `rate_id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `reservation_id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=75;

--
-- AUTO_INCREMENT for table `reserved_rooms`
--
ALTER TABLE `reserved_rooms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `room_id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `additional_costs`
--
ALTER TABLE `additional_costs`
  ADD CONSTRAINT `additional_costs_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`) ON UPDATE CASCADE;

--
-- Constraints for table `guest_party`
--
ALTER TABLE `guest_party`
  ADD CONSTRAINT `guest_party_ibfk_1` FOREIGN KEY (`party_id`) REFERENCES `party` (`party_id`),
  ADD CONSTRAINT `guest_party_ibfk_2` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`guest_id`);

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`) ON UPDATE CASCADE;

--
-- Constraints for table `party`
--
ALTER TABLE `party`
  ADD CONSTRAINT `party_ibfk_1` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`guest_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`);

--
-- Constraints for table `phone_numbers`
--
ALTER TABLE `phone_numbers`
  ADD CONSTRAINT `phone_numbers_ibfk_1` FOREIGN KEY (`phone_num_guest_id`) REFERENCES `guest` (`guest_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `rates`
--
ALTER TABLE `rates`
  ADD CONSTRAINT `rates_ibfk_1` FOREIGN KEY (`accommodation_type`) REFERENCES `accommodation_type` (`accommodation_type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `rates_ibfk_2` FOREIGN KEY (`guest_category`) REFERENCES `guest_category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `fk_to_guestID` FOREIGN KEY (`guest_id`) REFERENCES `guest` (`guest_id`),
  ADD CONSTRAINT `fk_to_userID` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`accommodation_type`) REFERENCES `accommodation_type` (`accommodation_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`guest_category`) REFERENCES `guest_category` (`category_id`),
  ADD CONSTRAINT `reservation_ibfk_3` FOREIGN KEY (`party_id`) REFERENCES `party` (`party_id`);

--
-- Constraints for table `reserved_rooms`
--
ALTER TABLE `reserved_rooms`
  ADD CONSTRAINT `reserved_rooms_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reserved_rooms_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_1` FOREIGN KEY (`cottage_id`) REFERENCES `cottage` (`cottage_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
