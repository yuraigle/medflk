package ru.orlov.medflk.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.service.StatusService;
import ru.orlov.medflk.task.NsiInitializerTask;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@Controller
@RequiredArgsConstructor
public class HomeController implements Initializable {

    private final StatusService statusService;
    private final NsiInitializerTask nsiInitializerTask;

    @FXML
    private Label statusLine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusService.setStatusProperty(statusLine.textProperty());

        Task<Void> task = nsiInitializerTask.getTaskWithStatus(statusService.getStatusProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
