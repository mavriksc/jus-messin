package org.mavriksc.messin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FindSubImg {

    public static void main(String[] args) throws IOException {
        BufferedImage original = ImageIO.read(FindSubImg.class.getClassLoader().getResourceAsStream("original.png"));
        BufferedImage subimg = ImageIO.read(FindSubImg.class.getClassLoader().getResourceAsStream("subimg.png"));
        int[] coord = new int[2];
        boolean cont = contains(original, subimg, coord);
        if (cont) {
            System.out.println("FOUND AT:(" + coord[0] + "," + coord[1]+")");
        } else {
            System.out.println("NOT FOUND");
        }

    }

    static boolean contains(BufferedImage img, BufferedImage subImg, int[] coordinates) {
        int verticalLimit = img.getWidth() - subImg.getWidth();
        int horizontalLimit = img.getHeight() - subImg.getHeight();

        for (int i = 0; i <= horizontalLimit; i++) {
            for (int j = 0; j <= verticalLimit; j++) {
                subSearch:
                for (int k = 0; k < subImg.getHeight(); k++) {
                    for (int l = 0; l < subImg.getWidth(); l++) {
                        if (img.getRGB(l + j, k + i) != subImg.getRGB(l, k)) {
                            break subSearch;
                        }
                    }
                    if (k==subImg.getHeight()-1){
                        coordinates[0] = j;
                        coordinates[1] = i;
                        return true;
                    }
                }

            }
        }
        return false;
    }
}
