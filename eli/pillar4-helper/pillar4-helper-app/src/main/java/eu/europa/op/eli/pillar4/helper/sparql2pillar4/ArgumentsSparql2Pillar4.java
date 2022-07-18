package eu.europa.op.eli.pillar4.helper.sparql2pillar4;

import java.io.File;
import java.net.URL;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import eu.europa.op.eli.pillar4.helper.ArgumentsSomething2Pillar4;
import eu.europa.op.eli.pillar4.helper.cli.FileExistsValidator;

@Parameters(
		commandDescription = "Generates a Pillar 4 sitemap and Atom feed from a SPARQL query returning ?eli, ?updateDate and optionaly ?title."
)
public class ArgumentsSparql2Pillar4 extends ArgumentsSomething2Pillar4 {

	@Parameter(
			names = { "-q", "--query" },
			description = "Path to the file containing a SPARQL query to execute against the specified SPARQL endpoint. "
					+ "The file must be a valid SPARQL query, with an extension typically ending in '.rq'. "
					+ "The query MUST return 2 columns with the following name : ?eli must contain the IRI of a LegalResource, and ?updateDate contains the update date of the resource in xsd:date or xsd:dateTime datatype. "
					+ "An optional column '?title' can contain a title to be included in the Atom feed. ",
			required = true,
			validateWith = FileExistsValidator.class
	)
	private File query;

	@Parameter(
			names = { "-e", "--endpoint" },
			description = "URL of the SPARQL endpoint to execute the query. ",
			required = true
	)
	private String endpoint;

	public File getQuery() {
		return query;
	}

	public void setQuery(File query) {
		this.query = query;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String toString() {
		return "ArgumentsSparql2Pillar4 [query=" + query + ", endpoint=" + endpoint + ", outputSitemap=" + outputSitemap
				+ ", baseUrl=" + baseUrl + ", outputAtom=" + outputAtom + ", atomHeader=" + atomHeader + ", feedUrl="
				+ feedUrl + ", atomDays=" + atomDays + "]";
	}
	
}
