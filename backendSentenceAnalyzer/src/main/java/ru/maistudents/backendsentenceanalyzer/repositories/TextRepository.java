package ru.maistudents.backendsentenceanalyzer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maistudents.backendsentenceanalyzer.entities.Text;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

}
