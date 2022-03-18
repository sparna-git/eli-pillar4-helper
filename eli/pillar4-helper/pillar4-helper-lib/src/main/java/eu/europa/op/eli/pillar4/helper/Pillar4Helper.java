package eu.europa.op.eli.pillar4.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.CalendarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

import com.redfin.sitemapgenerator.SitemapIndexGenerator;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.WireFeedOutput;

import crawlercommons.CrawlerCommons;
import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.AbstractSiteMap.SitemapType;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapIndex;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.SiteMapTester;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class Pillar4Helper {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	public void csv2Pillar4(File input, File output, String baseUrl, String baseurlset)
			throws Pillar4Exception, ParseException, CsvException, ParserConfigurationException, TransformerException {

		log.debug("Executing csv2Pillar4...");
		long start = System.currentTimeMillis();

		try {
			// Read csv file
			List<SitemapCsv> stmapdata = new CsvToBeanBuilder(new FileReader(input)).withType(SitemapCsv.class)
					.withSkipLines(1).build().parse();

			try {
				// Format Date
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				// Generator Sitemap
				WebSitemapGenerator wsg = new WebSitemapGenerator(baseUrl, new File(output, ""));
				for (SitemapCsv out : stmapdata) {

					// Date Format
					Date dateSitemap = formatter.parse(out.getLastDate());

					// Creation for the sitemap structure
					WebSitemapUrl url = new WebSitemapUrl.Options(out.getLoc()) // Loc
							.lastMod(dateSitemap) // Last Date
							.build();

					wsg.addUrl(url);
				}

				// Write the sitemap File
				wsg.write();

				//
				String FileXMLCreated = null;
				if (stmapdata.size() > 50000) {
					wsg.writeSitemapsWithIndex();
					FileXMLCreated = output.getAbsolutePath() + "\\" + "sitemap_index.xml";
				} else {
					FileXMLCreated = output.getAbsolutePath() + "\\" + "sitemap.xml";
				}

				// Update URLSet in sitemap generated
				if (!baseurlset.isEmpty()) {
					SitemapUpdate stmSetup = new SitemapUpdate();
					stmSetup.ModifiedXMLSitemap(FileXMLCreated, baseurlset);
				}

				// Call class for the create Atom File
				

			} catch (IOException e) {
				System.out.println(e);
			}

		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		long end = System.currentTimeMillis();
		log.debug("csv2Pillar4 executed in " + (end - start) + " ms");
	}

	public void sitemap2Atom(File input, File output, URL baseUrl)
			throws Pillar4Exception, IOException, ParseException, UnknownFormatException {
		log.debug("Executing sitemap2Atom...");
		long start = System.currentTimeMillis();

		List<SiteMap> lsiteMaps = new ArrayList<SiteMap>();

		File[] SITEMAP_XML = output.listFiles();

		AbstractSiteMap asm;
		// read all xml file

		List<SiteMap> siteMaps = new ArrayList<>();
		SiteMapParser sitemapParser = new SiteMapParser();

		SiteMapParser parser = new SiteMapParser();
		String contentType = "application/octet-stream";
		//byte[] content = "this is a bogus sitemap".getBytes(StandardCharsets.UTF_8);
		
		
		
		// Read all sitemap xml files
		for(File sFile : SITEMAP_XML) {
			if(sFile.isFile()) {
				byte[] content = getResourceAsBytes(sFile.toString());
				
				// Config
				parser.parseSiteMap(contentType, content, baseUrl);
				AbstractSiteMap asmTest = parser.parseSiteMap(contentType, content, baseUrl);				
				SiteMap sm = (SiteMap) asmTest;
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);				
				Calendar c = Calendar.getInstance();
				c.add(c.DATE, -61);
				Date dateQuery = c.getTime();
				String dateFilter = formatter.format(dateQuery);
				
				if(!sm.isIndex()) {
					siteMaps.add(sm); 
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
					SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
					// Creation source for the Atom Feed
					Feed feed = new Feed();
					SyndFeed feedAtt = new SyndFeedImpl();
			        try {
			           feed.setFeedType("atom_1.0");
			           feed.setTitle("Fictitious ELI update feed");
			            
			           feed.setId("urn:country-xy:eli:eli-update-feed");
			            
			           String dateNow = sdf.format(new Date());
			           Date udate = (Date) sdf.parseObject(dateNow);			 
			           feed.setUpdated(udate);
			           //feed.setAuthors(); 
			           
			           
			           feedAtt.setAuthor("Country Legislation Service");
			           feedAtt.setLink("http://data.europa.eu/eli/eli-update-feed.atom");
			           
			           
			            
			        } catch (Exception ee) {
			            System.out.println(ee);
			        }
					
					
					
					for (SiteMapURL su : sm.getSiteMapUrls()) {
						//su.getAttributes();
						URL urlSitemap = su.getUrl();
						String lastUpdate = formatter.format(su.getLastModified());
						// Filter for the write data sitemap in Atom 
						if (su.getLastModified().after(dateQuery)) {
							// Create an Entry
					        Entry entry = new Entry();
					        try {
					        	entry.setTitle(urlSitemap.toString());
					        	//entry.set
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
					//Publish your ATOM Feed
			        try {
			            File RSSDoc = new File("C:\\Temp\\atomfeed.atom");
			            if (!RSSDoc.exists()) {
			                RSSDoc.createNewFile();
			            }
			            WireFeedOutput wfo = new WireFeedOutput();
			            wfo.output(feed, RSSDoc);	
			            //wfo.output(feedAtt, RSSDoc);	
			        } catch (Exception ee) {
			            System.out.println(ee);
			        }
				}
				
				
				
			}
		}
		
		
		/*
		 * for(File file : SITEMAP_XML) { if(file.isFile()) { String contentType =
		 * "text/xml"; byte[] content = getResourceAsBytes(file.toString()); URL url =
		 * new URL("http://data.europa.eu/eli/sitemap1.xml");
		 * 
		 * asm = sitemapParser.parseSiteMap(content, url); List<SiteMapURL> siteMapUrls
		 * = new LinkedList<>(); if(asm.getType() == SitemapType.INDEX) { for
		 * (AbstractSiteMap siteMapFromIndex : ((SiteMapIndex) asm).getSitemaps()) {
		 * siteMapUrls.addAll(parse(siteMapFromIndex.getUrl().toURI(), )); } } } }
		 */
		// Get Date Calculate
		
		/*
		 * List<SiteMap> stmSource = siteMaps .stream() .filter(src ->
		 * src.getLastModified().after(date)) .collect(Collectors.toList());
		 * 
		 * System.out.println("Total: "+stmSource.size());
		 * 
		 * stmSource.forEach(s ->
		 * {System.out.println(s.getUrl()+" ***** "+s.getLastModified());});
		 */
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
}