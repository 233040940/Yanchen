package com.local.common.utils;

import com.local.common.id.snowflake.SnowFlakeIDGenerator;
import io.jsonwebtoken.*;
import org.springframework.util.Base64Utils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * @author yc
 * @project yanchen
 * @description JWT 工具类
 * @date 2020-06-12 08:28
 */
public class JwtProvider {


    private JwtProvider(){

        throw new RuntimeException("JwtProvider is tool class,Not support instanced");
    }

    /**
     * Token加密密匙
     */
    private static final String TOKEN_SECRET = ".~!@@1av23gAbc#$%^&*(";
    /**
     * 加密类型
     */
    private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;


    private static Key generateKey() {
        // 将密码转换为字节数组
        byte[] bytes = Base64Utils.encode(TOKEN_SECRET.getBytes());
        // 根据指定的加密方式，生成密钥
        return new SecretKeySpec(bytes, JWT_ALG.getJcaName());
    }

    /**
     * 创建token
     *
     * @param sub token所面向的用户
     * @param expire token有效时间
     * @param aud 接收token的一方
     * @param jti token的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @param iss token签发者
     * @return 加密后的token字符串
     */
    public static String createToken(String sub, long expire,String aud, String jti, String iss) {
        return Jwts.builder()
                .signWith(JWT_ALG, generateKey())
                .setSubject(sub)
                .setAudience(aud)
                .setId(jti)
                .setIssuer(iss)
                .setExpiration(new Date(DateTimeHelper.getSystemTimeStampToMillis() + expire))
                .compact();
    }

    /**
     * 创建token
     *
     * @param sub token所面向的用户
     * @param expire token有效时间
     * @param aud 接收token的一方
     * @return token 字符串
     */
    public static String createToken(String sub, long expire,String aud) {
        return createToken(sub,expire, aud, UUID.randomUUID().toString(), null);
    }

    /**
     * 创建token
     * @param expire token有效时间
     * @param sub token所面向的用户
     * @return token字符串
     */
    public static String createToken(String sub,long expire) {
        return createToken(sub, expire,null);
    }

    /**
     * 解析token
     * 可根据Jws<Claims>   获取  header|body|getSignature三部分数据
     *
     * @param token token字符串
     * @return Jws
     */
    public static Jws<Claims> parseToken(String token) {

        try {
            // 解析 token 字符串
            return Jwts.parser().setSigningKey(generateKey()).parseClaimsJws(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 校验token,校验是否是本服务器的token
     *
     * @param token token字符串
     * @return boolean
     */
    public static boolean checkToken(String token) {

        return !empty(parseToken(token));

    }

    private static boolean empty(Jws<Claims> jws) {

        return jws == null?true:jws.getBody()==null;
    }

    /**
     * 根据sub判断token
     *
     * @param token token字符串
     * @param sub   面向的用户
     * @return boolean
     */
    public static boolean checkToken(String token, String sub) {

        Jws<Claims> claimsJws = parseToken(token);
        try {
            return empty(claimsJws)?false:claimsJws.getBody().getSubject().equals(sub);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据sub判断token
     *
     * @param token token字符串
     * @param sub   面向的用户
     * @param iss   签发者
     * @return boolean
     */
    public static boolean checkToken(String token, String sub, String iss) {

        Jws<Claims> claimsJws = parseToken(token);
        try {
            return empty(claimsJws)?false:(claimsJws.getBody().getSubject().equals(sub)&&claimsJws.getBody().getIssuer().equals(iss));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
