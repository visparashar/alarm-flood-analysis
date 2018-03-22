package com.siemens.daac.poc.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
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
	private static String MERGED_FILE_PATH = readProperty(CSVReaderConstant.MERGED_FILE_PATH);
	private static String NOT_SURE_FILE_PATH = readProperty(CSVReaderConstant.NOT_SURE_FILE_PATH);
	private static int ALARM_TAG_INDEX = Integer.valueOf(readProperty(CSVReaderConstant.ALARM_TAG_INDEX));
	private static int ALARM_ID_INDEX = Integer.valueOf(readProperty(CSVReaderConstant.ALARM_ID_INDEX));
	private static String readProperty(String propName) {
		Properties prop = new Properties();
		InputStream input = null;
		String propValue = null;
		try {
			input = new FileInputStream("src/main/resources/application.properties");
			prop.load(input);
			propValue = prop.getProperty(propName);
		} catch (IOException ex) {

		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return propValue;
	}

	public static Map<String, String> processInputFile(String inputFilePath) throws IOException {
		Map<String, String> floodStatusMap = new HashMap<String, String>();
		Set<String> inputSet = new HashSet<String>();
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
				percentageOfTrueFlood = (inputSet.size() / count) * CSVReaderConstant.HUNDRED;
				if (percentageOfTrueFlood > CSVReaderConstant.SIXTY) {
					floodStatus = CSVReaderConstant.ONE_STRING;
				} else if (percentageOfTrueFlood < CSVReaderConstant.THIRTY) {
					floodStatus = CSVReaderConstant.ZERO_STRING;
				} else {
					floodStatus = CSVReaderConstant.NOT_SURE;
				}
				floodStatusMap.put(entry.getName(), floodStatus);
				addColumn(inputFilePath, floodStatus, entry.getName());
				count = CSVReaderConstant.ZERO;
			}
		} finally {
			if (br != null)
				br.close();
		}
		return floodStatusMap;
	}

	private static Function<String, String> mapToItem = (line) -> {
		String[] p = line.split(CSVReaderConstant.COMMA);
		count++;
		return p[ALARM_TAG_INDEX] + p[ALARM_ID_INDEX];
	};

	private static void addColumn(String zipFileName, String floodStatus, String fileName) throws IOException {
		BufferedReader br = null;
		BufferedWriter bw = null;
		String filePath = MERGED_FILE_PATH;
		final String lineSep = System.getProperty(CSVReaderConstant.LINE_SEPARATOR);

		try {
			ZipFile zipFile = new ZipFile(zipFileName);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				InputStream inputFS = zipFile.getInputStream(entry);
				if (fileName.equals(entry.getName())) {
					File file2 = null;
					if (floodStatus.equals(CSVReaderConstant.NOT_SURE)) {
						filePath = NOT_SURE_FILE_PATH;
					}
					File directory = new File(filePath);
					if (!directory.exists()) {
						directory.mkdir();
					}

					file2 = new File(filePath + fileName);
					br = new BufferedReader(new InputStreamReader(inputFS));
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2)));
					String line = null;
					int i = 0;
					for (line = br.readLine(); line != null; line = br.readLine(), i++) {
						if (i == 0) {
							String addedColumn = CSVReaderConstant.COMMA + CSVReaderConstant.FLOOD_STATUS_CSV;
							bw.write(line + addedColumn + lineSep);
						} else {
							String addedColumn = CSVReaderConstant.COMMA + floodStatus;
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
