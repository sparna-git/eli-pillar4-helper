package eu.europa.op.eli.pillar4.helper.sitemap2atom;

import java.io.File;
import java.net.URL;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
		commandDescription = "Generates a Pillar 4 atom feed from a Pillar 4 sitemap file."
)
public class ArgumentsSitemap2Atom {

	@Parameter(
			names = { "-i", "--input" },
			description = "Path to the input Sitemap file",
			required = true
	)
	private File input;
	
	@Parameter(
			names = { "-o", "--output" },
			description = "Path to the output Atom feed",
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
	
	@Override
	public String toString() {
		return "ArgumentsSitemap2Atom [input=" + input + ", output=" + output + ",baseurl=" + baseurl + ", inputAtom= " + inputAtom + "]";
	}
	
	
}
