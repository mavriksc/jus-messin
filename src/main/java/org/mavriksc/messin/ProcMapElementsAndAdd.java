package org.mavriksc.messin;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcMapElementsAndAdd {

    static {

    }
    public static void main(String[] args) {
        Map<String, String> textures = new HashMap<>();
        textures.put("1", "asdfasdfas");
        textures.put("2", "asdfasdsdsdsdsdfas");
        textures.put("3", "asasddffasdfas");
        textures.put("4", "asdfasdffffffffffffas");

        textures.putAll(textures.entrySet().stream().collect(Collectors.toMap(entry->"d"+entry.getKey(),entry->entry.getValue()+"zzzzz")));
        textures.forEach((k,v)-> System.out.println(k+" "+v));


    }
}
