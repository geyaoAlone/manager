package com.geyao.manager.common.filter;

import com.alibaba.fastjson.JSON;
import com.geyao.manager.common.constants.SysConstant;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.dataobject.table.SysUser;
import com.geyao.manager.common.db.redis.RedisDao;
import com.geyao.manager.common.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * token校验过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String[] PASS_FILTER_URL = {"/security","/danger"};
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisDao redisDao;

    private boolean checkPassUrl(String url){
        for(String str:PASS_FILTER_URL){
            if(url.startsWith(str)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = request.getRequestURI();
        if(!checkPassUrl(url)){
            LOG.info("request url [{}]", url);
            String header = request.getHeader(SysConstant.JWT_HEADER);
            if (header != null && header.startsWith(SysConstant.JWT_PREFIX)) {
                String token = header.replace(SysConstant.JWT_PREFIX, "");
                String userStr = jwtTokenUtil.getMobileFromToken(token);
                if (userStr != null && redisDao.hasKey(userStr) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    boolean canPass = false;
                    SysUser user =JSON.parseObject ((String) redisDao.get(userStr),SysUser.class);
                    boolean isValidate = jwtTokenUtil.validateToken(token, user);
                    boolean isExpired = jwtTokenUtil.isExpired(token);


                    if (isValidate && !isExpired) {
                        canPass = true;

                        //token一天之内要过期，刷新token
                        if (jwtTokenUtil.isExpireSoon(token)) {
                            System.out.println("token past coming soon !!!");
                            String newToken = jwtTokenUtil.refreshToken(token);

                            //旧token放入黑名单,保留一分钟，解决并发过程中新token刷新同时，旧token请求失效的情况
                            redisDao.set(token, newToken, SysConstant.REDIS_TOKEN_TIMEOUT);

                            //response中返回新token
                            response.setHeader(SysConstant.JWT_HEADER, newToken);
                        }
                    } else if (isValidate) {
                        //如果token在黑名单中，说明该token已经被并发请求刷新，并且已经返回了新token，但该token一分钟之内就会失效
                        Object newToken = redisDao.get(token);
                        if (!Objects.isNull(newToken)) {
                            canPass = true;

                            //response中返回新token
                            response.setHeader(SysConstant.JWT_HEADER, SysConstant.JWT_PREFIX + newToken);
                        }
                    }

                    if (canPass) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json; charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.append(JSON.toJSONString(new ResultVO(SysConstant.INVOKE_ERROR, "token check is error")));
                    }
                } else {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json; charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.append(JSON.toJSONString(new ResultVO(SysConstant.INVOKE_ERROR, "token is error")));

                }
            } else {

                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                out.append(JSON.toJSONString(new ResultVO(SysConstant.INVOKE_ERROR, "invoke no token")));
            }
        }else{
            LOG.info("[{}] no need check",url);
        }
        chain.doFilter(request, response);
    }
}
