package com.antonina.socialsynchro.common.utils;

import java.util.Random;

public class GenerateUtils {
    private final static String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static String LOWER = UPPER.toLowerCase();
    private final static String NUM = "1234567890";
    private final static String ALPHANUM = UPPER + LOWER + NUM;

    private GenerateUtils() { }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUM.length());
            sb.append(ALPHANUM.charAt(index));
        }
        return sb.toString();
    }
}
