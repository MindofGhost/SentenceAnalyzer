package ru.maistudents.backendsentenceanalyzer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maistudents.backendsentenceanalyzer.analyzer.RussianLuceneMorphology;
import ru.maistudents.backendsentenceanalyzer.entities.Role;
import ru.maistudents.backendsentenceanalyzer.entities.Text;
import ru.maistudents.backendsentenceanalyzer.entities.Word;
import ru.maistudents.backendsentenceanalyzer.repositories.RoleRepository;
import ru.maistudents.backendsentenceanalyzer.repositories.TextRepository;
import ru.maistudents.backendsentenceanalyzer.repositories.WordRepository;

import java.util.*;

@Service
public class TextService {

    private final WordRepository wordRepository;
    private final TextRepository textRepository;
    private final RoleRepository roleRepository;
    private final RussianLuceneMorphology russianLuceneMorphology;


    @Autowired
    public TextService(WordRepository wordRepository,
                       TextRepository textRepository,
                       RoleRepository roleRepository,
                       RussianLuceneMorphology russianLuceneMorphology) {
        this.wordRepository = wordRepository;
        this.textRepository = textRepository;
        this.roleRepository = roleRepository;
        this.russianLuceneMorphology = russianLuceneMorphology;

    }

    public Map<Integer, String> getOutputText(Text text) {
        Map<Integer, String> integerRoleMap = new HashMap<>();
        for(Word word: wordRepository.findAllByParentTextOrderByPosition(text)) {
            integerRoleMap.put(word.getPosition(), word.getRole().getName());
        }
        return integerRoleMap;
    }

    public void analyzeText(Text text) {
        final String delimiters = "[, .:\"«»—\\n\\t\\r\\f]";
        String[] words = text.getText().split(delimiters);
        int position = 1;
        for (String word : words) {
            if (word.matches("[а-яА-Я-]+")) {
                createWord(text, word.trim(), position++);
            }
        }
        textRepository.save(text);
    }

    private void createWord(Text text, String value, int position) {
        Word word = new Word();
        word.setParentText(text);
        word.setPosition(position);
        word.setValue(value);
        word.setRole(generateRole(value));
        wordRepository.save(word);
    }

    private Role generateRole(String value) {
        value = value.toLowerCase(Locale.ROOT);
        for (String word : russianLuceneMorphology.getMorphInfo(value)) {
            if (word.split("\\|")[0].equals(value)) {
                return switchRole(word.split(" ")[1]);
            }
        }
        return switchRole(russianLuceneMorphology.getMorphInfo(value).get(0).split(" ")[1]);
    }

    private Role switchRole(String lemma){
        switch (lemma) {
            // Существительное
            case "С":
            // Местоимение существительное
            case "МС":
                return roleRepository.findRoleByName("Подлежащее");
            // Глагол
            case "Г":
            case "ИНФИНИТИВ":
            case "ПРЕДИКАТИВ":
                return roleRepository.findRoleByName("Сказуемое");
            // Прилагательное
            case "П":
            case "ПРИЧАСТИЕ":
            // числительное порядковое
            case "ЧИСЛ-П":
                return roleRepository.findRoleByName("Определение");
            case "ДЕЕПРИЧАСТИЕ":
            // Наречие
            case "Н":
            // Местоименное прилагательное
            case "МС-П":
                return roleRepository.findRoleByName("Обстоятельство");
            case "ЧИСЛ":
            default:
                return roleRepository.findRoleByName("Дополнение");
        }
    }
}
