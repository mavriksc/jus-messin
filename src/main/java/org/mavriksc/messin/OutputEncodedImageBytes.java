package org.mavriksc.messin;

import java.io.ByteArrayInputStream;
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
            //System.out.println(getEscapeFormat(img));
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

    private static String getEscapeFormat(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int i = b & 0xFF;
            if (i == 0) {
                sb.append("\\000");
            } else if (i<=31||i>=127) {
                sb.append(String.format("\\%03o", i));
            }else  if (i==92){
                sb.append("\\\\");
            }else if(i==39){
                sb.append("''");
            }else {
                sb.append((char)i);
            }
        }
        return sb.toString();
    }
}
