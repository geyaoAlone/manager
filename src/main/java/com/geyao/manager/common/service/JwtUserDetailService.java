package com.geyao.manager.common.service;

import com.geyao.manager.common.dataobject.JwtUserDetails;
import com.geyao.manager.common.dataobject.table.SysUser;
import com.geyao.manager.common.db.redis.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailService implements UserDetailsService {

    @Autowired
    private RedisDao userService;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        SysUser user = (SysUser)userService.get(mobile);
        return new JwtUserDetails(null, user);
    }
}