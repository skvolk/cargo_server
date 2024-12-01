package com.api.cargosimpleserver.DTO;

import com.api.cargosimpleserver.Models.WarehouseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Data Transfer Object (DTO) для представления информации о складе.
 * <p>
 * Используется для:
 * - Передачи и валидации данных о складских помещениях
 * - Управления информацией о складской инфраструктуре
 * - Обмена данными между слоями приложения
 * <p>
 * Основные характеристики:
 * - Содержит полную информацию о складе
 * - Обеспечивает строгую валидацию входных данных
 * - Поддерживает бизнес-логику управления складами
 *
 * @see WarehouseStatus
 */
@Data
public class WarehouseDTO {
    private Long id;

    @Size(min = 2, max = 100, message = "Название склада должно содержать от 2 до 100 символов")
    @NotBlank(message = "Название склада не может быть пустым")
    private String name;

    @Size(min = 10, max = 255, message = "Адрес должен содержать от 10 до 255 символов")
    @NotBlank(message = "Адрес не может быть пустым")
    private String address;

    @Size(min = 2, max = 100, message = "Имя контактного лица должно содержать от 2 до 100 символов")
    @NotBlank(message = "Контактное лицо не может быть пустым")
    private String contactPerson;

    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Некорректный формат телефонного номера")
    @NotBlank(message = "Телефон не может быть пустым")
    private String phone;

    @Email(message = "Некорректный формат электронной почты")
    @NotBlank(message = "Email не может быть пустым")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    private String email;

    @NotNull(message = "Вместимость склада не может быть пустой")
    @Min(value = 1, message = "Вместимость склада должна быть больше 0")
    @Max(value = 100000, message = "Вместимость склада слишком большая")
    private Integer capacity;

    @NotNull(message = "Статус склада не может быть пустым")
    private WarehouseStatus status;

    @JsonIgnore
    @AssertTrue(message = "Некорректный статус склада")
    public boolean isValidStatus() {
        return status != null;
    }
}
