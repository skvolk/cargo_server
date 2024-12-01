package com.api.cargosimpleserver.Exceptions;

/**
 * Исключение, выбрасываемое при попытке создания товара,
 * который уже существует в системе.
 * <p>
 * Используется в сценариях:
 * - Добавление товара с существующим артикулом
 * - Дублирование товара в базе данных
 * - Нарушение уникальности ключевых полей товара
 * <p>
 * Наследуется от {@link RuntimeException}, что позволяет
 * гибко обрабатывать конфликты при создании новых товаров.
 */
public class ProductAlreadyExistsException extends RuntimeException {

    /**
     * Создает исключение с пользовательским информативным сообщением.
     *
     * @param message Детальное описание причины возникновения конфликта
     *                Должно содержать информацию о дублирующемся товаре
     * <p>
     * Примеры сообщений:
     * - "Товар с артикулом {articleNumber} уже существует"
     * - "Товар '{productName}' already present in the database"
     */
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
