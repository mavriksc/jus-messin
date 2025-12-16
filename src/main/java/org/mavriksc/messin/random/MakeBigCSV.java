package org.mavriksc.messin.random;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

public class MakeBigCSV {
    public static void main(String[] a) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder("esn\r\n");
        LongStream.range(0,1000000).forEach(l -> sb.append(ThreadLocalRandom.current().nextLong(1000000L,Long.MAX_VALUE)).append("\r\n"));
        try (PrintWriter csv = new PrintWriter("biggun.csv")) {
            csv.println(sb.toString());
        }

        int range = ThreadLocalRandom.current().nextInt(3);

    }
}
