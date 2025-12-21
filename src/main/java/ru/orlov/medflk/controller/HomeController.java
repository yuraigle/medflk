package ru.orlov.medflk.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import ru.orlov.medflk.config.StageManager;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;
import ru.orlov.medflk.service.FileValidatorService;
import ru.orlov.medflk.service.NsiDownloaderService;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Log4j2
@Controller
public class HomeController implements Initializable {

    private final StageManager stageManager;
    private final ApplicationContext ctx;
    private final FileValidatorService validator;
    private final NsiDownloaderService nsiDownloaderService;

    @FXML
    private TextArea vLog;

    @Lazy
    public HomeController(
            StageManager stageManager,
            FileValidatorService validator,
            NsiDownloaderService nsiDownloaderService,
            ApplicationContext ctx
    ) {
        this.stageManager = stageManager;
        this.ctx = ctx;
        this.validator = validator;
        this.nsiDownloaderService = nsiDownloaderService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initNsiPackets();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            procLog.debug();
            vLog.setText(procLog.toString());
        }
    }

    @FXML
    void updateNsi() {
        nsiDownloaderService.updateAll();
        initNsiPackets();
    }

    private void initNsiPackets() {
        Arrays.stream(ctx.getBeanDefinitionNames())
                .filter(name -> name.matches("^.*[a-zA-Z][0-9]{3}Service$"))
                .forEach(name -> {
                    Object bean = ctx.getBean(name);
                    if (bean instanceof AbstractNsiService) {
                        ((AbstractNsiService) bean).initPacket();
                    }
                });
        log.info("Nsi packets initialized");
    }
}
