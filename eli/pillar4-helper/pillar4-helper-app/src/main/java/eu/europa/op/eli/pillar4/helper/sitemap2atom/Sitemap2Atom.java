package eu.europa.op.eli.pillar4.helper.sitemap2atom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.op.eli.pillar4.helper.CliCommandIfc;
import eu.europa.op.eli.pillar4.helper.Pillar4Helper;

public class Sitemap2Atom implements CliCommandIfc {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public void execute(Object args) throws Exception {
		ArgumentsSitemap2Atom a = (ArgumentsSitemap2Atom)args;
		
		log.debug("Command run with following arguments : ");
		log.debug(a.toString());
		
		Pillar4Helper pillar4Helper = new Pillar4Helper();
		pillar4Helper.sitemap2Atom(a.getInput(), a.getOutput(), a.getbaseurl(), a.getInputAtom());
	}

	
	
}
