package com.design.onlineorder.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.exception.MyException;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Created by DrEAmSs on 2022-04-26 8:57
 * JWT鉴权工具类
 *   头部：放有签名算法和令牌类型（这个就是JWT）
 *   载荷：你在令牌上附带的信息：比如用户的id，用户的电话号码，这样以后验证了令牌之后就可以直接从这里获取信息而不用再查数据库了
 *   签名：用来加令牌的
 */
public class JwtUtils {

    /**
     * 签发对象：这个用户的id
     * 签发时间：现在
     * 有效时间：1小时
     * 载荷内容：暂时设计为：这个人的名字，这个人的昵称
     * 加密密钥：这个人的id加上一串字符串
     */
    public static String createToken(String userId, String nickname, String username) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.HOUR, 1);
        Date expiresDate = nowTime.getTime();
        return JWT.create().withAudience(userId)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("username", username)    //载荷，随便写几个都可以
                .withClaim("nickname", nickname)
                .sign(Algorithm.HMAC256(userId));   //加密
    }

    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     */
    public static void verifyToken(String token, String secret) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new MyException(400, ResultEnum.TOKEN_ILLEGAL.getLabel());
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) {
        String audience;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
            throw new MyException(400, ResultEnum.TOKEN_PARSE_ERROR.getLabel());
        }
        return audience;
    }

    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }
}
