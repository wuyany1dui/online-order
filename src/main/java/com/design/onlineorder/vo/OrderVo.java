package com.design.onlineorder.vo;

import com.design.onlineorder.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:25
 */
@Data
@ApiModel
public class OrderVo {

    @ApiModelProperty("订单主键id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("商店id")
    private String storeId;

    @ApiModelProperty("商店名称")
    private String storeName;

    @ApiModelProperty("商店所有人id")
    private String merchantId;

    @ApiModelProperty("商店所有人名称")
    private String merchantName;

    @ApiModelProperty("订单的商品信息")
    private List<OrderProductInfoVo> productInfos;

    @ApiModelProperty("订单金额")
    private BigDecimal price;

    @ApiModelProperty("订单创建时间")
    private Timestamp createTime;

    @ApiModelProperty("订单支付时间")
    private Timestamp payTime;

    @ApiModelProperty("订单状态 0：未支付 1：已支付 2：已过期")
    private OrderStatusEnum status;
}
