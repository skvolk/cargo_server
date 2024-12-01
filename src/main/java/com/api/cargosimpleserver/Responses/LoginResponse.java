package com.api.cargosimpleserver.Responses;

import lombok.Builder;
import lombok.Data;

/**
 * Ответ на запрос аутентификации (входа) пользователя.
 *
 * <p>
 * Этот класс представляет собой структуру данных, которая
 * используется для передачи информации о результате
 * успешной аутентификации пользователя, включая его логин.
 * </p>
 *
 * @see RegisterResponse
 */
@Data
@Builder
public class LoginResponse {

    private String login;
}
