package com.zhangzlyuyx.easy.core.util;

import java.io.File;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.Map.Entry;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * 加密工具类
 * @author zhangzlyuyx
 *
 */
public class CryptoUtils {

	/******************** begin md5 ********************/
	
	/**
	 * MD5加密，生成16进制MD5字符串
	 * <p>MD5是一种不可逆的加密算法，目前是最牢靠的加密算法之一，尚没有能够逆运算的程序被开发出来，它对应任何字符串都可以加密成一段唯一的固定长度的代码。</p>
	 * @param data
	 * @return
	 */
	public static String encodeMd5(String data){
		return SecureUtil.md5(data);
	}
	
	/**
	 * MD5加密，生成16进制MD5字符串
	 * <p>MD5是一种不可逆的加密算法，目前是最牢靠的加密算法之一，尚没有能够逆运算的程序被开发出来，它对应任何字符串都可以加密成一段唯一的固定长度的代码。</p>
	 * @param bytes
	 * @return
	 */
	public static String encodeMd5(byte[] bytes) {
		return new Digester(DigestAlgorithm.MD5).digestHex(bytes);
	}
	
	/**
	 * MD5加密，生成16进制MD5字符串
	 * <p>MD5是一种不可逆的加密算法，目前是最牢靠的加密算法之一，尚没有能够逆运算的程序被开发出来，它对应任何字符串都可以加密成一段唯一的固定长度的代码。</p>
	 * @param data
	 * @return
	 */
	public static String encodeMd5(InputStream data){
		return SecureUtil.md5(data);
	}
	
	/**
	 * MD5加密文件，生成16进制MD5字符串
	 * <p>MD5是一种不可逆的加密算法，目前是最牢靠的加密算法之一，尚没有能够逆运算的程序被开发出来，它对应任何字符串都可以加密成一段唯一的固定长度的代码。</p>
	 * @param dataFile
	 * @return
	 */
	public static String encodeMd5(File dataFile){
		return SecureUtil.md5(dataFile);
	}
	
	/******************** end md5 ********************/
	
	/******************** begin sha1 ********************/
	
	/**
	 * SHA1加密，生成16进制SHA1字符串
	 * <p>是由NISTNSA设计为同DSA一起使用的，它对长度小于264的输入，产生长度为160bit的散列值，因此抗穷举(brute-force)性更好。SHA-1设计时基于和MD4相同原理,并且模仿了该算法。SHA-1是由美国标准技术局（NIST）颁布的国家标准，是一种应用最为广泛的Hash函数算法，也是目前最先进的加密技术，被政府部门和私营业主用来处理敏感的信息。而SHA-1基于MD5，MD5又基于MD4。</p>
	 * @param data
	 * @return
	 */
	public static String encodeSha1(String data){
		return SecureUtil.sha1(data);
	}
	
	/**
	 * SHA1加密，生成16进制SHA1字符串
	 * <p>是由NISTNSA设计为同DSA一起使用的，它对长度小于264的输入，产生长度为160bit的散列值，因此抗穷举(brute-force)性更好。SHA-1设计时基于和MD4相同原理,并且模仿了该算法。SHA-1是由美国标准技术局（NIST）颁布的国家标准，是一种应用最为广泛的Hash函数算法，也是目前最先进的加密技术，被政府部门和私营业主用来处理敏感的信息。而SHA-1基于MD5，MD5又基于MD4。</p>
	 * @param data
	 * @return
	 */
	public static String encodeSha1(InputStream data){
		return SecureUtil.sha1(data);
	}
	
	/**
	 * SHA1加密文件，生成16进制SHA1字符串
	 * <p>是由NISTNSA设计为同DSA一起使用的，它对长度小于264的输入，产生长度为160bit的散列值，因此抗穷举(brute-force)性更好。SHA-1设计时基于和MD4相同原理,并且模仿了该算法。SHA-1是由美国标准技术局（NIST）颁布的国家标准，是一种应用最为广泛的Hash函数算法，也是目前最先进的加密技术，被政府部门和私营业主用来处理敏感的信息。而SHA-1基于MD5，MD5又基于MD4。</p>
	 * @param dataFile
	 * @return
	 */
	public static String encodeSha1(File dataFile){
		return SecureUtil.sha1(dataFile);
	}
	
	/******************** end sha1 ********************/
	
	/******************** begin sha256 ********************/
	
	/**
	 * SHA256加密，生成16进制SHA256字符串
	 * @param data
	 * @return
	 */
	public static String encodeSha256(String data){
		return SecureUtil.sha256(data);
	}
	
	/**
	 * SHA256加密，生成16进制SHA256字符串
	 * @param data
	 * @return
	 */
	public static String encodeSha256(InputStream data){
		return SecureUtil.sha256(data);
	}
	
	/**
	 * SHA256加密文件，生成16进制SHA256字符串
	 * @param dataFile
	 * @return
	 */
	public static String encodeSha256(File dataFile){
		return SecureUtil.sha256(dataFile);
	}
	
	/******************** end sha256 ********************/
	
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
	
	/******************** begin hex ********************/
	
	/**
	 * 将字节数组转换为十六进制字符数组
	 * @param data
	 * @param toLowerCase
	 * @return
	 */
	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
		return HexUtil.encodeHex(data, toLowerCase);
	}
	
	/**
	 * 将十六进制字符数组转换为字节数
	 * @param hexData
	 * @return
	 */
	public static byte[] decodeHex(char[] hexData) {
		return HexUtil.decodeHex(hexData);
	}
	
	/******************** end hex ********************/
	
	/******************** begin unicode ********************/
	
	/**
	 * 字符串编码为Unicode形式
	 * @param str 被编码的字符串
	 * @param isSkipAscii 是否跳过ASCII字符（只跳过可见字符）
	 * @return
	 */
	public static String encodeUnicode(String str, boolean isSkipAscii) {
		return UnicodeUtil.toUnicode(str, isSkipAscii);
	}
	
	/**
	 * Unicode字符串转为普通字符串
	 * @param unicode Unicode字符串,如: \\uXXXX
	 * @return
	 */
	public static String decodeUnicode(String unicode) {
		return UnicodeUtil.toString(unicode);
	}
	
	/******************** end unicode ********************/
	
	/******************** begin rsa ********************/
	
	/**
	 * 生成 rsa 密钥对 ,key私钥value公钥
	 * @return
	 */
	public static Entry<String, String> generateRsaKey() {
		RSA rsa = new RSA();
		return new AbstractMap.SimpleEntry<String, String>(rsa.getPrivateKeyBase64(), rsa.getPublicKeyBase64());
	}
	
	/**
	 * 用私钥对信息生成MD5withRSA数字签名
	 * @param privateKeyBase64 私钥Base64
	 * @param data 加密数据
	 * @return
	 */
	public static byte[] generateRsaSign(String privateKeyBase64, byte[] data) {
		return SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKeyBase64, null).sign(data);
	}
	
	/**
	 * 用私钥对信息生成MD5withRSA数字签名
	 * @param privateKeyBase64 私钥Base64
	 * @param data 加密数据
	 * @return
	 */
	public static String generateRsaSignBase64(String privateKeyBase64, byte[] data) {
		byte[] sign = generateRsaSign(privateKeyBase64, data);
		return encodeBase64(sign);
	}
	
	/**
	 * 用公钥检验MD5withRSA数字签名的合法性
	 * @param publicKeyBase64 公钥Base64
	 * @param data 数据
	 * @param sign 签名
	 * @return
	 */
	public static boolean verifyRsaSign(String publicKeyBase64, byte[] data, byte[] sign) {
		return SecureUtil.sign(SignAlgorithm.MD5withRSA, null, publicKeyBase64).verify(data, sign);
	}
	
	/**
	 * 用公钥检验MD5withRSA数字签名的合法性
	 * @param publicKeyBase64 公钥Base64
	 * @param data 数据
	 * @param signBase64 签名base64字符
	 * @return
	 */
	public static boolean verifyRsaSignBase64(String publicKeyBase64, byte[] data, String signBase64) {
		byte[] sign = decodeBase64(signBase64);
		return SecureUtil.sign(SignAlgorithm.MD5withRSA, null, publicKeyBase64).verify(data, sign);
	}
	
	/**
	 * 非对称加密RSA 公钥加密 -> 私钥解密
	 * <p>由 RSA 公司发明，是一个支持变长密钥的公共密钥算法，需要加密的文件块的长度也是可变的。</p>
	 * @param data
	 * @param publicKeyBase64
	 * @return
	 */
	public static byte[] encodeRsaPublicKey(byte[] data, String publicKeyBase64) {
		RSA rsa = new RSA(null, publicKeyBase64);
		return rsa.encrypt(data, KeyType.PublicKey);
	}
	
	/**
	 * 非对称加密RSA  私钥加密 -> 公钥解密
	 * <p>由 RSA 公司发明，是一个支持变长密钥的公共密钥算法，需要加密的文件块的长度也是可变的。</p>
	 * @param data
	 * @param privateKeyBase64
	 * @return
	 */
	public static byte[] encodeRsaPrivateKey(byte[] data, String privateKeyBase64) {
		RSA rsa = new RSA(privateKeyBase64, null);
		return rsa.encrypt(data, KeyType.PrivateKey);
	}
	
	/**
	 * 非对称加密RSA 私钥解密  <- 公钥加密
	 * <p>由 RSA 公司发明，是一个支持变长密钥的公共密钥算法，需要加密的文件块的长度也是可变的。</p>
	 * @param data
	 * @param privateKeyBase64
	 * @return
	 */
	public static byte[] decodeRsaPrivateKey(byte[] data, String privateKeyBase64) {
		RSA rsa = new RSA(privateKeyBase64, null);
		return rsa.decrypt(data, KeyType.PrivateKey);
	}
	
	/**
	 * 非对称加密RSA 公钥解密  <- 私钥加密
	 * <p>由 RSA 公司发明，是一个支持变长密钥的公共密钥算法，需要加密的文件块的长度也是可变的。</p>
	 * @param data
	 * @param publicKeyBase64
	 * @return
	 */
	public static byte[] decodeRsaPublicKey(byte[] data, String publicKeyBase64) {
		RSA rsa = new RSA(null, publicKeyBase64);
		return rsa.decrypt(data, KeyType.PublicKey);
	}
	
	/******************** end rsa ********************/
	
	/******************** begin des ********************/
	
	/**
	 * 生成 DES 随机密钥
	 * @return
	 */
	public static byte[] generateDesKey() {
		return SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
	}
	
	/**
	 * 对称加密DES
	 * <p>(数据加密标准，速度较快，适用于加密大量数据的场合。)</p>
	 * @param key 密钥
	 * @param data 被加密的bytes
	 * @return
	 */
	public static byte[] encodeDes(byte[] key, byte[] data) {
		DES des = new DES(key);
		return des.encrypt(data);
	}
	
	/**
	 * 对称加密DES
	 * <p>(数据加密标准，速度较快，适用于加密大量数据的场合。)</p>
	 * @param key 密钥
	 * @param data 被加密的bytes
	 * @return
	 */
	public static byte[] encodeDes(byte[] key, InputStream data) {
		DES des = new DES(key);
		return des.encrypt(data);
	}
	
	/**
	 * 对称加密DES
	 * <p>(数据加密标准，速度较快，适用于加密大量数据的场合。)</p>
	 * @param key 密钥
	 * @param bytes 被解密的bytes
	 * @return
	 */
	public static byte[] decodeDes(byte[] key, byte[] bytes) {
		DES des = new DES(key);
		return des.decrypt(bytes);
	}
	
	/**
	 * 对称加密DES
	 * <p>(数据加密标准，速度较快，适用于加密大量数据的场合。)</p>
	 * @param key 密钥
	 * @param bytes 被解密的bytes
	 * @return
	 */
	public static byte[] decodeDes(byte[] key, InputStream bytes) {
		DES des = new DES(key);
		return des.decrypt(bytes);
	}
	
	/******************** end des ********************/

	public static void main(String[] args) {
		
		Entry<String, String> kv = generateRsaKey();
		
		String ss = generateRsaSignBase64(kv.getKey(), "123456".getBytes());
		
		boolean aa = verifyRsaSignBase64(kv.getValue(), "123456".getBytes(), ss);
		
		System.out.println(aa);
	}
}
