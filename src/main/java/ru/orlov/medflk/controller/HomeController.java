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

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@Controller
@RequiredArgsConstructor
public class HomeController implements Initializable {

    private final TaskInitNsi taskInitNsi;
    private final TaskDownloadNsi taskDownloadNsi;
    private final TaskValidateFile taskValidateFile;

    @FXML
    private TextArea vLog;

    @FXML
    private Label statusLine;

    @FXML
    private Button btnSelectFile;

    @FXML
    private Button btnUpdateNsi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runInBackground(taskInitNsi);
    }

    @FXML
    void downloadNsi() {
        runInBackground(taskDownloadNsi);
    }

    @FXML
    void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Архив zip", "*.zip"));
        fileChooser.setTitle("Выберите файл для проверки");

        File file = fileChooser.showOpenDialog(MedflkFxApplication.primaryStage);
        if (file != null) {
            vLog.setText("");
            btnSelectFile.setDisable(true);
            statusLine.setText("Проверяем файл " + file.getName());

            taskValidateFile.setFile(file);
            taskValidateFile.setOnSucceeded(ev -> {
                ValidationResult res = (ValidationResult) ev.getSource().getValue();
                vLog.setText(res.toString());
                btnSelectFile.setDisable(false);
                statusLine.setText("");
                taskValidateFile.setFile(null);
                taskValidateFile.setOnSucceeded(null);
            });

            Thread thread = new Thread(taskValidateFile);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void runInBackground(Task<Void> task) {
        btnUpdateNsi.setDisable(true);
        btnSelectFile.setDisable(true);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnScheduled(ev ->
                statusLine.textProperty().bind(task.messageProperty()));
        task.setOnFailed(ev ->
                statusLine.textProperty().unbind());
        task.setOnSucceeded(ev -> {
            statusLine.textProperty().unbind();
            btnUpdateNsi.setDisable(false);
            btnSelectFile.setDisable(false);
        });
    }

}
