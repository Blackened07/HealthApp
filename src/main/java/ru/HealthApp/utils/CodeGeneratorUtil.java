package ru.HealthApp.utils;

import java.util.Random;

public final class CodeGeneratorUtil {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    private CodeGeneratorUtil() {
    }

    public static String generateFamilyInvitationCode() {
        String prefix = "FAM_";
        StringBuilder code = new StringBuilder(prefix);

        for (int i = 0; i < 9; i++) {
            code.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));

            if (i % 3 == 0) {
                code.append("_");
            }
        }

        return code.toString().toUpperCase();
    }
}
