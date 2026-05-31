ALTER TABLE busi_order ADD COLUMN customer_id bigint DEFAULT NULL COMMENT '客户信息ID' AFTER product_id;
