package com.bonc.epm.paas.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.lang.StringUtils;

/**
 *  
 * 提供信息加密方法
 * @author fengtao
 * @version 2016年8月31日
 * @see EncryptUtils
 * @since
 */
public class EncryptUtils {


    /**
     * 
     * Description: <br>
     * 对字符串进行MD5进行加密处理
     * @param msg 待加密的字符串
     * @return 加密后字符串
     * @see
     */
    public static String encryptMD5(String msg){
        return encrypt(msg, null);
    }
	
    /**
     * 
     * Description:
     * 基本加密处理
     * @param msg 待加密的字符串
     * @param type 加密类型
     * @return password String
     * @see
     */
    private static String encrypt(String msg, String type){
        StringBuilder password = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if(StringUtils.isNotBlank(type)) {
                md.update(type.getBytes());
            }
            else {
                md.update(msg.getBytes());
            }
			
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                String param = Integer.toString((bytes[i] & 0xff) + 0x100, 16);
                password.append(param.substring(1));
            }
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password.toString();
    }
	
    /**
     * 
     * Description:
     * 盐值的原理非常简单，就是先把密码和盐值指定的内容合并在一起，再使用md5对合并后的内容进行演算，
     * 这样一来，就算密码是一个很常见的字符串，再加上用户名，最后算出来的md5值就没那么容易猜出来了。
     * 因为攻击者不知道盐值的值，也很难反算出密码原文。
     * @param msg 待加密信息
     * @return String 
     * @see
     */
    public static String encryptSalt(String msg) {
        String salt = getSalt();
        return encrypt(msg, salt);
    }
	
    /**
     * 
     * Description:
     * SHA（Secure Hash Algorithm，安全散列算法）是消息摘要算法的一种，被广泛认可的MD5算法的继任者。
     * SHA算法家族目前共有SHA-0、SHA-1、SHA-224、SHA-256、SHA-384和SHA-512五种算法，
     * 通常将后四种算法并称为SHA-2算法
     * @param msg 待加密信息
     * @return sb 加密后信息
     * @see
     */
    public static String encryptSHA(String msg) {
        StringBuilder sb = new StringBuilder();
        try{
            String salt = getSaltSHA1();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(msg.getBytes());
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
	
    /**
     * 
     * Description:
     * PBKDF2加密
     * @param msg 待加密信息
     * @return 加密后信息
     * @see
     */
    public static String encryptPBKDF2(String msg) {
        try {
            int iterations = 1000;
            int keyLength = 64 * 8;
            byte[] salt = getSalt().getBytes();
            PBEKeySpec spec = new PBEKeySpec(msg.toCharArray(), salt, iterations, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + toHex(salt) + toHex(hash);
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
    }
    
    /**
     * 
     * Description:
     * 转化十六进制
     * @param array byte[]
     * @return hex String
     * @see
     */
    private static String toHex(byte[] array) {
        if (null != array) {
            BigInteger bi = new BigInteger(1, array);
            String hex = bi.toString(16);
            int paddingLength = (array.length * 2) - hex.length();
            if(paddingLength > 0) {
                return String.format("%0"  +paddingLength + "d", 0) + hex;
            }
            else {
                return hex;
            }
        }
        return null;
    }
	

    /**
     * 
     * Description: 
     *  <li>SHA-1 (Simplest one – 160 bits Hash)</li>
     *  <li>SHA-256 (Stronger than SHA-1 – 256 bits Hash)</li>
     *  <li>HA-384 (Stronger than SHA-256 – 384 bits Hash)</li>
     *  <li>SHA-512 (Stronger than SHA-384 – 512 bits Hash)</li>
     * @return salt toString
     * @see
     */
    private static String getSaltSHA1(){
        byte[] salt = new byte[16];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return salt.toString();
    }
    
    /**
     * 
     * Description:
     * 盐值的原理非常简单，就是先把密码和盐值指定的内容合并在一起，再使用md5对合并后的内容进行演算，
     * 这样一来，就算密码是一个很常见的字符串，再加上用户名，最后算出来的md5值就没那么容易猜出来了。
     * 因为攻击者不知道盐值的值，也很难反算出密码原文。
     * @return salt toString
     * @see
     */
    private static String getSalt(){
        byte[] salt = new byte[16];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            sr.nextBytes(salt);
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
        return salt.toString();
    }

	/*public static void main(String[] args) {
		System.out.println(EncryptUtils.encryptMD5("123456"));
		System.out.println(EncryptUtils.encryptSalt("123456"));
		System.out.println(EncryptUtils.encryptSHA("123456"));
		System.out.println(EncryptUtils.encryptPBKDF2("123456"));
	}*/
}
