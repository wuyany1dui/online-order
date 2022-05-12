package com.design.onlineorder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by DrEAmSs on 2022-04-27 10:44
 */
@Data
@ApiModel
public class CommentVo {

    @ApiModelProperty("商品id")
    private String productId;

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("评论内容")
    private String content;
}
