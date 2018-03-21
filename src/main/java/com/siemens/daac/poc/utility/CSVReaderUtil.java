package com.siemens.daac.poc.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 */

/**
 * @author z003w4je
 *
 */
public class CSVReaderUtil {
	private static double count = 0;

	public static Map<String, Map<String,String>> processInputFile(String inputFilePath) throws IOException {
		Set<String> inputSet = new HashSet<String>();
		Map<String, Map<String,String>> statusPercentageMapOfZip =  new HashMap<String, Map<String,String>>();;
		Map<String, String> statusPercentageMap = new HashMap<String, String>();
		String floodStatus = new String();
		double percentageOfTrueFlood = 0.0;
		BufferedReader br = null;
		try {
			ZipFile zipFile = new ZipFile(inputFilePath);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				InputStream inputFS = zipFile.getInputStream(entry);
				br = new BufferedReader(new InputStreamReader(inputFS));
				inputSet = br.lines().skip(1).map(mapToItem).collect(Collectors.toSet());
				percentageOfTrueFlood = (inputSet.size() / count) * 100;
				if (percentageOfTrueFlood > 60) {
					floodStatus = CSVReaderConstant.TRUE;
				} else if (percentageOfTrueFlood < 30) {
					floodStatus = CSVReaderConstant.FALSE;
				} else {
					floodStatus = CSVReaderConstant.NOT_SURE;
				}
				statusPercentageMap.put(CSVReaderConstant.FLOOD_STATUS, floodStatus);
				statusPercentageMap.put(CSVReaderConstant.PERCENTAGE_OF_TRUE_FLOOD,
						String.valueOf(percentageOfTrueFlood));
				statusPercentageMapOfZip.put(entry.getName(), statusPercentageMap);
				addColumn(inputFilePath, floodStatus, entry.getName());
				count = 0;
			}
		} finally {
			if (br != null)
				br.close();
		}
		return statusPercentageMapOfZip;
	}

	private static Function<String, String> mapToItem = (line) -> {
		String[] p = line.split(",");
		count++;
		return p[4] + p[5];
	};

	private static void addColumn(String zipFileName, String floodStatus, String fileName) throws IOException {
		BufferedReader br = null;
		BufferedWriter bw = null;
		final String lineSep = System.getProperty(CSVReaderConstant.LINE_SEPARATOR);

		try {
			ZipFile zipFile = new ZipFile(zipFileName);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				InputStream inputFS = zipFile.getInputStream(entry);
				if (fileName.equals(entry.getName())) {

					File file2 = new File(
							fileName.replace(CSVReaderConstant.CSV_EXTENSION, CSVReaderConstant.EMPTY_STRING)
									+ CSVReaderConstant.FILE_SEP + CSVReaderConstant.CSV_EXTENSION);
					br = new BufferedReader(new InputStreamReader(inputFS));
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2)));
					String line = null;
					int i = 0;
					for (line = br.readLine(); line != null; line = br.readLine(), i++) {
						if (i == 0) {
							String addedColumn = CSVReaderConstant.COMMA + CSVReaderConstant.FLOOD_STATUS_CSV;
							bw.write(line + addedColumn + lineSep);
						} else {
							String addedColumn = String.valueOf(CSVReaderConstant.COMMA + floodStatus);
							bw.write(line + addedColumn + lineSep);
						}
					}

				}
			}
		} finally {
			if (br != null)
				br.close();
			if (bw != null)
				bw.close();
		}

	}

	public static void main(String[] args) throws IOException {
		processInputFile("ALARM_LIST_ALARM_FLOODS_2016-12-01.zip");
	}
}
