package ru.maistudents.backendsentenceanalyzer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maistudents.backendsentenceanalyzer.entities.Role;
import ru.maistudents.backendsentenceanalyzer.repositories.RoleRepository;

import javax.annotation.PostConstruct;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void insertRoles() {
        insertRole("Подлежащее");
        insertRole("Сказуемое");
        insertRole("Определение");
        insertRole("Обстоятельство");
        insertRole("Дополнение");
    }

    private void insertRole(String name) {
        if (!roleRepository.existsByName(name)) {
            roleRepository.save(new Role(name));
        }
    }
}
