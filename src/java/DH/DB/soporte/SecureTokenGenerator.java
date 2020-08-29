/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.soporte;

import java.security.SecureRandom;

/**
 *
 * @author mvall
 */
//public class RandomString {
//
//    /**
//     * Generate a random string.
//     */
//    public String nextString() {
//        for (int idx = 0; idx < buf.length; ++idx)
//            buf[idx] = symbols[random.nextInt(symbols.length)];
//        return new String(buf);
//    }
//
//    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//
//    public static final String lower = upper.toLowerCase(Locale.ROOT);
//
//    public static final String digits = "0123456789";
//
//    public static final String alphanum = upper + lower + digits;
//
//    private final Random random;
//
//    private final char[] symbols;
//
//    private final char[] buf;
//
//    public RandomString(int length, Random random, String symbols) {
//        if (length < 1) throw new IllegalArgumentException();
//        if (symbols.length() < 2) throw new IllegalArgumentException();
//        this.random = Objects.requireNonNull(random);
//        this.symbols = symbols.toCharArray();
//        this.buf = new char[length];
//    }
//
//    /**
//     * Create an alphanumeric string generator.
//     */
//    public RandomString(int length, Random random) {
//        this(length, random, alphanum);
//    }
//
//    /**
//     * Create an alphanumeric strings from a secure generator.
//     */
//    public RandomString(int length) {
//        this(length, new SecureRandom());
//    }
//
//    /**
//     * Create session identifiers.
//     */
//    public RandomString() {
//        this(21);
//    }
//    
//}

public class SecureTokenGenerator {
public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

// 2048 bit keys should be secure until 2030 - https://web.archive.org/web/20170417095741/https://www.emc.com/emc-plus/rsa-labs/historical/twirl-and-rsa-key-size.htm
public static final int SECURE_TOKEN_LENGTH = 256;

private static final SecureRandom random = new SecureRandom();

private static final char[] symbols = CHARACTERS.toCharArray();

private static final char[] buf = new char[SECURE_TOKEN_LENGTH];

/**
    * Generate the next secure random token in the series.
    */
public static String nextToken() {
       for (int idx = 0; idx < buf.length; ++idx)
           buf[idx] = symbols[random.nextInt(symbols.length)];
       return new String(buf);
}
}