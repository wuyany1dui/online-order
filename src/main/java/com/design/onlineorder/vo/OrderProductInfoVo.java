package com.design.onlineorder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:26
 */
@Data
@ApiModel
public class OrderProductInfoVo {

    @ApiModelProperty("商品id")
    private String productId;

    @ApiModelProperty("商品个数")
    private Integer count;
}