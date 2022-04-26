package com.design.onlineorder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author Created by DrEAmSs on 2022-04-26 13:27
 */
@Data
@TableName("t_store")
@ApiModel
public class Store {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("商店名称")
    private String name;

    @ApiModelProperty("所属用户id")
    private String userId;

    @ApiModelProperty("商店描述")
    private String description;

    @ApiModelProperty("店铺销量")
    private Integer sales;

    @ApiModelProperty("店铺销售额")
    private BigDecimal salesVolume;

    @ApiModelProperty("商品类型，存json，商家自己定义")
    private String type;

    @ApiModelProperty("排序字段")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @ApiModelProperty("更新时间")
    private Timestamp updateTime;
}
