package com.siemens.daac.poc.service;

import java.io.IOException;

public interface FileProcessorService {
	
	
	public boolean read(String filePath,boolean forTraining) throws IOException;
	
	public boolean write();
	
	public boolean merge(String filePath) throws Exception;
	
	public boolean delete();
	
}
