package com.nhnacademy.codequestweb.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.*;

public class SecretKeyUtils {
    private static SecretKey secretKey;

    private SecretKeyUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static SecretKey getSecretKey() throws NoSuchAlgorithmException {
        if (secretKey == null) {
            secretKey = generateSecretKey();
        }
        return secretKey;
    }

    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // 256비트 길이의 AES 키 생성
        return keyGenerator.generateKey();
    }

    @SuppressWarnings("java:S5542") // Is Not Important Information
    public static String encrypt(String data, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @SuppressWarnings("java:S5542") // Is Not Important Information
    public static String decrypt(String encryptedData, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
