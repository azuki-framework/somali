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
package org.azkwf.somali.config;

import java.util.regex.Pattern;

/**
 * このクラスは、ログパース設定を保持したクラスです。
 * 
 * @author Kawakicchi
 */
public class ParserConfig {

	private Pattern pattern;
	private String formatDate;
	private int patternIndexDate;
	private int patternIndexLevel;
	private int patternIndexLogger;
	private int patternIndexMessage;

	public ParserConfig() {
		String date = "([0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}){1}";
		String level = "\\[([^\\]\\s]+)[\\s]*\\]";
		String logger = "([^\\s]+){1}";
		String message = "(.*){1}";

		String ptn = String.format("^%s %s %s - %s$", date, level, logger, message);

		pattern = Pattern.compile(ptn, Pattern.MULTILINE | Pattern.DOTALL);
		patternIndexDate = 1;
		patternIndexLevel = 2;
		patternIndexLogger = 3;
		patternIndexMessage = 4;
		formatDate = "HH:mm:ss.SSS";
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(final Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the patternIndexDate
	 */
	public int getPatternIndexDate() {
		return patternIndexDate;
	}

	/**
	 * @param patternIndexDate the patternIndexDate to set
	 */
	public void setPatternIndexDate(int patternIndexDate) {
		this.patternIndexDate = patternIndexDate;
	}

	/**
	 * @return the patternIndexLevel
	 */
	public int getPatternIndexLevel() {
		return patternIndexLevel;
	}

	/**
	 * @param patternIndexLevel the patternIndexLevel to set
	 */
	public void setPatternIndexLevel(int patternIndexLevel) {
		this.patternIndexLevel = patternIndexLevel;
	}

	/**
	 * @return the patternIndexLogger
	 */
	public int getPatternIndexLogger() {
		return patternIndexLogger;
	}

	/**
	 * @param patternIndexLogger the patternIndexLogger to set
	 */
	public void setPatternIndexLogger(int patternIndexLogger) {
		this.patternIndexLogger = patternIndexLogger;
	}

	/**
	 * @return the patternIndexMessage
	 */
	public int getPatternIndexMessage() {
		return patternIndexMessage;
	}

	/**
	 * @param patternIndexMessage the patternIndexMessage to set
	 */
	public void setPatternIndexMessage(int patternIndexMessage) {
		this.patternIndexMessage = patternIndexMessage;
	}

	/**
	 * @return the formatDate
	 */
	public String getFormatDate() {
		return formatDate;
	}

	/**
	 * @param formatDate the formatDate to set
	 */
	public void setFormatDate(String formatDate) {
		this.formatDate = formatDate;
	}
}
