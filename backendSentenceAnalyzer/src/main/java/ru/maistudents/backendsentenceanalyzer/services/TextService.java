package ru.maistudents.backendsentenceanalyzer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maistudents.backendsentenceanalyzer.analyzer.RussianLuceneMorphology;
import ru.maistudents.backendsentenceanalyzer.dto.OutputWordDTO;
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

    public List<OutputWordDTO> getOutputText(Text text) {
        List<OutputWordDTO> wordDTOList = new LinkedList<>();
        for(Word word: wordRepository.findAllByParentTextOrderByPosition(text)) {
            wordDTOList.add(new OutputWordDTO(word.getValue(), word.getRole().getName()));
        }
        return wordDTOList;
    }

    public void analyzeText(Text text) {
        final String delimiters = "[\\s\\n\\t\\r\\f]";
        String[] words = text.getText().split(delimiters);
        int position = 1;
        for (String word : words) {
            createWord(text, word.trim(), position++);
        }
        textRepository.save(text);
    }

    private void createWord(Text text, String value, int position) {
        if (value != null && !value.equals("")) {
            Word word = new Word();
            word.setParentText(text);
            word.setPosition(position);
            word.setValue(value);
            word.setRole(generateRole(value));
            wordRepository.save(word);
        }
    }

    private Role generateRole(final String value) {
        String delimiters = "[-:;.,!?]";
        String valueWithoutPunctuationMark = value.toLowerCase(Locale.ROOT).split(delimiters)[0];


        if (value.charAt(0) == '\"'
                || value.charAt(0) == '\''
                || value.charAt(0) == '«') {
            delimiters = "[\"'«»]";
            valueWithoutPunctuationMark = value.toLowerCase(Locale.ROOT).split(delimiters)[1];
        }

        for (String word : russianLuceneMorphology.getMorphInfo(valueWithoutPunctuationMark)) {
            if (word.split("\\|")[0].equals(valueWithoutPunctuationMark)) {
                return switchRole(word.split(" ")[1]);
            }
        }
        return switchRole(russianLuceneMorphology.getMorphInfo(valueWithoutPunctuationMark).get(0).split(" ")[1]);
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
