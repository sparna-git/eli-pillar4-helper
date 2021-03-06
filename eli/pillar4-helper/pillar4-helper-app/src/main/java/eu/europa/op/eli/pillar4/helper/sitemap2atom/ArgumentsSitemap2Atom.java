package eu.europa.op.eli.pillar4.helper.sitemap2atom;

import java.io.File;
import java.net.URL;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import eu.europa.op.eli.pillar4.helper.cli.FileExistsValidator;

@Parameters(
		commandDescription = "Generates a Pillar 4 atom feed from a Pillar 4 sitemap file."
)
public class ArgumentsSitemap2Atom {

	@Parameter(
			names = { "-i", "--sitemapInput" },
			description = "Path to the input Sitemap file, from which the most recent entries will be read to be inserted in the Atom feed.",
			required = true,
			validateWith = FileExistsValidator.class
	)
	private File sitemapInput;
	
	@Parameter(
			names = { "-su", "--sitemapBaseUrl" },
			description = "Base URL of every ELI listed in the sitemap file. All ELIs in the sitemap MUST begin by this base URL.",
			required = true
	)
	private URL sitemapBaseUrl;
	
	@Parameter(
			names = { "-ao", "--atomOutput" },
			description = "Path to the file where the output Atom feed will be written",
			required = true
	)
	private File atomOutput;
	
	@Parameter(
			names = { "-ah", "--atomHeader" },
			description = "Path to the input Atom 'skeleton' file containing the Atom header information; this skeleton can be generated by another command.",
			required = true
	)
	private File atomHeader;
	
	@Parameter(
			names = { "-ad", "--atomDays" },
			description = "The number of days to consider to include sitemap entries in the Atom feed. Entries updated since this number of days will be included. Defaults to 60.",
			required = false
	)
	private int atomDays = 60;
	
	
	public File getSitemapInput() {
		return sitemapInput;
	}

	public void setSitemapInput(File sitemapInput) {
		this.sitemapInput = sitemapInput;
	}
	
	public URL getSitemapBaseUrl() {
		return sitemapBaseUrl;
	}

	public void setSitemapBaseUrl(URL sitemapBaseUrl) {
		this.sitemapBaseUrl = sitemapBaseUrl;
	}

	public File getAtomOutput() {
		return atomOutput;
	}
	
	public void setAtomOutput(File atomOutput) {
		this.atomOutput = atomOutput;
	}

	public File getAtomHeader() {
		return atomHeader;
	}

	public void setAtomHeader(File atomHeader) {
		this.atomHeader = atomHeader;
	}

	public int getAtomDays() {
		return atomDays;
	}

	public void setAtomDays(int atomDays) {
		this.atomDays = atomDays;
	}

	@Override
	public String toString() {
		return "ArgumentsSitemap2Atom [sitemapInput=" + sitemapInput + ", sitemapBaseUrl=" + sitemapBaseUrl
				+ ", atomOutput=" + atomOutput + ", atomHeader=" + atomHeader + ", atomDays=" + atomDays + "]";
	}

	

	
}
