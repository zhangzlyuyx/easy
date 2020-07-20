package com.zhangzlyuyx.easy.core.util;

import java.io.File;
import java.io.InputStream;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * 加密工具类
 * @author zhangzlyuyx
 *
 */
public class CryptoUtils {

	/******************** begin md5 ********************/
	
	/**
	 * MD5加密，生成16进制MD5字符串
	 * @param data
	 * @return
	 */
	public static String encodeMd5(String data){
		return SecureUtil.md5(data);
	}
	
	/**
	 * MD5加密，生成16进制MD5字符串
	 * @param data
	 * @return
	 */
	public static String encodeMd5(InputStream data){
		return SecureUtil.md5(data);
	}
	
	/**
	 * MD5加密文件，生成16进制MD5字符串
	 * @param file
	 * @return
	 */
	public static String encodeMd5(File file){
		return SecureUtil.md5(file);
	}
	
	/******************** end md5 ********************/
	
	/******************** begin sha1 ********************/
	
	/**
	 * SHA1加密，生成16进制SHA1字符串
	 * @param data
	 * @return
	 */
	public static String encodeSha1(String data){
		return SecureUtil.sha1(data);
	}
	
	/**
	 * SHA1加密，生成16进制SHA1字符串
	 * @param data
	 * @return
	 */
	public static String encodeSha1(InputStream data){
		return SecureUtil.sha1(data);
	}
	
	/******************** end sha1 ********************/
	
	/******************** begin base64 ********************/
	
	/**
	 * base64编码
	 * @param source
	 * @return
	 */
	public static String encodeBase64(String source) {
		return Base64.encode(source);
	}
	
	/**
	 * base64编码
	 * @param source
	 * @param charset 字符集
	 * @return
	 */
	public static String encodeBase64(String source, String charset) {
		return Base64.encode(source, charset);
	}
	
	/**
	 * base64编码
	 * @param source
	 * @return
	 */
	public static String encodeBase64(byte[] source) {
		return Base64.encode(source);
	}
	
	/**
	 * base64解码
	 * @param base64 base64字符
	 * @return
	 */
	public static byte[] decodeBase64(String base64) {
		return Base64.decode(base64);
	}
	
	/**
	 * base64解码
	 * @param base64
	 * @param charset 字符集
	 * @return
	 */
	public static byte[] decodeBase64(String base64, String charset) {
		return Base64.decode(base64, charset);
	}
	
	/******************** end base64 ********************/
	
	/******************** begin rsa ********************/
	
	/**
	 * RSA 公钥加密 -> 私钥解密
	 * @param data
	 * @param publicKeyBase64
	 * @return
	 */
	public static byte[] encodeRsaPublicKey(byte[] data, String publicKeyBase64) {
		RSA rsa = new RSA(null, publicKeyBase64);
		return rsa.encrypt(data, KeyType.PublicKey);
	}
	
	/**
	 * RSA  私钥加密 -> 公钥解密
	 * @param data
	 * @param privateKeyBase64
	 * @return
	 */
	public static byte[] encodeRsaPrivateKey(byte[] data, String privateKeyBase64) {
		RSA rsa = new RSA(privateKeyBase64, null);
		return rsa.encrypt(data, KeyType.PrivateKey);
	}
	
	/**
	 * RSA 私钥解密  <- 公钥加密
	 * @param data
	 * @param privateKeyBase64
	 * @return
	 */
	public static byte[] decodeRsaPrivateKey(byte[] data, String privateKeyBase64) {
		RSA rsa = new RSA(privateKeyBase64, null);
		return rsa.decrypt(data, KeyType.PrivateKey);
	}
	
	/**
	 * RSA 公钥解密  <- 私钥加密
	 * @param data
	 * @param publicKeyBase64
	 * @return
	 */
	public static byte[] decodeRsaPublicKey(byte[] data, String publicKeyBase64) {
		RSA rsa = new RSA(null, publicKeyBase64);
		return rsa.decrypt(data, KeyType.PublicKey);
	}
	
	/******************** end rsa ********************/
}
