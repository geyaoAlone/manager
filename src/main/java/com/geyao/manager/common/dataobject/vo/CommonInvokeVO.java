package com.geyao.manager.common.dataobject.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class CommonInvokeVO {
    /**
     * 掉用码
     */
    private String invokeCode;

    private JSONObject invokeData;

}
