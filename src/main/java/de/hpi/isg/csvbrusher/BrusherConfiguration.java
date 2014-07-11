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

import com.beust.jcommander.Parameter;

public class BrusherConfiguration {

	@Parameter(
			names = { "-f", "--field-delimiter" },
			description = "the delimiter of fields in each line of the output (and input) file (comma*, tab, semicolon)")
	public String fieldDelimiter = "comma";

	@Parameter(names = { "-of", "--output-field-delimiter" },
			description = "the optional delimiter of fields in each line of the input file (comma, tab, semicolon)",
			required = false)
	public String outputFieldDelimiter = "none";

	@Parameter(names = { "-q", "--quote-char" },
			description = "the quote of fields in each line of the input (and output) file (none*, single, double)")
	public String quoteChar = "none";

	@Parameter(names = { "-oq", "--output-quote-char" },
			description = "the optional quote of fields in each line of the output file (none, single, double)",
			required = false)
	public String outputQuoteChar;

	@Parameter(names = { "-e", "--escape-delimiters" }, description = "whether to replace delimiters in quoted fields")
	public boolean escapeDelimiters = false;

	@Parameter(names = { "--remove-null-strings" }, description = "remove PostgreSQL null strings (\\N)")
	public boolean removeNullStrings = false;

	@Parameter(names = { "-l", "--require-equal-lines" },
			description = "check if all lines have the same number of fields")
	public boolean requireEqualLines = false;
}