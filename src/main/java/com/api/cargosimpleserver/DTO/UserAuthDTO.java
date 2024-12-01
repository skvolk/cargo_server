package com.api.cargosimpleserver.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) для аутентификации пользователя.
 * <p>
 * Используется для:
 * - Передачи учетных данных при входе в систему
 * - Валидации входных данных для процесса авторизации
 * - Обеспечения безопасности при аутентификации
 * <p>
 * Основные характеристики:
 * - Минимальный набор данных для входа в систему
 * - Строгая валидация логина и пароля
 * - Защита от некорректных входных данных
 * <p>
 * Жизненный цикл:
 * 1. Получение данных от клиента
 * 2. Валидация входных параметров
 * 3. Проверка учетных данных
 * 4. Предоставление доступа или отказ
 */
@Getter
@Setter
@AllArgsConstructor
public class UserAuthDTO {

    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String login;

    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
