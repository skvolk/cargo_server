package com.api.cargosimpleserver.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Представляет сущность товара в логистической системе.
 * <p>
 * Содержит полную информацию о товаре, включая идентификационные,
 * ценовые и складские характеристики.
 */
@Entity
@Table(name = "products")
@Data
public class Product {

    /**
     * Уникальный идентификатор товара в базе данных.
     * Автоматически генерируется при создании новой записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Артикул товара.
     * <p>
     * Уникальный код продукта в системе:
     * - Обязателен для заполнения
     * - Должен быть уникальным
     */
    @Column(unique = true, nullable = false)
    private String articleNumber;

    /**
     * Наименование товара.
     * <p>
     * Полное официальное название товара.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Подробное описание товара.
     * <p>
     * Содержит развернутую информацию о характеристиках,
     * особенностях и составе товара.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Категория товара.
     * <p>
     * Классификационная группа, к которой относится товар.
     * Используется для систематизации и поиска товаров.
     */
    @Column(nullable = false)
    private String category;

    /**
     * Производитель товара.
     * <p>
     * Наименование компании-изготовителя товара.
     * Важная информация для отслеживания происхождения товара.
     */
    @Column(nullable = false)
    private String manufacturer;

    /**
     * Закупочная цена товара.
     * <p>
     * Стоимость приобретения товара у поставщика.
     * Используется для расчета себестоимости и маржинальности.
     */
    @Column(nullable = false)
    private BigDecimal purchasePrice;

    /**
     * Розничная цена товара.
     * <p>
     * Цена реализации товара конечному потребителю.
     * Определяет выручку и прибыль от продажи.
     */
    @Column(nullable = false)
    private BigDecimal sellingPrice;

    /**
     * Минимальный уровень складского запаса.
     * <p>
     * Критическое количество товара на складе,
     * при котором необходимо инициировать пополнение запаса.
     */
    @Column(nullable = false)
    private Integer minStockLevel;

    /**
     * Максимальный уровень складского запаса.
     * <p>
     * Предельное количество товара, рекомендованное
     * для хранения на складе.
     */
    @Column(nullable = false)
    private Integer maxStockLevel;

    /**
     * Список складских записей для данного товара.
     * <p>
     * Отражает текущие остатки товара на различных складах.
     * Реализует связь один-ко-многим с сущностью {@link WarehouseInStock}.
     */
    @OneToMany(mappedBy = "product")
    private List<WarehouseInStock> stocks;
}