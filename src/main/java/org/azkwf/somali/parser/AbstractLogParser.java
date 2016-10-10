/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkwf.somali.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import org.azkwf.somali.config.ParserConfig;
import org.azkwf.somali.record.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * このクラスは、ログ解析機能を実装するための基底クラスです。
 * 
 * @author Kawakicchi
 */
public abstract class AbstractLogParser implements LogParser {

	private Logger logger;

	private final LogParserEvent event;

	private final List<LogParserListener> listeners;

	private ParserConfig config;

	private SimpleDateFormat dateFormat;

	public AbstractLogParser() {
		logger = LoggerFactory.getLogger(LogParser.class);

		config = new ParserConfig();
		dateFormat = new SimpleDateFormat(config.getFormatDate());

		event = new LogParserEvent(this);
		listeners = new ArrayList<LogParserListener>();
	}

	@Override
	public synchronized final void addLogParserListener(final LogParserListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	protected abstract BufferedReader getReader() throws IOException;

	@Override
	public void parse() {
		logger.debug("parse start.");

		BufferedReader reader = null;
		try {
			synchronized (listeners) {
				for (LogParserListener l : listeners) {
					l.logParserStarted(event);
				}
			}

			reader = getReader();

			StringBuffer builder = null;
			String line = null;
			while (null != (line = reader.readLine())) {
				if (config.getPattern().matcher(line).matches()) {
					if (null != builder) {
						log(builder.toString());
					}
					builder = new StringBuffer(line);
				} else {
					if (null != builder) {
						builder.append("\n");
						builder.append(line);
					}
				}
			}
			if (null != builder) {
				log(builder.toString());
			}

		} catch (IOException ex) {
			logger.error("Log file io error.", ex);
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException ex) {
					logger.warn("File close error.", ex);
				}
			}

			synchronized (listeners) {
				for (LogParserListener l : listeners) {
					l.logParserFinished(event);
				}
			}
		}

		logger.debug("parse end.");
	}

	private void log(final String log) {
		BasicLogRecord record = new BasicLogRecord(log);

		Matcher matcher = config.getPattern().matcher(log);
		if (matcher.find()) {

			int index = config.getPatternIndexDate();
			if (0 < index) {
				try {
					record.date = dateFormat.parse(matcher.group(index));
				} catch (ParseException ex) {
					logger.error("Date parse error.", ex);
				}
			}

			index = config.getPatternIndexLevel();
			if (0 < index) {
				record.level = matcher.group(index);
			}

			index = config.getPatternIndexLogger();
			if (0 < index) {
				record.logger = matcher.group(index);
			}

			index = config.getPatternIndexMessage();
			if (0 < index) {
				record.message = matcher.group(index);
			}

			synchronized (listeners) {
				for (LogParserListener l : listeners) {
					l.logParserLogRecord(record, event);
				}
			}
		} else {
			logger.error("Format error log.{}", log);
		}
	}

	private class BasicLogRecord implements LogRecord {

		private Date date;
		private String level;
		private String logger;
		private String message;
		private String log;

		public BasicLogRecord(final String log) {
			this.log = log;
		}

		@Override
		public Date getDate() {
			return date;
		}

		@Override
		public String getLevel() {
			return level;
		}

		@Override
		public String getLogger() {
			return logger;
		}

		@Override
		public String getMessage() {
			return message;
		}

		public String toString() {
			return log;
		}
	}
}
