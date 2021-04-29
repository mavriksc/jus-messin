/*
* @(#)RemoveDupes.java Feb 22, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SCarlisle
 *
 */
public class RemoveDupes {

    private static String sourcePath = "C:\\git\\vzw-b2b\\src\\localization\\";
    private static String filename = "messages_en.properties";

    public static void main(String[] args) {
        Set<String> origFile = new HashSet<>(readTxtFile(sourcePath + filename));
        List<String> dirs = getDirs(sourcePath);
        for (String string : dirs) {
            List<String> cleaned = removeDupes(origFile, readTxtFile(sourcePath + "\\" + string + "\\" + filename));
            writeFile(cleaned, sourcePath + "\\" + string + "\\" + filename);
        }
    }

    private static void writeFile(List<String> cleaned, String path) {
        try {
            FileWriter fw = new FileWriter(path + ".new");
            for (String string : cleaned) {
                fw.append(string).append("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<String> removeDupes(Set<String> orig, List<String> target) {
        return target.stream().filter(s -> {
            if (!orig.contains(s)) {
                return true;
            } else {
                System.out.println("REMOVED DUPLICATE LINE:"+s);
                return false;
            }
        }).collect(Collectors.toList());
    }

    private static List<String> getDirs(String path) {
        File f = new File(path);
        return Arrays.asList(Objects.requireNonNull(f.list((dir, name) -> !name.contains("."))));
    }

    private static List<String> readTxtFile(String aFileName) {
        Path path = Paths.get(aFileName);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("you messed up");
            e.printStackTrace();
        }
        return lines;
    }

}
