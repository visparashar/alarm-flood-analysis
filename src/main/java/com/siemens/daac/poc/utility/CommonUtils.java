package com.siemens.daac.poc.utility;

import java.io.File;

import org.apache.log4j.Logger;

import com.siemens.daac.poc.constant.ProjectConstants;

public class CommonUtils {
	
	final static Logger logger = Logger.getLogger(CommonUtils.class);
	
	public static String getRWorkspacePath(){
		
		String defaultPath = System.getProperty("user.dir");
		defaultPath+=File.separator+ProjectConstants.R_WORKSPACE_FOLDER_NAME;
		if(logger.isDebugEnabled())
			logger.debug("The RWorkspace Path is "+defaultPath);
		return defaultPath;
	}
	
	
	
	
	

}
