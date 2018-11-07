package main;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadJsonFromFile {
public static void main(String[] args){
    File file = new File("C:\\Users\\SCarlisle\\qis\\test-currency-format\\src\\main\\resources\\response.json");
    String jsonString="";
    try {
         jsonString =readFile(file.getPath(), StandardCharsets.UTF_8);
    } catch (IOException e) {
        e.printStackTrace();
    }
    JSONObject object = new JSONObject(jsonString);
}





    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
