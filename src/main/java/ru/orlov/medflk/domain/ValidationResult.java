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
        endedAt = LocalDateTime.now();
    }

    public void addFlkError(FlkErr err) {
        errors.add(err);
        declined = true;
        endedAt = LocalDateTime.now();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s Проверка файла %s (%sKb)%n%n",
                startedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                filename, filesize / 1024));

        for (Line line : lines) {
            if ("OK".equals(line.getComment())) {
//                continue;
            }

            if (line.getIdTest() != null) {
                sb.append(String.format("%s : ", line.getIdTest()));
            }
            sb.append(String.format("%s%n", line.getComment()));
        }

        sb.append(String.format("%n%s Проверка окончена за %sс. %s%n",
                endedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                ChronoUnit.SECONDS.between(startedAt, endedAt),
                declined ? "Файл отклонён" : "OK"
        ));

        return sb.toString();
    }

    public void debug() {
        System.out.println();
        System.out.println(this);
        System.out.println();
    }

    @Data
    @AllArgsConstructor
    public static class Line {
        private String idTest;
        private String comment;
    }
}
