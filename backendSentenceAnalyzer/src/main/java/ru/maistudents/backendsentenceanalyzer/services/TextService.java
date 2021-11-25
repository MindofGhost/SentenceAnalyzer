package ru.maistudents.backendsentenceanalyzer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maistudents.backendsentenceanalyzer.entities.Role;
import ru.maistudents.backendsentenceanalyzer.entities.Text;
import ru.maistudents.backendsentenceanalyzer.entities.Word;
import ru.maistudents.backendsentenceanalyzer.repositories.RoleRepository;
import ru.maistudents.backendsentenceanalyzer.repositories.TextRepository;
import ru.maistudents.backendsentenceanalyzer.repositories.WordRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class TextService {

    private final WordRepository wordRepository;
    private final TextRepository textRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public TextService(WordRepository wordRepository,
                       TextRepository textRepository,
                       RoleRepository roleRepository) {
        this.wordRepository = wordRepository;
        this.textRepository = textRepository;
        this.roleRepository = roleRepository;
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
        word.setRole(generateRole());
        wordRepository.save(word);
    }

    private Role generateRole() {
        int roleNumber = (int) ( Math.random() * 4 );
        return switchRole(roleNumber);
    }

    private Role switchRole(int number){
        switch (number) {
            case 0:
                return roleRepository.findRoleByName("Подлежащее");
            case 1:
                return roleRepository.findRoleByName("Сказуемое");
            case 2:
                return roleRepository.findRoleByName("Определение");
            case 3:
                return roleRepository.findRoleByName("Обстоятельство");
            case 4:
                return roleRepository.findRoleByName("Дополнение");
        }
        return null;
    }
}
