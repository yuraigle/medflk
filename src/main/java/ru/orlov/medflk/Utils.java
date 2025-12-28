package ru.orlov.medflk;

import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.orlov.medflk.jaxb.ZlList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Log4j2
public class Utils {

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

    public static String prettyPrintXml(String xmlString) {
        int indent = 4;

        try {
            InputSource src = new InputSource(new StringReader(xmlString));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "windows-1251");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Writer out = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(out));
            String content = out.toString();
            content = content.replaceAll("\r?\n\\s+\r?\n", "\n");
            return content;
        } catch (Exception e) {
            throw new RuntimeException("Error occurs when pretty-printing xml:\n" + xmlString, e);
        }
    }
}
