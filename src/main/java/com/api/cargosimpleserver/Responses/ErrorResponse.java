package com.api.cargosimpleserver.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Стандартный ответ об ошибке для обработки и передачи
 * информации об исключительных ситуациях в приложении.
 *
 * <p>
 * Этот класс используется для унифицированного представления
 * ошибок, возникающих в различных компонентах системы,
 * и предоставляет клиенту детальную информацию о произошедшей ошибке.
 * </p>
 *
 * @see RuntimeException
 * @see Exception
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;

    private String message;

    private Object errors;
}
