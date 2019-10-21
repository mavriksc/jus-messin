package org.mavriksc.messin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReadAndUpdateFile {

    public static void main(String[] args) throws IOException {
        String detectedVer = "(31) chromeVersion(76/77/78), if you don't know this field will be filled automatically upon the first run of the bot): \" + chromeVersion";
        Path p = Paths.get("C:\\git\\mystuff\\jus-messin\\src\\main\\resources\\props.txt");
        List<String> lines = Files.readAllLines(p);
        String lineToChange = lines.stream().filter(s-> s.contains("chromeVersion")).findFirst().orElse("");
        if(!lineToChange.isEmpty()){
            lines.set(lines.indexOf(lineToChange),detectedVer);
        }
        Files.write(p,lines, StandardCharsets.UTF_8);

    }
}
