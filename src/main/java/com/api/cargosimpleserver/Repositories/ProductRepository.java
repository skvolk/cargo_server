package com.api.cargosimpleserver.Repositories;

import com.api.cargosimpleserver.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления сущностями товаров в базе данных.
 * <p>
 * Расширяет JpaRepository, предоставляя стандартные CRUD операции для работы с товарами
 * и дополнительные методы специфического поиска.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Проверяет существование товара с указанным артикулом.
     *
     * @param articleNumber артикул товара для проверки
     * @return true, если товар с таким артикулом уже существует,
     *         иначе false
     */
    boolean existsByArticleNumber(String articleNumber);
}
