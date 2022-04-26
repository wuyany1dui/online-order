package com.design.onlineorder.vo;

import com.design.onlineorder.enums.UserTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by DrEAmSs on 2022-04-26 11:00
 */
@Data
@ApiModel
public class UserVo {

    @ApiModelProperty("用户主键id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("手机号码")
    private Long phoneNumber;

    @ApiModelProperty("电子邮箱")
    private String email;

    @ApiModelProperty("个性签名")
    private String sign;

    @ApiModelProperty("头像图片地址")
    private String avatar;

    @ApiModelProperty("用户类型 0：普通用户 1：商家 2：系统管理员")
    private UserTypeEnum type;
}
