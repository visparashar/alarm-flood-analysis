package com.siemens.daac.poc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
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
public class CSVReader {
	private static double count = 0;

	private static String processInputFile(String inputFilePath) {
		Set<String> inputSet = new HashSet<String>();
		String floodStatus = new String();
		double percentageOfTrueFlood = 0;
		try {
			File inputF = new File(inputFilePath);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			inputSet = br.lines().skip(1).map(mapToItem1).collect(Collectors.toSet());
			percentageOfTrueFlood = (inputSet.size() / count) * 100;
			if (percentageOfTrueFlood > 60) {
				floodStatus = "true";
			} else if (percentageOfTrueFlood < 30) {
				floodStatus = "false";
			} else {
				floodStatus = "notSure";
			}
			count = 0;
			br.close();
		} catch (IOException e) {

		}
		return "floodStatus " + floodStatus + "percentageOfTrueFlood " + percentageOfTrueFlood;
	}

	private static Function<String, String> mapToItem1 = (line) -> {
		String[] p = line.split(",");
		count++;
		return p[4] + p[5];
	};
/*
	public static void main(String[] args) {
		processInputFile("ALARM_LIST_ALARM_FLOODS_2016-12-01.csv");
	}*/

}
