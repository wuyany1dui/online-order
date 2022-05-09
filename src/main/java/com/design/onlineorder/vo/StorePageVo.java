package com.design.onlineorder.vo;

import com.design.onlineorder.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-05-09 9:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorePageVo {

    private Integer count;

    private List<Store> data;
}
