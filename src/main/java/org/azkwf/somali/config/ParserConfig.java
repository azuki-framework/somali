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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * このクラスは、ログパース設定を保持したクラスです。
 *
 * @author Kawakicchi
 */
public class ParserConfig {

    private static final String KEY_LOG_PATTERN = "log.pattern";

    private static final String KEY_LOG_PATTERN_GROUP_DATE = "log.pattern.group.date";

    private static final String KEY_LOG_PATTERN_GROUP_LEVEL = "log.pattern.group.level";

    private static final String KEY_LOG_PATTERN_GROUP_LOGGER = "log.pattern.group.logger";

    private static final String KEY_LOG_PATTERN_GROUP_MESSAGE = "log.pattern.group.message";

    private static final String KEY_LOG_DATE_FORMAT = "log.date.format";

    private Pattern pattern;

    private Integer patternIndexDate;

    private Integer patternIndexLevel;

    private Integer patternIndexLogger;

    private Integer patternIndexMessage;

    private String formatDate;

    public ParserConfig() {
        String date = "([0-9]{4}\\/[0-9]{2}\\/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}){1}";
        String level = "([^\\s]+)[\\s]+";
        String param = "\\{(.+)\\}";
        String logger = "([^\\s]+){1}";
        String message = "(.*){1}";

        String ptn = String.format("^%s %s%s - %s$", date, level, param, message);

        pattern = Pattern.compile(ptn, Pattern.MULTILINE | Pattern.DOTALL);
        patternIndexDate = 1;
        patternIndexLevel = 2;
        patternIndexLogger = 0;
        patternIndexMessage = 4;
        formatDate = "yyy/MM/dd HH:mm:ss.SSS";
    }

    public static ParserConfig load(final File file) throws IOException {
        ParserConfig config = new ParserConfig();
        Properties p = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            p.load(stream);
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        config.setPattern(Pattern.compile(p.getProperty(KEY_LOG_PATTERN), Pattern.MULTILINE | Pattern.DOTALL));
        config.setFormatDate(p.getProperty(KEY_LOG_DATE_FORMAT));
        config.setPatternIndexDate(toInteger(p.getProperty(KEY_LOG_PATTERN_GROUP_DATE)));
        config.setPatternIndexLevel(toInteger(p.getProperty(KEY_LOG_PATTERN_GROUP_LEVEL)));
        config.setPatternIndexLogger(toInteger(p.getProperty(KEY_LOG_PATTERN_GROUP_LOGGER)));
        config.setPatternIndexMessage(toInteger(p.getProperty(KEY_LOG_PATTERN_GROUP_MESSAGE)));

        return config;
    }

    public static void store(final File file,
        final ParserConfig config) throws IOException {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            Properties p = new Properties();

            p.put(KEY_LOG_PATTERN, config.getPattern().pattern());
            p.put(KEY_LOG_DATE_FORMAT, config.getFormatDate());
            p.put(KEY_LOG_PATTERN_GROUP_DATE, toString(config.getPatternIndexDate()));
            p.put(KEY_LOG_PATTERN_GROUP_LEVEL, toString(config.getPatternIndexLevel()));
            p.put(KEY_LOG_PATTERN_GROUP_LOGGER, toString(config.getPatternIndexLogger()));
            p.put(KEY_LOG_PATTERN_GROUP_MESSAGE, toString(config.getPatternIndexMessage()));

            p.store(stream, "");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
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
    public Integer getPatternIndexDate() {
        return patternIndexDate;
    }

    /**
     * @param patternIndexDate the patternIndexDate to set
     */
    public void setPatternIndexDate(Integer patternIndexDate) {
        this.patternIndexDate = patternIndexDate;
    }

    /**
     * @return the patternIndexLevel
     */
    public Integer getPatternIndexLevel() {
        return patternIndexLevel;
    }

    /**
     * @param patternIndexLevel the patternIndexLevel to set
     */
    public void setPatternIndexLevel(Integer patternIndexLevel) {
        this.patternIndexLevel = patternIndexLevel;
    }

    /**
     * @return the patternIndexLogger
     */
    public Integer getPatternIndexLogger() {
        return patternIndexLogger;
    }

    /**
     * @param patternIndexLogger the patternIndexLogger to set
     */
    public void setPatternIndexLogger(Integer patternIndexLogger) {
        this.patternIndexLogger = patternIndexLogger;
    }

    /**
     * @return the patternIndexMessage
     */
    public Integer getPatternIndexMessage() {
        return patternIndexMessage;
    }

    /**
     * @param patternIndexMessage the patternIndexMessage to set
     */
    public void setPatternIndexMessage(Integer patternIndexMessage) {
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

    private static String toString(final Integer value) {
        String string = "";
        if (null != value) {
            string = value.toString();
        }
        return string;
    }

    private static Integer toInteger(final String value) {
        Integer integer = null;
        if (null != value && 0 < value.length()) {
            integer = Integer.parseInt(value);
        }
        return integer;
    }

}
