package eu.europa.op.eli.pillar4.helper;

import com.opencsv.bean.CsvBindByPosition;

public class Pillar4Entry {
	
	@CsvBindByPosition(position = 0)
	protected String url;

	@CsvBindByPosition(position = 1)
	protected String updateDate;
	
	@CsvBindByPosition(position = 2)
	protected String title;
	


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

}
