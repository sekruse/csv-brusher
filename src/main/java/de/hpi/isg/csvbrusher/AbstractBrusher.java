/***********************************************************************************************************************
 * Copyright (C) 2014 by Sebastian Kruse
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 **********************************************************************************************************************/
package de.hpi.isg.csvbrusher;

import java.io.File;

/**
 * Abstract base classes for classes that implement the functionality to read a CSV file and write a brushed version of
 * it.
 * 
 * @author Sebastian Kruse
 */
public abstract class AbstractBrusher {

	private static final String POSTGRES_NULL_STRING = "\\N";

	protected char inputFieldDelimiter;

	protected char outputFieldDelimiter;

	protected char inputQuoteChar;

	protected char outputQuoteChar;

	protected String nullString;

	protected boolean escapeDelimiters;

	protected boolean requireEqualLines;

	public void configure(final BrusherConfiguration brusherConfiguration) {

		this.inputFieldDelimiter = getFieldDelimiterChar(brusherConfiguration.fieldDelimiter);
		this.outputFieldDelimiter = getFieldDelimiterChar(brusherConfiguration.outputFieldDelimiter);
		this.outputFieldDelimiter = this.outputFieldDelimiter == 0 ? this.inputFieldDelimiter
				: this.outputFieldDelimiter;

		this.inputQuoteChar = getQuoteChar(brusherConfiguration.quoteChar);
		this.outputQuoteChar = getQuoteChar(brusherConfiguration.outputQuoteChar);
		if (this.outputQuoteChar == 0) {
			this.outputQuoteChar = this.inputQuoteChar;
		}

		this.nullString = brusherConfiguration.removeNullStrings ? POSTGRES_NULL_STRING : null;

		this.escapeDelimiters = brusherConfiguration.escapeDelimiters;

		this.requireEqualLines = brusherConfiguration.requireEqualLines;
	}

	private static char getQuoteChar(final String quoteCharName) {
		char quoteChar;
		if ("none".equalsIgnoreCase(quoteCharName)) {
			quoteChar = 0;
		} else if ("single".equalsIgnoreCase(quoteCharName)) {
			quoteChar = '\'';
		} else if ("double".equalsIgnoreCase(quoteCharName)) {
			quoteChar = '"';
		} else {
			throw new IllegalArgumentException("Unkown quote char.");
		}
		return quoteChar;
	}

	private static char getFieldDelimiterChar(final String fieldDelimiterName) {
		char fieldDelimiter;
		if (fieldDelimiterName == null) {
			return 0;
		}
		if ("comma".equalsIgnoreCase(fieldDelimiterName)) {
			fieldDelimiter = ',';
		} else if ("semicolon".equalsIgnoreCase(fieldDelimiterName)) {
			fieldDelimiter = ';';
		} else if ("tab".equalsIgnoreCase(fieldDelimiterName)) {
			fieldDelimiter = '\t';
		} else {
			throw new IllegalArgumentException("Unkown field delimiter.");
		}
		return fieldDelimiter;
	}

	abstract public void brush(File inputFile, File outputFile);

}
