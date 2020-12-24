package com.geyao.manager.common.dataobject.vo;

import com.geyao.manager.common.dataobject.table.SysUser;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class LoginVO extends SysUser {


    /**
     * 验证码类型
     */
    private String validateType;

    /**
     * 验证码
     */
    private String validateCode;

    public String loginBaseCheck(){
        if(StringUtils.isBlank(getModuleCode()) || StringUtils.isBlank(getMerchantCode())){
            return "商户信息为空！";
        }
        if(StringUtils.isBlank(getUsername()) && StringUtils.isBlank(getMobile())){
            return "登陆信息为空！";
        }
        return "";
    }

    public String loginValidateCodeCheck(){
        if(StringUtils.isBlank(validateType) || StringUtils.isBlank(validateCode)){
            return "验证码为空！";
        }
        return "";
    }
}
