package com.api.cargosimpleserver.Security;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для ModelMapper.
 *
 * <p>
 * Этот класс настраивает и предоставляет экземпляр ModelMapper,
 * который используется для преобразования объектов между различными
 * уровнями приложения, такими как DTO и сущности.
 * </p>
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Создание и возврат экземпляра ModelMapper.
     *
     * <p>
     * ModelMapper упрощает процесс маппирования между объектами,
     * позволяя автоматически сопоставлять поля с одинаковыми именами
     * и типами.
     * </p>
     *
     * @return экземпляр ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}