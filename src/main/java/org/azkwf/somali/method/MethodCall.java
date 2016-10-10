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

	public MethodCall() {
		arguments = new ArrayList<String>();
		methodCalls = new ArrayList<MethodCall>();
	}

	public MethodCall(final String name) {
		this.name = name;
		arguments = new ArrayList<String>();
		methodCalls = new ArrayList<MethodCall>();
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addArgument(final String argument) {
		arguments.add(argument);
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void addMethodCall(final MethodCall methodCall) {
		methodCalls.add(methodCall);
	}

	public List<MethodCall> getMethodCalls() {
		return methodCalls;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	public String toString() {
		return String.format("%s (%dms)", name, 12);
	}
}
