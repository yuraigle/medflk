package ru.orlov.medflk.service;

import lombok.Getter;
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
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.io.IOUtils.copy;

@Log4j2
@Service
@RequiredArgsConstructor
public class NsiDownloaderService {

    private final HttpClient httpClient;

    @Getter
    private final Map<String, String> rmzPackages = Map.of(
            "V001", "1.2.643.5.1.13.13.11.1070",
            "M001", "1.2.643.5.1.13.13.11.1005",
            "M002", "1.2.643.5.1.13.13.99.2.734"
    );

    public void updateFfoms(String packet) {
        File nsiDir = new File("nsi");
        if (!nsiDir.exists() && !nsiDir.mkdir()) {
            throw new RuntimeException("Не удалось создать каталог " + nsiDir.getAbsolutePath());
        }

        downloadFfomsNsi(packet, Path.of(nsiDir.toString(), packet + ".ZIP").toFile());
    }

    public void updateRmz(String packet) {
        File nsiDir = new File("nsi");
        if (!nsiDir.exists() && !nsiDir.mkdir()) {
            throw new RuntimeException("Не удалось создать каталог " + nsiDir.getAbsolutePath());
        }

        downloadRmzNsi(rmzPackages.get(packet), Path.of(nsiDir.toString(), packet + ".ZIP").toFile());
    }

    private void downloadFile(String uri, File saveAs) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri)).GET().build();

        try (
                FileOutputStream out = new FileOutputStream(saveAs)
        ) {
            InputStream is = httpClient
                    .sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                    .thenApply(HttpResponse::body).join();
            copy(is, out, 1024);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void downloadFfomsNsi(String id, File saveAs) {
        try {
            String version = getLatestFfomsVersion(id);
            String url = "https://nsi.ffoms.ru/refbook?type=XML&id=-1&version=" + version;
            downloadFile(url, saveAs);
            log.info("Справочник {} загружен с сайта ФФОМС", saveAs.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void downloadRmzNsi(String oid, File saveAs) {
        try {
            String version = getLatestRmzVersion(oid);
            String url = "https://nsi.rosminzdrav.ru/api/dataFiles/%s_%s_xml.zip"
                    .formatted(oid, version);
            downloadFile(url, saveAs);
            log.info("Справочник {} загружен с сайта РосМинЗдрав", saveAs.getName());
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
