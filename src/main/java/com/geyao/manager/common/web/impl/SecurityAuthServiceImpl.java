package com.geyao.manager.common.web.impl;

import com.geyao.manager.common.dataobject.ResultVO;
import com.geyao.manager.common.dataobject.SysUser;

import com.geyao.manager.common.db.redis.RedisDao;
import com.geyao.manager.common.utils.JwtTokenUtil;
import com.geyao.manager.common.web.SecurityAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SecurityAuthServiceImpl implements SecurityAuthService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisDao redis;

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
    public ResultVO login(SysUser user) {
        return null;
    }
}
