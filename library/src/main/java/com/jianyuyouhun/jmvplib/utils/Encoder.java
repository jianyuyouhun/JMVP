package com.jianyuyouhun.jmvplib.utils;

import java.security.MessageDigest;

/**
 * MD5加密
 * Created by wangyu on 2017/5/10.
 */

public class Encoder {
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_SHA1 = "SHA1";

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * encode string
     *
     * @return String
     */
    public static String encode(String algorithm, String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * encode By SHA1
     *
     * @param str
     * @return String
     */
    public static String encodeBySHA1(String str) {
        return encode(ALGORITHM_SHA1, str);
    }

    /**
     * encode By MD5
     */
    public static String encodeByMD5(String str) {
        return encode(ALGORITHM_MD5, str);
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     */
    private static String getFormattedText(byte[] bytes) {
        final StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (int j = 0; j < bytes.length; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
}
