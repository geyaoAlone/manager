package com.geyao.manager.common.web.impl;

import com.alibaba.fastjson.JSONObject;
import com.geyao.manager.common.constants.SysConstant;
import com.geyao.manager.common.dataobject.table.SysMerchant;
import com.geyao.manager.common.dataobject.vo.LoginVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.dataobject.table.SysUser;

import com.geyao.manager.common.db.redis.RedisDao;
import com.geyao.manager.common.enums.UserInfoEnum;
import com.geyao.manager.common.service.SmsService;
import com.geyao.manager.common.service.UserService;
import com.geyao.manager.common.utils.JwtTokenUtil;
import com.geyao.manager.common.web.SecurityAuthInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
public class SecurityAuthController implements SecurityAuthInterface {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisDao redis;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserService userService;

    @Override
    public ResultVO test(String str) {
        System.out.println(str);
        String token = jwtTokenUtil.generateToken(str);
        return new ResultVO("",token);
    }

    @Override
    public ResultVO checkToken(String token) {
        return null;
    }

    @Override
    public ResultVO refreshToken(String token) {
        return null;
    }

    @Override
    public ResultVO login(LoginVO loginVO) {
        String checkRes = loginVO.loginBaseCheck();
        if(!"".equals(checkRes)){
            return new ResultVO(checkRes);
        }

        SysMerchant merchant = userService.querySysMerchantInfo(loginVO.getModuleCode(),loginVO.getMerchantCode());
        if(Objects.isNull(merchant)) {
            return new ResultVO("非法登陆");
        }
        SysUser user = userService.querySysUser(loginVO.getModuleCode(),loginVO.getMerchantCode(), loginVO.getUsername(), loginVO.getMobile());;
        System.out.println(UserInfoEnum.getUserLoginStyle(merchant.getLoginStyle()));
        switch (UserInfoEnum.getUserLoginStyle(merchant.getLoginStyle())) {
            //"用户名或手机号 + 密码
            case style_1:
                if(Objects.isNull(user)) {
                    return new ResultVO("用户不存在");
                }
                if(!loginVO.getPassword().equals(user.getPassword())) {
                    return new ResultVO("登录信息错误！",false);
                }
                break;
            //用户名或手机号 + 验证码
            case style_2 :
                checkRes = loginVO.loginValidateCodeCheck();
                if(!"".equals(checkRes)){
                    return new ResultVO(checkRes);
                }
                if(Objects.isNull(user)) {
                    return new ResultVO("用户不存在");
                }
                checkRes = smsService.checkValidateCode(loginVO.getMobile(),loginVO.getValidateType(),loginVO.getValidateCode());
                if(!"".equals(checkRes)){
                    return new ResultVO(checkRes);
                }
                break;
            //手机号 + 验证码(登陆等于注册)
            case style_3 :
                //TODO
                break;

            //用户名 + 密码 + 验证码
            case style_4 :
                return new ResultVO("登陆方式过于严格！开发中...");

            //只要用户名，无需验证
            case style_5 :
                if(Objects.isNull(user)) {
                    return new ResultVO("用户不存在");
                }
                break;

            default: return new ResultVO("登陆方式未知！无法登陆");
        }
        String token = jwtTokenUtil.generateToken(loginVO.getModuleCode()+"_"+loginVO.getMerchantCode()+"_"+loginVO.getUsername());
        if(token == null){
            return new ResultVO("登陆失败！授权失败");
        }
        if(!redis.set(loginVO.getModuleCode()+"_"+loginVO.getMerchantCode()+"_"+loginVO.getUsername(),user, SysConstant.JWT_TIMEOUT)){
            return new ResultVO("登陆失败！信息缓存失败！");
        }
        user.setPassword("");
        JSONObject data = new JSONObject();
        data.put("user",user);
        data.put("token",token);
        return new ResultVO("登陆成功",data);
    }
}
