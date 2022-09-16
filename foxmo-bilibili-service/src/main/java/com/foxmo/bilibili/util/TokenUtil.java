package com.foxmo.bilibili.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.foxmo.bilibili.domain.exception.ConditionException;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class TokenUtil {
    private static final String issuer = "签发者";

    /**
     * 基于JWT的用户token验证，生成用户身份令牌
     * @param userId
     * @return  返回用户身份令牌
     * @throws Exception
     */
    public static String generateToken(Long userId) throws Exception{
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND,60);
        return JWT.create()
                .withKeyId(String.valueOf(userId))
                .withExpiresAt(calendar.getTime())  //到期时间
                .sign(algorithm);
    }

    /**
     * 验证token是否合法
     * @param token
     * @return 若验证成功，返回 userId
     */
    public static Long verifyToken(String token){
        try{
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();

            log.info("+++++++++++userId = {}+++++++++++++",userId);

            return Long.valueOf(userId);
        }catch(TokenExpiredException e){
            throw new ConditionException("555","token已过期！");
        }catch(Exception e){
            throw new ConditionException("非法用户token！");
        }
    }
}
