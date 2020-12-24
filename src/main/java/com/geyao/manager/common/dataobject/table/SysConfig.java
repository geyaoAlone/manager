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
@TableName("t_sys_config")
public class SysConfig implements Serializable {

  @TableId(type = IdType.AUTO)
  private long id;

  private String name;

  private String value;

  private String description;

  private Date createTime;

  private Integer status;

  private String configType;

}
