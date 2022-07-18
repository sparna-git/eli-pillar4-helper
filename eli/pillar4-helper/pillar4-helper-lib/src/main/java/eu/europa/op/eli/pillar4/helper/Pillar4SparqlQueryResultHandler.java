package eu.europa.op.eli.pillar4.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.AbstractTupleQueryResultHandler;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResultHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pillar4SparqlQueryResultHandler extends AbstractTupleQueryResultHandler {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	public static String COLUMN_NAME_ELI = "eli";
	public static String COLUMN_NAME_UPDATE_DATE = "updateDate";
	public static String COLUMN_NAME_TITLE = "title";
	
	private List<Pillar4Entry> entries = new ArrayList<>();
	
	@Override
	public void handleSolution(BindingSet bindingSet) throws TupleQueryResultHandlerException {
		if(!bindingSet.hasBinding(COLUMN_NAME_ELI) || !bindingSet.hasBinding(COLUMN_NAME_UPDATE_DATE)) {
			throw new TupleQueryResultHandlerException("SPARQL query must return ?eli and ?updateDate columns");
		}
		
		if ((entries.size() % 1000) == 0) {
			log.debug("Reading SPARQL query result " + entries.size() + "...");
		}
		
		Pillar4Entry entry = new Pillar4Entry();
		
		if(bindingSet.getValue(COLUMN_NAME_ELI) instanceof IRI) {
			String theUri = ((IRI)bindingSet.getValue(COLUMN_NAME_ELI)).toString();
			entry.setUrl(theUri);
		} else {
			throw new TupleQueryResultHandlerException("First column must be a URI");
		}
		
		if(bindingSet.getValue(COLUMN_NAME_UPDATE_DATE) instanceof Literal) {
			String theUpdateDate = ((Literal)bindingSet.getValue(COLUMN_NAME_UPDATE_DATE)).stringValue();
			entry.setUpdateDate(theUpdateDate);
		} else {
			throw new TupleQueryResultHandlerException("Second column must be a date or dateTime Literal");
		}
		
		if(bindingSet.hasBinding(COLUMN_NAME_TITLE)) {
			if(bindingSet.getValue(COLUMN_NAME_TITLE) instanceof Literal) {
				String theTitle = ((Literal)bindingSet.getValue(COLUMN_NAME_TITLE)).stringValue();
				entry.setTitle(theTitle);
			} else {
				throw new TupleQueryResultHandlerException("third column must be a Literal");
			}
		}
		
		entries.add(entry);
	}

	public List<Pillar4Entry> getEntries() {
		return entries;
	}
	
}
