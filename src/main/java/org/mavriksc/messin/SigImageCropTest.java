package org.mavriksc.messin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class SigImageCropTest {
    private static final String FILE = "sig-image-2.dat";
    private static final String HEADER = "data:image/png;base64,";

    public static void main(String[] args) throws IOException {
        File file = new File(ClassLoader.getSystemResource(FILE).getFile());
        List<String> lines = Files.readAllLines(file.toPath());
        byte[] bytes = Base64.getDecoder().decode(lines.get(0).substring(HEADER.length()));
        //byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(lines.get(0).substring(HEADER.length()));
        long start = new Date().getTime();
        BufferedImage img = ImageUtil.bytesToBufferedImage(bytes);
        System.out.println(new Date().getTime()-start);
        File out = new File("saved-2.png");
        //ImageIO.write(trimImage(img), "png", out);
        ImageIO.write(img,"png",out);
    }

    private static byte[] getBytes(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            System.out.println("error getting bytes");
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private static BufferedImage trimImage(BufferedImage image) {
        WritableRaster raster = image.getAlphaRaster();
        int width = raster.getWidth();
        int height = raster.getHeight();
        int left = 0;
        int top = 0;
        int right = width - 1;
        int bottom = height - 1;
        int minRight = width - 1;
        int minBottom = height - 1;

        top:
        for (; top < bottom; top++) {
            for (int x = 0; x < width; x++) {
                if (raster.getSample(x, top, 0) != 0) {
                    minRight = x;
                    minBottom = top;
                    break top;
                }
            }
        }

        left:
        for (; left < minRight; left++) {
            for (int y = height - 1; y > top; y--) {
                if (raster.getSample(left, y, 0) != 0) {
                    minBottom = y;
                    break left;
                }
            }
        }

        bottom:
        for (; bottom > minBottom; bottom--) {
            for (int x = width - 1; x >= left; x--) {
                if (raster.getSample(x, bottom, 0) != 0) {
                    minRight = x;
                    break bottom;
                }
            }
        }

        right:
        for (; right > minRight; right--) {
            for (int y = bottom; y >= top; y--) {
                if (raster.getSample(right, y, 0) != 0) {
                    break right;
                }
            }
        }

        return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
    }

}
