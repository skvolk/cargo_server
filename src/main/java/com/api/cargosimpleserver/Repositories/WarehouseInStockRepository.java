package com.api.cargosimpleserver.Repositories;

import com.api.cargosimpleserver.Models.WarehouseInStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для управления записями о товарах на складе.
 * <p>
 * Предоставляет методы для работы с остатками товаров в различных складских помещениях.
 */
@Repository
public interface WarehouseInStockRepository extends JpaRepository<WarehouseInStock, Long> {

    /**
     * Находит все записи о товарах на складе по идентификатору товара.
     *
     * @param productId идентификатор товара
     * @return список записей о товарах на складе
     */
    List<WarehouseInStock> findByProductId(Long productId);

    /**
     * Получает общее количество определенного товара во всех складах.
     *
     * @param productId идентификатор товара
     * @return суммарное количество товара (возвращает 0, если товар отсутствует)
     */
    @Query("SELECT COALESCE(SUM(ws.currentQuantity), 0) FROM WarehouseInStock ws WHERE ws.product.id = :productId")
    Integer getTotalProductQuantity(@Param("productId") Long productId);

    /**
     * Получает общее количество всех товаров на определенном складе.
     *
     * @param warehouseId идентификатор склада
     * @return суммарное количество товаров на складе (возвращает 0, если склад пустой)
     */
    @Query("SELECT COALESCE(SUM(ws.currentQuantity), 0) FROM WarehouseInStock ws WHERE ws.warehouse.id = :warehouseId")
    Integer getTotalWarehouseQuantity(@Param("warehouseId") Long warehouseId);

    /**
     * Проверяет наличие записи о товаре на конкретном складе.
     *
     * @param productId идентификатор товара
     * @param warehouseId идентификатор склада
     * @return true, если товар существует на указанном складе, иначе false
     */
    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);

    /**
     * Подсчитывает количество записей для определенного склада.
     *
     * @param warehouseId идентификатор склада
     * @return количество товарных позиций на складе
     */
    long countByWarehouseId(Long warehouseId);

    /**
     * Проверяет существование товара хотя бы на одном складе.
     *
     * @param productId идентификатор товара
     * @return true, если товар присутствует на любом складе, иначе false
     */
    boolean existsByProductId(Long productId);
}
