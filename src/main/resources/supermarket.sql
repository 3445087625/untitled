-- ============================================
-- 超市前台销售系统 数据库脚本
-- 课程设计：数据库原理课程设计
-- 题目：超市前台销售系统
-- 姓名：常建航  学号：231451080401
-- ============================================

DROP DATABASE IF EXISTS supermarket;
CREATE DATABASE supermarket DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE supermarket;

-- ============================================
-- 1. 用户表（收银员/管理员）
-- ============================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    user_id         INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username        VARCHAR(30)  NOT NULL COMMENT '登录账号',
    password        VARCHAR(100) NOT NULL COMMENT '登录密码',
    real_name       VARCHAR(20)  DEFAULT NULL COMMENT '真实姓名',
    role_type       TINYINT      NOT NULL DEFAULT 2 COMMENT '角色: 1管理员 2收银员',
    phone           VARCHAR(11)  DEFAULT NULL COMMENT '联系电话',
    status          TINYINT      DEFAULT 1 COMMENT '状态: 1正常 0禁用',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 商品分类表
DROP TABLE IF EXISTS category;
CREATE TABLE category (
    category_id     INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    category_name   VARCHAR(50)  NOT NULL COMMENT '分类名称',
    parent_id       INT          DEFAULT 0 COMMENT '父分类ID, 0表示顶级分类',
    sort_order      INT          DEFAULT 0 COMMENT '排序号',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_category_name (category_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 3. 商品表
DROP TABLE IF EXISTS product;
CREATE TABLE product (
    product_id      INT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    product_name    VARCHAR(100) NOT NULL COMMENT '商品名称',
    category_id     INT          NOT NULL COMMENT '所属分类ID',
    price           DECIMAL(10,2) NOT NULL COMMENT '售价',
    cost_price      DECIMAL(10,2) DEFAULT 0.00 COMMENT '进价',
    stock           INT          DEFAULT 0 COMMENT '库存数量',
    unit            VARCHAR(10)  DEFAULT '个' COMMENT '单位(个/箱/瓶/斤)',
    barcode         VARCHAR(50)  DEFAULT NULL COMMENT '条形码',
    status          TINYINT      DEFAULT 1 COMMENT '状态: 1在售 0下架',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_category (category_id),
    KEY idx_barcode (barcode),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 4. 销售订单表
DROP TABLE IF EXISTS sales_order;
CREATE TABLE sales_order (
    order_id        INT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no        VARCHAR(32)  NOT NULL COMMENT '订单编号',
    user_id         INT          NOT NULL COMMENT '收银员ID',
    total_amount    DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    actual_amount   DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实收金额',
    payment_method  TINYINT      DEFAULT 1 COMMENT '支付方式: 1现金 2微信 3支付宝',
    order_status    TINYINT      DEFAULT 1 COMMENT '状态: 1已完成 2已退款',
    remark          VARCHAR(200) DEFAULT NULL COMMENT '备注',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time),
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单表';

-- 5. 销售明细表
DROP TABLE IF EXISTS sales_detail;
CREATE TABLE sales_detail (
    detail_id       INT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    order_id        INT          NOT NULL COMMENT '订单ID',
    product_id      INT          NOT NULL COMMENT '商品ID',
    product_name    VARCHAR(100) NOT NULL COMMENT '商品名称(冗余,防止商品修改后历史数据变化)',
    price           DECIMAL(10,2) NOT NULL COMMENT '销售单价',
    quantity        INT          NOT NULL DEFAULT 1 COMMENT '购买数量',
    subtotal        DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    KEY idx_order_id (order_id),
    KEY idx_product_id (product_id),
    CONSTRAINT fk_detail_order FOREIGN KEY (order_id) REFERENCES sales_order(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_detail_product FOREIGN KEY (product_id) REFERENCES product(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售明细表';

-- ============================================
-- 初始数据
-- ============================================

-- 管理员和收银员账号
INSERT INTO sys_user(username, password, real_name, role_type, phone, status) VALUES
('admin',    '123456', '系统管理员', 1, '13800000001', 1),
('cashier01','123456', '张收银',     2, '13800000002', 1),
('cashier02','123456', '李收银',     2, '13800000003', 1);

-- 商品分类
INSERT INTO category(category_id, category_name, parent_id, sort_order) VALUES
(1, '食品饮料', 0, 1),
(2, '日用百货', 0, 2),
(3, '烟酒',     0, 3),
(4, '休闲食品', 1, 1),
(5, '饮料',     1, 2),
(6, '乳制品',   1, 3);

-- 商品数据
INSERT INTO product(product_name, category_id, price, cost_price, stock, unit, barcode, status) VALUES
('可口可乐330ml',    5, 3.00,  2.20,  200, '罐', '690123456701', 1),
('雪碧330ml',        5, 3.00,  2.20,  180, '罐', '690123456702', 1),
('农夫山泉550ml',    5, 2.00,  1.20,  300, '瓶', '690123456703', 1),
('康师傅红烧牛肉面', 1, 4.50,  3.20,  150, '袋', '690123456704', 1),
('乐事薯片原味',     4, 7.50,  5.00,  100, '袋', '690123456705', 1),
('奥利奥饼干',       4, 9.90,  6.50,  80,  '盒', '690123456706', 1),
('蒙牛纯牛奶250ml',  6, 3.50,  2.50,  200, '盒', '690123456707', 1),
('伊利安慕希酸奶',   6, 6.00,  4.50,  120, '瓶', '690123456708', 1),
('中华香烟(硬)',     3, 45.00, 38.00, 50,  '盒', '690123456709', 1),
('青岛啤酒500ml',    3, 5.00,  3.50,  300, '罐', '690123456710', 1),
('海飞丝洗发水',     2, 39.90, 28.00, 60,  '瓶', '690123456711', 1),
('舒肤佳香皂',       2, 4.50,  3.00,  200, '块', '690123456712', 1),
('心相印抽纸',       2, 12.90, 9.00,  150, '提', '690123456713', 1),
('金龙鱼调和油5L',   1, 69.90, 55.00, 40,  '桶', '690123456714', 1),
('旺旺仙贝',         4, 5.00,  3.50,  120, '袋', '690123456715', 1);

-- ============================================
-- 创建视图：销售汇总视图
-- ============================================
CREATE OR REPLACE VIEW v_sales_summary AS
SELECT
    DATE(so.create_time) AS sale_date,
    COUNT(DISTINCT so.order_id) AS order_count,
    SUM(so.actual_amount) AS total_sales,
    SUM(sd.quantity) AS total_quantity
FROM sales_order so
JOIN sales_detail sd ON so.order_id = sd.order_id
WHERE so.order_status = 1
GROUP BY DATE(so.create_time)
ORDER BY sale_date DESC;

-- ============================================
-- 创建触发器：销售时自动扣减库存
-- ============================================
DELIMITER $$
DROP TRIGGER IF EXISTS trg_sales_reduce_stock$$
CREATE TRIGGER trg_sales_reduce_stock
AFTER INSERT ON sales_detail
FOR EACH ROW
BEGIN
    UPDATE product SET stock = stock - NEW.quantity
    WHERE product_id = NEW.product_id AND stock >= NEW.quantity;
END$$
DELIMITER ;
