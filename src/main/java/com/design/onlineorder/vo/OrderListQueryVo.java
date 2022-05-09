package com.design.onlineorder.vo;

import com.design.onlineorder.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by DrEAmSs on 2022-04-27 14:27
 */
@Data
@ApiModel
public class OrderListQueryVo {

    @ApiModelProperty("订单主键id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("订单状态")
    private OrderStatusEnum status;

    @ApiModelProperty("当前页（以0开始）")
    private Integer pageIndex = 0;

    @ApiModelProperty("当前页容量")
    private Integer pageSize = 0;
}
