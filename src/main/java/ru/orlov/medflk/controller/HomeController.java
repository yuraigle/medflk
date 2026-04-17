package ru.orlov.medflk.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.service.StatusService;
import ru.orlov.medflk.task.FileValidatorTask;
import ru.orlov.medflk.task.NsiInitializerTask;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Log4j2
@Controller
@RequiredArgsConstructor
public class HomeController implements Initializable {

    private final StatusService statusService;
    private final NsiInitializerTask nsiInitializerTask;
    private final FileValidatorTask fileValidatorTask;

    @Value("${app.test_file:}")
    private String testFile = null;

    @FXML
    private Label statusLine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusService.setStatusProperty(statusLine.textProperty());

        Task<Void> task = nsiInitializerTask.getTaskWithStatus(statusService.getStatusProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        if (testFile != null && !isBlank(testFile)) {
            task.setOnSucceeded(ev -> {
                File file = Path.of(testFile).toFile();
                Task<FlkP> task2 = fileValidatorTask
                        .getTaskWithStatus(file, statusService.getStatusProperty());

                Thread thread2 = new Thread(task2);
                thread2.setDaemon(true);
                thread2.start();
            });
        }
    }

}
