package ru.orlov.medflk.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.orlov.medflk.jaxb.FlkErr;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationResult {

    private String filename;
    private Long filesize;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private List<Line> lines = new ArrayList<>();
    private List<FlkErr> errors = new ArrayList<>();
    private Boolean declined = false;

    public ValidationResult(File file) {
        this.filename = file.getName();
        this.filesize = file.length();
        this.startedAt = LocalDateTime.now();
    }

    public void addLine(String comment) {
        lines.add(new Line(null, comment));
    }

    public void addError(String comment) {
        lines.add(new Line(null, comment));
        declined = true;
    }

    public void addFlkError(FlkErr err) {
        errors.add(err);
        declined = true;
    }

    public void debug() {
        System.out.printf("%n%s Проверка файла %s (%sKb)%n",
                startedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                filename, filesize / 1024);

        for (Line line : lines) {
            if ("OK".equals(line.getComment())) {
                continue;
            }

            if (line.getIdTest() != null) {
                System.out.printf("%s : ", line.getIdTest());
            }
            System.out.printf("%s%n", line.getComment());
        }

        System.out.printf("%s Проверка окончена за %sс. %s%n",
                endedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                ChronoUnit.SECONDS.between(startedAt, endedAt),
                declined ? "Файл отклонён" : "OK"
        );
    }

    @Data
    @AllArgsConstructor
    public static class Line {
        private String idTest;
        private String comment;
    }
}
