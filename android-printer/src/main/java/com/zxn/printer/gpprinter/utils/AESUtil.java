package com.zxn.printer.gpprinter.utils;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zxn on 2019/7/12.
 */
public class AESUtil {

    private final static byte[] CustomKeyBytes = new byte[]{ 36, 49, 66, 64, 33, 48, 35, 56, 88, 43, 53, 61, 62, 46, 107, 121 };

    /**
     * 密钥算法
     * */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加密/解密算法/工作模式/填充方式
     * */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 生成密钥
     * @return byte[] 二进制密钥
     * */
    @SuppressWarnings("unused")
    private static byte[] initkey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);// 实例化密钥生成器
            kg.init(128); //初始化密钥生成器
            SecretKey secretKey = kg.generateKey(); //生成密钥
            return secretKey.getEncoded(); //获取二进制密钥编码形式
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换密钥
     * @param key 二进制密钥
     * @return Key 密钥
     * */
    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);// 生成密钥
    }

    /**
     * 加密数据
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密后的数据
     * */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);//还原密钥
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);//实例化
        cipher.init(Cipher.ENCRYPT_MODE, k);//初始化，设置为加密模式
        return cipher.doFinal(data);//执行操作
    }

    /**
     * 解密数据
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密后的数据
     * */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key); //欢迎密钥
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM); //实例化
        cipher.init(Cipher.DECRYPT_MODE, k); //初始化，设置为解密模式
        return cipher.doFinal(data);//执行操作
    }

    /**
     * 加密数据
     * @param str
     * @return
     */
    public static String dataEncrypt(String str) {
        String encrypt = null;
        try {
            byte[] ret = encrypt(str.getBytes("UTF-8"), CustomKeyBytes);
            encrypt = new String(Base64.encode(ret));
            return URLEncoder.encode(encrypt, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            encrypt = str;
        }
        return encrypt;
    }

    /**
     * 解密数据
     * @param str
     * @return
     */
    public static String dataDecrypt(String str) {
        String decrypt = null;
        try {
            byte[] ret = decrypt(Base64.decode(URLDecoder.decode(str, "UTF-8")), CustomKeyBytes);//Main方法测试或本地处理时使用
            //			byte[] ret = decrypt(Base64.decode(str), CustomKeyBytes);//网络传输时使用
            decrypt = new String(ret, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            /*decrypt = str;*/
        }
        return decrypt;
    }


}
