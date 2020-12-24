package com.geyao.manager.common.dataobject.table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("t_sys_merchant")
public class SysMerchant implements Serializable {

  private String moduleCode;

  @TableId(value = "merchant_code")
  private String merchantCode;

  private String merchantName;

  private Integer status;

  private Date createTime;

  /**
   * 登陆方式：
   * 枚举LoginStyleEnum value值
   */
  private String loginStyle;



}
