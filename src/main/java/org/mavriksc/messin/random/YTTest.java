package org.mavriksc.messin.random;

import com.google.api.services.youtube.YouTube;

import java.io.IOException;

public class YTTest {
    public static void main(String[]a) throws IOException {
        YouTube ytService = new YouTube.Builder(null,null,null).build();
        YouTube.Videos.Rate r= ytService.videos().rate(null,"like");
        r.execute();
    }
}
