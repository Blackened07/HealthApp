package ru.HealthApp.mapper;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.auth.UserResponseDTO;
import ru.HealthApp.entities.User;

@Component
public class UserMapper {

    public UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getFamilyRole(),
                user.getLastActivity()
        );
    }
}
