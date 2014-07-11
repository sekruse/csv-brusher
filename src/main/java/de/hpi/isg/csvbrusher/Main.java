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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.ParametersDelegate;

public class Main {

	public static void main(final String[] args) throws FileNotFoundException {
		final Parameters parameters = new Parameters();
		try {
			new JCommander(parameters, args);
		} catch (final ParameterException e) {
			System.out.println("Error: " + e.getMessage());
			new JCommander(parameters).usage();
			System.exit(1);
		}

		AbstractBrusher brusher = null;
		if ("super-csv".equals(parameters.csvLib)) {
			brusher = new SuperCsvBrusher();
		} else if ("open-csv".equals(parameters.csvLib)) {
			brusher = new OpenCsvBrusher();
		} else {
			throw new IllegalArgumentException("Unknown CSV library: " + parameters.csvLib);
		}
		brusher.configure(parameters.brusherConfig);

		final List<File> inputFiles = collectInputFiles(parameters.inputFiles);
		final File outputDir = ensureOutputDirectory(parameters.outputDir);

		for (final File inputFile : inputFiles) {
			final File outputFile = createOutputFile(outputDir, inputFile);
			System.out.format("Brushing: %s -> %s\n", inputFile, outputFile);
			brusher.brush(inputFile, outputFile);
		}

	}

	private static File ensureOutputDirectory(final String outputDirPath) {
		final File outputDir = new File(outputDirPath);
		if (outputDir.exists()) {
			if (!outputDir.isDirectory()) {
				throw new IllegalArgumentException(
						"Output directory is a file: " + outputDir);
			}
		} else {
			outputDir.mkdirs();
		}
		return outputDir;
	}

	private static List<File> collectInputFiles(final List<String> inputFiles) {
		final Set<File> files = new HashSet<>();
		for (final String inputFileName : inputFiles) {
			collectInputFilesAux(new File(inputFileName), files);
		}
		return new ArrayList<>(files);
	}

	private static void collectInputFilesAux(final File inputFile, final Set<File> files) {

		if (!inputFile.exists()) {
			System.err.println("No such file: " + inputFile);
		} else if (inputFile.isDirectory()) {
			for (final File child : inputFile.listFiles()) {
				collectInputFilesAux(child, files);
			}
		} else {
			files.add(inputFile);
		}

	}

	private static File createOutputFile(final File outputDir, final File inputFile) {
		return new File(outputDir,
				inputFile.getName());
	}

	public static class Parameters {

		@Parameter(description = "the input CSV files", required = true)
		public List<String> inputFiles;

		@Parameter(names = { "-o", "--output-dir" }, description = "directory for brushed files", required = true)
		public String outputDir;

		@Parameter(names = { "--csv-library" }, description = "the CSV library to use (*super-csv, open-csv",
				required = false)
		public String csvLib = "super-csv";

		@ParametersDelegate
		public final BrusherConfiguration brusherConfig = new BrusherConfiguration();

	}

}
