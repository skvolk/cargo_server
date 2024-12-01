package com.api.cargosimpleserver.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) для регистрации нового пользователя.
 * <p>
 * Используется для:
 * - Передачи данных при регистрации пользователя
 * - Валидации входных данных для создания учетной записи
 * - Обеспечения безопасности при регистрации
 * <p>
 * Основные характеристики:
 * - Минимальный набор данных для создания пользователя
 * - Строгая валидация логина и пароля
 * - Защита от некорректных входных данных
 * <p>
 * Жизненный цикл:
 * 1. Получение данных от клиента
 * 2. Валидация входных параметров
 * 3. Преобразование в сущность пользователя
 * 4. Сохранение в базе данных
 */
@Getter
@Setter
@AllArgsConstructor
public class UserRegistrationDTO {

    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String login;

    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
