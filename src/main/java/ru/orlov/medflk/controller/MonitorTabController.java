package ru.orlov.medflk.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.task.DirMonitoringTask;
import ru.orlov.medflk.task.DirValidatorTask;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static ru.orlov.medflk.Utils.fmtDtRus;
import static ru.orlov.medflk.Utils.pluralForm;

@Log4j2
@Controller
@RequiredArgsConstructor
public class MonitorTabController implements Initializable {

    @FXML
    private TextField txtDirIn;

    @FXML
    private TextField txtDirOk;

    @FXML
    private TextField txtDirFlk;

    @FXML
    private Button btnStartMonitor;

    @FXML
    private Button btnStopMonitor;

    @FXML
    private Label lblQueueSize;

    @FXML
    private TextArea txtMonitorLog;

    private final DirMonitoringTask dirMonitoringTask;
    private final DirValidatorTask dirValidatorTask;
    private Task<Void> task;
    private Task<Void> task2;
    private Thread thread;
    private Thread thread2;

    @Value("${app.dir_in}")
    private String dirIn;

    @Value("${app.dir_ok}")
    private String dirOk;

    @Value("${app.dir_flk}")
    private String dirFlk;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStopMonitor.setDisable(true);
        txtMonitorLog.setEditable(false);

        txtDirIn.setText(dirIn);
        txtDirOk.setText(dirOk);
        txtDirFlk.setText(dirFlk);

        DirMonitoringTask.queue.addListener((obs, v1, v2) ->
                Platform.runLater(() -> {
                    String s = pluralForm(v2.size(), "файл", "файла", "файлов");
                    lblQueueSize.setText(v2.size() + " " + s + " в очереди");
                })
        );
    }

    @FXML
    public void startMonitor() {
        btnStartMonitor.setDisable(true);
        btnStopMonitor.setDisable(false);
        appendLogLine("Запущен мониторинг папки " + txtDirIn.getText());

        task = dirMonitoringTask.getTask(txtDirIn.getText());
        task2 = dirValidatorTask.getTask(txtDirOk.getText(), txtDirFlk.getText());
        task2.messageProperty().addListener((obs, v1, v2) ->
                appendLogLine(v2)
        );

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
        appendLogLine("Мониторинг остановлен");

        task.cancel();
        thread.interrupt();

        task2.cancel();
        thread2.interrupt();
    }

    @FXML
    public void clearLog() {
        txtMonitorLog.clear();
    }

    private void appendLogLine(String line) {
        txtMonitorLog.appendText(LocalDateTime.now().format(fmtDtRus) + " " + line + "\n");
    }
}
