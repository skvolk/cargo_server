package com.api.cargosimpleserver.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Представляет сущность склада в логистической системе.
 * <p>
 * Содержит детальную информацию о складском помещении, включая
 * контактные данные, емкость и текущий статус.
 */
@Entity
@Table(name = "warehouses")
@Data
public class Warehouse {

    /**
     * Уникальный идентификатор склада.
     * Автоматически генерируется базой данных при создании новой записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование склада.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Физический адрес склада.
     * Содержит полный почтовый адрес складского помещения.
     */
    @Column(nullable = false)
    private String address;

    /**
     * Контактное лицо склада.
     * ФИО ответственного сотрудника за управление складом.
     */
    @Column(nullable = false)
    private String contactPerson;

    /**
     * Контактный телефон склада.
     * Номер телефона для связи с администрацией склада.
     */
    @Column(nullable = false)
    private String phone;

    /**
     * Электронная почта склада.
     * Адрес электронной почты для официальной коммуникации.
     */
    @Column(nullable = false)
    private String email;

    /**
     * Общая вместимость склада.
     * Определяет максимальное количество товаров, которое может
     * быть единовременно размещено на складе.
     */
    @Column(nullable = false)
    private Integer capacity;

    /**
     * Текущий статус склада.
     * Указывает на активность или неактивность складского помещения.
     * Использует перечисление {@link WarehouseStatus}.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WarehouseStatus status;

    /**
     * Список товаров, находящихся на складе.
     * Представляет связь один-ко-многим с записями складских запасов.
     * Позволяет отслеживать текущие остатки и резервы.
     */
    @OneToMany(mappedBy = "warehouse")
    private List<WarehouseInStock> stocks;
}