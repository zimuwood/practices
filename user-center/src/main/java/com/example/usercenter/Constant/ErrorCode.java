package com.example.usercenter.Constant;

import lombok.AllArgsConstructor;

/**
 * 错误码
 * @author cloudyW
 */
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(10000, "success", null),
    PARAMS_ERROR(40000, "请求参数错误", null),
    NULL_ERROR(40001, "请求数据为空", null),
    NOT_LOGIN(40100, "未登录", null),
    NO_AUTH(40101, "无权限", null),
    USER_INFO_ERROR(40200, "用户输入信息错误", null),
    USER_DUPLICATE(40201, "用户重复", null),
    USER_NOT_EXIST(40202, "用户不存在", null),
    SYSTEM_ERROR(50000, "系统内部异常", null),
    DATA_BASE_ERROR(50100, "数据库错误", null);

    /**
     * 错误码
     */
    private final int code;
    private final String message;
    private final String description;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
