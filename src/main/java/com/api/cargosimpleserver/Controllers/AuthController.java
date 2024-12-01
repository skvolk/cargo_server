package com.api.cargosimpleserver.Controllers;

import com.api.cargosimpleserver.DTO.UserAuthDTO;
import com.api.cargosimpleserver.DTO.UserRegistrationDTO;
import com.api.cargosimpleserver.Responses.LoginResponse;
import com.api.cargosimpleserver.Responses.RegisterResponse;
import com.api.cargosimpleserver.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер для управления аутентификацией и регистрацией пользователей.
 * <p>
 * Функциональность:
 * - Регистрация новых пользователей
 * - Аутентификация существующих пользователей
 * <p>
 * Бизнес-логика:
 * - Обработка запросов на регистрацию
 * - Проверка учетных данных при входе
 * - Базовая валидация пользовательских данных
 * <p>
 * Маршрутизация:
 * - Базовый путь: /api/auth
 * @see UserService
 * @see UserRegistrationDTO
 * @see UserAuthDTO
 * @see RegisterResponse
 * @see LoginResponse
 */
@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * Регистрация нового пользователя в системе.
     * <p>
     * Метод обрабатывает запрос на создание новой учетной записи.
     *
     * @param registrationDTO DTO с информацией для регистрации пользователя
     * @return Ответ с результатом регистрации
     * <p>
     * Бизнес-правила:
     * - Проверка уникальности логина
     * - Валидация входных данных
     * - Хеширование пароля
     * - Создание профиля пользователя
     * <p>
     * Сценарии использования:
     * - Первичная регистрация в системе
     * - Создание нового пользовательского аккаунта
     *
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserRegistrationDTO registrationDTO) {
        userService.register(registrationDTO);
        RegisterResponse response = RegisterResponse.builder()
                .login(registrationDTO.getLogin())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Аутентификация пользователя в системе.
     * <p>
     * Метод обрабатывает запрос на вход пользователя в систему.
     *
     * @param authDTO DTO с учетными данными пользователя
     * @return Ответ с результатом аутентификации
     * <p>
     * Бизнес-правила:
     * - Проверка корректности учетных данных
     * - Верификация пользователя
     * - Генерация токена доступа (неявно в сервисе)
     * <p>
     * Сценарии использования:
     * - Вход зарегистрированного пользователя
     * - Получение доступа к защищенным ресурсам
     *
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserAuthDTO authDTO) {
        userService.authenticate(authDTO);
        LoginResponse response = LoginResponse.builder()
                .login(authDTO.getLogin())
                .build();

        return ResponseEntity.ok(response);
    }
}
