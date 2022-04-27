package com.design.onlineorder.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
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

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @TableLogic
    @ApiModelProperty("逻辑删除字段 0：未删除 1：已删除")
    private Integer deleted;
}
