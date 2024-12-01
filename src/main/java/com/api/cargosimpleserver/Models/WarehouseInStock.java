package com.api.cargosimpleserver.Models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Представляет информацию о наличии товара на складе.
 * <p>
 * Отражает текущее количество и статус товара в определенном складском помещении,
 * включая данные о доступном и зарезервированном количестве.
 */
@Entity
@Table(name = "warehouse_stocks")
@Data
public class WarehouseInStock {

    /**
     * Уникальный идентификатор записи о складском запасе.
     * Автоматически генерируется базой данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Товар, находящийся на складе.
     * Устанавливает связь многие-к-одному с сущностью Product.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Склад, на котором расположен товар.
     * Устанавливает связь многие-к-одному с сущностью Warehouse.
     */
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    /**
     * Текущее доступное количество товара на складе.
     */
    @Column(nullable = false)
    private Integer currentQuantity;

    /**
     * Количество товара, зарезервированного для будущих операций.
     */
    @Column(nullable = false)
    private Integer reservedQuantity;

    /**
     * Точное местоположение товара на складе.
     * Может включать информацию о стеллаже, полке или секции.
     */
    @Column(nullable = false)
    private String location;
}