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
package org.azkwf.somali.util;

/**
 * @author Kawakicchi
 */
public class SomaliUtil {

    public static String msToString(final Long time) {
        String s = null;
        if (null != time) {
            if (time < 1000) {
                s = String.format("%d ms", time);
            } else if (time < 60 * 1000) {
                s = String.format("%d sec", time / 1000);
            } else {
                s = String.format("%d min", time / (1000 * 60));
            }
        }
        return s;
    }

    public static boolean isNotEmpty(final String string) {
        return (null != string && 0 < string.length());
    }
}
