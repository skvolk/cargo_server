package com.api.cargosimpleserver.Repositories;

import com.api.cargosimpleserver.Models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностями складов в базе данных.
 * <p>
 * Расширяет JpaRepository, предоставляя стандартные CRUD операции
 * для сущности Warehouse с идентификатором типа Long.
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    /**
     * Проверяет существование склада с указанным именем.
     *
     * @param name имя склада для проверки
     * @return true, если склад с таким именем уже существует, иначе false
     */
    boolean existsByName(String name);
}
