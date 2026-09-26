package ru.HealthApp.service.validators;

import org.springframework.stereotype.Component;
import ru.HealthApp.entities.Account;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.exceptions.IllegalActionException;

@Component
public class FamilyActionGuard {

    public void checkIsAdminEmailInInvitationRequest(User admin){
        if (!admin.isAdmin()) {
            throw new AccessDeniedException(ExceptionMessage.NOT_ADMIN_EMAIL.getMessage());
        }
    }

    public void checkInvitationToNewFamily(User actor, User invitedUser) {
        if (actor.equals(invitedUser)) {
            throw new IllegalActionException(ExceptionMessage.SELF_INVITATION_ERROR.getMessage());
        }
        if (isActorAllRight(actor)) {
            throw new IllegalActionException(ExceptionMessage.INVITATION_ERROR.getMessage());
        } else if (isInvitedAllRight(invitedUser)) {
            throw new IllegalActionException(ExceptionMessage.INVITING_ERROR.getMessage());
        }
    }

    public boolean checkInvitationToExistFamily(User actor, User invitedUser) {
        if (!isActorAllRight(actor)) {
            throw new IllegalActionException(ExceptionMessage.INVITATION_ERROR.getMessage());
        } else if (isInvitedAllRight(invitedUser)) {
            throw new IllegalActionException(ExceptionMessage.INVITING_ERROR.getMessage());
        }
        return true;
    }

    public void checkDeletion(User deletionUser, Long familyId ) {
        if (deletionUser.isNoFamily()) {
            throw new IllegalActionException(ExceptionMessage.USER_IS_NO_FAMILY.getMessage());
        }

        if (deletionUser.getFamily().getId() != familyId) {
            throw new IllegalActionException(ExceptionMessage.USER_NOT_IN_YOUR_FAMILY.getMessage());
        }

        if (deletionUser.isAdmin()) {
            throw new IllegalArgumentException(ExceptionMessage.CANNOT_REMOVE_ADMIN.getMessage());
        }
    }

    private boolean isActorAllRight(User actor) {
        if (actor.isNoFamily()) {
            return false;
        }

        if (actor.getRole() != Account.SystemRole.USER) {
            return false;
        }

        return actor.isAdmin();
    }

    private boolean isInvitedAllRight(User target) {
        if (target.isVirtual()) {
            return true;
        }
        if (target.isAdmin()) {
            return true;
        }
        if (target.getRole() == Account.SystemRole.DOCTOR) {
            return true;
        }

        return !target.isNoFamily();
    }
}
