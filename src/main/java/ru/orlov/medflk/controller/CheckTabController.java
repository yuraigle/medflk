package ru.orlov.medflk.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.MedflkFxApplication;
import ru.orlov.medflk.domain.CheckFact;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.service.StatusService;
import ru.orlov.medflk.task.FileValidatorTask;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static ru.orlov.medflk.Utils.getPluralForm;

@Log4j2
@Controller
@RequiredArgsConstructor
public class CheckTabController implements Initializable {

    public static final ObservableList<CheckFact> checkFactList
            = FXCollections.observableList(new ArrayList<>());
    private final FileValidatorTask fileValidatorTask;
    private final StatusService statusService;
    @FXML
    private TableView<CheckFact> factsTable;

    @FXML
    private TableColumn<String, String> factsTableTest;

    @FXML
    private TableColumn<String, String> factsTableDescription;

    @FXML
    private TableColumn<String, String> factsTableResult;

    @FXML
    private Button btnSelectFile;

    @FXML
    private Label labelFileName;

    @FXML
    private Label labelFileResult;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        factsTableTest.setCellValueFactory(new PropertyValueFactory<>("test"));
        factsTableDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        factsTableResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        factsTable.setItems(checkFactList);
    }

    @FXML
    void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Архив zip", "*.zip"));
        fileChooser.setTitle("Выберите файл для проверки");

        File file = fileChooser.showOpenDialog(MedflkFxApplication.primaryStage);
        if (file != null) {
            Task<FlkP> task = fileValidatorTask
                    .getTaskWithStatus(file, statusService.getStatusProperty());

            task.setOnScheduled(ev -> {
                btnSelectFile.setDisable(true);
                labelFileName.setText(file.getName());
                labelFileResult.setText("");
            });

            task.setOnSucceeded(ev -> onTaskFinished(task));
            task.setOnFailed(ev -> onTaskFinished(task));
            task.setOnCancelled(ev -> onTaskFinished(task));

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void onTaskFinished(Task<FlkP> task) {
        btnSelectFile.setDisable(false);

        try {
            FlkP flkP = task.get();
            int cntErr = flkP.getPrList().size();
            if (cntErr == 0) {
                labelFileResult.setStyle("-fx-text-fill: green");
                labelFileResult.setText("Нет ошибок");
            } else {
                labelFileResult.setStyle("-fx-text-fill: red");
                labelFileResult.setText(cntErr + " " + getPluralForm(cntErr, "ошибка", "ошибки", "ошибок"));
            }
        } catch (Exception ex) {
            labelFileResult.setStyle("-fx-text-fill: red");
            labelFileResult.setText("Ошибка при проверке");
        }
    }
}
