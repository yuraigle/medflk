package ru.orlov.medflk.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.MedflkFxApplication;
import ru.orlov.medflk.domain.NsiRow;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;
import ru.orlov.medflk.service.FileValidatorService;
import ru.orlov.medflk.service.NsiDownloaderService;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Controller
@RequiredArgsConstructor
public class HomeController implements Initializable {

    private final ApplicationContext ctx;
    private final NsiDownloaderService nsiDownloaderService;
    private final FileValidatorService validator;

    @FXML
    private TextArea vLog;

    @FXML
    private Label statusLine;

    @FXML
    private Button btnSelectFile;

    @FXML
    private Button btnUpdateNsi;

    @FXML
    private TableView<NsiRow> nsiTable;

    @FXML
    private TableColumn<String, String> nsiTableCode;

    @FXML
    private TableColumn<LocalDate, String> nsiTableDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nsiTableCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        nsiTableDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                List<String> nsiServices = Arrays.stream(ctx.getBeanDefinitionNames())
                        .filter(name -> name.matches("^.*[a-zA-Z][0-9]{3}Service$"))
                        .toList();
                int ttl = nsiServices.size();
                AtomicInteger cntReady = new AtomicInteger(0);

                nsiServices.forEach(name -> {
                    updateMessage("Инициализируем справочники " + cntReady.incrementAndGet() + "/" + ttl);
                    Object bean = ctx.getBean(name);
                    if (bean instanceof AbstractNsiService nsi) {
                        nsi.initPacket();

                        String pack = nsi.getClass().getSimpleName().substring(0, 4);
                        nsiTable.getItems().add(new NsiRow(pack, LocalDate.now()));
                    }
                });
                return null;
            }

            @Override
            protected void scheduled() {
                statusLine.textProperty().bind(messageProperty());
                btnSelectFile.setDisable(true);
            }

            @Override
            protected void succeeded() {
                statusLine.textProperty().unbind();
                statusLine.setText("");
                btnSelectFile.setDisable(false);
            }

            @Override
            protected void failed() {
                statusLine.textProperty().unbind();
                statusLine.setText("Ошибка чтения справочников");
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void downloadNsi() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                List<String> packages1 = nsiDownloaderService.getFfomsPackages();
                List<String> packages2 = nsiDownloaderService.getRmzPackages().keySet().stream().toList();
                int ttl = packages1.size() + packages2.size();
                AtomicInteger cntReady = new AtomicInteger(0);

                packages1.forEach(p -> {
                    updateMessage("Скачиваем справочники " + cntReady.incrementAndGet() + "/" + ttl);
                    nsiDownloaderService.updateFfoms(p);
                });

                packages2.forEach(p -> {
                    updateMessage("Скачиваем справочники " + cntReady.incrementAndGet() + "/" + ttl);
                    nsiDownloaderService.updateRmz(p);
                });
                return null;
            }

            @Override
            protected void scheduled() {
                statusLine.textProperty().bind(messageProperty());
                btnUpdateNsi.setDisable(true);
            }

            @Override
            protected void succeeded() {
                statusLine.textProperty().unbind();
                statusLine.setText("");
                btnUpdateNsi.setDisable(false);
                btnSelectFile.setDisable(false);
            }

            @Override
            protected void failed() {
                statusLine.textProperty().unbind();
                statusLine.setText("Ошибка при попытке скачать справочник");
                btnUpdateNsi.setDisable(false);
                btnSelectFile.setDisable(false);
            }
        };

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
                    ValidationResult result = validator.validate(file);
                    vLog.setText(result.toString());
                    return null;
                }

                @Override
                protected void scheduled() {
                    statusLine.textProperty().bind(messageProperty());
                    vLog.setText("");
                    btnSelectFile.setDisable(true);
                }

                @Override
                protected void succeeded() {
                    statusLine.textProperty().unbind();
                    statusLine.setText("");
                    btnSelectFile.setDisable(false);
                }

                @Override
                protected void failed() {
                    statusLine.textProperty().unbind();
                    statusLine.setText("Ошибка при проверке файла");
                    btnSelectFile.setDisable(false);
                }
            };

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
