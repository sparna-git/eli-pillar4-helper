package eu.europa.op.eli.pillar4.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.AbstractTupleQueryResultHandler;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResultHandlerException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.WireFeedOutput;

import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.SiteMapURL;

public class Pillar4Helper {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	

	private SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
 

	// the number of days before today to build the date range
	// in which entries of the sitemap will be inserted in the Atom
	private int numberOfDaysInRange = 60;

	
	public List<Pillar4Entry> parseCsv(File input) throws Pillar4Exception {
		try {
			log.debug("Reading input CSV file...");
			CsvToBean<Pillar4Entry> parser = new CsvToBeanBuilder<Pillar4Entry>(new FileReader(input))
					.withType(Pillar4Entry.class)
					.withSkipLines(1)
					.build();
			List<Pillar4Entry> entries = parser.parse();
			
			// sort the list
			entries.sort((row1, row2) -> row2.getUpdateDate().compareTo(row1.getUpdateDate()));
			
			log.debug("Done reading CSV");
			return entries;
		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}
	}
	
	public List<Pillar4Entry> executeSparql(File query, String endpointUrl) throws Pillar4Exception {
		try {
			log.debug("executing SPARQL...");
			List<Pillar4Entry> entries;
			SPARQLRepository r = new SPARQLRepository(endpointUrl);
			r.init();
			
			try(RepositoryConnection c = r.getConnection()) {
				TupleQuery q = c.prepareTupleQuery(Files.readString(query.toPath()));
				Pillar4SparqlQueryResultHandler handler = new Pillar4SparqlQueryResultHandler();
				q.evaluate(handler);
				entries = handler.getEntries();
			} catch (TupleQueryResultHandlerException e) {
				throw new Pillar4Exception(e);
			}
			
			// sort the list
			entries.sort((row1, row2) -> row2.getUpdateDate().compareTo(row1.getUpdateDate()));
			
			log.debug("Done executing SPARQL");
			return entries;
		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}
	}
	
	public void entries2Pillar4(
			List<Pillar4Entry> entries,
			File outputSitemapDir,
			File outputAtomFile,
			URL baseUrl,
			URL baseurlset,
			File atomHeader
	) throws Pillar4Exception {

		log.debug("Generating Pillar4...");
		long start = System.currentTimeMillis();

		try {
			// Create folder is not exist
			Path path = Paths.get(outputSitemapDir.toString());
			if (!Files.exists(path)) {
				Files.createDirectory(path);
			}

			
			List<Entry> atomEntries = new ArrayList<Entry>();
			Date dateThreshold = this.getDateThreshold();
			
			// Generator Sitemap
			WebSitemapGenerator wsg = new WebSitemapGenerator(baseUrl, outputSitemapDir);
			for (int i = 0; i < entries.size(); i++) {
				Pillar4Entry csvLine = entries.get(i);
				if ((i % 1000) == 0) {
					log.debug("Processing entry " + i + "...");
				}

				// Date Format
				Date updateDate;
				try {
					updateDate = DATE_TIME_FORMATTER.parse(csvLine.getUpdateDate());
				} catch(Exception e) {
					updateDate = DATE_FORMATTER.parse(csvLine.getUpdateDate());
				}
				

				// Creation for the sitemap structure
				WebSitemapUrl url = new WebSitemapUrl
						.Options(csvLine.getUrl()) // Loc
						.lastMod(updateDate) // Last Date
						.build();

				wsg.addUrl(url);
				
				// then build Atom feed if date is in range
				if (updateDate.after(dateThreshold)) {
					// Create an Entry
					Entry entry = new Entry();
					
					entry.setId(csvLine.getUrl());
					entry.setUpdated(updateDate);
					if(csvLine.getTitle() != null && !csvLine.getTitle().equals("")) {
						entry.setTitle(csvLine.getTitle());
					} else {
						entry.setTitle(csvLine.getUrl());
					}					

					// Add Entry to Feed
					log.debug("Adding entry to output feed "+entry.getTitle()+" (updated "+DATE_TIME_FORMATTER.format(updateDate)+")...");
					atomEntries.add(entry);
				}
			}

			log.debug("Writing output sitemap...");
			log.debug("Writing to "+outputSitemapDir.getAbsolutePath());
			// Write the sitemap File
			String mainoutputSitemapPath = null;
			if (entries.size() > 50000) {
				wsg.write();
				wsg.writeSitemapsWithIndex();
				mainoutputSitemapPath = outputSitemapDir.getAbsolutePath() + "/" + "sitemap_index.xml";
			} else {
				wsg.write();
				mainoutputSitemapPath = outputSitemapDir.getAbsolutePath() + "/" + "sitemap.xml";
			}

			// Update URLSet in sitemap generated
			if (baseurlset != null) {
				log.debug("Updating base URL in sitemap in '" + mainoutputSitemapPath + "' ...");
				SitemapUpdate stmSetup = new SitemapUpdate();
				stmSetup.ModifiedXMLSitemap(mainoutputSitemapPath, baseurlset);
			}

			// Then write Atom feed			
			entries2Atom(atomEntries, outputAtomFile, atomHeader);

		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}

		long end = System.currentTimeMillis();
		log.debug("Pillar4 generated in " + (end - start) + " ms");
	}
	

	
	public void entries2Atom(
			List<Entry> entries,
			File output,
			File atomHeader
	) throws Pillar4Exception {
		log.debug("Writing final Atom feed...");
		long start = System.currentTimeMillis();
		try {
			/*
			 * if the atom properties exist Generate The feed root element
			 */
			Feed feed = parseAtomHeader(atomHeader);
			// init entries if null
			if (feed.getEntries() == null) {
				feed.setEntries(new ArrayList<Entry>());
			}
			
			for (Entry entry : entries) {
				// Add Entry to Feed
				log.debug("Adding entry to output feed "+entry.getId()+" (updated "+DATE_TIME_FORMATTER.format(entry.getUpdated())+")...");
				feed.getEntries().add(entry);
			}

			// Publish the ATOM Feed
			log.debug("Writing AtomFeed File...");
			log.debug("Writing to "+output.getAbsolutePath());
			if (!output.exists()) {
				output.createNewFile();
			}
			WireFeedOutput wfo = new WireFeedOutput();
			wfo.output(feed, output);

		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}

		long end = System.currentTimeMillis();
		log.debug("Done writing final Atom feed in " + (end - start) + " ms");
	}
	
	

	public void sitemap2Atom(
			File input,
			File output,
			URL baseUrl,
			File atomHeader
	) throws Pillar4Exception {

		log.debug("Executing sitemap2Atom...");
		long start = System.currentTimeMillis();

		log.debug("Reading sitemap from " + input.toString() + "...");

		try {			
			Date dateThreshold = this.getDateThreshold();
			SiteMapParser parser = new SiteMapParser();

			// Read all XML files
			
			File[] xmlFiles = input.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().endsWith(".xml");
			    }
			});
			if(xmlFiles == null) {
				throw new Pillar4Exception("Cannot find any xml files in "+input.getAbsolutePath());
			}
			
			List<Entry> atomEntries = new ArrayList<Entry>();
			for (File sFile : xmlFiles) {
				if (sFile.isFile()) {

					InputStream is = new FileInputStream(sFile);
					AbstractSiteMap sm = parser.parseSiteMap("text/xml", IOUtils.toByteArray(is), baseUrl);
					is.close();

					if (!sm.isIndex()) {

						log.debug("Parsing sitemap file "+sFile.getAbsolutePath()+"...");
						for (SiteMapURL su : ((SiteMap)sm).getSiteMapUrls()) {
							URL urlSitemap = su.getUrl();
							// log.debug("Testing entry for feed inclusion : "+urlSitemap.toString()+"/"+su.getLastModified()+"...");
							
							// Filter for the write data sitemap in Atom
							if (su.getLastModified().after(dateThreshold)) {
								// Create an Entry
								Entry entry = new Entry();

								entry.setTitle(urlSitemap.toString());
								entry.setId(urlSitemap.toString());
								entry.setUpdated(su.getLastModified());

								// Add Entry to Feed
								log.debug("Adding entry to output feed "+urlSitemap.toString()+" (updated "+DATE_TIME_FORMATTER.format(su.getLastModified())+")...");
								atomEntries.add(entry);
							}
						} // end for
					}
				}
			}

			// Publish the ATOM Feed
			entries2Atom(
					atomEntries,
					output,
					atomHeader
			);

		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}

		long end = System.currentTimeMillis();
		log.debug("sitemap2Atom executed in " + (end - start) + " ms");
	}
	
	private Date getDateThreshold() {
		Calendar c = Calendar.getInstance();
		c.add(c.DATE, -numberOfDaysInRange);
		return c.getTime();
	}

	protected Feed parseAtomHeader(File header) throws Pillar4Exception {		
		try {
			// parse Atom header information provided in the header
			SyndFeedInput inputParser = new SyndFeedInput();
			return (Feed)inputParser.build(header).createWireFeed();
		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}
	}

	public int getNumberOfDaysInRange() {
		return numberOfDaysInRange;
	}

	public void setNumberOfDaysInRange(int numberOfDaysInRange) {
		this.numberOfDaysInRange = numberOfDaysInRange;
	}	
	
}