超市前台销售系统 — E-R 图
===========================

## 使用方式
- **Mermaid**: 复制到 https://mermaid.live 实时渲染
- **PlantUML**: IDEA 安装 PlantUML 插件后可直接预览
- **报告**: 截图后放入课程设计报告


## Mermaid 版

```mermaid
erDiagram
    sys_user ||--o{ sales_order : "收银 (user_id)"
    category ||--o{ product : "归属 (category_id)"
    sales_order ||--o{ sales_detail : "包含 (order_id)"
    product ||--o{ sales_detail : "明细 (product_id)"

    sys_user {
        int user_id PK "用户ID"
        varchar username UK "登录账号"
        varchar password "密码"
        varchar real_name "真实姓名"
        tinyint role_type "1管理员2收银员"
        varchar phone "电话"
        tinyint status "1正常0禁用"
        datetime create_time "创建时间"
    }

    category {
        int category_id PK "分类ID"
        varchar category_name UK "分类名称"
        varchar description "描述"
        datetime create_time "创建时间"
    }

    product {
        int product_id PK "商品ID"
        varchar product_name "名称"
        int category_id FK "分类"
        decimal price "售价"
        int stock "库存"
        varchar unit "单位"
        varchar barcode "条形码"
        tinyint status "1上架0下架"
        datetime create_time "创建时间"
    }

    sales_order {
        int order_id PK "订单ID"
        varchar order_no UK "SO+时间戳"
        int user_id FK "收银员"
        decimal total_amount "总金额"
        decimal discount_amount "优惠"
        decimal paid_amount "实收"
        decimal change_amount "找零"
        tinyint payment_method "1现金2微信3支付宝"
        tinyint order_status "1完成2退款"
        varchar remark "备注"
        datetime create_time "创建时间"
    }

    sales_detail {
        int detail_id PK "明细ID"
        int order_id FK "订单"
        int product_id FK "商品"
        varchar product_name "商品名(冗余)"
        decimal price "单价"
        int quantity "数量"
        decimal subtotal "小计"
    }
```


## PlantUML 版 (IDEA 原生支持)

```plantuml
@startuml

entity "sys_user" as sys_user {
    * user_id : INT <<PK>>
    * username : VARCHAR(30) <<UK>>
    * password : VARCHAR(100)
    real_name : VARCHAR(20)
    role_type : TINYINT
    status : TINYINT
    create_time : DATETIME
}

entity "category" as category {
    * category_id : INT <<PK>>
    * category_name : VARCHAR(50) <<UK>>
    description : VARCHAR(200)
    create_time : DATETIME
}

entity "product" as product {
    * product_id : INT <<PK>>
    * product_name : VARCHAR(100)
    category_id : INT <<FK>>
    price : DECIMAL(10,2)
    stock : INT
    unit : VARCHAR(10)
    barcode : VARCHAR(50)
    status : TINYINT
    create_time : DATETIME
}

entity "sales_order" as sales_order {
    * order_id : INT <<PK>>
    * order_no : VARCHAR(32) <<UK>>
    user_id : INT <<FK>>
    total_amount : DECIMAL(10,2)
    discount_amount : DECIMAL(10,2)
    paid_amount : DECIMAL(10,2)
    change_amount : DECIMAL(10,2)
    payment_method : TINYINT
    order_status : TINYINT
    create_time : DATETIME
}

entity "sales_detail" as sales_detail {
    * detail_id : INT <<PK>>
    order_id : INT <<FK>>
    product_id : INT <<FK>>
    product_name : VARCHAR(100)
    price : DECIMAL(10,2)
    quantity : INT
    subtotal : DECIMAL(10,2)
}

sys_user ||--o{ sales_order
category ||--o{ product
sales_order ||--o{ sales_detail
product ||--o{ sales_detail

@enduml
```


## 关系汇总

```
         ┌──────────┐        ┌──────────────┐
         │ sys_user │ 1 ── N │ sales_order  │
         │  (用户)  │        │  (销售订单)  │
         └──────────┘        └──────┬───────┘
                                    │ 1
         ┌──────────┐               │        ┌──────────────┐
         │ category │ 1 ── N ┌──────┘  N ─── │ sales_detail │
         │  (分类)  │        │                │  (销售明细)  │
         └──────────┘   ┌────┴──────┐         └──────────────┘
                        │  product  │
                        │  (商品)   │
                        └───────────┘

外键：
  sales_order.user_id → sys_user.user_id
  product.category_id → category.category_id
  sales_detail.order_id → sales_order.order_id  (ON DELETE CASCADE)
  sales_detail.product_id → product.product_id
```
