package com.api.cargosimpleserver.Services;

import com.api.cargosimpleserver.DTO.WarehouseDTO;
import com.api.cargosimpleserver.DTO.WarehouseInStockDTO;
import com.api.cargosimpleserver.Repositories.ProductRepository;
import com.api.cargosimpleserver.Repositories.WarehouseRepository;
import com.api.cargosimpleserver.Models.Product;
import com.api.cargosimpleserver.Models.Warehouse;
import com.api.cargosimpleserver.Models.WarehouseInStock;
import com.api.cargosimpleserver.Repositories.WarehouseInStockRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления складскими запасами.
 *
 * <p>
 * Предоставляет функционал для:
 * - Создания записей о складских запасах
 * - Обновления существующих записей
 * - Удаления записей о складских запасах
 * - Проверки возможности добавления товаров на склад
 * <p>
 *
 */
@Service
@RequiredArgsConstructor
public class WarehouseInStockService {

    private final ProductRepository productRepository;

    private final WarehouseRepository warehouseRepository;

    private final ModelMapper modelMapper;

    private final WarehouseInStockRepository warehouseInStockRepository;

    /**
     * Создание новой записи о складском запасе.
     *
     * @param warehouseStockDTO DTO с информацией о складском запасе
     * @return Созданная запись о складском запасе в виде DTO
     * @throws EntityNotFoundException если товар или склад не найдены
     * @throws IllegalArgumentException если невозможно добавить товар на склад
     */
    @Transactional
    public WarehouseInStockDTO createWarehouseStock(@Valid @NotNull WarehouseInStockDTO warehouseStockDTO) {

        Product product = productRepository.findById(warehouseStockDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Товар не найден"));

        Warehouse warehouse = warehouseRepository.findById(warehouseStockDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));

        if (canAddProductToWarehouse(warehouse.getId(), warehouseStockDTO.getCurrentQuantity())) {
            throw new IllegalArgumentException("Невозможно добавить товар: нет места на складе");
        }

        if (warehouseInStockRepository.existsByProductIdAndWarehouseId(
                warehouseStockDTO.getProductId(),
                warehouseStockDTO.getWarehouseId()
        )) {
            throw new IllegalArgumentException("Запись для данного товара на складе уже существует!");
        }

        WarehouseInStock warehouseStock = modelMapper.map(warehouseStockDTO, WarehouseInStock.class);
        warehouseStock.setProduct(product);
        warehouseStock.setWarehouse(warehouse);

        WarehouseInStock savedStock = warehouseInStockRepository.save(warehouseStock);

        return modelMapper.map(savedStock, WarehouseInStockDTO.class);
    }

    /**
     * Обновление существующей записи о складском запасе.
     *
     * @param warehouseStockDTO DTO с обновленной информацией о складском запасе
     * @return Обновленная запись о складском запасе в виде DTO
     * @throws EntityNotFoundException если запись, товар или склад не найдены
     * @throws IllegalArgumentException если невозможно обновить количество
     */
    @Transactional
    public WarehouseInStockDTO updateWarehouseStock(@Valid @NotNull WarehouseInStockDTO warehouseStockDTO) {

        WarehouseInStock existingWarehouseStock = warehouseInStockRepository.findById(warehouseStockDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Складской запас с ID " + warehouseStockDTO.getId() + " не найден"
                ));

        if (warehouseStockDTO.getCurrentQuantity() != null) {
            Long warehouseId = existingWarehouseStock.getWarehouse().getId();

            int quantityDifference = warehouseStockDTO.getCurrentQuantity() - existingWarehouseStock.getCurrentQuantity();

            if (canAddProductToWarehouse(warehouseId, quantityDifference)) {
                throw new IllegalArgumentException("Невозможно обновить количество: нет места на складе");
            }
        }

        if (warehouseStockDTO.getProductId() != null) {
            Product product = productRepository.findById(warehouseStockDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Товар с ID " + warehouseStockDTO.getProductId() + " не найден"
                    ));
            existingWarehouseStock.setProduct(product);
        }

        if (warehouseStockDTO.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(warehouseStockDTO.getWarehouseId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Склад с ID " + warehouseStockDTO.getWarehouseId() + " не найден"
                    ));
            existingWarehouseStock.setWarehouse(warehouse);
        }

        modelMapper.map(warehouseStockDTO, existingWarehouseStock);

        WarehouseInStock updatedWarehouseStock = warehouseInStockRepository.save(existingWarehouseStock);

        return modelMapper.map(updatedWarehouseStock, WarehouseInStockDTO.class);
    }

    /**
     * Удаление записи о складском запасе.
     *
     * @param id Идентификатор записи о складском запасе
     * @throws EntityNotFoundException если запись не найдена
     */
    @Transactional
    public void deleteWarehouseStock(@Valid @NotNull Long id) {
        warehouseInStockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись о складском запасе не найдена"));

        warehouseInStockRepository.deleteById(id);
    }

    /**
     * Проверка возможности добавления товара на склад.
     *
     * @param warehouseId Идентификатор склада
     * @param quantityToAdd Количество товара для добавления
     * @return true, если товар может быть добавлен, иначе false
     */
    public boolean canAddProductToWarehouse(Long warehouseId, Integer quantityToAdd) {

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));


        Integer totalWarehouseQuantity = warehouseInStockRepository.getTotalWarehouseQuantity(warehouseId);

        return totalWarehouseQuantity + quantityToAdd > warehouse.getCapacity();
    }

    /**
     * Получение всех записей о складских запасах.
     *
     * @return Список всех записей о складских запасах в виде DTO
     */
    public List<WarehouseInStockDTO> getAllWarehouseStocks() {
        return warehouseInStockRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получение записи о складском запасе по идентификатору.
     *
     * @param id Идентификатор записи о складском запасе
     * @return Запись о складском запасе в виде DTO
     * @throws RuntimeException если запись не найдена
     */
    public WarehouseInStockDTO getWarehouseStockById(Long id) {
        return warehouseInStockRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Склад не найден"));
    }

    /**
     * Получение всех складских запасов для определенного товара.
     *
     * @param productId Идентификатор товара
     * @return Список записей о складских запасах для данного товара в виде DTO
     */
    public List<WarehouseInStockDTO> getStocksByProduct(Long productId) {
        return warehouseInStockRepository.findByProductId(productId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Конвертация сущности WarehouseInStock в DTO.
     *
     * @param stock Сущность WarehouseInStock
     * @return DTO WarehouseInStockDTO
     */
    private WarehouseInStockDTO convertToDTO(WarehouseInStock stock) {
        return modelMapper.map(stock, WarehouseInStockDTO.class);
    }

    /**
     * Проверка существования записи о складском запасе по идентификатору товара.
     *
     * @param productId Идентификатор товара
     * @return true, если запись существует, иначе false
     */
    public boolean existsByProductId(Long productId) {
        return warehouseInStockRepository.existsByProductId(productId);
    }
}
