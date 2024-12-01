package com.api.cargosimpleserver.Controllers;

import com.api.cargosimpleserver.DTO.WarehouseDTO;
import com.api.cargosimpleserver.Services.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления складами.
 * <p>
 * Функциональность:
 * - Создание новых складов
 * - Получение информации о складах
 * - Обновление параметров складов
 * - Удаление складов
 * <p>
 * Бизнес-логика:
 * - Контроль целостности данных
 * - Предотвращение удаления складов с товарами
 * - Управление статусом складов
 * <p>
 * Маршрутизация:
 * - Базовый путь: /api/warehouse
 *
 * @see WarehouseService
 * @see WarehouseDTO
 */
@RestController
@RequestMapping("/api/warehouses")
@Validated
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    /**
     * Создание нового склада.
     * <p>
     * Метод обрабатывает запрос на регистрацию нового склада в системе.
     *
     * @param warehouseDTO DTO с информацией о создаваемом складе
     * @return Созданный склад с HTTP-статусом 201 (Created)
     * <p>
     * Бизнес-правила:
     * - Валидация входных данных
     * - Генерация уникального идентификатора
     *
     */
    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        WarehouseDTO createdWarehouse = warehouseService.createWarehouse(warehouseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWarehouse);
    }

    /**
     * Получение списка всех складов.
     * <p>
     * Возвращает полный перечень складов в системе.
     *
     * @return Список складов
     * <p>
     * Применение:
     * - Формирование общего реестра складов
     * - Инвентаризация складской инфраструктуры
     *
     */
    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    /**
     * Получение детальной информации о складе по его идентификатору.
     *
     * @param id Уникальный идентификатор склада
     * @return Детальная информация о складе
     * <p>
     * Сценарии использования:
     * - Просмотр параметров конкретного склада
     * - Получение подробной информации
     *
     */
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    /**
     * Обновление информации о складе.
     * <p>
     * Позволяет изменять параметры существующего склада с проверкой бизнес-правил.
     *
     * @param id Идентификатор обновляемого склада
     * @param warehouseDTO Новые данные о складе
     * @return Обновленный склад или сообщение об ошибке
     * <p>
     * Ограничения:
     * - Нельзя деактивировать склад с товарами
     *
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWarehouse(
            @PathVariable Long id,
            @RequestBody WarehouseDTO warehouseDTO
    ) {
        WarehouseDTO currentWarehouse = warehouseService.getWarehouseById(id);

        warehouseDTO.setId(id);

        if (warehouseService.hasProducts(id) && currentWarehouse.getStatus() != warehouseDTO.getStatus()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ошибка: Невозможно изменить активность склада с активного на неактивный, так как на складе есть товары.");
        }

        WarehouseDTO updatedWarehouse = warehouseService.updateWarehouse(warehouseDTO);
        return ResponseEntity.ok(updatedWarehouse);
    }

    /**
     * Удаление склада.
     * <p>
     * Метод обеспечивает безопасное удаление склада с проверкой наличия товаров.
     *
     * @param id Идентификатор склада для удаления
     * @return Результат операции удаления
     * <p>
     * Ограничения:
     * - Запрещено удалять склады с товарами
     *
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {

        if (warehouseService.hasProducts(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка: Невозможно удалить склад, так как на нём есть товары.");
        }

        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok().build();
    }
}
