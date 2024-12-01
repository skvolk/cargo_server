package com.api.cargosimpleserver.Exceptions;

/**
 * Исключение, выбрасываемое при невозможности найти запрошенный товар.
 * <p>
 * Используется в случаях, когда:
 * - Товар не существует в базе данных
 * - Запрошенный идентификатор товара не найден
 * - Поиск товара по заданным критериям не дал результатов
 * <p>
 * Наследуется от {@link RuntimeException}, что позволяет
 * не объявлять его явно в сигнатурах методов.
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Создает исключение с пользовательским информативным сообщением.
     *
     * @param message Детальное описание причины возникновения исключения
     *                Должно содержать контекст отсутствующего товара
     * <p>
     * Примеры сообщений:
     * - "Товар с ID {id} не найден"
     * - "Товар с артикулом {articleNumber} отсутствует в базе"
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}
