package ru.orlov.medflk.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.task.DirMonitoringTask;
import ru.orlov.medflk.task.DirValidatorTask;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@Controller
@RequiredArgsConstructor
public class MonitorTabController implements Initializable {

    private final DirMonitoringTask dirMonitoringTask;
    private final DirValidatorTask dirValidatorTask;

    @FXML
    private Button btnStartMonitor;

    @FXML
    private Button btnStopMonitor;

    @FXML
    private TextArea txtMonitorLog;

    private Task<Void> task;
    private Task<Void> task2;
    private Thread thread;
    private Thread thread2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStopMonitor.setDisable(true);
    }

    @FXML
    public void startMonitor() {
        btnStartMonitor.setDisable(true);
        btnStopMonitor.setDisable(false);

        task = dirMonitoringTask.getTaskWithStatus(txtMonitorLog.textProperty());
        task2 = dirValidatorTask.getTaskWithStatus(txtMonitorLog.textProperty());

        thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        thread2 = new Thread(task2);
        thread2.setDaemon(true);
        thread2.start();
    }

    @FXML
    public void stopMonitor() {
        btnStartMonitor.setDisable(false);
        btnStopMonitor.setDisable(true);

        task.cancel();
        thread.interrupt();

        task2.cancel();
        thread2.interrupt();
    }

}
