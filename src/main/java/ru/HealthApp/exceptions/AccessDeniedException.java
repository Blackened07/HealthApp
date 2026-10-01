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

    public static AccessDeniedException getNotAdminException() {
        return new AccessDeniedException(
                ExceptionMessage.NOT_ADMIN_EXCEPTION.getMessage()
        );
    }

    public static AccessDeniedException getReadException() {
        return new AccessDeniedException(
                ExceptionMessage.READ_EXCEPTION.getMessage()
        );
    }

    public static AccessDeniedException getWriteException() {
        return new AccessDeniedException(
                ExceptionMessage.WRITE_EXCEPTION.getMessage()
        );
    }

    public static AccessDeniedException getNoFamilyException() {
        return new AccessDeniedException(
                ExceptionMessage.NO_FAMILY_EXCEPTION.getMessage()
        );
    }

    public static AccessDeniedException getExpiredCodeException() {
        return new AccessDeniedException(
                ExceptionMessage.EXPIRED_CODE.getMessage()
        );
    }

    public static AccessDeniedException getWrongVerifyCodeException() {
        return new AccessDeniedException(
                ExceptionMessage.WRONG_VERIFY_CODE.getMessage()
        );
    }

    public static AccessDeniedException getNotAdminEmailException() {
        return new AccessDeniedException(
                ExceptionMessage.NOT_ADMIN_EMAIL.getMessage()
        );
    }

    public static AccessDeniedException getDoctorRegistrationDisabledException() {
        return new AccessDeniedException("Пока нельзя зарегистрироваться как доктор");
    }

}
