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
			description = "Path to the input CSV file",
			required = true
	)
	private File input;
	
	@Parameter(
			names = { "-o", "--output" },
			description = "Path to the output directory",
			required = true
	)
	private File output;
	
	@Parameter(
			names = { "-u", "--baseurl" },
			description = "Base URL",
			required = true
	)
	private URL baseurl;
	
	@Parameter(
			names = { "-uset", "--baseurlset" },
			description = "Base URL",
			required = false
	)
	private URL baseurlset;
	
	@Parameter(
			names = { "-iAtom", "--inputAtom" },
			description = "Path to the input Atom Properties file",
			required = true
	)
	private File inputAtom;

	public File getInput() {
		return input;
	}

	public void setInput(File input) {
		this.input = input;
	}

	public File getOutput() {
		return output;
	}

	public void setOutput(File output) {
		this.output = output;
	}
	
	public File getInputAtom() {
		return inputAtom;
	}

	public void setInputAtom(File inputAtom) {
		this.inputAtom = inputAtom;
	}
	
	public URL getbaseurl() {
		return baseurl;
	}
	
	public void setbaseurl(URL baseurl) {
		this.baseurl = baseurl;
	}
	
	public URL getbaseurlset() {
		return baseurlset;
	}
	
	public void setbaseurlset(URL baseurlset) {
		this.baseurlset = baseurlset;
	}
	
	
	@Override
	public String toString() {
		return "ArgumentsCsv2SitemapAndAtom [input=" + input + ", output=" + output + ",baseurl="+ baseurl +",baseurlset=" + baseurlset + ", inputAtom= " + inputAtom + "]";
	}
	
	
}
