package ru.HealthApp.utils;

import java.util.Random;

public final class CodeGenerator {

    private CodeGenerator() {
    }

    public static String generateVerificationEmailCode() {

        int code = 100000 + new Random().nextInt(900000);
        return String.valueOf(code);
    }
}
