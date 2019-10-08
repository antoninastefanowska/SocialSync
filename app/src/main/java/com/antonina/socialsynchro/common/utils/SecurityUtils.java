package com.antonina.socialsynchro.common.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtils {

    private SecurityUtils() { }

    public String generateKey() {
        String output = null;
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            SecretKey key = generator.generateKey();

            output = Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String encrypt(String input) {
        String output = null;
        try {
            String stringKey = ApplicationConfig.getInstance().getKey("encryption_key");
            byte[] decodedKey = Base64.decode(stringKey.getBytes("UTF-8"), Base64.DEFAULT);

            SecureRandom random = new SecureRandom();
            byte[] ivData = new byte[16];
            random.nextBytes(ivData);

            SecretKey key = new SecretKeySpec(decodedKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivData);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encryptedData = cipher.doFinal(input.getBytes("UTF-8"));

            byte[] outputData = new byte[ivData.length + encryptedData.length];
            System.arraycopy(ivData, 0, outputData, 0, ivData.length);
            System.arraycopy(encryptedData, 0, outputData, ivData.length, encryptedData.length);

            output = Base64.encodeToString(outputData, Base64.DEFAULT);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String decrypt(String input) {
        String output = null;
        try {
            String keyString = ApplicationConfig.getInstance().getKey("encryption_key");

            byte[] decodedKey = Base64.decode(keyString.getBytes("UTF-8"), Base64.DEFAULT);
            byte[] decodedInput = Base64.decode(input.getBytes("UTF-8"), Base64.DEFAULT);

            byte[] ivData = new byte[16];
            byte[] encryptedData = new byte [decodedInput.length - ivData.length];

            System.arraycopy(decodedInput, 0, ivData, 0, ivData.length);
            System.arraycopy(decodedInput, ivData.length, encryptedData, 0, encryptedData.length);

            IvParameterSpec iv = new IvParameterSpec(ivData);
            SecretKey key = new SecretKeySpec(decodedKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            output = new String(cipher.doFinal(encryptedData), "UTF-8");
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return output;
    }
}
