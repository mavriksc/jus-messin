package org.mavriksc.messin.random;

import java.nio.CharBuffer;

public class CharBufferTesting {

    public static void main(String[] args){
        CharBuffer cb1 = CharBuffer.allocate(10);
        cb1.put('0');
        cb1.put('a'); // element type - char ?
        cb1.put('b');
        cb1.rewind();
        cb1.limit(7);
        System.out.println(cb1);

        CharBuffer cb2 = CharBuffer.allocate(11);
        cb2.put("0ab"); // element type - String ?
        cb2.rewind();
        cb2.limit(7);
        System.out.println(cb2);

        // 0ab = 0ab + same number of "empty positions" until limit
        // (don't know how to name "empty positions" correctly)
        System.out.println(cb1.equals(cb2));  // TRUE
    }
}
