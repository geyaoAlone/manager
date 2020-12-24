package com.geyao.manager.common.enums;

/**
 * 登陆方式枚举
 */
public enum UserInfoEnum {
    role_1("admin_00","平台管理员"),
    role_2("bbshop_11","理发店系统店主"),
    role_3("bbshop_12","理发店系统会员"),





    style_1("UM_P","用户名或手机号 + 密码"),
    style_2("UM_V","用户名或手机号 + 验证码"),
    style_3("M_V_R ","手机号 + 验证码(登陆等于注册)"),
    style_4("U_P_V","用户名 + 密码 + 验证码"),
    style_5("U","只要用户名，无需验证");

    public static UserInfoEnum[] USER_ROLE = {role_1,role_2,role_3};
    public static UserInfoEnum[] USER_LOGIN_STYLE = {style_1,style_2,style_3,style_4,style_5};

    private String value;
    private String name;

    UserInfoEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }


    public static UserInfoEnum getUserLoginStyle(String value) {
        for(UserInfoEnum uiEnum : USER_LOGIN_STYLE){
            if (uiEnum.getValue().equals(value)) {
                return uiEnum;
            }
        }
        return null;
    }


}
