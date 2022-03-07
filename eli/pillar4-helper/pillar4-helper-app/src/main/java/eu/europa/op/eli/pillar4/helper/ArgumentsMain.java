package eu.europa.op.eli.pillar4.helper;

import com.beust.jcommander.Parameter;

public class ArgumentsMain {

	@Parameter(
			names = { "-h", "--help" },
			description = "Prints usage message",
			help = true
	)
	private boolean help = false;
	
	
	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}

}
