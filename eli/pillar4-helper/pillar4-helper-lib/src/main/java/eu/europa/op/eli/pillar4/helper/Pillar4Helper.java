package eu.europa.op.eli.pillar4.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.opencsv.bean.CsvToBeanBuilder;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.atom.Person;
import com.rometools.rome.feed.synd.SyndPerson;
import com.rometools.rome.io.WireFeedOutput;

import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.SiteMapURL;

public class Pillar4Helper {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public void csv2Pillar4(File input, File output, URL baseUrl, URL baseurlset, File inputAtom)
			throws Pillar4Exception {

		log.debug("Executing csv2Pillar4...");
		long start = System.currentTimeMillis();

		try {
			// Read CSV file
			log.debug("Reading input CSV file...");
			List<SitemapCsv> stmapdata = new CsvToBeanBuilder<SitemapCsv>(new FileReader(input))
					.withType(SitemapCsv.class).withSkipLines(1).build().parse();

			// sort the list
			stmapdata.sort((row1, row2) -> row2.getLastDate().compareTo(row1.getLastDate()));

			// Format Date
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			// Create folder is not exist
			Path path = Paths.get(output.toString());
			if (!Files.exists(path)) {
				Files.createDirectory(path);
			}

			// Generator Sitemap
			WebSitemapGenerator wsg = new WebSitemapGenerator(baseUrl, output);
			for (int i = 0; i < stmapdata.size(); i++) {
				SitemapCsv out = stmapdata.get(i);
				if ((i % 1000) == 0) {
					log.debug("Processing CSV line " + i + "...");
				}

				// Date Format
				Date dateSitemap = formatter.parse(out.getLastDate());

				// Creation for the sitemap structure
				WebSitemapUrl url = new WebSitemapUrl.Options(out.getLoc()) // Loc
						.lastMod(dateSitemap) // Last Date
						.build();

				wsg.addUrl(url);
			}

			log.debug("Writing output sitemap...");
			// Write the sitemap File
			String mainoutputSitemapPath = null;
			if (stmapdata.size() > 50000) {
				wsg.write();
				wsg.writeSitemapsWithIndex();
				mainoutputSitemapPath = output.getAbsolutePath() + "/" + "sitemap_index.xml";
			} else {
				wsg.write();
				mainoutputSitemapPath = output.getAbsolutePath() + "/" + "sitemap.xml";
			}

			// Update URLSet in sitemap generated
			if (baseurlset != null) {
				log.debug("Updating base URL in sitemap in '" + mainoutputSitemapPath + "' ...");
				SitemapUpdate stmSetup = new SitemapUpdate();
				stmSetup.ModifiedXMLSitemap(mainoutputSitemapPath, baseurlset);
			}

			// Call class for the create Atom File
			// log.debug("Writing output Atom Feed ...");
			sitemap2Atom(output, output, baseUrl, inputAtom);

		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}

		long end = System.currentTimeMillis();
		log.debug("csv2Pillar4 executed in " + (end - start) + " ms");
	}

	public void sitemap2Atom(File input, File output, URL baseUrl, File inputAtom) throws Pillar4Exception {

		log.debug("Executing sitemap2Atom...");
		long start = System.currentTimeMillis();

		log.debug("Read all files sitemaps ... " + input.toString());
		File[] SITEMAP_XML = input.listFiles();
		
		Feed feed = null;
		AbstractSiteMap asm;
		SiteMapParser parser = new SiteMapParser();
		//String contentType = "application/octet-stream";
		String contentType = "text/xml";

		try {
			/*
			 * if the atom properties exist Generate The feed root element
			 */
			String inputAtomFile = inputAtom.toString();
			if (inputAtomFile != null) {
				feed = setHeaderAtomFeed(inputAtomFile);
			}

			// Read all sitemap xml files
			for (File sFile : SITEMAP_XML) {
				if (sFile.isFile() && !sFile.getName().equals("sitemap_index.xml")) {

					// load xml file a properties information
					byte[] content = getResourceAsBytes(sFile.toString());

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					c.add(c.DATE, -61);
					Date dateQuery = c.getTime();
					String dateFilter = formatter.format(dateQuery);

					AbstractSiteMap asmTest = parser.parseSiteMap(contentType, content, baseUrl);
					SiteMap sm = (SiteMap) asmTest;

					if (!sm.isIndex()) {

						log.debug("Writing Entry Atom...");
						for (SiteMapURL su : sm.getSiteMapUrls()) {
							URL urlSitemap = su.getUrl();

							// Filter for the write data sitemap in Atom
							if (su.getLastModified().after(dateQuery)) {
								// Create an Entry
								Entry entry = new Entry();
								try {
									entry.setTitle(urlSitemap.toString());
									// entry.set
									entry.setId(urlSitemap.toString());

									// Date
									entry.setUpdated(su.getLastModified());

								} catch (Exception ex) {
									System.out.println(ex);
								}

								// Add Entry to Feed
								List entries = feed.getEntries();
								if (entries == null) {
									entries = new ArrayList();
								}
								entries.add(entry);
								feed.setEntries(entries);
							}
						} // for
					}
				}
			}

			// Publish your ATOM Feed
			if (feed != null) {
				log.debug("Writing AtomFeed File .... ");

				File RSSDoc = new File(output + "/" + "atomfeed.atom");
				if (!RSSDoc.exists()) {
					RSSDoc.createNewFile();
				}
				WireFeedOutput wfo = new WireFeedOutput();
				wfo.output(feed, RSSDoc);
			}

		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}

		long end = System.currentTimeMillis();
		log.debug("sitemap2Atom executed in " + (end - start) + " ms");
	}

	/**
	 * Read a test resource file and return its content as byte array.
	 * 
	 * @param resourceName path to the resource file
	 * @return byte content of the file
	 * @throws IOException
	 */
	protected static byte[] getResourceAsBytes(String resourceName) throws IOException {
		File file = new File(resourceName);
		InputStream is = new FileInputStream(file);
		return IOUtils.toByteArray(is);
	}

	protected Feed setHeaderAtomFeed(String info) throws ParseException, IOException {

		Feed hfeed = new Feed();
		
		try {

			// parse XML file
			File xmlFile = new File(info);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);

			doc.getDocumentElement().normalize();
			NodeList dataAtomList = doc.getElementsByTagName("source");
			for (int i = 0; i < dataAtomList.getLength(); i++) {

				Node node = dataAtomList.item(i);
				
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element Element_atominfo = (Element) node;
					
					hfeed.setFeedType(Element_atominfo.getElementsByTagName("typeFeed").item(0).getTextContent());
					hfeed.setTitle(Element_atominfo.getElementsByTagName("title").item(0).getTextContent());
					hfeed.setId(Element_atominfo.getElementsByTagName("idfeed").item(0).getTextContent());

					//Link
					Link linkAtom = new Link();
					linkAtom.setRel(Element_atominfo.getElementsByTagName("rel").item(0).getTextContent());
					linkAtom.setType(Element_atominfo.getElementsByTagName("type").item(0).getTextContent());
					linkAtom.setHref(Element_atominfo.getElementsByTagName("link").item(0).getTextContent());
					
					List links = hfeed.getOtherLinks();
					if (links == null) {
						links = new ArrayList();
					}
					links.add(linkAtom);
					hfeed.setOtherLinks(links);
					 
					//Author
					Person AuthorsAtom = new Person();
					AuthorsAtom.setName(Element_atominfo.getElementsByTagName("author").item(0).getTextContent());
					
					List authorsAtom = hfeed.getAuthors();
					if (authorsAtom == null) {
						authorsAtom = new ArrayList();
					}
					authorsAtom.add(AuthorsAtom);
					hfeed.setAuthors(authorsAtom);
				
				}

			}
			String dateNow = sdf.format(new Date());
			Date udate = (Date) sdf.parseObject(dateNow);
			hfeed.setUpdated(udate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hfeed;
	}
}