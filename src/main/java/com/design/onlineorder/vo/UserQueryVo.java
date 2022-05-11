package com.design.onlineorder.vo;

import lombok.Data;

/**
 * @author Created by DrEAmSs on 2022-05-11 10:02
 */
@Data
public class UserQueryVo {

    private String username;
    private String nickname;

    private Integer pageIndex;
    private Integer pageSize;
}
