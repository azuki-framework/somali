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
package org.azkwf.somali.swing.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author Kawakicchi
 */
public class LogReadConfig {

    private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static final String KEY_CONDITION_FILE = "condition.file";

    private static final String KEY_CONDITION_DATE_FROM = "condition.date.from";

    private static final String KEY_CONDITION_DATE_TO = "condition.date.to";

    private static final String KEY_CONDITION_LEVEL = "condition.level";

    private static final String KEY_CONDITION_LOGGER = "condition.logger";

    private static final String KEY_CONDITION_MESSAGE = "condition.message";

    private String logFile;

    private Date dateFrom;

    private Date dateTo;

    private String level;

    private String logger;

    private String message;

    public static LogReadConfig load(final File file) throws IOException {
        LogReadConfig config = new LogReadConfig();
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

        config.setLogFile(p.getProperty(KEY_CONDITION_FILE, ""));
        config.setDateFrom(toDate(p.getProperty(KEY_CONDITION_DATE_FROM)));
        config.setDateTo(toDate(p.getProperty(KEY_CONDITION_DATE_TO)));
        config.setLevel(p.getProperty(KEY_CONDITION_LEVEL, ""));
        config.setLogger(p.getProperty(KEY_CONDITION_LOGGER, ""));
        config.setMessage(p.getProperty(KEY_CONDITION_MESSAGE, ""));

        return config;
    }

    public static void store(final File file,
        final LogReadConfig config) throws IOException {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            Properties p = new Properties();

            p.put(KEY_CONDITION_FILE, config.getLogFile());
            p.put(KEY_CONDITION_DATE_FROM, toString(config.getDateFrom()));
            p.put(KEY_CONDITION_DATE_TO, toString(config.getDateTo()));
            p.put(KEY_CONDITION_LEVEL, config.getLevel());
            p.put(KEY_CONDITION_LOGGER, config.getLogger());
            p.put(KEY_CONDITION_MESSAGE, config.getMessage());

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

    /**
     * @return the logFile
     */
    public String getLogFile() {
        return logFile;
    }

    /**
     * @param logFile the logFile to set
     */
    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * @return the logger
     */
    public String getLogger() {
        return logger;
    }

    /**
     * @param logger the logger to set
     */
    public void setLogger(String logger) {
        this.logger = logger;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public static String toString(final Date date) {
        String string = "";
        if (null != date) {
            string = FORMAT_DATE.format(date);
        }
        return string;
    }

    public static Date toDate(final String string) {
        Date date = null;
        if (null != string && 0 < string.length()) {
            try {
                date = FORMAT_DATE.parse(string);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return date;
    }
}
