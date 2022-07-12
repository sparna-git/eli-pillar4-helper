package eu.europa.op.eli.pillar4.helper.atomheader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.op.eli.pillar4.helper.AtomHeaderGenerator;
import eu.europa.op.eli.pillar4.helper.CliCommandIfc;

public class AtomHeader implements CliCommandIfc {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public void execute(Object args) throws Exception {
		ArgumentsAtomHeader a = (ArgumentsAtomHeader)args;
		
		log.debug("Command run with following arguments : ");
		log.debug(a.toString());
		
		AtomHeaderGenerator generator = new AtomHeaderGenerator(
				a.getTitle(),
				a.getId(),
				a.getLink(),
				a.getAuthor()
		);
		generator.generateAtomHeader(a.getOutput());
	}

	
	
}
