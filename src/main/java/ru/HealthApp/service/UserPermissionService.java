package ru.HealthApp.service;

import org.springframework.stereotype.Service;
import ru.HealthApp.repository.entities.User;

@Service
public class UserPermissionService {

    public boolean canManageFamily(User user) {

        return user.isAdmin();
    }

    public boolean canWriteMedicalRecords(User user) {

        return !user.isDoctor();
    }

    public boolean canAccessFamilyDashboard(User user) {

        return user.isAdmin();
    }

    public boolean canAccessMedicalRecords(User user) {

        return user.isDoctor();
    }

    public boolean canBeManagedBy(User manager, User target) {
        return target.isVirtual() && manager.isAdmin() && manager.getFamily().equals(target.getFamily());
    }

    public boolean canBeWrittenBy(User target, User writer) {
        if (writer.equals(target)) return true;
        if (target.isVirtual() && writer.isAdmin() && writer.getFamily().equals(target.getFamily())) return true;
        return writer.getFamily().equals(target.getFamily()) && writer.isAdmin();
    }

    public boolean canBeReadBy(User target, User reader) {
        if (reader.equals(target)) return true;

        if (reader.isDoctor() && target.isDoctorOfUserFamily(reader)) {
            return true;
        }

        return !reader.isNoFamily() && reader.getFamily().equals(target.getFamily());
    }
}
