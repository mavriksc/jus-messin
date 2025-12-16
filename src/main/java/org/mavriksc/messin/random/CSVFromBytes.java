package org.mavriksc.messin.random;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CSVFromBytes {
    public static void main(String[] ar) throws IOException {
        final String filename = "biggun.csv";

        try(InputStream is = new FileInputStream(filename)) {

            byte[] b1 = new byte[15];
            if (is.read(b1, 0, b1.length) != -1) {
                String s = new String(b1, Charset.defaultCharset());
                String[] split = s.split("\n");
                boolean verify = StringUtils.equalsIgnoreCase(split[0].trim(), "esn");
                System.out.println(verify);
            }
        }

        byte[] b = Files.readAllBytes(Paths.get(filename));

        System.out.println(new String(b,Charset.defaultCharset()));



    }
}
