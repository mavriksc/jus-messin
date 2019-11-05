package org.mavriksc.messin;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * This is an example on how to remove text from PDF document.
 * <p>
 * Note:
 * ------------
 * Because of nature of the PDF structure itself, actually this will not work 100% able to find text that need to be replaced.
 * There are other solutions for that, for example using PDFTextStripper.
 *
 * @author Christian H <chadilukito@gmail.com>
 */
public final class ReplaceText {

    public static void main(String[] args) throws IOException {
        PDDocument document = null;
        try {
            document = PDDocument.load(ReplaceText.class.getClassLoader().getResourceAsStream("pdfTest1.pdf"));
            document = _ReplaceText(document, "@changeMe@", "HEY THIS WORKED!!!");
            document.save("pdfTest1(out).pdf");
        } finally {
            if (document != null) {
                document.close();
            }
        }

    }

    private static PDDocument _ReplaceText(PDDocument document, String searchString, String replacement)
            throws IOException {
        if (StringUtils.isEmpty(searchString) || StringUtils.isEmpty(replacement)) {
            return document;
        }

        for (PDPage page : document.getPages()) {
            PDFStreamParser parser = new PDFStreamParser(page);
            parser.parse();
            List tokens = parser.getTokens();

            for (int j = 0; j < tokens.size(); j++) {
                Object next = tokens.get(j);
                if (next instanceof Operator) {
                    Operator op = (Operator) next;

                    String pstring = "";
                    int prej = 0;

                    //Tj and TJ are the two operators that display strings in a PDF
                    if (op.getName().equals("Tj")) {
                        // Tj takes one operator and that is the string to display so lets update that operator
                        COSString previous = (COSString) tokens.get(j - 1);
                        String string = previous.getString();
                        System.out.println(string);
                        string = string.replaceFirst(searchString, replacement);
                        previous.setValue(string.getBytes());
                    } else if (op.getName().equals("TJ")) {
                        COSArray previous = (COSArray) tokens.get(j - 1);
                        for (int k = 0; k < previous.size(); k++) {
                            Object arrElement = previous.getObject(k);
                            if (arrElement instanceof COSString) {
                                COSString cosString = (COSString) arrElement;
                                String string = cosString.getString();

                                if (j == prej) {
                                    pstring += string;
                                } else {
                                    prej = j;
                                    pstring = string;
                                }
                                System.out.println(pstring);
                            }
                        }

                        if (searchString.equals(pstring.trim())) {
                            COSString cosString2 = (COSString) previous.getObject(0);
                            cosString2.setValue(replacement.getBytes());

                            int total = previous.size() - 1;
                            for (int k = total; k > 0; k--) {
                                previous.remove(k);
                            }
                        }
                    }
                }
            }

            // now that the tokens are updated we will replace the page content stream.
            PDStream updatedStream = new PDStream(document);
            OutputStream out = updatedStream.createOutputStream(COSName.FLATE_DECODE);
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(tokens);
            out.close();
            page.setContents(updatedStream);
        }

        return document;
    }
}
