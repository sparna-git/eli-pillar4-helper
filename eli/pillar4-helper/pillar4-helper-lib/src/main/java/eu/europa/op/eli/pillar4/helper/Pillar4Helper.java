package eu.europa.op.eli.pillar4.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

import com.redfin.sitemapgenerator.SitemapIndexGenerator;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;

public class Pillar4Helper {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public void csv2Pillar4(File input, File output, String baseUrl, String baseurlset)
			throws Pillar4Exception, ParseException, CsvException, ParserConfigurationException, TransformerException {
		
		log.debug("Executing csv2Pillar4...");
		long start = System.currentTimeMillis();

		try {
			//Read csv file 
			List<SitemapCsv> stmapdata = new CsvToBeanBuilder(new FileReader(input)).withType(SitemapCsv.class)
					.withSkipLines(1).build().parse();

			/*
			String fileSetup = input.getPath().replace(input.getName(), "")+"setup.csv";
			SitemapUpdate stmSetup = new SitemapUpdate();
			List<SetupDataSitemap> stmsetup = new ArrayList<>();
			try {
				stmsetup = stmSetup.readSetupSitemapdata(fileSetup);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Get Base URL
			String URLSitemap = null;
			for(SetupDataSitemap src : stmsetup) {
				if(src.getNameId().equals("BaseURL")) {
					URLSitemap = src.getValue();
				}
			}	
			*/
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
				if(stmapdata.size() > 50000) {
					wsg.writeSitemapsWithIndex();
					FileXMLCreated = output.getAbsolutePath()+"\\"+"sitemap_index.xml";
				}else {
					FileXMLCreated = output.getAbsolutePath()+"\\"+"sitemap.xml";
				}
					
				//Update URLSet in sitemap generated
				SitemapUpdate stmSetup = new SitemapUpdate();
				stmSetup.ModifiedXMLSitemap(FileXMLCreated,baseurlset);
				
				// Generate Atom File
				//AtomGenerator atmgenerator = new AtomGenerator();
				//atmgenerator.generateAtom(output, stmsetup);				

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

	public void sitemap2Atom(File input, File output) throws Pillar4Exception {
		log.debug("Executing sitemap2Atom...");
		long start = System.currentTimeMillis();

		// Read xml file (Sitemap)
		//AtomGenerator atmgenerator = new AtomGenerator();
		//atmgenerator.generateAtom(output, setupData);

		long end = System.currentTimeMillis();
		log.debug("sitemap2Atom executed in " + (end - start) + " ms");
	}
}