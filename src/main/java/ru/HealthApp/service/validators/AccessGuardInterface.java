package ru.HealthApp.service.validators;

import ru.HealthApp.entities.User;

public interface AccessGuardInterface {

    void checkManageAccess(User actor);

    void checkReadAccess(User reader, User target);

    void checkWriteAccess(User actor, User target);

    void checkFamilyDashboardAccess(User actor);
}
