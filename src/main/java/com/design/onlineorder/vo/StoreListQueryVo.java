package com.design.onlineorder.vo;

import lombok.Data;

/**
 * @author Created by DrEAmSs on 2022-05-09 9:35
 */
@Data
public class StoreListQueryVo {

    private String userId;

    private String type;

    private Integer pageIndex;

    private Integer pageSize;
}
