package ru.HealthApp.service.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.exceptions.AccessDeniedException;
import ru.HealthApp.service.exceptions.ExceptionMessage;

@Component
@RequiredArgsConstructor
public class AccessGuard {

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

    public void checkWriteAccess(User writer, User target) {
        if (!canBeWrittenBy(target, writer)) {
            throw new AccessDeniedException(ExceptionMessage.WRITE_EXCEPTION.getMessage());
        }
    }

    public void checkFamilyDashboardAccess(User actor) {
        if (!canManageFamily(actor)) {
            throw new AccessDeniedException(ExceptionMessage.NOT_ADMIN_EXCEPTION.getMessage());
        }

        if (actor.isNoFamily()) {
            throw new AccessDeniedException(ExceptionMessage.NO_FAMILY_EXCEPTION.getMessage());
        }
    }


    private boolean canBeReadBy(User target, User reader) {

        if (reader.getId().equals(target.getId())) {
            return true;
        }

        if (reader.isDoctor() && target.isDoctorOfUserFamily(reader)) {
            return true;
        }

        if (reader.isNoFamily() || target.isNoFamily()) {
            return false;
        }

        return reader.getFamily().equals(target.getFamily());
    }

    private boolean canBeWrittenBy(User target, User writer) {

        if (writer.getId().equals(target.getId())) {
            return true;
        }

        if (target.isVirtual() && writer.isAdmin() && writer.getFamily().equals(target.getFamily())) {
            return true;
        }

        return !target.isNoFamily() && writer.getFamily().equals(target.getFamily()) && writer.isAdmin();
    }

    private boolean canManageFamily(User actor) {
        return actor.isAdmin();
    }

}
