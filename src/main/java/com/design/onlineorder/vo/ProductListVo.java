package com.design.onlineorder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Created by DrEAmSs on 2022-04-26 16:43
 */
@Data
@ApiModel
public class ProductListVo {

    @ApiModelProperty("餐品主键id")
    private String id;

    @ApiModelProperty("餐品名称")
    private String name;

    @ApiModelProperty("餐品价格")
    private BigDecimal price;

    @ApiModelProperty("餐品描述")
    private String description;

    @ApiModelProperty("餐品销量")
    private Integer sales;

    @ApiModelProperty("餐品首选图")
    private String firstImage;

    private String type;

    private String storeId;

    private String storeName;

    private String merchantId;

    private String merchantName;
}
