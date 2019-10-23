package org.mavriksc.messin.hackerrank;

public class WordArea {

    static int designerPdfViewer(int[] h, String word) {

        return word.length() * word.toLowerCase().chars().map(c -> h[c - 'a']).max().orElse(0);

    }
}
