package com.design.onlineorder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author Created by DrEAmSs on 2022-04-26 15:57
 */
@Data
@TableName("t_product")
@ApiModel
public class Product {

    @ApiModelProperty("餐品主键id")
    private String id;

    @ApiModelProperty("商店id")
    private String storeId;

    @ApiModelProperty("餐品名称")
    private String name;

    @ApiModelProperty("餐品价格")
    private BigDecimal price;

    @ApiModelProperty("餐品描述")
    private String description;

    @ApiModelProperty("餐品销量")
    private Integer sales;

    @ApiModelProperty("餐品类型")
    private String type;

    @ApiModelProperty("餐品排序字段")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @ApiModelProperty("更新时间")
    private Timestamp updateTime;

    @ApiModelProperty("餐品首选图")
    private String firstImage;

    @ApiModelProperty("餐品图")
    private String image;
}
