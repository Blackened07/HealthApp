package ru.HealthApp.exceptions;

public final class IllegalActionException extends HealthAppException {
    public IllegalActionException(String message) {
        super(message, false);
    }

    public static IllegalActionException getInvitationAlreadyExistException() {
        return new IllegalActionException(
                ExceptionMessage.INVITATION_ALREADY_EXIST.getMessage());
    }

    public static IllegalActionException getUserAlreadyInFamilyException() {
        return new IllegalActionException(
                ExceptionMessage.USER_ALREADY_IN_FAMILY.getMessage());
    }

    public static IllegalActionException getSelfInvitationErrorException() {
        return new IllegalActionException(
                ExceptionMessage.SELF_INVITATION_ERROR.getMessage());
    }

    public static IllegalActionException getInvitationErrorException() {
        return new IllegalActionException(
                ExceptionMessage.INVITATION_ERROR.getMessage());
    }

    public static IllegalActionException getInvitingErrorException() {
        return new IllegalActionException(
                ExceptionMessage.INVITING_ERROR.getMessage());
    }

    public static IllegalActionException getUserIsNoFamilyException() {
        return new IllegalActionException(
                ExceptionMessage.USER_IS_NO_FAMILY.getMessage());
    }

    public static IllegalActionException getUserNotInYourFamilyException() {
        return new IllegalActionException(
                ExceptionMessage.USER_NOT_IN_YOUR_FAMILY.getMessage());
    }

    public static IllegalActionException getCannotRemoveAdminException() {
        return new IllegalActionException(
                ExceptionMessage.CANNOT_REMOVE_ADMIN.getMessage());
    }

    public static IllegalActionException getRecordTypeMismatchException() {
        return new IllegalActionException("Разные типы! Невозможно отредактировать запись!");
    }

    public static IllegalActionException getDoctorAlreadyCuresFamilyException() {
        return new IllegalActionException("Доктор уже курирует вашу семью");
    }
}
