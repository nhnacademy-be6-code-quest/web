package com.nhnacademy.codequestweb.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class SecretKeyUtilsTest {

    private SecretKey secretKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        secretKey = SecretKeyUtils.getSecretKey();
    }

    @Test
    void testGetSecretKey() throws NoSuchAlgorithmException {
        SecretKey key1 = SecretKeyUtils.getSecretKey();
        SecretKey key2 = SecretKeyUtils.getSecretKey();
        assertNotNull(key1);
        assertNotNull(key2);
        assertSame(key1, key2);
    }

    @Test
    void testEncryptAndDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String originalText = "This is a secret message";
        String encryptedText = SecretKeyUtils.encrypt(originalText, secretKey);
        assertNotNull(encryptedText);
        assertNotEquals(originalText, encryptedText);

        String decryptedText = SecretKeyUtils.decrypt(encryptedText, secretKey);
        assertNotNull(decryptedText);
        assertEquals(originalText, decryptedText);
    }

    @Test
    void testEncryptWithInvalidKey() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKey anotherKey = SecretKeyUtils.getSecretKey();
        String originalText = "This is a secret message";
        String encryptedText = SecretKeyUtils.encrypt(originalText, anotherKey);
        assertNotNull(encryptedText);
        assertNotEquals(originalText, encryptedText);
    }

    @Test
    void testGenerateSecretKey() throws NoSuchAlgorithmException {
        SecretKey generatedKey = SecretKeyUtils.getSecretKey();
        assertNotNull(generatedKey);
        assertEquals("AES", generatedKey.getAlgorithm());
    }
}
