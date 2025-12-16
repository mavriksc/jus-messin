package org.mavriksc.messin.random;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtil {

    public static byte[] scaleImageToSquare(byte[] imageData, int maxDimension) {
        try {
            byte[] data = null;
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage image = ImageIO.read(bis);
            if (image != null) {// invalid image
                BufferedImage scaledImage = scale(image, maxDimension);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(scaledImage, "jpeg", bos);
                data = bos.toByteArray();
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read image data", e);
        }
    }

    public static boolean isValidImage(byte[] imageData) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage image = ImageIO.read(bis);
            return image != null;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read image data", e);
        }
    }

    public static BufferedImage bytesToBufferedImage(byte[] bytes) {
        BufferedImage image=null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            image = ImageIO.read(bis);
        } catch (IOException e) {
            System.out.println("things broke");
        }
        return image;
    }

    private static BufferedImage scale(BufferedImage src, int maxDimension) {
        int type = BufferedImage.TYPE_INT_RGB;
        int width = src.getWidth();
        int height = src.getHeight();

        // Default the scaled image to the src one
        BufferedImage scaledImage = src;

        // If the src image is smaller than maxDimension x maxDimension, then
        // don't scale it.
        if (width > maxDimension || height > maxDimension) {
            double scale = (double) maxDimension / (double) height;
            if (width > height) {
                scale = (double) maxDimension / (double) width;
            }

            int scaledW = (int) (scale * width);
            int scaledH = (int) (scale * height);

            scaledImage = toBufferedImage(src.getScaledInstance(scaledW, scaledH, Image.SCALE_AREA_AVERAGING), type);
        }

        // Want the final image to be maxDimension x maxDimension, so need to
        // create a new one of that size
        BufferedImage image = new BufferedImage(maxDimension, maxDimension, type);

        // Center the image
        int x = (maxDimension - scaledImage.getWidth()) / 2;
        int y = (maxDimension - scaledImage.getHeight()) / 2;
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, maxDimension, maxDimension);
        g.drawImage(scaledImage, x, y, null);
        g.dispose();
        return image;
    }

    private static BufferedImage toBufferedImage(Image image, int type) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage result = new BufferedImage(w, h, type);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    static byte[] getBytes(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (InputStream is = url.openStream()) {
                byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
                int n;

                while ((n = is.read(byteChunk)) > 0) {
                    baos.write(byteChunk, 0, n);
                }
            } catch (IOException e) {
                System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
                e.printStackTrace();
                // Perform any other exception handling that's appropriate.
            }
            return baos.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public byte[] getBinaryFile(URL url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url.toExternalForm());
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toByteArray(entity) : null;
        }
    }
}
