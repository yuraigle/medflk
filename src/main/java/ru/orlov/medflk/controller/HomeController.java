package ru.orlov.medflk.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.MedflkFxApplication;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.service.FileValidatorService;
import ru.orlov.medflk.task.NsiInitializerTask;
import ru.orlov.medflk.service.StatusService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@Controller
@RequiredArgsConstructor
public class HomeController implements Initializable {

    private final NsiInitializerTask nsiInitializerTask;
    private final FileValidatorService validatorService;
    private final StatusService statusService;

    @FXML
    private Label statusLine;

    @FXML
    private TextArea vLog;

    @FXML
    private Button btnSelectFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusService.setStatusProperty(statusLine.textProperty());

        Task<Void> task = nsiInitializerTask.getTaskWithStatus(statusService.getStatusProperty());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Архив zip", "*.zip"));
        fileChooser.setTitle("Выберите файл для проверки");

        File file = fileChooser.showOpenDialog(MedflkFxApplication.primaryStage);
        if (file != null) {
            Task<ValidationResult> task = new Task<>() {
                @Override
                protected ValidationResult call() {
                    updateMessage("Проверяем файл " + file.getName());
                    ValidationResult result = validatorService.validate(file);
                    vLog.setText(result.toString());
                    return null;
                }

                @Override
                protected void scheduled() {
                    statusService.getStatusProperty().bind(messageProperty());
                    vLog.setText("");
                    btnSelectFile.setDisable(true);
                }

                @Override
                protected void succeeded() {
                    statusService.getStatusProperty().unbind();
                    statusService.getStatusProperty().set("");
                    btnSelectFile.setDisable(false);
                }

                @Override
                protected void failed() {
                    statusService.getStatusProperty().unbind();
                    statusService.getStatusProperty().set("Ошибка при проверке файла");
                    btnSelectFile.setDisable(false);
                }
            };

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
