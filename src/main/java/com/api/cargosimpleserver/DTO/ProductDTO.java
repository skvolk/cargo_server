package com.api.cargosimpleserver.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) для управления информацией о товаре.
 * <p>
 * Используется для:
 * - Передачи и валидации данных о товаре
 * - Управления товарной информацией в системе
 * - Обмена данными между слоями приложения
 * <p>
 * Основные характеристики:
 * - Полное описание товара
 * - Строгая валидация входных данных
 * - Поддержка бизнес-логики управления товарами
 * <p>
 * Жизненный цикл:
 * 1. Создание/редактирование товара
 * 2. Валидация входных параметров
 * 3. Сохранение или обновление в базе данных
 * 4. Использование в логистических и складских процессах
 */
@Data
public class ProductDTO {

    private Long id;

    @Size(min = 8, max = 8, message = "Артикул должен содержать 8 символов")
    @NotBlank(message = "Артикул не может быть пустым")
    private String articleNumber;

    @Size(min = 2, max = 100, message = "Наименование должно содержать от 2 до 100 символов")
    @NotBlank(message = "Наименование не может быть пустым")
    private String name;

    @Size(min = 2, max = 1000, message = "Описание должно содержать от 2 до 1000 символов")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Size(min = 2, max = 50, message = "Категория должна содержать от 2 до 50 символов")
    @NotBlank(message = "Категория не может быть пустой")
    private String category;

    @Size(min = 2, max = 50, message = "Производитель должен содержать от 2 до 50 символов")
    @NotBlank(message = "Производитель не может быть пустым")
    private String manufacturer;

    @NotNull(message = "Цена закупки не может быть пустой")
    private BigDecimal purchasePrice;

    @NotNull(message = "Цена продажи не может быть пустой")
    private BigDecimal sellingPrice;

    @NotNull(message = "Минимальный уровень запаса не может быть пустым")
    @Min(value = 0, message = "Минимальный уровень запаса не может быть отрицательным")
    private Integer minStockLevel;

    @NotNull(message = "Максимальный уровень запаса не может быть пустым")
    @Min(value = 1, message = "Максимальный уровень запаса должен быть больше 0")
    private Integer maxStockLevel;

    @JsonIgnore
    @AssertTrue(message = "Максимальный уровень запаса должен быть больше минимального")
    public boolean isValidStockLevels() {
        return maxStockLevel > minStockLevel;
    }
}
