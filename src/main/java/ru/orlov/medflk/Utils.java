package ru.orlov.medflk;

import lombok.extern.log4j.Log4j2;
import ru.orlov.medflk.jaxb.ZlList;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Log4j2
public class Utils {

    public static final DateTimeFormatter fmtDtRus = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm:ss");

    public static final Pattern rxFile = Pattern
            .compile("^([CHT]|D[A-Z])[MST][0-9]{2,6}[MST][0-9]{2,6}_[0-9]{4}.*$");

    public static String pluralForm(Integer n, String s1, String s2, String s0) {
        if (n % 10 == 1 && n % 100 != 11) return s1; // 1 ошибка
        if (n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20)) return s2; // 2 ошибки
        return s0; // 0 ошибок
    }

    public static String pluralErr(Integer n) {
        return n + " " + pluralForm(n, "ошибка", "ошибки", "ошибок");
    }

    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> rawCollection) {
        List<T> result = new ArrayList<>(rawCollection.size());
        for (Object o : rawCollection) {
            try {
                result.add(clazz.cast(o));
            } catch (ClassCastException e) {
                log.error("Error while casting: {}", e.getMessage());
            }
        }
        return result;
    }

    public static String getZlListMdType(ZlList zlList) {
        String filename = zlList.getZglv().getFilename(); // CHTX
        if (filename == null || filename.isBlank()) {
            return "H";
        }
        if (filename.toUpperCase().startsWith("D")) {
            return "X";
        }
        return filename.toUpperCase().substring(0, 1);
    }


    public static void waitForFileUnlock(File file, long timeoutMillis) throws IOException {
        long startTime = System.currentTimeMillis();

        while (true) {
            try (
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    FileChannel channel = raf.getChannel()
            ) {
                FileLock lock = channel.tryLock();
                if (lock != null) {
                    try {
                        return; // unlocked
                    } finally {
                        lock.release();
                    }
                }
            } catch (IOException | OverlappingFileLockException ignore) {
            }

            if (System.currentTimeMillis() - startTime > timeoutMillis) {
                throw new IOException("Файл заблокирован: " + file.getName());
            }
        }
    }
}
