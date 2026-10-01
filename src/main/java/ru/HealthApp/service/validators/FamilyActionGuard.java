package ru.HealthApp.service.validators;

import org.springframework.stereotype.Component;
import ru.HealthApp.entities.Account;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.exceptions.IllegalActionException;

@Component
public class FamilyActionGuard implements IFamilyActionGuard {

    public void checkIsAdminEmailInInvitationRequest(User admin){
        if (!admin.isAdmin()) {
            throw AccessDeniedException.getNotAdminEmailException();
        }
    }

    public void checkInvitationToNewFamily(User actor, User invitedUser) {
        if (actor.equals(invitedUser)) {
            throw IllegalActionException.getSelfInvitationErrorException();
        }
        if (isActorAllRight(actor)) {
            throw IllegalActionException.getInvitationErrorException();
        } else if (isInvitedAllRight(invitedUser)) {
            throw IllegalActionException.getInvitingErrorException();
        }
    }

    public boolean checkInvitationToExistFamily(User actor, User invitedUser) {
        if (!isActorAllRight(actor)) {
            throw IllegalActionException.getInvitationErrorException();
        } else if (isInvitedAllRight(invitedUser)) {
            throw IllegalActionException.getInvitingErrorException();
        }
        return true;
    }

    public void checkDeletion(User deletionUser, Long familyId ) {
        if (deletionUser.isNoFamily()) {
            throw IllegalActionException.getUserIsNoFamilyException();
        }

        long deletionUserFamilyId = deletionUser.getFamily().getId();

        if (deletionUserFamilyId != familyId) {
            throw IllegalActionException.getUserNotInYourFamilyException();
        }

        if (deletionUser.isAdmin()) {
            throw IllegalActionException.getCannotRemoveAdminException();
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
