package eu.europa.op.eli.pillar4.helper.csv2pillar4;

import java.io.File;
import java.net.URL;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import eu.europa.op.eli.pillar4.helper.ArgumentsSomething2Pillar4;
import eu.europa.op.eli.pillar4.helper.cli.FileExistsValidator;

@Parameters(
		commandDescription = "Generates a Pillar 4 sitemap and Atom feed from a CSV file."
)
public class ArgumentsCsv2Pillar4 extends ArgumentsSomething2Pillar4 {

	@Parameter(
			names = { "-i", "--input" },
			description = "Path to the input CSV file. "
					+ "The CSV file MUST contain 2 columns : the ELI URI, and the update date of this ELI. "
					+ "The CSV file MUST have a first line containing the headers, that will be ignored at parsing. "
					+ "The date MUST use the format yyyy-MM-dd or yyyy-MM-dd'T'hh:mm:ss. "
					+ "An optional third column can contain a title to be included in the Atom feed. ",					
			required = true,
			validateWith = FileExistsValidator.class
	)
	private File input;

	
	public File getInput() {
		return input;
	}

	public void setInput(File input) {
		this.input = input;
	}

	@Override
	public String toString() {
		return "ArgumentsSparql2Pillar4 [input=" + input + ", outputSitemap=" + outputSitemap + ", baseUrl=" + baseUrl
				+ ", outputAtom=" + outputAtom + ", atomHeader=" + atomHeader + ", feedUrl=" + feedUrl + ", atomDays="
				+ atomDays + "]";
	}
	
}
