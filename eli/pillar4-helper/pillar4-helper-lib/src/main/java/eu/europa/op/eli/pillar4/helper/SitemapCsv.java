package eu.europa.op.eli.pillar4.helper;

import com.opencsv.bean.CsvBindByPosition;

public class SitemapCsv {
	
	@CsvBindByPosition(position = 0)
	protected String Loc;

	@CsvBindByPosition(position = 1)
	protected String LastDate;
	
	protected String BaseURL;

	public String getBaseURL() {
		return BaseURL;
	}

	public void setBaseURL(String baseURL) {
		BaseURL = baseURL;
	}

	public String getLoc() {
		return Loc;
	}

	public void setLoc(String loc) {
		Loc = loc;
	}

	public String getLastDate() {
		return LastDate;
	}

	public void setLastDate(String lastDate) {
		LastDate = lastDate;
	}
	
	
	

}
