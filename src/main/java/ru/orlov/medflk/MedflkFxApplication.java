package ru.orlov.medflk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

@Log4j2
public class MedflkFxApplication extends Application {

    public static Stage primaryStage;
    private ConfigurableApplicationContext ctx;

    @Override
    public void init() {
        ctx = new SpringApplicationBuilder(Main.class).run();
    }

    @Override
    public void stop() {
        ctx.close();
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        Image appIcon = new Image(requireNonNull(
                getClass().getResourceAsStream("/images/icon.png")));
        primaryStage.getIcons().add(appIcon);
        primaryStage.setTitle("МедКонтроль");

        Parent rootNode = loadRootNode();
        primaryStage.setScene(new Scene(rootNode));
        primaryStage.show();
    }

    private Parent loadRootNode() {
        Parent rootNode;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(ctx::getBean);
            loader.setClassLoader(ctx.getClassLoader());
            loader.setLocation(getClass().getResource("/fxml/root.fxml"));
            rootNode = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rootNode;
    }

}
