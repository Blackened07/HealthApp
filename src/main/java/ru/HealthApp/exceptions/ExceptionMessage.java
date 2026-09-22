package ru.HealthApp.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    //Access
    EMAIL_ALREADY_VERIFY("Почта уже зарегистрирована и подтверждена"),
    EMAIL_NOT_VERIFY("Почта не подтверждена"),
    EXPIRED_CODE("Срок действия кода истек!"),
    WRONG_EMAIL_OR_PASS("Неверный email или пароль"),
    WRONG_VERIFY_CODE("Неверный код подтверждения!"),
    READ_EXCEPTION("Вы не можете просматривать данные пользователя"),
    NOT_ADMIN_EXCEPTION("Вы не администратор"),
    NO_FAMILY_EXCEPTION("Вы одиночный юзер"),
    WRITE_EXCEPTION("У вас нет прав на внесение или изменение данных этого пользователя."),

    //InvalidMetric
    MAIN_VALUE_ERROR("Основной показатель не может быть пустым."),
    VALUE_OUT_OF_RANGE("%s за пределами нормы: от %.1f до %.1f"),
    BP_VALUE2_ERROR("Для замера давления необходимы два числа (верхнее и нижнее)"),
    SUB_ZERO_VALUE("Значение показателя должно быть больше нуля."),
    PRESSURE_DANGER("Критическое давление: %.0f/%.0f"),
    GLUCOSE_DANGER("Опасный уровень сахара: %.1f"),
    TEMPERATURE_DANGER("Критическая температура: %.1f"),

    //ResourceNotFound
    RECORD_NOT_FOUND("Запись не найдена"),
    FAMILY_NOT_FOUND("Семья не найдена"),

    //IllegalAct
    INVITATION_ERROR("Приглашение сломано"),
    INVITING_ERROR("Невозможно быть в двух и более семьях"),
    USER_ALREADY_IN_FAMILY("Пользователь уже состоит в семье"),
    INVITATION_ALREADY_EXIST("Вы уже отправили приглашение"),
    CANNOT_REMOVE_ADMIN("Нельзя удалить админа семьи"),

    //FamGuard
    NOT_ADMIN_EMAIL("Почта принадлежит не администратору");

    private final String message;

    public static String createMessageWithArgs(ExceptionMessage message, Object... args) {

        if(args.length == 1) {
            return String.format(message.getMessage(), args[0]);
        }

        if(args.length == 2) {
            return String.format(message.getMessage(), args[0], args[1]);
        }

        if(args.length == 3) {
            return String.format(message.getMessage(), args[0], args[1], args[2]);
        }

        return message.getMessage();
    }


}
