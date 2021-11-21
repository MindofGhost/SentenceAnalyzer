package ru.maistudents.backendsentenceanalyzer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maistudents.backendsentenceanalyzer.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByName(String name);

    Boolean existsByName(String name);
}
