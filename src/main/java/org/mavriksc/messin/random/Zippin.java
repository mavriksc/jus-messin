package org.mavriksc.messin.random;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zippin {
    public static void main(String[] args) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("response.json.zip");
             FileInputStream file = new FileInputStream(ClassLoader.getSystemResource("response.json").getFile());
             ZipOutputStream zipOutputStream = new ZipOutputStream(fos)) {
            ZipEntry zipEntry = new ZipEntry("response.json");
            zipOutputStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = file.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
        }

    }


}
