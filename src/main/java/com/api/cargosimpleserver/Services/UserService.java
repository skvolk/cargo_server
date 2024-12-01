package com.api.cargosimpleserver.Services;

import com.api.cargosimpleserver.DTO.UserAuthDTO;
import com.api.cargosimpleserver.DTO.UserRegistrationDTO;
import com.api.cargosimpleserver.Models.User;
import com.api.cargosimpleserver.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления пользователями.
 *
 * <p>
 * Предоставляет функционал для регистрации и аутентификации пользователей.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Регистрация нового пользователя.
     *
     * <p>
     * Проверяет, существует ли пользователь с указанным именем. Если нет,
     * создаёт нового пользователя и сохраняет его в базе данных.
     * </p>
     *
     * @param registrationDTO DTO с данными для регистрации, включая логин и пароль
     * @throws RuntimeException если пользователь с таким именем уже зарегистрирован
     */
    @Transactional
    public void register(@Valid UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getLogin()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже зарегистрирован!");
        }

        User user = new User();
        user.setUsername(registrationDTO.getLogin());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userRepository.save(user);
    }

    /**
     * Аутентификация пользователя.
     *
     * <p>
     * Проверяет, существует ли пользователь с указанным именем и соответствует ли
     * введённый пароль сохранённому. Если аутентификация не удалась, выбрасывает
     * исключение.
     * </p>
     *
     * @param authDTO DTO с данными для аутентификации, включая логин и пароль
     * @throws RuntimeException если пользователь не найден или неверный пароль
     */
    @Transactional
    public void authenticate(UserAuthDTO authDTO) {
        User user = userRepository.findByUsername(authDTO.getLogin())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));

        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Неверный пароль!");
        }
    }
}
