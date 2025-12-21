package ru.orlov.medflk.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.config.StageManager;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.service.FileValidatorService;
import ru.orlov.medflk.service.NsiDownloaderService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@Controller
public class HomeController implements Initializable {

    private final ApplicationContext ctx;
    private final StageManager stageManager;
    private final FileValidatorService validator;
    private final NsiDownloaderService nsiDownloaderService;

    @FXML
    private TextArea vLog;

    @FXML
    private Label statusLine;

    @FXML
    private Button btnSelectFile;

    @FXML
    private Button btnUpdateNsi;

    @Lazy
    public HomeController(
            StageManager stageManager,
            ApplicationContext ctx,
            FileValidatorService validator,
            NsiDownloaderService nsiDownloaderService
    ) {
        this.ctx = ctx;
        this.stageManager = stageManager;
        this.validator = validator;
        this.nsiDownloaderService = nsiDownloaderService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TaskInitNsi task = new TaskInitNsi(ctx);
        runInBackground(task);
    }

    @FXML
    void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Архив zip", "*.zip"));
        fileChooser.setTitle("Выберите файл для проверки");

        File file = fileChooser.showOpenDialog(stageManager.getPrimaryStage());
        if (file != null) {
            vLog.setText("");
            log.info("Selected file: {}", file.getAbsolutePath());
            ValidationResult procLog = validator.validate(file);
            vLog.setText(procLog.toString());
        }
    }

    @FXML
    void updateNsi() {
        TaskDownloadNsi task = new TaskDownloadNsi(nsiDownloaderService);
        runInBackground(task);
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
