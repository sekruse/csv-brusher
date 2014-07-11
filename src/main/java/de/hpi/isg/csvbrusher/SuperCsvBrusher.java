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
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class SuperCsvBrusher extends AbstractBrusher {

	@Override
	public void configure(final BrusherConfiguration brusherConfiguration) {

		super.configure(brusherConfiguration);

		this.inputQuoteChar = ascend(this.inputQuoteChar, (char) 0);
	}

	private char ascend(final char currentValue, final char fallbackValue) {
		return currentValue == 0 ? fallbackValue : currentValue;
	}

	@Override
	public void brush(final File inputFile, final File outputFile) {

		try (CsvListReader reader = createCSVReader(inputFile,
				this.inputFieldDelimiter, this.inputQuoteChar)) {

			try (Writer writer = new FileWriter(outputFile)) {

				int lineLength = -1;
				List<String> line;
				while ((line = reader.read()) != null) {
					// Set or check line length.
					if (lineLength < 0) {
						lineLength = line.size();
					} else if (lineLength != line.size()) {
						throw new RuntimeException(String.format(
								"Line has %d fields, expected %d: %s",
								line.size(), lineLength, line));
					}

					String separator = "";
					for (String field : line) {

						// Process field.
						if (field == null) {
							field = "";

						} else if (this.nullString != null
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

	private CsvListReader createCSVReader(final File path, final char fieldDelimiter,
			final char quoteChar) throws FileNotFoundException {

		final FileReader fileReader = new FileReader(path);
		final CsvPreference csvPreference = new CsvPreference.Builder(
				this.inputQuoteChar, this.inputFieldDelimiter, "\n").build();
		return new CsvListReader(fileReader, csvPreference);

	}

}
