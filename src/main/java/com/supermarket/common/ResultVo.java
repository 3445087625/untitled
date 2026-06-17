package com.supermarket.common;

/**
 * 统一响应结果类
 * 前端Ajax请求均返回此格式的JSON
 */
public class ResultVo<T> {
    private Integer code;    // 状态码：200成功 500失败
    private String msg;      // 提示信息
    private T data;          // 返回数据

    private ResultVo() {}

    public static <T> ResultVo<T> success(T data) {
        ResultVo<T> vo = new ResultVo<>();
        vo.code = 200;
        vo.msg = "操作成功";
        vo.data = data;
        return vo;
    }

    public static <T> ResultVo<T> success(String msg, T data) {
        ResultVo<T> vo = new ResultVo<>();
        vo.code = 200;
        vo.msg = msg;
        vo.data = data;
        return vo;
    }

    public static <T> ResultVo<T> error(String msg) {
        ResultVo<T> vo = new ResultVo<>();
        vo.code = 500;
        vo.msg = msg;
        vo.data = null;
        return vo;
    }

    public static <T> ResultVo<T> error(Integer code, String msg) {
        ResultVo<T> vo = new ResultVo<>();
        vo.code = code;
        vo.msg = msg;
        vo.data = null;
        return vo;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
