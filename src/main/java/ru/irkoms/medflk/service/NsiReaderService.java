package ru.irkoms.medflk.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.File;

import static org.apache.commons.io.IOUtils.copy;

@Log4j2
@Service
@RequiredArgsConstructor
public class NsiReaderService {

    public static <T> T readNsi(Class<T> cls) throws Exception {
        String nsi = cls.getSimpleName().replaceAll("Packet$", "");
        try (ZipFile zf = new ZipFile("nsi/nsi.zip")) {
            final Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryNsiName = entry.getName().replaceAll("\\.[A-Za-z]{3}$", "");
                if (!entryNsiName.equals(nsi)) {
                    continue;
                }

                Unmarshaller unmarshaller = JAXBContext.newInstance(cls).createUnmarshaller();
                try (InputStream is = zf.getInputStream(entry)) {
                    return cls.cast(unmarshaller.unmarshal(is));
                }
            }
        } catch (IOException e) {
            log.error("Error while reading ZIP: {}", e.getMessage());
            throw e;
        } catch (JAXBException | ClassCastException e) {
            log.error("Error while reading XML: {}", e.getMessage());
            throw e;
        }

        throw new Exception(cls.getSimpleName() + " not found!");
    }

    public void downloadNsi() {
        File nsiDir = new File("nsi");
        if (!nsiDir.exists()) {
            nsiDir.mkdir();
        }

        downloadNsi("http://nsi.ffoms.ru/fedPack?type=FULL", "nsi/nsi.zip");
        downloadNsi("http://nsi.ffoms.ru/refbook?type=XML&id=119", "nsi/f002.zip");
        downloadNsi("http://nsi.ffoms.ru/refbook?type=XML&id=216", "nsi/f032.zip");
    }

    private void downloadNsi(String uri, String filename) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri)).GET().build();

        HttpClient client = HttpClient.newHttpClient();
        try (
                FileOutputStream out = new FileOutputStream(filename);
        ) {
            InputStream is = client
                    .sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                    .thenApply(HttpResponse::body).join();
            copy(is, out, 1024);
            log.info("{} updated", filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
