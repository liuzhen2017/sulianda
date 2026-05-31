ALTER TABLE `busi_order`
  ADD COLUMN `check_status` varchar(32) DEFAULT NULL COMMENT '检查状态' AFTER `reject_remark`,
  ADD COLUMN `check_summary` varchar(1000) DEFAULT NULL COMMENT '检查摘要' AFTER `check_status`,
  ADD COLUMN `issue_type` varchar(64) DEFAULT NULL COMMENT '异常类型' AFTER `check_summary`,
  ADD COLUMN `issue_status` varchar(32) DEFAULT NULL COMMENT '问题件状态' AFTER `issue_type`,
  ADD COLUMN `release_status` varchar(32) DEFAULT NULL COMMENT '放行状态' AFTER `issue_status`;
