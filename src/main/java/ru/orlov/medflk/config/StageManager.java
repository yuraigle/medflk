package ru.orlov.medflk.config;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StageManager {

    @Getter
    private final Stage primaryStage;
    private final FxmlLoader fxmlLoader;

    public void switchScene(String view) {
        Parent rootNode = loadRootNode(view);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("МедКонтроль");

        Image appIcon = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/images/icon.png")));
        primaryStage.getIcons().add(appIcon);

        primaryStage.setScene(new Scene(rootNode));
        primaryStage.show();
    }

    public void switchToNextScene(String view) {
        Parent rootNode = loadRootNode(view);
        primaryStage.getScene().setRoot(rootNode);
        primaryStage.show();
    }

    private Parent loadRootNode(String fxmlPath) {
        Parent rootNode;
        try {
            rootNode = fxmlLoader.load(fxmlPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rootNode;
    }

    public void exit() {
        primaryStage.close();
    }

}
