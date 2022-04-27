package com.design.onlineorder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by DrEAmSs on 2022-04-27 10:06
 */
@Data
@ApiModel
public class CategoryListVo {

    @ApiModelProperty("分类主键id")
    private String id;

    @ApiModelProperty("分类名称")
    private String name;
}
