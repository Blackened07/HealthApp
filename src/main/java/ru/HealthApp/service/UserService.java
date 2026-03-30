package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.mapper.HealthRecordMapper;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.exceptions.ResourceNotFoundException;
import ru.HealthApp.utils.PasswordUtil;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HealthRecordMapper mapper;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.userNotFound(userId));
    }

    public UserResponseDTO createUser(String email, String password, String firstName) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(PasswordUtil.encode(password));
        user.setFirstName(firstName);
        user.setLastActivity(java.time.LocalDateTime.now());
        
        User savedUSer = userRepository.save(user);

        return mapper.toResponse(savedUSer);
    }
}
