package com.design.onlineorder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Created by DrEAmSs on 2022-04-27 10:28
 */
@Data
@TableName("t_comment")
@ApiModel
public class Comment {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("餐品id")
    private String productId;

    @ApiModelProperty("餐品名称")
    private String productName;

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;
}
