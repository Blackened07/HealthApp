package ru.HealthApp.exceptions;

public final class AccessDeniedException extends HealthAppException {

    public AccessDeniedException(String message) {
        super(message, true);
    }

    public static AccessDeniedException getWrongEmailOrPassException() {
        return new AccessDeniedException(
                ExceptionMessage.WRONG_EMAIL_OR_PASS.getMessage()
        );
    }

    public static AccessDeniedException getNotVerifyEmailException() {
        return new AccessDeniedException(
                ExceptionMessage.EMAIL_NOT_VERIFY.getMessage()
        );
    }

    public static AccessDeniedException getAlreadyVerifyEmailException() {
        return new AccessDeniedException(
                ExceptionMessage.EMAIL_ALREADY_VERIFY.getMessage()
        );
    }

}
