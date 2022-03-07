package eu.europa.op.eli.pillar4.helper.cli;

import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.converters.IParameterSplitter;

public class SpaceSplitter implements IParameterSplitter {

	@Override
	public List<String> split(String param) {
		return Arrays.asList(param.split(" "));
	}

}
