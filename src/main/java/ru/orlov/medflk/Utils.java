package ru.orlov.medflk;

import lombok.extern.log4j.Log4j2;
import ru.orlov.medflk.jaxb.OnkSl;
import ru.orlov.medflk.jaxb.OnkUsl;
import ru.orlov.medflk.jaxb.ZlList;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static boolean dsIsOnkoC00ToD10OrD45ToD48(String ds1) {
        if (ds1 == null) return false;

        ds1 = ds1.toUpperCase();

        boolean ds1Usl1 = ds1.compareTo("C00.0") >= 0 && ds1.compareTo("D10") < 0;
        boolean ds1Usl2 = ds1.compareTo("D45") >= 0 && ds1.compareTo("D48") < 0;
        return ds1Usl1 || ds1Usl2;
    }

    public static boolean dsIsOnkoDetTill21(String ds1) {
        String[] ds1List = """
                        C40, C49, C62, C64, C70, C71, C72, C81, C95, C22.2, C38.1, C47.3, C47.4,
                        C47.5, C47.6, C47.8, C47.9, C48.0, C74.1, C74.9, C76.0, C76.1, C76.2, C76.3,
                        C76.7, C76.8, C83.3, C83.5, C83.7, C84.6, C84.7, C85.2, C91.0, C91.8, C92.0,
                        C92.3, C92.4, C92.5, C92.6, C92.7, C92.8, C92.9, C93.0, C94.0, C94.2
                """.trim().split("[, \n]+");

        return ds1 != null && Arrays.stream(ds1List).anyMatch(ds1::startsWith);
    }

    public static boolean onkSlHasRegNum(OnkSl onkSl, String regnum) {
        if (onkSl == null || onkSl.getOnkUslList() == null) return false;

        for (OnkUsl onkUsl : onkSl.getOnkUslList()) {
            if (onkUsl.getLekPrList() == null) continue;

            boolean hasRegNum = onkUsl.getLekPrList().stream()
                    .anyMatch(lp -> lp.getRegnum().equals(regnum));

            if (hasRegNum) {
                return true;
            }
        }

        return false;
    }

    public static void waitForFileUnlock(File file, long timeoutMillis) throws IOException, InterruptedException {
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

            Thread.sleep(500);
        }
    }
}
