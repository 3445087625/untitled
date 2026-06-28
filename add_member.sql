DROP TABLE IF EXISTS member;
CREATE TABLE member (
    member_id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '会员ID',
    account_no  VARCHAR(32)  NOT NULL UNIQUE COMMENT '会员账号',
    nickname    VARCHAR(50)  NOT NULL COMMENT '昵称',
    points      INT          DEFAULT 0 COMMENT '积分',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    status      TINYINT      DEFAULT 1 COMMENT '状态: 1正常 0禁用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

INSERT INTO member(account_no, nickname, points, phone) VALUES
('11111111111', '常建航', 150, '13800001111'),
('22222222222', '王小明', 320, '13800002222'),
('33333333333', '李大白', 80,  '13800003333'),
('44444444444', '赵小红', 560, '13800004444');

-- sales_order 增加 member_id 字段
ALTER TABLE sales_order ADD COLUMN member_id INT DEFAULT NULL COMMENT '会员ID' AFTER user_id;
