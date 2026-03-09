package ru.HealthApp.service.validators;

import org.springframework.stereotype.Component;
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
        if (!target.canBeWrittenBy(actor)) {
            throw new AccessDeniedException(ExceptionMessage.WRITE_EXCEPTION.getMessage());
        }
    }

    public void checkReadAccess(User actor, User target) {
        if (!target.canBeReadBy(actor)) {
            throw new AccessDeniedException(ExceptionMessage.READ_EXCEPTION.getMessage());
        }
    }

    public void checkFamilyDashboardAccess(User actor) {
        if (!actor.canAccessFamilyDashboard()) {
            throw new AccessDeniedException(ExceptionMessage.NOT_ADMIN_EXCEPTION.getMessage());
        }

        if (actor.isNoFamily()) {
            throw new AccessDeniedException(ExceptionMessage.NO_FAMILY_EXCEPTION.getMessage());
        }
    }
}
