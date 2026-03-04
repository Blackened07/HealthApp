package ru.HealthApp.service.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    READ_EXCEPTION("Вы не можете просматривать данные пользователя"),
    WRITE_EXCEPTION("У вас нет прав на внесение или изменение данных этого пользователя."),
    USER_SEARCHING_ERROR("Пользователь с %n не найден"),
    MAIN_VALUE_ERROR("Основной показатель не может быть пустым."),
    VALUE_OUT_OF_RANGE("%n за пределами нормы: от %.1n до %.1n"),
    BP_VALUE2_ERROR("Для замера давления необходимы два числа (верхнее и нижнее)"),
    SUB_ZERO_VALUE("Значение показателя должно быть больше нуля.");

    private final String message;

    public static String createMessageWithArgs(ExceptionMessage message, Object... args) {
        return String.format(message.getMessage(), args);
    }
}
