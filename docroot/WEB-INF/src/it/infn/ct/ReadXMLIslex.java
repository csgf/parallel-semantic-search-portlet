/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct;

import java.io.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author grid
 */
public class ReadXMLIslex {
    
    public static void readFile(String appServerPath){
        try {
            
            
            //File fXmlFile = new File(appServerPath 
            //    + "/WEB-INF/job/islex_final.xml");
            
            File fXmlFile = new File("/Users/grid/Downloads/ISLEX/islex_final.xml");
            
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
                            
            
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList listResult=doc.getElementsByTagName("LexicalEntry");
            for(int i=0;i<3/*listResult.getLength()*/;i++)
            {
                Node node=listResult.item(i);
                NodeList listChild=node.getChildNodes();
                System.out.println("FIGLIO 0 DI LEXICAL ENTRY "+listChild.item(0).getNodeName());
            }
            
            
            System.out.println("Root element :"+doc.getElementsByTagName("LexicalEntry").getLength());
            
        } 
        catch (SAXException ex) {
            Logger.getLogger(ReadXMLIslex.class.getName()).log(Level.SEVERE, null, ex);}
        catch (IOException ex) {
            Logger.getLogger(ReadXMLIslex.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParserConfigurationException ex) {
            Logger.getLogger(ReadXMLIslex.class.getName()).log(Level.SEVERE, null, ex);
        }
			
        
        
    }
    
    
}
