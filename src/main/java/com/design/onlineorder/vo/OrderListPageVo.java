package com.design.onlineorder.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-04-27 14:41
 */
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class OrderListPageVo {

    private Integer count;

    private List<OrderVo> data;
}
