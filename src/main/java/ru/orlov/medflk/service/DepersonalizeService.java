package ru.orlov.medflk.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.lang3.StringUtils.substringBetween;
import static ru.orlov.medflk.Utils.prettyPrintXml;

/**
 * Создаём деперсонализированные файлы реестров на базе реальных
 * Они нужны для тестов, отладки и демонстрационных целей
 */
@Log4j2
@Service
public class DepersonalizeService {

    public void depersonalizeDir(String dir) {
        try (Stream<Path> files = Files.walk(Path.of(dir))) {
            files
                    .filter(Files::isRegularFile)
                    .forEach(p -> depersonalize(p.toAbsolutePath().toString()));
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void depersonalize(String filename) {
        String dir = "samples";
        Path outPath = (new File(dir)).toPath();
        if (!outPath.toFile().exists() && !outPath.toFile().mkdirs()) {
            throw new RuntimeException();
        }

        String num = String.valueOf(Math.round(Math.random() * 100));

        String newNameM = Path.of(filename).toFile().getName()
                                  .toUpperCase().replaceAll("^.*/", "")
                                  .replaceAll("_(\\d{4}).*", "_$1") + num;

        String newNameL; // HM -> LM, DVM -> LVM, TM -> LCM, CM -> LCM
        if (newNameM.startsWith("C") || newNameM.startsWith("T")) {
            newNameL = "L" + newNameM;
        } else {
            newNameL = newNameM.replaceFirst(".", "L");
        }

        String outZipName = Path.of(outPath.toString(), newNameM + ".ZIP").toString();
        try (
                ZipFile zf = new ZipFile(filename);
                FileOutputStream fos = new FileOutputStream(outZipName);
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName().toUpperCase();

                try (InputStream is = zf.getInputStream(entry)) {
                    String contents = new String(is.readAllBytes(), Charset.forName("CP1251"));
                    String newContents = prettyPrintXml(contents);

                    if (name.startsWith("L")) {
                        newContents = newContents.replaceAll("<FILENAME>.*</FILENAME>",
                                "<FILENAME>" + newNameL + "</FILENAME>");
                        newContents = newContents.replaceAll("<FILENAME1>.*</FILENAME1>",
                                "<FILENAME1>" + newNameM + "</FILENAME1>");
                        newContents = replacePersonalizedTagsL(newContents);
                        zos.putNextEntry(new ZipEntry(newNameL + ".XML"));
                    } else {
                        newContents = newContents.replaceAll("<FILENAME>.*</FILENAME>",
                                "<FILENAME>" + newNameM + "</FILENAME>");
                        newContents = replacePersonalizedTagsM(newContents);
                        zos.putNextEntry(new ZipEntry(newNameM + ".XML"));
                    }

                    zos.write(newContents.getBytes(Charset.forName("CP1251")));
                }
            }

            log.info("Created file {}", outZipName);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private String replacePersonalizedTagsM(String xml) {
        StringBuilder sb = new StringBuilder();

        for (String part : xml.split("<ZAP>")) {
            if (sb.isEmpty()) {
                sb.append(part);
                continue;
            }

            // полис нового образца, случайный номер, серии нет
            String enp = "38" + randomDigitsOfLength(14);
            part = part.replaceAll("<ENP>.*?</ENP>", "<ENP>" + enp + "</ENP>");
            part = part.replaceAll("<VPOLIS>.*?</VPOLIS>", "<VPOLIS>3</VPOLIS>");
            part = part.replaceAll("<SPOLIS>.*?</SPOLIS>", "");
            part = part.replaceAll("<NPOLIS>.*?</NPOLIS>", "<NPOLIS>" + enp + "</NPOLIS>");

            // затираем данные социального статуса пациента
            part = part.replaceAll("<SOC>.*?</SOC>", "<SOC>000</SOC>");

            // и номер карты
            part = part.replaceAll("<NHISTORY>.*?</NHISTORY>", "<NHISTORY>" + randomDigitsOfLength(6) + "</NHISTORY>");

            // случайный снилс врача и медработника
            part = part.replaceAll("<IDDOKT>.*?</IDDOKT>", "<IDDOKT>" + randomDigitsOfLength(11) + "</IDDOKT>");
            part = part.replaceAll("<CODE_MD>.*?</CODE_MD>", "<CODE_MD>" + randomDigitsOfLength(11) + "</CODE_MD>");

            sb.append("<ZAP>").append(part);
        }

        return sb.toString();
    }

    private String replacePersonalizedTagsL(String xml) {
        StringBuilder sb = new StringBuilder();

        for (String part : xml.split("<PERS>")) {
            if (sb.isEmpty()) {
                sb.append(part);
                continue;
            }

            String sex = substringBetween(part, "<W>", "</W>");
            // пол оставляем без изменений, ФИО заменяем
            if (sex.equals("2")) {
                part = part.replaceAll("<FAM>.*?</FAM>", "<FAM>ТЕСТОВА</FAM>");
                part = part.replaceAll("<IM>.*?</IM>", "<IM>МАРИЯ</IM>");
                part = part.replaceAll("<OT>.*?</OT>", "<OT>ИВАНОВНА</OT>");
            } else {
                part = part.replaceAll("<FAM>.*?</FAM>", "<FAM>ТЕСТОВ</FAM>");
                part = part.replaceAll("<IM>.*?</IM>", "<IM>ИВАН</IM>");
                part = part.replaceAll("<OT>.*?</OT>", "<OT>ИВАНОВИЧ</OT>");
            }

            // случайный день рождения, год не меняем
            String yBirth = substringBetween(part, "<DR>", "-");
            if (yBirth != null) {
                String dr = randomDateOfYear(Integer.parseInt(yBirth));
                part = part.replaceAll("<DR>.*?</DR>",
                        "<DR>" + dr + "</DR>");
            }

            // случайный СНИЛС
            String randomSnils = randomDigitsOfLength(11);
            part = part.replaceAll("<SNILS>.*?</SNILS>", "<SNILS>" + randomSnils + "</SNILS>");

            // случайный документ
            String docSer = substringBetween(part, "<DOCSER>", "</DOCSER>");
            if (docSer != null) {
                StringBuilder newDocSer = new StringBuilder();
                Random rand = new Random();
                for (int i = 0; i < docSer.length(); i++) {
                    String c1 = docSer.substring(i, i + 1).toUpperCase();

                    if (c1.matches("\\d")) {
                        c1 = String.valueOf(rand.nextInt(10));
                    }
                    if (c1.matches("[А-ЯЁ]")) {
                        c1 = randomCyrillicLetter().toUpperCase();
                    }
                    // латинские буквы меняем, римские цифры оставляем как есть
                    if (c1.matches("[A-Z]") && !c1.matches("[IVLC]")) {
                        c1 = randomLatinLetter().toUpperCase();
                    }

                    newDocSer.append(c1);
                }
                part = part.replaceAll("<DOCSER>.*?</DOCSER>",
                        "<DOCSER>" + newDocSer + "</DOCSER>");
            }

            String docNum = substringBetween(part, "<DOCNUM>", "</DOCNUM>");
            if (docNum != null) {
                String newDocNum = randomDigitsOfLength(docNum.length());
                part = part.replaceAll("<DOCNUM>.*?</DOCNUM>", "<DOCNUM>" + newDocNum + "</DOCNUM>");
            }

            part = part.replaceAll("<DOCORG>.*?</DOCORG>", "<DOCORG>Тестовый отдел МВД</DOCORG>");

            // ОКАТО г. Иркутск
            part = part.replaceAll("<OKATOG>.*?</OKATOG>", "<OKATOG>25401000000</OKATOG>");
            part = part.replaceAll("<OKATOP>.*?</OKATOP>", "<OKATOP>25401000000</OKATOP>");

            String yDoc = substringBetween(part, "<DOCDATE>", "-");
            if (yDoc != null) {
                String docDate = randomDateOfYear(Integer.parseInt(yDoc));
                part = part.replaceAll("<DOCDATE>.*?</DOCDATE>",
                        "<DOCDATE>" + docDate + "</DOCDATE>");
            }

            sb.append("<PERS>").append(part);
        }

        return sb.toString();
    }

    private String randomDateOfYear(int yr) {
        long minDay = LocalDate.of(yr, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(yr, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        if (randomDate.isAfter(LocalDate.now())) {
            randomDate = LocalDate.now().minusDays(1);
        }

        return randomDate.format(DateTimeFormatter.ISO_DATE);
    }

    private String randomDigitsOfLength(int length) {
        Random rand = new Random();
        StringBuilder newDocNum = new StringBuilder();

        for (int i = 0; i < length; i++) {
            newDocNum.append(rand.nextInt(10));
        }

        return newDocNum.toString();
    }

    private String randomCyrillicLetter() {
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        Random random = new Random();

        int index = random.nextInt(alphabet.length());
        return String.valueOf(alphabet.charAt(index));
    }

    private String randomLatinLetter() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();

        int index = random.nextInt(alphabet.length());
        return String.valueOf(alphabet.charAt(index));
    }
}
