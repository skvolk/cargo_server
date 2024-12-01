package com.api.cargosimpleserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс приложения для CargoSimpleServer.
 *
 * <p>
 * Этот класс является точкой входа для Spring Boot приложения,
 * которое предназначено для управления запасами на складе.
 * </p>
 *
 */
@SpringBootApplication
public class CargoSimpleServerApplication {

    /**
     * Главный метод запуска приложения.
     *
     * <p>
     * Инициализирует и запускает Spring Boot приложение с переданными
     * аргументами командной строки.
     * </p>
     *
     * @param args массив аргументов командной строки, передаваемых при запуске
     */
    public static void main(String[] args) {
        SpringApplication.run(CargoSimpleServerApplication.class, args);
    }

}
