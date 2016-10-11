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
package org.azkwf.somali.method;

import java.util.ArrayList;
import java.util.List;

import org.azkwf.somali.util.SomaliUtil;

/**
 * このクラスは、メソッド呼び出し情報を保持したクラスです。
 *
 * @author Kawakicchi
 */
public class MethodCall {

    private String name;

    private final List<String> arguments;

    private final List<MethodCall> methodCalls;

    private String result;

    private String exception;

    private Long time;

    private int indexStart;

    private int indexEnd;

    public MethodCall() {
        arguments = new ArrayList<String>();
        methodCalls = new ArrayList<MethodCall>();
    }

    public MethodCall(final String name) {
        this.name = name;
        arguments = new ArrayList<String>();
        methodCalls = new ArrayList<MethodCall>();
    }

    /**
     * nameを設定します。
     * 
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * nameを取得します。
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    public void addArgument(final String argument) {
        arguments.add(argument);
    }

    /**
     * argumentsを取得します。
     * 
     * @return arguments
     */
    public List<String> getArguments() {
        return arguments;
    }

    public void addMethodCall(final MethodCall methodCall) {
        methodCalls.add(methodCall);
    }

    /**
     * methodCallsを取得します。
     * 
     * @return methodCalls
     */
    public List<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    /**
     * resultを取得します。
     * 
     * @return result
     */
    public String getResult() {
        return result;
    }

    /**
     * resultを設定します。
     * 
     * @param result result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * exceptionを取得します。
     * 
     * @return exception
     */
    public String getException() {
        return exception;
    }

    /**
     * exceptionを設定します。
     * 
     * @param exception exception
     */
    public void setException(String exception) {
        this.exception = exception;
    }

    /**
     * timeを取得します。
     * 
     * @return time
     */
    public Long getTime() {
        return time;
    }

    /**
     * timeを設定します。
     * 
     * @param time time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    public int getIndexStart() {
        return indexStart;
    }

    public void setIndexStart(int indexStart) {
        this.indexStart = indexStart;
    }

    public int getIndexEnd() {
        return indexEnd;
    }

    public void setIndexEnd(int indexEnd) {
        this.indexEnd = indexEnd;
    }

    public String toString() {
        return String.format("%s (%s)", name, SomaliUtil.msToString(time));
    }

}
