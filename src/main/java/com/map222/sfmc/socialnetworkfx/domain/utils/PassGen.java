package com.map222.sfmc.socialnetworkfx.domain.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PassGen {
    /**
     * Converts from binary byte array to hex string
     * @param hash -the byte array
     * @return the string result
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Provides hashing for a string password attempt
     * @param attempt -the password attempt to hash
     * @return a string representing the hashed result using SHA-256
     */
    public static String convertPassword(String attempt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    attempt.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        }
        catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        }
    }
}
