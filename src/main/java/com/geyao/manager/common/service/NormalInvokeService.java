package com.geyao.manager.common.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geyao.manager.common.constants.SysConstant;
import com.geyao.manager.common.dataobject.mongo.NoAuthInvokeRecord;
import com.geyao.manager.common.dataobject.table.SysUser;
import com.geyao.manager.common.dataobject.vo.CommonInvokeVO;
import com.geyao.manager.common.dataobject.vo.ModuleInvokeUrlVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.db.mongo.NoAuthInvokeDao;
import com.geyao.manager.common.db.mysql.mapper.ModuleInvokeInfoMapper;
import com.geyao.manager.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class NormalInvokeService {

    @Autowired
    private ModuleInvokeInfoMapper moduleInvokeInfoMapper;
    @Autowired
    private NoAuthInvokeDao noAuthInvokeDao;

    public ResultVO checkCode(String invokeCode){
        ModuleInvokeUrlVO urlVO = moduleInvokeInfoMapper.queryModuleInvokeUrl(invokeCode);
        if(Objects.isNull(urlVO)){
            return new ResultVO(SysConstant.INVOKE_FAIL,"调用失败！code非法");
        }
        if(urlVO.getInvokeUrl().startsWith("http://")
            || urlVO.getInvokeUrl().startsWith("https://")){
            urlVO.setFullUrl(urlVO.getInvokeUrl());
        } else {
            urlVO.setFullUrl(urlVO.getBaseUrl() + urlVO.getInvokeUrl());
        }
        return new ResultVO("code合法",urlVO);
    }

    public ResultVO getInvoke_auth(SysUser user,ModuleInvokeUrlVO urlVO){
        try {
            log.info("### start get invoke {} ------> params:{}",urlVO.getInvokeCode(),JSON.toJSONString(user));
            String s = HttpClientUtil.postJsonData(urlVO.getFullUrl(), JSON.toJSONString(user));
            log.info("### post get {} return ------> {}",urlVO.getInvokeCode(),s);
            return JSON.parseObject(s, ResultVO.class);

        } catch (RuntimeException e) {
            log.info("### get invoke {} return exception! {}",urlVO.getFullUrl(), e.getMessage());
            return new ResultVO(SysConstant.INVOKE_ERROR,"调用异常！");
        }
    }



    public ResultVO postInvoke_auth(SysUser user,ModuleInvokeUrlVO urlVO,CommonInvokeVO vo){
        JSONObject data = vo.getData();
        data.put("sysUser",user);
        try {
            log.info("### start post invoke {} ------> params:{}",urlVO.getInvokeCode(),JSON.toJSONString(data));
            String s = HttpClientUtil.postJsonData(urlVO.getFullUrl(), JSON.toJSONString(data));
            log.info("### post invoke {} return ------> {}",urlVO.getInvokeCode(),s);
            return JSON.parseObject(s, ResultVO.class);

        } catch (RuntimeException e) {
            log.info("### post invoke {} return exception! {}",urlVO.getFullUrl(), e.getMessage());
            return new ResultVO(SysConstant.INVOKE_ERROR,"调用异常！");
        }
    }

    public ResultVO getInvoke(ModuleInvokeUrlVO urlVO) {
        NoAuthInvokeRecord record = new NoAuthInvokeRecord();
        record.setInvokeUrl(urlVO.getFullUrl());
        record.setInvokeStyle("GET");
        record.setInvokeTime(new Date());
        record.setInvokeContent(null);
        try {
            log.info("### start no auth get invoke {}",urlVO.getInvokeCode());
            String s = HttpClientUtil.getData(urlVO.getFullUrl(),null,1000000);
            log.info("### post get {} return ------> {}",urlVO.getInvokeCode(),s);
            record.setResponseData(s);
            record.setResult(1);
            return JSON.parseObject(s, ResultVO.class);

        } catch (RuntimeException e) {
            log.info("### get invoke {} return exception! {}",urlVO.getFullUrl(), e.getMessage());
            record.setResponseData(null);
            record.setResult(0);
            return new ResultVO(SysConstant.INVOKE_ERROR,"调用异常！");
        }finally {
            noAuthInvokeDao.saveInvokeRecord(record);
        }
    }

    public ResultVO postInvoke(ModuleInvokeUrlVO urlVO, CommonInvokeVO vo) {
        NoAuthInvokeRecord record = new NoAuthInvokeRecord();
        record.setInvokeUrl(urlVO.getFullUrl());
        record.setInvokeStyle("POST");
        record.setInvokeTime(new Date());
        JSONObject data = vo.getData();
        record.setInvokeContent(JSON.toJSONString(data));
        try {
            log.info("### start post invoke {} ------> params:{}",urlVO.getInvokeCode(),JSON.toJSONString(data));
            String s = HttpClientUtil.postJsonData(urlVO.getFullUrl(), JSON.toJSONString(data));
            log.info("### post invoke {} return ------> {}",urlVO.getInvokeCode(),s);
            record.setResponseData(s);
            record.setResult(1);
            if(s.startsWith("{") && s.contains("code") && s.contains("msg") && s.contains("data") && s.endsWith("}")){
                return JSON.parseObject(s, ResultVO.class);
            }else if(s.startsWith("{") && s.endsWith("}")){
                JSONObject jsonObject = JSON.parseObject(s);
                return new ResultVO("调用成功",jsonObject);
            }else {
                return new ResultVO("调用成功",s);
            }
        } catch (RuntimeException e) {
            log.info("### post invoke {} return exception! {}",urlVO.getFullUrl(), e.getMessage());
            record.setResponseData(null);
            record.setResult(0);
            return new ResultVO(SysConstant.INVOKE_ERROR,"调用异常！");
        }finally {
            noAuthInvokeDao.saveInvokeRecord(record);
        }
    }

    public ResultVO transfers_post(String url,String param) {
        NoAuthInvokeRecord record = new NoAuthInvokeRecord();
        record.setInvokeUrl(url);
        record.setInvokeStyle("POST");
        record.setInvokeTime(new Date());
        record.setInvokeContent(param);
        try {
            log.info("### start post invoke {} ------> params:{}",url,param);
            String s = HttpClientUtil.postJsonData(url,param);
            log.info("### post invoke {} return ------> {}",url,s);
            record.setResponseData(s);
            record.setResult(1);
            if(s.startsWith("{") && s.contains("code") && s.contains("msg") && s.contains("data") && s.endsWith("}")){
                return JSON.parseObject(s, ResultVO.class);
            }else if(s.startsWith("{") && s.endsWith("}")){
                JSONObject jsonObject = JSON.parseObject(s);
                return new ResultVO("调用成功",jsonObject);
            }else {
                return new ResultVO("调用成功",s);
            }
        } catch (RuntimeException e) {
            log.info("### post invoke {} return exception! {}",url, e.getMessage());
            record.setResponseData(null);
            record.setResult(0);
            return new ResultVO(SysConstant.INVOKE_ERROR,"调用异常！");
        }finally {
            noAuthInvokeDao.saveInvokeRecord(record);
        }
    }

    public static void main(String[] args) {
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add("abc");
        orderNoList.add("def");
        orderNoList.add("sss");
        System.out.println(orderNoList.contains("def"));
    }

}
