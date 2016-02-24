package org.azkwf.somali.record;

import java.util.Date;

public class LogRecord {

	private Date date;
	private String value;

	public LogRecord(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
