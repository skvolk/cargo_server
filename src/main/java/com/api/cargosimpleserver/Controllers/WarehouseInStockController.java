package com.api.cargosimpleserver.Controllers;

import com.api.cargosimpleserver.DTO.WarehouseInStockDTO;
import com.api.cargosimpleserver.Services.WarehouseInStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления складскими запасами.
 * <p>
 * Назначение:
 * - Обеспечение API для работы с остатками товаров на складе
 * - Поддержка операций создания, чтения, обновления и удаления складских записей
 * <p>
 * Основные возможности:
 * - Создание новых складских записей
 * - Получение списка и детальной информации о складских запасах
 * - Обновление существующих записей
 * - Удаление складских позиций
 * <p>
 * Маршрутизация:
 * - Базовый путь: /api/warehouse-stocks
 *
 * @see WarehouseInStockService
 * @see WarehouseInStockDTO
 */
@RestController
@RequestMapping("/api/warehouse-stocks")
@Validated
@RequiredArgsConstructor
public class WarehouseInStockController {

    private final WarehouseInStockService warehouseStockService;

    /**
     * Создание новой записи о складском запасе.
     * <p>
     * Эндпоинт для добавления нового товара на склад.
     *
     * @param warehouseStockDTO DTO с информацией о складском запасе
     * @return Созданная запись о складском запасе с HTTP-статусом 201 (Created)
     * <p>
     * Примеры использования:
     * - Добавление нового товара
     * - Первичный учет поступившей продукции
     *
     */
    @PostMapping
    public ResponseEntity<WarehouseInStockDTO> createWarehouseStock(
            @RequestBody WarehouseInStockDTO warehouseStockDTO
    ) {
        WarehouseInStockDTO createdStock = warehouseStockService.createWarehouseStock(warehouseStockDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStock);
    }

    /**
     * Получение полного списка складских запасов.
     * <p>
     * Возвращает все существующие записи о складских остатках.
     *
     * @return Список всех складских запасов
     * <p>
     * Сценарии использования:
     * - Инвентаризация
     * - Формирование общего отчета о складских остатках
     *
     */
    @GetMapping
    public ResponseEntity<List<WarehouseInStockDTO>> getAllWarehouseStocks() {
        return ResponseEntity.ok(warehouseStockService.getAllWarehouseStocks());
    }

    /**
     * Получение детальной информации о складском запасе по его идентификатору.
     *
     * @param id Уникальный идентификатор складской записи
     * @return Детальная информация о складском запасе
     * <p>
     * Применение:
     * - Просмотр конкретной позиции
     * - Детальный анализ остатков
     *
     */
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseInStockDTO> getWarehouseStockById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseStockService.getWarehouseStockById(id));
    }

    /**
     * Обновление существующей записи о складском запасе.
     *
     * @param id Идентификатор обновляемой записи
     * @param stockDTO Новые данные о складском запасе
     * @return Обновленная запись о складском запасе
     * <p>
     * Варианты использования:
     * - Корректировка остатков
     * - Изменение параметров складской записи
     *
     */
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseInStockDTO> updateWarehouseStock(
            @PathVariable Long id,
            @RequestBody WarehouseInStockDTO stockDTO
    ) {
        stockDTO.setId(id);
        WarehouseInStockDTO updatedStock = warehouseStockService.updateWarehouseStock(stockDTO);
        return ResponseEntity.ok(updatedStock);
    }

    /**
     * Удаление складской записи по её идентификатору.
     *
     * @param id Идентификатор удаляемой записи
     * @return HTTP-ответ без тела (No Content)
     * <p>
     * Сценарии применения:
     * - Списание товара
     * - Удаление устаревших записей
     *
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseStock(@PathVariable Long id) {
        warehouseStockService.deleteWarehouseStock(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение списка складских запасов для конкретного товара.
     *
     * @param productId Идентификатор товара
     * @return Список складских запасов указанного товара
     * <p>
     * Применение:
     * - Отслеживание остатков конкретного товара
     * - Анализ распределения товара по складам
     *
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<WarehouseInStockDTO>> getStocksByProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(warehouseStockService.getStocksByProduct(productId));
    }
}
