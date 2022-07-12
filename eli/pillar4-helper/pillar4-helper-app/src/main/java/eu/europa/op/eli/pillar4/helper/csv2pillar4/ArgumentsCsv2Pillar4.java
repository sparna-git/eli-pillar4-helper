package eu.europa.op.eli.pillar4.helper.csv2pillar4;

import java.io.File;
import java.net.URL;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
		commandDescription = "Generates a Pillar 4 sitemap and Atom feed from a CSV file."
)
public class ArgumentsCsv2Pillar4 {

	@Parameter(
			names = { "-i", "--input" },
			description = "Path to the input CSV file. "
					+ "The CSV file MUST contain 2 columns : the ELI URI, and the update date of this ELI. "
					+ "The CSV file MUST have a first line containing the headers, that will be ignored at parsing. "
					+ "The date MUST use the format yyyy-MM-dd or yyyy-MM-dd'T'hh:mm:ss"
					+ "An optional third column can contain a title to be included in the Atom feed. ",					
			required = true
	)
	private File input;
	
	@Parameter(
			names = { "-so", "--sitemapOutput" },
			description = "Path to the output directory for the sitemap files",
			required = true
	)
	private File outputSitemap;
	
	@Parameter(
			names = { "-su", "--sitemapBaseUrl" },
			description = "Base URL of ELIs that will be listed in the sitemap file. All ELIs in the sitemap MUST begin by this base URL.",
			required = true
	)
	private URL baseUrl;
	
	@Parameter(
			names = { "-ao", "--atomOutput" },
			description = "Path to the output atom file",
			required = true
	)
	private File outputAtom;
	
	@Parameter(
			names = { "-ah", "--atomHeader" },
			description = "Path to the input Atom 'skeleton' file containing the Atom header information",
			required = true
	)
	private File atomHeader;

	@Parameter(
			names = { "-au", "--atomUrl" },
			description = "Target Atom Feed URL. If set, a dct:relation attribute will be inserted in the sitemap file.",
			required = false
	)
	private URL feedUrl;
	
	@Parameter(
			names = { "-ad", "--atomDays" },
			description = "The number of days that the Atom feed should contain. Defaults to 60.",
			required = false
	)
	private int atomDays = 60;
	
	public File getInput() {
		return input;
	}

	public void setInput(File input) {
		this.input = input;
	}
	
	public File getOutputSitemap() {
		return outputSitemap;
	}

	public void setOutputSitemap(File outputSitemap) {
		this.outputSitemap = outputSitemap;
	}

	public File getAtomHeader() {
		return atomHeader;
	}

	public void setAtomHeader(File atomHeader) {
		this.atomHeader = atomHeader;
	}

	public URL getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(URL baseUrl) {
		this.baseUrl = baseUrl;
	}

	public URL getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(URL feedUrl) {
		this.feedUrl = feedUrl;
	}

	public File getOutputAtom() {
		return outputAtom;
	}

	public void setOutputAtom(File outputAtom) {
		this.outputAtom = outputAtom;
	}

	@Override
	public String toString() {
		return "ArgumentsCsv2Pillar4 [input=" + input + ", output=" + outputSitemap + ", baseUrl=" + baseUrl + ", feedUrl="
				+ feedUrl + ", atomHeader=" + atomHeader + "]";
	}
	
}
