package ru.orlov.medflk.config;

import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final FxmlLoader fxmlLoader;

    @Bean
    @Lazy
    public StageManager stageManager(Stage stage) {
        return new StageManager(stage, fxmlLoader);
    }

    @Bean
    public Stage stage() {
        return null;
    }

}
