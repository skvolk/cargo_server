package com.api.cargosimpleserver.Exceptions;

import com.api.cargosimpleserver.Responses.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для всего приложения.
 * <p>
 * Централизованно управляет обработкой различных типов исключений,
 * преобразуя их в понятные HTTP-ответы с информативными сообщениями об ошибках.
 * <p>
 * Основные функции:
 * - Перехват и обработка исключений на уровне контроллеров
 * - Логирование ошибок
 * - Формирование стандартизированных ответов об ошибках
 * - Маппинг исключений на соответствующие HTTP-статусы
 *
 * @see ControllerAdvice
 * @see ExceptionHandler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Логгер для записи информации об ошибках.
     * Используется для логирования исключительных ситуаций
     * с целью диагностики и мониторинга.
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Обработчик общих runtime-исключений.
     *
     * @param ex Перехваченное runtime-исключение
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST
     * <p>
     * Применяется для:
     * - Логирования неожиданных ошибок
     * - Возврата информативного сообщения об ошибке
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        logger.error("Ошибка при обработке запроса: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Обработчик ошибок валидации аргументов метода.
     *
     * @param ex Исключение, возникающее при невалидных аргументах
     * @return ResponseEntity с детализированными ошибками валидации
     * <p>
     * Особенности:
     * - Собирает все ошибки валидации по полям
     * - Формирует структурированный ответ с описанием ошибок
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации",
                errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик нарушений ограничений (constraint violations).
     *
     * @param ex Исключение при нарушении Constrains в модели
     * @return ResponseEntity с описанием нарушенных ограничений
     * <p>
     * Применяется для:
     * - Валидации данных на уровне сущностей
     * - Информирования о конкретных нарушениях ограничений
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )
        );

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации",
                errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Универсальный обработчик непредвиденных исключений.
     *
     * @param ex Любое перехваченное исключение
     * @return ResponseEntity с общим сообщением об ошибке
     * <p>
     * Служит страховочным механизмом для:
     * - Предотвращения падения приложения
     * - Возврата клиенту информативного сообщения
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Произошла ошибка",
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик исключений, связанных с отсутствием товара.
     *
     * @param ex Исключение при попытке работы с несуществующим товаром
     * @return ResponseEntity со статусом NOT_FOUND
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Товар не найден",
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик исключений при попытке создания дублирующегося товара.
     *
     * @param ex Исключение при конфликте данных о товаре
     * @return ResponseEntity со статусом CONFLICT
     */
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Конфликт данных",
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}