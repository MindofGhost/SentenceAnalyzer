package ru.maistudents.backendsentenceanalyzer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maistudents.backendsentenceanalyzer.entities.Text;
import ru.maistudents.backendsentenceanalyzer.entities.Word;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findAllByParentTextOrderByPosition(Text parentText);
}
