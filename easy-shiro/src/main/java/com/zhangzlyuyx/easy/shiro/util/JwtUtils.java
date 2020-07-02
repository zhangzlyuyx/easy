package com.zhangzlyuyx.easy.shiro.util;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.util.CryptoUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * jwt 工具类
 * @author zhangzlyuyx
 *
 */
public class JwtUtils {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
	
	/**
	 * 默认 base64 密钥
	 */
	public static String JWT_BASE64_KEY = "c3VucmlzZQ==";
	
	/**
	 * 创建 jwt
	 * @param jwtId id
	 * @param subject 主题
	 * @param issuer 签发人
	 * @param issuedAt 签发时间
	 * @param ttlMillis 过期有效时间(根据签发时间累加)
	 * @param base64Key base64 密钥
	 * @return
	 */
	public static String createJWT(String jwtId, String subject, String issuer, long issuedAt, long ttlMillis, String base64Key) {
		SecretKey secretKey = generalSecretKey((base64Key != null && base64Key.length() > 0) ? base64Key : JWT_BASE64_KEY);
		JwtBuilder builder = Jwts.builder();
		builder.setId(jwtId);//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
		builder.setSubject(subject);//sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
		builder.setIssuer(issuer);//iss:签发人
		builder.setIssuedAt(new Date(issuedAt));//iat: jwt的签发时间
		builder.signWith(SignatureAlgorithm.HS512, secretKey);//设置签名使用的签名算法和签名使用的秘钥
		if(ttlMillis >= 0) {
			builder.setExpiration(new Date(issuedAt + ttlMillis));//设置过期时间
		}
		return builder.compact();
	}
	
	/**
	 * 解析 jwt
	 * @param jwt
	 * @param secretKey 密钥
	 * @return
	 */
	public static Claims parseJWT(String jwt, String base64Key) {		
		if(jwt == null || jwt.length() == 0) {
			return null;
		}
		SecretKey secretKey = generalSecretKey(base64Key);
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
		return claims;
	}
	
	/**
	 * 验证 jwt
	 * @param jwt jwt
	 * @param base64Key base64密钥
	 * @return
	 */
	public static Result<Claims> validateJWT(String jwt, String base64Key){
		try {
			if(base64Key == null) {
				base64Key = JWT_BASE64_KEY;
			}
			Claims claims = parseJWT(jwt, base64Key);
			return new Result<>(true, "解析成功", claims);
		} catch (ExpiredJwtException e) {
			LOGGER.error("jwt", e);
			return new Result<>(false, "认证信息已过期");
		} catch (SignatureException e) {
			LOGGER.error("jwt", e);
			return new Result<>(false, "认证信息签名校验失败");
		} catch (MalformedJwtException e) {
			LOGGER.error("jwt", e);
			return new Result<>(false, "认证信息格式错误");
		} catch (Exception e) {
			LOGGER.error("jwt", e);
			return new Result<>(false, e.getMessage());
		}
	}
	
	/**
	 * 生成加密 key
	 * @param base64Key base64密文
	 * @return
	 */
	public static SecretKey generalSecretKey(String base64Key){
		byte[] key = null;
		try {
			key = CryptoUtils.decodeBase64(base64Key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");
		return secretKey;
	}
}
