package com.geyao.manager.wechat.web.impl;

import com.geyao.manager.wechat.utils.SignUtils;
import com.geyao.manager.wechat.web.WeChatOfficialAccountsController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@RestController
public class WeChatOfficialAccountsControllerImpl implements WeChatOfficialAccountsController {

    @Override
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp，nonce参数
        String signature = request.getParameter("signature");
        log.info("----->signature:{}",signature);
        //时间戳
        String timestamp = request.getParameter("timestamp");
        log.info("----->timestamp:{}",timestamp);
        //随机数
        String nonce = request.getParameter("nonce");
        log.info("----->nonce:{}",nonce);
        //随机字符串
        String echostr = request.getParameter("echostr");
        log.info("----->echostr:{}",echostr);
        if(SignUtils.checkSignature(signature, timestamp, nonce)) {
            log.info("[signature: "+signature + "]<-->[timestamp: "+ timestamp+"]<-->[nonce: "+nonce+"]<-->[echostr: "+echostr+"]");
            response.getOutputStream().println(echostr);
        }

    }
}
