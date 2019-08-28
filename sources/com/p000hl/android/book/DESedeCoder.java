package com.p000hl.android.book;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/* renamed from: com.hl.android.book.DESedeCoder */
public class DESedeCoder {
    public static byte[] generateKey() {
        try {
            SecureRandom sr = new SecureRandom();
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            kg.init(sr);
            return kg.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("DES算法，生成密钥出错!");
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            SecureRandom sr = new SecureRandom();
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(1, secretKey, sr);
            return cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("DES算法，加密数据出错!");
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            SecureRandom sr = new SecureRandom();
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(2, secretKey, sr);
            return cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("DES算法，解密出错。");
            return null;
        }
    }

    public static byte[] CBCEncrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(1, secretKey, new IvParameterSpec(iv));
            return cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("DES算法，加密数据出错!");
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] CBCDecrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(2, secretKey, new IvParameterSpec(iv));
            return cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("DES算法，解密出错。");
            e.printStackTrace();
            return null;
        }
    }
}
