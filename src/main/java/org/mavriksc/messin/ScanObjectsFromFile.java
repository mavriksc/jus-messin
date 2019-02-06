package org.mavriksc.messin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ScanObjectsFromFile {

    public static void main(String[] args){
       List<Map<String,Object>> items = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(ClassLoader.getSystemResource("stats.txt").getFile()));
            while (scanner.hasNext()){
                String[] itemString = scanner.nextLine().split(";");
                Map<String,Object> item = new HashMap<>();
                item.put("id",Integer.parseInt(itemString[0]));
                item.put("name",itemString[1]);
                item.put("quant",Integer.parseInt(itemString[2]));
                item.put("price",Float.parseFloat(itemString[3]));
                item.put("suplierId",Integer.parseInt(itemString[4]));
                items.add(item);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        items.forEach(i->{
            i.forEach((key, value) -> System.out.println(key + " : " + value));
            System.out.println();
        });
    }
}
