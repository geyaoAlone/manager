package com.geyao.manager.wechat.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微信公众号接口
 */
@RequestMapping("/weChat/officialAccount")
public interface WeChatOfficialAccountsController {

    @RequestMapping(value="/test", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
