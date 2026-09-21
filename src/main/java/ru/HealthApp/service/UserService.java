package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.entities.Account;
import ru.HealthApp.mapper.HealthAppMapper;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.utils.PasswordUtil;
import ru.HealthApp.utils.PropertiesUtil;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HealthAppMapper mapper;
    private final JavaMailSender sender;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.userNotFound(userId));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.userNotFound(email));
    }

    public UserResponseDTO createUser(String email, String password, String firstName, String code) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(PasswordUtil.encode(password));
        user.setFirstName(firstName);
        user.setLastActivity(java.time.LocalDateTime.now());
        user.setEnabled(false);
        user.setVerificationCode(code);
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(15));

        User savedUser = userRepository.save(user);
        sendVerificationCode(code, email);

        return mapper.toResponse(savedUser);
    }

    private void sendVerificationCode(String code, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(PropertiesUtil.getProperty(PropertiesUtil.ADMIN_EMAIL));
        message.setTo(email);
        message.setSubject("Код подтверждения для HealthApp");
        message.setText("Ваш код подтверждения: " + code);
        sender.send(message);
    }


    public void saveVerify(Account account) {
        User user = (User) account;
        user.setEnabled(true);
        user.setVerificationCode(null);
        userRepository.save(user);
    }
}
