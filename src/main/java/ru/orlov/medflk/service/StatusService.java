package ru.orlov.medflk.service;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    @Getter
    @Setter
    private StringProperty statusProperty = new SimpleStringProperty();

    public static void showStatusMessage(StringProperty status, String message) {
        status.set(message);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(ev -> {
            if (!status.isBound()) {
                status.set("");
            }
        });
        pause.play();
    }
}
