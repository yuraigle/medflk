package ru.irkoms.medflk.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.irkoms.medflk.jaxb.*;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.apache.commons.lang3.StringUtils.substring;

@Log4j2
@Service
public class RegistryReaderService {

    public AZlList parseZlList(File zip) throws Exception {
        try {
            String type = guessFileType(zip.getName());
            return switch (type) {
                case "H" -> parseList(zip, ZlListH.class);
                case "C" -> parseList(zip, ZlListC.class);
                case "T" -> parseList(zip, ZlListT.class);
                case "X" -> parseList(zip, ZlListX.class);
                default -> null;
            };
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new Exception("XML/ZIP не читается");
        }
    }

    public APersList parsePersList(File zip) throws Exception {
        try {
            return parseList(zip, PersList.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new Exception("XML/ZIP не читается");
        }
    }

    private <T> T parseList(File zip, Class<T> cls) throws Exception {
        try (ZipFile zf = new ZipFile(zip)) {
            Unmarshaller unmarshaller = JAXBContext.newInstance(cls).createUnmarshaller();
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName().toUpperCase();
                String clsName = cls.getSimpleName();

                try (InputStream is = zf.getInputStream(entry)) {
                    if (clsName.startsWith("ZlList") && name.matches("^[CHTD].*\\.XML$")) {
                        return cls.cast(unmarshaller.unmarshal(is));
                    } else if (clsName.startsWith("PersList") && name.matches("^L.*\\.XML$")) {
                        return cls.cast(unmarshaller.unmarshal(is));
                    }
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

    public static String guessFileType(String filename) throws Exception {
        // наша региональщина
        Pattern pattern1 = Pattern.compile("^HM\\d{5}[HPS]\\d{6}\\w{1,8}\\.ZIP$");

        // федеральный стандарт именования
        Pattern pattern2 = Pattern.compile("^[HCT]M\\d{6}[ST]\\d{2,5}_\\d+\\.ZIP$");
        Pattern pattern3 = Pattern.compile("^D[A-Z]M\\d{6}[ST]\\d{2,5}_\\d+\\.ZIP$");

        String fn = filename.toUpperCase();
        if (pattern1.matcher(fn).matches()) {
            String priz = substring(fn, 8, 10);
            return switch (priz) { // наша региональщина
                case "41", "44", "51" -> "C";
                case "40" -> "T";
                case "16", "33", "34", "48", "49" -> "X";
                default -> "H";
            };
        } else if (pattern2.matcher(fn).matches()) {
            return filename.toUpperCase().substring(0, 1);
        } else if (pattern3.matcher(fn).matches()) {
            return "X";
        } else {
            throw new Exception("Неожиданное имя файла: " + fn);
        }
    }

}
