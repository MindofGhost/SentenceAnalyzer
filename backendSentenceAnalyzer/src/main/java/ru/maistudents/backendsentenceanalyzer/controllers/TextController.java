package ru.maistudents.backendsentenceanalyzer.controllers;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import ru.maistudents.backendsentenceanalyzer.entities.Text;
import ru.maistudents.backendsentenceanalyzer.exceptions.FileHasBadContentException;
import ru.maistudents.backendsentenceanalyzer.exceptions.FileIsEmptyException;
import ru.maistudents.backendsentenceanalyzer.exceptions.FileWrongFormatException;
import ru.maistudents.backendsentenceanalyzer.exceptions.InputTextIsEmpty;
import ru.maistudents.backendsentenceanalyzer.services.TextService;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TextController {

    private final TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @CrossOrigin
    @PostMapping(value = "/text", consumes = {MediaType.TEXT_PLAIN_VALUE})
    public Map<String, String> analyzeInputText(@RequestBody String inputString) {
        if (inputString.isEmpty()) {
            throw new InputTextIsEmpty("Input text is empty");
        }

        Text text = setText(inputString);
        textService.analyzeText(text);

        return textService.getOutputText(text);
    }
    @CrossOrigin
    @PostMapping("/file")
    public Map<String, String> analyzeInputFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileIsEmptyException("Input file is empty");
        }
        String inputString = readFile(file);

        Text text = setText(inputString);
        textService.analyzeText(text);

        return textService.getOutputText(text);
    }

    private String readFile(MultipartFile file) {
        if (Objects.requireNonNull(file.getOriginalFilename()).matches(".+txt$")) {
            return readTxtFile(file);
        } else {
            if (Objects.requireNonNull(file.getOriginalFilename()).matches(".+docx$")) {
                return readDocxFile(file);
            } else {
                if (Objects.requireNonNull(file.getOriginalFilename()).matches(".+doc$")) {
                    return readDocFile(file);
                } else {
                    throw new FileWrongFormatException("Input file have a wrong format. Only txt, doc and docx are supported");
                }
            }
        }
    }

    private String readDocFile(MultipartFile file) {
        try {
            HWPFDocument document = new HWPFDocument(file.getInputStream());
            WordExtractor extractor = new WordExtractor(document);
            return  extractor.getText();
        } catch (IOException e) {
            throw new FileHasBadContentException("Bad content");
        }
    }

    private String readTxtFile(MultipartFile file) {
        try {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileHasBadContentException("Bad content");
        }
    }

    private String readDocxFile(MultipartFile file) {
        String inputString = "";
        try {

            XWPFDocument document = new XWPFDocument(file.getInputStream());
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                inputString = inputString.concat(para.getText()).concat(" ");
            }
        } catch (IOException e) {
            throw new FileHasBadContentException("Bad content");
        }
        return inputString;
    }


    private Text setText(String inputString) {
        Text text = new Text();
        text.setText(inputString);
        return text;
    }
}
