package com.geyao.manager.common.web;

import com.geyao.manager.common.dataobject.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 短信服务接口
 */
@RequestMapping("/sms")
public interface SmsInterface {

    @RequestMapping("/sendValidateCode")
    public ResultVO sendValidateCode(String mobile, String type);


    @RequestMapping("/checkValidateCode")
    public ResultVO checklidateCode(String mobile, String type,String validateCode);

}
