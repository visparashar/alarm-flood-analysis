package com.siemens.daac.poc.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;

import com.siemens.daac.poc.constant.ProjectConstants;

public class CommonUtils {
	
	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();
	
	public static String getRWorkspacePath(){
		
		String defaultPath = System.getProperty("user.dir");
		defaultPath+=File.separator+ProjectConstants.R_WORKSPACE_FOLDER_NAME;
		if(logger.isDebugEnabled())
			logger.debug("The RWorkspace Path is "+defaultPath);
		return defaultPath;
	}
	
	public static String readProperty(String propName) {
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
	
	
	

}
