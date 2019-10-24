package org.mavriksc.messin;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public class PDFMessin {
    public static void main(String[] args) throws IOException {
        PDDocument doc = PDDocument.load(PDFMessin.class.getClassLoader().getResourceAsStream("pdfTest1.pdf"));
        System.out.println("things");

    }
    
}
