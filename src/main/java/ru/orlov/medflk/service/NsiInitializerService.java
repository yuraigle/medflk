package ru.orlov.medflk.service;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.domain.NsiRow;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NsiInitializerService {

    private final ApplicationContext ctx;

    public static final SimpleBooleanProperty isNsiReady = new SimpleBooleanProperty(false);

    public static final ObservableList<NsiRow> classifiers
            = FXCollections.observableList(new ArrayList<>());

    public List<String> getAllNsiServices() {
        return Arrays.stream(ctx.getBeanDefinitionNames())
                .filter(name -> name.matches("^.*[a-zA-Z][0-9]{3}Service$"))
                .toList();
    }

    public AbstractNsiService initializeNsiService(String code) {
        Object bean = ctx.getBean(code.toLowerCase() + "Service");

        if (bean instanceof AbstractNsiService nsi) {
            nsi.initPacket();
            return nsi;
        }

        return null;
    }

}
