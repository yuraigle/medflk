package ru.orlov.medflk;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ru.orlov.medflk.config.StageManager;

@Log4j2
public class MedflkFxApplication extends Application {

    private static Stage stage;
    private ConfigurableApplicationContext ctx;

    @Override
    public void init() {
        ctx = new SpringApplicationBuilder(Main.class).run();
    }

    @Override
    public void stop() {
        ctx.close();
        stage.close();
    }

    @Override
    public void start(Stage stage) {
        MedflkFxApplication.stage = stage;
        StageManager stageManager = ctx.getBean(StageManager.class, stage);
        stageManager.switchScene("/fxml/home.fxml");
    }

}
