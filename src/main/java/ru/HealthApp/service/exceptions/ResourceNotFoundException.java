package ru.HealthApp.service.exceptions;

import lombok.Getter;

@Getter
public final class ResourceNotFoundException extends HealthAppException {

    private final String resourceType;
    private final Object resourceId;

    public ResourceNotFoundException(String resourceType, Object resourceId) {

        super(String.format("%s c ID %s не найден", resourceType, resourceId), false);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public static ResourceNotFoundException userNotFound(Long userId) {
        return new ResourceNotFoundException("Пользователь", userId);
    }

    public static ResourceNotFoundException recordNotFound(Long recordId) {
        return new ResourceNotFoundException("Запись", recordId);
    }

}
