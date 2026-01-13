package ru.orlov.medflk.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.MedflkFxApplication;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.service.StatusService;
import ru.orlov.medflk.task.FileValidatorTask;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@Controller
@RequiredArgsConstructor
public class CheckTabController implements Initializable {

    private final FileValidatorTask fileValidatorTask;
    private final StatusService statusService;

    @FXML
    private TextArea vLog;

    @FXML
    private Button btnSelectFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Архив zip", "*.zip"));
        fileChooser.setTitle("Выберите файл для проверки");

        File file = fileChooser.showOpenDialog(MedflkFxApplication.primaryStage);
        if (file != null) {
            Task<ValidationResult> task = fileValidatorTask
                    .getTaskWithStatus(file, statusService.getStatusProperty());

            task.setOnScheduled(ev -> {
                vLog.setText("");
                btnSelectFile.setDisable(true);
            });

            task.setOnSucceeded(ev -> {
                try {
                    vLog.setText(task.get().toString());
                } catch (Exception e) {
                    log.error(e);
                }
                btnSelectFile.setDisable(false);
            });

            task.setOnFailed(ev -> btnSelectFile.setDisable(true));
            task.setOnCancelled(ev -> btnSelectFile.setDisable(true));

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
