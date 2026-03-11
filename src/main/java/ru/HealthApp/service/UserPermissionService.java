package ru.HealthApp.service;

import org.springframework.stereotype.Service;
import ru.HealthApp.repository.entities.User;

@Service
public class UserPermissionService {

    /**
     * Может ли пользователь управлять семьей (добавлять/удалять членов)
     */
    public boolean canManageFamily(User user) {

        return user.isAdmin();
    }

    /**
     * Может ли пользователь записывать медицинские данные
     */
    public boolean canWriteMedicalRecords(User user) {

        return !user.isDoctor();
    }

    /**
     * Может ли пользователь просматривать семейную панель
     */
    public boolean canAccessFamilyDashboard(User user) {

        return user.isAdmin();
    }

    /**
     * Может ли пользователь просматривать медицинские записи (для врачей)
     */
    public boolean canAccessMedicalRecords(User user) {

        return user.isDoctor();
    }

    /**
     * Может ли данный пользователь управлять этим пользователем
     */
    public boolean canBeManagedBy(User manager, User target) {
        return target.isVirtual() && manager.isAdmin() && manager.getFamily().equals(target.getFamily());
    }

    /**
     * Может ли данный пользователь записывать данные для этого пользователя
     */
    public boolean canBeWrittenBy(User target, User writer) {
        if (writer.equals(target)) return true;
        if (target.isVirtual() && writer.isAdmin() && writer.getFamily().equals(target.getFamily())) return true;
        return writer.getFamily().equals(target.getFamily()) && writer.isAdmin();
    }

    /**
     * Может ли данный пользователь читать данные этого пользователя
     */
    public boolean canBeReadBy(User target, User reader) {
        if (reader.equals(target)) return true;

        if (reader.isDoctor() && target.isDoctorOfUserFamily(reader)) {
            return true;
        }

        return !reader.isNoFamily() && reader.getFamily().equals(target.getFamily());
    }
}
