package com.api.cargosimpleserver.Responses;

import lombok.Builder;
import lombok.Data;

/**
 * Ответ на запрос регистрации пользователя.
 *
 * <p>
 * Этот класс представляет собой структуру данных, которая
 * используется для передачи информации о результате
 * регистрации пользователя, включая его логин.
 * </p>
 */
@Data
@Builder
public class RegisterResponse {

    private String login;
}
