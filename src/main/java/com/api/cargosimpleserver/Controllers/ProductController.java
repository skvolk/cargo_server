package com.api.cargosimpleserver.Controllers;

import com.api.cargosimpleserver.DTO.ProductDTO;
import com.api.cargosimpleserver.Services.ProductService;
import com.api.cargosimpleserver.Services.WarehouseInStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления товарами.
 * <p>
 * Функциональность:
 * - Создание новых товаров
 * - Получение списка и информации о товарах
 * - Обновление параметров товаров
 * - Удаление товаров с проверкой складских остатков
 * <p>
 * Бизнес-логика:
 * - Контроль целостности данных о товарах
 * - Предотвращение удаления товаров, имеющихся на складах
 * <p>
 * Маршрутизация:
 * - Базовый путь: /api/products
 *
 * @see ProductService
 * @see WarehouseInStockService
 * @see ProductDTO
 */
@RestController
@RequestMapping("/api/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final WarehouseInStockService warehouseInStockService;

    /**
     * Создание нового товара.
     * <p>
     * Метод обрабатывает запрос на регистрацию нового товара в системе.
     *
     * @param productDTO DTO с информацией о создаваемом товаре
     * @return Созданный товар с HTTP-статусом 201 (Created)
     * <p>
     * Бизнес-правила:
     * - Валидация входных данных
     * - Генерация уникального идентификатора
     * - Проверка уникальности параметров товара
     *
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO savedProductDTO = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }

    /**
     * Получение списка всех товаров.
     * <p>
     * Возвращает полный перечень товаров в системе.
     *
     * @return Список товаров
     * <p>
     * Применение:
     * - Формирование каталога товаров
     * - Инвентаризация номенклатуры
     *
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Получение детальной информации о товаре по его идентификатору.
     *
     * @param id Уникальный идентификатор товара
     * @return Детальная информация о товаре
     * <p>
     * Сценарии использования:
     * - Просмотр параметров конкретного товара
     * - Получение подробной информации о товаре
     *
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Обновление информации о товаре.
     * <p>
     * Позволяет изменять параметры существующего товара.
     *
     * @param id Идентификатор обновляемого товара
     * @param productDTO Новые данные о товаре
     * @return Обновленный товар
     * <p>
     * Ограничения:
     * - Проверка существования товара
     * - Валидация входных данных
     *
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    /**
     * Удаление товара.
     * <p>
     * Метод обеспечивает безопасное удаление товара с проверкой наличия на складах.
     *
     * @param id Идентификатор товара для удаления
     * @return Результат операции удаления
     * <p>
     * Ограничения:
     * - Запрещено удалять товары, имеющиеся на складах
     *
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        boolean isProductInStock = warehouseInStockService.existsByProductId(id);

        if (isProductInStock) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Невозможно удалить товар, так как он присутствует на складах");
        }

        productService.deleteProduct(id);
        return ResponseEntity.ok("Товар успешно удален");
    }
}
