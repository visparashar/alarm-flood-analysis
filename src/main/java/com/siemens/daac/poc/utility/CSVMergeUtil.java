package com.siemens.daac.poc.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.siemens.daac.poc.constant.ProjectConstants;

public class CSVMergeUtil {
	static final Logger logger  = Logger.getLogger(CSVMergeUtil.class);

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

	public static boolean merge(String filePath) throws IOException {
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();
		List<Path> paths=new ArrayList<Path>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isFile())
				paths.add(Paths.get(listOfFiles[i].toString()));
		}
		if(logger.isDebugEnabled())
			logger.debug("CSV Successfully Merged");
		List<String> mergedLines = getMergedLines(paths);
		String trainingSetLocation =filePath+"/"+ProjectConstants.TRAINING_SET_DIR_NAME+"/training_set.csv";
		Path target = Paths.get(trainingSetLocation);
		Files.write(target, mergedLines, Charset.forName("UTF-8"));
		return true;
	}

	private static List<String> getMergedLines(List<Path> paths) throws IOException {
		List<String> mergedLines = new ArrayList<> ();
		for (Path p : paths){
			List<String> lines = Files.readAllLines(p, Charset.forName("UTF-8"));
			if (!lines.isEmpty()) {
				if (mergedLines.isEmpty()) {
					mergedLines.add(lines.get(0)); //add header only once
				}
				mergedLines.addAll(lines.subList(1, lines.size()));
			}
		}
		return mergedLines;
	}
}