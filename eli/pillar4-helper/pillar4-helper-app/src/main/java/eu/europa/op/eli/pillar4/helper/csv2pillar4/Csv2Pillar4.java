package eu.europa.op.eli.pillar4.helper.csv2pillar4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.op.eli.pillar4.helper.CliCommandIfc;
import eu.europa.op.eli.pillar4.helper.Pillar4Helper;

public class Csv2Pillar4 implements CliCommandIfc {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public void execute(Object args){
		
		ArgumentsCsv2Pillar4 a = (ArgumentsCsv2Pillar4)args;
		
		log.debug("Command run with following arguments : ");
		log.debug(a.toString());
		
		Pillar4Helper pillar4Helper = new Pillar4Helper();
		pillar4Helper.setNumberOfDaysInRange(a.getAtomDays());
		pillar4Helper.entries2Pillar4(
				pillar4Helper.parseCsv(a.getInput()),
				a.getOutputSitemap(),
				a.getOutputAtom(),
				a.getBaseUrl(),
				a.getFeedUrl(),
				a.getAtomHeader()
		);
	}

	
	
}
