package eu.europa.op.eli.pillar4.helper.sparql2pillar4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.op.eli.pillar4.helper.CliCommandIfc;
import eu.europa.op.eli.pillar4.helper.Pillar4Helper;

public class Sparql2Pillar4 implements CliCommandIfc {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public void execute(Object args) throws Exception {
		
		ArgumentsSparql2Pillar4 a = (ArgumentsSparql2Pillar4)args;
		
		log.debug("Command run with following arguments : ");
		log.debug(a.toString());
		
		Pillar4Helper pillar4Helper = new Pillar4Helper();
		pillar4Helper.entries2Pillar4(
				pillar4Helper.executeSparql(a.getQuery(), a.getEndpoint()),
				a.getOutputSitemap(),
				a.getOutputAtom(),
				a.getBaseUrl(),
				a.getFeedUrl(),
				a.getAtomHeader()
		);
	}

	
	
}
