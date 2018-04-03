package com.siemens.daac.poc.service;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import com.siemens.daac.poc.utility.CSVMergeUtil;
import com.siemens.daac.poc.utility.CSVReaderUtil;

@Service
public class CSVFileProcessorService implements FileProcessorService{


	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();

	@Override
	public boolean read(String filePath) throws IOException {
		//		Calling CSV Reader Util
		if(filePath!=null && !filePath.isEmpty()) {
			HashMap<String,String> fileNameWithStatusMap = (HashMap<String, String>) CSVReaderUtil.processInputFile(filePath);
//			TODO : There should be atleast one true and one false
			if(fileNameWithStatusMap.values().contains("1") && fileNameWithStatusMap.values().contains("0"))
			{
				return true;
			}else {
				if(logger.isDebugEnabled())
					logger.debug("There is no True Flood in the ZIP , So our training set is not efficient ,"
							+ "demanding more data");
				return false;
			}
		}
		logger.error("The FilePath is null or Empty");
		return false;
	}

	@Override
	public boolean write() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean merge(String filePath) throws IOException {
		// TODO Auto-generated method stub
//		Calling CSV Merge Util
		return CSVMergeUtil.merge(filePath);
	}

	@Override
	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}



}
