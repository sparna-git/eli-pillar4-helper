package eu.europa.op.eli.pillar4.helper;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pillar4Helper {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	public void csv2Pillar4(File input, File output) throws Pillar4Exception {
		log.debug("Executing csv2Pillar4...");
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		log.debug("csv2Pillar4 executed in "+(end - start)+" ms");
	}
	
	public void sitemap2Atom(File input, File output) throws Pillar4Exception {
		log.debug("Executing sitemap2Atom...");
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		log.debug("sitemap2Atom executed in "+(end - start)+" ms");
	}
	
}
