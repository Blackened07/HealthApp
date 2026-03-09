package ru.HealthApp.service.validators;

import org.springframework.stereotype.Component;
import ru.HealthApp.repository.entities.User;

@Component
public class FamilyActionGuard {

    public void checkActionPermitted(User author, User target){
        
    }
}
