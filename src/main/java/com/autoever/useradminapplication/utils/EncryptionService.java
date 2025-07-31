package com.autoever.useradminapplication.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "MySuperSecretKey"; // 키 관리 시스템(KMS) 또는 환경 변수에 저장 권장
    private final PasswordEncoder passwordEncoder;

    /**
     *  비밀번호를 BCrypt로 암호화 (Default 강도 10)
     *
     *  @param password
     *  @return
     */
    public String encryptPassword(String password) {
        if (password != null && !password.isBlank()) {
            return passwordEncoder.encode(password);
        }
        return null; // 암호화 필요 없거나 입력값이 없는 경우
    }

    // AES로 데이터 암호화
    public String encryptByAES(String data) {
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
    public String decrypt(String encryptedData) {
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
