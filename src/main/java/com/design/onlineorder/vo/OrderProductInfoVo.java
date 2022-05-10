package com.design.onlineorder.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:26
 */
@Data
@ApiModel
public class OrderProductInfoVo {

    @ApiModelProperty("商品id")
    private String productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("图片首选图地址")
    private String firstImage;

    @ApiModelProperty("商品个数")
    private Integer count;

    @ApiModelProperty("商品单价")
    private BigDecimal price;
}
