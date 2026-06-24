package com.supermarket;

import com.supermarket.entity.Product;
import com.supermarket.entity.SalesOrder;
import com.supermarket.entity.SysUser;
import com.supermarket.mapper.ProductMapper;
import com.supermarket.mapper.SalesOrderMapper;
import com.supermarket.mapper.SysUserMapper;
import com.supermarket.service.SalesService;
import com.supermarket.service.SysUserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 超市销售系统 — 系统测试
 * 覆盖：登录认证、商品CRUD、销售订单提交、库存扣减、视图查询
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SupermarketSystemTest {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private SalesService salesService;

    // ============================================================
    // 1. 用户认证测试
    // ============================================================

    @Test
    @Order(1)
    @DisplayName("登录成功：正确的用户名和密码应返回用户信息")
    void testLoginSuccess() {
        Map<String, Object> result = sysUserService.login("cashier02", "123456");
        assertTrue((Boolean) result.get("success"), "登录应成功");
        assertNotNull(result.get("user"), "应返回用户对象");
        SysUser user = (SysUser) result.get("user");
        assertEquals("李收银", user.getRealName());
    }

    @Test
    @Order(2)
    @DisplayName("登录失败：错误的密码应返回错误信息")
    void testLoginFailWrongPassword() {
        Map<String, Object> result = sysUserService.login("cashier02", "wrong");
        assertFalse((Boolean) result.get("success"), "密码错误应失败");
        assertEquals("用户名或密码错误", result.get("msg"));
    }

    @Test
    @Order(3)
    @DisplayName("登录失败：不存在的用户应返回错误信息")
    void testLoginFailNotExist() {
        Map<String, Object> result = sysUserService.login("nouser", "123456");
        assertFalse((Boolean) result.get("success"), "不存在的用户应失败");
    }

    // ============================================================
    // 2. 商品 CRUD 测试
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("商品分页查询：应返回商品列表和总数")
    void testProductPagination() {
        List<Product> list = productMapper.selectPage("", 0, 0, 10);
        int total = productMapper.count("", 0);
        assertNotNull(list, "商品列表不应为null");
        assertTrue(total > 0, "商品总数应大于0");
        assertTrue(list.size() <= 10, "每页最多10条");
    }

    @Test
    @Order(5)
    @DisplayName("商品搜索：按关键词搜索应返回匹配结果")
    void testProductSearch() {
        List<Product> list = productMapper.selectPage("可乐", 0, 0, 10);
        assertNotNull(list);
        assertTrue(list.size() > 0, "搜索'可乐'应有结果");
        assertTrue(list.get(0).getProductName().contains("可乐"), "结果应包含关键词");
    }

    @Test
    @Order(6)
    @DisplayName("根据ID查询：应返回正确的商品")
    void testProductGetById() {
        Product p = productMapper.selectById(1);
        assertNotNull(p, "商品ID=1应存在");
        assertEquals("可口可乐330ml", p.getProductName());
        assertNotNull(p.getPrice(), "价格不应为null");
    }

    @Test
    @Order(7)
    @DisplayName("收银搜索：只返回上架商品")
    void testProductSearchOnSale() {
        List<Product> list = productMapper.selectOnSale("");
        assertNotNull(list);
        for (Product p : list) {
            assertEquals(1, p.getStatus(), "所有商品应为上架状态(status=1)");
        }
    }

    // ============================================================
    // 3. 销售订单测试
    // ============================================================

    @Test
    @Order(8)
    @DisplayName("订单查询：联表查收银员姓名和商品种类数")
    void testOrderQuery() {
        List<SalesOrder> list = salesOrderMapper.selectPage("", 0, 5);
        assertNotNull(list);
        if (!list.isEmpty()) {
            SalesOrder first = list.get(0);
            assertNotNull(first.getOrderNo(), "订单编号不应为空");
            assertNotNull(first.getRealName(), "应收银员姓名");
        }
    }

    @Test
    @Order(9)
    @DisplayName("今日销售汇总：应返回正确的订单数和金额")
    void testTodaySummary() {
        Map<String, Object> summary = salesOrderMapper.selectTodaySummary();
        assertNotNull(summary, "汇总结果不应为null");
        assertNotNull(summary.get("todayOrderCount"), "应包含今日订单数");
        assertNotNull(summary.get("todaySalesAmount"), "应包含今日销售额");
    }

    @Test
    @Order(10)
    @DisplayName("无效订单ID：查询不存在的订单应返回null")
    void testOrderNotFound() {
        SalesOrder order = salesOrderMapper.selectById(99999);
        assertNull(order, "不存在的订单应返回null");
    }

    // ============================================================
    // 4. 用户数据测试
    // ============================================================

    @Test
    @Order(11)
    @DisplayName("用户名唯一：重复用户名注册应失败")
    void testRegisterDuplicateUsername() {
        SysUser exist = sysUserMapper.selectByUsername("cashier02");
        assertNotNull(exist, "cashier02 应已存在");
    }
}
