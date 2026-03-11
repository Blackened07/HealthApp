package ru.HealthApp.service.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    READ_EXCEPTION("Вы не можете просматривать данные пользователя"),
    NOT_ADMIN_EXCEPTION("Вы не администратор"),
    NO_FAMILY_EXCEPTION("Вы одиночный юзер"),
    WRITE_EXCEPTION("У вас нет прав на внесение или изменение данных этого пользователя."),
    USER_SEARCHING_ERROR("Пользователь с %n не найден"),
    MAIN_VALUE_ERROR("Основной показатель не может быть пустым."),
    VALUE_OUT_OF_RANGE("%n за пределами нормы: от %.1n до %.1n"),
    BP_VALUE2_ERROR("Для замера давления необходимы два числа (верхнее и нижнее)"),
    SUB_ZERO_VALUE("Значение показателя должно быть больше нуля."),
    PRESSURE_DANGER("Критическое давление: %.0f/%.0f"),
    GLUCOSE_DANGER("Опасный уровень сахара: %.1f"),
    TEMPERATURE_DANGER("Критическая температура: %.1f"),
    RECORD_NOT_FOUND("Запись не найдена"),
    FAMILY_NOT_FOUND("Семья не найдена"),
    USER_ALREADY_IN_FAMILY("Пользователь уже состоит в семье"),
    CANNOT_REMOVE_ADMIN("Нельзя удалить админа семьи");

    private final String message;

    public static String createMessageWithArgs(ExceptionMessage message, Object... args) {
        return String.format(message.getMessage(), args);
    }
}
