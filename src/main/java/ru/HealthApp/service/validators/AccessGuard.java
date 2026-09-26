package ru.HealthApp.service.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.exceptions.ExceptionMessage;

@Component("accessGuard")
@RequiredArgsConstructor
public class AccessGuard implements AccessGuardInterface {

    public void checkManageAccess(User actor) {
        if (!canManageFamily(actor)) {
            throw new AccessDeniedException(ExceptionMessage.NOT_ADMIN_EXCEPTION.getMessage());
        }
    }

    public void checkReadAccess(User reader, User target) {
        if (!canBeReadBy(target, reader)) {
            throw new AccessDeniedException(ExceptionMessage.READ_EXCEPTION.getMessage());
        }
    }

    public void checkWriteAccess(User actor, User target) {
        if (!canBeWrittenBy(target, actor)) {
            throw new AccessDeniedException(ExceptionMessage.WRITE_EXCEPTION.getMessage());
        }
    }

    public void checkFamilyDashboardAccess(User actor) {
        if (actor.isNoFamily()) {
            throw new AccessDeniedException(ExceptionMessage.NO_FAMILY_EXCEPTION.getMessage());
        }
    }

    private boolean canBeReadBy(User target, User reader) {
        if (isSameUser(reader, target)) {
            return true;
        }

        if (hasNoFamily(reader, target)) {
            return false;
        }

        return areInSameFamily(reader, target);
    }

    private boolean canBeWrittenBy(User target, User writer) {
        if (isSameUser(writer, target)) {
            return true;
        }

        if (areInSameFamily(writer, target) && writer.isAdmin()) {
            return true;
        }

        return target.isVirtual() && writer.isAdmin();
    }

    private boolean isSameUser(User actor, User target) {
        return actor.getId().equals(target.getId());
    }

    private boolean hasNoFamily(User actor, User target) {
        return actor.isNoFamily() || target.isNoFamily();
    }

    private boolean areInSameFamily(User actor, User target) {
        return actor.getFamily().equals(target.getFamily());
    }

    private boolean canManageFamily(User actor) {
        return actor.isAdmin();
    }

}
