package org.mavriksc.messin;

import java.nio.charset.Charset;
import java.util.Base64;

public class Base64Encoding {
    public static void main(String[] args){

        String src = "Sean Carlisle";
        String encoded =  new String(Base64.getEncoder().encode(src.getBytes()), Charset.defaultCharset());
        System.out.println(encoded);
        System.out.println(new String(Base64.getDecoder().decode(encoded),Charset.defaultCharset()));

    }
}
