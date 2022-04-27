package com.design.onlineorder.vo;

import com.design.onlineorder.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Created by DrEAmSs on 2022-04-27 14:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentListPageVo {

    private Integer count;

    private List<Comment> data;
}
