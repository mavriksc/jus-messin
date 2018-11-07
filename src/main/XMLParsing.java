/*
* @(#)XMLParsing.java Apr 18, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package main;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author SCarlisle
 *
 */
public class XMLParsing {
    
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setValidating(true);
//        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse("http://rss.adnkronos.com/RSS_Politica.xml");

        NodeList nodes = doc.getElementsByTagName("title");

        for(int k=0; k < nodes.getLength(); k++) {
            System.out.print(nodes.item(k).getTextContent());
        }

    }

}
