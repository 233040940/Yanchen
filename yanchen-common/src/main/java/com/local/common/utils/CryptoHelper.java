package com.local.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author yc
 * @project yanchen
 * @description 加密数据工具类
 * @date 2020-06-14 12:55
 */
public class CryptoHelper {

    private static final String hashWithDigest(final String in, final String digest) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(digest);
            messageDigest.update(in.getBytes("UTF-8"), 0, in.length());
            byte[] hash = messageDigest.digest();
            return new BigInteger(1, hash).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Hashing the password failed", ex);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding the string failed", e);
        }

    }

    public static final String hexSha1(final String in) {
        return hashWithDigest(in, "SHA-1");
    }

    public static final String hexSha512(final String in) {
        return hashWithDigest(in, "SHA-512");
    }

    public static final String hexMD5(final String in) {
        return hashWithDigest(in, "MD5");
    }

    public static final boolean checkSha1Hash(final String hash, final String password, final String salt) {
        return hash.equals(makeSaltedSha1Hash(password, salt));
    }

    public static final boolean checkSaltedSha512Hash(final String hash, final String password, final String salt) {
        return hash.equals(makeSaltedSha512Hash(password, salt));
    }

    public static final boolean checkSaltedMD5Hash(final String hash, final String password, final String salt) {
        return hash.equals(makeSaltedMD5Hash(password, salt));
    }

    public static final String makeSaltedSha512Hash(final String password, final String salt) {
        return hexSha512(password + salt);
    }

    public static final String makeSaltedSha1Hash(final String password, final String salt) {
        return hexSha1(password + salt);
    }

    public static final String makeSaltedMD5Hash(final String password, final String salt) {
        return hexMD5(password + salt);
    }

    public static final String ofBase64(final String from) {
        return new String(Base64.getDecoder().decode(from));
    }

    public static final String toBase64(final String to) {
        return Base64.getEncoder().encodeToString(to.getBytes());
    }

}
