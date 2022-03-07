package eu.europa.op.eli.pillar4.helper;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.ParameterException;

import eu.europa.op.eli.pillar4.helper.csv2pillar4.ArgumentsCsv2Pillar4;
import eu.europa.op.eli.pillar4.helper.csv2pillar4.Csv2Pillar4;
import eu.europa.op.eli.pillar4.helper.sitemap2atom.ArgumentsSitemap2Atom;
import eu.europa.op.eli.pillar4.helper.sitemap2atom.Sitemap2Atom;


public class Main {

	enum COMMAND {		

		SITEMAP2ATOM(new ArgumentsSitemap2Atom(), new Sitemap2Atom()),
		CSV2PILLAR4(new ArgumentsCsv2Pillar4(), new Csv2Pillar4())
		;

		private CliCommandIfc command;
		private Object arguments;

		private COMMAND(Object arguments, CliCommandIfc command) {
			this.command = command;
			this.arguments = arguments;
		}

		public CliCommandIfc getCommand() {
			return command;
		}

		public Object getArguments() {
			return arguments;
		}		
	}
	
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.run(args);
	}

	public void run(String[] args) throws Exception {
		ArgumentsMain main = new ArgumentsMain();
		JCommander jc = new JCommander(main);
		for (COMMAND aCOMMAND : COMMAND.values()) {
			jc.addCommand(aCOMMAND.name().toLowerCase(), aCOMMAND.getArguments());
		}
		
		try {
			jc.parse(args);
		// a mettre avant ParameterException car c'est une sous-exception
		} catch (MissingCommandException e) {
			// if no command was found, exit with usage message and error code
			System.err.println("Unknown command.");
			jc.usage();
			System.exit(-1);
		} catch (ParameterException e) {
			System.err.println(e.getMessage());
			jc.usage(jc.getParsedCommand());
			System.exit(-1);
		} 
		
		// if help was requested, print it and exit with a normal code
		if(main.isHelp()) {
			jc.usage();
			System.exit(0);
		}
		
		// if no command was found (0 parameters passed in command line)
		// exit with usage message and error code
		if(jc.getParsedCommand() == null) {
			System.err.println("no command found.");
			jc.usage();
			System.exit(-1);
		}
		// executes the command with the associated arguments
		COMMAND.valueOf(jc.getParsedCommand().toUpperCase()).getCommand().execute(
				COMMAND.valueOf(jc.getParsedCommand().toUpperCase()).getArguments()
		);
		
	}


}
