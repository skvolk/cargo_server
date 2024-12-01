package com.api.cargosimpleserver.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Data Transfer Object (DTO) для представления информации о товаре на складе.
 * <p>
 * Используется для:
 * - Передачи данных о наличии товаров на складе
 * - Валидации информации о складских остатках
 * - Управления количественными показателями товара
 * <p>
 * Включает расширенную валидацию с информативными сообщениями об ошибках.
 * <p>
 * Основные характеристики:
 * - Содержит информацию о товаре, складе и его количестве
 * - Поддерживает бизнес-логику учета товаров
 * - Обеспечивает целостность данных через валидационные аннотации
 */
@Data
public class WarehouseInStockDTO {
    private Long id;

    @NotNull(message = "ID товара не может быть пустым")
    @Positive(message = "ID товара должен быть положительным числом")
    private Long productId;

    @NotNull(message = "ID склада не может быть пустым")
    @Positive(message = "ID склада должен быть положительным числом")
    private Long warehouseId;

    @NotNull(message = "Текущее количество не может быть пустым")
    @Min(value = 0, message = "Текущее количество не может быть отрицательным")
    @Max(value = 100000, message = "Текущее количество слишком большое")
    private Integer currentQuantity;

    @NotNull(message = "Зарезервированное количество не может быть пустым")
    @Min(value = 0, message = "Зарезервированное количество не может быть отрицательным")
    @Max(value = 100000, message = "Зарезервированное количество слишком большое")
    private Integer reservedQuantity;

    @Size(min = 2, max = 50, message = "Локация должна содержать от 2 до 50 символов")
    @NotBlank(message = "Локация не может быть пустой")
    private String location;

    @JsonIgnore
    @AssertTrue(message = "Зарезервированное количество не может превышать текущее количество")
    public boolean isValidQuantities() {
        return reservedQuantity <= currentQuantity;
    }
}
