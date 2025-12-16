package org.mavriksc.messin.random;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageTesting {
    private static final String BAD_IMAGE1 = "https://s3.amazonaws.com/erp-prod/ORAPRD2/9853593-1211410-555534-0.jpg";
    private static final String GOOD_IMAGE1 = "https://www.att.com/catalog/en/idse/Apple/Apple%20iPhone%20XR/Black-hero-zoom.png";
    private static final String GOOD_IMAGE2 = "https://www.fileformat.info/format/bmp/sample/4cb74cda027a43f3b278c05c3770950f/download";
    private static final String GIF = "https://media.giphy.com/media/13gvXfEVlxQjDO/giphy.gif";


    public static void main(String[] args) throws IOException {

        byte[] image = ImageUtil.getBinaryFile(new URL(BAD_IMAGE1));
        System.out.println("IMAGE VALID?:" + ImageUtil.isValidImage(image));
        byte[] image300 = ImageUtil.scaleImageToSquare(image, 300);
        byte[] image80 = ImageUtil.scaleImageToSquare(image, 80);
        Files.write(Paths.get("phone300.jpg"), image300);
        Files.write(Paths.get("phone80.jpg"), image80);

    }
}
