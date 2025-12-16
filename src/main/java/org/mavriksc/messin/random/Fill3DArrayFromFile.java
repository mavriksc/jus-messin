package org.mavriksc.messin.random;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Fill3DArrayFromFile {

    public static void main(String[] a) throws IOException {
        int[][][] ct = new int[204][204][139];
        for (int x = 0; x < 204; x++) {
            for (int y = 0; y < 204; y++) {
                for (int z = 0; z < 139; z++) {
                    ct[x][y][z] = -1000;
                }
            }
        }
        File file = new File(ClassLoader.getSystemResource("CT-Chest.txt").getFile());
        List<String> lines = Files.readAllLines(file.toPath());
        lines.stream().filter(s->!s.trim().isEmpty()).forEach(s->{
            String[] split = s.split(" ");
            ct[Integer.parseInt(split[0])][Integer.parseInt(split[1])][Integer.parseInt(split[2])]=Integer.parseInt(split[3]);
        });
        System.out.println(ct[5][8][99]);
    }

}
