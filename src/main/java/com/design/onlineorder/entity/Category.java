package com.design.onlineorder.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Created by DrEAmSs on 2022-04-27 9:08
 */
@Data
@TableName("t_category")
@ApiModel
public class Category {

    @ApiModelProperty("分类主键id")
    private String id;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @ApiModelProperty("更新时间")
    private Timestamp updateTime;

    @ApiModelProperty("逻辑删除字段 0：未删除 1：已删除")
    @TableLogic
    private Integer deleted;
}
