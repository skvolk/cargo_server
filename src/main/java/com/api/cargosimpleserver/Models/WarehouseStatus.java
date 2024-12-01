package com.api.cargosimpleserver.Models;

/**
 * Перечисление статусов склада.
 * <p>
 * Определяет возможные состояния склада в системе управления складом.
 * Используется для отслеживания текущего состояния складских помещений.
 */
public enum WarehouseStatus {

    /**
     * Статус активного склада.
     * Означает, что склад функционирует и готов к приему и отгрузке товаров.
     */
    ACTIVE,

    /**
     * Статус неактивного склада.
     * Указывает на то, что склад временно не используется,
     * может быть закрыт на ремонт, инвентаризацию или по другим причинам.
     */
    INACTIVE
}
