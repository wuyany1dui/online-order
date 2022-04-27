package com.design.onlineorder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-04-26 16:52
 */
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListPageVo {

    @ApiModelProperty("总数量")
    private Integer count;

    @ApiModelProperty("当前页数据")
    private List<CategoryListVo> data;
}
