package org.mavriksc.messin.random;

import java.io.IOException;
import java.io.InputStream;

public class OutputEncodedImageBytes {

    public static void main(String[] args) throws IOException {
        InputStream in  =OutputEncodedImageBytes.class.getClassLoader().getResourceAsStream("noimgsmall.jpg");
        byte[] img = new byte[0];
        if (in != null) {
            img = new byte[in.available()];
            int code = in.read(img);
            System.out.println(getHexFormat(img));
        }
    }

    private static String getHexFormat(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("\\x");
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
