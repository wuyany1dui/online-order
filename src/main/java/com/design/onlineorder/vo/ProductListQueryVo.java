package com.design.onlineorder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Created by DrEAmSs on 2022-04-26 16:48
 */
@Data
@ApiModel
public class ProductListQueryVo {

    @ApiModelProperty("餐品主键id")
    private String id;

    @ApiModelProperty("商店id")
    private String storeId;

    @ApiModelProperty("餐品类型")
    private String type;

    @ApiModelProperty("餐品起始价格")
    private BigDecimal startPrice;

    @ApiModelProperty("餐品结束价格")
    private BigDecimal endPrice;

    @ApiModelProperty("当前页（以0开始）")
    private Integer pageIndex;

    @ApiModelProperty("当前页容量")
    private Integer pageSize;
}
