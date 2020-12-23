package com.geyao.manager.common.constants;

/**
 * 常量接口
 */
public interface SysConstant {

    //接口调用成功
    public static final int INVOKE_SUCCESS = 1;
    //接口调用失败
    public static final int INVOKE_FAIL = 0;
    //接口调用异常
    public static final int INVOKE_ERROR = -1;
    /**
     * jwt token前缀
     */
    String JWT_PREFIX = "Bearer ";

    /**
     * jwt请求头
     */
    String JWT_HEADER = "Authorization";

    /**
     * jwt失效时间
     */
    int JWT_TIMEOUT = 60 * 60 * 24 * 1000 * 7;

    /**
     * redis验证码key
     */
    String REDIS_IDENTIFYCODE_KEY_WRAPPER = "identifyCodeValidate[{0}]";

    /**
     * redis验证码失效时间
     */
    int REDIS_IDENTIFYCODE_TIMEOUT = 60;

    /**
     * redis token黑名单失效时间
     */
    int REDIS_TOKEN_TIMEOUT = 30000;
}
