package eu.europa.op.eli.pillar4.helper.atomheader;

import java.io.File;
import java.net.URL;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
		commandDescription = "Generates a base Atom header file"
)
public class ArgumentsAtomHeader {
	
	@Parameter(
			names = { "-o", "--output" },
			description = "Path to the output Atom header file",
			required = true
	)
	private File output;
	
	@Parameter(
			names = { "-t", "--title" },
			description = "Title to be inserted in the generated Atom header. If not provided, a placeholder value will be used.",
			required = false
	)
	private String title;
	
	@Parameter(
			names = { "-i", "--id" },
			description = "Atom ID to be inserted in Atom header. If not provided, a placeholder value will be used.",
			required = false
	)
	private String id;
	
	@Parameter(
			names = { "-l", "--link" },
			description = "Link URL (starting with 'http') to be inserted in Atom header. If not provided, a placeholder value will be used.",
			required = false
	)
	private URL link;
	
	@Parameter(
			names = { "-a", "--author" },
			description = "Author name to be inserted in Atom header. If not provided, a placeholder value will be used.",
			required = false
	)
	private String author;
	



	public File getOutput() {
		return output;
	}

	public void setOutput(File output) {
		this.output = output;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public URL getLink() {
		return link;
	}

	public void setLink(URL link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "ArgumentsAtomHeader [output=" + output + ", title=" + title + ", id=" + id + ", link=" + link
				+ ", author=" + author + "]";
	}	
	
}
