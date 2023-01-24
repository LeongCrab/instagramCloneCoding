package com.example.demo.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


@Component
public class AES128 {
    @Value("${AES128.secret-key}")
    private String key;

    public String encrypt(String target){
        if(target == null){
            return null;
        }
        SecretKeySpec keySpec = null;
        try {
            keySpec = new SecretKeySpec(key.getBytes(), "AES");
        } catch(NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }

        try {
            Encoder encoder = Base64.getEncoder();
            return new String(encoder.encode(cipher.doFinal(target.getBytes())));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String target){
        SecretKeySpec keySpec = null;
        keySpec= new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }

        try {
            Decoder encoder = Base64.getDecoder();
            return new String(cipher.doFinal(encoder.decode(target.getBytes())));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
