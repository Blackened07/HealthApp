package ru.HealthApp.service.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.entities.Doctor;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.exceptions.ExceptionMessage;

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

    public void checkDoctorAccess(Doctor reader, User target) {
        if (canBeReadByDoctor(reader, target)) {
            throw new AccessDeniedException(ExceptionMessage.READ_EXCEPTION.getMessage());
        }
    }

    public void checkWriteAccess(User actor, User target) {
        if (!canBeWrittenBy(target, actor)) {
            throw new AccessDeniedException(ExceptionMessage.WRITE_EXCEPTION.getMessage());
        }
    }

    public void checkFamilyDashboardAccess(User actor) {
        /*if (!canManageFamily(actor)) {
            throw new AccessDeniedException(ExceptionMessage.NOT_ADMIN_EXCEPTION.getMessage());
        }*/

        if (actor.isNoFamily()) {
            throw new AccessDeniedException(ExceptionMessage.NO_FAMILY_EXCEPTION.getMessage());
        }
    }

    private boolean canBeReadByDoctor(Doctor reader, User target) {
        if (target.isNoFamily()) {
            return false;
        }

        return target.isDoctorOfUserFamily(reader);
    }

    private boolean canBeReadBy(User target, User reader) {

        if (reader.getId().equals(target.getId())) {
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

        if (writer.getFamily().equals(target.getFamily()) && writer.isAdmin()) {
            return true;
        }

        if (target.isVirtual() && writer.isAdmin()) {
            return true;
        }

        return !target.isNoFamily() && writer.getFamily().equals(target.getFamily()) && writer.isAdmin();
    }

    private boolean canManageFamily(User actor) {
        return actor.isAdmin();
    }

}
