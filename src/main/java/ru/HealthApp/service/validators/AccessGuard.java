package ru.HealthApp.service.validators;

import org.springframework.stereotype.Component;
import ru.HealthApp.repository.entities.FamilyRole;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.exceptions.AccessDeniedException;
import ru.HealthApp.service.exceptions.ExceptionMessage;

@Component
public class AccessGuard {

    /**
     * Проверка прав на СОЗДАНИЕ или РЕДАКТИРОВАНИЕ записи.
     * Вызывается перед сохранением в базу.
     */

    public void checkWriteAccess(User actor, User target) {

        if (actor.getFamilyRole() == FamilyRole.DOCTOR) {
            throw new AccessDeniedException(ExceptionMessage.WRITE_EXCEPTION.getMessage(), true);
        }

        if (actor.getId().equals(target.getId())) {
            return;
        }

        if (target.isVirtual() && actor.getFamilyRole() == FamilyRole.ADMIN && isSameFamily(actor, target)) {
            return;
        }

        throw new AccessDeniedException(ExceptionMessage.WRITE_EXCEPTION.getMessage(), true);
    }

    public void checkReadAccess(User actor, User target) {

        if (actor.getFamilyRole() == FamilyRole.DOCTOR) {
            boolean isDoctorOfThisFamily = target.getFamily().getDoctors().contains(actor);
            if (isDoctorOfThisFamily) {
                return;
            }
        }

        if (actor.getId().equals(target.getId())) {
            return;
        }

        if (isSameFamily(actor, target)) {
            return;
        }

        throw new AccessDeniedException(ExceptionMessage.READ_EXCEPTION.getMessage(), true);
    }

    private boolean isSameFamily(User actor, User target) {

        return actor.getFamily() != null && actor.getFamily().equals(target.getFamily());
    }
}
