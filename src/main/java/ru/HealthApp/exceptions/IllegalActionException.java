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
}
