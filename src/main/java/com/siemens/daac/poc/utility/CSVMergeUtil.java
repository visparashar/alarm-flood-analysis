package com.siemens.daac.poc.utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.siemens.daac.poc.constant.ProjectConstants;

public class CSVMergeUtil {
	static final Logger logger  = Logger.getLogger(CSVMergeUtil.class);

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
		String trainingSetLocation =filePath+ProjectConstants.TRAINING_SET_DIR_NAME;
		File dir = new File(trainingSetLocation);
		if(!dir.exists())
			dir.mkdirs();
		Path target = Paths.get(trainingSetLocation+"/"+ProjectConstants.TRAINING_SET_CSV_NAME);
		Path temp =Files.write(target, mergedLines, Charset.forName("UTF-8"));
//	move file from one location to archievelocation	
		if(temp!=null) {
			String toLocation =CommonUtils.readProperty("archived-file-path");
			
			if(moveFileToDestination(filePath, toLocation))
				return true;
			
		}
		return false;
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
	
//	move the input file to archieved folder
	public static boolean moveFileToDestination(String fromLocation , String toLocation) throws IOException {
		File fromLocationDir = new File(fromLocation);
		if(fromLocationDir.isDirectory())
		{
			File toLocationDir = new File(toLocation);
			if(!toLocationDir.exists())
				toLocationDir.mkdirs();
			File[] listOfFiles = fromLocationDir.listFiles();
			List<Path> paths=new ArrayList<Path>();
			for (int i = 0; i < listOfFiles.length; i++) {
				System.out.println(listOfFiles[i].getName());
				if(listOfFiles[i].isFile()) {
					System.out.println(toLocation+listOfFiles[i].getName());
					Files.move(Paths.get(listOfFiles[i].toString()),Paths.get(toLocation).resolve(listOfFiles[i].getName()),StandardCopyOption.REPLACE_EXISTING);
				}
			}
			return true;
		}
		return false;
	}
}