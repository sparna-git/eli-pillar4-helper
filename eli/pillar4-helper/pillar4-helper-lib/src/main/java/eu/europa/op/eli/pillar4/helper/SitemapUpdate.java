package eu.europa.op.eli.pillar4.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class SitemapUpdate {
	
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public void ModifiedXMLSitemap(String inputXML,URL baseUrlset) throws FileNotFoundException, TransformerException, ParserConfigurationException {
		
		File fileXMLGenerated = new File(inputXML);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// enable validation to comply with security checks, even if this is not needed since
		// the file we parse is the file that has just been written before
		dbf.setValidating(true);
		dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		
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
			// completely disable DTDs for security reasons
			transformerFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	        Transformer transformer = transformerFactory.newTransformer();			
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(inputXML);
	        transformer.transform(source, result);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// avoid logs for security reasons
			// System.out.println("Error in parsing the sitemap file : "+e.getMessage());
			// e.printStackTrace();
			// log.debug("error in sitemap update");
		}
	}	
	
}
