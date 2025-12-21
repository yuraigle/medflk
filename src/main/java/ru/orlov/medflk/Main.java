package ru.orlov.medflk;

import javafx.application.Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@RequiredArgsConstructor
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Application.launch(MedflkFxApplication.class, args);
    }

}
