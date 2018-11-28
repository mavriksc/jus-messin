package org.mavriksc.messin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileWriting {

    //have to check for and create directory structure before creating file.
    //if use create or mkdirs on whole file path will make dir with filename

    private static final String path = "resources";
    private static final String filename = "java.ini";

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        data.add("asdfasdfasdfasd");
        data.add("asdfasdfasdfasd");
        data.add("asdfasdfasdfasd");
        data.add("asdfasdfasdfasd");
        data.add("asdfasdfasdfasd");
        Path p = Paths.get(path);
        try {
            if (!Files.exists(p)) {
                try {
                    Files.createDirectories(p);
                } catch (IOException e) {
                    //fail to create directory
                    e.printStackTrace();
                }
            }
            File f = new File(path + "/" + filename);

            Files.write(f.toPath(), data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
