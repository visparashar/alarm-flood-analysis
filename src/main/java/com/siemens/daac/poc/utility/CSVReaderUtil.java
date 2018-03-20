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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 */

/**
 * @author z003w4je
 *
 */
public class CSVReaderUtil {
	private static double count = 0;

	public static Map<String, String> processInputFile(String inputFilePath) {
		Set<String> inputSet = new HashSet<String>();
		Map<String, String> statusPercentageMap = new HashMap<String, String>();
		String floodStatus = new String();
		double percentageOfTrueFlood = 0.0;
		try {
			File inputF = new File(inputFilePath);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
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
			statusPercentageMap.put(CSVReaderConstant.PERCENTAGE_OF_TRUE_FLOOD, String.valueOf(percentageOfTrueFlood));
			addColumn(inputFilePath.replace(".csv", ""),floodStatus);
			count = 0;
		} catch (IOException e) {

		}
		return statusPercentageMap;
	}

	private static Function<String, String> mapToItem = (line) -> {
		String[] p = line.split(",");
		count++;
		return p[4] + p[5];
	};

	private static void addColumn(String fileName, String floodStatus) throws IOException {
		BufferedReader br = null;
		BufferedWriter bw = null;
		final String lineSep = System.getProperty(CSVReaderConstant.LINE_SEPARATOR);

		try {
			File file = new File(fileName + CSVReaderConstant.CSV_EXTENSION);
			File file2 = new File(fileName + CSVReaderConstant.FILE_SEP + CSVReaderConstant.CSV_EXTENSION);// so the
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2)));
			String line = null;
			int i = 0;
			for (line = br.readLine(); line != null; line = br.readLine(), i++) {
				if (i==0) {
					String addedColumn = CSVReaderConstant.COMMA + CSVReaderConstant.FLOOD_STATUS_CSV;
					bw.write(line + addedColumn + lineSep);
				} else {
				String addedColumn = String.valueOf(CSVReaderConstant.COMMA + floodStatus);
				bw.write(line + addedColumn + lineSep);
				}
			}

		} catch (Exception e) {
			
		} finally {
			if (br != null)
				br.close();
			if (bw != null)
				bw.close();
		}

	}
}
