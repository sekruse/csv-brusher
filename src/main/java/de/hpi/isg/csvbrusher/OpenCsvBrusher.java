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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import au.com.bytecode.opencsv.CSVReader;

public class OpenCsvBrusher extends AbstractBrusher {

	@Override
	public void brush(final File inputFile, final File outputFile) {

		try (CSVReader reader = createCSVReader(inputFile, this.inputFieldDelimiter,
				this.inputQuoteChar)) {

			try (Writer writer = new FileWriter(outputFile)) {

				String[] line;
				while ((line = reader.readNext()) != null) {
					String separator = "";
					for (String field : line) {

						// Process field.
						if (this.nullString != null
								&& field.equals(this.nullString)) {
							field = "";

						} else if (this.escapeDelimiters) {
							field = field.replace(this.outputFieldDelimiter, ' ')
									.replace('\n', ' ');
							if (this.outputQuoteChar != 0) {
								field = field.replace(this.outputQuoteChar, '-');
							}
						}

						// Write field.
						writer.write(separator);
						if (this.outputQuoteChar != 0) {
							writer.write(this.outputQuoteChar);
						}
						writer.write(field);
						if (this.outputQuoteChar != 0) {
							writer.write(this.outputQuoteChar);
						}
						separator = String.valueOf(this.outputFieldDelimiter);
					}
					writer.write('\n');
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static CSVReader createCSVReader(final File path, final char fieldDelimiter,
			final char quoteChar) throws FileNotFoundException {
		if (quoteChar == 0) {
			return new CSVReader(new FileReader(path), fieldDelimiter);
		} else {
			return new CSVReader(new FileReader(path), fieldDelimiter,
					quoteChar);
		}

	}

}
