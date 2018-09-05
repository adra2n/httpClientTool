package com.taolc.http.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtil {

	
	/**
	 * 创建JWT Token
	 * @param key
	 * @param secret
	 * @return
	 */
	public static String createToken(String key,String secret) {
		//Example using HS256
		Algorithm algorithm = Algorithm.HMAC256(secret);
	    
		String token = JWT.create().withIssuer(key).sign(algorithm);
		
	    return token;
		
	}
	
	/**
	 * verified and decoded JWT.
	 * 若有异常会throws
	 * @param token
	 * @param key
	 * @param secret
	 */
	public static DecodedJWT verifyToken(String token,String key,String secret) {
		
		Algorithm algorithm = Algorithm.HMAC256(secret);
		
	    JWTVerifier verifier = JWT.require(algorithm).withIssuer(key).build(); 
	    
		return verifier.verify(token);
	}
	
	/**
	 * 解密
	 * @param token
	 * @return
	 */
	public static DecodedJWT decodeToken(String token) {
		return JWT.decode(token);
	}
	
	
}
