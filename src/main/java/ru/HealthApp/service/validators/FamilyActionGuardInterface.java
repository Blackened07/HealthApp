package ru.HealthApp.service.validators;

import ru.HealthApp.entities.User;

public interface FamilyActionGuardInterface {

    void checkIsAdminEmailInInvitationRequest(User admin);

    void checkInvitationToNewFamily(User actor, User invitedUser);

    boolean checkInvitationToExistFamily(User actor, User invitedUser);

    void checkDeletion(User deletionUser, Long familyId);
}
