package com.api.cargosimpleserver.Repositories;

import com.api.cargosimpleserver.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для управления пользовательскими сущностями в базе данных.
 * <p>
 * Расширяет JpaRepository, предоставляя базовые операции CRUD
 * для работы с пользователями и специализированные методы поиска.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по его уникальному имени пользователя.
     *
     * @param username уникальное имя пользователя для поиска
     * @return Optional с найденным пользователем или пустой Optional,
     *         если пользователь не найден
     */
    Optional<User> findByUsername(String username);
}
