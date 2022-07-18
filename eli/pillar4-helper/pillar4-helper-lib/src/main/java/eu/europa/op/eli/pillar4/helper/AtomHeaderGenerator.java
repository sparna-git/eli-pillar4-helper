package eu.europa.op.eli.pillar4.helper;

import java.io.File;
import java.net.URL;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.atom.Person;
import com.rometools.rome.io.WireFeedOutput;

public class AtomHeaderGenerator {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	public static String DEFAULT_TITLE = "**Put title here**";
	public static String DEFAULT_ID = "**Put Atom ID here**";
	public static String DEFAULT_LINK = "http://**Put link here**";
	public static String DEFAULT_AUTHOR = "**Put author name here**";
	
	protected String title;
	protected String id;
	protected URL link;
	protected String author;
	
	public AtomHeaderGenerator() {
		super();
	}

	public AtomHeaderGenerator(String title, String id, URL link, String author) {
		super();
		this.title = title;
		this.id = id;
		this.link = link;
		this.author = author;
	}


	public void generateAtomHeader(File output) throws Pillar4Exception {
		Feed feed = new Feed();
		
		feed.setTitle((title == null || title.isEmpty())?DEFAULT_TITLE:title);
		feed.setId((id == null || id.isEmpty())?DEFAULT_ID:id);
		feed.setFeedType("atom_1.0");
		
		// link
		Link linkAtom = new Link();
		linkAtom.setRel("self");
		linkAtom.setType("application/atom+xml");
		linkAtom.setHref((link == null)?DEFAULT_LINK:link.toString());
		feed.setOtherLinks(Collections.singletonList(linkAtom));
		
		// author
		Person authors = new Person();
		authors.setName((author == null || author.isEmpty())?DEFAULT_AUTHOR:author);
		
		feed.setAuthors(Collections.singletonList(authors));
		
		// write 
		log.debug("Writing ATOM header "+output.getAbsolutePath());
		try {
			if (!output.exists()) {
				output.createNewFile();
			}
			WireFeedOutput wfo = new WireFeedOutput();
			wfo.output(feed, output);
		} catch (Exception e) {
			throw new Pillar4Exception(e);
		}
		
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
	
}
