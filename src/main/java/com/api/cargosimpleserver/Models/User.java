package com.api.cargosimpleserver.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Представляет сущность пользователя в системе.
 * <p>
 * Содержит базовую информацию для аутентификации и идентификации
 * пользователей в приложении.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     * <p>
     * Автоматически генерируется базой данных при создании новой записи.
     * Является первичным ключом в таблице пользователей.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальное имя пользователя для входа в систему.
     * <p>
     * Требования:
     * - Уникальное значение в базе данных.
     * - Не может быть пустым.
     * - Используется для идентификации пользователя при аутентификации.
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * Зашифрованный пароль пользователя.
     * <p>
     * Требования:
     * - Хранится в зашифрованном виде
     * - Не может быть пустым
     * - Используется для проверки подлинности при входе в систему
     */
    @Column(name = "password", nullable = false)
    private String password;
}