package org.azkwf.somali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

public class LogParser {

	private List<LogParserListener> listeners;

	public static void main(final String[] args) {
		LogParser parser = new LogParser();
		
		parser.parse(new File(args[0]));
	}

	public LogParser() {
		listeners = new ArrayList<LogParserListener>();
	}

	public void addLogParserListener(final LogParserListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	private static final Pattern PTN_START = Pattern.compile("^\\[[0-9]{2}:[0-9]{2}:[0-9]{2} lib21\\] .*");

	public void parse(final File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Windows-31J"));
			String line;
			long cntLine = 0;
			StringBuilder buffer = null;
			while (null != (line = reader.readLine())) {
				if (isNewLine(line)) {
					if (null == buffer) {
						buffer = new StringBuilder();
						buffer.append(line);
					} else {
						cntLine ++;
						doParseLine(cntLine, buffer.toString());

						buffer = new StringBuilder();
						buffer.append(line);
					}
				} else {
					if (null == buffer) {
						// ゴミ捨てる
					} else {
						buffer.append("\r\n");
						buffer.append(line);
					}
				}
			}
			if (null != buffer) {
				cntLine ++;
				doParseLine(cntLine, buffer.toString());
			}
			System.out.println(cntLine);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (null != reader) {
				try {
				reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	protected boolean isNewLine(final String line) {
		return PTN_START.matcher(line).matches();
	}

	protected void doParseLine(final long no, final String line) {
		System.out.println(line);
	}
}
