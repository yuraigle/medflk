package ru.orlov.medflk.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    @Getter
    @Setter
    private StringProperty statusProperty = new SimpleStringProperty();

}
