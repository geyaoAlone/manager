package com.geyao.manager.common.dataobject.mongo;

import lombok.Data;

import java.util.Date;

@Data
public class NoAuthInvokeRecord {

    private String invokeUrl;
    private String invokeStyle;
    private Date invokeTime;
    private String invokeContent;
    private String responseData;
    private Integer result;

}
