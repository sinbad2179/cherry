package cn.cherry.authenticate.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * 加解密工具类
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 10:53
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CryptoUtil {

    /**
     * 创建加密器
     *
     * @param cipherType 加密类型
     * @param key        加密秘钥
     * @return 加密器
     */
    public static Cipher createEncryptCipher(String cipherType, Key key) {
        return createEncryptCipher(cipherType, key, null);
    }

    /**
     * 创建解密器
     *
     * @param cipherType 加密类型
     * @param key        加密秘钥
     * @return 解密器
     */
    public static Cipher createDecryptCipher(String cipherType, Key key) {
        return createDecryptCipher(cipherType, key, null);
    }

    /**
     * 创建加密器
     *
     * @param cipherType         加密类型
     * @param key                加密秘钥
     * @param algorithmParameter 算法参数
     * @return 加密器
     */
    public static Cipher createEncryptCipher(String cipherType, Key key, AlgorithmParameterSpec algorithmParameter) {
        try {
            Cipher encryptCipher = Cipher.getInstance(cipherType);
            if (algorithmParameter == null) {
                encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                encryptCipher.init(Cipher.ENCRYPT_MODE, key, algorithmParameter);
            }
            return encryptCipher;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 创建解密器
     *
     * @param cipherType         加密类型
     * @param key                加密秘钥
     * @param algorithmParameter 算法参数
     * @return 解密器
     */
    public static Cipher createDecryptCipher(String cipherType, Key key, AlgorithmParameterSpec algorithmParameter) {
        try {
            Cipher decryptCipher = Cipher.getInstance(cipherType);
            if (algorithmParameter == null) {
                decryptCipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                decryptCipher.init(Cipher.DECRYPT_MODE, key, algorithmParameter);
            }
            return decryptCipher;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 加密
     */
    public static byte[] encrypt(Cipher encryptCipher, String secretMessage)
            throws BadPaddingException, IllegalBlockSizeException {
        if (StringUtils.isBlank(secretMessage)) {
            return new byte[0];
        }
        byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        return Base64.getEncoder().encode(encryptedMessageBytes);
    }

    /**
     * 解密
     */
    public static byte[] decrypt(Cipher decryptCipher, String encryptMessage)
            throws BadPaddingException, IllegalBlockSizeException {
        if (StringUtils.isBlank(encryptMessage)) {
            return new byte[0];
        }
        byte[] encryptedMessageBytes = Base64.getDecoder()
                .decode(encryptMessage.getBytes(StandardCharsets.UTF_8));
        return decryptCipher.doFinal(encryptedMessageBytes);
    }

    /**
     * 加密
     */
    public static String encryptToString(Cipher encryptCipher, String secretMessage)
            throws BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedMessageBytes = encrypt(encryptCipher, secretMessage);
        return new String(encryptedMessageBytes, StandardCharsets.UTF_8);
    }

    /**
     * 解密
     */
    public static String decryptToString(Cipher decryptCipher, String encryptMessage)
            throws BadPaddingException, IllegalBlockSizeException {
        byte[] decryptMessageBytes = decrypt(decryptCipher, encryptMessage);
        return new String(decryptMessageBytes, StandardCharsets.UTF_8);
    }
}
