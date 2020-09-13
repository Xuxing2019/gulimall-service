package com.atguigu.common.exception;

/**
 * @author xuxing
 * @date 2020/7/29
 */
public enum GulimallExceptionEnum {
    UNKNOWN_EXCEPTION(10001,"未知异常"),
    PARAM_VERIFY_EXCEPTION(10002,"数据校验异常");
    private Integer code;
    private String message;
    GulimallExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
