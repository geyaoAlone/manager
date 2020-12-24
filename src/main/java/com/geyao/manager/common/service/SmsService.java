package com.geyao.manager.common.service;

import com.geyao.manager.common.db.redis.RedisDao;
import com.geyao.manager.common.utils.SmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class SmsService {

    @Autowired
    private RedisDao redisDao;

    public boolean sendValidateCode(String mobile,String type){
//        if(queryConfigValue(BuziConstant.CONFIG_ADMIN_CODE).contains(mobile)){
//            return true;
//        }
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);//生成短信验证码
        if(redisDao.set(mobile +"_" + type + "_validate_code",verifyCode,320)
                //&& SmsUtils.aliyunSendSms(mobile,verifyCode,queryConfigValue(BuziConstant.ALIYUN_KEY_ID_CODE),queryConfigValue(BuziConstant.ALIYUN_SECRET_CODE))
        ){
            log.info("[{}] get validate code[{}] success!!!",mobile,verifyCode);
            return true;
        } else {
            return false;
        }
    }


    public String checkValidateCode(String mobile,String type,String verifyCode){
//        if(queryConfigValue(BuziConstant.CONFIG_ADMIN_CODE).contains(mobile)){
//            return "";
//        }
        if(!redisDao.hasKey(mobile +"_" + type + "_validate_code")){
            return "验证码已过期";
        }
        String validateCode = (String)redisDao.get(mobile +"_" + type + "_validate_code");
        if(verifyCode.equals(validateCode)){
            return "";
        }else{
            return "验证码错误";
        }
    }
}
