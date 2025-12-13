package ru.irkoms.medflk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.io.IOUtils.copy;

@Log4j2
@Service
@RequiredArgsConstructor
public class NsiDownloaderService {

    private final HttpClient httpClient;

    public void updateAll() {
        File nsiDir = new File("nsi");
        if (!nsiDir.exists() && !nsiDir.mkdir()) {
            throw new RuntimeException("Не удалось создать каталог " + nsiDir.getAbsolutePath());
        }

        List<String> packets = List.of("F002", "F008", "F010", "F011", "F032", "Q015", "V002",
                "V006", "V008", "V009", "V010", "V012", "V014", "V015", "V016", "V017",
                "V018", "V020", "V021", "V024", "V025", "V026", "V027", "V028"
        );

        for (String packet : packets) {
            downloadFfomsNsi(packet, "nsi/" + packet + ".ZIP");
        }

        downloadRmzNsi("1.2.643.5.1.13.13.11.1070", "nsi/V001.ZIP");
        downloadRmzNsi("1.2.643.5.1.13.13.11.1005", "nsi/M001.ZIP");
    }

    private void downloadFile(String uri, String filename) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri)).GET().build();

        try (
                FileOutputStream out = new FileOutputStream(filename)
        ) {
            InputStream is = httpClient
                    .sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                    .thenApply(HttpResponse::body).join();
            copy(is, out, 1024);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void downloadFfomsNsi(String id, String filename) {
        try {
            String version = getLatestFfomsVersion(id);
            String url = "https://nsi.ffoms.ru/refbook?type=XML&id=-1&version=" + version;
            downloadFile(url, filename);
            log.info("Справочник {} загружен с сайта ФФОМС", filename);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void downloadRmzNsi(String oid, String filename) {
        try {
            String version = getLatestRmzVersion(oid);
            String url = "https://nsi.rosminzdrav.ru/api/dataFiles/" + oid + "_" + version + "_xml.zip";
            downloadFile(url, filename);
            log.info("Справочник {} загружен с сайта РосМинЗдрав", filename);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String getLatestFfomsVersion(String id) throws Exception {
        String url = "https://nsi.ffoms.ru/export?pageId=refbookList" +
                "&containerId=refbookList&size=-1&contentType=csv&columns=d.code";

        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        String body = resp.body();

        if (body != null) {
            for (String line : body.split("\r?\n")) {
                if (line.contains(id)) {
                    return line.split(";")[0];
                }
            }
        }

        throw new Exception("Версия " + id + " не найдена на сайте ФФОМС");
    }

    private String getLatestRmzVersion(String oid) throws Exception {
        String url = "https://nsi.rosminzdrav.ru/api/versions?identifier=" + oid;

        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        String body = resp.body();

        if (body != null) {
            Pattern versionPattern = Pattern.compile("\"version\":\\s*\"(.*?)\"");
            Matcher matcher = versionPattern.matcher(body);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        throw new Exception("Версия " + oid + " не найдена на сайте РосМинЗдрав");
    }
}
