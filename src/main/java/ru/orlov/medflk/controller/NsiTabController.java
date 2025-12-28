package ru.orlov.medflk.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.domain.NsiRow;
import ru.orlov.medflk.service.NsiInitializerService;
import ru.orlov.medflk.service.StatusService;
import ru.orlov.medflk.task.NsiDownloaderTask;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Log4j2
@Controller
@RequiredArgsConstructor
public class NsiTabController implements Initializable {

    private final NsiDownloaderTask nsiDownloaderTask;
    private final StatusService statusService;

    @FXML
    private Button btnUpdateNsi;

    @FXML
    private TableView<NsiRow> nsiTable;

    @FXML
    private TableColumn<String, String> nsiTableCode;

    @FXML
    private TableColumn<LocalDate, String> nsiTableDate;

    @FXML
    private TableColumn<String, String> nsiTableVersion;

    @FXML
    private TableColumn<String, String> nsiTableDescription;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nsiTableCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        nsiTableDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        nsiTableVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
        nsiTableDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        Platform.runLater(() -> nsiTable.setItems(NsiInitializerService.classifiers));
    }

    @FXML
    void downloadNsi() {
        Task<Void> task = nsiDownloaderTask.getTaskWithStatus(statusService.getStatusProperty());

        task.setOnScheduled(ev -> btnUpdateNsi.setDisable(true));
        task.setOnFailed(ev -> btnUpdateNsi.setDisable(false));
        task.setOnCancelled(ev -> btnUpdateNsi.setDisable(false));
        task.setOnSucceeded(ev -> btnUpdateNsi.setDisable(false));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
