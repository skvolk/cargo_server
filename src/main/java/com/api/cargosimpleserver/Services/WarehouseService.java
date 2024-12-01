package com.api.cargosimpleserver.Services;

import com.api.cargosimpleserver.DTO.WarehouseDTO;
import com.api.cargosimpleserver.Models.Warehouse;
import com.api.cargosimpleserver.Models.WarehouseStatus;
import com.api.cargosimpleserver.Repositories.WarehouseInStockRepository;
import com.api.cargosimpleserver.Repositories.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс для управления складами.
 *
 * <p>
 * Этот класс предоставляет методы для создания, обновления, удаления и получения информации о складах.
 * Он использует репозитории для взаимодействия с базой данных и ModelMapper для преобразования между
 * сущностями и объектами передачи данных (DTO).
 * <p>
 *
 * <p>
 * Основные функции:
 * - Создание нового склада
 * - Обновление существующего склада
 * - Удаление склада по идентификатору
 * - Получение списка всех складов
 * - Получение склада по идентификатору
 * - Проверка наличия товаров на складе перед его удалением
 * <p>
 *
 * <p>
 * Логирование событий осуществляется с помощью SLF4J, что позволяет отслеживать действия
 * и возможные ошибки в процессе работы со складами.
 * <p>
 *
 */
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    private final WarehouseInStockRepository warehouseInStockRepository;

    private final WarehouseRepository warehouseRepository;

    private final ModelMapper modelMapper;

    /**
     * Создание нового склад.
     *
     * @param warehouseDTO данные склада
     * @return созданный склад в виде DTO
     * @throws IllegalArgumentException если склад с таким названием уже существует
     */
    @Transactional
    public WarehouseDTO createWarehouse(@Valid @NotNull WarehouseDTO warehouseDTO) {

        if (warehouseRepository.existsByName(warehouseDTO.getName())) {
            throw new IllegalArgumentException("Склад с таким названием уже существует");
        }

        Warehouse warehouse = modelMapper.map(warehouseDTO, Warehouse.class);

        if (warehouse.getStatus() == null) {
            warehouse.setStatus(WarehouseStatus.ACTIVE);
        }

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        logger.info("Создан новый склад: {}", savedWarehouse.getName());

        return modelMapper.map(savedWarehouse, WarehouseDTO.class);
    }

    /**
     * Обновление существующего склада.
     *
     * @param warehouseDTO данные склада
     * @return обновленный склад в виде DTO
     * @throws EntityNotFoundException если склад не найден
     */
    @Transactional
    public WarehouseDTO updateWarehouse(@Valid @NotNull WarehouseDTO warehouseDTO) {

        Warehouse existingWarehouse = warehouseRepository.findById(warehouseDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Склад с ID " + warehouseDTO.getId() + " не найден"));

        modelMapper.map(warehouseDTO, existingWarehouse);

        Warehouse updatedWarehouse = warehouseRepository.save(existingWarehouse);
        logger.info("Обновлен склад: {}", updatedWarehouse.getName());

        return modelMapper.map(updatedWarehouse, WarehouseDTO.class);
    }

    /**
     * Удаление склада по ID.
     *
     * @param id идентификатор склада
     * @throws EntityNotFoundException если склад не найден
     * @throws IllegalStateException если на складе есть товары
     */
    @Transactional
    public void deleteWarehouse(Long id) {

        if (hasProducts(id)) {
            throw new IllegalStateException("Невозможно удалить склад, так как на нем есть товары");
        }

        if (!warehouseRepository.existsById(id)) {
            throw new EntityNotFoundException("Склад с ID " + id + " не найден");
        }

        warehouseRepository.deleteById(id);
        logger.info("Склад с ID {} был удален", id);
    }

    /**
     * Получение списка всех складов.
     *
     * @return список складов в виде DTO
     */
    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получение склада по ID.
     *
     * @param id идентификатор склада
     * @return склад в виде DTO
     * @throws EntityNotFoundException если склад не найден
     */
    public WarehouseDTO getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Склад не найден"));
    }

    /**
     * Конвертирование сущности склада в DTO.
     *
     * @param warehouse сущность склада
     * @return DTO склада
     */
    private WarehouseDTO convertToDTO(Warehouse warehouse) {
        return modelMapper.map(warehouse, WarehouseDTO.class);
    }

    /**
     * Проверка наличия товаров на складе.
     *
     * @param warehouseId идентификатор склада
     * @return true, если на складе есть товары, иначе false
     */
    public boolean hasProducts(Long warehouseId) {
        return warehouseInStockRepository.countByWarehouseId(warehouseId) > 0;
    }
}
