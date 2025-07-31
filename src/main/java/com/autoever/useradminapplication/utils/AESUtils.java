package com.autoever.useradminapplication.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Deprecated
public class AESUtils {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "MySuperSecretKey"; // 키 관리 시스템(KMS) 또는 환경 변수에 저장 권장

    // 데이터 암호화
    public static String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    // 데이터 복호화
    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedBytes);
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }

}
