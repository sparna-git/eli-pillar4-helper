package eu.europa.op.eli.pillar4.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.opencsv.bean.CsvToBeanBuilder;


public class SitemapUpdate {
	
	public void ModifiedXMLSitemap(String inputXML,URL baseUrlset) throws FileNotFoundException, TransformerException {
		
		File fileXMLGenerated = new File(inputXML);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try(InputStream fileXML = new FileInputStream(fileXMLGenerated)){
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fileXML);
			
			doc.getDocumentElement().normalize();
			
			//Get Root url
			Element rootURL = doc.getDocumentElement();
			
			/* Add URL in urlset
			 * rootURL.setAttribute(prefix,url);
			 */
			rootURL.setAttribute("xmlns:dct","http://purl.org/dc/terms/");
			rootURL.setAttribute("dct:relation",baseUrlset.toString());
			
			// write the content into xml file
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(inputXML);
	        transformer.transform(source, result);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}	
	
}
