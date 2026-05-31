CREATE TABLE IF NOT EXISTS `busi_order_event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `order_no` varchar(64) DEFAULT NULL,
  `event_type` varchar(64) NOT NULL,
  `before_status` varchar(32) DEFAULT NULL,
  `after_status` varchar(32) DEFAULT NULL,
  `title` varchar(128) NOT NULL,
  `detail` varchar(1000) DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(64) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `dept_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_busi_order_event_order_id` (`order_id`),
  KEY `idx_busi_order_event_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
