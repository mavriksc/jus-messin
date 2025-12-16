package org.mavriksc.messin.random;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class WriteJsonToFile {

    public static void main(String[] args){
        String filePathToSave = "/data/provisioning/nexus_contents.json";
        String url = "https://data.cityofnewyork.us/api/views/kku6-nxdu/rows.json?accessType=DOWNLOAD";

        try {
            FileUtils.copyURLToFile(
                    new URL(url)
                    , new File(filePathToSave),
                    5000,
                    5000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
