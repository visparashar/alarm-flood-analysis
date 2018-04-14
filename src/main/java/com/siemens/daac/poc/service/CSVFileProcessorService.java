package com.siemens.daac.poc.service;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.utility.CSVMergeUtil;
import com.siemens.daac.poc.utility.CSVReaderUtil;

@Service
public class CSVFileProcessorService implements FileProcessorService{


	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();

	@Override
	public boolean read(String filePath ,boolean forTraining) throws IOException {
		//		Calling CSV Reader Util
		if(filePath!=null && !filePath.isEmpty()) {
			HashMap<String,String> fileNameWithStatusMap = (HashMap<String, String>) CSVReaderUtil.processInputFile(filePath,forTraining);
			if(!forTraining) {
				if(fileNameWithStatusMap.values().contains("notSure")) {
//					TODO need to write logic for if all the flood are true and for if there is no notSure file
					ProjectConstants.isRequiredToRunPrediction=true;
					return true;
				}
				else if(fileNameWithStatusMap.values().contains("1")) {
					ProjectConstants.isRequiredToRunPrediction=false;
					return true;
				}else {
					return false;
				}
			}
//			TODO : There should be atleast one true and one false
			if(fileNameWithStatusMap.values().contains("notSure")){
				if(fileNameWithStatusMap.values().contains("1") && fileNameWithStatusMap.values().contains("0")|| fileNameWithStatusMap.values().contains("1"))
				{
					logger.info("There is either only true floods or atleast one true and one false in the training set");
					ProjectConstants.isRequiredToRunPrediction =true;
					return true;
				}else{
					if(logger.isDebugEnabled())
						logger.debug("There is no True Flood in the ZIP , So our training set is not efficient ,"
								+ "demanding more data");
					return false;
				}
			}
			else {
				logger.info("There is no test data in floor zip for predictions sending true");
				return true;
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
