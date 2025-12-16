package org.mavriksc.messin.random

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


fun main() {
    val inStream = FileInputStream("C:\\git\\mystuff\\jus-messin\\src\\main\\resources\\Things1.docx")
    val doc = XWPFDocument(inStream)
    val outFile = File("Things1.pdf")
    val outStream = FileOutputStream(outFile)
    val options:PdfOptions? = null
    PdfConverter.getInstance().convert(doc,outStream,options)
}
